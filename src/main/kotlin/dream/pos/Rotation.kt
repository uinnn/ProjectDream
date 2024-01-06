package dream.pos

import com.soywiz.kds.random.*

/**
 * All rotations.
 */
enum class Rotation {

  /**
   * Front rotation.
   *
   * `a.k.a NONE`
   */
  FRONT,

  /**
   * 90 clockwise rotation.
   */
  CLOCKWISE,

  /**
   * Back rotation.
   *
   * `a.k.a 180 clockwise rotation`
   */
  BACK,

  /**
   * 90 counterclockwise rotation.
   */
  COUNTER_CLOCKWISE;

  /**
   * Rotates this rotation agains [CLOCKWISE]
   */
  fun rotateClockWise(): Rotation {
    return when (this) {
      FRONT -> CLOCKWISE
      CLOCKWISE -> BACK
      BACK -> COUNTER_CLOCKWISE
      COUNTER_CLOCKWISE -> FRONT
    }
  }

  /**
   * Rotates this rotation agains [BACK]
   */
  fun rotateBack(): Rotation {
    return when (this) {
      FRONT -> BACK
      CLOCKWISE -> COUNTER_CLOCKWISE
      BACK -> FRONT
      COUNTER_CLOCKWISE -> CLOCKWISE
    }
  }

  /**
   * Rotates this rotation agains [COUNTER_CLOCKWISE]
   */
  fun rotateCounterClockWise(): Rotation {
    return when (this) {
      FRONT -> BACK
      CLOCKWISE -> COUNTER_CLOCKWISE
      BACK -> FRONT
      COUNTER_CLOCKWISE -> CLOCKWISE
    }
  }


  /**
   * Rotates this rotation against [rotation].
   */
  fun rotated(rotation: Rotation): Rotation {
    return when (rotation) {
      BACK -> rotateBack()
      COUNTER_CLOCKWISE -> rotateCounterClockWise()
      CLOCKWISE -> rotateClockWise()
      else -> this
    }
  }

  /**
   * Rotates [a] and [b] by this rotation.
   */
  fun rotate(a: Int, b: Int): Int {
    return when (this) {
      CLOCKWISE -> (a + b / 4) % b
      BACK -> (a + b / 2) % b
      COUNTER_CLOCKWISE -> (a + b * 3 / 4) % b
      else -> a
    }
  }

  companion object {
    
    /**
     * Gets a random rotation.
     */
    fun random() = entries.fastRandom()
  }
}
