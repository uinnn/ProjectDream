package dream.entity.base

import dream.entity.EntityOwnable
import dream.entity.player.Player

class EntityTameable : Animal(), EntityOwnable {

  override var ownerId: String
    get() = watcher.string(17)
    set(value) {
      watcher.update(17, value)
    }

  override val owner: Player?
    get() = level.players.find(::isOwner)

  override fun isOwner(entity: Entity): Boolean {
    return entity.id.toString() == ownerId
  }

}
