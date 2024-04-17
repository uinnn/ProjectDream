package dream.level

import dream.api.*
import dream.block.*
import dream.coroutines.*
import dream.level.chunk.*
import dream.level.chunk.storage.*
import dream.level.provider.*
import dream.level.storage.*
import dream.misc.*
import kotlin.coroutines.*

/**
 * Represents a Level.
 */
class DefaultLevel(
  name: String,
  provider: LevelProvider,
  saveHandler: ISaveHandler,
) : Level(name, provider, saveHandler), Scope {
  override var coroutineContext: CoroutineContext = LevelDispatcher

  init {
    provider.registerWorld(this)
  }

  override fun createChunkProvider(): ChunkProvider {
    return object : ChunkProvider {
      override fun provide(x: Int, z: Int): Chunk {
        val chunk = Chunk(this@DefaultLevel, x, z)
        val pallete = Pallete(0, true)
        pallete.fill(Blocks.STONE.state)
        chunk.palletes += pallete
        return chunk
      }

      override fun exists(x: Int, z: Int): Boolean {
        return true
      }

      override fun populate(provider: ChunkProvider, x: Int, z: Int) {
      }

      override fun populateChunk(provider: ChunkProvider, chunk: Chunk, x: Int, z: Int): Boolean {
        return false
      }

      override fun saveChunks(saveAllChunks: Boolean, progress: IProgressUpdate): Boolean {
        return true
      }

      override fun unloadQueuedChunks(): Boolean {
        return false
      }

      override fun canSave(): Boolean {
        return false
      }

      override fun makeString(): String {
        return "DebugLevelSource"
      }

      override fun getLoadedChunkCount(): Int {
        return 1
      }

      override fun recreateStructures(chunk: Chunk, x: Int, z: Int) {
      }

    }
  }
}
