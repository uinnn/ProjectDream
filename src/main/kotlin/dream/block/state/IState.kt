package dream.block.state

import dream.*
import dream.block.*
import dream.block.property.*
import dream.collision.*
import dream.entity.base.*
import dream.entity.player.*
import dream.item.*
import dream.level.*
import dream.level.chunk.*
import dream.misc.*
import dream.pos.*
import dream.tiles.*
import dream.utils.*

/**
 * Represents a block state.
 *
 * State is capable to store persistent data across a block.
 */
@Open
interface IState {
  
  /**
   * Gets the unique id for this state.
   *
   * This is a combination of [block] id and [block] metadata from this state.
   *
   * Is supposed that the state [block] is registered.
   */
  val id: Int get() = Blocks.STATES[this]
  
  /**
   * Gets all properties keys of this block state.
   */
  val properties: Set<Prop>
  
  /**
   * Gets all properties values of this block state.
   *
   * Key - represents the property data
   *
   * Value - represents the actual value.
   */
  val values: PropertyMap
  
  /**
   * Gets the block representing this block state.
   */
  val block: Block
  
  /**
   * Gets the current value of [property] on this block state.
   */
  fun <T : Any> getValue(property: Property<T>): T
  
  /**
   * Gets a new state with [value] setted by [property].
   */
  fun <T : Any> with(property: Property<T>, value: T): IState
  
  /**
   * Cycles the value of [property] on this block state.
   */
  fun <T : Any> cycle(property: Property<T>): IState {
    return with(property, property.allowedValues.next(getValue(property)))
  }
  
  
  // DELEGATION FOR SHORTCUT BASED ON BLOCKS
  
  fun its(other: IState) = block === other.block
  fun its(other: Block) = block === other
  
  /**
   * The key representing this block.
   */
  val key: Key get() = block.key
  
  /**
   * The id representing this block.
   */
  val blockId: Int get() = block.id
  
  /**
   * The item backed of this block.
   */
  val item get() = block.item
  
  /**
   * Gets the block material.
   */
  val material get() = block.material
  
  /**
   * Gets the block pallete.
   */
  val mapPallete get() = block.pallete
  
  /**
   * Checks if this block is air.
   */
  val isAir get() = block.isAir
  
  /**
   * If this block is a full block.
   */
  val isFullBlock get() = block.isFullBlock
  
  /**
   * How much light is subtracted for going through this block.
   */
  val lightOpacity get() = block.lightOpacity
  
  /**
   * If this block is translucent.
   */
  val isTranslucent get() = block.isTranslucent
  
  /**
   * Amount of light emitted by this block.
   */
  val lightEmitted get() = block.lightEmitted
  
  /**
   * If block should use the brightest neighbor light value as its own.
   */
  val useNeighborBrightness get() = block.useNeighborBrightness
  
  /**
   * Indicates how many hits it takes to break a block.
   */
  val hardness get() = block.hardness
  
  /**
   * Indicates how much this block can resist explosions.
   */
  val resistance get() = block.resistance
  
  /**
   * If stats is enabled for this block.
   */
  val enableStats get() = block.enableStats
  
  /**
   * Flags whether or not this block is of a type that needs random ticking.
   *
   * Random tick is associated by level gamerule "randomTickSpeed".
   *
   * Ref-counted by ExtendedBlockStorage in
   * order to broadly cull a chunk from the random chunk update list for efficiency's sake.
   */
  val isTickable get() = block.isTickable
  
  /**
   * If this block is a tile entity or not.
   */
  val isTileEntity get() = block.isTileEntity
  
  /**
   * Gets if this block is a tile provider
   */
  val isTileProvider get() = block.isTileProvider
  
  /**
   * Gets the tile provider of this block, or null if is not a tile provider.
   */
  val tileProvider: TileProvider? get() = block.tileProvider
  
  /**
   * The AABB of this block.
   */
  val box get() = block.box
  
  /**
   * Gets the AABB min data of this block.
   */
  val minBox get() = block.minBox
  val minX get() = block.minX
  val minY get() = block.minY
  val minZ get() = block.minZ
  
  /**
   * Gets the AABB max data of this block.
   */
  val maxBox get() = block.maxBox
  val maxX get() = block.maxX
  val maxY get() = block.maxY
  val maxZ get() = block.maxZ
  
