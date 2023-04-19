@file:Suppress("NOTHING_TO_INLINE")

package dream.item

import com.soywiz.kds.hashCode
import dream.Key
import dream.block.Block
import dream.chat.*
import dream.enchantment.*
import dream.entity.*
import dream.entity.item.*
import dream.entity.monster.Enderman
import dream.entity.player.Player
import dream.interfaces.*
import dream.item.block.*
import dream.item.food.*
import dream.item.tool.*
import dream.level.Level
import dream.nbt.*
import dream.nbt.adapter.TagAdapter
import dream.nbt.types.*
import dream.pos.*
import dream.tab.CreativeTab
import dream.utils.*

/**
 * Represents an Item Stack in inventory.
 */
data class ItemStack(
   var item: Item,
   override var amount: Int = 1,
   var metadata: Int = 0,
   var tag: CompoundTag = GlobalCompound,
) : Stackable,
   CompoundStorable,
   Keyable,
   Locale,
   Nameable,
   Comparable<ItemStack> {
   
   /**
    * Constructs a new empty [ItemStack] with Air item and 0 amount.
    */
   constructor() : this(Items.AIR, 0)
   
   /**
    * Constructs a new [ItemStack] from [tag].
    */
   constructor(tag: CompoundTag) : this() {
      load(tag)
   }
   
   /**
    * Gets the key of this item.
    */
   override val key: Key get() = item.key
   
   /**
    * Gets the id of this item.
    */
   val id: Int get() = item.id
   
   /**
    * Number of ticks that this item has in inventory.
    *
    * If a player removes this item of an inventory this will be setted to 0.
    */
   var ticks = 0
   
   /**
    * Number of animation frames to go when receiving an item.
    */
   var popTime = 0
   
   /**
    * Place block checker for adventure mode.
    */
   var placeCheck = AdventureCheck("CanPlaceOn")
   
   /**
    * Break block checker for adventure mode.
    */
   var breakCheck = AdventureCheck("CanDestroy")
   
   /**
    * The entity representation of this item.
    *
    * Normally this only sets [ItemFrame] as an entity representation,
    * to determinate this item standing in the frame.
    */
   var entityRepresentation: Entity? = null
   
   /**
    * Gets if this item has an entity representation.
    */
   val hasEntity: Boolean
      get() = entityRepresentation != null
   
   /**
    * Returns the [ItemFrame] representation of this item.
    *
    * Can return null if [entityRepresentation] is null or is not a [ItemFrame].
    */
   val itemFrame: ItemFrame?
      get() = entityRepresentation as? ItemFrame
   
   /**
    * Returns if this item is on an item frame.
    */
   val isOnFrame: Boolean
      get() = itemFrame != null
   
   /**
    * Returns if this item tag is not empty.
    */
   val hasTag: Boolean get() = tag.isNotEmpty()
   
   /**
    * Returns if this item is air.
    */
   val isAir: Boolean get() = item is ItemAir
   
   /**
    * Returns if this item is tool.
    */
   val isTool: Boolean get() = item is ItemTool
   
   /**
    * Gets the tool associated to this item, or null if is not a [ItemTool]
    */
   val tool get() = item as? ItemTool
   
   /**
    * Returns if this item is armor.
    */
   val isArmor: Boolean get() = item is ItemArmor
   
   /**
    * Gets the armor associated to this item, or null if is not a [ItemArmor]
    */
   val armor get() = item as? ItemArmor
   
   /**
    * Returns if this item is food.
    */
   val isFood: Boolean get() = item is ItemFood
   
   /**
    * Gets the food associated to this item, or null if item is not [ItemFood].
    */
   val food: Food? get() = (item as? ItemFood)?.food
   
   /**
    * Returns if this item is a block.
    */
   val isBlock: Boolean get() = item is ItemBlock
   
   /**
    * Gets the block associated to this item, or null if item is not [ItemBlock].
    */
   val block: Block? get() = item.block
   
   /**
    * If this item has subtypes.
    *
    * Some items like dyes, wools and others, have various item varieties.
    */
   val hasSubtypes: Boolean get() = item.hasSubtypes
   
   /**
    * If this item has any effects.
    */
   val hasEffect: Boolean get() = item.hasEffect(this)
   
   /**
    * Gets the animation of this item.
    */
   val animation: ItemAnimation get() = item.getAnimation(this)
   
   /**
    * Gets the use duration of this item.
    */
   val useDuration: Int get() = item.getUseDuration(this)
   
   /**
    * Gets the display name of this item.
    *
    * If this item not have a custom name, will get the default [item] name.
    */
   override var name: String
      get() = display?.stringOrNull("Name") ?: item.getName(this)
      set(value) {
         addDisplay("Name", value.toTag())
      }
   
   /**
    * Gets the name tag of this item.
    */
   var nameTag: StringTag?
      get() = display?.getTagOrNull("Name")
      set(value) {
         if (value != null) {
            addDisplay("Name", value)
         }
      }
   
   /**
    * Gets if this item has a custom display name.
    */
   override val hasName: Boolean get() = hasDisplay("Name")
   
   /**
    * Gets the lore of this item.
    *
    * If this item not have a custom lore, will get the default [item] lore.
    *
    * Setting an empty list removes the tag.
    */
   var lore: List<String>
      get() = loreTag?.wrap() ?: item.getLore(this)
      set(value) {
         if (value.isNotEmpty()) {
            addDisplay("Lore", value.toTag())
         } else {
            removeDisplay("Lore")
         }
      }
   
   /**
    * Gets the lore tag of this item.
    *
    * Setting an empty list removes the tag.
    */
   var loreTag: ListTag<StringTag>?
      get() = display?.stringListOrNull("Lore")
      set(value) {
         if (value != null && value.isNotEmpty()) {
            addDisplay("Lore", value)
         } else {
            removeDisplay("Lore")
         }
      }
   
   /**
    * Gets if this item has a custom display lore.
    */
   val hasLore: Boolean get() = hasDisplay("Lore")
   
   /**
    * Gets the repair cost in anvil of this item.
    */
   var repairCost: Int
      get() = tag.int("RepairCost", 0)
      set(value) {
         tag["RepairCost"] = value
      }
   
   /**
    * Gets the color hex value of this item.
    *
    * This only affects leather armor items.
    *
    * Setting this below 0 removes the tag.
    */
   var color: Int
      get() = display?.int("color", ItemArmor.LEATHER_COLOR) ?: ItemArmor.LEATHER_COLOR
      set(value) {
         if (value < 0) {
            removeDisplay("color")
         } else {
            addDisplay("color", value.toTag())
         }
      }
   
   /**
    * Returns if this item has color tag.
    */
   val hasColor: Boolean
      get() = hasDisplay("color")
   
   /**
    * Returns if this item accepts color.
    *
    * Only armor items of leather material accepts.
    */
   val acceptsColor: Boolean
      get() = isArmor && (item as ItemArmor).material.hasColor
   
   /**
    * Gets the creative tab of this item.
    */
   val tab: CreativeTab?
      get() = item.tab
   
   /**
    * Returns if this item has a creative tab present.
    */
   val hasTab: Boolean
      get() = item.hasTab
   
   /**
    * Gets the rarity of this item.
    */
   val rarity: ItemRarity
      get() = item.getRarity(this)
   
   /**
    * Gets if this item is damaged.
    */
   val isDamaged: Boolean
      get() = item.isDamageable(this) && metadata > 0
   
   /**
    * Gets the max durability of this item.
    */
   val maxDurability: Int
      get() = item.maxDurability
   
   /**
    * Gets the max stack of this item.
    */
   val maxStack: Int
      get() = item.maxStack
   
   /**
    * If this item will be rendered fully 3D.
    */
   val fullyRender: Boolean
      get() = item.fullyRender
   
   /**
    * Gets the unlocalized name of this item.
    */
   override val unlocalName: String
      get() = item.getUnlocalizedName(this)
   
   /**
    * Gets the `"display"` tag of this item.
    *
    * Used to display some attributes of this item, like name and lore.
    */
   val display: CompoundTag?
      get() = tag.compoundOrNull("display")
   
   /**
    * Gets if this item has `"display"` tag.
    */
   val hasDisplay: Boolean
      get() = display != null
   
   /**
    * Gets the `"ench"` tag of this item.
    *
    * Used to store all enchantments.
    */
   var enchantmentTag: ListTag<CompoundTag>?
      get() = tag.compoundListOrNull("ench")
      set(value) {
         if (value != null) {
            tag["ench"] = value
         } else {
            tag.remove("ench")
         }
      }
   
   /**
    * Gets all enchantments by this item.
    */
   val enchantments: ObjectIntMap<Enchantment>
      get() {
         val enchs = enchantmentTag ?: return ObjectIntMap(0)
         return enchs.associateToNotNull(ObjectIntMap(enchs.size)) {
            Enchantments.getDataOrNull(it)
         }
      }
   
   /**
    * Gets if this item has any enchantments.
    */
   val isEnchanted: Boolean
      get() = enchantmentTag != null
   
   /**
    * Gets the `"AttributeModifiers"` tag of this item.
    *
    * Used to store all attributes.
    */
   var attributeTag: ListTag<CompoundTag>?
      get() = tag.compoundListOrNull("AttributeModifiers")
      set(value) {
         if (value != null) {
            tag["AttributeModifiers"] = value
         } else {
            tag.remove("AttributeModifiers")
         }
      }
   
   /**
    * Gets if this item has any attributes.
    */
   val hasAttributes: Boolean
      get() = attributeTag != null
   
   /**
    * Gets if this item is unbreakable
    */
   var isUnbreakable: Boolean
      get() = tag.boolean("Unbreakable")
      set(value) {
         tag["Unbreakable"] = value
      }
   
   /**
    * Gets the hide flags of this item.
    */
   var flags: Int
      get() = tag.int("HideFlags")
      set(value) {
         tag["HideFlags"] = value
      }
   
   /**
    * Verify if this item has the specified hide [flag] present.
    */
   fun hasFlag(flag: HideFlag) = tag.hasBits("HideFlags", flag)
   operator fun contains(flag: HideFlag) = hasFlag(flag)
   
   /**
    * Adds the specified hide [flag] on this item.
    */
   fun addFlag(flag: HideFlag): ItemStack {
      tag.setBits("HideFlags", flag)
      return this
   }
   
   /**
    * Adds all hide [flags] on this item.
    */
   fun addFlags(vararg flags: HideFlag): ItemStack {
      for (flag in flags) {
         addFlag(flag)
      }
      
      return this
   }
   
   /**
    * Removes the specified hide [flag] on this item.
    */
   fun removeFlag(flag: HideFlag): ItemStack {
      tag.unsetBits("HideFlags", flag)
      return this
   }
   
   /**
    * Removes all hide [flags] on this item.
    */
   fun removeFlags(vararg flags: HideFlag): ItemStack {
      for (flag in flags) {
         removeFlag(flag)
      }
      
      return this
   }
   
   /**
    * Verify and updates the cached results of the adventure place checker.
    */
   fun canAdventurePlace(block: Block) = placeCheck.verify(tag, block)
   
   /**
    * Verify and updates the cached results of the adventure break checker.
    */
   fun canAdventureBreak(block: Block) = breakCheck.verify(tag, block)
   
   /**
    * Returns if this item has [key] in [subTag].
    */
   fun hasTag(key: String) = tag.has(key)
   
   /**
    * Returns if this item has [key] in [subTag].
    */
   fun hasTag(key: String, type: TagType<out Tag>) = tag.has(key, type)
   
   /**
    * Returns a sub tag behind [subTag] specified by [key].
    *
    * @param create if the sub tag must be created if not existent.
    */
   fun subTag(key: String, create: Boolean = false): CompoundTag {
      if (hasTag(key, CompoundType)) {
         return tag.compound(key)
      }
      
      if (create) {
         val tag = CompoundTag()
         this.tag[key] = tag
         return tag
      }
      
      return CompoundTag()
   }
   
   /**
    * Returns a sub compound tag behind [subTag] specified by [key] or null if not present.
    *
    * @param create if the sub tag must be created if not existent.
    */
   fun subTagOrNull(key: String, create: Boolean = false): CompoundTag? {
      if (hasTag(key, CompoundType)) {
         return tag.compound(key)
      }
      
      if (create) {
         val tag = CompoundTag()
         this.tag[key] = tag
         return tag
      }
      
      return null
   }
   
   /**
    * Returns a sub list tag behind [subTag] specified by [key].
    *
    * @param create if the sub tag must be created if not existent.
    */
   fun <T : Tag> subTagList(key: String, create: Boolean = false): ListTag<T> {
      if (hasTag(key, ListType)) {
         return tag.list(key)
      }
      
      if (create) {
         val tag = ListTag<T>()
         this.tag[key] = tag
         return tag
      }
      
      return ListTag()
   }
   
   /**
    * Returns a sub list tag behind [subTag] specified by [key] or null if not present.
    *
    * @param create if the sub tag must be created if not existent.
    */
   fun <T : Tag> subTagListOrNull(key: String, create: Boolean = false): ListTag<T>? {
      if (hasTag(key, ListType)) {
         return tag.list(key)
      }
      
      if (create) {
         val tag = ListTag<T>()
         this.tag[key] = tag
         return tag
      }
      
      return null
   }
   
   /**
    * Adds a new tag on the main tag of this item.
    */
   fun addTag(key: String, tag: Tag): ItemStack {
      set(key, tag)
      return this
   }
   
   /**
    * Adds a new tag on the display tag of this item.
    */
   fun addDisplay(key: String, tag: Tag): ItemStack {
      subTag("display", true)[key] = tag
      return this
   }
   
   /**
    * Removes [key] on [tag].
    */
   fun removeTag(key: String): ItemStack {
      tag.remove(key)
      return this
   }
   
   /**
    * Removes [key] on [display] tag and removes display tag if empty.
    */
   fun removeDisplay(key: String): ItemStack {
      val display = this.display ?: return this
      display.remove(key)
      if (display.isEmpty()) {
         tag.remove("display")
      }
      
      return this
   }
   
   /**
    * Gets a possible existent display tag by [key].
    */
   inline fun <reified T : Tag> getDisplay(key: String): T? {
      return display?.getTag(key)
   }
   
   /**
    * Returns if the display tag of this item has [key].
    */
   fun hasDisplay(key: String): Boolean {
      return display?.has(key) == true
   }
   
   /**
    * Returns if the display tag of this item has [key].
    */
   fun hasDisplay(key: String, type: TagType<out Tag>): Boolean {
      return display?.has(key, type) == true
   }
   
   /**
    * Clear any custom display name of this item.
    */
   fun clearName() = removeDisplay("Name")
   
   /**
    * Clear any custom display lore of this item.
    */
   fun clearLore() = removeDisplay("Lore")
   
   /**
    * Clear any display associated tag of this item.
    */
   fun clearDisplay() = removeTag("display")
   
   /**
    * Gets the display tag of this item or creates a new one.
    */
   fun getOrCreateDisplay(): CompoundTag {
      return tag.getOrAdd("display") { CompoundTag() }
   }
   
   /**
    * Gets the lore tag of this item or creates a new one.
    */
   fun getOrCreateLore(): ListTag<StringTag> {
      return getOrCreateDisplay().getOrAdd("Lore") { ListTag() }
   }
   
   /**
    * Adds a lore on this item.
    */
   fun addLore(str: String): ItemStack {
      getOrCreateLore().add(str.toTag())
      return this
   }
   
   /**
    * Adds a lore on this item.
    */
   fun addLore(index: Int, str: String): ItemStack {
      getOrCreateLore().add(index, str.toTag())
      return this
   }
   
   /**
    * Adds a lore on this item.
    */
   fun addLore(vararg str: String): ItemStack {
      getOrCreateLore().addAll(str.toTag())
      return this
   }
   
   /**
    * Adds a lore on this item.
    */
   fun addLore(index: Int, vararg str: String): ItemStack {
      getOrCreateLore().addAll(index, str.toTag())
      return this
   }
   
   /**
    * Adds the specified [enchantment] with given [level] on this item.
    */
   fun enchant(enchantment: Enchantment, level: Int): ItemStack {
      val tag = subTagList<CompoundTag>("ench", true)
      val data = CompoundTag().withEnch(enchantment, level)
      tag += data
      return this
   }
   
   /**
    * Removes the specified [enchantment] if present on this item.
    */
   fun disenchant(enchantment: Enchantment): ItemStack {
      val enchs = enchantmentTag ?: return this
      
      enchs.removeIf { it.hasEnchantment(enchantment) }
      if (enchs.isEmpty()) {
         tag.remove("ench")
      }
      
      return this
   }
   
   /**
    * Returns if this item is enchanted with [enchantment].
    */
   fun isEnchanted(enchantment: Enchantment): Boolean {
      val enchs = enchantmentTag ?: return false
      return enchs.any { it.hasEnchantment(enchantment) }
   }
   
   /**
    * Returns the enchantment compound tag represented by [enchantment]
    * present on this item tag.
    */
   fun enchantmentTag(enchantment: Enchantment): CompoundTag? {
      val enchs = enchantmentTag ?: return null
      return enchs.find { it.hasEnchantment(enchantment) }
   }
   
   /**
    * Gets the enchantment level of [enchantment] on this item.
    */
   fun level(enchantment: Enchantment): Int {
      val tag = enchantmentTag(enchantment) ?: return 0
      return tag.int("lvl")
   }
   
   /**
    * Gets if this item has a custom entity when dropped.
    */
   fun hasCustomEntity(level: Level): Boolean {
      return item.hasCustomEntity(level, this)
   }
   
   /**
    * Determines if this Item is fire immune.
    *
    * This is a simple pre-made 1.16 netherite fire resistance feature on 1.8.
    */
   fun isFireImmune(entity: EntityItem): Boolean {
      return item.isFireImmune(entity.level, entity, this)
   }
   
   /**
    * Gets the lifespan in ticks for [EntityItem] before removes this item.
    */
   fun lifespan(level: Level): Int {
      return item.lifespan(level, this)
   }
   
   /**
    * Creates an entity associated to this item.
    *
    * Almost all cases returns [EntityItem], but in some cases that
    * [Item.hasCustomEntity] returns true this will create the associated
    * custom entity for this item stack.
    */
   fun createEntity(level: Level, x: Double, y: Double, z: Double): Entity {
      return if (hasCustomEntity(level)) {
         item.createEntity(level, this, x, y, z)
      } else {
         val entity = EntityItem(level, x, y, z, this)
         entity.age = lifespan(level)
         entity
      }
   }
   
   /**
    * Creates an entity associated to this item.
    *
    * Almost all cases returns [EntityItem], but in some cases that
    * [Item.hasCustomEntity] returns true this will create the associated
    * custom entity for this item stack.
    */
   fun createEntity(level: Level, pos: Pos): Entity {
      return createEntity(level, pos.x, pos.y, pos.z)
   }

   /**
    * Gets if this item can be dropped.
    */
   fun canDrop(level: Level, x: Double, y: Double, z: Double): Boolean {
      return item.canDrop(level, this, x, y, z)
   }

   /**
    * Drops this item.
    *
    * Can return null if this item is air or stack is below 0.
    */
   fun drop(level: Level, x: Double, y: Double, z: Double): Entity? {
      if (isAir || amount <= 0) {
         return null
      }

      return if (canDrop(level, x, y, z)) {
         val entity = createEntity(level, x, y, z)
         item.onDrop(level, this, entity, x, y, z)
         entity.spawnAndGet(level)
      } else {
         null
      }
   }
   
   /**
    * Drops this item.
    *
    * Can return null if this item is air or stack is below 0.
    */
   fun drop(level: Level, pos: Pos): Entity? {
      return drop(level, pos.x, pos.y, pos.z)
   }
   
   /**
    * Called every tick since is in inventory of [entity].
    */
   fun onTick(entity: Entity, slot: Int, selected: Boolean) {
      if (popTime > 0)
         popTime--
      
      ticks++
      item.onTick(entity.level, this, entity, slot, selected)
   }
   
   /**
    * Called to tick item while dropped as [EntityItem].
    */
   fun onEntityTick(entity: EntityItem) {
      item.onEntityTick(entity.level, this, entity)
   }
   
   /**
    * Called to tick item when player is using.
    */
   fun onUseTick(player: Player) {
      item.onUseTick(player.level, this, player)
   }
   
   /**
    * Called to tick armor on the armor slot.
    */
   fun onArmorTick(entity: Entity, slot: Int) {
      item.onArmorTick(entity.level, this, entity, slot)
   }
   
   /**
    * Called when [player] stops using this item.
    */
   fun onStoppedUsing(player: Player, timeLeft: Int) {
      item.onStoppedUsing(player.level, this, player, timeLeft)
   }
   
   /**
    * Called when a player right clicks in a block using this item.
    */
   fun onUse(player: Player, pos: Pos, direction: Direction, hit: Pos = Pos.ZERO): Boolean {
      return item.onUse(player.level, this, player, pos, direction, hit)
   }
   
   /**
    * Called whenever this item is equipped and the player right clicks using this item.
    */
   fun onRightClick(player: Player): ItemStack {
      return item.onRightClick(player.level, this, player)
   }
   
   /**
    * Called when the player finishes using this Item (e.g. finishes eating.).
    *
    * Not called when the player stops using the Item before the action is complete.
    */
   fun onFinishUse(player: Player): ItemStack {
      return item.onFinishUse(player.level, this, player)
   }
   
   /**
    * Called when [player] right-clicks on [target] using this item.
    */
   fun onInteract(player: Player, target: Entity): Boolean {
      return item.onInteract(player.level, this, player, target)
   }
   
   /**
    * Hurts the given [target] entity by [attacker] using this item.
    */
   fun hurt(attacker: Creature, target: Entity): Boolean {
      return item.hurt(attacker.level, this, attacker, target)
   }
   
   /**
    * Called when [player] mines [block] succesfully using this item.
    */
   fun mine(player: Player, block: Block, pos: Loc): Boolean {
      return item.mine(player.level, this, player, block, pos)
   }
   
   /**
    * Returns true if players can use this item to affect
    * the world (e.g. placing blocks) when not in creative.
    */
   fun canEditBlocks(player: Player): Boolean {
      return item.canEditBlocks(player.level, this, player)
   }
   
   /**
    * Returns if this item is repaired by using [repair] in anvil.
    */
   fun isRepairable(repair: ItemStack): Boolean {
      return item.isRepairable(this, repair)
   }
   
   /**
    * Gets destroy speed multiplier of this item when breaking [block].
    */
   fun getDestroySpeed(block: Block): Float {
      return item.getDestroySpeed(this, block)
   }
   
   /**
    * Verify if this item can harvest [block].
    */
   fun canHarvest(block: Block): Boolean {
      return item.canHarvest(this, block)
   }
   
   /**
    * Whether this Item can be used to hide player head for enderman.
    */
   fun isEnderMask(player: Player, enderman: Enderman): Boolean {
      return item.isEnderMask(player.level, this, player, enderman)
   }
   
   /**
    * Grows this item by 1.
    */
   operator fun inc() = grow(1)
   
   /**
    * Shrinks this item by 1.
    */
   operator fun dec() = shrink(1)
   
   /**
    * Adds [quantity] to the stack of this item.
    */
   infix fun grow(quantity: Int): ItemStack {
      amount += quantity
      return this
   }
   
   /**
    * Removes [quantity] to the stack of this item.
    */
   infix fun shrink(quantity: Int): ItemStack {
      amount -= quantity
      return this
   }
   
   /**
    * Splits this item by the given [quantity].
    *
    * This returns a copy of this item with amount of a min of [quantity] and [amount].
    *
    * The current [amount] of this item will be subtracted by [quantity].
    *
    * Example:
    * ```
    * item.amount == 48
    *
    * val split = item.split(8)
    *
    * split.amount == 8
    * item.amount == 40
    * ```
    */
   fun split(quantity: Int): ItemStack {
      val min = min(quantity, amount)
      shrink(min)
      return copy(min)
   }
   
   /**
    * Returns if this item is of type [T].
    */
   inline fun <reified T : Item> its(): Boolean {
      return item is T
   }
   
   /**
    * Returns if this item is of type [item].
    */
   fun its(item: Item): Boolean {
      return this.item::class.isInstance(item)
   }
   
   /**
    * Executes [action] if this item stack is air.
    */
   inline fun ifAir(action: ItemStack.() -> Unit) {
      if (isAir) {
         action(this)
      }
   }
   
   /**
    * Executes [action] if this item stack is not air.
    */
   inline fun ifNotAir(action: ItemStack.() -> Unit) {
      if (!isAir) {
         action(this)
      }
   }
   
   /**
    * Executes [action] if this item stack item is equals to [T].
    */
   inline fun <reified T : Item> ifIs(action: ItemStack.() -> Unit) {
      if (item is T) {
         action(this)
      }
   }
   
   /**
    * Executes [action] if this item stack item is not equals to [T].
    */
   inline fun <reified T : Item> ifNotIs(action: ItemStack.() -> Unit) {
      if (item !is T) {
         action(this)
      }
   }
   
   /**
    * Gets this item or null if this item is air.
    */
   fun orNull(): ItemStack? {
      return if (isAir) null else this
   }
   
   /**
    * Creates a copy of this item stack.
    */
   fun copy(): ItemStack {
      val copy = ItemStack(item, amount, metadata, tag.copy())
      copy.ticks = ticks
      return copy
   }
   
   /**
    * Copies this item with given [amount].
    */
   fun copy(amount: Int): ItemStack {
      val copy = ItemStack(item, amount, metadata, tag.copy())
      copy.ticks = ticks
      return copy
   }
   
   /**
    * Creates a copy of this item stack or null if this item is air.
    */
   fun copyOrNull(): ItemStack? {
      return if (isAir) null else copy()
   }
   
   /**
    * Copies this item with given [amount] or null if this item is air.
    */
   fun copyOrNull(amount: Int): ItemStack? {
      return if (isAir) null else copy(amount)
   }
   
   /**
    * Applies the given [builder] to the tag of this item.
    */
   inline fun withTag(builder: CompoundTag.() -> Unit): ItemStack {
      tag.apply(builder)
      return this
   }
   
   /**
    * Creates a new [Component] with all data from this item.
    *
    * Used to show item on hover.
    */
   fun component(): Component {
      val name = displayName
      if (hasName) {
         name.style.italic(true)
      }
      
      return text("[").add(name).add("]")
         .showItem(this)
         .color(rarity.color)
   }
   
   /**
    * Verifies if this item is similar to [other], without comparing their stacks.
    *
    * @param checkTag if checks the tag of both is equals.
    */
   fun isSimilar(other: ItemStack, checkTag: Boolean = true): Boolean {
      if (this === other)
         return true
      
      return item == other.item &&
         metadata == other.metadata &&
         if (checkTag) tag == other.tag else true
   }
   
   /**
    * Verifies if [tag] has [key] presents.
    */
   operator fun contains(key: String) = key in tag
   
   /**
    * Verifies if the stack of this item equals or lower than [amount].
    */
   operator fun contains(amount: Int) = amount <= this.amount
   
   /**
    * Compares the stack of this item by [amount]
    */
   operator fun compareTo(amount: Int) = this.amount.compareTo(amount)
   override fun compareTo(other: ItemStack): Int {
      return amount.compareTo(other.amount)
   }
   
   /**
    * Gets a tag by the specified [key].
    */
   operator fun get(key: String) = tag[key]
   
   operator fun set(key: String, value: Tag) = tag.set(key, value)
   operator fun set(key: String, value: Boolean) = set(key, ByteTag.of(value))
   operator fun set(key: String, value: Byte) = set(key, ByteTag.of(value))
   operator fun set(key: String, value: Short) = set(key, ShortTag.of(value))
   operator fun set(key: String, value: Int) = set(key, IntTag.of(value))
   operator fun set(key: String, value: Long) = set(key, LongTag.of(value))
   operator fun set(key: String, value: Float) = set(key, FloatTag.of(value))
   operator fun set(key: String, value: Double) = set(key, DoubleTag.of(value))
   operator fun set(key: String, value: ByteArray) = set(key, ByteArrayTag(value))
   operator fun set(key: String, value: String) = set(key, StringTag.of(value))
   operator fun set(key: String, value: IntArray) = set(key, IntArrayTag(value))
   operator fun <T : Any> set(key: String, adapter: TagAdapter<T>, value: T) = adapter.write(key, tag, value)
   
   /**
    * Saves this item inside of [tag].
    */
   override fun save(tag: CompoundTag) {
      tag["id"] = Items.getKey(item)?.toString() ?: "minecraft:air"
      tag["Count"] = amount.toByte()
      tag["Damage"] = metadata.toShort()
      if (hasTag) {
         item.writeTag(this, this.tag)
         tag["tag"] = this.tag
      }
   }
   
   /**
    * Loads this item by [tag].
    */
   override fun load(tag: CompoundTag) {
      item = Items.byTag(tag, "id")
      amount = tag.byte("Count").toInt()
      metadata = tag.short("Damage").toInt()
      
      if (metadata < 0)
         metadata = 0
      
      this.tag = tag.compound("tag")
      if (this.tag.isNotEmpty()) {
         item.readTag(this, this.tag)
      }
   }
   
   override fun toString(): String {
      return "ItemStack(item=$item, metadata=$metadata, amount=$amount, tag=$tag)"
   }
   
   override fun equals(other: Any?): Boolean {
      if (other !is ItemStack)
         return false
      
      return isSimilar(other) && amount == other.amount
   }
   
   override fun hashCode(): Int {
      return hashCode(item, amount, metadata, tag)
   }
   
   private fun CompoundTag.withEnch(enchantment: Enchantment, level: Int): CompoundTag {
      set("id", enchantment.id.toShort())
      set("lvl", level)
      return this
   }
   
   companion object {
      
      /**
       * Empty Air [ItemStack].
       *
       * Should be used instead null.
       */
      @JvmField val AIR = ItemStack()
      
      /**
       * Decodes [data] to a [ItemStack].
       */
      fun fromByteArray(data: ByteArray): ItemStack {
         return ItemStack(data.decodeCompound())
      }
   }
}

