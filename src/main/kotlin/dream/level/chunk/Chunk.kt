package dream.level.chunk

import com.google.common.collect.*
import dream.block.*
import dream.block.state.*
import dream.collections.*
import dream.collision.*
import dream.entity.base.*
import dream.level.*
import dream.level.chunk.storage.*
import dream.misc.*
import dream.nbt.*
import dream.nbt.types.*
import dream.pos.*
import dream.tiles.*
import dream.utils.*
import kotlinx.coroutines.*
import java.util.concurrent.*


/**
 * Represents a chunk in a level.
 */
@Open
class Chunk(val level: Level, var x: Int, var z: Int) : CompoundStorable, Comparable<Chunk> {
  
  /**
   * Used to store block IDs, block MSBs, Sky-light maps, Block-light maps, and metadata.
   * Each entry corresponds to a logical segment of 16x16x16 blocks, stacked vertically.
   */
  var palletes: MutableList<Pallete> = SizedList(16)
  
  /**
   * Contains a 16x16 mapping on the X/Z plane of the biome ID to which each colum belongs.
   */
  var biomes = ByteArray(256) { -1 }
  
  /**
   * Height map of this chunk.
   */
  var heightMap = IntArray(256)
  
  /**
   * A map, similar to heightMap, that tracks how far down precipitation can fall.
   */
  var precipitationHeightMap = IntArray(256) { -999 }
  
  /**
   * Which columns need their skylightMaps updated.
   */
  var updateSkylightColumns = BooleanArray(256)
  
  /**
   * A map that stores tile entities, with positions as keys.
   */
  var tiles: MutableMap<Pos, Tile> = HashMap()
  
  /**
   * Returns the number of tile entities currently stored in the map.
   */
  val tileCount: Int
    get() = tiles.size
  
  /**
   * A set that stores entities.
   */
  var entities: MutableSet<Entity> = HashSet()
  
  /**
   * Returns a sequence of entities currently stored in the set.
   */
  val entitiesSequence: Sequence<Entity>
    get() = entities.asSequence()
  
  /**
   * Contains the current round-robin relight check index, and is implied as the relight check location as well.
   */
  var queuedLightChecks = 4096
  
  /**
   * Whether or not this Chunk is currently loaded into the World.
   */
  var isLoaded = false
  var isGapLightingUpdated = false
  
  /**
   * Indicates if this chunk terrain have been populated.
   */
  var isTerrainPopulated = false
  var isLightPopulated = false
  var isPop = false
  
  /**
   * Checks if this chunk have been populated
   */
  val isPopulated get() = isPop || isTerrainPopulated || isLightPopulated
  
  /**
   * Set to true if the chunk has been modified and needs to be updated internally.
   */
  var isModified = false
  
  /**
   * Whether this Chunk has any Entities and thus requires saving on every tick.
   */
  var hasEntities = false
  
  /**
   * The time according to World.worldTime when this chunk was last saved.
   */
  var lastSaveTime: Long = 0
  
  /**
   * Lowest value in the heightmap.
   */
  var heightMapMinimum = 0
  
  /**
   * the cumulative number of ticks players have been in this chunk.
   */
  var inhabitedTime: Long = 0
  
  /**
   * A queue for tiles entities.
   */
  var tileQueue: ConcurrentLinkedQueue<Pos> = Queues.newConcurrentLinkedQueue()
  
  /**
   * Checks if this chunk is empty.
   */
  val isEmpty get() = false
  
  /**
   * Calculates the total number of filled blocks across all palettes in the chunk.
   *
   * @return The total number of filled blocks.
   */
  val totalBlocks: Int
    get() = palletes.sumOf { it.blockCount }
  
  /**
   * Calculates the total number of filled tickable blocks across all palettes in the chunk.
   *
   * @return The total number of filled tickable blocks.
   */
  val totalTickableBlocks: Int
    get() = palletes.sumOf { it.tickBlocksCount }
  
  constructor(level: Level, primer: ChunkPrimer, chunkX: Int, chunkZ: Int) : this(level, chunkX, chunkZ) {
    val hasSky = level.provider.hasSky
    
    for (x in 0 until 16) {
      for (y in 0 until 16) {
        for (z in 0 until 256) {
          val index = x * 256 * 16 or y * 256 or z
          val state = primer.getState(index)
          
          if (!state.block.isAir) {
            val i = z shr 4
            val storage = palletes.getOrPut(i) { Pallete(i shl 4, hasSky) }
            storage[x, z and 15, i] = state
          }
        }
      }
    }
  }
  
