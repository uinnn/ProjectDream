package dream.entity

import dream.entity.base.*
import dream.item.*
import dream.misc.*
import dream.network.*
import dream.pos.*
import dream.utils.*
import kotlin.reflect.*

/**
 * A [Watcher] is used to watch and sync entity
 * metadata from packets and manipulates them.
 */
@Open
class Watcher(val entity: Entity) {

  /**
   * All objects being watched.
   */
  var objects = IntObjectMap<WatcherValue>()

  /**
   * Returns if this watcher is blank.
   */
  var isBlank = true

  /**
   * Returns if this watcher has changed.
   */
  var isChanged = false
  
  /**
   * Used to avoid [ConcurrentModificationException].
   *
   * All entities must register watcher values on their initialization.
   *
   * After the entity initialization, this will be setted to `true` to avoid errors.
   */
  var isLocked = false

  /**
   * Gets all watched objects.
   */
  val allWatched: List<WatcherValue>
    get() = objects.values.toList()

  /**
   * Gets all changed objects.
   */
  val allChanged: List<WatcherValue>
    get() {
      if (!isChanged) {
        return emptyList()
      }

      return objects.values.mapNotNull { obj ->
        if (obj.watched) { obj.watched = false; obj } else null
      }
    }

  /**
   * Writes this watcher to PacketBuffer.
   */
  fun write(buf: PacketBuffer) {
    objects.values.forEach { writeObject(buf, it) }
    buf.writeByte(127)
  }

  /**
   * Adds a new watchable object on this watcher.
   */
  fun add(id: Int, value: Any): Watcher {
    require(!isLocked)
    if (id in objects) return this
    objects.put(id, WatcherValue(id(value::class), id, value))
    return this
  }

  /**
   * Adds a byte object on this watcher.
   */
  fun addByte(id: Int, value: Byte): Watcher {
    return add(id, value)
  }

  /**
   * Adds a short object on this watcher.
   */
  fun addShort(id: Int, value: Short): Watcher {
    return add(id, value)
  }

  /**
   * Adds a int object on this watcher.
   */
  fun addInt(id: Int, value: Int): Watcher {
    return add(id, value)
  }

  /**
   * Adds a float object on this watcher.
   */
  fun addFloat(id: Int, value: Float): Watcher {
    return add(id, value)
  }

  /**
   * Adds a string object on this watcher.
   */
  fun addString(id: Int, value: String): Watcher {
    return add(id, value)
  }

  /**
   * Adds a item object on this watcher.
   */
  fun addItem(id: Int, value: ItemStack): Watcher {
    return add(id, value)
  }

  /**
   * Adds a pos object on this watcher.
   */
  fun addPos(id: Int, value: Pos): Watcher {
    return add(id, value)
  }

  /**
   * Adds a rotation object on this watcher.
   */
  fun addRotation(id: Int, value: Rot): Watcher {
    return add(id, value)
  }

  /**
   * Gets a watchable object on this watcher.
   */
  fun get(id: Int): WatcherValue {
    return objects.get(id)
  }

  /**
   * Gets a watchable object on this watcher.
   */
  fun getOrNull(id: Int): WatcherValue? {
    return objects.get(id) ?: null
  }

  /**
   * Returns the value of the watchable object present castted.
   */
  final inline fun <reified T> value(id: Int): T {
    return get(id).value as T
  }

  /**
   * Returns the value of the watchable object present castted.
   */
  final inline fun <reified T> valueOrNull(id: Int): T? {
    return getOrNull(id)?.value as? T
  }

  operator fun contains(id: Int) = getOrNull(id) != null

  /**
   * Gets a watchable object byte value on this watcher.
   */
  fun byte(id: Int, default: Byte = 0): Byte {
    return valueOrNull(id) ?: default
  }

  /**
   * Gets a watchable object byte value on this watcher.
   */
  fun byteOrNull(id: Int): Byte? {
    return valueOrNull(id)
  }

  /**
   * Gets a watchable object short value on this watcher.
   */
  fun short(id: Int, default: Short = 0): Short {
    return valueOrNull(id) ?: default
  }

  /**
   * Gets a watchable object byte value on this watcher.
   */
  fun shortOrNull(id: Int): Short? {
    return valueOrNull(id)
  }

  /**
   * Gets a watchable object int value on this watcher.
   */
  fun int(id: Int, default: Int = 0): Int {
    return valueOrNull(id) ?: default
  }

  /**
   * Gets a watchable object int value on this watcher.
   */
  fun intOrNull(id: Int): Int? {
    return valueOrNull(id)
  }

  /**
   * Gets a watchable object float value on this watcher.
   */
  fun float(id: Int, default: Float = 0f): Float {
    return valueOrNull(id) ?: default
  }

  /**
   * Gets a watchable object float value on this watcher.
   */
  fun floatOrNull(id: Int): Float? {
    return valueOrNull(id)
  }

