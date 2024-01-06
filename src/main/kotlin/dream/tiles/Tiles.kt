package dream.tiles

import dream.*
import dream.registry.KeyRegistry

/**
 * Represents a registry for tile entities.
 */
object Tiles : KeyRegistry<TileType<out Tile>>() {

  @JvmField
  val CHEST = register0("chest", ::TileBanner)
  @JvmField
  val BANNER = register0("banner", ::TileBanner)
  @JvmField
  val COMPARATOR = register0("comparator", ::TileComparator)
  @JvmField
  val DAYLIGHT_SENSOR = register0("daylight_sensor", ::TileDaylightSensor)
  @JvmField
  val BEACON = register0("beacon", ::TileBeacon)
  @JvmField
  val BREWING_STAND = register0("brewing_stand", ::TileBrewingStand)
  @JvmField
  val COMMAND_BLOCK = register0("command_block", ::TileCommandBlock)
  @JvmField
  val DISPENSER = register0("dispenser", ::TileBeacon)
  @JvmField
  val DROPPER = register0("dropper", ::TileBeacon)
  @JvmField
  val ENCHANTMENT_TABLE = register0("enchantment_table", ::TileBeacon)
  @JvmField
  val ENDER_CHEST = register0("ender_chest", ::TileEnderChest)
  @JvmField
  val END_PORTAL = register0("end_portal", ::TileEndPortal)
  @JvmField
  val FLOWER_POT = register0("flower_pot", ::TileFlowerPot)
  @JvmField
  val FURNACE = register0("furnace", ::TileFurnace)
  @JvmField
  val HOPPER = register0("hopper", ::TileBeacon)
  @JvmField
  val SPAWNER = register0("mob_spawner", ::TileSpawner)
  @JvmField
  val NOTE = register0("note", ::TileNote)
  @JvmField
  val PISTON = register0("piston", ::TilePiston)
  @JvmField
  val SIGN = register0("sign", ::TileSign)
  @JvmField
  val SKULL = register0("skull", ::TileBeacon)

  fun createOrNull(key: Key) = get(key)?.create()
  fun create(key: Key) = get(key)!!.create()

  fun <T : Tile> register(type: TileType<T>): TileType<T> {
    put(type.key, type)
    return type
  }

  fun <T : Tile> register(key: Key, factory: TileSupplier<T>): TileType<T> {
    return register(TileType(key, factory))
  }

  fun <T : Tile> register(key: String, factory: TileSupplier<T>): TileType<T> {
    return register(TileType(key(key), factory))
  }

  private fun <T : Tile> register0(key: String, factory: TileSupplier<T>): TileType<T> {
    return register(key("minecraft", key), factory)
  }
}
