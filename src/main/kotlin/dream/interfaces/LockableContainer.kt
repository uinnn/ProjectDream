package dream.interfaces

import dream.inventory.*

/**
 * Represents a lockable container.
 */
public interface LockableContainer : IInventory, Lockable, Containerable
