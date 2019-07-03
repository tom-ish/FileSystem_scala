package com.tomo.scala.commands

import com.tomo.scala.files.{DirEntry, Directory}
import com.tomo.scala.filesystem.State

/**
  * Created by Tomohiro on 03 juillet 2019.
  */

abstract class CreateEntry(entryName: String) extends Command {

  override def apply(state: State): State = {
    val workingDirectory = state.workingDirectory
    if(workingDirectory.hasEntry(entryName))
      state.setMessage("Entry " + entryName + " already exists!")
    else if(entryName.contains(Directory.SEPARATOR))
    // mkdir something/somethingElse forbidden
      state.setMessage(entryName + " must not contain separators!")
    else if(checkIllegal(entryName))
      state.setMessage(entryName + ": illegal entry name!")
    else
      doCreateEntry(entryName, state)
  }

  def checkIllegal(name: String) : Boolean = {
    name.contains(".")
  }

  def doCreateEntry(name: String, state: State) : State = {

    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry) : Directory = {
      if(path.isEmpty) currentDirectory.addEntry(newEntry)
      else {
        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }
    }

    val workingDirectory = state.workingDirectory

    // 1. all the directories in the full path
    val allDirsInPath = workingDirectory.getAllFoldersInPath

    // 2. create new directory entry in the working directory
    // TODO implement this
    val newEntry = createSpecificEntry(state)

    // 3. update the whole directory structure starting from the root
    //  (the directory structure is IMMUTABLE)
    val newRoot = updateStructure(state.root, allDirsInPath, newEntry)

    // 4. find new working directory instance given workingDirectory's full path in the NEW directory structure
    val newWorkingDirectory = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWorkingDirectory)
  }

  def createSpecificEntry(state: State) : DirEntry
}
