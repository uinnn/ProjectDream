package dream.entity

import dream.*
import dream.entity.base.Entity
import dream.entity.boss.*
import dream.entity.effect.*
import dream.entity.item.*
import dream.entity.monster.*
import dream.entity.passive.*
import dream.entity.player.Player
import dream.entity.projectile.*
import dream.utils.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf


/**
 * A registry for entities types.
 *
 * You can register your own entity by this.
 */
object Entities : Iterable<EntityType<out Entity>> {

  @JvmField
  val lookup = IntObjectMap<EntityType<out Entity>>()


  @JvmField
  val ITEM = register0(EntityItem::class, 1, "item", "Item")

  @JvmField
  val EXPERIENCE = register0(ExperienceOrb::class, 2, "xp_orb", "XPOrb")

  @JvmField
  val EGG = register0(Egg::class, 7, "egg", "Egg")

  @JvmField
  val LEASH = register0(LeashKnot::class, 8, "leash_knot", "LeashKnot")

  @JvmField
  val PAINTING = register0(Painting::class, 9, "painting", "Painting")

  @JvmField
  val ARROW = register0(Arrow::class, 10, "arrow", "Arrow")

  @JvmField
  val SNOWBAL = register0(Snowball::class, 11, "snowball", "Snowball")

  @JvmField
  val FIREBALL = register0(Fireball::class, 12, "fireball", "Fireball")

  @JvmField
  val SMALL_FIREBALL = register0(SmallFireball::class, 13, "small_fireball", "SmallFireball")

  @JvmField
  val ENDER_PEARL = register0(EnderPearl::class, 14, "ender_pearl", "ThrownEnderpearl")

  @JvmField
  val ENDER_EYE = register0(EnderEye::class, 15, "eye_of_ender_signal", "EyeOfEnderSignal")

  @JvmField
  val POTION = register0(ThrownPotion::class, 16, "potion", "ThrownPotion")

  @JvmField
  val EXP_BOTTLE = register0(ExperienceBottle::class, 17, "xp_bottle", "ThrownExpBottle")

  @JvmField
  val ITEM_FRAME = register0(ItemFrame::class, 18, "item_frame", "ItemFrame")

  @JvmField
  val WITHER_SKULL = register0(WitherSkull::class, 19, "wither_skull", "WitherSkull")

  @JvmField
  val TNT = register0(PrimedTNT::class, 20, "tnt", "PrimedTnt")

  @JvmField
  val FALLING_BLOCK = register0(FallingBlock::class, 21, "falling_block", "FallingSand")

  @JvmField
  val FIREWORK = register0(FireworkRocket::class, 22, "fireworks_rocket", "FireworksRocketEntity")

  @JvmField
  val ARMOR_STAND = register0(ArmorStand::class, 30, "armor_stand", "ArmorStand")

  @JvmField
  val BOAT = register0(Boat::class, 41, "boat", "Boat")

  @JvmField
  val MINECART = register0(RideableMinecart::class, 42, "minecart", "MinecartRideable")

  @JvmField
  val MINECART_COMMAND = register0(MinecartCommandBlock::class, 40, "commandblock_minecart", "MinecartCommandBlock")

  @JvmField
  val MINECART_CHEST = register0(MinecartChest::class, 43, "chest_minecart", "MinecartChest")

  @JvmField
  val MINECART_FURNACE = register0(MinecartFurnace::class, 44, "furnace_minecart", "MinecartFurnace")

  @JvmField
  val MINECART_TNT = register0(MinecartTNT::class, 45, "tnt_minecart", "MinecartTNT")

  @JvmField
  val MINECART_HOPPER = register0(MinecartHopper::class, 46, "hopper_minecart", "MinecartHopper")

  @JvmField
  val MINECART_SPAWNER = register0(MinecartSpawner::class, 47, "spawner_minecart", "MinecartMobSpawner")

