# ProjectDream

Project Dream is a unique Minecraft project developed by Uin. The project started development one year ago, and it is a fully
non-Mojang code Minecraft 1.8.9 version, written entirely in Kotlin.

One of the most impressive things about Project Dream is the fact that it is a complete reimplementation of Minecraft in Kotlin.
This means that the entire codebase has been rewritten from scratch, which is no small feat. 
This approach allows developers to have complete control over the game's mechanics and behavior, 
which enables them to implement unique features and improvements that are not available in the vanilla game.

Another notable aspect of Project Dream is the dedication to using Kotlin as the primary programming language.
Kotlin is a modern programming language that is gaining popularity in the software development community due to its concise syntax,
enhanced type safety, and null safety features.
The decision to use Kotlin in the development of Project Dream showcases the potential for new languages
to be used in game development and highlights the benefits of using modern tools and technologies.

Project Dream is still in development, but the progress made so far is impressive.
The game is shaping up to be a unique and innovative take on Minecraft, with features and gameplay mechanics that set it apart from the vanilla game. 
With its dedication to Kotlin and a commitment to creating a new experience for Minecraft players.

Project Dream is fully extensible, and their design is built to allow developers change everything.

#### Note:
Project Dream is being built with **VANILLA** 1.8.9 features. **A LOT** of code present in the last commit can or can't be present in the final code.
THIS IS NOT STABLE. As a single person developing this, is it for my own fun and **don't** have any financial purposes or Mojang contribuition.

#### All credits go to Mojang.

### Examples

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
      val blocks = min.allInBox(max)
      for (block in blocks) {
         block.setBlockAt(player.level, Blocks.AIR)
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
      return aroundEntities.any { it is EntityItem }
   }
}

val stack = stackOf(item)
player.drop(stack, trace = true)
```
