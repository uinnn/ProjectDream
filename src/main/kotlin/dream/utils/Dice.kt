package dream.utils

import dream.utils.stat.range.*

/**
 * Assigns a probabilty to each distinct [T] item, and randomly selects [T] values given those probabilities.
 *
 * In other words, this is a Probability Density Function (PDF) for discrete [T] values.
 */
open class Dice<T>(val probabilities: MutableMap<T, Double>) {
   constructor(vararg values: Pair<T, Double>) : this(values.toMutableMap())
   
   /**
    * The sum of all probabilities.
    */
   open val total = probabilities.values.sum()
   
   /**
    * The distribution of the probabilities.
    */
   protected open var distribution = probabilities.run {
      var binStart = 0.0
      asSequence()
         .sortedBy { it.value }
         .map { it.key to OpenDoubleRange(binStart, it.value + binStart) }
         .onEach { binStart = it.second.endExclusive }
         .toMap()
   }
   
   /**
    * Gets a random chance for this dice based on [total].
    */
   fun random() = randomDouble(total)
   
   /**
    * Randomly selects an entry with probability by given [chance].
    */
   fun rollEntryWith(chance: Double) = distribution.first { chance in it.value }
   
   /**
    * Randomly selects an entry with probability.
    */
   fun rollEntry() = rollEntryWith(random())
   
   /**
    * Randomly selects a [T] value with probability by given [chance].
    */
   fun rollWith(chance: Double) = rollEntryWith(chance).key
   
   /**
    * Randomly selects a [T] value with probability.
    */
   fun roll() = rollWith(random())
   
   /**
    * Estabilishs all probabilities of this dice.
    */
   fun estabilish() {
      distribution = probabilities.run {
         var binStart = 0.0
         asSequence()
            .sortedBy { it.value }
            .map { it.key to OpenDoubleRange(binStart, it.value + binStart) }
            .onEach { binStart = it.second.endExclusive }
            .toMap()
      }
   }
}

/**
 * Assigns a probabilty to each distinct [T] item, and randomly selects [T] values given those probabilities.
 *
 * In other words, this is a Probability Density Function (PDF) for discrete [T] values.
 *
 * This is a mutable version of [Dice].
 */
class MutableDice<T>(probabilities: MutableMap<T, Double>) : Dice<T>(probabilities) {
   constructor(vararg values: Pair<T, Double>) : this(values.toMutableMap())
   
   override var total = super.total
      private set
   
   /**
    * Adds a new choice to this dice by given [value] with [chance].
    */
   fun with(value: T, chance: Double, estabilish: Boolean = true): MutableDice<T> {
      probabilities[value] = chance
      total += chance
      
      if (estabilish)
         estabilish()
      
      return this
   }
   
   /**
    * Removes a present choice of this dice by given [value].
    */
   fun without(value: T, estabilish: Boolean = true): MutableDice<T> {
      val chance = probabilities.remove(value) ?: return this
      total -= chance
      
      if (estabilish)
         estabilish()
      
      return this
   }
}


/**
 * Creates a new empty [Dice].
 */
fun <T> diceOf() = Dice<T>()

/**
 * Creates a new [Dice] with the given [elements].
 */
fun <T> diceOf(vararg elements: Pair<T, Double>) = Dice(*elements)

/**
 * Creates a new empty [MutableDice].
 */
fun <T> mutableDiceOf() = MutableDice<T>()

/**
 * Creates a new [MutableDice] with the given [elements].
 */
fun <T> mutableDiceOf(vararg elements: Pair<T, Double>) = MutableDice(*elements)

/**
 * Creates a new [Dice] by this map.
 */
@JvmName("toMapDice")
fun <T> Map<T, Double>.toDice() = Dice(toMutableMap())

/**
 * Creates a new [Dice] by this map.
 */
fun <T> MutableMap<T, Double>.toDice() = Dice(this)

/**
 * Creates a new [MutableDice] by this map.
 */
@JvmName("toMutDice")
fun <T> Map<T, Double>.toMutableDice() = MutableDice(toMutableMap())

/**
 * Creates a new [MutableDice] by this map.
 */
fun <T> MutableMap<T, Double>.toMutableDice() = MutableDice(this)

/**
 * Creates a mutable version of this dice.
 */
fun <T> Dice<T>.toMutable() = MutableDice(probabilities)
