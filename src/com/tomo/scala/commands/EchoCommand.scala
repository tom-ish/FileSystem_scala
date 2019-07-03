package com.tomo.scala.commands
import com.tomo.scala.files.{Directory, File}
import com.tomo.scala.filesystem.State

import scala.annotation.tailrec

/**
  * Created by Tomohiro on 03 juillet 2019.
  */

class EchoCommand(args: Array[String]) extends Command {

  val > = ">"
  val >> = ">>"

  override def apply(state: State): State = {
    /*
       if no args return same state
       else if just one arg, print to console
       else if multiple arg
          operator = next to last argument
          if operator == ">"
            echo to a file
          else if operator == ">>"
            append to a file
          else
            echo everything to console
     */

    if(args.isEmpty)
      state
    else if(args.length == 1)
      state.setMessage(args(0))
    else {
      val operator = args(args.length-2)
      val filename = args(args.length-1)
      val content = createContent(args, args.length-2)

      if(>.equals(operator))
        doEcho(content, filename, state, append = false)
      else if(>>.equals(operator))
        doEcho(content, filename, state, append = true)
      else
        state.setMessage(createContent(args, args.length))
    }
  }

  def getRootAfterEcho(currentDirectory : Directory, path : List[String], contents : String, append : Boolean) : Directory = {
    if(path.isEmpty) currentDirectory
    else if(path.tail.isEmpty) {
      val dirEntry = currentDirectory.findEntry(path.head)
      if(dirEntry == null)
        currentDirectory.addEntry(new File(currentDirectory.path, path.head, contents))
      else if(dirEntry.isDirectory)
        currentDirectory
      else {
        if(append)
          currentDirectory.replaceEntry(path.head, dirEntry.asFile.appendContent(contents))
        else
          currentDirectory.replaceEntry(path.head, dirEntry.asFile.setContent(contents))
      }
    }
    else {
      val nextDirectory = currentDirectory.findEntry(path.head).asDirectory
      val newNextDirectory = getRootAfterEcho(nextDirectory, path.tail, contents, append)

      if(newNextDirectory == nextDirectory)
        currentDirectory
      else
        currentDirectory.replaceEntry(path.head, newNextDirectory)
    }
  }

  def doEcho(contents: String, filename: String, state: State, append: Boolean) : State = {
    if(filename.contains(Directory.SEPARATOR))
      state.setMessage("Echo: filename must not contain separator")
    else {
      val newRoot : Directory = getRootAfterEcho(state.root, state.workingDirectory.getAllFoldersInPath :+ filename, contents, append)
      if(newRoot == state.root)
        state.setMessage(filename + ": no such file")
      else
        State(newRoot, newRoot.findDescendant(state.workingDirectory.getAllFoldersInPath))
    }
  }

  // topIndex NON-INCLUSIVE
  def createContent(args: Array[String], topIndex: Int) : String = {

    @tailrec
    def createOutput(currentIndex: Int, output: String) : String = {
      if(currentIndex >= topIndex)
        output
      else
        createOutput(currentIndex+1, output + " " + args(currentIndex))
    }
    createOutput(0, "")
  }
}
