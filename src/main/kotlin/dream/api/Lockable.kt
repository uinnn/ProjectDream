package dream.api

import dream.misc.LockCode

/**
 * Represents a object that can be locked.
 */
interface Lockable {
  var code: LockCode
  val isLocked: Boolean
}
