package dream.pos

import dream.block.*
import dream.block.state.*
import dream.entity.*
import dream.level.*

/**
 * Represents a immutable coordinate-system with a level.
 */
class LevelPos(val level: Level, x: Double, y: Double, z: Double) : Pos(x, y, z) {
   
   constructor(level: Level) : this(level, 0.0, 0.0, 0.0)
   
   constructor(level: Level, x: Number, y: Number, z: Number) : this(level, x.toDouble(), y.toDouble(), z.toDouble())
   
   constructor(level: Level, codified: Long) : this(level, getX(codified), getY(codified), getZ(codified))
   
   constructor(entity: Entity) : this(entity.level, entity.x, entity.y, entity.z)
   
   val chunk get() = chunkAt(level)
   val tile get() = tileAt(level)
   val state get() = state()
   val block get() = block()
   
   fun stateOrNull() = stateAtOrNull(level)
   fun state(default: IState = Blocks.AIR.state) = stateAt(level, default)
   
   fun blockOrNull() = blockAtOrNull(level)
   fun block(default: Block = Blocks.AIR) = blockAt(level, default)
   
   override fun copy(): LevelPos {
      return LevelPos(level, x, y, z)
   }
   
   override fun asLevelPos(level: Level): LevelPos {
      return this
   }
   
}
