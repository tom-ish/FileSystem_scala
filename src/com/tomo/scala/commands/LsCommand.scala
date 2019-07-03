package com.tomo.scala.commands

import com.tomo.scala.files.DirEntry
import com.tomo.scala.filesystem.State

/**
  * Created by Tomohiro on 03 juillet 2019.
  */

class LsCommand extends Command {
  override def apply(state: State): State = {
    val contents = state.workingDirectory.contents
    val output = createOutputFromContents(contents)
    state.setMessage(output)
  }

  def createOutputFromContents(contents: List[DirEntry]) : String = {
    if(contents.isEmpty) ""
    else {
      val entry = contents.head
      entry.name + " [" + entry.getType + "]\n" + createOutputFromContents(contents.tail)
    }
  }
}