/**
 * Creates an empty [ItemStack].
 */
inline fun stackOf() = ItemStack.AIR

/**
 * Creates an empty [ItemStack].
 */
inline fun emptyStack() = ItemStack.AIR

/**
 * Creates a [ItemStack] with the given data specified.
 */
fun stackOf(
   item: Item,
   amount: Int = 1,
   metadata: Int = 0,
   tag: CompoundTag = CompoundTag(),
) = ItemStack(item, amount, metadata, tag)

/**
 * Creates a [ItemStack] with the given tag specified.
 */
inline fun stackOf(tag: CompoundTag) = ItemStack(tag)

/**
 * Creates a [ItemStack] with the given [builder] applying them.
 */
inline fun stack(
   item: Item,
   amount: Int = 1,
   metadata: Int = 0,
   tag: CompoundTag = CompoundTag(),
   builder: ItemStack.() -> Unit,
) = ItemStack(item, amount, metadata, tag).apply(builder)

/**
 * Creates a [ItemStack] with the given [builder] applying them.
 */
inline fun stack(builder: ItemStack.() -> Unit) = ItemStack().apply(builder)

/**
 * Decodes this byte array to [ItemStack].
 */
fun ByteArray.decodeItem() = ItemStack(decodeCompound())

/**
 * Checks if both [first] and [second] items is air.
 */
