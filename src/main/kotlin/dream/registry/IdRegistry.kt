package dream.registry

import com.google.common.collect.*
import dream.misc.*
import java.util.*
import kotlin.collections.set

/**
 * Represents a registry based on id's
 */
@Open
class IdRegistry<T> : Iterable<T> {
  val map: IdentityHashMap<T, Int> = IdentityHashMap(512)
  val list: MutableList<T?> = Lists.newArrayList()

  fun put(key: T, value: Int) {
    map[key] = value
    while (list.size <= value) {
      list.add(null)
    }
    list[value] = key
  }

  fun getId(key: T): Int {
    return map[key] ?: -1
  }

  fun getValue(index: Int): T? {
    return list.getOrNull(index)
  }

  override fun iterator(): Iterator<T> {
    return map.keys.iterator()
  }
}
