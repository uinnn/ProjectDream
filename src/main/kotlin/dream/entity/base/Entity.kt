package dream.entity.base

import dream.Sound
import dream.api.*
import dream.block.Block
import dream.block.Blocks
import dream.block.state.IState
import dream.collision.AABB
import dream.coroutines.EntityDispatcher
import dream.damage.*
import dream.enchantment.EnchantmentDamage
import dream.entity.EntityType
import dream.entity.Watcher
import dream.entity.effect.LightningBolt
import dream.entity.item.EntityItem
import dream.entity.watcher
import dream.item.ItemStack
import dream.level.Dimension
import dream.level.Level
import dream.level.chunk.Chunk
import dream.misc.Open
import dream.nbt.CompoundStorable
import dream.nbt.Tag
import dream.nbt.TagType
import dream.nbt.adapter.TagAdapter
import dream.nbt.types.*
import dream.pos.Direction
import dream.pos.Loc
import dream.pos.Pos
import dream.pos.Vec
import dream.utils.*
import korlibs.memory.toByte
import korlibs.memory.toIntFloor
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * A base entity in Minecraft.
 *
 * This is simple non-living and non-insentient entities.
 */
@Open
abstract class Entity : Identifiable, Localizable, CompoundStorable, Tickable, Nameable, Scope, Comparable<Entity> {
  
  override var coroutineContext: CoroutineContext = EntityDispatcher
  
  constructor()
  
  constructor(level: Level) {
    this.level = level
  }
  
  constructor(x: Double, y: Double, z: Double) {
    setPosition(x, y, z)
  }
  
  constructor(level: Level, x: Double, y: Double, z: Double) : this(level) {
    setPosition(x, y, z)
  }
  
  /**
   * Called to initialize entity data.
   */
  init {
    initialize()
  }
  
  /**
   * Initializes this entity.
   */
  fun initialize() {
  }
  
  /**
   * The serial id of this entity.
   *
   * Serial id is used by packets to manipulate and get entities
   */
  var serialId = 0
  
  /**
   * The unique id of this entity.
   */
  override val id: UUID = EntityUUID()
  
  /**
   * The world that's this entity is spawned.
   */
  lateinit var level: Level
  
  /**
   * Verify if this entity has a world.
   */
  val hasLevel: Boolean
    get() = this::level.isInitialized
  
  /**
   * Gets the chunk that this entity resides.
   */
  val chunk get() = level.chunkAt(chunkX, chunkZ)
  
  /**
   *  The type of this entity.
   */
  lateinit var type: EntityType<Entity>
  
  /**
   * The registered type id of the entity.
   */
  val typeId: Int get() = type.id
  
  /**
   * The registered type name of the entity.
   */
  val typeName get() = type.name
  
  /**
   * The registered type key of the entity.
   */
  val typeKey get() = type.key
  
  /**
   * Verify if this entity has a type.
   */
  val hasType: Boolean
    get() = this::type.isInitialized
  
  /**
   * The current X position of this entity.
   */
  var x = 0.0
  
  /**
   * The previous X position of this entity.
   */
  var prevX = 0.0
  
  /**
   * The current Y position of this entity.
   */
  var y = 0.0
  
  /**
   * The previous Y position of this entity.
   */
  var prevY = 0.0
  
  /**
   * The current Z position of this entity.
   */
  var z = 0.0
  
  /**
   * The previous Z position of this entity.
   */
  var prevZ = 0.0
  
  /**
   * The current yaw of this entity.
   */
  var yaw = 0f
    set(value) {
      check(value.isFinite()) { "entity yaw must not be infinite" }
      field = value
    }
  
  /**
   * The previous yaw of this entity.
   */
  var prevYaw = 0f
  
  /**
   * The current pitch of this entity.
   */
  var pitch = 0f
    set(value) {
      check(value.isFinite()) { "entity pitch must not be infinite" }
      field = value
    }
  
  /**
   * The previous pitch of this entity.
   */
  var prevPitch = 0f
  
