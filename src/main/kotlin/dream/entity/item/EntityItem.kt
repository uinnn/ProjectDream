package dream.entity.item

import dream.entity.base.*
import dream.entity.player.*
import dream.item.*
import dream.level.*
import dream.utils.*

/**
 * Represents an entity item.
 */
class EntityItem : Entity {

  constructor()

  constructor(level: Level, x: Double, y: Double, z: Double) : super(level, x, y, z) {
    yaw = randomFloat() * 360f
    motionX = randomDouble() * 0.2 - 0.1
    motionY = 0.2
    motionZ = randomDouble() * 0.2 - 0.1
  }

  constructor(level: Level, x: Double, y: Double, z: Double, item: ItemStack) : this(level, x, y, z) {
    this.item = item
  }

  /**
   * The item represented by this entity.
   */
  var item: ItemStack
    get() = watcher.itemOrNull(10) ?: ItemStack(Items.STONE).also { item = it }
    set(value) {
      watcher.update(10, value)
      watcher.watch(10)
    }

  /**
   * The age of this entity item.
   */
  var age = 0

  /**
   * The pickup delay of this entity item.
   */
  var pickupDelay = 0

  /**
   * The health of this entity item.
   *
   * Used to get the resistance when damaged.
   */
  var health = 5

  /**
   * The entity name that's throwed this item.
   */
  var thrower = ""

  /**
   * The entity name owner of this item.
   */
  var owner = ""

  /**
   * Checks if this entity item has a thrower.
   */
  val hasThrower: Boolean get() = thrower.isNotBlank()

  /**
   * Checks if this entity item has an owner.
   */
  val hasOwner: Boolean get() = owner.isNotBlank()

  /**
   * Determines if this Item is fire immune.
   *
   * This is a simple pre-made 1.16 netherite fire resistance feature on 1.8.
   */
  val isItemFireImmune: Boolean get() = item.isFireImmune(this)

  override fun initialize() {
    setSize(0.25f, 0.25f)
  }

  override fun tick(partial: Int) {
    super.tick(partial)
    item.onEntityTick(this)
  }
  
  fun canPickup(entity: Entity): Boolean {
    return entity is Player && (hasOwner && owner == entity.name)
  }
  
  override fun onCollide(entity: Entity) {
    if (entity is Player && hasOwner && owner == entity.name) {
      entity.inventory.setItem(0, item)
    }
  }

  /**
   * Sets this entity item pickup delay to default.
   */
  fun withDefaultPickupDelay(): EntityItem {
    pickupDelay = 10
    return this
  }

  /**
   * Makes this entity item without pickup delay.
   */
  fun withoutPickupDelay(): EntityItem {
    pickupDelay = 0
    return this
  }

  /**
   * Makes this entity item unpickupable.
   *
   * Players cannot pick this item.
   */
  fun setUnpickupable(): EntityItem {
    pickupDelay = 32767
    return this
  }

  /**
   * Sets this entity item not despawnable.
   *
   * This item will not despawn.
   */
  fun setNotDespawnable(): EntityItem {
    age = -6000
    return this
  }

  /**
   * Makes this entity item only visual.
   *
   * This will set the item unpickupable and not despawnable.
   */
  fun setOnlyVisual(): EntityItem {
    setUnpickupable()
    setNotDespawnable()
    return this
  }
}
