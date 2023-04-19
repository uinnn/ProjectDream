package dream.block

import dream.*
import dream.block.material.*
import dream.block.state.*
import dream.collision.*
import dream.entity.*
import dream.entity.player.*
import dream.interfaces.*
import dream.item.*
import dream.level.*
import dream.misc.*
import dream.pos.*
import dream.tab.*
import dream.tiles.*
import dream.utils.*

/**
 * Represents a Block.
 */
@Open
public class Block(var material: Material, var pallete: MapPallete) : Keyable, Locale {
   
   constructor() : this(Materials.AIR)
   constructor(material: Material) : this(material, material.pallete)
   constructor(material: Material, tab: CreativeTab) : this(material) {
      this.tab = tab
   }
   
   /**
    * The key representing this block.
    */
   override val key: Key get() = Blocks.getKey(this) ?: MaterialKeys.AIR
   
   /**
    * The id representing this block.
    */
   val id: Int get() = Blocks.getId(this)
   
   
   /**
    * The unlocalized name of this block.
    */
   override var unlocalName: String = ""
   
   //
   //    EXTENSION SECTION
   //
   
   /**
    * Checks if this block is air.
    */
   val isAir get() = this is BlockAir
   
   //
   //    DATA SECTION
   //
   
   /**
    * The tab that this block is displayed.
    */
   lateinit var tab: CreativeTab
   
   /**
    * If this block is a full block.
    */
   var isFullBlock = isOpaqueCube
   
   /**
    * How much light is subtracted for going through this block.
    */
   var lightOpacity = if (isOpaqueCube) 255 else 0
   
   /**
    * If this block is translucent.
    */
   var isTranslucent = !material.blocksLight
   
   /**
    * Amount of light emitted by this block.
    */
   var lightEmitted = 0
   
   /**
    * If block should use the brightest neighbor light value as its own.
    */
   var useNeighborBrightness = false
   
   /**
    * Indicates how many hits it takes to break a block.
    */
   var hardness = 0f
   
   /**
    * Indicates how much this block can resist explosions.
    */
   var resistance = 0f
   
   /**
    * If stats is enabled for this block.
    */
   var enableStats = true
   
   /**
    * Flags whether or not this block is of a type that needs random ticking.
    *
    * Random tick is associated by level gamerule "randomTickSpeed".
    *
    * Ref-counted by ExtendedBlockStorage in
    * order to broadly cull a chunk from the random chunk update list for efficiency's sake.
    */
   var isTickable = false
   
   /**
    * If this block is a tile entity or not.
    */
   var isTileEntity = false
   
   /**
    * Gets if this block is a tile provider
    */
   val isTileProvider get() = this is TileProvider
   
   /**
    * Gets the tile provider of this block, or null if is not a tile provider.
    */
   val tileProvider: TileProvider? get() = this as? TileProvider
   
   /**
    * The AABB of this block.
    */
   var box = AABB(0, 0, 0, 1, 1, 1)
   
   /**
    * Gets the AABB min data of this block.
    */
   val minBox get() = box.min
   val minX get() = minBox.x
   val minY get() = minBox.y
   val minZ get() = minBox.z
   
   /**
    * Gets the AABB max data of this block.
    */
   val maxBox get() = box.max
   val maxX get() = maxBox.x
   val maxY get() = maxBox.y
   val maxZ get() = maxBox.z
   
   /**
    * The sounds data for this block.
    *
    * This control the place/break/step sound and their volume/pitch.
    */
   var sound = BlockSound.STONE
   
   /**
    * The gravity of particles on this block.
    */
   var particleGravity = 1.0f
   
   /**
    * Determines how much velocity is maintained while moving on top of this block.
    */
   var slipperiness = 0.6f
   
   /**
    * The block data for this block.
    */
   var data: BlockData = createData()
   
   /**
    * The default state for this block.
    */
   var state: IState = data.baseState
   
   /**
    * The render for this block.
    */
   val render = Render.BLOCK
   
   /**
    * If this block is a full cube.
    */
   val isFullCube get() = true
   
   /**
    * If this block is a opaque cube.
    *
    * Used to determine ambient occlusion and culling when rebuilding chunks for render.
    */
   val isOpaqueCube get() = true
   
   /**
    * If this block is a normal solid opaque cube.
    */
   val isBlockNormalCube get() = material.blocksMovement && isFullCube
   
   /**
    * Used for nearly all game logic (non-rendering) purposes.
    */
   val isNormalCube get() = material.isOpaque && isFullCube && !canProvidePower
   
   /**
    * If this block is visually opaque or not.
    */
   val isVisuallyOpaque get() = material.blocksMovement && isFullCube
   
   /**
    * If this block can provide power.
    */
   val canProvidePower get() = false
   
   //
   //    LISTENER SECTION
   //
   
   /**
    * Called when a update is fetched for this block.
    */
   fun onUpdate(level: Level, pos: Pos, state: IState) {
   }
   
   /**
    * Called randomly when level ticks.
    *
    * This is managed by level gamerule "randomTickSpeed".
    */
   fun onRandomTick(level: Level, pos: Pos, state: IState) {
      onUpdate(level, pos, state)
   }
   