  /**
   * The sounds data for this block.
   *
   * This control the place/break/step sound and their volume/pitch.
   */
  val sound get() = block.sound
  
  /**
   * The gravity of particles on this block.
   */
  val particleGravity get() = block.particleGravity
  
  /**
   * Determines how much velocity is maintained while moving on top of this block.
   */
  val slipperiness get() = block.slipperiness
  
  /**
   * The block data for this block.
   */
  val data: BlockData get() = block.data
  
  /**
   * The default state for this block.
   */
  val defaultState: IState get() = block.state
  
  /**
   * The render for this block.
   */
  val render get() = block.render
  
  /**
   * If this block is a full cube.
   */
  val isFullCube get() = block.isFullCube
  
  /**
   * If this block is a opaque cube.
   *
   * Used to determine ambient occlusion and culling when rebuilding chunks for render.
   */
  val isOpaqueCube get() = block.isOpaqueCube
  
  /**
   * If this block is a normal solid opaque cube.
   */
  val isBlockNormalCube get() = block.isBlockNormalCube
  
  /**
   * Used for nearly all game logic (non-rendering) purposes.
   */
  val isNormalCube get() = block.isNormalCube
  
  /**
   * If this block is visually opaque or not.
   */
  val isVisuallyOpaque get() = block.isVisuallyOpaque
  
  /**
   * If this block can provide power.
   */
  val canProvidePower get() = block.canProvidePower
  
  /**
   * Called when a update is fetched for this block.
   */
  fun onUpdate(level: Level, pos: Pos) {
    block.onUpdate(level, pos, this)
  }
  
  /**
   * Called randomly when level ticks.
   *
   * This is managed by level gamerule "randomTickSpeed".
   */
  fun onRandomTick(level: Level, pos: Pos) {
    block.onRandomTick(level, pos, this)
  }
  
  /**
   * Called randomly when is raining on this block.
   *
   * This is similar to [onRandomTick].
   */
  fun onRaining(level: Level, pos: Pos) {
    block.onRaining(level, pos, this)
  }
  
  /**
   * Called when a neighbor block of this block changes.
   *
   * Can be applied to make physics for this block.
   */
  fun onNeighborChange(level: Level, pos: Pos, neighbor: Block) {
    block.onNeighborChange(level, pos, this, neighbor)
  }
  
  /**
   * Called when player destroys this block.
   */
  fun onMine(level: Level, pos: Pos, player: Player) {
    block.onMine(level, pos, this, player)
  }
  
  /**
   * Called when player harvests this block.
   */
  fun onHarvest(level: Level, pos: Pos, tile: Tile?) {
    block.onHarvest(level, pos, this, tile)
  }
  
  /**
   * Called when this block is added to level.
   *
   * This is called whenever a block in a level changes to this block.
   */
  fun onAdded(level: Level, pos: Pos, chunk: Chunk = level.chunkAt(pos)) {
    block.onAdded(level, pos, chunk, this)
  }
  
  /**
   * Called when this block is removed to level.
   *
   * This is called whenever a other block overrides this block in a level.
   */
  fun onRemoved(level: Level, pos: Pos, chunk: Chunk = level.chunkAt(pos)) {
    block.onRemoved(level, pos, chunk, this)
  }
  
