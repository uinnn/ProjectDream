package dream.library

import dream.utils.*
import java.io.*
import java.net.URL

/**
 * Represents a repository for libraries.
 */
@JvmInline
value class Repository(val url: String) {

  /**
   * Returns the url of [library] in this repository.
   */
  fun url(library: Library): URL {
    val artifact = library.artifact.replace('.', '/')
    val group = library.group.replace('.', '/')
    val version = library.version
    return URL("$url$group/$artifact/$version/$artifact-$version")
  }

  /**
   * Returns the Jar input stream of [Library] in this repository.
   */
  fun stream(library: Library): InputStream {
    val url = url(library)
    return URL("$url.jar").openStream()
  }

  /**
   * Downloads [library] inside of the file [inside] and returns
   * the downloaded file.
   *
   * If the library is already downloaded, this will return the file contained.
   */
  fun download(library: Library, inside: File): File {
    val url = url(library)
    val name = url.file.substring(url.file.lastIndexOf('/') + 1)
    val file = file(inside, name)
    if (file.exists()) return file else file.createNewFile()
    url.openStream().safeCopyTo(file.outputStream())
    return file
  }

  companion object {
    val MAVEN = Repository("https://repo.maven.apache.org/maven2/")
  }
}