  /**
   * Checks if the specified chunk coordinates are equal to the current chunk's coordinates.
   *
   * @param chunkX The X coordinate of the chunk.
   * @param chunkZ The Z coordinate of the chunk.
   * @return `true` if the current chunk coordinates match the specified chunk coordinates, `false` otherwise.
   */
  fun isOnChunk(chunkX: Int, chunkZ: Int): Boolean {
    return chunkX == x && chunkZ == z
  }
  
  /**
   * Checks if either the X or Z coordinate of the chunk matches the specified chunk coordinates.
   *
   * @param chunkX The X coordinate of the chunk.
   * @param chunkZ The Z coordinate of the chunk.
   * @return `true` if either the X or Z coordinate of the current chunk matches the specified chunk coordinates, `false` otherwise.
   */
  fun isAnyOnChunk(chunkX: Int, chunkZ: Int): Boolean {
    return chunkX == x || chunkZ == z
  }
  
  /**
   * Retrieves the height at the specified position.
   *
   * @param pos The position.
   * @return The height at the specified position.
   */
  fun getHeight(pos: Pos): Int {
    return getHeight(pos.flooredX and 15, pos.flooredZ and 15)
  }
  
  /**
   * Retrieves the height at the specified coordinates.
   *
   * @param x The X coordinate.
   * @param z The Z coordinate.
   * @return The height at the specified coordinates.
   */
  fun getHeight(x: Int, z: Int): Int {
    return heightMap[z shl 4 or x]
  }
  
  /**
   * Checks if the specified position has a clear view of the sky.
   *
   * @param x The X coordinate.
   * @param y The Y coordinate.
   * @param z The Z coordinate.
   * @return `true` if the position has a clear view of the sky, `false` otherwise.
   */
  fun canSeeSky(x: Int, y: Int, z: Int): Boolean = y >= getHeight(x, z)
  
  /**
   * Checks if the specified position has a clear view of the sky.
   *
   * @param pos The position.
   * @return `true` if the position has a clear view of the sky, `false` otherwise.
   */
  fun canSeeSky(pos: Pos): Boolean = pos.y >= getHeight(pos)
  
  /**
   * Retrieves the light level at the specified position.
   *
   * @param pos The position.
   * @param light The type of light to retrieve. Default value is [Light.BLOCK].
   * @return The light level at the specified position.
   */
  fun getLightFor(pos: Pos, light: Light = Light.BLOCK): Int {
    val x = pos.flooredX and 15
    val y = pos.flooredY
    val z = pos.flooredZ and 15
    val storage = palletes.getOrNull(y shr 4)
    
    return when {
      storage == null -> if (canSeeSky(pos)) light.defaultLight else 0
      light == Light.SKY -> if (level.hasSky) storage.getBlocklight(x, y and 15, z) else 0
      light == Light.BLOCK -> storage.getBlocklight(x, y and 15, z)
      else -> light.defaultLight
    }
  }
  
  /**
   * Sets the light level at the specified position.
   *
   * @param pos The position.
   * @param value The light value to set.
   * @param light The type of light to set. Default value is [Light.BLOCK].
   */
  fun setLightFor(pos: Pos, value: Int, light: Light = Light.BLOCK) {
    val x = pos.flooredX and 15
    val y = pos.flooredY
    val z = pos.flooredZ and 15
    var storage = palletes.getOrNull(y shr 4)
    
    if (storage == null) {
      storage = Pallete(y shr 4 shl 4, level.hasSky)
      palletes[y shr 4] = storage
      generateSkylightMap()
    }
    
    isModified = true
    
    when (light) {
      Light.SKY -> if (level.hasSky) storage.setSkylight(x, y and 15, z, value)
      Light.BLOCK -> storage.setBlocklight(x, y and 15, z, value)
    }
  }
  
