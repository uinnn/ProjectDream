package dream.nbt

import java.io.*

/**
 * Represents a type of [Tag].
 *
 * Used for write/read tags and informational purposes.
 */
interface TagType<T : Tag> {
   
   /**
    * The identifier of this tag type.
    */
   val id: Byte
   
   /**
    * If ItemStack tags support this tag type.
    *
    * Since the client don't know some new types of tags, when
    * serializing an ItemStack to client, an error can be occour.
    *
    * If a tag not supports ItemStack, not use them in items.
    */
   val supportItems: Boolean
   
   /**
    * Loads this tag type from [data].
    */
   fun load(data: ObjectInput): T
}