   /**
    * Called randomly when is raining on this block.
    *
    * This is similar to [onRandomTick].
    */
   fun onRaining(level: Level, pos: Pos, state: IState) {
   }
   
   /**
    * Called when a neighbor block of this block changes.
    *
    * Can be applied to make physics for this block.
    */
   fun onNeighborChange(level: Level, pos: Pos, state: IState, neighbor: Block) {
   }
   
   /**
    * Called when player destroys this block.
    */
   fun onMine(level: Level, pos: Pos, state: IState, player: Player) {
   }
   
   /**
    * Called when player harvests this block.
    */
   fun onHarvest(level: Level, pos: Pos, state: IState, tile: Tile) {
   }
   
   /**
    * Called when this block is added to level.
    *
    * This is called whenever a block in a level changes to this block.
    */
   fun onAdded(pos: LevelPos, state: IState) {
   }
   
   /**
    * Called when this block is removed to level.
    *
    * This is called whenever a other block overrides this block in a level.
    */
   fun onRemoved(level: Level, pos: Pos, state: IState) {
   }
   
   /**
    * Called when this block explodes.
    *
    * This is called whenever this block is removed by due a explosion.
    */
   fun onExploded(level: Level, pos: Pos, state: IState, explosion: Explosion) {
   }
   
   /**
    * Called when player interacts with this block.
    *
    * This have a precisive touch with side directional and hit coordinates information.
    *
    * @param side the touch side information of the interaction.
    *             Can be used to verify if player have touched the UP/DOWN of the block.
    *
    * @param hit the hit coordinates information of the interaction.
    *            The x/y/z of the hit is calculated by the pixel of the block:
    *            x/y/z will always be in a range of [0.0..1.0] and every hit at pixel block
    *            adds 0.75 in their respectively coordinates.
    *
    * @param click the information of the click, if it's a right or left click.
    */
   fun onInteract(level: Level, pos: Pos, state: IState, player: Player, side: Direction, hit: Pos, click: Click) {
   }
   
   /**
    * Called when entity collides with this block.
    */
   fun onCollide(level: Level, pos: Pos, state: IState, entity: Entity) {
   }
   
   /**
    * Called when player places this block.
    *
    * This is called before updating the state to level, and can be used to modify the placed block state.
    *
    * @param side the touch side information of the interaction.
    *             Can be used to verify if player have touched the UP/DOWN of the block.
    *
    * @param hit the hit coordinates information of the interaction.
    *            The x/y/z of the hit is calculated by the pixel of the block:
    *            x/y/z will always be in a range of [0.0..1.0] and every hit at pixel block
    *            adds 0.75 in their respectively coordinates.
    *
    * @param meta meta information of the block placed. Is equals to the item metadata placed.
    */
   fun onPlaced(level: Level, pos: Pos, player: Player, meta: Int, side: Direction, hit: Pos): IState {
      return getStateFromMetadata(meta)
   }
   
   /**
    * Called after the block have been placed and updated to level.
    *
    * @param side the touch side information of the interaction.
    *             Can be used to verify if player have touched the UP/DOWN of the block.
    *
    * @param hit the hit coordinates information of the interaction.
    *            The x/y/z of the hit is calculated by the pixel of the block:
    *            x/y/z will always be in a range of [0.0..1.0] and every hit at pixel block
    *            adds 0.75 in their respectively coordinates.
    */
   fun onPostPlaced(level: Level, pos: Pos, player: Player, item: ItemStack, side: Direction, hit: Pos) {
   }
   
   //
   //    FUNCTION SECTION
   //
   
   /**
    * How many ticks to wait before ticking.
    */
   fun tickRate(level: Level): Int {
      return 10
   }
   
   /**
    * Creates the block data for this block.
    */
   fun createData(): BlockData {
      return BlockData(this)
   }
   
   /**
    * Gets the pallete by [state].
    */
   fun getPallete(state: IState): MapPallete {
      return pallete
   }
   
   /**
    * Convert the given metadata into a state for this Block.
    *
    * This is a deserialization function for blocks.
    */
   fun getStateFromMetadata(meta: Int): IState {
      return state
   }
   
   /**
    * Convert the state into the correct metadata value
    *
    * This is a serialization function for blocks.
    */
   fun getMetadataFromState(state: IState): Int {
      return 0
   }
   
   /**
    * Get the actual Block state of this Block at the given position.
    *
    * This applies properties not visible in the metadata, such as fence connections.
    */
   fun getActualState(state: IState, level: Level, pos: Pos): IState {
      return state
   }
   
   /**
    * Gets if entities can passes by this block.
    */
   fun isPassable(level: Level, pos: Pos): Boolean {
      return !material.blocksMovement
   }
   
   /**
    * Whether this Block can be replaced directly by other blocks.
    */
   fun isReplaceable(level: Level, pos: Pos): Boolean {
      return false
   }
   
   /**
    * Gets a specific block hardness in [level]/[pos].
    */
   fun getHardness(level: Level, pos: Pos): Float {
      return hardness
   }
   