  /**
   * Retrieves the subtracted light level at the specified position.
   *
   * @param pos The position.
   * @param amount The amount of light to subtract.
   * @return The subtracted light level at the specified position.
   */
  fun getLightSubtracted(pos: Pos, amount: Int): Int {
    val x = pos.flooredX and 15
    val y = pos.flooredY
    val z = pos.flooredZ and 15
    
    return palletes.getOrNull(y shr 4)?.run {
      val skyLight = if (level.hasSky) getSkylight(x, y and 15, z) else 0
      maxOf(getBlocklight(x, y and 15, z), skyLight - amount)
    } ?: if (level.hasSky && amount < Light.SKY.defaultLight) Light.SKY.defaultLight - amount else 0
  }
  
  /**
   * Retrieves the highest non-empty segment of the chunk.
   *
   * @return The highest non-empty segment of the chunk.
   */
  fun getTopFilledSegment(): Int {
    countdown(palletes.lastIndex) {
      val storage = palletes.getOrNull(it)
      if (storage != null) return storage.yBase
    }
    
    return 0
  }
  
  /**
   * Generates a sequence of [IState] objects for each block in the chunk.
   * @param containsAir Determines whether to include air blocks in the sequence. Default is `false`.
   * @return A sequence of [IState] objects.
   */
  fun generateStatesSequence(containsAir: Boolean = false): Sequence<IState> {
    return sequence {
      for (x in 0 until 16) {
        for (y in 0 until 256) {
          for (z in 0 until 16) {
            val state = getState(x, y, z)
            if (!containsAir && state.isAir) continue
            
            yield(state)
          }
        }
      }
    }
  }
  
  /**
   * Generates a sequence of pairs, where each pair consists of a [LevelPos] object representing the position of a block
   * in the chunk and the corresponding [IState] object.
   * @param containsAir Determines whether to include air blocks in the sequence. Default is `false`.
   * @return A sequence of pairs of [LevelPos] and [IState].
   */
  fun generateStatesPosSequence(containsAir: Boolean = false): Sequence<Pair<LevelPos, IState>> {
    return sequence {
      for (x in 0 until 16) {
        for (y in 0 until 256) {
          for (z in 0 until 16) {
            val state = getState(x, y, z)
            if (!containsAir && state.isAir) continue
            
            yield(LevelPos(level, x, y, z) to state)
          }
        }
      }
    }
  }
  
  /**
   * Generates the height map for the chunk.
   * It determines the height of the blocks in the chunk and updates the height map and other related data structures.
   * After generating the height map, the `isModified` flag is set to `true` to indicate that the chunk has been modified.
   */
  fun generateHeightMap() {
    val top = getTopFilledSegment()
    heightMapMinimum = Int.MAX_VALUE
    
    for (x in 0..15) {
      for (z in 0..15) {
        val shift = z shl 4
        precipitationHeightMap[x + shift] = -999
        
        for (y in top + 16 downTo 1) {
          val block = getBlock(x, y - 1, z)
          
          if (block.lightOpacity != 0) {
            heightMap[shift or x] = y
            
            if (y < heightMapMinimum) {
              heightMapMinimum = y
            }
            
            break
          }
        }
      }
    }
    isModified = true
  }
  
  /**
   * Generates the skylight map for the level.
   */
  fun generateSkylightMap() {
    val top = getTopFilledSegment()
    heightMapMinimum = Int.MAX_VALUE
    
    for (x in 0..15) {
      for (z in 0..15) {
        precipitationHeightMap[x + (z shl 4)] = -999
        
        var y = top + 16
        while (y > 0) {
          if (getBlockLightOpacity(x, y - 1, z) != 0) {
            heightMap[z shl 4 or x] = y
            if (y < heightMapMinimum) {
              heightMapMinimum = y
            }
            
            break
          }
          
          y--
        }
        
        if (level.hasSky) {
          var light = 15
          y = top + 16 - 1
          
          do {
            var opacity = getBlockLightOpacity(x, y, z)
            if (opacity == 0 && light != 15) {
              opacity = 1
            }
            
            light -= opacity
            if (light > 0) {
              palletes.getOrNull(y shr 4)?.setSkylight(x, y and 15, z, light)
              level.notifyLightSet(Pos((x shl 4) + x, y, (z shl 4) + z))
            }
            
            y--
          } while (y > 0 && light > 0)
        }
      }
    }
    
    isModified = true
  }
  
  /**
   * Propagates skylight occlusion for the specified column at the given coordinates.
   * @param x The X coordinate of the column.
   * @param z The Z coordinate of the column.
   */
  fun propagateSkylightOcclusion(x: Int, z: Int) {
    updateSkylightColumns[x + z * 16] = true
    isGapLightingUpdated = true
  }
  