  /**
   * Called when this block explodes.
   *
   * This is called whenever this block is removed by due a explosion.
   */
  fun onExploded(level: Level, pos: Pos, explosion: Explosion) {
    block.onExploded(level, pos, this, explosion)
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
  fun onInteract(level: Level, pos: Pos, player: Player, side: Direction, hit: Pos, click: Click) {
    block.onInteract(level, pos, this, player, side, hit, click)
  }
  
  /**
   * Called when entity collides with this block.
   */
  fun onCollide(level: Level, pos: Pos, entity: Entity) {
    block.onCollide(level, pos, this, entity)
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
    return block.onPlaced(level, pos, player, meta, side, hit)
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
    block.onPostPlaced(level, pos, player, item, side, hit)
  }
  
  /**
    * Called when block event is called.
    */
   fun onEventReceived(level: Level, pos: Pos, id: Int, value: Int): Boolean {
     return block.onEventReceived(level, pos, this, id, value)
   }
   
   /**
    * Modifies the acceleration of an entity at a specific position in the game world.
    *
    * This function allows customization of an entity's motion based on its interaction with a block.
    * The modification to the acceleration is done here.
    *
    * @param level The game level in which the entity exists.
    * @param pos The position of the block that interacts with the entity.
    * @param entity The entity whose acceleration is being modified.
    * @param motion The current motion (velocity) of the entity.
    * @return The modified motion (velocity) of the entity after considering the interaction.
    */
   fun modifyAcceleration(level: Level, pos: Pos, entity: Entity, motion: Vec): Vec {
     return block.modifyAcceleration(level, pos, entity, motion)
   }
   
   /**
    * Called when an entity falls on the block at the specified position.
    *
    * This function triggers the `onFall` method of the entity, allowing it to respond to the fall event.
    *
    * @param level The game level in which the block exists.
    * @param pos The position of the block.
    * @param entity The entity that fell on the block.
    * @param distance The distance the entity fell.
    */
   fun onFall(level: Level, pos: Pos, entity: Entity, distance: Float) {
     block.onFall(level, pos, entity, distance)
   }
   
   /**
    * Called when an entity has landed on the block at the specified position.
    *
    * This function is used to handle the event when an entity lands on the block surface.
    *
    * This function must always chance `motionY` of entity.
    *
    * @param level The game level in which the block exists.
    * @param entity The entity that landed on the block.
    */
   fun onLanded(level: Level, entity: Entity) {
     block.onLanded(level, entity)
   }
  
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
    return block.createData()
  }
  
  /**
   * Gets the pallete of this block.
   */
  fun getPallete(): MapPallete {
    return block.getPallete(this)
  }
  
  /**
   * Convert the given metadata into a state for this Block.
   *
   * This is a deserialization function for blocks.
   */
  fun getState(meta: Int = id): IState {
    return block.getStateFromMeta(meta)
  }
  
  /**
   * Convert the state into the correct metadata value
   *
   * This is a serialization function for blocks.
   */
  fun getMetadata(): Int {
    return block.getMetaFromState(this)
  }
  
  /**
   * Get the actual Block state of this Block at the given position.
   *
   * This applies properties not visible in the metadata, such as fence connections.
   */
  fun getActualState(level: Level, pos: Pos): IState {
    return block.getActualState(this, level, pos)
  }
  
  /**
   * Gets if entities can passes by this block.
   */
  fun isPassable(level: Level, pos: Pos): Boolean {
    return block.isPassable(level, pos)
  }
  
  /**
   * Whether this Block can be replaced directly by other blocks.
   */
  fun isReplaceable(level: Level, pos: Pos): Boolean {
    return block.isReplaceable(level, pos)
  }
  
  /**
   * Checks if an item can replace a block at the specified position and side.
   *
   * @param level The level in which the block exists.
   * @param pos The position of the block.
   * @param side The side at which the replacement is being attempted.
   * @param item The item stack that might be used for replacement.
   * @return `true` if the item can replace the block, `false` otherwise.
   */
  fun canReplace(level: Level, pos: Pos, side: Direction, item: ItemStack): Boolean {
    return block.canReplace(level, pos, side, item)
  }
  
  /**
   * Checks if a block can be placed at the specified position and side.
   *
   * @param level The level in which the block might be placed.
   * @param pos The position at which the placement is being attempted.
   * @param side The side at which the placement is being attempted.
   * @return `true` if the block can be placed at the specified position and side, `false` otherwise.
   */
  fun canPlaceAtSide(level: Level, pos: Pos, side: Direction): Boolean {
    return block.canPlaceAtSide(level, pos, side)
  }
  
  /**
   * Checks if a block can be placed at the specified position.
   *
   * @param level The level in which the block might be placed.
   * @param pos The position at which the placement is being attempted.
   * @return `true` if the block can be placed at the specified position, `false` otherwise.
   */
  fun canPlaceAt(level: Level, pos: Pos): Boolean {
    return block.canPlaceAt(level, pos)
  }
  
  /**
   * Gets a specific block hardness in [level]/[pos].
   */
  fun getHardness(level: Level, pos: Pos): Float {
    return block.getHardness(level, pos)
  }
  
  /**
   * Gets a relative block hardness in [level]/[pos] with [player].
   */
  fun getHardness(player: Player, level: Level, pos: Pos): Float {
    return block.getHardness(player, level, pos)
  }
  
  /**
   * Gets a mixed/combined light brightness for this block.
   */
  fun getMixedLight(level: Level, pos: Pos): Int {
    return block.getMixedLight(level, pos)
  }
  
