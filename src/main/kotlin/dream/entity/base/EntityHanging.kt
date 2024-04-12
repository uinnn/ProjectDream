package dream.entity.base

import dream.pos.Direction
import dream.pos.Pos

abstract class EntityHanging : Entity() {

  /**
   * The direction that this entity hanging is facing.
   */
  var facing = Direction.NORTH

  /**
   * The position that this entity is hanged.
   */
  var hangingPos = Pos.ZERO

  /**
   * Amount of pixels thats this entity contains in width.
   */
  //abstract val pixelsWidth: Int

  /**
   * Amount of pixels thats this entity contains in height.
   */
  //abstract val pixelsHeight: Int

  init {
    setSize(0.5f, 0.5f)
  }



}
