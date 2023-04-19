package dream.level

import dream.misc.*
import kotlinx.serialization.*

/**
 * Represents a rain data for a level.
 */
@Serializable
@Open
public data class Rain(
   var isRaining: Boolean = false,
   var isThundering: Boolean = false,
   var cleanTime: Int = 0,
   var rainTime: Int = 0,
   var thunderTime: Int = 0,
   var rainStrength: Float = 0f,
   var prevRainStrength: Float = 0f,
   var thunderStrength: Float = 0f,
   var prevThunderStrength: Float = 0f,
   var lastLightningBolt: Int = 0,
)
