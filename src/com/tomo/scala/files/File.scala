package com.tomo.scala.files

import com.tomo.scala.filesystem.FileSystemException

/**
  * Created by Tomohiro on 03 juillet 2019.
  */

class File(override val parentPath: String, override val name: String, val contents: String)
  extends DirEntry(parentPath, name) {

  override def asDirectory: Directory = throw new FileSystemException("A file cannot be converted to a directory!")

  override def asFile: File = this

  override def isDirectory: Boolean = false

  override def isFile: Boolean = true

  override def getType: String = "File"

  def setContent(newContents: String) : File = new File(parentPath, name, newContents)

  def appendContent(newContents: String) : File = setContent(contents + "\n" + newContents)
}

object File {

  def empty(parentPath: String, name: String) : File = new File(parentPath, name, "")
}