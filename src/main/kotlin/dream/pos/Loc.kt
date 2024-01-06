package dream.pos

import dream.block.Block
import dream.block.Blocks
import dream.block.state.IState
import dream.entity.base.Entity
import dream.level.Level
import dream.level.level
import dream.nbt.types.CompoundTag
import dream.nbt.types.compound

/**
 * Represents a mutable location coordinate-system with camera rotation.
 */
class Loc(
  var level: Level,
  x: Double,
  y: Double,
  z: Double,
  var yaw: Float = 0f,
  var pitch: Float = 0f,
) : MutablePos(x, y, z) {

  constructor(level: Level) : this(level, 0.0, 0.0, 0.0)

  constructor(level: Level, codified: Long) : this(level, getX(codified), getY(codified), getZ(codified))

  constructor(entity: Entity) : this(entity.level, entity.x, entity.y, entity.z, entity.yaw, entity.pitch)

  constructor(level: Level, x: Number, y: Number, z: Number, yaw: Number = 0f, pitch: Number = 0f) : this(
    level,
    x.toDouble(),
    y.toDouble(),
    z.toDouble(),
    yaw.toFloat(),
    pitch.toFloat()
  )

  val chunk get() = chunkAt(level)
  val tile get() = tileAt(level)
  val state get() = state()
  val block get() = block()

  fun stateOrNull() = stateAtOrNull(level)
  fun state(default: IState = Blocks.AIR.state) = stateAt(level, default)

  fun blockOrNull() = blockAtOrNull(level)
  fun block(default: Block = Blocks.AIR) = blockAt(level, default)

  /**
   * Gets the look direction of this loc.
   */
  val look: Direction get() = Direction.byYaw(yaw)

  /**
   * Returns if the look direction of this loc is equals to [direction].
   */
  fun isLooking(direction: Direction): Boolean {
    return look == direction
  }

  /**
   * Gets a pos representing the loc look.
   */
  fun lookPos(mod: Float = 1f): Pos {
    return fromRotation(yaw * mod, pitch * mod)
  }

  /**
   * Gets the nearby entities on this loc.
   */
  fun nearbyEntities(x: Double, y: Double, z: Double): List<Entity> {
    return level.getEntitiesAround(this, x, y, z)
  }

  /**
   * Gets the nearby entities on this loc.
   */
  fun nearbyEntities(x: Number, y: Number, z: Number): List<Entity> {
    return nearbyEntities(x.toDouble(), y.toDouble(), z.toDouble())
  }

  /**
   * Gets the nearby entities on this loc.
   */
  fun nearbyEntities(pos: Pos): List<Entity> {
    return nearbyEntities(pos.x, pos.y, pos.z)
  }

  /**
   * Gets a copied version of this pos.
   */
  override fun copy(): Loc {
    return Loc(level, x, y, z, yaw, pitch)
  }

  /**
   * Gets a mutable pos based on this pos.
   */
  override fun mutable(): Loc {
    return this
  }

  /**
   * Gets a loc based on this pos.
   */
  override fun asLoc(level: Level, yaw: Float, pitch: Float): Loc {
    return this
  }

  /**
   * Saves this pos to [tag] without storing [yaw] and [pitch].
   */
  fun saveWithoutLook(tag: CompoundTag) {
    tag["Level"] = level.name
    tag["Pos"] = asLong()
  }

  /**
   * Loads this pos from [tag] without loading [yaw] and [pitch].
   */
  fun loadWithoutLook(tag: CompoundTag) {
    level = level(tag.string("Level"))
    set(tag.long("Pos"))
  }

  fun toTagWithoutLook() = compound { saveWithoutLook(this) }

  /**
   * Saves this pos to [tag].
   */
  override fun save(tag: CompoundTag) {
    tag["Level"] = level.name
    tag["Pos"] = asLong()
    tag["Yaw"] = yaw
    tag["Pitch"] = pitch
  }

  /**
   * Loads this pos from [tag].
   */
  override fun load(tag: CompoundTag) {
    level = level(tag.string("Level"))
    set(tag.long("Pos"))
    yaw = tag.float("Yaw")
    pitch = tag.float("Pitch")
  }

  override fun toString(): String {
    return "Loc(level=$level, x=$x, y=$y, z=$z, yaw=$yaw, pitch=$pitch)"
  }
}
