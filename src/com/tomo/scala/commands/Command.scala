package com.tomo.scala.commands

import com.tomo.scala.filesystem.State

/**
  * Created by Tomohiro on 02 juillet 2019.
  */

trait Command {
  def apply(state: State) : State
}

object Command {
  val MKDIR = "mkdir"
  val LS = "ls"
  val PWD = "pwd"
  val TOUCH = "touch"
  val CD = "cd"
  val RM = "rm"
  val ECHO = "echo"
  val CAT = "cat"

  def emptyCommand : Command = new Command {
    override def apply(state: State): State = state
  }

  def incompleteCommand(name: String) : Command = new Command {
    override def apply(state: State): State = state.setMessage(name + ": incomplete command!")
  }

  def from(input: String) : Command = {
    val tokens : Array[String] = input.split(" ")

    if(input.isEmpty || tokens.isEmpty)
      emptyCommand
    else {
      tokens(0) match {
        case MKDIR =>
          if (tokens.length < 2) incompleteCommand(MKDIR)
          else new MkdirCommand(tokens(1))
        case LS => new LsCommand
        case PWD => new PwdCommand
        case TOUCH =>
          if (tokens.length < 2) incompleteCommand(TOUCH)
          else new TouchCommand(tokens(1))
        case CD =>
          if (tokens.length < 2) incompleteCommand(CD)
          else new CdCommand(tokens(1))
        case RM =>
          if (tokens.length < 2) incompleteCommand(RM)
          else new RmCommand(tokens(1))
        case ECHO =>
          if (tokens.length < 2) incompleteCommand(ECHO)
          else new EchoCommand(tokens.tail)
        case CAT =>
          if (tokens.length < 2) incompleteCommand(CAT)
          else new CatCommand(tokens(1))
        case _ => new UnknownCommand
      }
    }
  }
}