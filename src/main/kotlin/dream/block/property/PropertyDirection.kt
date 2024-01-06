package dream.block.property

import com.google.common.collect.Sets
import dream.pos.Direction
import dream.utils.enumSet

/**
 * A direction block property.
 */
class PropertyDirection(
  name: String,
  allowedValues: Iterable<Direction>,
) : PropertyEnum<Direction>(name, Direction::class, allowedValues) {

  constructor(name: String) : this(name, enumSet<Direction>())

  constructor(name: String, predicate: (Direction) -> Boolean) :
    this(name, Sets.filter(enumSet<Direction>(), predicate))

}
