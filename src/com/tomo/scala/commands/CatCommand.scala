package com.tomo.scala.commands
import com.tomo.scala.filesystem.State

/**
  * Created by Tomohiro on 03 juillet 2019.
  */

class CatCommand(filename : String) extends Command {

  override def apply(state: State): State = {
    val workingDirectory = state.workingDirectory
    val dirEntry = workingDirectory.findEntry(filename)

    if(dirEntry == null || !dirEntry.isFile)
      state.setMessage(filename + ": no such file")
    else
      state.setMessage(dirEntry.asFile.contents)
  }

}
