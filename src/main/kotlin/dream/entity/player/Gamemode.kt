package dream.entity.player

/**
 * Represents all type of player game modes.
 */
enum class Gamemode(val id: Int, val display: String) {
   SURVIVAL(0, "survival"),
   CREATIVE(1, "creative"),
   ADVENTURE(2, "adventure"),
   SPECTATOR(3, "spectator");
   
   val isSurvival get() = this == SURVIVAL
   val isCreative get() = this == CREATIVE
   val isAdventure get() = this == ADVENTURE
   val isSpectator get() = this == SPECTATOR
   val isSurvivalOrAdventure get() = isSurvival || isAdventure
   
   /**
    * Configure player capabilities according to this gamemode.
    */
   fun configure(player: Player) {
      when (this) {
         CREATIVE -> {
            player.allowFly = true
            player.isCreative = true
            player.disableDamage = true
         }
         SPECTATOR -> {
            player.allowFly = true
            player.isCreative = false
            player.disableDamage = true
            player.isFlying = true
         }
         else -> {
            player.allowFly = false
            player.isCreative = false
            player.disableDamage = false
            player.isFlying = false
         }
      }
      
      player.allowEdit = !isAdventure
   }
   
   companion object {
      val values = values()
   
      /**
       * Gets a gamemode by id.
       */
      fun byId(id: Int) = when (id) {
         1 -> CREATIVE
         2 -> ADVENTURE
         3 -> SPECTATOR
         else -> SURVIVAL
      }
   
      /**
       * Gets a gamemode by name.
       */
      fun byName(name: String): Gamemode {
         return values.find { it.name.equals(name, true) } ?: SURVIVAL
      }
   }
}
