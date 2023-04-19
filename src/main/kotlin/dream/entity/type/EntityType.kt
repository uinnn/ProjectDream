package dream.entity.type

import dream.*
import dream.entity.*
import dream.errors.*
import dream.interfaces.*
import dream.level.*
import dream.nbt.types.*
import dream.utils.*
import kotlin.reflect.*

/**
 * Represents a basic entity type.
 *
 * Can be used to create new entity types.
 */
open class EntityType<T : Entity>(
   val type: KClass<T>,
   val id: Int,
   override val key: Key,
   override val name: String,
) : Keyable, Nameable {
   
   /**
    * Creates a new entity by this type.
    */
   open fun create(level: Level): T {
      return try {
         type.newInstance(level)
      } catch (ex: Exception) {
         throw EntityCreateException(
            """
                    Failed to create entity of type $name.
                    Class: $type
                    Key: $key
                    ID: $id
                    Name: $name
                """,
            ex
         )
      }
   }
   
   /**
    * Creates a new entity by this type and loads them.
    */
   open fun createByTag(level: Level, tag: CompoundTag): T {
      val entity = create(level)
      entity.load(tag)
      return entity
   }
   
}
