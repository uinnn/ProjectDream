package dream.level

import dream.api.Scope
import dream.block.Blocks
import dream.coroutines.LevelDispatcher
import dream.level.chunk.Chunk
import dream.level.chunk.storage.Pallete
import dream.level.provider.ChunkProvider
import dream.level.provider.LevelProvider
import dream.level.storage.ISaveHandler
import dream.misc.IProgressUpdate
import dream.utils.getOrPut
import kotlin.coroutines.CoroutineContext

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
        val pallete = chunk.palletes.getOrPut(0) { Pallete(0, true) }
        pallete.fill(Blocks.STONE.state)
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
        return true
      }

      override fun makeString(): String {
        return "DebugLevelSource"
      }

      override fun getLoadedChunkCount(): Int {
        return 0
      }

      override fun recreateStructures(chunk: Chunk, x: Int, z: Int) {
      }

    }
  }
}
