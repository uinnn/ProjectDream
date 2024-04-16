package dream.level.storage

import dream.entity.player.Player
import dream.level.chunk.storage.AnvilChunkLoader
import dream.level.chunk.storage.ChunkLoader
import dream.level.provider.LevelProvider
import dream.nbt.readCompound
import dream.nbt.types.CompoundTag
import dream.nbt.writeTag
import dream.utils.create
import dream.utils.createFolder
import java.io.File

class SaveHandler(override val directory: File) : ISaveHandler, PlayerStorage {
  init {
    directory.createFolder()
  }

  val playersDirectory = createFolder(directory, "playerdata")
  val mapDataDirectory = createFolder(directory, "data")

  override val playerStorage: PlayerStorage get() = this
  override val availablePlayerDatas: List<String> get() = playersDirectory.list()!!.toList()

  override fun loadLevelInfo(): LevelInfo {
    return LevelInfo()
  }

  override fun checkSessionLock() {
  }

  override fun getChunkLoader(provider: LevelProvider): ChunkLoader {
    return AnvilChunkLoader()
  }

  override fun saveLevelInfoWithPlayer(worldInformation: LevelInfo, tag: CompoundTag) {
  }

  override fun saveWorldInfo(worldInformation: LevelInfo) {
  }

  override fun flush() {
  }

  override fun getMapFileFromName(mapName: String): File {
    return File(mapDataDirectory, "$mapName.dat")
  }

  override fun write(player: Player) {
    val file = getPlayerFile(player).create()
    val tag = player.store()
    try {
      file.writeTag(tag)
    } catch (ex: Exception) {
      println("Failed to save player data for ${player.name}")
      ex.printStackTrace()
    }
  }

  override fun read(player: Player): CompoundTag {
    val file = getPlayerFile(player)
    if (!file.exists() || !file.isFile) {
      return CompoundTag()
    }

    try {
      val tag = file.readCompound()
      player.load(tag)
      return tag
    } catch (e: Exception) {
      println("Failed to load player data for ${player.name}")
      e.printStackTrace()
      return CompoundTag()
    }
  }

  override fun getPlayerFile(player: Player): File {
    return File(playersDirectory, "${player.id}.dat")
  }
}
