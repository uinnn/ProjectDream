package dream.level.storage

import dream.entity.player.*
import dream.nbt.types.*

interface PlayerStorage {
   
   /**
    * Returns an list of usernames for which player.dat exists for.
    */
   val players: List<String>
   
   /**
    * Writes the player data to disk from the specified player.
    */
   fun write(player: Player)
   
   /**
    * Reads the player data from disk into the specified player.
    */
   fun read(player: Player): CompoundTag
   
}
