package dream

import dream.entity.player.Player
import dream.packet.game.SPacketSound
import dream.pos.Pos

/**
 * Represents all sounds category
 */
enum class SoundCategory {
  AMBIENCE,
  HURT,
  FALL,
  DIG,
  STEP,
  FIRE,
  FIREWORK,
  SWIM,
  LIQUID,
  MINECART,
  PORTAL,
  NOTE,
  PISTON,
  RANDOM,
  RECORD,
  MUSIC,
  MOBS
}

/**
 * Represents a sound in the game.
 */
class Sound(val name: String, val category: SoundCategory) {

  /**
   * Creates packet for this sound.
   */
  fun createPacket(x: Int, y: Int, z: Int, volume: Float, pitch: Float): SPacketSound {
    return SPacketSound(this, x, y, z, volume, pitch)
  }

  /**
   * Creates packet for this sound.
   */
  fun createPacket(pos: Pos, volume: Float, pitch: Float): SPacketSound {
    return SPacketSound(this, pos.flooredX, pos.flooredY, pos.flooredZ, volume, pitch)
  }

  /**
   * Plays this sound to [player].
   */
  fun play(player: Player, volume: Float = 1f, pitch: Float = 1f) {
    player.sendPacket(createPacket(player.x.toInt(), player.y.toInt(), player.z.toInt(), volume, pitch))
  }

  /**
   * Plays this sound to all [players].
   */
  fun playAll(players: Iterable<Player>, volume: Float = 1f, pitch: Float = 1f) {
    for (player in players) {
      play(player, volume, pitch)
    }
  }

