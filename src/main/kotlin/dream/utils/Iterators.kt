package dream.utils

import com.google.common.collect.Iterators

operator fun <T> Iterator<T>.plus(other: Iterator<T>): MutableIterator<T> = Iterators.concat(this, other)
fun <T> Iterator<T>.add(other: Iterator<T>): MutableIterator<T> = Iterators.concat(this, other)

fun <T, K> Iterator<T>.map(callback: (T) -> K): MutableIterator<K> = Iterators.transform(this, callback)
fun <T> Iterator<T>.filter(filter: (T) -> Boolean): MutableIterator<T> = Iterators.filter(this, filter)

fun <T> Iterator<T>.asIterable() = Iterable { this }

fun <T> Iterator<T>.toList() = asIterable().toList()
fun <T> Iterator<T>.toMutableList() = asIterable().toMutableList()

fun <T> Iterator<T>.toSet() = asIterable().toSet()
fun <T> Iterator<T>.toHashSet() = asIterable().toHashSet()
fun <T> Iterator<T>.toMutableSet() = asIterable().toMutableSet()

