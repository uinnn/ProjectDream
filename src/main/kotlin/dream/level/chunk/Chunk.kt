package dream.level.chunk

import com.google.common.collect.Queues
import dream.block.*
import dream.block.state.IState
import dream.collections.ClassInheritanceMultiMap
import dream.entity.Entity
import dream.level.*
import dream.level.chunk.storage.BlockStorage
import dream.misc.Open
import dream.nbt.CompoundStorable
import dream.nbt.types.CompoundTag
import dream.pos.*
import dream.tiles.*
import dream.utils.*
import java.util.concurrent.ConcurrentLinkedQueue


/**
 * Represents a chunk in a level.
 */
@Open
class Chunk(val level: Level, val x: Int, val z: Int) : CompoundStorable, Comparable<Chunk> {
   
   /**
    * Used to store block IDs, block MSBs, Sky-light maps, Block-light maps, and metadata.
    * Each entry corresponds to a logical segment of 16x16x16 blocks, stacked vertically.
    */
   val blocks = ArrayList<BlockStorage>(16)
   
   /**
    * Contains a 16x16 mapping on the X/Z plane of the biome ID to which each colum belongs.
    */
   val biomes = ByteArray(256) { -1 }
   
   /**
    * Height map of this chunk.
    */
   val heightMap = IntArray(256)
   
   /**
    * A map, similar to heightMap, that tracks how far down precipitation can fall.
    */
   val precipitationHeightMap = IntArray(256) { -999 }
   
   /**
    * Which columns need their skylightMaps updated.
    */
   val updateSkylightColumns = BooleanArray(256)
   
   
   /**
    * All tiles entites of this chunk.
    */
   val tiles = HashMap<Pos, Tile>()
   
   /**
    * All entities on this chunk.
    */
   val entities = MutableList(16) { ClassInheritanceMultiMap(Entity::class) }
   
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
    * Boolean value indicating if the terrain is populated.
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
   val tileQueue: ConcurrentLinkedQueue<Pos> = Queues.newConcurrentLinkedQueue()
   
   /**
    * Checks if this chunk is empty.
    */
   val isEmpty get() = false
   
   /**
    * Checks if the level of this chunk is client or not.
    */
   val isClient get() = level.isClient

   constructor(level: Level, primer: ChunkPrimer, chunkX: Int, chunkZ: Int) : this(level, chunkX, chunkZ) {
      val hasSky = level.provider.hasSky
      
      for (x in 0 until 16) {
         for (y in 0 until 16) {
            for (z in 0 until 256) {
               val index = x * 256 * 16 or y * 256 or z
               val state = primer.getState(index)
               
               if (!state.block.isAir) {
                  val i = z shr 4
                  val storage = blocks.getOrPut(i) { BlockStorage(i shl 4, hasSky) }
                  storage[x, z and 15, i] = state
               }
            }
         }
      }
   }
   
   /**
    * Checks whether the chunk is at the X/Z location specified.
    */
   fun isOnChunk(chunkX: Int, chunkZ: Int): Boolean {
      return chunkX == x && chunkZ == z
   }
   
   /**
    * Checks wheter the chunk is at the X or Z location specified.
    */
   fun isAnyOnChunk(chunkX: Int, chunkZ: Int): Boolean {
      return chunkX == x || chunkZ == z
   }
   
   /**
    * Returns the value in the height map at the given pos.
    */
   fun getHeight(pos: Pos): Int {
      return getHeight(pos.flooredX and 15, pos.flooredZ and 15)
   }
   
   /**
    * Returns the value in the height map at the given coordinates.
    */
   fun getHeight(x: Int, z: Int): Int {
      return heightMap[z shl 4 or x]
   }
   
   fun canSeeSky(x: Int, y: Int, z: Int) = y >= getHeight(x, z)
   
   /**
    * Checks if the given [pos] is able to see sky.
    */
   fun canSeeSky(pos: Pos): Boolean {
      return pos.y >= getHeight(pos)
   }
   
