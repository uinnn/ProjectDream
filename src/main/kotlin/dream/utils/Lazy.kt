package dream.utils

import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> mutableLazy(lock: Any? = null, initializer: () -> T) = MutableLazy(initializer, lock)

internal object UninitializedLazy

open class MutableLazy<out T>(
  initializer: () -> T, lock: Any? = null,
) : Lazy<T>, Serializable, ReadWriteProperty<Any, @UnsafeVariance T> {
  private var initializer: (() -> T)? = initializer
  @Volatile
  private var _value: Any? = UninitializedLazy

  // final field is required to enable safe publication of constructed instance
  private val lock = lock ?: this

  override val value: T
    get() {
      val v1 = _value
      if (v1 !== UninitializedLazy) {
        @Suppress("UNCHECKED_CAST") return v1 as T
      }

      return synchronized(lock) {
        val v2 = _value
        if (v2 !== UninitializedLazy) {
          @Suppress("UNCHECKED_CAST") (v2 as T)
        } else {
          val typedValue = initializer!!()
          _value = typedValue
          initializer = null
          typedValue
        }
      }
    }

  override fun isInitialized(): Boolean = _value !== UninitializedLazy

  override fun getValue(thisRef: Any, property: KProperty<*>): T {
    return value
  }

  override fun setValue(thisRef: Any, property: KProperty<*>, value: @UnsafeVariance T) {
    _value = value
  }

  override fun toString(): String = if (isInitialized()) value.toString() else "Lazy value not initialized yet."
}
