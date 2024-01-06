package dream.entity

import dream.*
import dream.api.*
import dream.entity.base.*
import dream.errors.*
import dream.level.*
import dream.misc.*
import dream.nbt.types.*
import dream.utils.*
import kotlin.reflect.*

/**
 * Represents a basic entity type.
 *
 * Can be used to create new entity types.
 */
@Open
class EntityType<T : Entity>(
  val type: KClass<T>,
  val id: Int,
  override val key: Key,
  override val name: String,
) : Keyable, Nameable {

  /**
   * Creates a new entity by this type.
   *
   * Since this not contains a level parameter, some entity data will not be present.
   */
  fun createEmpty(): T {
    return try {
      type.newInstance()
    } catch (ex: Throwable) {
      throwCreationException(ex)
    }
  }

  /**
   * Creates a new entity by this type and loads them.
   */
  fun createEmpty(tag: CompoundTag): T {
    val entity = createEmpty()
    entity.load(tag)
    return entity
  }

  /**
   * Creates a new entity by this type.
   */
  fun create(level: Level): T {
    return try {
      type.newInstance(level)
    } catch (ex: Throwable) {
      throwCreationException(ex)
    }
  }

  /**
   * Creates a new entity by this type and loads them.
   */
  fun create(level: Level, tag: CompoundTag): T {
    val entity = create(level)
    entity.load(tag)
    return entity
  }

  private fun throwCreationException(ex: Throwable): Nothing {
    throw MinecraftException("Failed to create entity", ex)
      .categories("Class" to type, "Name" to name, "Id" to id, "Key" to key)
  }

}
