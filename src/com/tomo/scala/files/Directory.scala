package com.tomo.scala.files

import com.tomo.scala.filesystem.FileSystemException

import scala.annotation.tailrec

/**
  * Created by Tomohiro on 02 juillet 2019.
  */

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry])
  extends DirEntry(parentPath, name) {

  def hasEntry(name: String) : Boolean = findEntry(name) != null

  // /a/b/c/d => List["a", "b", "c", "d"]
  def getAllFoldersInPath : List[String] = {
    path.substring(1).split(Directory.SEPARATOR).toList.filter(x => !x.isEmpty)
  }

  def findDescendant(path: List[String]) : Directory = {
    if(path.isEmpty) this
    else findEntry(path.head).asDirectory.findDescendant(path.tail)
  }

  def findDescendant(relativePath: String): Directory = {
    if(relativePath.isEmpty) this
    else findDescendant(relativePath.split(Directory.SEPARATOR).toList)
  }

  def addEntry(newEntry: DirEntry) : Directory =
    new Directory(parentPath, name, contents :+ newEntry)

  def removeEntry(entryName: String) : Directory = {
    if(!hasEntry(entryName)) this
    else
      new Directory(parentPath, name, contents.filter(x => !x.name.equals(entryName)))
  }

  def findEntry(entryName: String) : DirEntry = {
    @tailrec
    def findEntryHelper(name: String, contentList: List[DirEntry]) : DirEntry =
      if(contentList.isEmpty) null
      else if(contentList.head.name.equals(name)) contentList.head
      else findEntryHelper(name, contentList.tail)

    findEntryHelper(entryName, contents)
  }

  def replaceEntry(entryName: String, newEntry: DirEntry) : Directory =
    new Directory(parentPath, name, contents.filter(e => !e.name.equals(entryName)) :+ newEntry)

  def isRoot: Boolean = parentPath.isEmpty

  override def asDirectory: Directory = this
  override def asFile: File = throw new FileSystemException("A directory cannot be converted to a file!")

  override def isDirectory: Boolean = true
  override def isFile: Boolean = false

  override def getType: String = "Directory"

}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT: Directory = Directory.empty("", "")
  def empty(parentPath: String, name: String) : Directory = new Directory(parentPath, name, List())
}