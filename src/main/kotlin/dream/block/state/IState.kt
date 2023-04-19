package dream.block.state

import dream.block.*
import dream.block.property.*
import dream.misc.*
import dream.utils.*

/**
 * Represents a block state.
 *
 * State is capable to store persistent data across a block.
 */
@Open
public interface IState {
   
   /**
    * Gets the unique id for this state.
    *
    * This is a combination of [block] id and [block] metadata from this state.
    *
    * Is supposed that the state [block] is registered.
    */
   val uniqueId: Int get() = Blocks.STATES[this]
   
   /**
    * Gets all properties keys of this block state.
    */
   public val properties: Set<Prop>
   
   /**
    * Gets all properties values of this block state.
    *
    * Key - represents the property data
    *
    * Value - represents the actual value.
    */
   public val values: PropertyMap
   
   /**
    * Gets the block representing this block state.
    */
   public val block: Block
   
   /**
    * Gets the current value of [property] on this block state.
    */
   public fun <T : Any> getValue(property: Property<T>): T
   
   /**
    * Gets a new state with [value] setted by [property].
    */
   public fun <T : Any> with(property: Property<T>, value: T): IState
   
   /**
    * Cycles the value of [property] on this block state.
    */
   public fun <T : Any> cycle(property: Property<T>): IState {
      return with(property, property.allowedValues.next(getValue(property)))
   }
}
