package com.paragon.impl.event.network

import com.paragon.bus.event.CancellableEvent
import net.minecraft.client.multiplayer.ServerData

open class ServerEvent(private val serverData: ServerData) : CancellableEvent() {
    fun getServerData(): ServerData {
        return serverData
    }

    class Connect(val state: State, serverData: ServerData) : ServerEvent(serverData)
    enum class State {
        /**
         * Called before the connection attempt
         */
        PRE,

        /**
         * Indicates that the connection attempt was successful
         */
        CONNECT,

        /**
         * Indicates that an exception occurred when trying to connect to the target server.
         * This will be followed by an instance of [ServerEvent.Disconnect] being posted.
         */
        FAILED
    }

    class Disconnect(forced: Boolean, serverData: ServerData) :
        ServerEvent(serverData) {
        /**
         * @return Whether or not the connection was forcefully closed
         */
        /**
         * Whether or not the connection was forcefully closed. True if the
         * server called for the client to be disconnected. False if the
         * client manually disconnected through [GuiIngameMenu].
         */
        val isForced: Boolean = forced

    }

}