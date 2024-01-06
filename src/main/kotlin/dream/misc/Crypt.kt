package dream.misc

import java.security.*
import java.security.spec.*
import javax.crypto.*
import javax.crypto.spec.*

/**
 * Crypt data manager.
 */
object Crypt {

  @JvmField
  val KEY_GENERATOR: KeyGenerator = KeyGenerator.getInstance("AES").apply { init(128) }
  @JvmField
  val KEY_FACTORY: KeyFactory = KeyFactory.getInstance("RSA")
  @JvmField
  val KEY_PAIR_GENERATOR: KeyPairGenerator = KeyPairGenerator.getInstance("RSA").apply { initialize(1024) }

  /**
   * Generate a new shared secret AES key from a secure random source
   */
  fun createNewSharedKey(): SecretKey = KEY_GENERATOR.generateKey()

  /**
   * Generates RSA KeyPair
   */
  fun generateKeyPair(): KeyPair = KEY_PAIR_GENERATOR.generateKeyPair()

  /**
   * Compute a serverId hash for use by sendSessionRequest()
   */
  fun getServerIdHash(serverId: String, publicKey: PublicKey, secretKey: SecretKey): ByteArray {
    return digestOperation("SHA-1", serverId.toByteArray(Charsets.ISO_8859_1), secretKey.encoded, publicKey.encoded)
  }

  /**
   * Compute a message digest on arbitrary byte[] data
   */
  fun digestOperation(algorithm: String, vararg data: ByteArray): ByteArray {
    val digest = MessageDigest.getInstance(algorithm)
    for (value in data) digest.update(value)
    return digest.digest()
  }

  /**
   * Create a new PublicKey from encoded X.509 data
   */
  fun decodePublicKey(encodedKey: ByteArray): PublicKey {
    val encodedSpec = X509EncodedKeySpec(encodedKey)
    return KEY_FACTORY.generatePublic(encodedSpec)
  }

  /**
   * Decrypt shared secret AES key using RSA private key
   */
  fun decryptSharedKey(key: PrivateKey, secretKeyEncrypted: ByteArray): SecretKey {
    return SecretKeySpec(decryptData(key, secretKeyEncrypted), "AES")
  }

  /**
   * Encrypt byte[] data with RSA public key
   */
  fun encryptData(key: Key, data: ByteArray): ByteArray {
    return cipherOperation(1, key, data)
  }

  /**
   * Decrypt byte[] data with RSA private key
   */
  fun decryptData(key: Key, data: ByteArray): ByteArray {
    return cipherOperation(2, key, data)
  }

  /**
   * Encrypt or decrypt byte[] data using the specified key
   */
  fun cipherOperation(opMode: Int, key: Key, data: ByteArray): ByteArray {
    return createCipher(opMode, key.algorithm, key).doFinal(data)
  }

  /**
   * Creates the Cipher Instance.
   */
  private fun createCipher(opMode: Int, transformation: String, key: Key): Cipher {
    val cipher = Cipher.getInstance(transformation)
    cipher.init(opMode, key)
    return cipher
  }

  /**
   * Creates a Cipher instance using the AES/CFB8/NoPadding algorithm. Used for protocol encryption.
   */
  fun createNetCipher(opMode: Int, key: Key): Cipher {
    val cipher = Cipher.getInstance("AES/CFB8/NoPadding")
    cipher.init(opMode, key, IvParameterSpec(key.encoded) as AlgorithmParameterSpec)
    return cipher
  }
}

/**
 * Creates a Cipher instance using the AES/CFB8/NoPadding algorithm. Used for protocol encryption.
 */
fun Key.createNetCipher(opMode: Int): Cipher = Crypt.createNetCipher(opMode, this)

/**
 * Encrypt this data with RSA public key
 */
fun ByteArray.cipher(key: Key) = Crypt.cipherOperation(1, key, this)

/**
 * Decrypt this data with RSA private key
 */
fun ByteArray.decipher(key: Key) = Crypt.cipherOperation(2, key, this)

/**
 * Encrypt byte[] data with RSA public key
 */
fun ByteArray.decodePublicKey() = Crypt.decodePublicKey(this)