  /**
   * The current head yaw of this entity.
   */
  var headYaw = 0f
    set(value) {
      check(value.isFinite()) { "entity head yaw must not be infinite" }
      field = value
    }
  
  /**
   * The motion on X coordinate of this entity.
   */
  var motionX = 0.0
  
  /**
   * The motion on Y coordinate of this entity.
   */
  var motionY = 0.0
  
  /**
   * The motion on Z coordinate of this entity.
   */
  var motionZ = 0.0
  
  /**
   * Gets the motion of this entity.
   */
  var motion: Vec
    get() = Vec(motionX, motionY, motionZ)
    set(value) {
      motionX = value.x
      motionY = value.y
      motionZ = value.z
    }
  
  /**
   * The Compound Tag of this entity.
   */
  var tag = CompoundTag()
  
  /**
   * The bounding box of this entity.
   */
  var box = AABB.ZERO
  
  /**
   * Gets if this entity is on ground.
   */
  var onGround = false
  
  /**
   * Gets if this entity after moved has collided.
   */
  var isCollided = false
  
  /**
   * Gets if this entity after moved has collided horizontally.
   */
  var isCollidedHorizontal = false
  
  /**
   * Gets if this entity after moved has collided vertically.
   */
  var isCollidedVertical = false
  
  /**
   * Gets if the velocity of this entity has changed.
   */
  var hasVelocityChanged = false
  
  /**
   * Gets if this entity is in web.
   */
  var isInWeb = false
  
  /**
   * Gets if this entity is outside the border of the world.
   */
  var isOutsideBorder = false
  
  /**
   * Gets if this entity is dead.
   */
  var isDead = false
  
  /**
   * Gets if this entity is alive.
   */
  val isAlive: Boolean get() = !isDead
  
  /**
   * The distance weight that this entity will be rendered.
   */
  var renderDistanceWeight = 1.0
  
  /**
   * Gets the width of this entity.
   */
  var width = 0.6f
  
  /**
   * Gets the height of this entity.
   */
  var height = 1.8f
  
  /**
   * Gets how many ticks this entity is alive.
   */
  var ticks = 0
  
  /**
   * The data watcher of this entity.
   */
  var watcher = watcher(this) {
    addByte(0, 0)
    addShort(1, 300)
    add(2, "")
    addByte(3, 0)
    addByte(4, 0)
  }
  
  /**
   * The entity that's this entity is riding.
   */
  var riding: Entity? = null
  
  /**
   * The entity that is riding this entity.
   */
  var passenger: Entity? = null
  
  /**
   * The pitch delta of the rider of this entity.
   */
  var riderPitch = 0.0
  
  /**
   * The yaw delta of the rider of this entity.
   */
  var riderYaw = 0.0
  
  /**
   * The fall distance of this entity.
   */
  var fallDistance = 0f
  
  /**
   * The distance of this entity walked.
   */
  var distanceWalked = 0f
  
  /**
   * The previous distance of this entity walked.
   */
  var prevDistanceWalked = 0f
  
  /**
   * The distance of this entity walked on step.
   */
  var distanceWalkedOnStep = 0f
  
  /**
   * The distance that has to be exceeded in order to triger a new step sound
   * and an [onWalk] event on a block.
   */
  var nextStepDistance = 0
  
  /**
   * The X position of this entity at the previous tick.
   *
   * Used to calculate position during rendering routines.
   */
  var lastTickPosX = 0.0
  
  /**
   * The Y position of this entity at the previous tick.
   *
   * Used to calculate position during rendering routines.
   */
  var lastTickPosY = 0.0
  
  /**
   * The Z position of this entity at the previous tick.
   *
   * Used to calculate position during rendering routines.
   */
  var lastTickPosZ = 0.0
  
  /**
   * The pos of this entity at the previous tick.
   */
  var lastTickPos: Pos
    get() = Pos(lastTickPosX, lastTickPosY, lastTickPosZ)
    set(value) {
      lastTickPosX = value.x
      lastTickPosY = value.y
      lastTickPosZ = value.z
    }
  
