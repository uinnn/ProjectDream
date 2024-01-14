# ProjectDream

Custom Minecraft Server non-based NMS implementation. Is a full rewritten of mojang code for Minecraft 1.8.9 in Kotlin.

This is under development by a single person as a pure hobby. No agreements or licenses are required in the project. Feel free to do whatever you want with the currently present code. It is not affiliated with Mojang. The project has been in development for almost 2 years, despite being on hold for months depending on my enthusiasm to create it.


### Example 1
#### The following code snippet collects all slots of the container that are food, and adds their nutrition value to the player nutrition without removing the food.

```kt
val container = player.openContainer
val foods: List<Food> = container.filterNotNull { it.item.food }
val nutrition = foods.sumOf { it.nutrition }
player.nutrition += nutrition
```

### Example 2
#### The following code snippet swaps the most stacked item in the container with the player held item.

```kt
val slot = container.maxBy { it.item.amount }
if (slot.canTake(player)) {
   val held = player.heldItem.copy()
   player.heldItem = slot.item
   slot.item = held
}
```

### Example 3
#### The following code snippet performs a simulation of interaction with the first spawner found in the player chunk.

```kt
val chunk = player.chunk
val tiles = chunk.tiles.values
val spawner = tiles.findIs<TileSpawner>()
spawner?.onInteract(player, Click.LEFT)
```

### Example 4
#### The following code snippet transforms each non-air block of a chunk into a single block.

```kt
val states = chunk.generateStatesPosSequence(containsAir = false)
for ((pos, state) in states) {
   chunk.setState(pos, Blocks.STONE.state)
}
```

### Example 5
#### The following code snippet makes all blocks in a radius of 3x3x3 AIR around the player, repeating for 100 times with a delay of 1 tick.

```kt
player.repeated(100) {
   if (player.hasMoved) {
      val pos = player.pos
      val min = pos.subtract(1, 1, 1)
      val max = pos.offset(1, 1, 1)
      min.allInBox(max).forEach { block ->
         level.setAir(block)
      }
   }
   delay(50)
}
```

### Example 6
#### The following code snippet gets all tools and armors slots from a container.

```kt
val items = container.items
val armors = items.filter { it.isArmor }
val totalArmorDefense = armors.sumOf { it.armor!!.defense }
val tools = items.filter { it.isTool }
val swords = items.filter { it.item is ItemSword }
```

### Example 7
#### Create a custom item implementation to only drop if no dropped items is on around.

```kt
val item = object : Item() {
   override fun canDrop(level: Level, item: ItemStack, x: Double, y: Double, z: Double): Boolean {
      val aroundEntities = level.getEntitiesAround(Pos(x, y, z), 3.0, 3.0, 3.0)
      return !aroundEntities.any { it is EntityItem }
   }
}

val stack = stackOf(item)
player.drop(stack, trace = true)
```
