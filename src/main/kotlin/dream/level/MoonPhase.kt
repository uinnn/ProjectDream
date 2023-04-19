package dream.level

/**
 * All moon phases.
 */
enum class MoonPhase(val factor: Float) {
   FULL(1f),
   WANING_GIBBOUS(0.75f),
   THIRD_QUARTER(0.5f),
   WANING_CRESCENT(0.25f),
   NEW(0f),
   WAXING_CRESCENT(0.25f),
   FIRST_QUARTER(0.5f),
   WAXING_GIBBOUS(0.25f);
   
   companion object {
      val values = values()
      
      fun byIndex(index: Int) = values[index % 8]
   }
}
