package dream.nbt

import dream.utils.*
import java.io.*

/**
 * An abstract implementation of [Tag] that's supports a collection of elements.
 */
abstract class CollectionTag<T> : ArrayList<T>, Tag, Serializable {

  override val genericValue: MutableList<T>
    get() = this

  constructor() : super()
  constructor(values: Collection<T>) : super(values)

  /**
   * Fetches the type of all elements of this collection.
   */
  abstract fun fetchType(): TagType<out Tag>
  
  override fun changeValue(value: Any) {
    if (value is Collection<*> || value is Iterable<*>) {
      clear()
      addAll(value.cast())
    }
  }
  
  override fun toString(): String {
    return genericValue.toString()
  }
  
  override fun equals(other: Any?): Boolean {
    if (other !is CollectionTag<*>)
      return false
    
    return genericValue == other.genericValue
  }
  
  override fun hashCode(): Int {
    return genericValue.hashCode()
  }
}
