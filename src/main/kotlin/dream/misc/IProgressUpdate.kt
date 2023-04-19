package dream.misc

public interface IProgressUpdate {
   
   /**
    * Shows the 'Saving level' string.
    */
   fun displaySavingString(message: String)
   
   /**
    * this string, followed by "working..." and then the "% complete" are the 3 lines shown. This resets progress to 0,
    * and the WorkingString to "working...".
    */
   fun resetProgressAndMessage(message: String)
   
   /**
    * Displays a string on the loading screen supposed to indicate what is being done currently.
    */
   fun displayLoadingString(message: String)
   
   /**
    * Updates the progress bar on the loading screen to the specified amount. Args: loadProgress
    */
   fun setLoadingProgress(progress: Int)
   
   fun setDoneWorking()
}
