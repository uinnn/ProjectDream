package dream.block.state

import com.google.common.collect.*
import dream.block.*
import dream.block.property.*
import kotlin.reflect.*

/**
 * Represents a base block state.
 */
class State(override val block: Block, override val values: PropertyMap) : IState {

  /**
   * The table of this state.
   */
  lateinit var table: Table<Prop, Any, IState>

  /**
   * Checks if the [table] of this state has been initialized.
   */
  val isTableInitialized get() = this::table.isInitialized

  /**
   * Gets all properties keys of this block state.
   */
  override val properties get() = values.keys

  /**
   * Gets the current value of [property] on this block state.
   */
  override fun <T : Any> getValue(property: Property<T>): T {
    if (property !in values) {
      error("Cannot get property $property value because it not exists in $block")
    }

    return property.valueClass.cast(values[property])
  }

  /**
   * Gets a new state with [value] setted by [property].
   */
  override fun <T : Any> with(property: Property<T>, value: T): IState {
    if (property !in values) {
      error("Cannot set property $property value because it not exists in $block")
    }

    if (value !in property.allowedValues) {
      error("Cannot set property $property value because it not is allowed in $block")
    }

    return if (values[property] == value) this else table[property, value]!!
  }

  fun buildTable(data: Map<MutableMap<Prop, Any>, IState>) {
    if (isTableInitialized) {
      error("Table has been initialized")
    }

    table = HashBasedTable.create()
    for ((key, value) in values) {
      for (allowed in key.allowedValues) {
        if (allowed != value) {
          val state = data[computeNewMap(key, allowed)]
          if (state != null) {
            table.put(key, allowed, state)
          }
        }
      }
    }
  }

  override fun toString(): String {
    return "State(block=$block, values=${values.values.joinToString()})"
  }

  override fun equals(other: Any?): Boolean {
    return this === other
  }

  override fun hashCode(): Int {
    return values.hashCode()
  }

  private fun computeNewMap(prop: Prop, value: Any): HashMap<Prop, Any> {
    val map = HashMap(values)
    map[prop] = value
    return map
  }
}
