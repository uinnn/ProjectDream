package dream.pos

import dream.misc.*
import dream.nbt.types.*
import dream.utils.*
import kotlinx.serialization.*

/**
 * Represents a rotation around an axis.
 */
@Serializable
@Open
data class Rot(val x: Float, val y: Float, val z: Float) {

  constructor() : this(0f, 0f, 0f)
  constructor(tag: ListTag<FloatTag>) : this(tag.getFloat(0), tag.getFloat(1), tag.getFloat(2))

  /**
   * Saves this rotation to tag.
   */
  fun toTag() = tagList<FloatTag> {
    add(x)
    add(y)
    add(z)
  }

  companion object {

    /**
     * Empty rotation
     */
    @JvmField
    val EMPTY = Rot()
  }
}
