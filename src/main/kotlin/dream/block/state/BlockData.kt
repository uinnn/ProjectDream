package dream.block.state

import com.google.common.collect.*
import dream.block.*
import dream.block.property.*
import dream.misc.*
import dream.utils.*
import java.util.*

/**
 * Represents a set of property/state of a block.
 */
@Open
data class BlockData(val block: Block, var properties: List<Prop>, var validStates: List<IState>) {

  //constructor(block: Block) : this(block, emptyList(), emptyList())
  constructor(block: Block, vararg props: Prop) : this(block, ArrayList(), ArrayList()) {
    Arrays.sort(props)
    properties = ImmutableList.copyOf(props)

    val data = LinkedHashMap<PropertyMap, State>()
    val states = ArrayList<State>()

    for (cartesian in properties.map(Prop::allowedValues).cartesian()) {
      val map = populateMap(properties, cartesian)
      val state = State(block, ImmutableMap.copyOf(map))
      data[map] = state
      states += state
    }

    for (state in states) {
      state.buildTable(data)
      println(state.toString())
    }

    this.validStates = ImmutableList.copyOf(states)

    println(this.toString())
  }

  //val baseState get() = validStates[0]
  val baseState = State(block, ImmutableMap.of())
  val allowedValues get() = properties.map(Prop::allowedValues)
}
