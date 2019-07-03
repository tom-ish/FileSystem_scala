package com.tomo.scala.filesystem

import java.util.Scanner

import com.tomo.scala.commands.Command
import com.tomo.scala.files.Directory

/**
  * Created by Tomohiro on 02 juillet 2019.
  */

object FileSystem extends App {

  val root = Directory.ROOT

/*
  var state = State(root, root)
  val scanner = new Scanner(System.in)

  while(true) {
    state.show
    val input = scanner.nextLine()
    state = Command.from(input).apply(state)
  }

 */

  io.Source.stdin.getLines().foldLeft(State(root, root))((currentState, newline) => {
    currentState.show
    Command.from(newline).apply(currentState)
  })
}
