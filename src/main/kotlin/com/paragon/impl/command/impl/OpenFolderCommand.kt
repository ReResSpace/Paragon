package com.paragon.impl.command.impl

import com.paragon.Paragon
import com.paragon.impl.command.Command
import com.paragon.impl.command.syntax.SyntaxBuilder
import java.awt.Desktop
import java.io.File

object OpenFolderCommand : Command("OpenFolder", SyntaxBuilder()) {

    override fun whenCalled(args: Array<String>, fromConsole: Boolean) {
        Desktop.getDesktop().open(File("paragon"))
        Paragon.INSTANCE.commandManager.sendClientMessage("Opened Paragon folder", fromConsole)
    }

}