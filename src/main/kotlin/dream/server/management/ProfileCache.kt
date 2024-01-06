package dream.server.management

import com.mojang.authlib.*
import com.soywiz.kds.iterators.*
import dream.serializer.*
import dream.server.*
import dream.utils.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import org.joda.time.*
import java.io.*
import java.util.*

/**
 * Represents a profile cache.
 *
 * Profiles will be cached after being fetched from Minecraft Auth API.
 */
class ProfileCache(val server: Server, val file: File) {
  
  /**
   * All profiles cached.
   */
  val profiles = LinkedList<GameProfile>()
  
  /**
   * Profiles lookup by UUID.
   */
  val idLookup = HashMap<UUID, ProfileEntry>()
  
  /**
   * Profiles lookup by Name.
   */
  val nameLookup = HashMap<String, ProfileEntry>()
  
  /**
   * Get all usernames that are cached in this cache.
   */
  val usernames get() = nameLookup.keys
  
  /**
   * Saves the cache in [file].
   */
  fun save() {
    file.outputStream().use {
      Json.encodeToStream(limit(), it)
    }
  }
  
  /**
   * Loads the cache from [file].
   */
  fun load() {
    file.inputStream().use { stream ->
      clearCache()
      Json.decodeFromStream<List<ProfileEntry>>(stream).fastForEachReverse {
        addEntry(it.profile, it.expiration)
      }
    }
  }
  
  /**
   * Adds an new entry to this cache.
   */
  fun addEntry(profile: GameProfile, expiration: DateTime = DateTime().plusMonths(1)) {
    val entry = ProfileEntry(profile, expiration)
    val id = profile.id
    if (id in idLookup) {
      val existent = idLookup[id]!!
      nameLookup.remove(existent.profile.name.lowercase())
      profiles -= profile
    }
    
    nameLookup[profile.name.lowercase()] = entry
    idLookup[id] = entry
    profiles.addFirst(profile)
    save()
  }
  
  /**
   * Get a player's GameProfile given their username.
   *
   * Mojang's server's will be contacted if the entry is not cached
   * locally.
   */
  fun getProfileByName(username: String): GameProfile? {
    val name = username.lowercase()
    var entry = nameLookup[name]
    
    if (entry != null && entry.isExpired()) {
      val profile = entry.profile
      idLookup.remove(profile.id)
      nameLookup.remove(profile.name)
      profiles.remove(profile)
      entry = null
    }
    
    if (entry != null) {
      val profile = entry.profile
      profiles.remove(profile)
      profiles.addFirst(profile)
    } else {
      // contact mojang servers
      val profile = searchProfile(server, name)
      if (profile != null) {
        addEntry(profile)
        entry = nameLookup[name]
      }
    }
    
    save()
    return entry?.profile
  }
  
  /**
   * Gets a profile cached by [id].
   */
  fun findProfileById(id: UUID): GameProfile? {
    return idLookup[id]?.profile
  }
  
  /**
   * Gets a profile entry cached by [id].
   */
  fun findById(id: UUID): ProfileEntry? {
    val entry = idLookup[id] ?: return null
    val profile = entry.profile
    profiles.remove(profile)
    profiles.addFirst(profile)
    return entry
  }
  
  fun limit(size: Int = 1000): List<ProfileEntry> {
    return profiles.take(size).filterNotNull { findById(it.id) }
  }
  
  fun clearCache() {
    profiles.clear()
    idLookup.clear()
    nameLookup.clear()
  }
  
  companion object {
    
    /**
     * Searchs a [GameProfile] in [server] with [name].
     */
    fun searchProfile(server: Server, name: String): GameProfile? {
      var gameprofile: GameProfile? = null
      val callback = object : ProfileLookupCallback {
        override fun onProfileLookupSucceeded(profile: GameProfile?) {
          gameprofile = profile
        }
        
        override fun onProfileLookupFailed(profile: GameProfile?, exception: Exception?) {
          gameprofile = null
        }
      }
      
      server.profileRepository.findProfilesByNames(arrayOf(name), Agent.MINECRAFT, callback)
      if (!server.isOnlineMode && gameprofile == null) {
        callback.onProfileLookupSucceeded(GameProfile(OfflineUUID(name), name))
      }
      
      return gameprofile
    }
    
  }
  
}


/**
 * Represents a profile entry on [ProfileCache].
 */
@Serializable(ProfileEntry.Serializer::class)
class ProfileEntry(
  var profile: GameProfile,
  var expiration: DateTime,
) : JsonStorable<ProfileEntry>, Comparable<ProfileEntry> {
  
  constructor(data: JsonObject) : this(
    profile = profileOf(data)!!, expiration = DateTime(data.long("expiresOn", 0))
  )
  
  override fun serialize(data: JsonObjectBuilder) {
    profile.storeAt(data)
    data.put("expiresOn", FORMAT.format(expiration))
  }
  
  override fun deserialize(data: JsonObject) {
    profile = profileOf(data)!!
    expiration = DateTime(data.long("expiresOn", 0))
  }
  
  override fun compareTo(other: ProfileEntry): Int {
    return expiration.compareTo(other.expiration)
  }
  
  fun isExpired(): Boolean {
    return now > expiration.millis
  }
  
  object Serializer : JsonLikeSerializer<ProfileEntry>() {
    override fun createObject(data: JsonObject) = ProfileEntry(data)
  }
}
