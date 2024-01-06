package dream.entity

import dream.entity.base.Entity
import dream.utils.Predicate

/**
 * A marker interface for representing an mob.
 */
interface IMob : IAnimal {
  companion object {
    val MOB_SELECTOR: Predicate<Entity> = { it is IMob }
    val VISIBLE_MOB_SELECTOR: Predicate<Entity> = { it is IMob && !it.isInvisible }
  }
}
