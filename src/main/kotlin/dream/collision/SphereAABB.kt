package dream.collision

import dream.pos.*

typealias SphereRegion = SphereAABB

/**
 * Represents an Axis-Aligned Bounding Box (AABB) in the shape of a sphere.
 */
class SphereAABB(override val center: Pos, radius: Double) : AABB(
  center.subtract(radius, radius, radius),
  center.offset(radius, radius, radius)
) {
  
  /**
   * The radius of this sphere AABB.
   */
  var radius = radius
    set(value) {
      resize(radius)
      field = value
    }
  
  /**
   * Resizes the sphere AABB to the specified radius.
   *
   * @param radius The new radius of the sphere AABB.
   */
  fun resize(radius: Double) {
    min.move(center.subtract(radius, radius, radius))
    max.move(center.offset(radius, radius, radius))
  }
}