fun isAir(first: ItemStack, second: ItemStack) = first.isAir && second.isAir

/**
 * Checks if both [first], [second] and [third] items is air.
 */
fun isAir(first: ItemStack, second: ItemStack, third: ItemStack): Boolean {
   return first.isAir && second.isAir && third.isAir
}


/**
 * Filter this iterable by items that is air.
 */
fun Iterable<ItemStack>.filterAir() = filter { it.isAir }

/**
 * Filter this iterable by items that is not air.
 */
fun Iterable<ItemStack>.filterNotAir() = filterNot { it.isAir }

/**
 * Filter this iterable by items that has [tag] specified.
 */
fun Iterable<ItemStack>.filterTag(tag: String) = filter { tag in it }

/**
 * Filter this iterable by items that the item type is [T].
 */
inline fun <reified T> Iterable<ItemStack>.filterItem() = filter { it.item is T }

/**
 * Maps this iterable by copying every item.
 */
fun Iterable<ItemStack>.copy() = map { it.copy() }

/**
 * Loops through this iterable of items, iterating in a copy of the item.
 */
inline fun Iterable<ItemStack>.forEachCopy(action: (ItemStack) -> Unit) {
   for (item in this) {
      action(item.copy())
   }
}

/**
 * Sums all items amount of this iterable.
 */
fun Iterable<ItemStack>.sumAmount() = sumOf { it.amount }
