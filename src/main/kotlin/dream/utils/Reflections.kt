package dream.utils

import kotlin.reflect.*
import kotlin.reflect.full.*
import java.lang.reflect.*

/**
 * Gets all enum constants of this class.
 */
val <T : Enum<T>> KClass<T>.enums: Array<T> get() = java.enumConstants

/**
 * Converts this array of kotlin classes to java classes.
 */
val <T : Any> Array<out KClass<out T>>.java: Array<Class<out T>>
   get() = mapArray { it.java }

/**
 * Gets a constructor of this class by [predicate].
 */
inline fun <T : Any> KClass<T>.findConstructor(predicate: Predicate<KFunction<T>>): KFunction<T>? {
   return constructors.find(predicate)
}

/**
 * Gets a constructor of this class by [parameters].
 */
fun <T : Any> KClass<T>.findConstructor(vararg parameters: KClass<out Any>): Constructor<T>? {
   return catchingOrNull {
      java.getDeclaredConstructor(*parameters.java)
   }
}

/**
 * Creates a new instance of this class by [parameters].
 */
fun <T : Any> KClass<T>.newInstance(vararg parameters: Any): T {
   val constructor = findConstructor(*parameters.mapArray { it::class })
      ?: error("No constructor found for parameters $parameters")
   
   return constructor.newInstance(*parameters)
}

/**
 * Creates a new instance of this class by [parameters].
 */
inline fun <T : Any> KClass<T>.newInstance(vararg parameters: Any, builder: T.() -> Unit): T {
   return newInstance(*parameters).apply(builder)
}

/**
 * Gets a constructor of this class.
 */
inline fun <T : Any> KClass<T>.createInstance(builder: T.() -> Unit): T {
   return createInstance().apply(builder)
}

/**
 * Makes the this object acessible.
 */
fun <T : AccessibleObject> T.accessible(): T {
   isAccessible = true
   return this
}

/**
 * Finds a Callable object on this iterable by name.
 */
fun <T, C : KCallable<T>> Iterable<C>.byName(name: String): C? {
   return find { name == it.name }
}

/**
 * Gets a member property by name.
 */
fun <T : Any> KClass<T>.property(name: String, declareds: Boolean = true): KProperty1<T, *>? {
   return if (declareds) {
      declaredMemberProperties.byName(name)
   } else {
      memberProperties.byName(name)
   }
}

/**
 * Gets a member extension property by name.
 */
fun <T : Any> KClass<T>.extensionProperty(name: String, declareds: Boolean = true): KProperty2<T, *, *>? {
   return if (declareds) {
      declaredMemberExtensionProperties.byName(name)
   } else {
      memberExtensionProperties.byName(name)
   }
}

/**
 * Gets a member function by name.
 */
fun <T : Any> KClass<T>.function(name: String, declareds: Boolean = true): KFunction<*>? {
   return if (declareds) {
      declaredMemberFunctions.byName(name)
   } else {
      memberFunctions.byName(name)
   }
}

/**
 * Gets a member extension function by name.
 */
fun <T : Any> KClass<T>.extensionFunction(name: String, declareds: Boolean = true): KFunction<*>? {
   return if (declareds) {
      declaredMemberExtensionFunctions.byName(name)
   } else {
      memberExtensionFunctions.byName(name)
   }
}

/**
 * Gets the object instance of this class or creates
 * a new instance if this class is not a object.
 */
fun <T : Any> KClass<T>.objectOrNew(): T {
   return objectInstance ?: createInstance()
}

/**
 * Gets the object instance of this class or creates
 * a new instance if this class is not a object.
 */
fun <T : Any> KClass<T>.objectOrNew(vararg parameters: Any): T {
   return objectInstance ?: newInstance(*parameters)
}
