package dream.interfaces

import dream.misc.*

/**
 * Represents a object that can be locked.
 */
public interface Lockable {
   public var code: LockCode
   public val isLocked: Boolean
}
