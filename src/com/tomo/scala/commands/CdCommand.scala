package com.tomo.scala.commands
import com.tomo.scala.files.{DirEntry, Directory}
import com.tomo.scala.filesystem.State

import scala.annotation.tailrec

/**
  * Created by Tomohiro on 03 juillet 2019.
  */

class CdCommand(directory: String) extends Command {

  /*
      cd /something/somethingElse/... => absolute path
      cd a/b/c => relative to the current working directory

      cd ./.. not implemented yet
   */

  override def apply(state: State): State = {
    // 1. find root
    val root = state.root
    val workingDirectory = state.workingDirectory

    // 2. find the absolute path of the directory I want to cd to
    val absolutePath =
      if(directory.startsWith(Directory.SEPARATOR)) directory
      else if(workingDirectory.isRoot) workingDirectory.path + directory
      else workingDirectory.path + Directory.SEPARATOR + directory

    // 3. find the directory to cd to, given the path
    val destinationDirectory = doFindEntry(root, absolutePath)

    // 4. change the state to the new directory
    // check if the destinationDirectory is a file or a directory
    if(destinationDirectory == null || !destinationDirectory.isDirectory) {
      state.setMessage(directory + ": no such directory")
    }
    else
      State(root, destinationDirectory.asDirectory)
  }

  def doFindEntry(root: Directory, path: String) : DirEntry = {

    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]) : DirEntry = {
      if(path.isEmpty || path.head.isEmpty) currentDirectory
      else if(path.tail.isEmpty) currentDirectory.findEntry(path.head)
      else {
        val nextDirectory = currentDirectory.findEntry(path.head)
        if(nextDirectory == null || !nextDirectory.isDirectory)
          null
        else
          findEntryHelper(nextDirectory.asDirectory, path.tail)
      }
    }

    @tailrec
    def collapseRelativeTokens(path : List[String], result: List[String]) : List[String] = {
      if(path.isEmpty)
        result
      else if(".".equals(path.head))
        collapseRelativeTokens(path.tail, result)
      else if("..".equals(path.head))
        if(result.isEmpty) null
        else collapseRelativeTokens(path.tail, result.init)
      else
        collapseRelativeTokens(path.tail, result :+ path.head)
    }

    // 1. tokens needed
    val tokens : List[String] = path.substring(1).split(Directory.SEPARATOR).toList

    // 1'. eliminate/collapse relative tokens
    val newTokens = collapseRelativeTokens(tokens, List())

    // 2. navigate to the correct entry
    if(newTokens == null) null
    else findEntryHelper(root, newTokens)

  }


}
