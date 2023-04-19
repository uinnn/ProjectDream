@file:Suppress("NOTHING_TO_INLINE")

package dream.library

import dream.library.LibraryRegistry.FOLDER
import dream.utils.*
import java.io.*
import java.lang.reflect.*
import java.net.*

object LibraryRegistry {
   
   /**
    * All libraries loaded.
    */
   private val LIBRARIES = ArrayList<File>()
   
   /**
    * Folder used to store libraries.
    */
   val FOLDER = createFolder("libraries")
   
   /**
    * Loads the given URL as library.
    */
   private fun load(url: URL): Any = ADD_URL.invoke(CLASS_LOADER, url)
   
   /**
    * Transforms the given [file] that is not in default [FOLDER] as default library.
    */
   fun convertAsLib(file: File) {
      load(file.copyTo(FOLDER, true))
   }
   
   /**
    * Loads the given file as library.
    */
   fun load(file: File) {
      LIBRARIES += file
      load(file.toURI().toURL())
   }
   
   /**
    * Loads the given [library] with [repository].
    */
   fun load(repository: Repository, library: Library) {
      load(repository.download(library, FOLDER))
   }
   
   /**
    * Loads all libraries inside the given [file].
    */
   fun loadAll(file: File) {
      if (file.isDirectory)
         return
      
      file
         .files { extension == "jar" && this !in LIBRARIES }
         .forEach(::load)
   }
   
   /**
    * Loads all libraries inside the default folder.
    *
    * @see [FOLDER]
    */
   fun loadAll() = loadAll(FOLDER)
   
   /**
    * Main class loader for loading libraries.
    */
   private val CLASS_LOADER by lazy {
      ClassLoader.getSystemClassLoader() as URLClassLoader
   }
   
   /**
    * Gets the method used to add new URL libaries to the classpath.
    */
   private val ADD_URL: Method by lazy {
      URLClassLoader::class.java.getDeclaredMethod("addURL", URL::class.java).apply {
         isAccessible = true
      }
   }
}

/**
 * Load all libraries inside of this file.
 */
inline fun File.loadAllLibs() = LibraryRegistry.loadAll(this)

/**
 * Loads this file as a library.
 */
inline fun File.loadLib() = LibraryRegistry.load(this)

/**
 * Transforms this file that is not in default [FOLDER] as default library.
 */
inline fun File.convertAsLib() = LibraryRegistry.convertAsLib(this)
