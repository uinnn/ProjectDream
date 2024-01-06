package dream.api

import dream.inventory.IInventory

/**
 * Represents a lockable container.
 */
interface LockableContainer : IInventory, Lockable, Containerable