  /**
   * Determinates if the [side] of this block should be rendered.
   */
  fun shouldSideBeRendered(level: Level, pos: Pos, side: Direction): Boolean {
    return block.shouldSideBeRendered(level, pos, side)
  }
  
  /**
   * Whether this Block is solid on the given Side
   */
  fun isSolid(level: Level, pos: Pos, side: Direction): Boolean {
    return block.isSolid(level, pos, side)
  }
  
  /**
   * Sets the AABB within a specific state.
   */
  fun setAABBWithState(level: Level, pos: Pos) {
    block.setAABBWithState(level, pos)
  }
  
  /**
   * Gets a selected AABB within [pos].
   */
  fun getSelectedAABB(level: Level, pos: Pos): AABB {
    return block.getSelectedAABB(level, pos)
  }
  
  /**
   * Gets a collision AABB within [pos].
   */
  fun getCollisionAABB(level: Level, pos: Pos): AABB {
    return block.getCollisionAABB(level, pos, this)
  }
  
  /**
   * Add all collision boxes of this Block to the list that intersect with the given mask.
   */
  fun addAABBTo(level: Level, pos: Pos, mask: AABB, list: MutableList<AABB>, colliding: Entity) {
    return block.addAABBTo(level, pos, this, mask, list, colliding)
  }
  
  /**
   * Checks wheter if this block is collidable or not.
   */
  fun isCollidable(): Boolean {
    return block.isCollidable()
  }
  
  /**
   * Determinates if this block can collide or not.
   */
  fun canCollide(hitIfLiquid: Boolean): Boolean {
    return block.canCollide(this, hitIfLiquid)
  }
  
  /**
   * Ray traces through the blocks collision from start pos to end pos returning a ray trace hit.
   */
  fun rayTrace(level: Level, pos: Pos, start: Pos, end: Pos): Hit {
    return block.rayTrace(level, pos, start, end)
  }
  
  /**
   * Checks if pos collides with this block.
   */
  fun isInside(pos: Pos): Boolean {
    return block.isInside(pos)
  }
  
  /**
   * Checks if coordinates collides with this block.
   */
  fun isInside(x: Double, y: Double, z: Double): Boolean {
    return block.isInside(x, y, z)
  }
  
  /**
   * Checks if entity collides with this block.
   */
  fun isInside(entity: Entity): Boolean {
    return block.isInside(entity)
  }
  
  /**
   * Gets the explosion resistance of this block.
   */
  fun getExplosionResistance(entity: Entity): Float {
    return block.getExplosionResistance(entity)
  }
  /**
   * Checks whether the block has a comparator input override.
   *
   * @return `true` if the block has a comparator input override, `false` otherwise.
   */
  fun hasComparatorInputOverride(): Boolean {
    return block.hasComparatorInputOverride()
  }
  
  /**
   * Retrieves the comparator input override value for the block at the specified position.
   *
   * This function provides the redstone comparator input override value for the block.
   *
   * @param level The game level in which the block exists.
   * @param pos The position of the block.
   * @return The redstone comparator input override value.
   */
  fun getComparatorInputOverride(level: Level, pos: Pos): Int {
    return block.getComparatorInputOverride(level, pos)
  }
  
  /**
   * Retrieves the weak redstone power level of the block from a specific side.
   *
   * @param level The game level in which the block exists.
   * @param pos The position of the block.
   * @param side The side from which the redstone power is being queried.
   * @return The weak redstone power level of the block.
   */
  fun getWeakPower(level: Level, pos: Pos, side: Direction): Int {
    return block.getWeakPower(level, pos, this, side)
  }
  
  /**
   * Retrieves the strong redstone power level of the block from a specific side.
   *
   * @param level The game level in which the block exists.
   * @param pos The position of the block.
   * @param side The side from which the redstone power is being queried.
   * @return The strong redstone power level of the block.
   */
  fun getStrongPower(level: Level, pos: Pos, side: Direction): Int {
    return block.getStrongPower(level, pos, this, side)
  }
  
  /**
   * Get the ItemStack that this block drops.
   *
   * @param fortune the fortune level, 0 if not have fortune.
   */
  fun getDrop(level: Level, pos: Pos, fortune: Int = 0): ItemStack {
    return block.getDrop(level, pos, this, fortune)
  }
}
