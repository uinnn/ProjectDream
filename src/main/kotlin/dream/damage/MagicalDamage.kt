package dream.damage

/**
 * Represents a magical damage.
 */
abstract class MagicalDamage(name: String) : AbstractDamage(name) {
  init {
    isUnblockable = true
  }
}

/**
 * Returns if this damage is a magical damage.
 */
val Damage.isMagical: Boolean
  get() = this is MagicalDamage
