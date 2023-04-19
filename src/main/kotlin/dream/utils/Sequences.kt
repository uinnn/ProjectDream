package dream.utils

import dream.item.*

fun <T : Any> sequenceOfNotNull(vararg elements: T?): Sequence<T> {
   return elements.asSequence().filterNotNull()
}

/**
 * Filter this iterable by items that is air.
 */
fun Sequence<ItemStack>.filterAir() = filter { it.isAir }

/**
 * Filter this iterable by items that is not air.
 */
fun Sequence<ItemStack>.filterNotAir() = filterNot { it.isAir }

/**
 * Filter this iterable by items that has [tag] specified.
 */
fun Sequence<ItemStack>.filterTag(tag: String) = filter { tag in it }

/**
 * Filter this iterable by items that the item type is [T].
 */
inline fun <reified T> Sequence<ItemStack>.filterItem() = filter { it.item is T }

/**
 * Maps this iterable by copying every item.
 */
fun Sequence<ItemStack>.copy() = map { it.copy() }

/**
 * Loops through this iterable of items, iterating in a copy of the item.
 */
inline fun Sequence<ItemStack>.forEachCopy(action: (ItemStack) -> Unit) {
   for (item in this) {
      action(item.copy())
   }
}

/**
 * Sums all items amount of this iterable.
 */
fun Sequence<ItemStack>.sumAmount() = sumOf { it.amount }
