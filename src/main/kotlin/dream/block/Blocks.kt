package dream.block

import dream.*
import dream.block.state.*
import dream.registry.*

/**
 * Represents a registry for blocks.
 */
object Blocks : NamespacedRegistry<Key, Block>() {

  /**
   * All blocks states by id.
   */
  val STATES = IdRegistry<IState>()

  @JvmField
  val AIR = register0(0, "air", BlockAir())
  @JvmField
  val STONE = register0(1, "stone", BlockBookshelf())
  @JvmField
  val VINES = register0(2, "vines", BlockChest())
  @JvmField
  val LADDER = register0(3, "ladder", Block())

  override fun register(id: Int, key: Key, value: Block) {
    super.register(id, key, value)
    for (state in value.data.states) {
      STATES[state] = id shl 4 or state.getMetadata()
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
  
  /**
   * Retrieves the block by its state ID or returns null if no block is found.
   *
   * @param id The state ID of the block.
   * @return The block corresponding to the state ID, or null if no block is found.
   */
  fun byStateIdOrNull(id: Int): Block? = get(id and 4095)
  
  /**
   * Retrieves the block by its state ID or returns the default block if no block is found.
   *
   * @param id The state ID of the block.
   * @param default The default block to return if no block is found. Default value is [AIR].
   * @return The block corresponding to the state ID, or the default block if no block is found.
   */
  fun byStateId(id: Int, default: Block = AIR): Block = byStateIdOrNull(id) ?: default
  
  /**
   * Retrieves the state by its ID or returns null if no state is found.
   *
   * @param id The ID of the state.
   * @return The state corresponding to the ID, or null if no state is found.
   */
  fun stateByIdOrNull(id: Int): IState? = byStateIdOrNull(id)?.getStateFromMeta(id shr 12 and 15)
  
  /**
   * Retrieves the state by its ID or returns the default state if no state is found.
   *
   * @param id The ID of the state.
   * @param default The default state to return if no state is found. Default value is [AIR.state].
   * @return The state corresponding to the ID, or the default state if no state is found.
   */
  fun stateById(id: Int, default: IState = AIR.state): IState = stateByIdOrNull(id) ?: default
  

}
