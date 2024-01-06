package dream.collections

import dream.item.*
import dream.misc.*
import dream.nbt.*
import dream.nbt.types.*
import dream.utils.*

/**
 * Represents a list of ItemStacks for storing multiple items in inventory.
 */
@Open
class ItemList(var items: Array<ItemStack>) : Iterable<ItemStack>, ListStorable<CompoundTag> {
  
  /**
   * Constructs an ItemList with the specified size.
   *
   * @param size The size of the ItemList.
   */
  constructor(size: Int) : this(Array(size) { EmptyItemStack })
  
  /**
   * Returns whether the ItemList is empty.
   */
  val isEmpty get() = items.any { !it.isAir }
  
  /**
   * Returns the size of the ItemList.
   */
  val size get() = items.size
  
  /**
   * The maximum stack size for each ItemStack in the ItemList.
   */
  var maxStack = 64
  
  /**
   * Rearranges the size of the ItemList to the specified new size.
   *
   * @param newSize The new size of the ItemList.
   */
  fun rearrangeSize(newSize: Int) {
    if (newSize != size) {
      items = items.copyOf(newSize) { EmptyItemStack }
    }
  }
  
  /**
   * Returns the ItemStack at the specified index.
   *
   * @param index The index of the ItemStack.
   * @return The ItemStack at the specified index, or an empty ItemStack if the index is out of bounds.
   */
  fun getItem(index: Int): ItemStack = items.getOrElse(index) { EmptyItemStack }
  
  /**
   * Returns the ItemStack at the specified index.
   *
   * @param index The index of the ItemStack.
   * @return The ItemStack at the specified index, or an empty ItemStack if the index is out of bounds.
   */
  operator fun get(index: Int) = getItem(index)
  
  /**
   * Returns the ItemStack at the specified index, or null if the index is out of bounds.
   *
   * @param index The index of the ItemStack.
   * @return The ItemStack at the specified index, or null if the index is out of bounds.
   */
  fun getItemOrNull(index: Int): ItemStack? {
    return items.getOrNull(index)?.orNull()
  }
  
  /**
   * Removes the ItemStack at the specified index.
   *
   * @param index The index of the ItemStack to remove.
   * @param whenDone The optional runnable to be executed after removing the ItemStack.
   * @return The removed ItemStack.
   */
  fun remove(index: Int, whenDone: Runnable? = null): ItemStack {
    val item = getItem(index)
    items[index] = EmptyItemStack
    whenDone?.run()
    return item
  }
  
  /**
   * Checks if the ItemStack at the specified index is non-empty.
   *
   * @param index The index of the ItemStack.
   * @return `true` if the ItemStack at the specified index is non-empty, `false` otherwise.
   */
  fun hasItem(index: Int): Boolean {
    val item = getItemOrNull(index) ?: return false
    return item.amount != 0
  }
  
  /**
   * Sets the ItemStack at the specified index.
   *
   * @param index The index of the ItemStack.
   * @param stack The new ItemStack.
   * @param whenDone The optional runnable to be executed after setting the ItemStack.
   */
  fun setItem(index: Int, stack: ItemStack, whenDone: Runnable? = null) {
    items[index] = stack
    if (!stack.isAir && stack.amount > maxStack) {
      stack.amount = maxStack
    }
    
    whenDone?.run()
  }
  
  /**
   * Decreases the amount of items in the ItemStack at the specified index.
   *
   * @param index The index of the ItemStack.
   * @param amount The amount to decrease.
   * @param whenDone The optional runnable to be executed after decreasing the ItemStack.
   * @return The decreased ItemStack, or an empty ItemStack if the ItemStack at the specified index is null or the amount is greater than the current amount.
   */
  fun decrease(index: Int, amount: Int, whenDone: Runnable? = null): ItemStack {
    val stack = getItemOrNull(index) ?: return EmptyItemStack
    return if (stack.amount <= amount) {
      remove(index)
      whenDone?.run()
      stack
    } else {
      val splitted = stack.split(amount)
      if (stack.amount == 0) remove(index)
      whenDone?.run()
      splitted
    }
  }
  
  /**
   * Clears all the ItemStacks in the ItemList.
   *
   * @param whenDone The optional runnable to be executed after clearing the ItemList.
   */
  fun clear(whenDone: Runnable? = null) {
    items.fill(EmptyItemStack)
    whenDone?.run()
  }
  
  /**
   * Adds an ItemStack to the ItemList without merging.
   *
   * @param item The ItemStack to add.
   * @param whenDone The optional runnable to be executed after adding the ItemStack.
   * @return `true` if the ItemStack was added successfully, `false` if there is no empty slot.
   */
  fun addWithoutMerge(item: ItemStack, whenDone: Runnable? = null): Boolean {
    val slot = firstEmptySlot()
    if (slot != -1) {
      setItem(slot, item, whenDone)
      return true
    }
    
    return false
  }
  
