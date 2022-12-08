package com.paragon.impl.command.impl

import com.paragon.Paragon
import com.paragon.impl.command.Command
import com.paragon.impl.command.syntax.ArgumentData
import com.paragon.impl.command.syntax.SyntaxBuilder
import net.minecraft.util.text.TextFormatting

/**
 * @author Surge
 */
object FriendCommand : Command("Friend", SyntaxBuilder.createBuilder(arrayListOf(
    ArgumentData("action", arrayOf("add", "remove", "list")),
    ArgumentData("name", arrayOf("any_str"), visibleWhen = arrayOf(
        Pair("action", "add"),
        Pair("action", "remove")
    ))
))) {

    override fun whenCalled(args: Array<String>, fromConsole: Boolean) {
        if (args.size == 1 && args[0].equals("list", ignoreCase = true)) {
            // List all players
            if (Paragon.INSTANCE.friendManager.names.isEmpty()) {
                sendMessage("${TextFormatting.RED}You haven't added anyone to your social list!")

                return
            }

            for (player in Paragon.INSTANCE.friendManager.names) {
                Paragon.INSTANCE.commandManager.sendClientMessage(player)
            }
        }
        else if (args.size == 2 && args[0].equals("add", ignoreCase = true)) {
            // Add a player
            runCatching {
                val name = args[1]

                Paragon.INSTANCE.friendManager.addName(name)

                sendMessage("${TextFormatting.GREEN}Added player " + name + " to your socials list!")

                // Save social
                Paragon.INSTANCE.storageManager.saveSocial()
            }.onFailure {
                sendMessage("${TextFormatting.RED}Invalid argument! Should be 'friend', 'neutral', or 'enemy'")
            }
        }
        else if (args.size == 2 && args[0].equals("remove", ignoreCase = true)) {
            // Remove a player
            val name = args[1]
            Paragon.INSTANCE.friendManager.removePlayer(name)
            sendMessage("${TextFormatting.GREEN}Removed player $name from your socials list!")

            // Save socials
            Paragon.INSTANCE.storageManager.saveSocial()
        }
        else {
            // Say that we have given an invalid syntax
            sendMessage("${TextFormatting.RED}Invalid Syntax!")
        }
    }

}