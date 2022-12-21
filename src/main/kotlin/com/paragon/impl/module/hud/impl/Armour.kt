package com.paragon.impl.module.hud.impl

import com.paragon.impl.module.client.Colours
import com.paragon.impl.setting.Setting
import com.paragon.impl.module.hud.HUDModule
import com.paragon.util.mc
import com.paragon.util.render.RenderUtil.renderItemStack
import com.paragon.util.render.font.FontUtil
import com.paragon.util.world.BlockUtil.getBlockAtPos
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import org.lwjgl.opengl.GL11.*

object Armour : HUDModule("Armour", "Displays your armour on screen") {

    private val waterOffset = Setting("Water Offset", true, null, null, null).describedBy("Position higher when you are underwater")

    override fun render() {
        val armourList: ArrayList<ItemStack> = ArrayList<ItemStack>(mc.player.inventory.armorInventory)

        armourList.reverse()

        glPushMatrix()
        glTranslatef(0f, (if (waterOffset.value!! && mc.player.position.up().getBlockAtPos() == Blocks.WATER) -10 else 0).toFloat(), 0f)

        var xSpacing = 0f

        for (itemStack in armourList) {
            // We don't want to render stack
            if (itemStack.isEmpty) {
                xSpacing += 18f
                continue
            }

            // Render stack
            renderItemStack(itemStack, x + xSpacing, y + 4, true)

            // Get the item's damage percentage
            val itemDamage = ((1 - itemStack.itemDamage.toFloat() / itemStack.maxDamage.toFloat()) * 100).toInt()

            // Scale
            glScalef(0.75f, 0.75f, 0.75f)
            val scaleFactor = 1 / 0.75f

            // Render the damage percentage
            FontUtil.drawStringWithShadow(itemDamage.toString(), (x + xSpacing + 9 - mc.fontRenderer.getStringWidth(itemDamage.toString()) / 2) * scaleFactor, y * scaleFactor, Colours.mainColour.value, alignment.value)
            glScalef(scaleFactor, scaleFactor, scaleFactor)
            xSpacing += 18f
        }

        glPopMatrix()
    }

    override var width: Float = 18f * 4f

    override var height: Float = 22f

}