package com.tomo.scala.commands

import com.tomo.scala.files.{DirEntry, Directory}
import com.tomo.scala.filesystem.State

/**
  * Created by Tomohiro on 03 juillet 2019.
  */

class MkdirCommand(name: String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry =
    Directory.empty(state.workingDirectory.path, name)

}
