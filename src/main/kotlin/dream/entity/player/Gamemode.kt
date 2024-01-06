package dream.entity.player

/**
 * Represents all type of player game modes.
 */
enum class Gamemode(val display: String) {
  SURVIVAL("survival"),
  CREATIVE("creative"),
  ADVENTURE("adventure"),
  SPECTATOR("spectator");
  
  val id get() = ordinal
  val isSurvival get() = this == SURVIVAL
  val isCreative get() = this == CREATIVE
  val isAdventure get() = this == ADVENTURE
  val isSpectator get() = this == SPECTATOR
  val isAdventureOrSpectator get() = isAdventure || isSpectator
  val isSurvivalOrAdventure get() = isSurvival || isAdventure

  /**
   * Configure player capabilities according to this gamemode.
   */
  fun configure(player: Player) {
    when (this) {
      CREATIVE -> {
        player.allowFly = true
        player.consumeItems = true
        player.disableDamage = true
      }

      SPECTATOR -> {
        player.allowFly = true
        player.consumeItems = false
        player.disableDamage = true
        player.isFlying = true
      }

      else -> {
        player.allowFly = false
        player.consumeItems = false
        player.disableDamage = false
        player.isFlying = false
      }
    }

    player.allowEdit = !isAdventureOrSpectator
  }

  companion object {

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
      return entries.find { it.name.equals(name, true) } ?: SURVIVAL
    }
  }
}
