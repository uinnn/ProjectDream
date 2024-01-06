package dream.collision

import dream.misc.Open
import dream.pos.*

/**
 * A result of hit.
 *
 * Used for ray tracing.
 */
@Open
class Hit {

  val isEmpty get() = this is Empty
  val isMissed get() = this is Miss
  val isBlock get() = this is Block
  val isEntity get() = this is Entity

  /**
   * Empty hit.
   */
  object Empty : Hit()

  /**
   * Hit missed.
   */
  object Miss : Hit()

  /**
   * Hitted on block.
   */
  class Block(hit: Pos, var side: Direction, var pos: Pos = Pos.ZERO) : Hit() {
    var hit = hit.copy()
  }

  /**
   * Hitted on entity.
   */
  class Entity(hit: Pos, var entity: dream.entity.base.Entity) : Hit() {
    var hit = hit.copy()
  }

}