  /**
   * Relights the block at the specified coordinates.
   * @param x The X coordinate of the block.
   * @param y The Y coordinate of the block.
   * @param z The Z coordinate of the block.
   */
  fun relightBlock(x: Int, y: Int, z: Int) {
    // Implementation details of relighting the block go here
  }
  
  /**
   * Sets the state of the block at the specified position.
   * @param pos The position of the block.
   * @param state The new state to set.
   * @return The previous state of the block, or null if the previous state was the same as the new state.
   */
  fun setState(pos: Pos, state: IState): IState? {
    // fast-check -> same state
    val oldState = getState(pos)
    if (oldState === state) {
      return null
    }
    
    val x = pos.flooredX and 15
    val y = pos.flooredY
    val z = pos.flooredZ and 15
    val index = z shl 4 or x
    
    if (y >= precipitationHeightMap[index] - 1) {
      precipitationHeightMap[index] = -999
    }
    
    val height = heightMap[index]
    var storage = palletes.getOrNull(y shr 4)
    var yHigherThanHeight = false
    
    // create a new block storage if empty
    if (storage == null) {
      if (state.isAir) return null
      
      storage = Pallete(y shr 4 shl 4, level.hasSky)
      palletes[y shr 4] = storage
      yHigherThanHeight = y >= height
    }
    
    storage[x, y and 15, z] = state
    
    // apply listener to be triggered when old block is removed
    if (!state.its(oldState)) {
      oldState.onRemoved(level, pos, this)
      if (oldState.isTileProvider) level.removeTile(pos)
    }
    
    // block state at the storage is not equals to new block (some internal error)
    if (!state.its(storage.getBlock(x, y and 15, z))) return null
    
    // corrects new light for the new block state.
    if (yHigherThanHeight) {
      generateSkylightMap()
    } else {
      val newOp = state.lightOpacity
      val oldOp = oldState.lightOpacity
      
      if (newOp > 0 && y >= height) {
        relightBlock(x, y + 1, z)
      } else if (newOp == 0 && y == height - 1) {
        relightBlock(x, y, z)
      }
      
      if (newOp != oldOp && (newOp < oldOp || getLightFor(pos, Light.SKY) > 0 || getLightFor(pos, Light.BLOCK) > 0)) {
        propagateSkylightOcclusion(x, z)
      }
    }
    
    // if old block is a tile entity; reset their info
    if (oldState.isTileProvider) {
      getTile(pos)?.resetTileInfo()
    }
    
    // old block is not equals to new block; call add listener to block
    if (!state.its(oldState)) {
      state.onAdded(level, pos, this)
    }
    
    // new block is a tile entity; set the block creating a new tile to them
    if (state.isTileProvider) {
      val tile = getTile(pos) ?: state.tileProvider?.provide(level, state.getMetadata())
      level.setTile(pos, tile)
      tile?.resetTileInfo()
    }
    
    isModified = true
    return oldState
  }
  
  /**
   * Retrieves the state at the specified coordinates, or returns null if the state is not found.
   * @param x The X coordinate of the state.
   * @param y The Y coordinate of the state.
   * @param z The Z coordinate of the state.
   * @return The state at the specified coordinates, or null if not found.
   */
  fun getStateOrNull(x: Int, y: Int, z: Int): IState? {
    if (y >= 0 && y shl 4 < palletes.size) {
      val storage = palletes.getOrNull(y shl 4) ?: return null
      return storage.getStateOrNull(x and 15, y and 15, z and 15)
    }
    
    return null
  }
  
  /**
   * Retrieves the state at the specified coordinates, or returns a default state if the state is not found.
   * @param x The X coordinate of the state.
   * @param y The Y coordinate of the state.
   * @param z The Z coordinate of the state.
   * @param default The default state to return if the state is not found.
   * @return The state at the specified coordinates, or the default state if not found.
   */
  fun getState(x: Int, y: Int, z: Int, default: IState = Blocks.AIR.state): IState {
    if (y >= 0 && y shl 4 < palletes.size) {
      val storage = palletes.getOrNull(y shl 4) ?: return default
      return storage.getState(x and 15, y and 15, z and 15, default)
    }
    
    return default
  }
  
