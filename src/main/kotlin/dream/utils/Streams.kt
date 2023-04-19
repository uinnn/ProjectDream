package dream.utils

import java.io.*

/**
 * Calls [InputStream.copyTo] in a safe way without the necessity closing both streams.
 */
fun InputStream.safeCopyTo(output: OutputStream): Long {
   var bytes: Long
   use { input ->
      output.use { output ->
         bytes = input.copyTo(output)
      }
   }
   return bytes
}