   /**
    * Get light for the given [pos] by [light].
    */
   fun getLightFor(pos: Pos, light: Light = Light.BLOCK): Int {
      val x = pos.flooredX and 15
      val y = pos.flooredY
      val z = pos.flooredZ and 15
      val storage = blocks.getOrNull(y shr 4)
      
      return when {
         storage == null -> if (canSeeSky(pos)) light.defaultLight else 0
         light == Light.SKY -> if (level.hasSky) storage.getBlocklight(x, y and 15, z) else 0
         light == Light.BLOCK -> storage.getBlocklight(x, y and 15, z)
         else -> light.defaultLight
      }
   }
   
   /**
    * Sets light for the given [pos] by [light].
    */
   fun setLightFor(pos: Pos, value: Int, light: Light = Light.BLOCK) {
      val x = pos.flooredX and 15
      val y = pos.flooredY
      val z = pos.flooredZ and 15
      var storage = blocks.getOrNull(y shr 4)
      
      if (storage == null) {
         storage = BlockStorage(y shr 4 shl 4, level.hasSky)
         blocks[y shr 4] = storage
         generateSkylightMap()
      }
      
      isModified = true
      
      when (light) {
         Light.SKY -> if (level.hasSky) storage.setSkylight(x, y and 15, z, value)
         Light.BLOCK -> storage.setBlocklight(x, y and 15, z, value)
      }
   }
   
   /**
    * Gets light at the given [pos] subtracted with [amount].
    */
   fun getLightSubtracted(pos: Pos, amount: Int): Int {
      val x = pos.flooredX and 15
      val y = pos.flooredY
      val z = pos.flooredZ and 15
      
      return blocks.getOrNull(y shr 4)?.run {
         val skyLight = if (level.hasSky) getSkylight(x, y and 15, z) else 0
         maxOf(getBlocklight(x, y and 15, z), skyLight - amount)
      } ?: if (level.hasSky && amount < Light.SKY.defaultLight) Light.SKY.defaultLight - amount else 0
   }
   
   /**
    * Returns the topmost block storage instance for this Chunk that actually contains a block.
    */
   fun getTopFilledSegment(): Int {
      countdown(blocks.lastIndex) {
         val storage = blocks.getOrNull(it)
         if (storage != null) return storage.yBase
      }
      
      return 0
   }
   
   /**
    * Generates a sequence that will contains **ALL** block states on this chunk.
    *
    * This is a very bulk operation and is not recommended to use this function frequently.
    *
    * @param containsAir if the sequence will apply empty blocks (AIR) in the sequence.
    *                    if true the sequence will always have a default size of 65.536 (16x256x16 area)
    */
   fun generateStatesSequence(containsAir: Boolean = false): Sequence<IState> {
      return sequence {
         for (x in 0 until 16) {
            for (y in 0 until 256) {
               for (z in 0 until 16) {
                  val state = getState(x, y, z)
                  if (state.block.isAir && !containsAir)
                     continue
                  
                  yield(state)
               }
            }
         }
      }
   }
   
   /**
    * Generates a sequence that will contains **ALL** block states on this chunk.
    *
    * This is a very bulk operation and is not recommended to use this function frequently.
    *
    * @param containsAir if the sequence will apply empty blocks (AIR) in the sequence.
    *                    if true the sequence will always have a default size of 65.536 (16x256x16 area)
    */
   fun generateStatesPosSequence(containsAir: Boolean = false): Sequence<Pair<LevelPos, IState>> {
      return sequence {
         for (x in 0 until 16) {
            for (y in 0 until 256) {
               for (z in 0 until 16) {
                  val state = getState(x, y, z)
                  if (state.block.isAir && !containsAir)
                     continue
                  
                  yield(LevelPos(level, x, y, z) to state)
               }
            }
         }
      }
   }
   
