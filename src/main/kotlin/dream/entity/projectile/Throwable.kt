package dream.entity.projectile

import dream.block.Block
import dream.entity.base.EntityLiving
import dream.pos.MutablePos

/**
 * A base interface for any projectile entity.
 */
interface Throwable {

  /**
   * The shooter of this projectile.
   */
  var shooter: EntityLiving

  /**
   * Gets the tile that's this projectile is in.
   */
  var tile: Block

  /**
   * Determinates if this projectile is on tile.
   */
  val isOnTile: Boolean

  /**
   * The tile pos.
   */
  var tilePos: MutablePos

  /**
   * Gets the total ticks that this projectile is on ground.
   */
  var ticksOnGround: Int

  /**
   * Gets the total ticks that this projectile is on air.
   */
  var ticksOnAir: Int

  /**
   * Points to the projectile heading direction.
   */
  fun setProjectileHeading(x: Double, y: Double, z: Double, velocity: Float, inaccuracy: Float)

  /**
   * Called when this projectile impacts on block.
   */
  fun onImpact()
}
