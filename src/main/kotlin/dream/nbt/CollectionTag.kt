package dream.nbt

import java.io.*

/**
 * An abstract implementation of [Tag] that's supports a collection of elements.
 */
abstract class CollectionTag<T> : ArrayList<T>, Tag, Serializable {
   
   override val genericValue: Any
      get() = this
   
   constructor() : super()
   constructor(values: Collection<T>) : super(values)
   
   /**
    * Fetches the type of all elements of this collection.
    */
   abstract fun fetchType(): TagType<out Tag>
}
