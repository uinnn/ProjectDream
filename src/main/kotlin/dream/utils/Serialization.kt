package dream.utils

import kotlinx.serialization.*
import kotlinx.serialization.json.*

/**
 * Encodes [value] to Json.
 */
inline fun <reified T> toJson(value: T, json: Json = Json): String {
  return json.encodeToString(value)
}

/**
 * Encodes [value] to Json.
 */
fun <T> toJson(serializer: SerializationStrategy<T>, value: T, json: Json = Json): String {
  return json.encodeToString(serializer, value)
}

/**
 * Parses this string to the given [T] value.
 */
inline fun <reified T> CharSequence.parseJson(json: Json = Json) = json.decodeFromString<T>(toString())

/**
 * Parses this string to the given [T] value.
 */
fun <T : Any> CharSequence.parseJson(deserializer: DeserializationStrategy<T>, json: Json = Json): T {
  return json.decodeFromString(deserializer, toString())
}

/*
/**
 * Encodes [value] to Yaml.
 */
inline fun <reified T> toYaml(value: T, yaml: Yaml = Yaml.default): String {
  return yaml.encodeToString(value)
}

/**
 * Encodes [value] to Yaml.
 */
fun <T> toYaml(serializer: SerializationStrategy<T>, value: T, yaml: Yaml = Yaml.default): String {
  return yaml.encodeToString(serializer, value)
}

/**
 * Parses this string to the given [T] value.
 */
inline fun <reified T> CharSequence.parseYaml(yaml: Yaml = Yaml.default) = yaml.decodeFromString<T>(toString())

/**
 * Parses this string to the given [T] value.
 */
fun <T : Any> CharSequence.parseYaml(deserializer: DeserializationStrategy<T>, yaml: Yaml = Yaml.default): T {
  return yaml.decodeFromString(deserializer, toString())
}

 */


/**
 * Extension property to convert a JsonPrimitive to a Byte.
 */
val JsonPrimitive.byte: Byte
    get() = content.toByte()

/**
 * Extension property to convert a JsonPrimitive to a nullable Byte.
 */
val JsonPrimitive.byteOrNull: Byte?
    get() = content.toByteOrNull()

/**
 * Extension property to convert a JsonPrimitive to a Short.
 */
val JsonPrimitive.short: Short
    get() = content.toShort()

/**
 * Extension property to convert a JsonPrimitive to a nullable Short.
 */
val JsonPrimitive.shortOrNull: Short?
    get() = content.toShortOrNull()

/**
 * Extension function to safely cast a JsonElement to a JsonPrimitive with a default value.
 * @param default The default JsonPrimitive.
 * @return The JsonPrimitive if the cast is successful, otherwise the default value.
 */
fun JsonElement.asPrimitive(default: JsonPrimitive): JsonPrimitive = this as? JsonPrimitive ?: default

/**
 * Extension function to safely cast a JsonElement to a JsonPrimitive or null.
 * @return The JsonPrimitive if the cast is successful, otherwise null.
 */
fun JsonElement.asPrimitiveOrNull(): JsonPrimitive? = this as? JsonPrimitive

/**
 * Extension function to check if a JsonObject contains a specific key.
 * @param key The key to check.
 * @return True if the key is present, false otherwise.
 */
fun JsonObject.has(key: String): Boolean = containsKey(key)

/**
 * Extension function to retrieve a JsonPrimitive value from a JsonObject with a default value.
 * @param key The key of the JsonPrimitive.
 * @param default The default JsonPrimitive.
 * @return The JsonPrimitive value if it exists, otherwise the default value.
 */
fun JsonObject.primitive(key: String, default: JsonPrimitive): JsonPrimitive =
    get(key)?.asPrimitiveOrNull() ?: default

/**
 * Extension function to retrieve a nullable JsonPrimitive value from a JsonObject.
 * @param key The key of the JsonPrimitive.
 * @return The JsonPrimitive value if it exists, otherwise null.
 */
fun JsonObject.primitiveOrNull(key: String): JsonPrimitive? = get(key)?.asPrimitiveOrNull()

/**
 * Extension function to retrieve a Byte value from a JsonObject with a default value.
 * @param key The key of the Byte value.
 * @param default The default Byte value.
 * @return The Byte value if it exists, otherwise the default value.
 */
fun JsonObject.byte(key: String, default: Byte): Byte = primitiveOrNull(key)?.byteOrNull ?: default

