package dream.collections

import dream.misc.*
import dream.utils.*
import kotlin.collections.lastIndex
import java.util.*

/**
 * An implementation of [LinkedList] that's adds supports to
 * search for the current/next/previous indexes.
 *
 * Optionally all elements specified in the constructor will be added in the list.
 */
@Open
public class IndexList<E> : LinkedList<E> {
   
   constructor()
   constructor(elements: Collection<E>) : super(elements)
   constructor(elements: Iterable<E>) {
      addAll(elements)
   }
   
   /**
    * The current index of this index list.
    *
    * This can be changed to set's the current index by the specified value directly without
    * calling the function [toNext] or [toPrevious] one by one.
    */
   var index = 0
   
   /**
    * Returns if this index list has next elements.
    */
   val hasNext get() = index < lastIndex
   
   /**
    * Returns if this index list has previous elements.
    */
   val hasPrevious get() = index > 0
   
   /**
    * Returns the next index of this index list, or the last index
    * if is reached to the end of this list.
    */
   val nextIndex get() = min(index + 1, lastIndex)
   
   /**
    * Returns the previous index of this index list, or the first index
    * if is reached to the start of this list.
    */
   val previousIndex get() = max(index - 1, 0)
   
   /**
    * Returns all previously elements of this index list,
    * or an empty list if not contains any previous elements.
    */
   val previously get() = subList(0, index)
   
   /**
    * Returns all nextly elements of this index list,
    * or an empty list if not contains any next elements.
    */
   val nextly get() = subList(nextIndex, size)
   
   /**
    * Returns the current element of this index list, or throws a
    * [NoSuchElementException] if not contains any element in the current [index].
    */
   fun current() = get(index)
   
   /**
    * Returns the current element of this index list,
    * or nulls if not contains any element in the current [index].
    */
   fun currentOrNull() = getOrNull(index)
   
   /**
    * Returns the next element of this index list, or throws a
    * [NoSuchElementException] if not contains any element in the next index.
    */
   fun next() = get(index + 1)
   
   /**
    * Returns the next element of this index list, or nulls
    * if not contains any element in the next index.
    */
   fun nextOrNull() = getOrNull(index + 1)
   
   /**
    * Returns the next element of this index list, or the first element if not
    * contains any element in the next index.
    */
   fun nextOrFirst(): E = nextOrNull() ?: first
   
   /**
    * Returns the previous element of this index list, or throws a
    * [NoSuchElementException] if not contains any element in the previous index.
    */
   fun previous() = get(index - 1)
   
   /**
    * Returns the previous element of this index list, or nulls
    * if not contains any element in the previous index.
    */
   fun previousOrNull() = getOrNull(index - 1)
   
   /**
    * Returns the previous element of this index list, or the last element if not
    * contains any element in the previous index.
    */
   fun previousOrLast(): E = previousOrNull() ?: last
   
   /**
    * Go to the given [index].
    */
   fun goto(index: Int): E {
      this.index = index
      return current()
   }
   
   /**
    * Go to the given [index].
    */
   fun gotoOrNull(index: Int): E? {
      this.index = index
      return currentOrNull()
   }
   
   /**
    * Go to the first index of this index list and returns the first element.
    */
   fun toFirst(): E {
      index = 0
      return first
   }
   
   /**
    * Go to the last index of this index list and returns the first element.
    */
   fun toLast(): E {
      index = lastIndex
      return last
   }
   
   /**
    * Go to the next index of this index list and returns the next element.
    *
    * If the next index is reached to the end of this list nothing will be changed.
    */
   fun toNext(): E {
      index++
      return current()
   }
   
   /**
    * Go to the next index of this index list and returns the next element.
    *
    * If the next index is reached to the end of this list will return null.
    */
   fun toNextOrNull(): E? {
      index++
      return currentOrNull()
   }
   
   /**
    * Go to the next index or the first index and returns the current element.
    */
   fun toNextOrFirst(): E {
      if (++index >= size)
         index = 0
      
      return current()
   }
   
   /**
    * Go to the previous index of this index list and returns the next element.
    *
    * If the previous index is reached to the start of this list nothing will be changed.
    */
   fun toPrevious(): E {
      index--
      return current()
   }
   
   /**
    * Go to the previous index of this index list and returns the next element.
    *
    * If the previous index is reached to the start of this list will return null.
    */
   fun toPreviousOrNull(): E? {
      index--
      return currentOrNull()
   }
   
   /**
    * Go to the previous index or the last index and returns the current element.
    */
   fun toPreviousOrLast(): E {
      if (--index < 0)
         index = lastIndex
      
      return current()
   }
}

/**
 * Returns an empty [IndexList].
 */
fun <E> indexListOf() = IndexList<E>()

/**
 * Returns a [IndexList] filled with all [elements].
 */
fun <E> indexListOf(vararg elements: E) = IndexList(elements.toMutableList())

/**
 * Returns a new [IndexList] filled with all elements of this collection.
 */
fun <E> Collection<E>.toIndexList(): IndexList<E> = IndexList(this)

/**
 * Returns a new [IndexList] filled with all elements of this collection.
 */
fun <E> Iterable<E>.toIndexList(): IndexList<E> = IndexList(this)

/**
 * Builds an index list with data gived in [block].
 */
inline fun <T> indexList(block: IndexList<T>.() -> Unit): IndexList<T> {
   return IndexList<T>().apply(block)
}
