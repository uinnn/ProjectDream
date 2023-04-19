package dream.item

import com.google.common.collect.*
import dream.*
import dream.attribute.*
import dream.block.*
import dream.entity.*
import dream.entity.item.*
import dream.entity.monster.*
import dream.entity.player.*
import dream.interfaces.*
import dream.item.block.*
import dream.level.*
import dream.misc.*
import dream.nbt.types.*
import dream.network.*
import dream.pos.*
import dream.tab.*

/**
 * Represents an Item.
 */
@Open
public class Item : Keyable, Locale {
   
   constructor()
   constructor(tab: CreativeTab) {
      this.tab = tab
   }
   
   // initialize item data
   init {
      initialize()
   }
   
   /**
    * Initialize all defaults data for this item.
    */
   fun initialize() {
   }
   
   /**
    * The key representing this item.
    */
   override val key: Key get() = Items.getKey(this) ?: MaterialKeys.AIR
   
   /**
    * The id representing this item.
    */
   val id: Int get() = Items.getId(this)
   
   /**
    * The creative tab that's this item displays in creative mode.
    */
   var tab: CreativeTab? = null
   
   /**
    * Returns if this item has a creative tab.
    */
   val hasTab: Boolean get() = tab != null
   
   /**
    * The maximum size of the item.
    */
   var maxStack: Int = 64
   
   /**
    * The maximum durability of the item.
    */
   var maxDurability: Int = 0
   
   /**
    * If this item will be rendered fully 3D, like weapons and tools.
    */
   var fullyRender: Boolean = false
   
   /**
    * If this item has subtypes.
    *
    * Some items like dyes, wools and others, have various item varieties.
    */
   var hasSubtypes: Boolean = false
   
   /**
    * The crafting result of this item.
    */
   lateinit var craftingResult: Item
   
   /**
    * If this item has a crafting result.
    */
   val hasCraftingResult: Boolean get() = ::craftingResult.isInitialized
   
   /**
    * The String representing this item's effect on a potion when used as an ingredient.
    */
   var potionEffect: String = ""
   
   /**
    * The unlocalized name of this item.
    */
   override var unlocalName: String = ""
   
   /**
    * Gets the block associated to this item, or null if item is not [ItemBlock].
    */
   val block: Block?
      get() = if (this is ItemBlock) block else null
   
   /**
    * Reads [item] tag by [tag].
    *
    * Can be used to add extra data to a [ItemStack].
    */
   fun readTag(item: ItemStack, tag: CompoundTag) = Unit
   
   /**
    * Write [item] tag by [tag].
    *
    * Can be used to add extra data to a [ItemStack].
    */
   fun writeTag(item: ItemStack, tag: CompoundTag) = Unit
   
   /**
    * Write this item to the packet [buffer].
    */
   fun writeToPacket(item: ItemStack, buffer: PacketBuffer) = Unit
   
   /**
    * Reads this item from the packet [buffer].
    */
   fun readFromPacket(item: ItemStack, buffer: PacketBuffer) = Unit
   
   /**
    * Gets destroy speed multiplier of this item with [item] when breaking [block].
    */
   fun getDestroySpeed(item: ItemStack, block: Block) = 1f
   
   /**
    * Converts the given ItemStack damage value into a metadata
    * value to be placed in the world when this item is placed as a Block.
    */
   fun getMetadata(metadata: Int) = 0
   
   /**
    * Verify if this item can harvest [block] using [item].
    */
   fun canHarvest(item: ItemStack, block: Block) = false
   
   /**
    * Verify if this item is a map.
    */
   fun isMap() = false
   
   /**
    * Gets the enchantability factor of this item.
    */
   fun getEnchantability() = 0
   
   /**
    * Gets the rarity of this item.
    */
   fun getRarity(item: ItemStack) = if (item.isEnchanted) ItemRarity.RARE else ItemRarity.COMMON
   
   /**
    * Gets if this item has effect.
    */
   fun hasEffect(item: ItemStack) = item.isEnchanted
   
   /**
    * Gets the animation perfomed by this item when is being used.
    */
   fun getAnimation(item: ItemStack) = ItemAnimation.NONE
   
   /**
    * Gets the duration this item takes to use or consume this item.
    */
   fun getUseDuration(item: ItemStack) = 0
   
   /**
    * Returns if this item has a [maxStack] of 1 and is damageable.
    */
   fun isTool(item: ItemStack) = maxStack == 1 && isDamageable(item)
   
   /**
    * Returns the default name of this item based on [item].
    */
   fun getName(item: ItemStack) = ""
   
   /**
    * Returns the default lore of this item based on [item].
    */
   fun getLore(item: ItemStack): List<String> = emptyList()
   
   /**
    * Gets the potion effect of this item based on [item].
    */
   fun getPotionEffect(item: ItemStack) = potionEffect
   
   /**
    * Gets if this item is a potion ingredient.
    */
   fun isPotionIngredient(item: ItemStack) = getPotionEffect(item).isNotEmpty()
   
