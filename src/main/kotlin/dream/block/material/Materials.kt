package dream.block.material

import dream.item.*

/**
 * Represents a registry for materials
 */
public object Materials {
   
   // transparents
   @JvmField val AIR = MaterialTransparent(MapPallete.AIR)
   @JvmField val FIRE = MaterialTransparent(MapPallete.AIR)
   
   // liquids
   @JvmField val WATER = MaterialLiquid(MapPallete.WATER).unpushable()
   @JvmField val LAVA = MaterialLiquid(MapPallete.TNT).unpushable()
   
   // logics
   @JvmField val PLANTS = MaterialLogic(MapPallete.FOLIAGE).unpushable()
   @JvmField val VINES = MaterialLogic(MapPallete.FOLIAGE).burnable().unpushable()
   @JvmField val CIRCUITS = MaterialLogic(MapPallete.AIR).unpushable()
   @JvmField val CARPET = MaterialLogic(MapPallete.CLOTH).burnable()
   @JvmField val SNOW = MaterialLogic(MapPallete.SNOW).replaceable().translucent().requiresTool().unpushable()
   
   // portals
   @JvmField val PORTAL = MaterialPortal(MapPallete.AIR).immovable()
   
   // simples
   @JvmField val GRASS = Material(MapPallete.GRASS)
   @JvmField val DIRT = Material(MapPallete.DIRT)
   @JvmField val WOOD = Material(MapPallete.WOOD).burnable()
   @JvmField val STONE = Material(MapPallete.STONE).requiresTool()
   @JvmField val IRON = Material(MapPallete.IRON).requiresTool()
   @JvmField val ANVIL = Material(MapPallete.IRON).requiresTool().immovable()
   @JvmField val LEAVES = Material(MapPallete.FOLIAGE).burnable().translucent().unpushable()
   @JvmField val SPONGE = Material(MapPallete.YELLOW)
   @JvmField val CLOTHES = Material(MapPallete.CLOTH).burnable()
   @JvmField val SAND = Material(MapPallete.SAND)
   @JvmField val GLASS = Material(MapPallete.AIR).translucent().adventureExempt()
   @JvmField val REDSTONE_LIGHT = Material(MapPallete.AIR).adventureExempt()
   @JvmField val TNT = Material(MapPallete.TNT).burnable().translucent()
   @JvmField val CORAL = Material(MapPallete.FOLIAGE).unpushable()
   @JvmField val ICE = Material(MapPallete.ICE).unpushable().adventureExempt()
   @JvmField val PACKED_ICE = Material(MapPallete.ICE).adventureExempt()
   @JvmField val CRAFTED_SNOW = Material(MapPallete.SNOW).requiresTool()
   @JvmField val CACTUS = Material(MapPallete.FOLIAGE).translucent().unpushable()
   @JvmField val CLAY = Material(MapPallete.CLAY)
   @JvmField val GOURD = Material(MapPallete.FOLIAGE).unpushable()
   @JvmField val DRAGON_EGG = Material(MapPallete.FOLIAGE).unpushable()
   @JvmField val CAKE = Material(MapPallete.AIR).unpushable()
   @JvmField val PISTON = Material(MapPallete.STONE).immovable()
   @JvmField val BARRIER = Material(MapPallete.AIR).requiresTool().immovable()
   @JvmField val WEB = MaterialWeb(MapPallete.CLOTH).requiresTool().unpushable()
   
}
