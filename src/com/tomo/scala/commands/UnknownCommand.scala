package com.tomo.scala.commands

import com.tomo.scala.filesystem.State

/**
  * Created by Tomohiro on 03 juillet 2019.
  */

class UnknownCommand extends Command {
  override def apply(state: State): State = state.setMessage("Command not found!")
}