  /**
   * Adds the specified ItemStack to the collection, merging if possible.
   *
   * @param stack The ItemStack to add.
   * @param subtractStack Indicates whether to subtract the added stack from the input stack.
   * @param whenDone A Runnable to be executed when the operation is done.
   * @return True if the stack was successfully added, false otherwise.
   */
  fun addMergingIfPossible(stack: ItemStack, subtractStack: Boolean = false, whenDone: Runnable? = null): Boolean {
    if (stack.isDamageable) {
      return addWithoutMerge(stack, whenDone)
    }
    
    while (true) {
      val partialSlot = firstPartial(stack)
      if (partialSlot == -1) {
        val firstEmpty = firstEmptySlot()
        if (firstEmpty == -1) {
          return false
        }
        
        if (stack.amount > maxStack) {
          val copy = stack.copy(maxStack)
          setItem(firstEmpty, copy, whenDone)
          if (subtractStack) {
            stack.amount -= maxStack
          }
        } else {
          setItem(firstEmpty, stack.copy(), whenDone)
          return true
        }
      } else {
        val partialItem = getItem(partialSlot)
        val total = stack.amount + partialItem.amount
        val maxAmount = min(partialItem.maxStack, maxStack)
        
        if (total <= maxAmount) {
          partialItem.amount = total
          whenDone?.run()
          break
        }
        
        partialItem.amount = maxAmount
        if (subtractStack) {
          stack.amount = total - maxAmount
        }
      }
    }
    
    return true
  }
  
  /**
   * Picks up the specified ItemStack, merging if possible.
   *
   * @param stack The ItemStack to pick up.
   * @param whenDone A Runnable to be executed when the operation is done.
   * @return True if the stack was successfully picked up, false otherwise.
   */
  fun pickup(stack: ItemStack, whenDone: Runnable? = null): Boolean {
    return addMergingIfPossible(stack, true, whenDone)
  }
  
  /**
   * Adds the specified ItemStack, merging if possible.
   *
   * @param stack The ItemStack to add.
   * @param whenDone A Runnable to be executed when the operation is done.
   * @return True if the stack was successfully added, false otherwise.
   */
  fun add(stack: ItemStack, whenDone: Runnable? = null): Boolean {
    return addMergingIfPossible(stack, false, whenDone)
  }
  
  /**
   * Retrieves all non-air ItemStacks in the collection.
   *
   * @return A list of non-air ItemStacks.
   */
  fun allNonAir(): List<ItemStack> = items.filter { !it.isAir }
  
  /**
   * Retrieves all ItemStacks matching the specified ItemStack, with optional tag and stack checking.
   *
   * @param stack The ItemStack to match.
   * @param checkTag Indicates whether to check the tags of the ItemStacks.
   * @param checkStack Indicates whether to check the stack size of the ItemStacks.
   * @return A list of matching ItemStacks.
   */
  fun all(stack: ItemStack, checkTag: Boolean = true, checkStack: Boolean = true): List<ItemStack> {
    return items.filter { stack.isEquals(it, checkTag, checkStack) }
  }
  
  /**
   * Retrieves all ItemStacks matching the specified Item.
   *
   * @param item The Item to match.
   * @return A list of matching ItemStacks.
   */
  fun all(item: Item): List<ItemStack> = items.filter { it.its(item) }
  
  /**
   * Retrieves the index of the first slot containing a partially filled ItemStack matching the specified ItemStack.
   *
   * @param stack The ItemStack to match.
   * @return The index of the first partial slot, or -1 if not found.
   */
  fun firstPartial(stack: ItemStack): Int {
    return items.indexOfFirst { item ->
      !item.isAir && item.isDamageable && item.amount < min(item.maxStack, maxStack) && item.isSimilar(stack)
    }
  }
  
  /**
   * Retrieves the index of the first slot containing an ItemStack matching the specified ItemStack,
   * with optional tag and stack checking.
   *
   * @param stack The ItemStack to match.
   * @param checkTag Indicates whether to check the tags of the ItemStacks.
   * @param checkStack Indicates whether to check the stack size of the ItemStacks.
   * @return The index of the first matching slot, or -1 if not found.
   */
  fun firstSlot(stack: ItemStack, checkTag: Boolean = true, checkStack: Boolean = true): Int {
    return items.indexOfFirst { !it.isAir && stack.isEquals(it, checkTag, checkStack) }
  }
  
  /**
   * Retrieves the index of the first empty slot in the collection.
   *
   * @return The index of the first empty slot, or -1 if not found.
   */
  fun firstEmptySlot(): Int {
    return items.indexOfFirst(ItemStack::isAir)
  }
  
  /**
   * Retrieves the index of the slot containing the specified ItemStack.
   *
   * @param stack The ItemStack to search for.
   * @return The index of the slot containing the ItemStack, or -1 if not found.
   */
  fun slotOf(stack: ItemStack): Int {
    return items.indexOf(stack)
  }
  
  /**
   * Saves the collection to a list tag.
   *
   * @param tag The list tag to save the collection to.
   */
  override fun save(tag: ListTag<CompoundTag>) {
    items.forEachIndexed { index, item ->
      if (!item.isAir) {
        val data = item.store()
        data["Slot"] = index.toByte()
        tag.add(data)
      }
    }
  }
  
  /**
   * Loads the collection from a list tag.
   *
   * @param tag The list tag to load the collection from.
   */
  override fun load(tag: ListTag<CompoundTag>) {
    for (data in tag) {
      val slot = (data.byte("Slot") and 255).toInt()
      items[slot] = data.createItem()
    }
  }
  
  /**
   * Returns an iterator over the ItemStacks in the collection.
   *
   * @return An iterator over the ItemStacks.
   */
  override fun iterator(): Iterator<ItemStack> {
    return items.iterator()
  }
}
