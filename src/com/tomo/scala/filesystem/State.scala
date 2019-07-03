package com.tomo.scala.filesystem

import com.tomo.scala.files.Directory

/**
  * Created by Tomohiro on 02 juillet 2019.
  */

class State(val root: Directory, val workingDirectory: Directory, val output: String) {

  def show : Unit = {
    println(output)
    print(State.SHELL_TOKEN)
  }


  def setMessage(message: String) : State = State(root, workingDirectory, message)

}

object State {
  val SHELL_TOKEN = "$ "

  def apply(root: Directory, workingDirectory: Directory, output: String = "") : State =
    new State(root, workingDirectory, output)
}