  @JvmField
  val ENDER_CRYSTAL = register0(EnderCrystal::class, 200, "ender_crystal", "EnderCrystal")

  @JvmField
  val CREEPER = register0(Creeper::class, 50, "creeper", "Creeper")

  @JvmField
  val SKELETON = register0(Skeleton::class, 51, "skeleton", "Skeleton")

  @JvmField
  val SPIDER = register0(Spider::class, 52, "spider", "Spider")

  @JvmField
  val GIANT = register0(Giant::class, 53, "giant", "Giant")

  @JvmField
  val ZOMBIE = register0(Zombie::class, 54, "zombie", "Zombie")

  @JvmField
  val SLIME = register0(Slime::class, 55, "slime", "Slime")

  @JvmField
  val GHAST = register0(Ghast::class, 56, "ghast", "Ghast")

  @JvmField
  val PIG_ZOMBIE = register0(PigZombie::class, 57, "zombie_pigman", "PigZombie")

  @JvmField
  val ENDERMAN = register0(Enderman::class, 58, "enderman", "Enderman")

  @JvmField
  val CAVE_SPIDER = register0(CaveSpider::class, 59, "cave_spider", "CaveSpider")

  @JvmField
  val SILVERFISH = register0(Silverfish::class, 60, "silverfish", "Silverfish")

  @JvmField
  val BLAZE = register0(Blaze::class, 61, "blaze", "Blaze")

  @JvmField
  val MAGMA_CUBE = register0(MagmaCube::class, 62, "magma_cube", "LavaSlime")

  @JvmField
  val ENDER_DRAGON = register0(EnderDragon::class, 63, "ender_dragon", "EnderDragon")

  @JvmField
  val WITHER = register0(Wither::class, 64, "wither", "WitherBoss")

  @JvmField
  val BAT = register0(Bat::class, 65, "bat", "Bat")

  @JvmField
  val WITCH = register0(Witch::class, 66, "witch", "Witch")

  @JvmField
  val ENDERMITE = register0(Endermite::class, 67, "endermite", "Endermite")

  @JvmField
  val GUARDIAN = register0(Guardian::class, 68, "guardian", "Guardian")

  @JvmField
  val PIG = register0(Pig::class, 90, "pig", "Pig")

  @JvmField
  val SHEEP = register0(Sheep::class, 91, "sheep", "Sheep")

  @JvmField
  val COW = register0(Cow::class, 92, "cow", "Cow")

  @JvmField
  val CHICKEN = register0(Chicken::class, 93, "chicken", "Chicken")

  @JvmField
  val SQUID = register0(Squid::class, 94, "squid", "Squid")

  @JvmField
  val WOLF = register0(Wolf::class, 95, "wolf", "Wolf")

  @JvmField
  val MOOSHROOM_COW = register0(MooshroomCow::class, 96, "mooshroom", "MushroomCow")

  @JvmField
  val SNOW_GOLEM = register0(SnowGolem::class, 97, "snowman", "Snowman")

  @JvmField
  val OCELOT = register0(Ocelot::class, 98, "ocelot", "Ozelot")

  @JvmField
  val IRON_GOLEM = register0(IronGolem::class, 99, "villager_golem", "VillagerGolem")

  @JvmField
  val HORSE = register0(Horse::class, 100, "horse", "EntityHorse")

  @JvmField
  val RABBIT = register0(Rabbit::class, 101, "rabbit", "Rabbit")

  @JvmField
  val VILLAGER = register0(Villager::class, 120, "villager", "Villager")

  @JvmField
  val PLAYER = register0(Player::class, -1, "player", "Player")

  @JvmField
  val LIGHTGNING = register0(LightningBolt::class, -1, "unknown", "LightningBolt")

  @JvmField
  val WEATHER = register0(WeatherEffect::class, -1, "unknown", "Weather")

  @JvmField
  val DRAGON_PART = register0(DragonPart::class, -1, "unknown", "ComplexPart")

  @JvmField
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
