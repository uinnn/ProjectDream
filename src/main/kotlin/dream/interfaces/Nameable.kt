package dream.interfaces

import dream.chat.*

/**
 * Represents an object that's can have a name.
 */
public interface Nameable {
   
   /**
    * The name of this object.
    */
   public val name: String
   
   /**
    * Returns if this object has a name.
    */
   public val hasName: Boolean
      get() = name.isNotBlank()
   
   /**
    * Returns the display name of this object.
    */
   public val displayName: Component
      get() = text(name)
}
