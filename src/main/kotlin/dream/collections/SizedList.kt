package dream.collections

import dream.misc.Open
import dream.utils.size

/**
 * A SizedList.
 *
 * SizedList is a list that only have a limited amount of entries,
 * if a list is reached in their limit, no entry will be added.
 */
@Open
class SizedList<T>(var limit: Int, capacity: Int = limit) : ArrayList<T>(capacity) {

  constructor(limit: Int, elements: Collection<T>) : this(limit) {
    addAll(elements)
  }

  constructor(limit: Int, elements: Iterable<T>) : this(limit) {
    addAll(elements)
  }

  /**
   * Trims this SizedList to [limit].
   */
  fun trim() {
    repeat(size - limit) {
      removeAt(limit + it)
    }
  }

  // add limiter
  override fun set(index: Int, element: T): T {
    return if (index <= limit) super.set(index, element) else element
  }

  // add limiter
  override fun add(element: T): Boolean {
    return if (size + 1 > limit) false else super.add(element)
  }

  // add limiter
  override fun add(index: Int, element: T) {
    if (index <= limit) {
      super.add(index, element)
    }
  }

  // add limiter
  override fun addAll(elements: Collection<T>): Boolean {
    return if (size + elements.size > limit) {
      super.addAll(elements.take(size - elements.size))
    } else {
      super.addAll(elements)
    }
  }
}


/**
 * Returns an empty [SizedList].
 */
fun <E> sizedListOf(limit: Int, capacity: Int = limit) = SizedList<E>(limit, capacity)

/**
 * Returns a [SizedList] filled with all [elements].
 */
fun <E> sizedListOf(limit: Int, vararg elements: E) = SizedList(limit, elements.toMutableList())

/**
 * Returns a [SizedList] filled with all [elements].
 */
fun <E> sizedListOf(vararg elements: E) = SizedList(elements.size, elements.toMutableList())

/**
 * Returns a new [SizedList] filled with all elements of this collection.
 */
fun <E> Collection<E>.toSizedList(limit: Int) = SizedList(limit, this)

/**
 * Returns a new [SizedList] filled with all elements of this collection.
 */
fun <E> Iterable<E>.toSizedList(limit: Int) = SizedList(limit, this)

/**
 * Returns a new [SizedList] filled with all elements of this collection.
 */
fun <E> Collection<E>.toSizedList() = SizedList(size, this)

/**
 * Returns a new [SizedList] filled with all elements of this collection.
 */
fun <E> Iterable<E>.toSizedList() = SizedList(size, this)
