package dream.entity

/**
 * Represents a entity that can be owned.
 */
interface EntityOwnable {
   
   /**
    * Gets the owner id.
    */
   var ownerId: String
   
   /**
    * Gets the owner.
    */
   val owner: Entity
   
   /**
    * Gets if [entity] is the owner of this ownable entity.
    */
   fun isOwner(entity: Entity)
}