  companion object : Iterable<Sound> {
    private val SOUNDS = ArrayList<Sound>(222)

    // ambience
    @JvmField
    val AMBIENCE_CAVE = register("ambient.cave.cave", SoundCategory.AMBIENCE)
    @JvmField
    val AMBIENCE_RAIN = register("ambient.weather.rain", SoundCategory.AMBIENCE)
    @JvmField
    val AMBIENCE_THUNDER = register("ambient.weather.thunder", SoundCategory.AMBIENCE)

    // hurt
    @JvmField
    val HURT_PLAYER = register("game.player.hurt", SoundCategory.HURT)
    @JvmField
    val HURT_NEUTRAL = register("game.neutral.hurt", SoundCategory.HURT)
    @JvmField
    val HURT_HOSTILE = register("game.hostile.hurt", SoundCategory.HURT)

    // fall
    @JvmField
    val FALL_BIG_PLAYER = register("game.player.hurt.fall.big", SoundCategory.FALL)
    @JvmField
    val FALL_BIG_NEUTRAL = register("game.neutral.hurt.fall.big", SoundCategory.FALL)
    @JvmField
    val FALL_BIG_HOSTILE = register("game.hostile.hurt.fall.big", SoundCategory.FALL)
    @JvmField
    val FALL_SMALL_PLAYER = register("game.player.hurt.fall.small", SoundCategory.FALL)
    @JvmField
    val FALL_SMALL_NEUTRAL = register("game.neutral.hurt.fall.small", SoundCategory.FALL)
    @JvmField
    val FALL_SMALL_HOSTILE = register("game.hostile.hurt.fall.small", SoundCategory.FALL)

    // dig
    @JvmField
    val DIG_WOOL = register("dig.cloth", SoundCategory.DIG)
    @JvmField
    val DIG_GRASS = register("dig.grass", SoundCategory.DIG)
    @JvmField
    val DIG_GRAVEL = register("dig.gravel", SoundCategory.DIG)
    @JvmField
    val DIG_SAND = register("dig.sand", SoundCategory.DIG)
    @JvmField
    val DIG_SNOW = register("dig.snow", SoundCategory.DIG)
    @JvmField
    val DIG_STONE = register("dig.stone", SoundCategory.DIG)
    @JvmField
    val DIG_WOOD = register("dig.wood", SoundCategory.DIG)
    @JvmField
    val DIG_GLASS = register("dig.glass", SoundCategory.DIG)

    // step
    @JvmField
    val STEP_WOOL = register("step.cloth", SoundCategory.STEP)
    @JvmField
    val STEP_GRASS = register("step.grass", SoundCategory.STEP)
    @JvmField
    val STEP_GRAVEL = register("step.gravel", SoundCategory.STEP)
    @JvmField
    val STEP_LADDER = register("step.ladder", SoundCategory.STEP)
    @JvmField
    val STEP_SAND = register("step.sand", SoundCategory.STEP)
    @JvmField
    val STEP_SNOW = register("step.snow", SoundCategory.STEP)
    @JvmField
    val STEP_STONE = register("step.stone", SoundCategory.STEP)
    @JvmField
    val STEP_WOOD = register("step.wood", SoundCategory.STEP)

    // fire
    @JvmField
    val FIRE = register("fire.fire", SoundCategory.FIRE)
    @JvmField
    val FIRE_IGNITE = register("fire.ignite", SoundCategory.FIRE)
    @JvmField
    val FIRE_CHARGE_USE = register("item.fireCharge.use", SoundCategory.FIRE)

    // fireworks
    @JvmField
    val FIREWORK_BLAST = register("fireworks.blast", SoundCategory.FIREWORK)
    @JvmField
    val FIREWORK_FAR_BLAST = register("fireworks.blast_far", SoundCategory.FIREWORK)
    @JvmField
    val FIREWORK_LARGE_BLAST = register("fireworks.largeBlast", SoundCategory.FIREWORK)
    @JvmField
    val FIREWORK_FAR_LARGE_BLAST = register("fireworks.largeBlast_far", SoundCategory.FIREWORK)
    @JvmField
    val FIREWORK_TWINKLE = register("fireworks.twinkle", SoundCategory.FIREWORK)
    @JvmField
    val FIREWORK_FAR_TWINKLE = register("fireworks.twinkle_far", SoundCategory.FIREWORK)
    @JvmField
    val FIREWORK_LAUNCH = register("fireworks.launch", SoundCategory.FIREWORK)

    // swim
    @JvmField
    val SWIM_PLAYER = register("game.player.swim", SoundCategory.SWIM)
    @JvmField
    val SWIM_NEUTRAL = register("game.neutral.swim", SoundCategory.SWIM)
    @JvmField
    val SWIM_HOSTILE = register("game.hostile.swim", SoundCategory.SWIM)
    @JvmField
    val SWIM_SPLASH_PLAYER = register("game.player.swim.splash", SoundCategory.SWIM)
    @JvmField
    val SWIM_SPLASH_NEUTRAL = register("game.neutral.swim.splash", SoundCategory.SWIM)
    @JvmField
    val SWIM_SPLASH_HOSTILE = register("game.hostile.swim.splash", SoundCategory.SWIM)

    // liquid
    @JvmField
    val WATER = register("liquid.water", SoundCategory.LIQUID)
    @JvmField
    val LAVA = register("liquid.lava", SoundCategory.LIQUID)
    @JvmField
    val LAVA_POP = register("liquid.lavapop", SoundCategory.LIQUID)

    // minecart
    @JvmField
    val MINECART_BASE = register("minecart.base", SoundCategory.MINECART)
    @JvmField
    val MINECART_INSIDE = register("minecart.inside", SoundCategory.MINECART)

    // portal
    @JvmField
    val PORTAL = register("portal.portal", SoundCategory.PORTAL)
    @JvmField
    val PORTAL_TRAVEL = register("portal.travel", SoundCategory.PORTAL)
    @JvmField
    val PORTAL_TRIGGER = register("portal.trigger", SoundCategory.PORTAL)

    // note
    @JvmField
    val NOTE_BASS = register("note.bass", SoundCategory.NOTE)
    @JvmField
    val NOTE_BASS_GUITAR = register("note.bassattack", SoundCategory.NOTE)
    @JvmField
    val NOTE_BASS_DRUM = register("note.bd", SoundCategory.NOTE)
    @JvmField
    val NOTE_PIANO = register("note.harp", SoundCategory.NOTE)
    @JvmField
    val NOTE_STICKS = register("note.hat", SoundCategory.NOTE)
    @JvmField
    val NOTE_PLING = register("note.pling", SoundCategory.NOTE)
    @JvmField
    val NOTE_SNARE_DRUM = register("note.snare", SoundCategory.NOTE)

    // tiles
    @JvmField
    val PISTON_EXTEND = register("tile.piston.out", SoundCategory.PISTON)
    @JvmField
    val PISTON_CONTRACT = register("tile.piston.in", SoundCategory.PISTON)

    // random
    @JvmField
    val ANVIL_BREAK = register("random.anvil_break", SoundCategory.RANDOM)
    @JvmField
    val ANVIL_LAND = register("random.anvil_land", SoundCategory.RANDOM)
    @JvmField
    val ANVIL_USE = register("random.anvil_use", SoundCategory.RANDOM)
    @JvmField
    val SHOOT_ARROW = register("random.bow", SoundCategory.RANDOM)
    @JvmField
    val ARROW_HIT = register("random.bowhit", SoundCategory.RANDOM)
    @JvmField
    val ITEM_BREAK = register("random.break", SoundCategory.RANDOM)
    @JvmField
    val BURP = register("random.burp", SoundCategory.RANDOM)
    @JvmField
    val CHEST_CLOSE = register("random.chestclosed", SoundCategory.RANDOM)
    @JvmField
    val CHEST_OPEN = register("random.chestopen", SoundCategory.RANDOM)
    @JvmField
    val GUI_CLICK = register("gui.button.press", SoundCategory.RANDOM)
    @JvmField
    val CLICK = register("random.click", SoundCategory.RANDOM)
    @JvmField
    val DOOR_CLOSE = register("random.door_close", SoundCategory.RANDOM)
    @JvmField
    val DOOR_OPEN = register("random.door_open", SoundCategory.RANDOM)
    @JvmField
    val DRINK = register("random.drink", SoundCategory.RANDOM)
    @JvmField
    val EAT = register("random.eat", SoundCategory.RANDOM)
    @JvmField
    val EXPLODE = register("random.explode", SoundCategory.RANDOM)
    @JvmField
    val FIZZ = register("random.fizz", SoundCategory.RANDOM)
    @JvmField
    val FUSE = register("game.tnt.primed", SoundCategory.RANDOM)
    @JvmField
    val CREEPER_FUSE = register("creeper.primed", SoundCategory.RANDOM)
    @JvmField
    val GLASS = register("random.glass", SoundCategory.RANDOM)
    @JvmField
    val LEVEL_UP = register("random.levelup", SoundCategory.RANDOM)
    @JvmField
    val ORB_PICKUP = register("random.orb", SoundCategory.RANDOM)
    @JvmField
    val ITEM_PICKUP = register("random.pop", SoundCategory.RANDOM)
    @JvmField
    val SPLASH = register("random.splash", SoundCategory.RANDOM)
    @JvmField
    val SUCCESSFUL_HIT = register("random.successful_hit", SoundCategory.RANDOM)
    @JvmField
    val WOOD_CLICK = register("random.wood_click", SoundCategory.RANDOM)

    // record
    @JvmField
    val RECORD_11 = register("records.11", SoundCategory.RECORD)
    @JvmField
    val RECORD_13 = register("records.13", SoundCategory.RECORD)
    @JvmField
    val RECORD_BLOCKS = register("records.blocks", SoundCategory.RECORD)
    @JvmField
    val RECORD_CAT = register("records.cat", SoundCategory.RECORD)
    @JvmField
    val RECORD_CHIRP = register("records.chirp", SoundCategory.RECORD)
    @JvmField
    val RECORD_FAR = register("records.far", SoundCategory.RECORD)
    @JvmField
    val RECORD_MALL = register("records.mall", SoundCategory.RECORD)
    @JvmField
    val RECORD_MELLOHI = register("records.mellohi", SoundCategory.RECORD)
    @JvmField
    val RECORD_STAL = register("records.stal", SoundCategory.RECORD)
    @JvmField
    val RECORD_STRAD = register("records.strad", SoundCategory.RECORD)
    @JvmField
    val RECORD_WAIT = register("records.wait", SoundCategory.RECORD)
    @JvmField
    val RECORD_WARD = register("records.ward", SoundCategory.RECORD)

    // music
    @JvmField
    val MUSIC_MENU = register("music.menu", SoundCategory.MUSIC)
    @JvmField
    val MUSIC_GAME = register("music.game", SoundCategory.MUSIC)
    @JvmField
    val MUSIC_CREATIVE = register("music.game.creative", SoundCategory.MUSIC)
    @JvmField
    val MUSIC_END = register("music.game.end", SoundCategory.MUSIC)
    @JvmField
    val MUSIC_END_DRAGON = register("music.game.end.dragon", SoundCategory.MUSIC)
    @JvmField
    val MUSIC_CREDITS = register("music.game.end.credits", SoundCategory.MUSIC)
    @JvmField
    val MUSIC_NETHER = register("music.game.nether", SoundCategory.MUSIC)

    // mobs
    @JvmField
    val BAT_DEATH = register("mob.bat.death", SoundCategory.MOBS)
    @JvmField
    val BAT_HURT = register("mob.bat.hurt", SoundCategory.MOBS)
    @JvmField
    val BAT_IDLE = register("mob.bat.idle", SoundCategory.MOBS)
    @JvmField
    val BAT_LOOP = register("mob.bat.loop", SoundCategory.MOBS)
    @JvmField
    val BAT_TAKEOFF = register("mob.bat.takeoff", SoundCategory.MOBS)

    @JvmField
    val BLAZE_BREATH = register("mob.blaze.breathe", SoundCategory.MOBS)
    @JvmField
    val BLAZE_DEATH = register("mob.blaze.death", SoundCategory.MOBS)
    @JvmField
    val BLAZE_HIT = register("mob.blaze.hit", SoundCategory.MOBS)

    @JvmField
    val CAT_HISS = register("mob.cat.hiss", SoundCategory.MOBS)
    @JvmField
    val CAT_HIT = register("mob.cat.hitt", SoundCategory.MOBS)
    @JvmField
    val CAT_MEOW = register("mob.cat.meow", SoundCategory.MOBS)
    @JvmField
    val CAT_PURR = register("mob.cat.purr", SoundCategory.MOBS)
    @JvmField
    val CAT_PURREOW = register("mob.cat.purreow", SoundCategory.MOBS)

    @JvmField
    val COW_HURT = register("mob.cow.hurt", SoundCategory.MOBS)
    @JvmField
    val COW_IDLE = register("mob.cow.say", SoundCategory.MOBS)
    @JvmField
    val COW_WALK = register("mob.cow.step", SoundCategory.MOBS)

    @JvmField
    val CREEPER_DEATH = register("mob.creeper.death", SoundCategory.MOBS)
    @JvmField
    val CREEPER_HISS = register("mob.creeper.say", SoundCategory.MOBS)

    @JvmField
    val ENDERDRAGON_DEATH = register("mob.enderdragon.end", SoundCategory.MOBS)
    @JvmField
    val ENDERDRAGON_GROWL = register("mob.enderdragon.growl", SoundCategory.MOBS)
    @JvmField
    val ENDERDRAGON_HIT = register("mob.enderdragon.hit", SoundCategory.MOBS)
    @JvmField
    val ENDERDRAGON_WINGS = register("mob.enderdragon.wings", SoundCategory.MOBS)

    @JvmField
    val ENDERMAN_DEATH = register("mob.endermen.death", SoundCategory.MOBS)
    @JvmField
    val ENDERMAN_HIT = register("mob.endermen.hit", SoundCategory.MOBS)
    @JvmField
    val ENDERMAN_IDLE = register("mob.endermen.idle", SoundCategory.MOBS)
    @JvmField
    val ENDERMAN_TELEPORT = register("mob.endermen.portal", SoundCategory.MOBS)
    @JvmField
    val ENDERMAN_SCREAM = register("mob.endermen.scream", SoundCategory.MOBS)
    @JvmField
    val ENDERMAN_STARE = register("mob.endermen.stare", SoundCategory.MOBS)

    @JvmField
    val GHAST_CHARGE = register("mob.ghast.charge", SoundCategory.MOBS)
    @JvmField
    val GHAST_DEATH = register("mob.ghast.death", SoundCategory.MOBS)
    @JvmField
    val GHAST_FIREBALL = register("mob.ghast.fireball", SoundCategory.MOBS)
    @JvmField
    val GHAST_MOAN = register("mob.ghast.moan", SoundCategory.MOBS)
    @JvmField
    val GHAST_SCREAM = register("mob.ghast.scream", SoundCategory.MOBS)
    @JvmField
    val GHAST_SCREAM_AFFECTIONATE = register("mob.ghast.affectionate_scream", SoundCategory.MOBS)

    @JvmField
    val HORSE_ANGRY = register("mob.horse.angry", SoundCategory.MOBS)
    @JvmField
    val HORSE_ARMOR = register("mob.horse.armor", SoundCategory.MOBS)
    @JvmField
    val HORSE_BREATHE = register("mob.horse.breathe", SoundCategory.MOBS)
    @JvmField
    val HORSE_DEATH = register("mob.horse.death", SoundCategory.MOBS)
    @JvmField
    val HORSE_GALLOP = register("mob.horse.gallop", SoundCategory.MOBS)
    @JvmField
    val HORSE_HIT = register("mob.horse.hit", SoundCategory.MOBS)
    @JvmField
    val HORSE_IDLE = register("mob.horse.idle", SoundCategory.MOBS)
    @JvmField
    val HORSE_JUMP = register("mob.horse.jump", SoundCategory.MOBS)
    @JvmField
    val HORSE_LAND = register("mob.horse.land", SoundCategory.MOBS)
    @JvmField
    val HORSE_SADDLE = register("mob.horse.leather", SoundCategory.MOBS)
    @JvmField
    val HORSE_SOFT = register("mob.horse.soft", SoundCategory.MOBS)
    @JvmField
    val HORSE_WOOD = register("mob.horse.wood", SoundCategory.MOBS)

    @JvmField
    val DONKEY_ANGRY = register("mob.horse.donkey.angry", SoundCategory.MOBS)
    @JvmField
    val DONKEY_DEATH = register("mob.horse.donkey.death", SoundCategory.MOBS)
    @JvmField
    val DONKEY_HIT = register("mob.horse.donkey.hit", SoundCategory.MOBS)
    @JvmField
    val DONKEY_IDLE = register("mob.horse.donkey.idle", SoundCategory.MOBS)

    @JvmField
    val HORSE_SKELETON_DEATH = register("mob.horse.skeleton.death", SoundCategory.MOBS)
    @JvmField
    val HORSE_SKELETON_HIT = register("mob.horse.skeleton.hit", SoundCategory.MOBS)
    @JvmField
    val HORSE_SKELETON_IDLE = register("mob.horse.skeleton.idle", SoundCategory.MOBS)

    @JvmField
    val HORSE_ZOMBIE_DEATH = register("mob.horse.zombie.death", SoundCategory.MOBS)
    @JvmField
    val HORSE_ZOMBIE_HIT = register("mob.horse.zombie.hit", SoundCategory.MOBS)
    @JvmField
    val HORSE_ZOMBIE_IDLE = register("mob.horse.zombie.idle", SoundCategory.MOBS)

    @JvmField
    val IRONGOLEM_DEATH = register("mob.irongolem.death", SoundCategory.MOBS)
    @JvmField
    val IRONGOLEM_HIT = register("mob.irongolem.hit", SoundCategory.MOBS)
    @JvmField
    val IRONGOLEM_THROW = register("mob.irongolem.throw", SoundCategory.MOBS)
    @JvmField
    val IRONGOLEM_WALK = register("mob.irongolem.walk", SoundCategory.MOBS)

    @JvmField
    val MAGMACUBE_STEP_SMALL = register("mob.magmacube.small", SoundCategory.MOBS)
    @JvmField
    val MAGMACUBE_STEP_BIG = register("mob.magmacube.big", SoundCategory.MOBS)
    @JvmField
    val MAGMACUBE_JUMP = register("mob.magmacube.jump", SoundCategory.MOBS)

    @JvmField
    val PIG_IDLE = register("mob.pig.say", SoundCategory.MOBS)
    @JvmField
    val PIG_DEATH = register("mob.pig.death", SoundCategory.MOBS)
    @JvmField
    val PIG_WALK = register("mob.pig.step", SoundCategory.MOBS)

    @JvmField
    val SHEEP_IDLE = register("mob.sheep.say", SoundCategory.MOBS)
    @JvmField
    val SHEEP_SHEAR = register("mob.sheep.shear", SoundCategory.MOBS)
    @JvmField
    val SHEEP_WALK = register("mob.sheep.step", SoundCategory.MOBS)

    @JvmField
    val SILVERFISH_HIT = register("mob.silverfish.hit", SoundCategory.MOBS)
    @JvmField
    val SILVERFISH_KILL = register("mob.silverfish.kill", SoundCategory.MOBS)
    @JvmField
    val SILVERFISH_IDLE = register("mob.silverfish.say", SoundCategory.MOBS)
    @JvmField
    val SILVERFISH_WALK = register("mob.silverfish.step", SoundCategory.MOBS)

    @JvmField
    val SKELETON_IDLE = register("mob.skeleton.say", SoundCategory.MOBS)
    @JvmField
    val SKELETON_DEATH = register("mob.skeleton.death", SoundCategory.MOBS)
    @JvmField
    val SKELETON_HURT = register("mob.skeleton.hurt", SoundCategory.MOBS)
    @JvmField
    val SKELETON_WALK = register("mob.skeleton.step", SoundCategory.MOBS)

    @JvmField
    val SLIME_ATTACK = register("mob.slime.attack", SoundCategory.MOBS)
    @JvmField
    val SLIME_STEP_SMALL = register("mob.slime.small", SoundCategory.MOBS)
    @JvmField
    val SLIME_STEP_BIG = register("mob.slime.big", SoundCategory.MOBS)

    @JvmField
    val SPIDER_IDLE = register("mob.spider.say", SoundCategory.MOBS)
    @JvmField
    val SPIDER_DEATH = register("mob.spider.death", SoundCategory.MOBS)
    @JvmField
    val SPIDER_WALK = register("mob.spider.step", SoundCategory.MOBS)

    @JvmField
    val VILLAGER_DEATH = register("mob.villager.death", SoundCategory.MOBS)
    @JvmField
    val VILLAGER_HAGGLE = register("mob.villager.haggle", SoundCategory.MOBS)
    @JvmField
    val VILLAGER_HIT = register("mob.villager.hit", SoundCategory.MOBS)
    @JvmField
    val VILLAGER_IDLE = register("mob.villager.idle", SoundCategory.MOBS)
    @JvmField
    val VILLAGER_NO = register("mob.villager.no", SoundCategory.MOBS)
    @JvmField
    val VILLAGER_YES = register("mob.villager.yes", SoundCategory.MOBS)

    @JvmField
    val WITHER_DEATH = register("mob.wither.death", SoundCategory.MOBS)
    @JvmField
    val WITHER_HURT = register("mob.wither.hurt", SoundCategory.MOBS)
    @JvmField
    val WITHER_IDLE = register("mob.wither.idle", SoundCategory.MOBS)
    @JvmField
    val WITHER_SHOOT = register("mob.wither.shoot", SoundCategory.MOBS)
    @JvmField
    val WITHER_SPAWN = register("mob.wither.spawn", SoundCategory.MOBS)

    @JvmField
    val WOLF_BARK = register("mob.wolf.bark", SoundCategory.MOBS)
    @JvmField
    val WOLF_DEATH = register("mob.wolf.death", SoundCategory.MOBS)
    @JvmField
    val WOLF_GROWL = register("mob.wolf.growl", SoundCategory.MOBS)
    @JvmField
    val WOLF_HOWL = register("mob.wolf.howl", SoundCategory.MOBS)
    @JvmField
    val WOLF_HURT = register("mob.wolf.hurt", SoundCategory.MOBS)
    @JvmField
    val WOLF_PANT = register("mob.wolf.panting", SoundCategory.MOBS)
    @JvmField
    val WOLF_SHAKE = register("mob.wolf.shake", SoundCategory.MOBS)
    @JvmField
    val WOLF_WALK = register("mob.wolf.step", SoundCategory.MOBS)
    @JvmField
    val WOLF_WHINE = register("mob.wolf.whine", SoundCategory.MOBS)

    @JvmField
    val ZOMBIE_METAL = register("mob.zombie.metal", SoundCategory.MOBS)
    @JvmField
    val ZOMBIE_WOOD = register("mob.zombie.wood", SoundCategory.MOBS)
    @JvmField
    val ZOMBIE_WOODBREAK = register("mob.zombie.woodbreak", SoundCategory.MOBS)
    @JvmField
    val ZOMBIE_IDLE = register("mob.zombie.say", SoundCategory.MOBS)
    @JvmField
    val ZOMBIE_DEATH = register("mob.zombie.death", SoundCategory.MOBS)
    @JvmField
    val ZOMBIE_HURT = register("mob.zombie.hurt", SoundCategory.MOBS)
    @JvmField
    val ZOMBIE_INFECT = register("mob.zombie.infect", SoundCategory.MOBS)
    @JvmField
    val ZOMBIE_UNFECT = register("mob.zombie.unfect", SoundCategory.MOBS)
    @JvmField
    val ZOMBIE_REMEDY = register("mob.zombie.remedy", SoundCategory.MOBS)
    @JvmField
    val ZOMBIE_WALK = register("mob.zombie.step", SoundCategory.MOBS)

    @JvmField
    val ZOMBIE_PIG_IDLE = register("mob.zombiepig.zpig", SoundCategory.MOBS)
    @JvmField
    val ZOMBIE_PIG_ANGRY = register("mob.zombiepig.zpigangry", SoundCategory.MOBS)
    @JvmField
    val ZOMBIE_PIG_DEATH = register("mob.zombiepig.zpigdeath", SoundCategory.MOBS)
    @JvmField
    val ZOMBIE_PIG_HURT = register("mob.zombiepig.zpighurt", SoundCategory.MOBS)

    /**
     * Register a Sound.
     *
     * A saund don't need to be registered to play them to a player.
     */
    fun register(sound: Sound): Sound {
      SOUNDS += sound
      return sound
    }

    /**
     * Register a Sound.
     *
     * A saund don't need to be registered to play them to a player.
     */
    fun register(sound: String, category: SoundCategory): Sound {
      return register(Sound(sound, category))
    }

    /**
     * Gets a sound by name.
     */
    fun byName(name: String): Sound? {
      return SOUNDS.find { it.name == name }
    }

    override fun iterator(): Iterator<Sound> {
      return SOUNDS.iterator()
    }
  }
}
