package dream.entity

/*

/**
 * A registry for entities types.
 *
 * You can register your own entity by this.
 */
object Entities : HashSet<EntityType<out Entity>>(64) {
    
    val ID_LOOKUP = IntObjectMap<EntityType<out Entity>>()
    
    
    @JvmField
    val ITEM = register(EntityType(EntityItem::class, 1, key("minecraft:item"), "Item"))
    @JvmField
    val EXPERIENCE = register(EntityType(2, key("minecraft:xp_orb"), "XPOrb"))
    @JvmField
    val EGG = register(ProjectileType(7, key("minecraft:egg"), "Egg"))
    @JvmField
    val LEASH = register(EntityType(8, key("minecraft:leash_knot"), "LeashKnot"))
    @JvmField
    val PAINTING = register(EntityType(9, key("minecraft:painting"), "Painting"))
    @JvmField
    val ARROW = register(ProjectileType(10, key("minecraft:arrow"), "Arrow"))
    @JvmField
    val SNOWBAL = register(ProjectileType(11, key("minecraft:snowball"), "Snowball"))
    @JvmField
    val FIREBALL = register(ProjectileType(12, key("minecraft:fireball"), "Fireball"))
    @JvmField
    val SMALL_FIREBALL = register(ProjectileType(13, key("minecraft:small_fireball"), "SmallFireball"))
    @JvmField
    val ENDER_PEARL = register(ProjectileType(14, key("minecraft:ender_pearl"), "ThrownEnderpearl"))
    @JvmField
    val ENDER_SIGNAL = register(ProjectileType(15, key("minecraft:eye_of_ender_signal"), "EyeOfEnderSignal"))
    @JvmField
    val POTION = register(ProjectileType(16, key("minecraft:potion"), "ThrownPotion"))
    @JvmField
    val EXP_BOTTLE = register(ProjectileType(17, key("minecraft:xp_bottle"), "ThrownExpBottle"))
    @JvmField
    val ITEM_FRAME = register(EntityType(18, key("minecraft:item_frame"), "ItemFrame"))
    @JvmField
    val WITHER_SKULL = register(ProjectileType(19, key("minecraft:wither_skull"), "WitherSkull"))
    @JvmField
    val TNT = register(EntityType(20, key("minecraft:tnt"), "PrimedTnt"))
    @JvmField
    val FALLING_BLOCK = register(EntityType(21, key("minecraft:falling_block"), "FallingSand"))
    @JvmField
    val FIREWORK = register(EntityType(22, key("minecraft:fireworks_rocket"), "FireworksRocketEntity"))
    @JvmField
    val ARMOR_STAND = register(EntityType(30, key("minecraft:armor_stand"), "ArmorStand"))
    @JvmField
    val BOAT = register(EntityType(41, key("minecraft:boat"), "Boat"))
    @JvmField
    val MINECART = register(EntityType(42, key("minecraft:minecart"), "MinecartRideable"))
    @JvmField
    val MINECART_COMMAND = register(EntityType(40, key("minecraft:commandblock_minecart"), "MinecartCommandBlock"))
    @JvmField
    val MINECART_CHEST = register(EntityType(43, key("minecraft:chest_minecart"), "MinecartChest"))
    @JvmField
    val MINECART_FURNACE = register(EntityType(44, key("minecraft:furnace_minecart"), "MinecartFurnace"))
    @JvmField
    val MINECART_TNT = register(EntityType(45, key("minecraft:tnt_minecart"), "MinecartTNT"))
    @JvmField
    val MINECART_HOPPER = register(EntityType(46, key("minecraft:hopper_minecart"), "MinecartHopper"))
    @JvmField
    val MINECART_SPAWNER = register(EntityType(47, key("minecraft:spawner_minecart"), "MinecartMobSpawner"))
    @JvmField
    val ENDER_CRYSTAL = register(EntityType(200, key("minecraft:ender_crystal"), "EnderCrystal"))
    @JvmField
    val CREEPER = register(CreatureType(50, key("minecraft:creeper"), "Creeper", true))
    @JvmField
    val SKELETON = register(CreatureType(51, key("minecraft:skeleton"), "Skeleton", true))
    @JvmField
    val SPIDER = register(CreatureType(52, key("minecraft:spider"), "Spider", true))
    @JvmField
    val GIANT = register(CreatureType(53, key("minecraft:giant"), "Giant", true))
    @JvmField
    val ZOMBIE = register(CreatureType(54, key("minecraft:zombie"), "Zombie", true))
    @JvmField
    val SLIME = register(CreatureType(55, key("minecraft:slime"), "Slime", true))
    @JvmField
    val GHAST = register(CreatureType(56, key("minecraft:ghast"), "Ghast", true))
    @JvmField
    val PIG_ZOMBIE = register(CreatureType(57, key("minecraft:zombie_pigman"), "PigZombie", true))
    @JvmField
    val ENDERMAN = register(CreatureType(58, key("minecraft:enderman"), "Enderman", true))
    @JvmField
    val CAVE_SPIDER = register(CreatureType(59, key("minecraft:cave_spider"), "CaveSpider", true))
    @JvmField
    val SILVERFISH = register(CreatureType(60, key("minecraft:silverfish"), "Silverfish", true))
    @JvmField
    val BLAZE = register(CreatureType(61, key("minecraft:blaze"), "Blaze", true))
    @JvmField
    val MAGMA_CUBE = register(CreatureType(62, key("minecraft:magma_cube"), "LavaSlime", true))
    @JvmField
    val ENDER_DRAGON = register(CreatureType(63, key("minecraft:ender_dragon"), "EnderDragon", true))
    @JvmField
    val WITHER = register(CreatureType(64, key("minecraft:wither"), "WitherBoss", true))
    @JvmField
    val BAT = register(CreatureType(65, key("minecraft:bat"), "Bat", true))
    @JvmField
    val WITCH = register(CreatureType(66, key("minecraft:witch"), "Witch", true))
    @JvmField
    val ENDERMITE = register(CreatureType(67, key("minecraft:endermite"), "Endermite", true))
    @JvmField
    val GUARDIAN = register(CreatureType(68, key("minecraft:guardian"), "Guardian", true))
    @JvmField
    val PIG = register(AnimalType(90, key("minecraft:pig"), "Pig", true))
    @JvmField
    val SHEEP = register(AnimalType(91, key("minecraft:sheep"), "Sheep", true))
    @JvmField
    val COW = register(AnimalType(92, key("minecraft:cow"), "Cow", true))
    @JvmField
    val CHICKEN = register(AnimalType(93, key("minecraft:chicken"), "Chicken", true))
    @JvmField
    val SQUID = register(WaterCreatureType(94, key("minecraft:squid"), "Squid", true))
    @JvmField
    val WOLF = register(AnimalType(95, key("minecraft:wolf"), "Wolf", true))
    @JvmField
    val MUSHROOM_COW = register(AnimalType(96, key("minecraft:mooshroom"), "MushroomCow", true))
    @JvmField
    val SNOWMAN = register(AnimalType(97, key("minecraft:snowman"), "Snowman", true))
    @JvmField
    val OCELOT = register(AnimalType(98, key("minecraft:ocelot"), "Ozelot", true))
    @JvmField
    val GOLEM = register(CreatureType(99, key("minecraft:villager_golem"), "VillagerGolem", true))
    @JvmField
    val HORSE = register(AnimalType(100, key("minecraft:horse"), "EntityHorse", true))
    @JvmField
    val RABBIT = register(AnimalType(101, key("minecraft:rabbit"), "Rabbit", true))
    @JvmField
    val VILLAGER = register(CreatureType(120, key("minecraft:villager"), "Villager", true))
    @JvmField
    val PLAYER = register(CreatureType(-1, key("minecraft:player"), "Player", true))
    @JvmField
    val LIGHTGNING = register(EntityType(-1, key("minecraft:unknown"), "LightningBolt"))
    @JvmField
    val WEATHER = register(EntityType(-1, key("minecraft:unknown"), "Weather"))
    @JvmField
    val COMPLEX_PART = register(EntityType(-1, key("minecraft:unknown"), "ComplexPart"))
    @JvmField
    val FISHING_HOOK = register(EntityType(-1, key("minecraft:unknown"), "FishingHook"))
    
    /**
     * Register a new entity type in the server.
     */
    fun <T : EntityType<out Entity>> register(type: T): T {
        add(type)
        return type
    }
}

 */
