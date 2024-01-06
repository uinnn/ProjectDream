package dream.entity.base

import dream.pos.*

abstract class EntityHanging : Entity() {

  var facing = Direction.NORTH
  var hangingPos = Pos.ZERO

}
