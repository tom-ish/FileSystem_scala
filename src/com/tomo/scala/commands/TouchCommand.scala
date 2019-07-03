package com.tomo.scala.commands
import com.tomo.scala.files.{DirEntry, Directory, File}
import com.tomo.scala.filesystem.State

/**
  * Created by Tomohiro on 03 juillet 2019.
  */

class TouchCommand(name: String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry =
    File.empty(state.workingDirectory.path, name)

}