  /**
   * The height step value that's this entity will jump.
   */
  var stepHeight = 0f
  
  /**
   * If this entity doesn't clip at collision.
   */
  var noClip = false
  
  /**
   * Reduces the velocity when another entity collides with this entity.
   */
  var collisionReduction = 0f
  
  /**
   * Gets if this entity is immune to fire.
   */
  var isFireImmune = false
  
  /**
   * The amount of ticks that this entity is on fire.
   */
  var fireTicks = 0
  
  /**
   * The amount of ticks that this entity must be on fire before seting on fire.
   */
  var fireImmuneTicks = 0
  
  /**
   * Gets if this entity is on water.
   */
  var isOnWater = false
  
  /**
   * If this entity has not updated their stats before.
   */
  var isFirstUpdate = true
  
  /**
   * Gets if this entity is immune to any type of damage.
   */
  var isInvulnerable = false
  
  /**
   * The amount of ticks that this entity is immune to any type of damage.
   */
  var hurtImmuneTicks = 0
  
  /**
   * Render this entity even if it is outside the camera frustum check.
   */
  var ignoreFrustumCheck = false
  
  /**
   * Gets if this entity is on air
   *
   * `a.k.a airborne`.
   */
  var isOnAir = false
  
  /**
   * If this entity is added to the chunk that is inside on.
   */
  var addedToChunk = false
  
  /**
   * The chunk on X coordinate of this entity.
   */
  var chunkX = 0
  
  /**
   * The chunk on Y coordinate of this entity.
   */
  var chunkY = 0
  
  /**
   * The chunk on Z coordinate of this entity.
   */
  var chunkZ = 0
  
  /**
   * The dimension that this entity is located.
   */
  var dimension = Dimension.OVERWORLD
  
  /**
   * Gets if this entity is on overworld.
   */
  val isOnOverworld: Boolean
    get() = dimension == Dimension.OVERWORLD
  
  /**
   * Gets if this entity is on nether.
   */
  val isOnNether: Boolean
    get() = dimension == Dimension.NETHER
  
  /**
   * Gets if this entity is on the end.
   */
  val isOnEnd: Boolean
    get() = dimension == Dimension.END
  
  /**
   * Gets if this entity is on portal.
   */
  var inPortal = false
  
  /**
   * The current portal tick before entering on.
   */
  var portalTicks = 0
  
  /**
   * The necessary tick amount before entering on portal.
   */
  var ticksUntilPortal = 0
  
  /**
   * A pos representing the last portal that this entity was in.
   */
  var lastPortalPos: Pos? = null
  
  /**
   * A direction related to the position of the last portal that this entity was in.
   */
  var portalDirection = Direction.NORTH
  
  /**
   * Gets the max portal ticks to wait.
   */
  val maxPortalTicks: Int get() = 0
  
  /**
   * Gets the name of this entity.
   *
   * If this entity not have a custom name, will return the default entity name.
   */
  override var name: String
    get() = watcher.string(2, "")
    set(value) {
      watcher.update(2, value)
    }
  
  /**
   * Gets if this entity has a custom name.
   */
  override val hasName: Boolean
    get() = watcher.contains(2)
  
  /**
   * The current location of this entity.
   */
  override var location: Loc
    get() = Loc(this)
    set(value) {
      x = value.x
      y = value.y
      z = value.z
      yaw = value.yaw
      pitch = value.pitch
      level = value.level
    }
  
  /**
   * The current pos of this entity.
   */
  var pos: Pos
    get() = Pos(this)
    set(value) {
      x = value.x
      y = value.y
      z = value.z
    }
  
  /**
   * The previous location of this entity.
   */
  var prevLocation: Loc
    get() = Loc(level, prevX, prevY, prevZ, prevYaw, prevPitch)
    set(value) {
      prevX = value.x
      prevY = value.y
      prevZ = value.z
      prevYaw = value.yaw
      prevPitch = value.pitch
    }
  
