package dream.block.property

import com.google.common.collect.*
import dream.pos.*
import dream.utils.*

/**
 * A direction block property.
 */
public class PropertyDirection(
   name: String,
   allowedValues: Iterable<Direction>,
) : PropertyEnum<Direction>(name, Direction::class, allowedValues) {
   
   constructor(name: String) : this(name, enumSet<Direction>())
   
   constructor(name: String, predicate: (Direction) -> Boolean) :
      this(name, Sets.filter(enumSet<Direction>(), predicate))
   
}
