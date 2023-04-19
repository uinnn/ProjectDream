package dream.app

import dream.block.Blocks
import dream.entity.player.Player
import dream.item.food.Food
import dream.item.tool.ItemSword
import dream.misc.Click
import dream.tiles.TileSpawner
import dream.utils.repeated
import kotlinx.coroutines.delay

fun main() {

   // few examples

   lateinit var player: Player
   val container = player.openContainer

   // EXAMPLE 1
   // Collect all slots of the container that's is food and adds to player nutrition (without removing food)

   val foods: List<Food> = container.filterNotNull { it.item.food }
   val nutrition = foods.sumOf { it.nutrition }
   player.nutrition += nutrition

   // EXAMPLE 2
   // Do a swap between the most stacked item in the container and the player held item

   val slot = container.maxBy { it.item.amount }
   // check if player can take a item at the slot.
   if (slot.canTake(player)) {
      val held = player.heldItem.copy()
      player.heldItem = slot.item
      slot.item = held
   }

   // EXAMPLE 3
   // Do a simulation of interaction of the first spawner found in the player chunk

   val chunk = player.chunk
   val tiles = chunk.tiles.values
   val spawner = tiles.findIs<TileSpawner>()
   spawner?.onInteract(player, Click.LEFT)

   // EXAMPLE 4
   // Transform EACH non-air block of a chunk in a single block

   // generate a lazy sequence with all pos and blockstates of the chunk
   val states = chunk.generateStatesPosSequence(containsAir = false)
   for ((pos, state) in states) {
      chunk.setState(pos, Blocks.STONE.state)
   }

   // EXAMPLE 5
   // Makes all blocks in a radius of 3x3x3 AIR around player repeting for 100 times with a delay of 1 tick

   player.repeated(100) {
      // check if player has moved. If not, no reason to delete blocks
      if (player.hasMoved) {
         val pos = player.pos
         val min = pos.subtract(1, 1, 1)
         val max = pos.offset(1, 1, 1)
         val blocks = min.allInBox(max)
         for (block in blocks) {
            block.setBlockAt(player.level, Blocks.AIR)
         }
      }

      delay(50)
   }

   // EXAMPLE 6
   // Gets all tools/armors slots from a container.

   val items = container.items
   val armors = items.filter { it.isArmor }
   val totalArmorDefense = armors.sumOf { it.armor!!.defense }
   val tools = items.filter { it.isTool }
   val swords = items.filter { it.item is ItemSword }

}




/*
 * Prototype extension functions that can be in the final API.
 */


inline fun <reified R> Iterable<*>.findIs(): R? {
   for (element in this) if (element is R) return element
   return null
}


inline fun <R, T, C : MutableCollection<in R>> Iterable<T>.filterNotNullTo(
      destination: C,
      selector: (T) -> R?
): C {
   for (element in this) {
      val selected = selector(element)
      if (selected != null) {
         destination.add(selected)
      }
   }
   return destination
}


inline fun <R, T> Iterable<T>.filterNotNull(selector: (T) -> R?): List<R> {
   return filterNotNullTo(ArrayList(), selector)
}

inline fun <reified R, T, C : MutableCollection<in R>> Iterable<T>.filterIsInstanceTo(
      destination: C,
      selector: (T) -> Any?
): C {
   for (element in this) {
      val selected = selector(element)
      if (selected is R) {
         destination.add(selected)
      }
   }
   return destination
}


inline fun <reified R, T> Iterable<T>.filterIsInstance(selector: (T) -> Any?): List<R> {
   return filterIsInstanceTo(ArrayList(), selector)
}