  /**
   * Return if this entity has moved.
   *
   * Checking their prev position to actual position.
   */
  val hasMoved: Boolean
    get() = prevX != x || prevY != y || prevZ != z
  
  /**
   * Gets the eye height of this entity.
   */
  val eyeHeight: Float
    get() = height * 0.85f
  
  /**
   * Gets the horizontal look direction of this entity.
   */
  val lookDirection: Direction
    get() = Direction.byYaw(yaw)
  
  /**
   * Gets the nearest direction based on pos of this entity.
   */
  val nearestDirection: Direction
    get() = Direction.nearest(x, y, z)
  
  /**
   * Gets the max fall height that's this entity can fall without taking damage.
   */
  val maxFallHeight: Int
    get() = 3
  
  /**
   * Gets if this entity is burning.
   */
  val isBurning: Boolean
    get() = !isFireImmune && fireTicks > 0
  
  /**
   * Gets if fire should be rendered on this entity.
   */
  val canRenderFire: Boolean
    get() = isBurning
  
  /**
   * Gets the position of the entity's foot.
   */
  val footPos: Pos
    get() = Pos(x.toIntFloor(), box.minY.toIntFloor(), y.toIntFloor())
  
  /**
   * Gets the state inside the entity's foot position.
   */
  var stateInside: IState
    get() = level.getState(footPos)
    set(value) {
      level.setState(footPos, value)
    }
  
  /**
   * Gets the block inside the entity's foot position.
   */
  var blockInside: Block
    get() = level.getBlock(footPos)
    set(value) {
      level.setBlock(footPos, value)
    }
  
  /**
   * Checks if the entity is currently on a ladder.
   * @return `true` if the entity is on a ladder, `false` otherwise.
   */
  val isOnLadder: Boolean
    get() {
      val state = stateInside
      return state.its(Blocks.LADDER) || state.its(Blocks.VINES)
    }
  
  /**
   * Gets if this entity is burning.
   */
  var isBurningFlag: Boolean
    get() = getFlag(0)
    set(value) = setFlag(0, value)
  
  /**
   * Gets if this entity is sneaking.
   */
  var isSneaking: Boolean
    get() = getFlag(1)
    set(value) = setFlag(1, value)
  
  /**
   * Gets if this entity is with shift key down.
   *
   * This is a alias for [isSneaking].
   */
  var isShiftKeyDown: Boolean
    get() = getFlag(1)
    set(value) = setFlag(1, value)
  
  /**
   * Gets if this entity is sprinting.
   */
  var isSprinting: Boolean
    get() = getFlag(3)
    set(value) = setFlag(3, value)
  
  /**
   * Gets if this entity is eating.
   */
  var isEating: Boolean
    get() = getFlag(4)
    set(value) = setFlag(4, value)
  
  /**
   * Gets if this entity is invisible.
   */
  var isInvisible: Boolean
    get() = getFlag(5)
    set(value) = setFlag(5, value)
  
  /**
   * Gets if this entity is invisible.
   */
  var isSilent: Boolean
    get() = watcher.byte(4) == 1.toByte()
    set(value) {
      watcher.update(4, value.toByte())
    }
  
  /**
   * Gets if the given [flag] is active for this entity on [watcher].
   */
  fun getFlag(flag: Int): Boolean {
    return (watcher.byte(0) and 1 shl flag) != 0.toByte()
  }
  
  /**
   * Enables or disables an entity flag specified by [flag].
   */
  fun setFlag(flag: Int, enable: Boolean) {
    val value = watcher.byte(0)
    if (enable) {
      watcher.update(0, value or 1 shl flag)
    } else {
      watcher.update(0, value and (1 shl flag).inv())
    }
  }
  
  /**
   * Applies the given [builder] to the tag of this item.
   */
  final inline fun withTag(builder: CompoundTag.() -> Unit): CompoundTag {
    return tag.apply(builder)
  }
  
  /**
   * Checks if the tag of entity has [key] present.
   */
  fun hasTag(key: String): Boolean {
    return key in tag
  }
  
