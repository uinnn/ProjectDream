package dream.extension

class ShadowedExtension(val shadow: Extension) : Extension() {
   
   override fun onLoad() = shadow.onLoad()
   override fun onStart() = shadow.onStart()
   override fun onDisable() = shadow.onDisable()
   
}
