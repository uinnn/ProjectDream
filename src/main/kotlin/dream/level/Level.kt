package dream.level

import dream.*
import dream.api.*
import dream.block.*
import dream.block.material.*
import dream.block.state.*
import dream.collision.*
import dream.entity.base.*
import dream.entity.player.*
import dream.level.accessor.*
import dream.level.biome.*
import dream.level.border.*
import dream.level.chunk.*
import dream.level.provider.*
import dream.level.storage.*
import dream.misc.*
import dream.nbt.*
import dream.nbt.types.*
import dream.pos.*
import dream.tiles.*
import dream.utils.*
import korlibs.memory.*
import java.util.*


/**
 * A base class for any levels.
 */
@Open
abstract class Level(
  override val name: String,
  val provider: LevelProvider,
  val saveHandler: ISaveHandler,
) : Nameable, Identifier, CompoundStorable, EntityAccessor, BlockAccessor, Tickable, Comparable<Level> {
  
  /**
   * The unique id of this level.
   */
  override val id: UUID = randomUUID()
  
  /**
   * Gets the void level of this world.
   */
  var voidLevel = -64
  
  /**
   * Gets the sea level of this world.
   */
  var seaLevel = 63
  
  /**
   * If true, block ticks will be schedule immediately.
   */
  var scheduledUpdatesAreImmediate = false
  
  /**
   * The time in ticks of this world.
   *
   * Ranging from 0 to 23999.
   */
  var time = 0
  
  /**
   * The rain data of this level.
   */
  var rain = Rain()
  
  /**
   * Contains the current Linear Congruential Generator seed for block updates.
   *
   * Used with an A value of 3 and a C value of 0x3c6ef35f, producing a highly planar series
   * of values ill-suited for choosing random blocks in a 16x128x16 field.
   */
  var updateLCG = randomInt()
  
  /**
   * Is a temporary list of blocks and light values used when updating light levels.
   *
   * Holds up to 32x32x32 blocks (the maximum influence of a light source)
   *
   * Every element is a packed bit value: 0000000000LLLLzzzzzzyyyyyyxxxxxx.
   *
   * The 4-bit L is a light level used when darkening blocks.
   *
   * 6-bit numbers x, y and z represent the block's offset from the original block.
   *
   * plus 32 (i.e. value of 31 would mean a -1 offset
   */
  var lightUpdateBlockList = IntArray(32768)
  
  /**
   * How much light is subtracted from full daylight.
   */
  var skylightSubtracted = 0
  
  /**
   * Gets if this level has sky.
   */
  var hasSky: Boolean
    get() = provider.hasSky
    set(value) {
      provider.hasSky = value
    }
  
  /**
   * Gets the current moon phase of this level.
   */
  val moonPhase get() = provider.moonPhase
  val moonPhaseIndex get() = provider.moonPhaseIndex
  
  /**
   * Gets the player storage for this level.
   */
  val playerStorage get() = saveHandler.playerStorage
  
  var border = LevelBorder()
  
  /**
   * Used to initialize this level.
   */
  fun initialize(): Level {
    return this
  }
  
  /**
   * Gets all players.
   */
  override val players = ArrayList<Player>()
  
  /**
   * Gets all entities of this world mapped by their serial id.
   */
  override val entitiesById = IntObjectMap<Entity>()
  
  /**
   * Gets all entities of this world mapped by their unique id.
   */
  override val entitiesByUUID = HashMap<UUID, Entity>()
  
  /**
   * Gets all tile entities loaded on this world mapped by their position.
   */
  override val tilesByPos = HashMap<Pos, Tile>()
  
  /**
   * Spawns the given entity on this level.
   */
  fun spawn(entity: Entity) {
    val x = (entity.x / 16.0).toIntFloor()
    val z = (entity.z / 16.0).toIntFloor()
    if (entity is Player) {
      players += entity
    }

    chunkAt(x, z).addEntity(entity)
    entitiesById[entity.serialId] = entity
    entitiesByUUID[entity.id] = entity
  }

  fun removeEntity(entity: Entity) {
    entity.setDead()
    if (entity is Player) {
      players -= entity
    }

    val x = entity.chunkX
    val z = entity.chunkZ
    if (entity.addedToChunk && isChunkLoaded(x, z, true)) {
      chunkAt(x, z).removeEntity(entity)
    }

    entitiesById.remove(entity.serialId)
    entitiesByUUID.remove(entity.id)
  }
  
  /**
   * Adds a tile entity to this world.
   */
  fun addTile(tile: Tile) {
  
  }
  
  /**
   * Sets a tile entity to the given [pos].
   */
  fun setTile(pos: Pos, tile: Tile?) {
  
  }
  
  fun removeTile(pos: Pos) {
  
  }
  
  /**
   * Gets the chunk manager from the level provider.
   */
  val chunkManager get() = provider.chunkManager
  
  /**
   * Gets the current active chunks coordinates of this level.
   */
  var activeChunks = HashSet<ChunkCoordinate>()
  
  /**
   * The chunk provider of this level.
   *
   * This handles chunk operations and caching.
   */
  var chunkProvider: ChunkProvider = createChunkProvider()
  
  /**
   * Creates a chunk provider for this level.
   */
  abstract fun createChunkProvider(): ChunkProvider
  
  /**
   * Determinates if the given pos is valid for this level.
   */
  fun isValid(pos: Pos): Boolean {
    return pos.x in -30e6..30e6 && pos.z in -30e6..30e6 && pos.y in 0.0..256.0
  }
  
  /**
   * Checks if a chunk at the given coordinates is loaded.
   */
  fun isChunkLoaded(x: Int, z: Int, allowEmpty: Boolean): Boolean {
    return chunkProvider.exists(x, z) && (allowEmpty || !chunkProvider.provide(x, z).isEmpty)
  }
  
  /**
   * Checks if block at given [pos] is loaded.
   */
  fun isBlockLoaded(pos: Pos, allowEmpty: Boolean = true): Boolean {
    return if (!isValid(pos)) false else isChunkLoaded(pos.flooredX shr 4, pos.flooredZ shr 4, allowEmpty)
  }
  
  /**
   * Checks if area at given center [pos] in a [radius] is loaded.
   */
  fun isAreaLoaded(center: Pos, radius: Int, allowEmpty: Boolean = true): Boolean {
    val x = center.flooredX
    val y = center.flooredY
    val z = center.flooredZ
    return isAreaLoaded(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius, allowEmpty)
  }
  
  /**
   * Checks if rectangular area at [from] to [to] is loaded.
   */
  fun isAreaLoaded(from: Pos, to: Pos, allowEmpty: Boolean = true): Boolean {
    return isAreaLoaded(from.flooredX, from.flooredY, from.flooredZ, to.flooredX, to.flooredY, to.flooredZ, allowEmpty)
  }
  
  /**
   * Checks if rectangular area at [box] is loaded.
   */
  fun isAreaLoaded(box: AABB, allowEmpty: Boolean = true): Boolean {
    return isAreaLoaded(box.min, box.max, allowEmpty)
  }
  
  /**
   * Checks if a rectangular area is loaded.
   */
  fun isAreaLoaded(
    minX: Int,
    minY: Int,
    minZ: Int,
    maxX: Int,
    maxY: Int,
    maxZ: Int,
    allowEmpty: Boolean = true,
  ): Boolean {
    if (maxY < 0 && minY >= 256) return false
    
    for (x in (minX shr 4)..(maxX shr 4)) {
      for (z in (minZ shr 4)..(maxZ shr 4)) {
        if (!isChunkLoaded(x, z, allowEmpty)) return false
      }
    }
    
    return true
  }
  
  /**
   * Gets a chunk at the given [pos].
   */
  fun chunkAt(pos: Pos): Chunk {
    return chunkProvider.provide(pos)
  }
  
  /**
   * Returns back a chunk looked up by chunk coordinates.
   */
  fun chunkAt(chunkX: Int, chunkZ: Int): Chunk {
    return chunkProvider.provide(chunkX, chunkZ)
  }
  
  /**
   * Determinates if the given coordinates pos is void.
   *
   * By default only checks if Y is below -64.
   */
  fun isVoid(x: Double, y: Double, z: Double): Boolean {
    return y < -64
  }
  
  /**
   * Checks to see if an air block exists at the provided location.
   *
   * As this checks only the material, any block that have their material as AIR can
   * pass by this check.
   *
   * If you want to verify if a block is exactly a AIR block use [isAirBlock].
   */
  fun isAir(pos: Pos): Boolean {
    return getBlock(pos).material == Materials.AIR
  }
  
  /**
   * Checks if block is a exactly block air.
   */
  fun isAirBlock(pos: Pos): Boolean {
    return getBlock(pos) is BlockAir
  }
  
  override fun getBlockOrNull(x: Int, y: Int, z: Int): Block? {
    return chunkAt(x shl 4, z shl 4).getBlockOrNull(x, y, z)
  }
  
  /**
   * Get block at the given coordinates.
   */
  override fun getBlock(x: Int, y: Int, z: Int, default: Block): Block {
    return chunkAt(x shl 4, z shl 4).getBlock(x, y, z, default)
  }
  
  override fun getStateOrNull(x: Int, y: Int, z: Int): IState? {
    return chunkAt(x shl 4, z shl 4).getStateOrNull(x, y, z)
  }
  
  /**
   * Get state at the given coordinates.
   */
  override fun getState(x: Int, y: Int, z: Int, default: IState): IState {
    return chunkAt(x shl 4, z shl 4).getState(x, y, z, default)
  }
  
  /**
   * Set state at the given coordinates.
   */
  override fun setState(x: Int, y: Int, z: Int, state: IState, flags: Int): Boolean {
    return setState(Pos(x, y, z), state, flags)
  }
  
  override fun setState(pos: Pos, state: IState, flags: Int): Boolean {
    if (!isValid(pos)) return false
    
    val chunk = chunkAt(pos)
    val oldState = chunk.setState(pos, state) ?: return false
    val block = state.block
    val oldBlock = oldState.block
    
    if (block.lightOpacity != oldBlock.lightOpacity || block.lightEmitted != oldBlock.lightEmitted) {
      checkLight(pos)
    }
    
    if (flags and 2 != 0 && (flags and 4 == 0) && chunk.isPopulated) {
      markBlockToUpdate(pos)
    }
    
    if (flags and 1 != 0) {
      notifyBlockChange(pos, block)
      if (block.hasComparatorInputOverride()) {
        updateComparators(pos, block)
      }
    }
    
    return true
  }
  
  fun notifyBlockChange(pos: Pos, neighbor: Block) {
    getState(pos).onNeighborChange(this, pos, neighbor)
  }
  
  fun notifyNeighborChange(pos: Pos, neighbor: Block) {
    pos.forEachConnected { relative, _ ->
      notifyBlockChange(relative, neighbor)
    }
  }
  
  fun markBlockToUpdate(pos: Pos) {
    TODO()
  }
  
  fun checkLightFor(pos: Pos, light: Light): Boolean {
    TODO()
  }
  
  fun checkLight(pos: Pos): Boolean {
    return if (hasSky) {
      checkLightFor(pos, Light.SKY) || checkLightFor(pos, Light.BLOCK)
    } else {
      checkLightFor(pos, Light.BLOCK)
    }
  }
  
  /**
   * Notify light set at the given pos.
   */
  fun notifyLightSet(pos: Pos) {
    TODO()
  }
  
  fun setChunkDirtyAt(pos: Pos, tile: Tile) {
    if (isBlockLoaded(pos)) {
      chunkAt(pos).isModified = true
    }
  }
  
  fun updateComparators(pos: Pos, block: Block) {
    pos.forEachHorizontal { relative, direction ->
      if (isBlockLoaded(relative)) {
        var state = getState(relative)
        
        // TODO:
        // implement the blocks comparators
        when {
          //Blocks.COMPARATOR.isAssociated(state.block) -> state.block.onNeighborChange(this, relative, state, block)
          state.block.isNormalCube -> {
            val relative2 = relative.relative(direction)
            state = getState(relative2)
            //if (Blocks.COMPARATOR.isAssociated(state.block))
            state.block.onNeighborChange(this, relative2, state, block)
            
          }
        }
      }
    }
  }
  
  fun getBiomeGeneratorAt(pos: Pos): BiomeGenerator {
    TODO()
  }
  
  override fun tick(partial: Int) {
    for (entity in entities) {
      entity.tick(partial)
    }
  }
  
  /**
   * Plays a sound in the given coordinates.
   */
  fun playSound(sound: Sound, x: Int, y: Int, z: Int, volume: Float = 1f, pitch: Float = 1f) {
  
  }
  
  /**
   * Plays a sound in the given coordinates.
   */
  fun playSound(sound: Sound, pos: Pos, volume: Float = 1f, pitch: Float = 1f) {
  
  }
  
  /**
   * Plays a sound in the given entity coordinates.
   */
  fun playSound(sound: Sound, entity: Entity, volume: Float = 1f, pitch: Float = 1f) {
  
  }
  
  /**
   * Plays a sound for all players in the given coordinates.
   */
  fun playSoundAll(sound: Sound, x: Int, y: Int, z: Int, volume: Float = 1f, pitch: Float = 1f) {
  
  }
  
  /**
   * Plays a sound for all players in the given coordinates.
   */
  fun playSoundAll(sound: Sound, pos: Pos, volume: Float = 1f, pitch: Float = 1f) {
  
  }
  
  /**
   * Plays a sound for all players in the given entity coordinates.
   */
  fun playSoundAll(sound: Sound, entity: Entity, volume: Float = 1f, pitch: Float = 1f) {
  
  }
  
  fun sendDigProgress(breaker: Entity, pos: Pos, progress: Int) {
  
  }
  
  /**
   * Saves this level to [tag].
   */
  override fun save(tag: CompoundTag) {
  
  }
  
  /**
   * Loads this level from [tag].
   */
  override fun load(tag: CompoundTag) {
  
  }
  
  override fun compareTo(other: Level): Int {
    return id.compareTo(other.id)
  }
}