   /**
    * Generates the height map for a chunk from scratch.
    */
   fun generateHeightMap() {
      val top = getTopFilledSegment()
      heightMapMinimum = Int.MAX_VALUE
      for (x in 0..15) {
         for (z in 0..15) {
            precipitationHeightMap[x + (z shl 4)] = -999
            for (y in top + 16 downTo 1) {
               val block = getBlock(x, y - 1, z)
               if (block.lightOpacity != 0) {
                  heightMap[z shl 4 or x] = y
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
    * Generates the initial skylight map for the chunk upon generation or load.
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
                     blocks.getOrNull(y shr 4)?.setSkylight(x, y and 15, z, light)
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
    * Propagates a given sky-visible block's light value downward and upward to neighboring blocks as necessary.
    */
   fun propagateSkylightOcclusion(x: Int, z: Int) {
      updateSkylightColumns[x + z * 16] = true
      isGapLightingUpdated = true
   }
   
   /**
    * Initiates the recalculation of both the block-light and sky-light for a given block inside a chunk.
    */
   fun relightBlock(x: Int, y: Int, z: Int) {
   }
   
   fun getStateOrNull(x: Int, y: Int, z: Int): IState? {
      if (y >= 0 && y shl 4 < blocks.size) {
         val storage = blocks.getOrNull(y shl 4) ?: return null
         return storage.getStateOrNull(x and 15, y and 15, z and 15)
      }
      
      return null
   }
   
   /**
    * Gets a state at the given coordinates, or [default] if no state found.
    */
   fun getState(x: Int, y: Int, z: Int, default: IState = Blocks.AIR.state): IState {
      if (y >= 0 && y shl 4 < blocks.size) {
         val storage = blocks.getOrNull(y shl 4) ?: return default
         return storage.getState(x and 15, y and 15, z and 15, default)
      }
      
      return default
   }
   
   /**
    * Sets the block state to [state] at the given [pos].
    *
    * @return the old state or null if block not changed.
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
      val newBlock = state.block
      val oldBlock = oldState.block
      var storage = blocks.getOrNull(y shr 4)
      var yHigherThanHeight = false
      
      // create a new block storage if empty
      if (storage == null) {
         if (newBlock.isAir)
            return null
         
         storage = BlockStorage(y shr 4 shl 4, level.hasSky)
         blocks[y shr 4] = storage
         yHigherThanHeight = y >= height
      }
      
      storage[x, y and 15, z] = state
      
      // apply listener to be triggered when old block is removed
      if (oldBlock !== newBlock) {
         when {
            !isClient -> oldBlock.onRemoved(level, pos, oldState)
            oldBlock.isTileProvider -> level.removeTile(pos)
         }
      }
      
      // block state at the storage is not equals to new block (some internal error)
      if (storage.getBlock(x, y and 15, z) !== newBlock)
         return null
      
      // corrects new light for the new block state.
      if (yHigherThanHeight) {
         generateSkylightMap()
      } else {
         val newOpacity = newBlock.lightOpacity
         val oldOpacity = oldBlock.lightOpacity
         
         if (newOpacity > 0 && y >= height) {
            relightBlock(x, y + 1, z)
         } else if (newOpacity == 0 && y == height - 1) {
            relightBlock(x, y, z)
         }
         
         if (newOpacity != oldOpacity && (newOpacity < oldOpacity || getLightFor(
               pos,
               Light.SKY
            ) > 0 || getLightFor(pos) > 0)
         ) {
            propagateSkylightOcclusion(x, z)
         }
      }
      
      // if old block is a tile entity; reset their info
      if (oldBlock.isTileProvider) {
         getTile(pos)?.resetTileInfo()
      }
      
      // old block is not equals to new block; call add listener to block
      if (isClient && oldBlock !== newBlock) {
         newBlock.onAdded(pos.asLevelPos(level), state)
      }
      
      // new block is a tile entity; set the block creating a new tile to them
      if (newBlock.isTileProvider) {
         val tile = getTile(pos) ?: newBlock.tileProvider?.createNew(level, newBlock.getMetadataFromState(state))
         level.setTile(pos, tile)
         tile?.resetTileInfo()
      }
      
      isModified = true
      return oldState
   }
   
   /**
    * Gets a state at the given pos, or [default] if no state found.
    */
   fun getState(pos: Pos, default: IState = Blocks.AIR.state): IState {
      return getState(pos.flooredX, pos.flooredY, pos.flooredZ, default)
   }
   
   fun getBlockOrNull(x: Int, y: Int, z: Int): Block? {
      if (y >= 0 && y shl 4 < blocks.size) {
         val storage = blocks.getOrNull(y shl 4) ?: return null
         return storage.getBlockOrNull(x and 15, y and 15, z and 15)
      }
      
      return null
   }
   
   /**
    * Gets a block at the given coordinates, or [default] if no block found.
    */
   fun getBlock(x: Int, y: Int, z: Int, default: Block = Blocks.AIR): Block {
      if (y >= 0 && y shl 4 < blocks.size) {
         val storage = blocks.getOrNull(y shl 4) ?: return default
         return storage.getBlock(x and 15, y and 15, z and 15, default)
      }
      
      return default
   }
   
   /**
    * Gets a block at the given pos, or [default] if no block found.
    */
   fun getBlock(pos: Pos, default: Block = Blocks.AIR): Block {
      return getBlock(pos.flooredX, pos.flooredY, pos.flooredZ, default)
   }
   
   /**
    * Return the metadata corresponding to the given coordinates inside a chunk.
    */
   fun getBlockMetadata(x: Int, y: Int, z: Int): Int {
      if (y shr 4 >= blocks.size) return 0
      
      return blocks.getOrNull(y shr 4)?.getMetadata(x and 15, y and 15, z and 15) ?: 0
   }
   
   /**
    * Return the metadata corresponding to the given pos inside a chunk.
    */
   fun getBlockMetadata(pos: Pos): Int {
      return getBlockMetadata(pos.flooredX, pos.flooredY, pos.flooredZ)
   }
   
   /**
    * Gets block light opacity at given [pos].
    */
   fun getBlockLightOpacity(pos: Pos): Int {
      return getBlock(pos).lightOpacity
   }
   
   /**
    * Gets block light opacity at given coordinates.
    */
   fun getBlockLightOpacity(x: Int, y: Int, z: Int): Int {
      return getBlock(x, y, z).lightOpacity
   }
   
   /**
    * Adds the given [entity] to this chunk.
    */
   fun addEntity(entity: Entity) {
      hasEntities = true
      
      val x = floorInt(entity.x / 16)
      val z = floorInt(entity.z / 16)
      if (!isAnyOnChunk(x, z)) {
         entity.wrongChunkPosCallback(this)
      }
      
      val y = between(0, floorInt(entity.y / 16), entities.lastIndex)
      
      entity.addedToChunk = true
      entity.chunkX = x
      entity.chunkY = y
      entity.chunkZ = z
      entities[y].add(entity)
      entity.onAddedToChunk(this)
   }
   
   /**
    * Removes the given [entity] at the specified [y] coordinate.
    */
   fun removeEntity(entity: Entity, y: Int) {
      entities[between(0, y, entities.lastIndex)].remove(entity)
   }
   
   /**
    * Removes the given [entity] from this chunk.
    */
   fun removeEntity(entity: Entity) {
      removeEntity(entity, entity.chunkY)
   }
   
   /**
    * Creates a new tile entity based on the given [pos].
    */
   fun createNewTileEntity(pos: Pos): Tile? {
      return getBlock(pos).tileProvider?.createNew(level, getBlockMetadata(pos))
   }
   
   /**
    * Gets a tile entity at [pos].
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
    * Gets a tile entity at [pos].
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
    * Adds the given [tile] at [pos] on this chunk.
    */
   fun addTile(pos: Pos, tile: Tile) {
      tile.level = level
      tile.pos = pos
      tile.state = getState(pos)
      
      if (getBlock(pos) is TileProvider) {
         tiles[pos]?.isInvalid = true
         tile.isInvalid = false
         tiles[pos] = tile
      }
      
      tile.onAddedToChunk(this)
   }
   
   /**
    * Adds the given [tile] on this chunk.
    */
   fun addTile(tile: Tile) {
      addTile(tile.pos, tile)
      if (isLoaded) {
         level.addTile(tile)
      }
   }
   
   /**
    * Removes a tile entity at the given [pos].
    */
   fun removeTile(pos: Pos) {
      if (isLoaded) {
         tiles.remove(pos)?.isInvalid = true
      }
   }
   
   fun onChunkLoad() {
   
   }
   
   fun onChunkUnload() {
   
   }
   
   fun markModified() {
      isModified = true
   }
   
   override fun compareTo(other: Chunk): Int {
      return (x shl z).compareTo(other.x shl other.z)
   }
   
   override fun save(tag: CompoundTag) {
   
   }
   
   override fun load(tag: CompoundTag) {
   
   }
}
