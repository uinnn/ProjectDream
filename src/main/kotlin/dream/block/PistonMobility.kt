package dream.block

/**
 * Represents all types of a piston mobility.
 */
enum class PistonMobility(val id: Int) {

  /**
   * Piston can push and can move the block.
   */
  FREE(0),

  /**
   * Piston can move the block but cannot push.
   */
  UNPUSHABLE(1),

  /**
   * Piston cannot move and cannot push the block.
   */
  IMMOVABLE(2)
}