  /**
   * Checks if the tag of entity has [key] of [type] present.
   */
  fun hasTag(key: String, type: TagType<*>): Boolean {
    return tag.has(key, type)
  }
  
  /**
   * Gets a tag by the specified [key].
   */
  operator fun get(key: String) = tag[key]
  
  /**
   * Sets [value] tag with [key] on the tag of this entity.
   */
  operator fun set(key: String, value: Tag) = tag.set(key, value)
  
  operator fun set(key: String, value: Boolean) = set(key, ByteTag.of(value))
  operator fun set(key: String, value: Byte) = set(key, ByteTag.of(value))
  operator fun set(key: String, value: Short) = set(key, ShortTag.of(value))
  operator fun set(key: String, value: Int) = set(key, IntTag.of(value))
  operator fun set(key: String, value: Long) = set(key, LongTag.of(value))
  operator fun set(key: String, value: Float) = set(key, FloatTag.of(value))
  operator fun set(key: String, value: Double) = set(key, DoubleTag.of(value))
  operator fun set(key: String, value: ByteArray) = set(key, ByteArrayTag(value))
  operator fun set(key: String, value: String) = set(key, StringTag.of(value))
  operator fun set(key: String, value: IntArray) = set(key, IntArrayTag(value))
  operator fun <T : Any> set(key: String, adapter: TagAdapter<T>, value: T) = adapter.write(key, tag, value)
  
  /**
   * Sets the position of this entity.
   *
   * @param updateBB if the bounding box of this entity should be updated too.
   */
  fun setPosition(x: Double, y: Double, z: Double, updateBB: Boolean = true) {
    this.x = x
    this.y = y
    this.z = z
    if (updateBB) {
      val width = this.width / 2
      box.set(x - width, y, z - width, x + width, y + height, z + width)
    }
  }
  
  /**
   * Sets the rotation of this entity.
   */
  fun setRotation(yaw: Float, pitch: Float) {
    this.yaw = yaw % 360
    this.pitch = pitch % 360
  }
  
  /**
   * Sets the angles of this entity.
   */
  fun setAngles(yaw: Float, pitch: Float) {
    val oldYaw = this.yaw
    val oldPitch = this.pitch
    this.yaw += yaw * 0.15f
    this.pitch = between(-90f, this.pitch + pitch * 0.15f, 90f)
    prevYaw += this.yaw - oldYaw
    prevPitch += this.pitch - oldPitch
  }
  
  /**
   * Sets the size of this entity.
   */
  fun setSize(width: Float, height: Float) {
    if (this.width != width || this.height != height) {
      val old = this.width
      
      this.width = width
      this.height = height
      box.expandMax(width.toDouble(), height.toDouble(), width.toDouble())
      
      if (width > old && !isFirstUpdate) {
        move((old - width).toDouble(), 0.0, (old - width).toDouble())
      }
    }
  }
  
  /**
   * Sets this entity dead.
   */
  fun setDead() {
    isDead = true
  }
  
  /**
   * Spawns this entity in the specified [level].
   */
  fun spawn(level: Level) {
    if (hasType && canSpawn(level)) {
      this.level = level
      level.spawn(this)
      onSpawn()
    }
  }
  
  /**
   * Spawns this entity and get it.
   */
  fun spawnAndGet(level: Level): Entity {
    spawn(level)
    return this
  }
  
  /**
   * Spawns [entity] on the level of this entity.
   */
  fun <T : Entity> spawnEntity(entity: T): T {
    level.spawn(entity)
    return entity
  }
  
  /**
   * Drops the given [item] by this entity.
   *
   * @return the entity dropped, or null if [item] is air or stack is below 0.
   */
  fun drop(item: ItemStack, around: Boolean = false, trace: Boolean = false): Entity? {
    val dropped = item.drop(level, x, y - 0.3 + eyeHeight, z)
    if (trace && dropped is EntityItem) {
      dropped.thrower = name
    }
    
    return dropped
  }
  