/**
 * Extension function to retrieve a nullable Byte value from a JsonObject.
 * @param key The key of the Byte value.
 * @return The Byte value if it exists, otherwise null.
 */
fun JsonObject.byteOrNull(key: String): Byte? = primitiveOrNull(key)?.byteOrNull

/**
 * Extension function to retrieve a Short value from a JsonObject with a default value.
 * @param key The key of the Short value.
 * @param default The default Short value.
 * @return The Short value if it exists, otherwise the default value.
 */
fun JsonObject.short(key: String, default: Short): Short = primitiveOrNull(key)?.shortOrNull ?: default

/**
 * Extension function to retrieve a nullable Short value from a JsonObject.
 * @param key The key of the Short value.
 * @return The Short value if it exists, otherwise null.
 */
fun JsonObject.shortOrNull(key: String): Short? = primitiveOrNull(key)?.shortOrNull

/**
 * Extension function to retrieve an Int value from a JsonObject with a default value.
 * @param key The key of the Int value.
 * @param default The default Int value.
 * @return The Int value if it exists, otherwise the default value.
 */
fun JsonObject.int(key: String, default: Int): Int = primitiveOrNull(key)?.intOrNull ?: default

/**
 * Extension function to retrieve a nullable Int value from a JsonObject.
 * @param key The key of the Int value.
 * @return The Int value if it exists, otherwise null.
 */
fun JsonObject.intOrNull(key: String): Int? = primitiveOrNull(key)?.intOrNull

/**
 * Extension function to retrieve a Long value from a JsonObject with a default value.
 * @param key The key of the Long value.
 * @param default The default Long value.
 * @return The Long value if it exists, otherwise the default value.
 */
fun JsonObject.long(key: String, default: Long): Long = primitiveOrNull(key)?.longOrNull ?: default

/**
 * Extension function to retrieve a nullable Long value from a JsonObject.
 * @param key The key of the Long value.
 * @return The Long value if it exists, otherwise null.
 */
fun JsonObject.longOrNull(key: String): Long? = primitiveOrNull(key)?.longOrNull

/**
 * Extension function to retrieve a Float value from a JsonObject with a default value.
 * @param key The key of the Float value.
 * @param default The default Float value.
 * @return The Float value if it exists, otherwise the default value.
 */
fun JsonObject.float(key: String, default: Float): Float = primitiveOrNull(key)?.floatOrNull ?: default

/**
 * Extension function to retrieve a nullable Float value from a JsonObject.
 * @param key The key of the Float value.
 * @return The Float value if it exists, otherwise null.
 */
fun JsonObject.floatOrNull(key: String): Float? = primitiveOrNull(key)?.floatOrNull

/**
 * Extension function to retrieve a Double value from a JsonObject with a default value.
 * @param key The key of the Double value.
 * @param default The default Double value.
 * @return The Double value if it exists, otherwise the default value.
 */
fun JsonObject.double(key: String, default: Double): Double = primitiveOrNull(key)?.doubleOrNull ?: default

/**
 * Extension function to retrieve a nullable Double value from a JsonObject.
 * @param key The key of the Double value.
 * @return The Double value if it exists, otherwise null.
 */
fun JsonObject.doubleOrNull(key: String): Double? = primitiveOrNull(key)?.doubleOrNull

/**
 * Extension function to retrieve a Boolean value from a JsonObject with a default value.
 * @param key The key of the Boolean value.
 * @param default The default Boolean value.
 * @return The Boolean value if it exists, otherwise the default value.
 */
fun JsonObject.boolean(key: String, default: Boolean): Boolean =
    primitiveOrNull(key)?.booleanOrNull ?: default

/**
 * Extension function to retrieve a nullable Boolean value from a JsonObject.
 * @param key The key of the Boolean value.
 * @return The Boolean value if it exists, otherwise null.
 */
fun JsonObject.booleanOrNull(key: String): Boolean? = primitiveOrNull(key)?.booleanOrNull

/**
 * Extension function to retrieve a String value from a JsonObject with a default value.
 * @param key The key of the String value.
 * @param default The default String value.
 * @return The String value if it exists, otherwise the default value.
 */
fun JsonObject.string(key: String, default: String): String = primitiveOrNull(key)?.contentOrNull ?: default

/**
 * Extension function to retrieve a nullable String value from a JsonObject.
 * @param key The key of the String value.
 * @return The String value if it exists, otherwise null.
 */
fun JsonObject.stringOrNull(key: String): String? = primitiveOrNull(key)?.contentOrNull