  /**
   * Retrieves the state of the block at the specified position.
   *
   * @param pos The position of the block.
   * @param default The default state to return if no block state is found. Default value is [Blocks.AIR.state].
   * @return The state of the block at the specified position, or the default state if no block state is found.
   */
  fun getState(pos: Pos, default: IState = Blocks.AIR.state): IState {
    return getState(pos.flooredX, pos.flooredY, pos.flooredZ, default)
  }
  
  /**
   * Retrieves the block at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @return The block at the specified coordinates, or null if no block exists.
   */
  fun getBlockOrNull(x: Int, y: Int, z: Int): Block? {
    if (y >= 0 && (y shl 4) < palletes.size) {
      val storage = palletes.getOrNull(y shl 4) ?: return null
      return storage.getBlockOrNull(x and 15, y and 15, z and 15)
    }
    
    return null
  }
  
  /**
   * Retrieves the block at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @param default The default block to return if no block is found. Default value is [Blocks.AIR].
   * @return The block at the specified coordinates, or the default block if no block is found.
   */
  fun getBlock(x: Int, y: Int, z: Int, default: Block = Blocks.AIR): Block {
    if (y >= 0 && (y shl 4) < palletes.size) {
      val storage = palletes.getOrNull(y shl 4) ?: return default
      return storage.getBlock(x and 15, y and 15, z and 15, default)
    }
    
    return default
  }
  
  /**
   * Retrieves the block at the specified position.
   *
   * @param pos The position of the block.
   * @param default The default block to return if no block is found. Default value is [Blocks.AIR].
   * @return The block at the specified position, or the default block if no block is found.
   */
  fun getBlock(pos: Pos, default: Block = Blocks.AIR): Block {
    return getBlock(pos.flooredX, pos.flooredY, pos.flooredZ, default)
  }
  
  /**
   * Retrieves the metadata of the block at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @return The metadata of the block at the specified coordinates, or 0 if no block is found.
   */
  fun getBlockMetadata(x: Int, y: Int, z: Int): Int {
    if (y shr 4 >= palletes.size) return 0
    
    return palletes.getOrNull(y shr 4)?.getMetadata(x and 15, y and 15, z and 15) ?: 0
  }
  
  /**
   * Retrieves the metadata of the block at the specified position.
   *
   * @param pos The position of the block.
   * @return The metadata of the block at the specified position, or 0 if no block is found.
   */
  fun getBlockMetadata(pos: Pos): Int {
    return getBlockMetadata(pos.flooredX, pos.flooredY, pos.flooredZ)
  }
  
  /**
   * Retrieves the light opacity of the block at the specified position.
   *
   * @param pos The position of the block.
   * @return The light opacity of the block at the specified position.
   */
  fun getBlockLightOpacity(pos: Pos): Int {
    return getBlock(pos).lightOpacity
  }
  
  /**
   * Retrieves the light opacity of the block at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @return The light opacity of the block at the specified coordinates.
   */
  fun getBlockLightOpacity(x: Int, y: Int, z: Int): Int {
    return getBlock(x, y, z).lightOpacity
  }
  
  /**
   * Adds an entity to the chunk.
   *
   * @param entity The entity to be added.
   */
  fun addEntity(entity: Entity) {
    hasEntities = true
    
    val x = floorInt(entity.x / 16)
    val z = floorInt(entity.z / 16)
    if (!isAnyOnChunk(x, z)) {
      entity.wrongChunkPosCallback(this)
    }
    
    val y = floorInt(entity.y / 16)
    
    entity.addedToChunk = true
    entity.chunkX = x
    entity.chunkY = y
    entity.chunkZ = z
    entities.add(entity)
    entity.onAddedToChunk(this)
  }
  
  /**
   * Removes an entity from the chunk.
   *
   * @param entity The entity to be removed.
   */
  fun removeEntity(entity: Entity) {
    entities.remove(entity)
  }
  
  /**
   * Retrieves a sequence of entities within the specified bounding box.
   *
   * @param box The bounding box to check for entity intersection.
   * @return A sequence of entities within the specified bounding box.
   */
  fun getEntities(box: AABB): Sequence<Entity> {
    return entitiesSequence.filter { it.box.intersects(box) }
  }
  
