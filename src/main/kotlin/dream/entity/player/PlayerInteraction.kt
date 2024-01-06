package dream.entity.player

import dream.api.*
import dream.block.*
import dream.inventory.*
import dream.item.*
import dream.item.tool.*
import dream.level.*
import dream.misc.*
import dream.packet.game.*
import dream.pos.*
import dream.tiles.*

@Open
class PlayerInteraction(var level: Level, var player: Player) : Tickable {

  var isDigging = false
  var digTick = 0
  var lastDigTick = 0
  var diggingPos = Pos.ZERO

  var receivedDigPacket = false
  var initialDig = 0
  var remainingDigDurability = -1
  var destroyedPos = Pos.ZERO

  val diggingState get() = level.getState(diggingPos)
  val destroyedState get() = level.getState(destroyedPos)
  val diggingBlock get() = level.getBlock(diggingPos)
  val destroyedBlock get() = level.getBlock(destroyedPos)

  override fun tick(partial: Int) {
    tickDigging()
  }
  
  fun tickDigging() {
    digTick++

    when {
      receivedDigPacket -> onReceivedDigPacket()
      isDigging -> onDigging()
    }
  }
  
  fun onReceivedDigPacket() {
    val block = destroyedBlock
    if (block.isAir) {
      receivedDigPacket = false
    } else {
      val amplifier = digTick - initialDig
      val hardness = block.getHardness(player, level, destroyedPos) * (amplifier + 1)
      val durability = (hardness * 10f).toInt()

      if (durability != remainingDigDurability) {
        level.sendDigProgress(player, destroyedPos, durability)
        remainingDigDurability = durability
      }

      if (hardness >= 1f) {
        receivedDigPacket = false
        tryHarvest(destroyedPos)
      }
    }
  }
  
  fun onDigging() {
    val block = diggingBlock
    if (block.isAir) {
      cancelDigging()
      remainingDigDurability = -1
    } else {
      val amplifier = digTick - initialDig
      val hardness = block.getHardness(player, level, destroyedPos) * (amplifier + 1)
      val durability = (hardness * 10f).toInt()
      if (durability != remainingDigDurability) {
        level.sendDigProgress(player, destroyedPos, durability)
        remainingDigDurability = durability
      }
    }
  }
  
  fun onFinishedDigging(pos: Pos) {
    if (pos == diggingPos) {
      val block = diggingBlock
      if (!block.isAir) {
        val amplifier = digTick - initialDig
        val hardness = block.getHardness(player, level, pos) * (amplifier + 1)
        
        when {
          hardness >= 0.7 -> {
            isDigging = false
            level.sendDigProgress(player, pos, -1)
            tryHarvest(pos)
          }
          !receivedDigPacket -> {
            isDigging = false
            receivedDigPacket = true
            destroyedPos = pos
            initialDig = lastDigTick
          }
        }
      }
    }
  }
  
  fun cancelDigging() {
    isDigging = false
    level.sendDigProgress(player, diggingPos, -1)
  }
  
  fun tryHarvest(pos: Pos): Boolean {
    return when {
      player.isSpectator -> false
      player.isCreative && player.heldItem.item is ItemSword -> false
      else -> {
        val state = level.getState(pos)
        
        if (player.isAdventure && !player.allowEdit) {
          val held = player.heldItem.orNull() ?: return false
          if (!held.canHarvest(state.block)) {
            return false
          }
        }
        
        // cache the old tile if removed.
        val tile = level.getTile(pos)
        
        // TODO: 18/07/2023
        // play the effects for breaking the block.
        //
        // this.theWorld.playAuxSFXAtEntity(this.thisPlayerMP, 2001, pos, Block.getStateId(iblockstate))
        val removed = removeBlock(pos)
        if (player.isCreative) {
          player.sendPacket(SPacketBlockChange(level, pos))
        } else {
          val held = player.heldItem
          val canHarvest = player.canHarvest(state.block)
          if (!held.isAir) {
            held.mine(player, state.block, pos)
            player.clearHeldItemIfEmpty()
          }
          
          if (removed && canHarvest) {
            state.onHarvest(level, pos, tile)
          }
        }
        
        return removed
      }
    }
  }

  fun tryUseItem(item: ItemStack): Boolean {
    if (player.isSpectator)
      return false

    val amount = item.amount
    val metadata = item.metadata
    val computed = item.onRightClick(player)

    // item and computed item is equals
    if (computed === item && computed.amount == amount && computed.useDuration > 0 && computed.metadata == metadata)
      return false

    player.heldItem = computed

    if (player.isCreative) {
      computed.amount = amount
      if (computed.isDamageable) {
        computed.metadata = metadata
      }
    }

    if (computed.amount == 0) {
      player.heldItem = EmptyItemStack
    }

    if (!player.isUsingItem) {
      player.sendContainer(player.container)
    }

    return true
  }
  
  fun activateBlockOrUseItem(item: ItemStack, pos: Pos, side: Direction, hit: Pos) {
    if (player.isSpectator) {
      var tile = level.getTile(pos)
      if (tile is LockableContainer) {
        val block = level.getBlock(pos)
        if (tile is TileChest && block is BlockChest) {
          tile = block.getLockableContainer(level, pos)
        }
        
        
      }
    } else {
      if (!player.isSneaking || player.heldItem.isAir) {
        val state = level.getState(pos)
        if (state.on)
      }
    }
    
    
    return if (this.gameType === WorldSettings.GameType.SPECTATOR) {
      var tileentity: TileEntity = worldIn.getTileEntity(pos)
      if (tileentity is ILockableContainer) {
        val block: Block = worldIn.getBlockState(pos).getBlock()
        if (tileentity is TileEntityChest && block is BlockChest) {
          tileentity = (block as BlockChest).getLockableContainer(worldIn, pos)
        }
        if (tileentity != null) {
          player.displayGUIChest(tileentity)
          return true
        }
      } else if (tileentity is IInventory) {
        player.displayGUIChest(tileentity as IInventory)
        return true
      }
      false
    } else {
      if (!player.isSneaking() || player.getHeldItem() == null) {
        val iblockstate: IBlockState = worldIn.getBlockState(pos)
        if (iblockstate.getBlock().onBlockActivated(worldIn, pos, iblockstate, player, side, offsetX, offsetY, offsetZ)) {
          return true
        }
      }
      if (stack == null) {
        false
      } else if (this.isCreative()) {
        val j: Int = stack.getMetadata()
        val i: Int = stack.stackSize
        val flag: Boolean = stack.onItemUse(player, worldIn, pos, side, offsetX, offsetY, offsetZ)
        stack.setItemDamage(j)
        stack.stackSize = i
        flag
      } else {
        stack.onItemUse(player, worldIn, pos, side, offsetX, offsetY, offsetZ)
      }
    }
  }

  
  fun removeBlock(pos: Pos): Boolean {
    val state = level.getState(pos)
    state.onMine(level, pos, player)

    val removed = level.setAir(pos)
    if (removed) {
      state.onRemoved(level, pos)
    }

    return removed
  }
}
