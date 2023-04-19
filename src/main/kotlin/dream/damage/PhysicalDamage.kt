package dream.damage

/**
 * Represents a physical damage.
 */
abstract class PhysicalDamage(name: String) : AbstractDamage(name)

/**
 * Returns if this damage is a physical damage.
 */
val Damage.isPhysical: Boolean
   get() = this is PhysicalDamage
