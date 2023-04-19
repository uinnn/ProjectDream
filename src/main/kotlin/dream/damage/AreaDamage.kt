package dream.damage

import dream.entity.*

/**
 * Represents a area damage.
 *
 * Area damage causes a damage in a defined radius.
 */
abstract class AreaDamage(
   name: String,
   var radiusX: Double,
   var radiusY: Double,
   var radiusZ: Double,
) : AbstractDamage(name) {
   
   /**
    * Makes the area damage on [entity]
    */
   abstract fun areaDamage(main: Entity, entity: Entity, amount: Float)
   
   /**
    * Gets all entities that's is in radius of this area damage.
    */
   fun nearby(entity: Entity): List<Entity> {
      return entity.nearbyEntities(radiusX, radiusY, radiusZ)
   }
   
   override fun damage(entity: Entity, amount: Float) {
      // assegurate that's will damage the main entity too
      areaDamage(entity, entity, amount)
      for (nearby in nearby(entity)) {
         areaDamage(entity, nearby, amount)
      }
   }
}

/**
 * Returns if this damage is a area damage.
 */
val Damage.isArea: Boolean
   get() = this is AreaDamage