  /**
   * Spawns running particles for this entity.
   */
  fun spawnRunningParticles() {
  
  }
  
  /**
   * Ticks basic logic of this entity.
   */
  override fun tick(partial: Int) {
    tickLogic()
  }
  
  /**
   * Ticks complex logic of this entity.
   */
  fun tickLogic() {
    prevDistanceWalked = distanceWalked
    prevX = x
    prevY = y
    prevZ = z
    prevYaw = yaw
    prevPitch = pitch
    
    if (riding != null && riding!!.isDead) {
      riding = null
    }
    
    if (inPortal) {
      tickPortalLogic()
    } else {
      tickNotPortalLogic()
    }
    
    if (ticksUntilPortal > 0) {
      ticksUntilPortal--
    }
    
    spawnRunningParticles()
    handleWaterMovement()
    
    if (fireTicks > 0) {
      tickFireLogic()
      isBurningFlag = true
    }
    
    if (isInLava()) {
      tickLavaLogic()
    }
    
    if (level.isVoid(x, y, z)) {
      fallenOnVoid()
    }
    
    
    isFirstUpdate = false
  }
  
  /**
   * Ticks in portal logic of this entity.
   *
   * Called every tick when this entity ``IS ON`` portal.
   */
  fun tickPortalLogic() {
  
  }
  
  /**
   * Ticks not in portal logic of this entity.
   *
   * Called every tick when this entity ``IS NOT ON`` portal.
   */
  fun tickNotPortalLogic() {
    portalTicks = max(portalTicks - 4, 0)
  }
  
  /**
   * Ticks fire logic of this entity.
   *
   * Called every tick when this entity is on fire.
   */
  fun tickFireLogic() {
    if (isFireImmune) {
      fireTicks = (fireTicks - 4).coerceAtLeast(0)
    } else {
      if (fireTicks-- % 20 == 0) {
        damage(OnFireDamage, 1f)
      }
    }
  }
  
  /**
   * Ticks lava logic of this entity.
   *
   * Called every tick when this entity is on lava.
   */
  fun tickLavaLogic() {
    damage(LavaDamage, 4f)
    fallDistance *= 0.5f
  }
  
  /**
   * Tries to moves this entity by the passed in displacement.
   *
   * @return if the entity has moved.
   */
  fun move(x: Double, y: Double, z: Double): Boolean {
    if (x == 0.0 && y == 0.0 && z == 0.0) {
      return false
    }
    
    if (noClip) {
      box.move(x, y, z)
      resetPosToAABB()
      return true
    }
    
    
    
    return false
  }
  
  /**
   * Handles water movement of this entity.
   *
   * @return if the entity is on water.
   */
  fun handleWaterMovement(): Boolean {
    
    return isOnWater
  }
  
  /**
   * Resets the pos of this entity to their AABB.
   */
  fun resetPosToAABB() {
    x = (box.minX + box.maxX) / 2
    y = box.minY
    z = (box.minZ + box.maxZ) / 2
  }
  
  /**
   * Makes this entity do a specific sound.
   *
   * No sound will be triggered if the entity is silent.
   */
  fun makeSound(sound: Sound, volume: Float = 1f, pitch: Float = 1f) {
    if (!isSilent) {
      level.playSound(sound, this, volume, pitch)
    }
  }
  
  /**
   * Sets the motion velocity of this entity.
   */
  fun setMotion(x: Double, y: Double, z: Double) {
    motionX = x
    motionY = y
    motionZ = z
  }
  
  /**
   * Adds motion velocity to this entity.
   */
  fun addMotion(x: Double, y: Double, z: Double) {
    motionX += x
    motionY += y
    motionZ += z
    isOnAir = true
  }
  
  /**
   * Gets a pos representing the entity look.
   */
  fun lookingAt(mod: Float = 1f): Pos {
    return if (mod == 1f) {
      Pos.fromRotation(yaw, pitch)
    } else {
      val yaw = prevYaw + (yaw - prevYaw) * mod
      val pitch = prevPitch + (pitch - prevPitch) * mod
      return Pos.fromRotation(yaw, pitch)
    }
  }
  