  /**
   * Retrieves a sequence of entities within the specified bounding box that satisfy the given predicate.
   *
   * @param box The bounding box to check for entity intersection.
   * @param predicate The predicate used to filter entities.
   * @return A sequence of entities within the specified bounding box that satisfy the given predicate.
   */
  fun getEntities(box: AABB, predicate: (Entity) -> Boolean): Sequence<Entity> {
    return getEntities(box).filter(predicate)
  }
  
  /**
   * Retrieves a sequence of entities of type [T].
   *
   * @return A sequence of entities of type [T].
   */
  final inline fun <reified T : Entity> getEntities(): Sequence<T> {
    return entitiesSequence.filterIsInstance<T>()
  }
  
  /**
   * Creates a new tile entity at the specified position.
   *
   * @param pos The position of the tile entity.
   * @return The created tile entity, or null if no tile entity can be created.
   */
  fun createNewTileEntity(pos: Pos): Tile? {
    val state = getState(pos)
    return state.tileProvider?.provide(level, state.getMetadata())
  }
  
  /**
   * Retrieves the tile entity at the specified position.
   *
   * @param pos The position of the tile entity.
   * @return The tile entity at the specified position, or null if no tile entity exists or if it is invalid.
   */
  fun getTile(pos: Pos): Tile? {
    val tile = tiles[pos] ?: return null
    if (tile.isInvalid) {
      tiles -= pos
      return null
    }
    return tile
  }
  
  /**
   * Retrieves the tile entity at the specified position.
   *
   * If the tile entity already exists at the position, it is returned. Otherwise, depending on the specified
   * creation type, a new tile entity may be created and added to the level or queued for creation.
   *
   * @param pos The position of the tile entity.
   * @param creation The type of creation to be performed if the tile entity does not exist.
   * @return The tile entity at the specified position, or null if no tile entity exists or if creation failed.
   */
  fun getTile(pos: Pos, creation: CreationType): Tile? {
    var tile = getTile(pos)
    if (tile != null) return tile
    
    when (creation) {
      CreationType.IMMEDIATE -> {
        tile = createNewTileEntity(pos)
        level.setTile(pos, tile)
      }
      
      CreationType.QUEUED -> {
        tileQueue += pos
      }
    }
    
    return tile
  }
  
  /**
   * Adds a tile entity to the chunk at the specified position.
   *
   * @param pos The position of the tile entity.
   * @param tile The tile entity to be added.
   */
  fun addTile(pos: Pos, tile: Tile) {
    tile.level = level
    tile.pos = pos
    
    val state = getState(pos)
    tile.state = state
    
    if (state.isTileProvider) {
      tiles[pos]?.isInvalid = true
      tile.isInvalid = false
      tiles[pos] = tile
    }
    
    tile.onAddedToChunk(this)
  }
  
  /**
   * Adds a tile entity to the chunk.
   *
   * @param tile The tile entity to be added.
   */
  fun addTile(tile: Tile) {
    addTile(tile.pos, tile)
    if (isLoaded) {
      level.addTile(tile)
    }
  }
  
  /**
   * Removes the tile entity at the specified position from the chunk.
   *
   * @param pos The position of the tile entity.
   */
  fun removeTile(pos: Pos) {
    if (isLoaded) {
      tiles.remove(pos)?.let {
        it.onRemovedFromChunk(this)
        it.isInvalid = true
      }
    }
  }
  
  /**
   * Fills the block pallete at the specified index with the given state.
   *
   * @param index The index of the block pallete.
   * @param state The state to fill the pallete with.
   */
  fun fillAt(index: Int, state: IState) {
    val pallete = palletes.getOrNull(index) ?: return
    pallete.fill(state)
  }
  
  /**
   * Clears the block pallete at the specified index.
   *
   * @param index The index of the block pallete.
   */
  fun clearAt(index: Int) {
    val pallete = palletes.getOrNull(index) ?: return
    pallete.clear()
  }
  
  /**
   * Fills all block palettes in the chunk with the given state.
   *
   * @param state The state to fill the palettes with.
   */
  fun fill(state: IState) {
    repeat(16) {
      val pallete = palletes.getOrPut(it) { Pallete(it, level.hasSky) }
      pallete.fill(state)
    }
  }
  
  /**
   * Clears all block palettes in the chunk.
   */
  fun clear() {
    repeat(16) {
      val pallete = palletes.getOrNull(it) ?: return@repeat
      pallete.clear()
    }
  }
  
