package dream

import java.io.*

/**
 * Represents the translation map for players.
 *
 * Translation map can be used for unique translated words for each player.
 */
data class Translation(val hint: MutableMap<String, String> = HashMap()) {

  /**
   * Adds a translation.
   */
  fun add(key: String, translation: String) {
    hint[key] = translation
  }

  /**
   * Replaces a translation.
   */
  fun replace(key: String, translation: String) {
    hint[key] = translation
  }

  /**
   * Removes a translation
   */
  fun remove(key: String) {
    hint.remove(key)
  }

  /**
   * Translate the given [key] if present, otherwise return the key itself.
   */
  fun translate(key: String): String {
    return hint[key] ?: key
  }

  companion object {
    @JvmField
    val NUMERIC_REGEX = "%(\\d+\\$)?[\\d.]*[df]".toRegex()

    @JvmField
    val default = Translation()

    /**
     * Creates a new translation based on the given [lines].
     *
     * [lines] is presumed to be style-like a ``.lang`` file.
     * Example:
     * ``
     * key1=value1
     * key2=value2
     * ``
     */
    fun of(lines: Sequence<String>, destination: Translation = Translation()): Translation {
      lines.forEach { line ->
        if (line.isEmpty() || line.first() == '#') return@forEach
        val split = line.split('=', limit = 2)
        if (split.size != 2) return@forEach
        val key = split[0]
        val value = NUMERIC_REGEX.replace(split[1], "%$1s")
        destination.add(key, value)
      }
      return destination
    }

    /**
     * Creates a new translation based on the given [stream].
     *
     * [stream] is presumed to be a ``.lang`` file.
     */
    fun of(stream: InputStream, destination: Translation = Translation()): Translation {
      return stream.bufferedReader().use {
        of(it.lineSequence(), destination)
      }
    }

    /**
     * Creates a new translation based on the given [file].
     *
     * [file] is presumed to be a ``.lang`` file.
     */
    fun of(file: File, destination: Translation = Translation()): Translation {
      return of(file.inputStream(), destination)
    }
  }

}