  /**
   * Gets a pos representing the eyes of this entity.
   */
  fun eyes(mod: Float = 1f): Pos {
    return if (mod == 1f) {
      Pos(x, y + eyeHeight, z)
    } else {
      val x = prevX + (x - prevX) * mod
      val y = prevY + (y - prevY) * mod + eyeHeight
      val z = prevZ + (z - prevZ) * mod
      Pos(x, y, z)
    }
  }
  
  /**
   * Sets this entity as been striked by any damage.
   */
  fun setBeenDamaged() {
    hasVelocityChanged = true
  }
  
  /**
   * Damages this entity.
   *
   * This has no effect on entities that is not [EntityLiving].
   *
   * This is sightly different from [damage], because this damage the entity fully directly without caring
   * of [isInvulnerable] and [Damage] characteristics,
   * resuming, this will subtract their health without side-effects.
   */
  fun damageDirect(amount: Float) {
  
  }
  
  /**
   * Damages this entity by the given [damage] source with [amount].
   */
  fun damage(damage: Damage, amount: Float): Boolean {
    if (!isInvulnerable(damage, amount)) {
      setBeenDamaged()
    }
    
    return false
  }
  
  /**
   * Determinates if this entity is invulnerable to [damage].
   */
  fun isInvulnerable(damage: Damage, amount: Float = 0f): Boolean {
    return isInvulnerable && damage !is VoidDamage && !damage.damageInCreative && !damage.canDamage(this, amount)
  }
  
  /**
   * Damages this entity by lightning bolt.
   *
   * @return true if should apply [damage] characteristics.
   */
  fun onLightningDamage(bolt: LightningBolt, damage: LightningBoltDamage, amount: Float): Boolean {
    return true
  }
  
  /**
   * Damages this entity by void.
   *
   * @return true if should apply [VoidDamage] characteristics.
   */
  fun onVoidDamage(amount: Float): Boolean {
    return true
  }
  
  /**
   * Called when this entity fallens on void.
   */
  fun fallenOnVoid() {
    setDead()
  }
  
  /**
   * Damages this entity by fire ``effect``.
   *
   * @return true if should apply [OnFireDamage] characteristics.
   */
  fun onFireDamage(amount: Float): Boolean {
    return true
  }
  
  /**
   * Damages this entity by fire ``block``.
   *
   * @return true if should apply [InFireDamage] characteristics.
   */
  fun onInFireDamage(amount: Float): Boolean {
    return true
  }
  
  /**
   * Damages this entity by lava.
   *
   * @return true if should apply [LavaDamage] characteristics.
   */
  fun onLavaDamage(amount: Float): Boolean {
    return true
  }
  
  /**
   * Damages this entity by fall.
   *
   * @return true if should apply [FallDamage] characteristics.
   */
  fun onFallDamage(distance: Float, multiplier: Float): Boolean {
    return distance > maxFallHeight
  }
  
  /**
   * Called when this entity falls on a block.
   */
  fun onFall(block: Block, distance: Float) {
  
  }
  
  /**
   * Called when [attacker] damages this entity with [item] enchanted with [ench].
   */
  fun onEnchantmentDamage(ench: EnchantmentDamage, attacker: Entity, item: ItemStack, level: Int): Boolean {
    damageDirect(ench.getDamage(attacker, this, item, level))
    return true
  }
  
  /**
   * Makes this entity attacks [target].
   */
  fun attack(target: Entity) {
  
  }
  
  /**
   * Gets if this entity is on lava.
   */
  fun isInLava(): Boolean {
    return false
  }
  
  /**
   * Gets if this entity can trigger walking on block.
   */
  fun canTriggerWalk() = true
  
  /**
   * Gets if this entity can spawn.
   */
  fun canSpawn(level: Level) = true
  
  /**
   * Called after this entity is spawned on the world.
   */
  fun onSpawn() = Unit
  
