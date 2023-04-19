@file:Suppress("NOTHING_TO_INLINE")

package dream.item

import dream.*
import dream.block.*
import dream.item.Items.AIR
import dream.item.block.*
import dream.item.misc.*
import dream.nbt.*
import dream.nbt.types.*
import dream.registry.*

/**
 * Represents a registry for all [Item]'s.
 */
object Items : NamespacedRegistry<Key, Item>() {
   
   /**
    * The air Item instance.
    *
    * Use this instead of null.
    */
   val AIR = vanilla(0, "air", ItemAir())
   
   val STONE = vanilla(1, "stone", Item())
   
   val BOOK = vanilla(-1, "book", ItemBook())
   
   /**
    * A registry to get [Item]'s from [Block]'s.
    */
   private val BLOCK_TO_ITEM = HashMap<Block, Item>()
   
   /**
    * Gets an item from the specified [block] or null if not existent.
    */
   fun byBlockOrNull(block: Block) = BLOCK_TO_ITEM[block]
   
   /**
    * Gets an item from the specified [block] or [AIR] if not existent.
    */
   fun byBlock(block: Block) = BLOCK_TO_ITEM[block] ?: AIR
   
   /**
    * Gets an item from the specified [id] or null if not existent.
    */
   fun byIdOrNull(id: Int) = get(id)
   
   /**
    * Gets an item from the specified [id] or [AIR] if not existent.
    */
   fun byId(id: Int) = get(id) ?: AIR
   
   /**
    * Gets an item from [key] or null if not existent.
    */
   fun byKeyOrNull(key: Key) = get(key)
   
   /**
    * Gets an item from [key] or [AIR] if not existent.
    */
   fun byKey(key: Key) = get(key) ?: AIR
   
   /**
    * Tries to find an item from [id] (e.g. `"minecraft:apple"`)
    * or a numerical id represented by the String.
    *
    * If both fail, will return null.
    */
   fun findOrNull(id: String) = byKeyOrNull(key(id)) ?: byIdOrNull(id.toIntOrNull() ?: -1)
   
   /**
    * Tries to find an item from [id] (e.g. `"minecraft:apple"`)
    * or a numerical id represented by the String.
    *
    * If both fail, will return [AIR].
    */
   fun find(id: String) = byKeyOrNull(key(id)) ?: byId(id.toIntOrNull() ?: -1)
   
   /**
    * Gets an item from [tag] key value specified by [key].
    */
   fun byTag(tag: CompoundTag, key: String): Item {
      return if (tag.has(key, StringType)) {
         find(tag.string(key))
      } else {
         byId(tag.short(key).toInt())
      }
   }
   
   /**
    * Register a new item in the registry specified by [id] and [key].
    */
   fun <T : Item> register(id: Int, key: String, item: T): T {
      register(id, key(key), item)
      return item
   }
   
   
   /**
    * Register a new item block by the specified [block] model.
    */
   fun <T : Item> register(block: Block, item: T): T {
      register(block.id, block.key, item)
      BLOCK_TO_ITEM[block] = item
      return item
   }
   
   /**
    * Register a new [ItemBlock] by the specified [block] model.
    */
   fun register(block: Block): ItemBlock {
      return register(block, ItemBlock(block))
   }
   
   /**
    * Register a new item in the registry specified by [id] and [key].
    */
   private fun <T : Item> vanilla(id: Int, key: String, item: T): T {
      register(id, minecraftKey(key), item)
      return item
   }
   
}

/**
 * Gets an item from the specified [id] or [AIR] if not existent.
 */
inline fun itemOf(id: Int) = Items.byId(id)

/**
 * Gets an item from [key] or [AIR] if not existent.
 */
inline fun itemOf(key: Key) = Items.byKey(key)

/**
 * Tries to find an item from [id] (e.g. `"minecraft:apple"`)
 * or a numerical id represented by the String.
 *
 * If both fail, will return [AIR].
 */
inline fun itemOf(id: String) = Items.find(id)
