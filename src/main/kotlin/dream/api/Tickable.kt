package dream.api

/**
 * Represents an object that can tick.
 */
fun interface Tickable {

  /**
   * Do a tick update on this object.
   *
   * @param partial - some implementations can offer an [partial] tick to allow delaying the ticker.
   * if not supports partial tick, [partial] will be ALWAYS -1
   */
  fun tick(partial: Int)
}
