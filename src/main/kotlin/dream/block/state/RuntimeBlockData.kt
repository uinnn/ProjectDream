package dream.block.state

import dream.block.*
import dream.block.property.*

/**
 * Represents a [RuntimeState]-based [BlockData].
 */
class RuntimeBlockData : BlockData {

  constructor(block: Block) : super(block)
  constructor(block: Block, properties: List<Prop>, states: List<IState>) : super(block, properties, states)
  constructor(block: Block, vararg props: Prop) : super(block) {
    properties = props.sortedBy { it.name }
    states = properties.map { RuntimeState(block, HashMap()) }
  }

}
