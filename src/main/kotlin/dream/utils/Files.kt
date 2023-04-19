@file:Suppress("NOTHING_TO_INLINE")

package dream.utils

import java.io.*

/**
 * Creates this file if not exists.
 */
fun File.create(): File {
   if (!exists()) {
      if (parentFile != null) parentFile.mkdirs()
      createNewFile()
   }
   return this
}

/**
 * Creates a folder from this file if not exists.
 */
fun File.createFolder(): File {
   if (!exists()) mkdirs()
   return this
}

inline fun file(path: String) = File(path)
inline fun file(path: String, child: String) = File(path, child)
inline fun file(root: File, child: String) = File(root, child)

fun createFile(path: String) = File(path).create()
fun createFile(parent: String, name: String) = File(parent, name).create()
fun createFile(parent: File, name: String) = File(parent, name).create()

fun createFolder(path: String) = File(path).createFolder()
fun createFolder(parent: String, name: String) = File(parent, name).createFolder()
fun createFolder(parent: File, name: String) = File(parent, name).createFolder()

/**
 * List all files from this file directory.
 */
fun File.files(): List<File> = listFiles()?.toList() ?: emptyList()

/**
 * List all files from this file directory filtered by the given [filter].
 */
fun File.files(filter: File.() -> Boolean): List<File> = listFiles(filter)?.toList() ?: emptyList()

/**
 * List all directories from this file directory.
 */
fun File.directories(): List<File> = files { isDirectory }

/**
 * List all directories from this file directory filtered by the given [filter].
 */
fun File.directories(filter: File.() -> Boolean): List<File> = files { isDirectory && filter() }
