package dream.utils

/**
 * Gets the current thread.
 */
inline val currentThread: Thread get() = Thread.currentThread()

/**
 * Gets the current thread name.
 */
inline val currentThreadName: String get() = Thread.currentThread().name

/**
 * Gets if this thread is current thread.
 */
fun Thread.isCurrentThread() = this == currentThread

/**
 * Sleeps the current thread by [millis].
 */
fun sleep(millis: Long) = Thread.sleep(millis)
