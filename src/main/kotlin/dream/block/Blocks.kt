package dream.block

import dream.*
import dream.block.state.*
import dream.registry.*

/**
 * Represents a registry for blocks.
 */
public object Blocks : NamespacedRegistry<Key, Block>() {
   
   /**
    * All blocks states by id.
    */
   val STATES = IdRegistry<IState>()
   
   @JvmField val AIR = register0(0, "air", BlockAir())
   @JvmField val STONE = register0(1, "stone", Block())
   
   override fun register(id: Int, key: Key, value: Block) {
      super.register(id, key, value)
      for (state in value.data.states) {
         STATES[state] = id shl 4 or value.getMetadataFromState(state)
      }
   }
   
   private fun <T : Block> register0(id: Int, key: String, value: T): T {
      register(id, minecraftKey(key), value)
      return value
   }
   
   /**
    * Gets a block by name.
    *
    * This tries parsing [name] as Key and if not found will try to parse as Int.
    */
   fun byName(name: String): Block? {
      return get(key(name)) ?: get(name.toIntOrNull() ?: return null)
   }
   
   fun byStateIdOrNull(id: Int) = get(id and 4095)
   fun byStateId(id: Int, default: Block = AIR) = byStateIdOrNull(id) ?: default
   
   fun stateByIdOrNull(id: Int) = byStateIdOrNull(id)?.getStateFromMetadata(id shr 12 and 15)
   fun stateById(id: Int, default: IState = AIR.state) = stateByIdOrNull(id) ?: default
   
}
