package dream.item

import dream.block.*
import dream.item.block.*

/**
 * Represents a colored item.
 */
class ItemColored(val coloredBlock: Block, hasSubtypes: Boolean) : ItemBlock(coloredBlock) {
   
   /**
    * All subtype names of this colored item.
    */
   val subtypeNames: MutableList<String> = ArrayList()
   
   init {
   
   
   }
   
   
}
