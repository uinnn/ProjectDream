package dream.entity.player

import dream.chat.*
import dream.interfaces.*

/**
 * Represents all parts of a player skin.
 */
enum class Skin(override val unlocalName: String) : Locale {
   CAPE("cape"),
   JACKET("jacket"),
   LEFT_ARMS("left_sleeve"),
   RIGHT_ARMS("right_sleeve"),
   LEFT_LEGS("left_pants_leg"),
   RIGHT_LEGS("right_pants_leg"),
   HAT("hat");
   
   val mask = 1 shl ordinal
   val component = text("options.modelPart.$unlocalName")
   
   companion object {
      val values = values()
      
      /**
       * Gets a skin by id.
       */
      fun byId(id: Int) = values[id]
   }
}
