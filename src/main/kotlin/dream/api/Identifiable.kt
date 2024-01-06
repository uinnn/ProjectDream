package dream.api

import java.util.*

/**
 * Represents an object that's can have a unique identifier.
 */
interface Identifiable {

  /**
   * The unique id.
   */
  val id: UUID
}
