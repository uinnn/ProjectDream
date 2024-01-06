package dream.block

class BlockAir : Block() {

  override val render: Render
    get() = Render.NONE

  override val isOpaqueCube: Boolean
    get() = false
  
}