  /**
   * Called when the chunk is loaded.
   */
  fun onChunkLoad() {
    isLoaded = true
    // Implementation specific logic goes here
  }
  
  /**
   * Called when the chunk is unloaded.
   */
  fun onChunkUnload() {
    isLoaded = false
    
    // Implementation specific logic goes here
  }
  
  /**
   * Marks the chunk as modified.
   */
  fun markModified() {
    isModified = true
  }
  
  override fun compareTo(other: Chunk): Int {
    return (x shl z).compareTo(other.x shl other.z)
  }
  
  override fun save(tag: CompoundTag) {
    tag["V"] = 1.toByte()
    tag["xPos"] = x
    tag["zPos"] = z
    tag["LastUpdate"] = level.time
    tag["HeightMap"] = heightMap
    tag["TerrainPopulated"] = isTerrainPopulated
    tag["LightPopulated"] = isLightPopulated
    tag["InhabitedTime"] = inhabitedTime

    val sections = ListTag<CompoundTag>()
    for (pallete in palletes) {
      sections += pallete.store()
    }

    tag["Sections"] = sections
    tag["Biomes"] = biomes

    hasEntities = false
    val entitiesTag = ListTag<CompoundTag>()
    for (entity in entities) {
      entitiesTag += entity.store()
      hasEntities = true
    }

    tag["Entities"] = entitiesTag

    val tilesTag = ListTag<CompoundTag>()
    for (tile in tiles.values) {
      tilesTag += tile.store()
    }

    tag["TileEntities"] = tilesTag
  }
  
  override fun load(tag: CompoundTag) {
    x = tag.int("xPos")
    z = tag.int("zPos")
    heightMap = tag.intArray("HeightMap")
    isTerrainPopulated = tag.boolean("TerrainPopulated")
    isLightPopulated = tag.boolean("LightPopulated")
    inhabitedTime = tag.long("InhabitedTime")

    val sections = tag.compoundList("Sections")
    for ((index, section) in sections.withIndex()) {
      val pallete = Pallete(0, true)
      pallete.load(section)
      palletes[index] = pallete
    }

    biomes = tag.byteArray("Biomes")

    /*val entitiesTag = tag.compoundList("Entities")
    for (entityTag in entitiesTag) {

    }
    */
  }


  /*

  *//**
    * Reads the data stored in the passed NBTTagCompound and creates a Chunk with that data in the passed World.
    * Returns the created Chunk.
    *//*
   private fun readChunkFromNBT(worldIn: Level, tag: CompoundTag): Chunk {

     if (tag.hasKey("Biomes", 7)) {
       chunk.setBiomeArray(tag.getByteArray("Biomes"))
     }

     val nbttaglist1: NBTTagList = tag.getTagList("Entities", 10)

     if (nbttaglist1 != null) {
       for (k2 in 0..<nbttaglist1.tagCount()) {
         val nbttagcompound1: NBTTagCompound = nbttaglist1.getCompoundTagAt(k2)
         val entity: Entity = EntityList.createEntityFromNBT(nbttagcompound1, worldIn)
         chunk.setHasEntities(true)

         if (entity != null) {
           chunk.addEntity(entity)
           var entity1: Entity? = entity

           var nbttagcompound4: NBTTagCompound = nbttagcompound1
           while (nbttagcompound4.hasKey("Riding", 10)) {
             val entity2: Entity = EntityList.createEntityFromNBT(nbttagcompound4.getCompoundTag("Riding"), worldIn)

             if (entity2 != null) {
               chunk.addEntity(entity2)
               entity1.mountEntity(entity2)
             }

             entity1 = entity2
             nbttagcompound4 = nbttagcompound4.getCompoundTag("Riding")
           }
         }
       }
     }

     val nbttaglist2: NBTTagList = tag.getTagList("TileEntities", 10)

     if (nbttaglist2 != null) {
       for (l2 in 0..<nbttaglist2.tagCount()) {
         val nbttagcompound2: NBTTagCompound = nbttaglist2.getCompoundTagAt(l2)
         val tileentity: TileEntity = TileEntity.createAndLoadEntity(nbttagcompound2)

         if (tileentity != null) {
           chunk.addTileEntity(tileentity)
         }
       }
     }

     return chunk
   }*/
}
