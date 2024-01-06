package dream.utils

import com.github.benmanes.caffeine.cache.Cache

/**
 * Returns if the given [key] is present in this cache.
 */
operator fun <K, V> Cache<K, V>.contains(key: K) = getIfPresent(key) != null
