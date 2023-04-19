package dream.nbt

import java.io.*

/**
 * An abstract implementation of [Tag] that's supports a collection of elements.
 */
interface ArrayTag : Tag, Serializable {
   
   /**
    * The element tag type of this array.
    */
   fun elementType(): TagType<out Tag>
}