  /**
   * Called when this entity walks.
   */
  fun onWalk() = Unit
  
  /**
   * Called when this entity collides with other entity.
   */
  fun onCollide(entity: Entity) = Unit
  
  /**
   * Gets if this entity can collides.
   */
  fun canCollide() = true
  
  /**
   * Gets if this entity should be pushed when colliding
   * with other entities.
   */
  fun shouldBePushed() = false
  
  /**
   * Called when [Watcher] has a value updated.
   */
  fun onWatcherUpdate(id: Int, value: Any) = Unit
  
  /**
   * Called on [Chunk] when trying to add this entity and their pos is wrond beside [chunk] pos.
   */
  fun wrongChunkPosCallback(chunk: Chunk) {
    setDead()
  }
  
  /**
   * Called on [Chunk] when this entity is added to [chunk].
   */
  fun onAddedToChunk(chunk: Chunk) {
  }
  
  /**
   * Saves this entity on [tag].
   */
  override fun save(tag: CompoundTag) {
  
  }
  
  /**
   * Loads this entity from [tag].
   */
  override fun load(tag: CompoundTag) {
  
  }
  
  /**
   * Used on sub classes of entity to save their extra data.
   */
  fun saveAdditional(tag: CompoundTag) {
  
  }
  
  /**
   * Used on sub classes of entity to load their extra data.
   */
  fun loadAdditional(tag: CompoundTag) {
  
  }
  
  /**
   * Determinates if this entity is same to [entity].
   */
  fun isSame(entity: Entity): Boolean {
    return this == entity
  }
  
  /**
   * Gets the distance between this entity and given coordinates.
   */
  fun distance(x: Double, y: Double, z: Double): Double {
    return (this.x - x) + (this.y - y) + (this.z - z)
  }
  
  /**
   * Gets the distance between this entity and given coordinates.
   */
  fun distance(x: Number, y: Number, z: Number): Double {
    return distance(x.toDouble(), y.toDouble(), z.toDouble())
  }
  
  /**
   * Gets the distance between this entity and other [pos].
   */
  fun distance(pos: Pos): Double {
    return distance(pos.x, pos.y, pos.z)
  }
  
  /**
   * Gets the distance between this entity and entity pos.
   */
  fun distance(entity: Entity): Double {
    return distance(entity.x, entity.y, entity.z)
  }
  
  /**
   * Returns if this entity is near to the given coordinates by [radius].
   */
  fun isNear(x: Double, y: Double, z: Double, radius: Double): Boolean {
    return distance(x, y, z) <= radius
  }
  
  /**
   * Returns if this entity is near to the given coordinates by [radius].
   */
  fun isNear(x: Number, y: Number, z: Number, radius: Double): Boolean {
    return distance(x, y, z) <= radius
  }
  
  /**
   * Returns if this entity is near to other [pos] by [radius].
   */
  fun isNear(pos: Pos, radius: Double): Boolean {
    return distance(pos) <= radius
  }
  
  /**
   * Returns if this entity is near to [entity] by [radius].
   */
  fun isNear(entity: Entity, radius: Double): Boolean {
    return distance(entity) <= radius
  }
  
  
  /**
   * Gets the nearby entities on this loc.
   */
  fun nearbyEntities(x: Double, y: Double, z: Double): List<Entity> {
    return level.getEntitiesAround(pos, x, y, z)
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
   * Creates a copy of this entity.
   */
  fun copy() = type.create(level, store())
  
  override fun compareTo(other: Entity): Int {
    return serialId.compareTo(other.serialId)
  }
  
  override fun hashCode(): Int {
    return serialId
  }
  
  override fun equals(other: Any?): Boolean {
    if (other !is Entity) return false
    
    return serialId == other.serialId
  }
  
  override fun toString(): String {
    return """
         Entity ${this::class.qualifiedName}
         ${if (hasLevel) "World ${level.name}" else "None World"}
         Position x: $x y: $y z: $z
      """.trimIndent()
  }
}
