package dream.level.storage

import dream.level.chunk.storage.*
import dream.level.provider.*
import dream.nbt.types.*
import java.io.*

interface ISaveHandler {
   
   /**
    * Gets the player storage for this save handler.
    */
   val playerStorage: PlayerStorage
   
   /**
    * Gets the File object corresponding to the base directory of this world.
    */
   val directory: File
   
   /**
    * Returns the name of the directory where world information is saved.
    */
   val directoryName: String get() = directory.name
   
   /**
    * Loads and returns the world info.
    */
   fun loadLevelInfo(): LevelInfo
   
   /**
    * Checks the session lock to prevent save collisions.
    */
   fun checkSessionLock()
   
   /**
    * initializes and returns the chunk loader for the specified world provider.
    */
   fun getChunkLoader(provider: LevelProvider): IChunkLoader
   
   /**
    * Saves the given World Info with the given NBTTagCompound as the Player.
    */
   fun saveLevelInfoWithPlayer(worldInformation: LevelInfo, tag: CompoundTag)
   
   /**
    * used to update level.dat from old format to MCRegion format.
    */
   fun saveWorldInfo(worldInformation: LevelInfo)
   
   /**
    * Called to flush all changes to disk, waiting for them to complete.
    */
   fun flush()
   
   /**
    * Gets the file location of the given map.
    */
   fun getMapFileFromName(mapName: String): File
   
}
