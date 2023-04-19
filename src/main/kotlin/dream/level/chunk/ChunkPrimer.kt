package dream.level.chunk

import dream.block.*
import dream.block.state.*
import dream.misc.*

@Open
class ChunkPrimer {
   val data = ShortArray(65536)
   val defaultState = Blocks.AIR.state
   
   fun getState(x: Int, y: Int, z: Int): IState {
      return getState(x shl 12 or z shl 8 or y)
   }
   
   fun getState(index: Int): IState {
      val value = data.getOrNull(index) ?: throw IndexOutOfBoundsException("The coordinate is out of range")
      return Blocks.stateByIdOrNull(value.toInt()) ?: defaultState
   }
   
   fun setState(x: Int, y: Int, z: Int, state: IState) {
      setState(x shl 12 or z shl 8 or y, state)
   }
   
   fun setState(index: Int, state: IState) {
      if (index in data.indices) {
         data[index] = Blocks.STATES[state].toShort()
      } else {
         throw IndexOutOfBoundsException("The coordinate is out of range")
      }
   }
}
