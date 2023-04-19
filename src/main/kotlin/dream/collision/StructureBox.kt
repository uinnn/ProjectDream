package dream.collision

import dream.nbt.*
import dream.nbt.types.*
import dream.pos.*

/**
 * Represents a structure AABB.
 */
class StructureBox : AABB, Storable<IntArrayTag> {
   
   constructor() : super()
   
   /**
    * Creates a AABB by the given values.
    */
   constructor(min: Pos, max: Pos) : super(min, max)
   
   /**
    * Creates a AABB by the given values.
    */
   constructor(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double)
      : super(minX, minY, minZ, maxX, maxY, maxZ)
   
   /**
    * Creates a AABB by the given values.
    */
   constructor(minX: Number, minY: Number, minZ: Number, maxX: Number, maxY: Number, maxZ: Number)
      : super(minX, minY, minZ, maxX, maxY, maxZ)
   
   override fun save(tag: IntArrayTag) {
      tag.add(blockMinX)
      tag.add(blockMinY)
      tag.add(blockMinZ)
      tag.add(blockMaxX)
      tag.add(blockMaxY)
      tag.add(blockMaxZ)
   }
   
   override fun load(tag: IntArrayTag) {
      blockMinX = tag.getInt(0)
      blockMinY = tag.getInt(1)
      blockMinZ = tag.getInt(2)
      blockMaxX = tag.getInt(3)
      blockMaxY = tag.getInt(4)
      blockMaxZ = tag.getInt(5)
   }
   
   override fun toTag(): IntArrayTag {
      val tag = IntArrayTag()
      save(tag)
      return tag
   }
   
}
