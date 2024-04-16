package dream.entity

import dream.Key
import dream.entity.base.Entity
import dream.entity.boss.DragonPart
import dream.entity.boss.EnderDragon
import dream.entity.boss.Wither
import dream.entity.effect.LightningBolt
import dream.entity.effect.WeatherEffect
import dream.entity.item.*
import dream.entity.monster.*
import dream.entity.passive.*
import dream.entity.player.Player
import dream.entity.projectile.*
import dream.minecraftKey
import dream.utils.IntObjectMap
import dream.utils.cast
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf


/**
 * A registry for entities types.
 *
 * You can register your own entity by this.
 */
object Entities : Iterable<EntityType<out Entity>> {

  val lookup = IntObjectMap<EntityType<out Entity>>()

  val ITEM = register0(EntityItem::class, 1, "item", "Item")
  val EXPERIENCE = register0(ExperienceOrb::class, 2, "xp_orb", "XPOrb")
  val EGG = register0(Egg::class, 7, "egg", "Egg")
  val LEASH = register0(LeashKnot::class, 8, "leash_knot", "LeashKnot")
  val PAINTING = register0(Painting::class, 9, "painting", "Painting")
  val ARROW = register0(Arrow::class, 10, "arrow", "Arrow")
  val SNOWBAL = register0(Snowball::class, 11, "snowball", "Snowball")
  val FIREBALL = register0(Fireball::class, 12, "fireball", "Fireball")
  val SMALL_FIREBALL = register0(SmallFireball::class, 13, "small_fireball", "SmallFireball")
  val ENDER_PEARL = register0(EnderPearl::class, 14, "ender_pearl", "ThrownEnderpearl")
  val ENDER_EYE = register0(EnderEye::class, 15, "eye_of_ender_signal", "EyeOfEnderSignal")
  val POTION = register0(ThrownPotion::class, 16, "potion", "ThrownPotion")
  val EXP_BOTTLE = register0(ExperienceBottle::class, 17, "xp_bottle", "ThrownExpBottle")
  val ITEM_FRAME = register0(ItemFrame::class, 18, "item_frame", "ItemFrame")
  val WITHER_SKULL = register0(WitherSkull::class, 19, "wither_skull", "WitherSkull")
  val TNT = register0(PrimedTNT::class, 20, "tnt", "PrimedTnt")
  val FALLING_BLOCK = register0(FallingBlock::class, 21, "falling_block", "FallingSand")
  val FIREWORK = register0(FireworkRocket::class, 22, "fireworks_rocket", "FireworksRocketEntity")
  val ARMOR_STAND = register0(ArmorStand::class, 30, "armor_stand", "ArmorStand")
  val BOAT = register0(Boat::class, 41, "boat", "Boat")
  val MINECART = register0(RideableMinecart::class, 42, "minecart", "MinecartRideable")
  val MINECART_COMMAND = register0(MinecartCommandBlock::class, 40, "commandblock_minecart", "MinecartCommandBlock")
  val MINECART_CHEST = register0(MinecartChest::class, 43, "chest_minecart", "MinecartChest")
  val MINECART_FURNACE = register0(MinecartFurnace::class, 44, "furnace_minecart", "MinecartFurnace")
  val MINECART_TNT = register0(MinecartTNT::class, 45, "tnt_minecart", "MinecartTNT")
  val MINECART_HOPPER = register0(MinecartHopper::class, 46, "hopper_minecart", "MinecartHopper")
  val MINECART_SPAWNER = register0(MinecartSpawner::class, 47, "spawner_minecart", "MinecartMobSpawner")
  val ENDER_CRYSTAL = register0(EnderCrystal::class, 200, "ender_crystal", "EnderCrystal")
  val CREEPER = register0(Creeper::class, 50, "creeper", "Creeper")
  val SKELETON = register0(Skeleton::class, 51, "skeleton", "Skeleton")
  val SPIDER = register0(Spider::class, 52, "spider", "Spider")
  val GIANT = register0(Giant::class, 53, "giant", "Giant")
  val ZOMBIE = register0(Zombie::class, 54, "zombie", "Zombie")
  val SLIME = register0(Slime::class, 55, "slime", "Slime")
  val GHAST = register0(Ghast::class, 56, "ghast", "Ghast")
  val PIG_ZOMBIE = register0(PigZombie::class, 57, "zombie_pigman", "PigZombie")
  val ENDERMAN = register0(Enderman::class, 58, "enderman", "Enderman")
  val CAVE_SPIDER = register0(CaveSpider::class, 59, "cave_spider", "CaveSpider")
  val SILVERFISH = register0(Silverfish::class, 60, "silverfish", "Silverfish")
  val BLAZE = register0(Blaze::class, 61, "blaze", "Blaze")
  val MAGMA_CUBE = register0(MagmaCube::class, 62, "magma_cube", "LavaSlime")
  val ENDER_DRAGON = register0(EnderDragon::class, 63, "ender_dragon", "EnderDragon")
  val WITHER = register0(Wither::class, 64, "wither", "WitherBoss")
  val BAT = register0(Bat::class, 65, "bat", "Bat")
  val WITCH = register0(Witch::class, 66, "witch", "Witch")
  val ENDERMITE = register0(Endermite::class, 67, "endermite", "Endermite")
  val GUARDIAN = register0(Guardian::class, 68, "guardian", "Guardian")
  val PIG = register0(Pig::class, 90, "pig", "Pig")
  val SHEEP = register0(Sheep::class, 91, "sheep", "Sheep")
  val COW = register0(Cow::class, 92, "cow", "Cow")
  val CHICKEN = register0(Chicken::class, 93, "chicken", "Chicken")
  val SQUID = register0(Squid::class, 94, "squid", "Squid")
  val WOLF = register0(Wolf::class, 95, "wolf", "Wolf")
  val MOOSHROOM_COW = register0(MooshroomCow::class, 96, "mooshroom", "MushroomCow")
  val SNOW_GOLEM = register0(SnowGolem::class, 97, "snowman", "Snowman")
  val OCELOT = register0(Ocelot::class, 98, "ocelot", "Ozelot")
  val IRON_GOLEM = register0(IronGolem::class, 99, "villager_golem", "VillagerGolem")
  val HORSE = register0(Horse::class, 100, "horse", "EntityHorse")
  val RABBIT = register0(Rabbit::class, 101, "rabbit", "Rabbit")
  val VILLAGER = register0(Villager::class, 120, "villager", "Villager")
  val PLAYER = register0(Player::class, -1, "player", "Player")
  val LIGHTGNING = register0(LightningBolt::class, -1, "unknown", "LightningBolt")
  val WEATHER = register0(WeatherEffect::class, -1, "unknown", "Weather")
  val DRAGON_PART = register0(DragonPart::class, -1, "unknown", "ComplexPart")
  val FISH_HOOK = register0(FishHook::class, -1, "unknown", "FishingHook")

  /**
   * Register a new entity type in the server.
   */
  fun <T : EntityType<out Entity>> register(type: T): T {
    lookup.put(type.id, type)
    return type
  }

  private fun <T : Entity> register0(type: KClass<T>, id: Int, key: String, name: String): EntityType<T> {
    return register(EntityType(type, id, minecraftKey(key), name))
  }

  /**
   * Gets a type by id.
   */
  fun byId(id: Int): EntityType<out Entity>? {
    return lookup.get(id)
  }

  /**
   * Gets a type by class.
   */
  fun <T : Entity> byClass(clazz: KClass<T>): EntityType<T>? {
    return find { clazz.isSubclassOf(it.type) }.cast()
  }

  /**
   * Gets a type by class.
   */
  inline fun <reified T : Entity> byClass(): EntityType<T>? {
    return find { it.type is T }.cast()
  }

  /**
   * Gets a type by key.
   */
  fun byKey(key: Key): EntityType<out Entity>? {
    return find { it.key == key }
  }

  /**
   * Gets a type by name.
   */
  fun byName(name: String): EntityType<out Entity>? {
    return find { it.name == name }
  }

  override fun iterator(): Iterator<EntityType<out Entity>> {
    return lookup.values.iterator()
  }
}
