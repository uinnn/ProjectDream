package dream.particle

/**
 * Represents different types of particles.
 */
enum class Particle(
  val display: String,
  val id: Int,
  val ignoreRange: Boolean = false,
  val arguments: Int = 0,
) {
  
  EXPLOSION_NORMAL("explode", 0, true),
  EXPLOSION_LARGE("largeexplode", 1, true),
  EXPLOSION_HUGE("hugeexplosion", 2, true),
  FIREWORKS_SPARK("fireworksSpark", 3),
  WATER_BUBBLE("bubble", 4),
  WATER_SPLASH("splash", 5),
  WATER_WAKE("wake", 6),
  SUSPENDED("suspended", 7),
  SUSPENDED_DEPTH("depthsuspend", 8),
  CRIT("crit", 9),
  CRIT_MAGIC("magicCrit", 10),
  SMOKE_NORMAL("smoke", 11),
  SMOKE_LARGE("largesmoke", 12),
  SPELL("spell", 13),
  SPELL_INSTANT("instantSpell", 14),
  SPELL_MOB("mobSpell", 15),
  SPELL_MOB_AMBIENT("mobSpellAmbient", 16),
  SPELL_WITCH("witchMagic", 17),
  DRIP_WATER("dripWater", 18),
  DRIP_LAVA("dripLava", 19),
  VILLAGER_ANGRY("angryVillager", 20),
  VILLAGER_HAPPY("happyVillager", 21),
  TOWN_AURA("townaura", 22),
  NOTE("note", 23),
  PORTAL("portal", 24),
  ENCHANTMENT_TABLE("enchantmenttable", 25),
  FLAME("flame", 26),
  LAVA("lava", 27),
  FOOTSTEP("footstep", 28),
  CLOUD("cloud", 29),
  REDSTONE("reddust", 30),
  SNOWBALL("snowballpoof", 31),
  SNOW_SHOVEL("snowshovel", 32),
  SLIME("slime", 33),
  HEART("heart", 34),
  BARRIER("barrier", 35),
  ITEM_CRACK("iconcrack_", 36, arguments = 2),
  BLOCK_CRACK("blockcrack_", 37, arguments = 1),
  BLOCK_DUST("blockdust_", 38, arguments = 1),
  WATER_DROP("droplet", 39),
  ITEM_TAKE("take", 40),
  MOB_APPEARANCE("mobappearance", 41, true);
  
  /**
   * Checks if the particle has additional arguments.
   *
   * @return `true` if the particle requires additional arguments, `false` otherwise.
   */
  val hasArguments: Boolean get() = arguments > 0
  
  companion object {
    
    /**
     * Retrieves the Particle enum entry corresponding to the given [id].
     *
     * @param id the ID of the particle to retrieve.
     * @return the Particle enum entry with the specified [id], or throws a [IndexOutOfBoundsException]
     */
    fun byId(id: Int) = entries[id]
    
    /**
     * Retrieves the Particle enum entry corresponding to the given [id].
     *
     * @param id the ID of the particle to retrieve.
     * @return the Particle enum entry with the specified [id], or null if not found.
     */
    fun byIdOrNull(id: Int) = entries.getOrNull(id)
    
  }
  
}
