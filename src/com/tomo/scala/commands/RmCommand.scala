package com.tomo.scala.commands
import com.tomo.scala.files.Directory
import com.tomo.scala.filesystem.State

/**
  * Created by Tomohiro on 03 juillet 2019.
  */

class RmCommand(name: String) extends Command {

  override def apply(state: State): State = {
    // 1. get working directory
    val workingDirectory = state.workingDirectory

    // 2. get absolute path
    val absolutePath =
      if(Directory.SEPARATOR.equals(name)) name
      else if(workingDirectory.isRoot) workingDirectory.path + name
      else workingDirectory.path + Directory.SEPARATOR + name

    // 3. do some checks
    if(Directory.ROOT_PATH.equals(absolutePath))
      state.setMessage("Nuclear war not supported yet !!!")
    else
      doRm(absolutePath, state)

  }

  def doRm(path: String, state: State) : State = {

    def rmHelper(currentDirectory: Directory, path : List[String]) : Directory = {
      if(path.isEmpty)
        currentDirectory
      else if(path.tail.isEmpty)
        currentDirectory.removeEntry(path.head)
      else {
        val nextDirectory = currentDirectory.findEntry(path.head)
        if(nextDirectory == null || !nextDirectory.isDirectory)
          currentDirectory
        else {
          val newNextDirectory = rmHelper(nextDirectory.asDirectory, path.tail)
          if(newNextDirectory == nextDirectory)
            currentDirectory
          else
            currentDirectory.replaceEntry(path.head, newNextDirectory)
        }
      }
    }

    // 4. find the entry to remove
    val tokens = path.substring(1).split(Directory.SEPARATOR).toList
    val newRoot: Directory = rmHelper(state.root, tokens)

    // 5. update structure like we do for mkdir
    if(newRoot == state.root)
      state.setMessage(path + ": no such file or directory")
    else
      State(newRoot, newRoot.findDescendant(state.workingDirectory.path.substring(1)))
  }

}
