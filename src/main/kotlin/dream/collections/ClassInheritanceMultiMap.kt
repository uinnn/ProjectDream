package dream.collections

import com.google.common.collect.*
import kotlin.reflect.*
import kotlin.reflect.full.*

class ClassInheritanceMultiMap<T : Any>(val inheritance: KClass<T>) : AbstractSet<T>() {
   companion object {
      val all = HashSet<KClass<*>>()
   }
   
   val map = HashMap<KClass<*>, ArrayList<T>>()
   val known: MutableSet<KClass<*>> = Sets.newIdentityHashSet<KClass<*>>()
   val values = ArrayList<T>()
   
   override val size: Int get() = values.size
   
   init {
      known += inheritance
      map[inheritance] = values
      all.forEach(::createLookup)
   }
   
   fun initLookup(clazz: KClass<*>): KClass<*> {
      if (!inheritance.isSubclassOf(clazz))
         error("Don't know how to search for $clazz")
      
      if (clazz !in known)
         createLookup(clazz)
      
      return clazz
   }
   
   fun createLookup(clazz: KClass<*>) {
      all += clazz
      
      values.forEach { value ->
         if (clazz.isSubclassOf(value::class))
            addForClass(value, clazz)
      }
      
      known += clazz
   }
   
   fun add(value: T): Boolean {
      known.forEach { clazz ->
         if (clazz.isSubclassOf(value::class))
            addForClass(value, clazz)
      }
      return true
   }
   
   fun addForClass(value: T, parentClass: KClass<*>) {
      val list = map.getOrPut(parentClass) { arrayListOf() }
      list += value
   }
   
   fun <S : Any> byClass(clazz: KClass<S>): List<S> {
      val list = map[initLookup(clazz)] ?: return emptyList()
      return list.filterIsInstance(clazz.java)
   }
   
   fun has(value: Any): Boolean {
      return value in byClass(value::class)
   }
   
   fun remove(value: T) {
      known.forEach { clazz ->
         if (clazz.isSubclassOf(value::class)) {
            val list = map[clazz] ?: return@forEach
            list -= value
         }
      }
   }
   
   override fun iterator(): Iterator<T> {
      return values.iterator()
   }
}