   /**
    * Returns if this item is damageable based on [item].
    */
   fun isDamageable(item: ItemStack) = true
   
   /**
    * Returns if this item should share tag on [PacketBuffer] based on [item].
    */
   fun shouldShareTag(item: ItemStack) = true
   
   /**
    * Returns if [item] is repaired by using [repair] in anvil.
    */
   fun isRepairable(item: ItemStack, repair: ItemStack) = false
   
   /**
    * Gets the default attributes of this item based on [item].
    */
   fun getAttributes(item: ItemStack): AttributeMap = HashMultimap.create()
   
   /**
    * Gets the unlocalized name of this item.
    */
   fun getUnlocalizedName() = "item.$unlocalName"
   
   /**
    * Gets the unlocalized name of this item.
    *
    * This version accepts [item] as parameter, so different stacks can have
    * different names based on their damage or NBT.
    */
   fun getUnlocalizedName(item: ItemStack) = "item.$unlocalName"
   
   /**
    * Gets if this item can be used to edit blocks.
    */
   fun canEditBlocks(level: Level, item: ItemStack, player: Player): Boolean {
      return false
   }
   
   /**
    * Called to tick item on inventory of [entity].
    */
   fun onTick(level: Level, item: ItemStack, entity: Entity, slot: Int, selected: Boolean) {
   
   }
   
   /**
    * Called to tick item when player is using.
    */
   fun onUseTick(level: Level, item: ItemStack, player: Player) {
   
   }
   
   /**
    * Called to tick armor on the armor slot.
    */
   fun onArmorTick(level: Level, item: ItemStack, entity: Entity, slot: Int) {
   
   }
   
   /**
    * Called to tick item while dropped as [EntityItem].
    */
   fun onEntityTick(level: Level, item: ItemStack, entity: EntityItem) {
   
   }
   
   /**
    * Called when this item is smelted.
    */
   fun onSmelted(level: Level, item: ItemStack, player: Player) {
   
   }
   
   /**
    * Called when this item is crafted.
    */
   fun onCrafted(level: Level, item: ItemStack, player: Player) {
   
   }
   
   /**
    * Called when a player right clicks in a block using this item.
    *
    * @param pos the block position
    * @param side the direction offset of block position
    * @param hit the hit vector data of the click
    */
   fun onUse(level: Level, item: ItemStack, player: Player, pos: Pos, side: Direction, hit: Pos): Boolean {
      return true
   }
   
   /**
    * Called whenever this item is equipped and the player right clicks using this item.
    */
   fun onRightClick(level: Level, item: ItemStack, player: Player): ItemStack {
      return item
   }
   
   /**
    * Called when the player finishes using this Item (e.g. finishes eating.).
    *
    * Not called when the player stops using the Item before the action is complete.
    */
   fun onFinishUse(level: Level, item: ItemStack, player: Player): ItemStack {
      return item
   }
   
   /**
    * Called when [player] stops using this item.
    *
    * @param timeLeft the time left to end use.
    */
   fun onStoppedUsing(level: Level, item: ItemStack, player: Player, timeLeft: Int) {
   
   }
   
   /**
    * Called when [player] right-clicks on [target] using this item.
    */
   fun onInteract(level: Level, item: ItemStack, player: Player, target: Entity): Boolean {
      return true
   }
   
   /**
    * Called when this item is dropped.
    */
   fun onDrop(level: Level, item: ItemStack, entity: Entity): Boolean {
      return true
   }
   
   /**
    * Hurts the given [target] entity by [attacker] using this item.
    */
   fun hurt(level: Level, item: ItemStack, attacker: Creature, target: Entity): Boolean {
      return true
   }
   
   /**
    * Called when [player] mines [block] succesfully using this item.
    */
   fun mine(level: Level, item: ItemStack, player: Player, block: Block, pos: Loc): Boolean {
      return true
   }
   
   /**
    * Determines if this Item is fire immune.
    *
    * This is a simple pre-made 1.16 netherite fire resistance feature on 1.8.
    */
   fun isFireImmune(level: Level, entity: EntityItem, item: ItemStack): Boolean {
      return false
   }
   
   /**
    * Gets the lifespan in ticks for [EntityItem] before removes it.
    */
   fun lifespan(level: Level, item: ItemStack): Int {
      return 6000
   }
   
   /**
    * Determines if this Item has a special entity for when they are in the world.
    *
    * When a [EntityItem] is spawned will check for this function and
    * if returns true, will call and spawn [createEntity].
    */
   fun hasCustomEntity(level: Level, item: ItemStack): Boolean {
      return false
   }
   
   /**
    * This function should return a new entity to replace the dropped item.
    *
    * This function is only called if [hasCustomEntity] returns true.
    */
   fun createEntity(level: Level, entity: EntityItem, item: ItemStack): Entity {
      return entity
   }
   
   /**
    * Whether this Item can be used to hide player head for enderman.
    */
   fun isEnderMask(level: Level, item: ItemStack, player: Player, enderman: Enderman): Boolean {
      return this is ItemPumpkin
   }
   
   override fun toString() = key.toString()
}
