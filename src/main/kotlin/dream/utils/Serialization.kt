package dream.utils

import com.charleskorn.kaml.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/**
 * Encodes [value] to Json.
 */
public inline fun <reified T> toJson(value: T, json: Json = Json): String {
   return json.encodeToString(value)
}

/**
 * Encodes [value] to Json.
 */
public fun <T> toJson(serializer: SerializationStrategy<T>, value: T, json: Json = Json): String {
   return json.encodeToString(serializer, value)
}

/**
 * Parses this string to the given [T] value.
 */
public inline fun <reified T> CharSequence.parseJson(json: Json = Json) = json.decodeFromString<T>(toString())

/**
 * Parses this string to the given [T] value.
 */
public fun <T : Any> CharSequence.parseJson(deserializer: DeserializationStrategy<T>, json: Json = Json): T {
   return json.decodeFromString(deserializer, toString())
}

/**
 * Encodes [value] to Yaml.
 */
public inline fun <reified T> toYaml(value: T, yaml: Yaml = Yaml.default): String {
   return yaml.encodeToString(value)
}

/**
 * Encodes [value] to Yaml.
 */
public fun <T> toYaml(serializer: SerializationStrategy<T>, value: T, yaml: Yaml = Yaml.default): String {
   return yaml.encodeToString(serializer, value)
}

/**
 * Parses this string to the given [T] value.
 */
public inline fun <reified T> CharSequence.parseYaml(yaml: Yaml = Yaml.default) = yaml.decodeFromString<T>(toString())

/**
 * Parses this string to the given [T] value.
 */
public fun <T : Any> CharSequence.parseYaml(deserializer: DeserializationStrategy<T>, yaml: Yaml = Yaml.default): T {
   return yaml.decodeFromString(deserializer, toString())
}
