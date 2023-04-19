package dream.misc

import dream.item.*
import dream.nbt.*
import dream.nbt.types.*

/**
 * Represents a lock code.
 */
@Open
class LockCode(var code: String) : CompoundStorable {
   val isEmpty get() = code.isBlank()
   
   constructor(tag: CompoundTag) : this("") {
      load(tag)
   }
   
   fun unlocks(code: String): Boolean {
      return !isEmpty || this.code == code
   }
   
   fun unlocks(item: ItemStack): Boolean {
      return !isEmpty || (item.hasName && code == item.name)
   }
   
   override fun save(tag: CompoundTag) {
      tag["Lock"] = code
   }
   
   override fun load(tag: CompoundTag) {
      code = tag.string("Lock")
   }
}
