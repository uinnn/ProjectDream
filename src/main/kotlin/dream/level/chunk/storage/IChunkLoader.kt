package dream.level.chunk.storage

import dream.api.Tickable
import dream.level.Level
import dream.level.chunk.Chunk

/**
 * Represents a loader for chunks.
 */
interface IChunkLoader : Tickable {

  /**
   * Loads the specified (XZ) chunk into the specified level.
   */
  fun loadChunk(level: Level, x: Int, z: Int): Chunk

  /**
   * Saves the specific chunk at level.
   */
  fun saveChunk(level: Level, chunk: Chunk)

  /**
   * Save extra data associated with this Chunk not normally saved during autosave,
   * only during chunk unload.
   *
   * Currently unused.
   */
  fun saveExtraChunkData(level: Level, chunk: Chunk)

  /**
   * Save extra data not associated with any Chunk. Not saved during autosave, only during world unload. Currently
   * unused.
   */
  fun saveExtraData()
}
