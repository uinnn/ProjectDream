package dream.api

import dream.pos.Loc

/**
 * Represents an object that's have a [Loc].
 */
interface Localizable {

  /**
   * The location.
   */
  val location: Loc
}
