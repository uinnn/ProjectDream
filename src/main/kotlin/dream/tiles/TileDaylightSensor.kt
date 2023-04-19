package dream.tiles

import dream.interfaces.*

/**
 * A daylight sensor tile.
 */
class TileDaylightSensor : Tile(Tiles.DAYLIGHT_SENSOR), Tickable {
   override fun tick() {
      if (hasLevel && !level.isClient) {
         val block = block
      }
   }
}