   /**
    * Gets a mixed/combined light brightness for this block.
    */
   fun getMixedLight(level: Level, pos: Pos): Int {
      return 0
   }
   
   /**
    * Determinates if the [side] of this block should be rendered.
    */
   fun shouldSideBeRendered(level: Level, pos: Pos, side: Direction): Boolean {
      return when (side) {
         Direction.DOWN -> minY > 0.0
         Direction.UP -> maxY < 1.0
         Direction.NORTH -> minZ > 0.0
         Direction.SOUTH -> maxZ < 1.0
         Direction.WEST -> minX > 0.0
         Direction.EAST -> maxX < 1.0
      }
   }
   
   /**
    * Whether this Block is solid on the given Side
    */
   fun isSolid(level: Level, pos: Pos, side: Direction): Boolean {
      return material.isSolid
   }
   
   /**
    * Sets the AABB within a specific state.
    */
   fun setAABBWithState(level: Level, pos: Pos) {
   }
   
   /**
    * Gets a selected AABB within [pos].
    */
   fun getSelectedAABB(level: Level, pos: Pos): AABB {
      return box.added(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z)
   }
   
   /**
    * Gets a collision AABB within [pos].
    */
   fun getCollisionAABB(level: Level, pos: Pos, state: IState): AABB {
      return box.added(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z)
   }
   
   /**
    * Add all collision boxes of this Block to the list that intersect with the given mask.
    */
   fun addAABBTo(level: Level, pos: Pos, state: IState, mask: AABB, list: MutableList<AABB>, colliding: Entity) {
      val box = getCollisionAABB(level, pos, state)
      if (mask.intersects(box)) {
         list.add(box)
      }
   }
   
   /**
    * Checks wheter if this block is collidable or not.
    */
   fun isCollidable(): Boolean {
      return true
   }
   
   /**
    * Determinates if this block can collide or not.
    */
   fun canCollide(state: IState, hitIfLiquid: Boolean): Boolean {
      return isCollidable()
   }
   
   /**
    * Ray traces through the blocks collision from start pos to end pos returning a ray trace hit.
    */
   fun rayTrace(level: Level, pos: Pos, start: Pos, end: Pos): Hit {
      setAABBWithState(level, pos)
      
      val startPos = start.subtract(pos)
      val endPos = end.subtract(pos)
      
      val candidates = sequenceOfNotNull(
         startPos.intermediateX(endPos, minX),
         startPos.intermediateX(endPos, maxX),
         startPos.intermediateY(endPos, minY),
         startPos.intermediateY(endPos, maxY),
         startPos.intermediateZ(endPos, minZ),
         startPos.intermediateZ(endPos, maxZ),
      )
      
      val finalPos = candidates
         .filter { isInside(it) }
         .minByOrNull { it.distance(startPos) } ?: return Hit.Empty
      
      return Hit.Block(finalPos.offset(pos), Direction.nearestFrom(startPos, finalPos.subtract(pos)), pos)
   }
   
   /**
    * Checks if pos collides with this block.
    */
   fun isInside(pos: Pos): Boolean {
      return box.isInside(pos)
   }
   
   /**
    * Checks if coordinates collides with this block.
    */
   fun isInside(x: Double, y: Double, z: Double): Boolean {
      return box.isInside(x, y, z)
   }
   
   /**
    * Checks if entity collides with this block.
    */
   fun isInside(entity: Entity): Boolean {
      return box.isInside(entity)
   }
   
   /**
    * Gets the explosion resistance of this block.
    */
   fun getExplosionResistance(entity: Entity): Float {
      return resistance / 5
   }
   
   fun hasComparatorInputOverride(): Boolean {
      return false
   }
   
   /**
    * Get the ItemStack that this block drops.
    *
    * @param fortune the fortune level, 0 if not have fortune.
    */
   fun getDrop(level: Level, pos: Pos, state: IState, fortune: Int = 0): ItemStack {
      return emptyStack()
   }
   
   override fun toString(): String {
      return key.toString()
   }
   
   //
   //    BUILDER SECTION
   //
   
   fun withSound(sound: BlockSound): Block {
      this.sound = sound
      return this
   }
   
   fun withLightOpacity(opacity: Int): Block {
      lightOpacity = opacity
      return this
   }
   
   fun withLight(light: Float): Block {
      lightEmitted = (light * 15).toInt()
      return this
   }
   
   fun withResistance(resistance: Float): Block {
      this.resistance = resistance * 3
      return this
   }
   
   fun withHardness(hardness: Float): Block {
      this.hardness = hardness * 3
      resistance = resistance.coerceAtLeast(hardness * 5)
      return this
   }
   
   fun makeUnbreakable(): Block {
      return withHardness(-1f)
   }
   
   fun makeTickable(): Block {
      isTickable = true
      return this
   }
   
   fun withUnlocalName(name: String): Block {
      unlocalName = name
      return this
   }
   
   fun disableStats(): Block {
      enableStats = false
      return this
   }
   
   fun withTab(tab: CreativeTab): Block {
      this.tab = tab
      return this
   }
   
}
