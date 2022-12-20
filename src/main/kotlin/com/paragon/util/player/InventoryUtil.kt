package com.paragon.util.player

import com.paragon.util.Wrapper
import com.paragon.util.mc
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemSword
import net.minecraft.network.play.client.CPacketHeldItemChange
import net.minecraft.util.EnumHand

object InventoryUtil : Wrapper {

    fun isHolding(item: Item): Boolean {
        return minecraft.player.heldItemMainhand.item == item || minecraft.player.heldItemOffhand.item == item
    }

    /**
     * Checks whether the player is holding the given [Item] in the given [EnumHand].
     *
     * @return the result of the check.
     */
    fun isHolding(item: Item, hand: EnumHand) = minecraft.player.getHeldItem(hand).item == item

    /**
     * @return The hand holding the given [Item], null if the player isn't holding it.
     */
    fun getHandHolding(item: Item) = when {
        minecraft.player.heldItemMainhand.item === item -> EnumHand.MAIN_HAND
        minecraft.player.heldItemOffhand.item === item -> EnumHand.OFF_HAND
        else -> null
    }

    /**
     * @return the inventory slot the given [Item] is in.
     */
    @JvmStatic
    fun getItemSlot(itemIn: Item) = (9..35).firstOrNull { mc.player.inventory.getStackInSlot(it).item == itemIn } ?: -1

    /**
     * @return the hotbar slot of the given [Item].
     */
    @JvmStatic
    fun getItemInHotbar(itemIn: Item) = (0..8).firstOrNull { mc.player.inventory.getStackInSlot(it).item == itemIn } ?: -1

    /**
     * Switches to an item in the player's hotbar
     *
     * @param itemIn The item to switch to
     * @param packet Switch silently - use packets instead
     * @return Whether the switch was successful
     */
    fun switchToItem(itemIn: Item, packet: Boolean): Boolean {
        if (isHolding(itemIn) || getItemInHotbar(itemIn) == -1) {
            return false
        }

        if (packet) {
            minecraft.connection!!.sendPacket(CPacketHeldItemChange(getItemInHotbar(itemIn)))
        } else {
            minecraft.player.inventory.currentItem = getItemInHotbar(itemIn)
        }

        return true
    }

    @JvmStatic
    fun switchToSlot(slot: Int, packet: Boolean) {
        if (slot == minecraft.player.inventory.currentItem) {
            return
        }

        minecraft.player.connection.sendPacket(CPacketHeldItemChange(slot))

        if (!packet) {
            minecraft.player.inventory.currentItem = slot
        }
    }

    fun getCountOfItem(item: Item, hotbarOnly: Boolean, ignoreHotbar: Boolean): Int {
        var count = 0
        for (i in (if (ignoreHotbar) 9 else 0) until if (hotbarOnly) 9 else 36) {
            val stack = minecraft.player.inventory.getStackInSlot(i)
            if (stack.item === item) {
                count += stack.count
            }
        }
        return count
    }

    val isHoldingSword: Boolean
        get() = minecraft.player.heldItemMainhand.item is ItemSword

    @JvmStatic
    fun getHotbarBlockSlot(block: Block): Int {
        var slot = -1
        for (i in 0..8) {
            val item = minecraft.player.inventory.getStackInSlot(i).item
            if (item is ItemBlock && item.block == block) {
                slot = i
                break
            }
        }
        return slot
    }

}