  /**
   * Gets a watchable object string value on this watcher.
   */
  fun string(id: Int, default: String = ""): String {
    return valueOrNull(id) ?: default
  }

  /**
   * Gets a watchable object string value on this watcher.
   */
  fun stringOrNull(id: Int): String? {
    return valueOrNull(id)
  }

  /**
   * Gets a watchable object item stack value on this watcher.
   */
  fun item(id: Int, default: ItemStack = EmptyItemStack): ItemStack {
    return valueOrNull(id) ?: default
  }

  /**
   * Gets a watchable object item stack value on this watcher.
   */
  fun itemOrNull(id: Int): ItemStack? {
    return valueOrNull(id)
  }

  /**
   * Gets a watchable object point value on this watcher.
   */
  fun pos(id: Int, default: Pos = Pos.ZERO): Pos {
    return valueOrNull(id) ?: default
  }

  /**
   * Gets a watchable object point value on this watcher.
   */
  fun posOrNull(id: Int): Pos? {
    return valueOrNull(id)
  }

  /**
   * Gets a watchable object rotation value on this watcher.
   */
  fun rotation(id: Int, default: Rot = Rot.EMPTY): Rot {
    return valueOrNull(id) ?: default
  }

  /**
   * Gets a watchable object rotation value on this watcher.
   */
  fun rotationOrNull(id: Int): Rot? {
    return valueOrNull(id)
  }

  /**
   * Updates an existent watchable object specified by [id].
   */
  fun update(id: Int, value: Any): Watcher {
    val obj = getOrNull(id) ?: return this
    if (obj.value != value) {
      obj.value = value
      entity.onWatcherUpdate(id, value)
      obj.watched = true
      isChanged = true
    }

    return this
  }

  /**
   * Sets an existent watchable object specified by [id] as watched.
   */
  fun watch(id: Int): Watcher {
    getOrNull(id)?.watched = true
    isChanged = true
    return this
  }

  companion object {
    private val DATA = ObjectByteMap<KClass<*>>(8).apply {
      put(Byte::class, 0)
      put(Short::class, 1)
      put(Int::class, 2)
      put(Float::class, 3)
      put(String::class, 4)
      put(ItemStack::class, 5)
      put(Pos::class, 6)
      put(Rot::class, 7)
    }

    /**
     * Gets the id of a watchable object with [key].
     */
    fun id(key: KClass<*>) = DATA.getByte(key).toInt()

    /**
     * Writes [obj] in [buf].
     */
    fun writeObject(buf: PacketBuffer, obj: WatcherValue) {
      buf.writeByte(obj.uniqueId)

      when (obj.type) {
        0 -> buf.writeByte((obj.value as Byte).toInt())
        1 -> buf.writeShort((obj.value as Short).toInt())
        2 -> buf.writeInt(obj.value as Int)
        3 -> buf.writeFloat(obj.value as Float)
        4 -> buf.writeString(obj.value as String)
        5 -> buf.writeItem(obj.value as ItemStack)
        6 -> buf.writePos(obj.value as Pos)
        7 -> buf.writeRotation(obj.value as Rot)
        else -> {}
      }
    }

    /**
     * Read a [WatcherValue] from [buf] by given [type] and [id].
     */
    fun readObject(buf: PacketBuffer, type: Int, id: Int): WatcherValue {
      return when (type) {
        0 -> WatcherValue(type, id, buf.readByte())
        1 -> WatcherValue(type, id, buf.readShort())
        2 -> WatcherValue(type, id, buf.readInt())
        3 -> WatcherValue(type, id, buf.readFloat())
        4 -> WatcherValue(type, id, buf.readString())
        5 -> WatcherValue(type, id, buf.readItem())
        6 -> WatcherValue(type, id, buf.readPos())
        7 -> WatcherValue(type, id, buf.readRotation())
        else -> WatcherValue(0, id, 0, false)
      }
    }

    /**
     * Read a list of [WatcherValue] from [buf].
     */
    fun readObjects(buf: PacketBuffer): List<WatcherValue> = buildList {
      var index = buf.readByte()
      while (index != 127.toByte()) {
        val type = ((index and 224) shl 5).toInt()
        val id = (index and 31).toInt()

        add(readObject(buf, type, id))
        index = buf.readByte()
      }
    }

    /**
     * Writes all [list] watcher values in [buf].
     */
    fun writeObjects(buf: PacketBuffer, list: List<WatcherValue>) {
      if (list.isNotEmpty()) {
        list.forEach { value ->
          writeObject(buf, value)
        }
      }

      buf.writeByte(127)
    }
  }
}

/**
 * A set of some data representing a watchable object in [Watcher].
 */
data class WatcherValue(var type: Int, var valueId: Int, var value: Any, var watched: Boolean = true) {
  val uniqueId get() = type shl 5 or (valueId and 31) and 255
}

/**
 * Creates and builds a new watcher.
 */
inline fun watcher(entity: Entity, builder: Builder<Watcher>): Watcher {
  return Watcher(entity).apply(builder)
}
