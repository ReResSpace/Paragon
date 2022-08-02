package com.paragon.client.systems.module.impl.render

import com.paragon.api.event.render.entity.HurtcamEvent
import com.paragon.api.event.render.entity.RenderEatingEvent
import com.paragon.api.module.Category
import com.paragon.api.module.Module
import com.paragon.api.setting.Setting
import me.wolfsurge.cerauno.listener.Listener
import net.minecraft.entity.passive.EntityBat
import net.minecraft.init.SoundEvents
import net.minecraftforge.client.event.RenderBlockOverlayEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object NoRender : Module("NoRender", Category.RENDER, "Cancels rendering certain things") {

    private val fire = Setting("Fire", true)
        .setDescription("Cancel rendering the fire overlay")

    private val water = Setting("Water", true)
        .setDescription("Cancel rendering the water overlay")

    private val bossInfo = Setting("BossInfo", true)
        .setDescription("Cancel rendering the boss info overlay")

    private val potions = Setting("PotionIcons", false)
        .setDescription("Cancel rendering the potion icons")

    private val portal = Setting("Portal", true)
        .setDescription("Cancel rendering the portal effect")

    private val bats = Setting("Bats", true)
        .setDescription("Cancel rendering bats")

    private val eatingAnimation = Setting("EatingAnimation", false)
        .setDescription("Stops rendering the eating animation")

    private val hurtcam = Setting("Hurtcam", true)
        .setDescription("Cancel rendering the hurtcam")

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Pre) {
        if (nullCheck()) {
            return
        }

        if (bossInfo.value && event.type == RenderGameOverlayEvent.ElementType.BOSSINFO) {
            event.isCanceled = true
        }

        if (potions.value && event.type == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
            event.isCanceled = true
        }

        if (portal.value && event.type == RenderGameOverlayEvent.ElementType.PORTAL) {
            event.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onRenderLivingEntity(event: RenderLivingEvent.Pre<*>) {
        if (nullCheck()) {
            return
        }
        if (bats.value && event.entity is EntityBat) {
            event.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onPlaySound(event: PlaySoundAtEntityEvent) {
        if (nullCheck()) return
        if (bats.value && event.sound == SoundEvents.ENTITY_BAT_AMBIENT || event.sound == SoundEvents.ENTITY_BAT_DEATH || event.sound == SoundEvents.ENTITY_BAT_HURT || event.sound == SoundEvents.ENTITY_BAT_LOOP || event.sound == SoundEvents.ENTITY_BAT_TAKEOFF) {
            event.volume = 0.0f
            event.pitch = 0.0f
            event.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onBlockOverlay(event: RenderBlockOverlayEvent) {
        if (fire.value && event.overlayType == RenderBlockOverlayEvent.OverlayType.FIRE) {
            event.isCanceled = true
        }
        if (water.value && event.overlayType == RenderBlockOverlayEvent.OverlayType.WATER) {
            event.isCanceled = true
        }
    }

    @Listener
    fun onRenderEating(event: RenderEatingEvent) {
        if (eatingAnimation.value) {
            event.cancel()
        }
    }

    @Listener
    fun onHurtcam(event: HurtcamEvent) {
        if (hurtcam.value) {
            event.cancel()
        }
    }
}