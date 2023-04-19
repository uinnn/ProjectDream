package dream.level.provider

import dream.level.*
import dream.level.chunk.*
import dream.misc.*
import dream.pos.*

/**
 * Represents a provider for chunks.
 *
 * Used to handle chunk operations and caching.
 */
public interface ChunkProvider {
   
   /**
    * If not exists, will provide a new chunk generating all blocks, otherwise will return a existent chunk.
    */
   fun provide(x: Int, z: Int): Chunk
   
   /**
    * If not exists, will provide a new chunk generating all blocks, otherwise will return a existent chunk.
    */
   fun provide(pos: Pos) = provide(pos.flooredX shr 4, pos.flooredZ shr 4)
   
   /**
    * Determinates if exists a chunk in the given coordinates.
    */
   fun exists(x: Int, z: Int): Boolean
   
   /**
    * Determinates if exists a chunk in the given coordinates.
    */
   fun exists(pos: Pos) = exists(pos.flooredX, pos.flooredZ)
   
   /**
    * Populates chunk with ores and etc.
    */
   fun populate(provider: ChunkProvider, x: Int, z: Int)
   
   fun populateChunk(provider: ChunkProvider, chunk: Chunk, x: Int, z: Int): Boolean
   
   /**
    * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
    * Return true if all chunks have been saved.
    */
   fun saveChunks(saveAllChunks: Boolean, progress: IProgressUpdate): Boolean
   
   /**
    * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
    */
   fun unloadQueuedChunks(): Boolean
   
   /**
    * Returns if the IChunkProvider supports saving.
    */
   fun canSave(): Boolean
   
   /**
    * Converts the instance data to a readable string.
    */
   fun makeString(): String
   
   //fun getPossibleCreatures(creatureType: EnumCreatureType?, pos: BlockPos?): List<BiomeGenBase.SpawnListEntry?>?
   
   fun getStrongholdGen(level: Level, structure: String, pos: Pos): Pos
   
   fun getLoadedChunkCount(): Int
   
   fun recreateStructures(chunk: Chunk, x: Int, z: Int)
}
