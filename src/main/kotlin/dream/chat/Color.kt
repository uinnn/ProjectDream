package dream.chat

/**
 * Represents a color in the chat.
 */
public enum class Color(
   val display: String,
   val code: Char,
   val index: Int = -1,
   val isStyle: Boolean = false,
) {
   
   /**
    * Black color
    *
    * * Index: 0
    * * Code: 0
    * * Label: §0
    */
   BLACK("BLACK", '0', 0),
   
   /**
    * Dark blue color
    *
    * * Index: 1
    * * Code: 1
    * * Label: §1
    */
   DARK_BLUE("DARK_BLUE", '1', 1),
   
   /**
    * Dark green color
    *
    * * Index: 2
    * * Code: 2
    * * Label: §2
    */
   DARK_GREEN("DARK_GREEN", '2', 2),
   
   /**
    * Dark aqua color
    *
    * * Index: 3
    * * Code: 3
    * * Label: §3
    */
   DARK_AQUA("DARK_AQUA", '3', 3),
   
   /**
    * Dark red color
    *
    * * Index: 4
    * * Code: 4
    * * Label: §4
    */
   DARK_RED("DARK_RED", '4', 4),
   
   /**
    * Dark purple color
    *
    * * Index: 5
    * * Code: 5
    * * Label: §5
    */
   DARK_PURPLE("DARK_PURPLE", '5', 5),
   
   /**
    * Gold color
    *
    * * Index: 6
    * * Code: 6
    * * Label: §6
    */
   GOLD("GOLD", '6', 6),
   
   /**
    * Gray color
    *
    * * Index: 7
    * * Code: 7
    * * Label: §7
    */
   GRAY("GRAY", '7', 7),
   
   /**
    * Dark gray color
    *
    * * Index: 8
    * * Code: 8
    * * Label: §8
    */
   DARK_GRAY("DARK_GRAY", '8', 8),
   
   /**
    * Blue color
    *
    * * Index: 9
    * * Code: 9
    * * Label: §9
    */
   BLUE("BLUE", '9', 9),
   
   /**
    * Green color
    *
    * * Index: 10
    * * Code: a
    * * Label: §a
    */
   GREEN("GREEN", 'a', 10),
   
   /**
    * Aqua color
    *
    * * Index: 11
    * * Code: b
    * * Label: §b
    */
   AQUA("AQUA", 'b', 11),
   
   /**
    * Red color
    *
    * * Index: 12
    * * Code: c
    * * Label: §c
    */
   RED("RED", 'c', 12),
   
   /**
    * Pink color
    *
    * * Index: 13
    * * Code: d
    * * Label: §d
    */
   PINK("LIGHT_PURPLE", 'd', 13),
   
   /**
    * Yellow color
    *
    * * Index: 14
    * * Code: e
    * * Label: §e
    */
   YELLOW("YELLOW", 'e', 14),
   
   /**
    * White color
    *
    * * Index: 15
    * * Code: f
    * * Label: §f
    */
   WHITE("WHITE", 'f', 15),
   
   /**
    * Obfuscated style.
    *
    * * Code: k
    * * Label: §k
    */
   OBFUSCATED("OBFUSCATED", 'k', isStyle = true),
   
   /**
    * Bold style.
    *
    * * Code: l
    * * Label: §l
    */
   BOLD("BOLD", 'l', isStyle = true),
   
   /**
    * Strikethrough style.
    *
    * * Code: m
    * * Label: §m
    */
   STRIKETHROUGH("STRIKETHROUGH", 'm', isStyle = true),
   
   /**
    * Underline style.
    *
    * * Code: n
    * * Label: §n
    */
   UNDERLINE("UNDERLINE", 'n', isStyle = true),
   
   /**
    * Italic style.
    *
    * * Code: o
    * * Label: §o
    */
   ITALIC("ITALIC", 'o', isStyle = true),
   
   /**
    * Reset function.
    *
    * The main function of this style is resetting all colors and styles to default.
    *
    * * Code: r
    * * Label: §r
    */
   RESET("RESET", 'r', -1);
   
   /**
    * The control label of this color.
    */
   val label get() = "$LABEL$code"
   
   /**
    * Returns if this type color is a true color.
    */
   val isColor get() = !isStyle && this != RESET
   
   override fun toString(): String = label
   
   companion object {
      val values = values()
      
      private val REGEX = "(?i)§[0-9A-FK-OR]".toRegex()
      
      /**
       * The default label used to format colors.
       */
      const val LABEL = '§'
      
      /**
       * Returns a copy of the given string, with formatting codes stripped away.
       */
      fun withoutCode(text: String): String {
         return if (text.isEmpty()) text else REGEX.replace(text, "")
      }
   }
}

/**
 * Returns a copy of this string uncolorized.
 */
inline val String.uncolor get() = Color.withoutCode(this)
