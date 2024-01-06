package dream.tiles

import dream.api.*

/**
 * A daylight sensor tile.
 */
class TileDaylightSensor : Tile(Tiles.DAYLIGHT_SENSOR), Tickable {
  override fun tick(partial: Int) {
    if (hasLevel) {
      val block = block
    }
  }
}
