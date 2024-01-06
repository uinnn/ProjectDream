package dream.block.state

import dream.block.*
import dream.block.property.*
import dream.misc.*
import dream.utils.*

/**
 * Represents a set of property/state of a block.
 */
@Open
data class BlockData(val block: Block, var properties: List<Prop>, var states: List<IState>) {

  val baseState get() = states[0]
  val allowedValues get() = properties.map(Prop::allowedValues)

  constructor(block: Block) : this(block, emptyList(), emptyList())
  constructor(block: Block, vararg props: Prop) : this(block) {
    properties = props.sortedBy { it.name }

    val data = LinkedHashMap<PropertyMap, IState>()
    val states = ArrayList<State>()

    for (cartesian in allowedValues.cartesian()) {
      val map = populateMap(properties, cartesian)
      val state = State(block, map)
      data[map] = state
      states += state
    }

    for (state in states) {
      state.buildTable(data)
    }

    this.states = states
  }
}
