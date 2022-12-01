package com.paragon.impl.managers

import com.paragon.impl.module.Module
import com.paragon.impl.module.client.*
import com.paragon.impl.module.combat.*
import com.paragon.impl.module.hud.impl.*
import com.paragon.impl.module.hud.impl.graphs.*
import com.paragon.impl.module.misc.*
import com.paragon.impl.module.movement.*
import com.paragon.impl.module.render.*
import net.minecraftforge.common.MinecraftForge
import java.util.function.Predicate

class ModuleManager {

    val modules: ArrayList<Module>

    init {
        MinecraftForge.EVENT_BUS.register(this)

        modules = arrayListOf(
            // Combat
            Aura,
            AutoCrystal,
            BowBomb,
            BowRelease,
            Criticals,
            Offhand,
            Replenish,
            Surround,
            WebAura,

            // Movement
            AntiVoid,
            AutoWalk,
            ElytraFlight,
            EntitySpeed,
            Flight,
            InventoryWalk,
            NoFall,
            NoSlow,
            ReverseStep,
            Sprint,
            Step,
            Strafe,
            Velocity,

            // Render
            AspectRatio,
            BetterScreenshot,
            BlockHighlight,
            Blur,
            Breadcrumbs,
            BreakESP,
            Chams,
            ChinaHat,
            ClearChat,
            EnchantColour,
            ESP,
            Fullbright,
            HandChams,
            HitColour,
            HoleESP,
            LogoutSpots,
            MobOwner,
            Nametags,
            NewChunks,
            NoRender,
            PhaseESP,
            PopChams,
            Shader,
            ShulkerViewer,
            SoundHighlight,
            SourceESP,
            StorageESP,
            SuperheroFX,
            Tracers,
            Trajectories,
            ViewClip,
            ViewModel,
            VoidESP,
            Xray,

            // Misc
            Alert,
            Announcer,
            AntiGhast,
            AntiHunger,
            AutoLog,
            AutoSable,
            AutoTranslate,
            BookBot,
            Blink,
            ChatModifications,
            ChorusControl,
            CustomWorld,
            ExtraTab,
            FakePlayer,
            FastUse,
            Interact,
            Lawnmower,
            MiddleClick,
            NoGlobalSounds,
            NoHandshake,
            NoRotate,
            OnDeath,
            RotationLock,
            TimerModule,
            UnfocusedCPU,
            XCarry,

            // Client
            ClientFont,
            Colours,
            ClickGUI,
            DiscordRPC,
            MainMenu,

            // HUD
            Armour,
            ArrayListHUD,
            CombatInfo,
            Coordinates,
            Crystals,
            CustomText,
            Direction,
            FPS,
            TPS,
            CPS,
            GraphCrystals,
            GraphFPS,
            GraphReceivedPackets,
            GraphSentPackets,
            GraphPing,
            GraphSpeed,
            HUD,
            HUDEditor,
            Inventory,
            Keystrokes,
            LagNotifier,
            Notifications,
            Ping,
            PotionHUD,
            ServerBrand,
            Speed,
            TabGui,
            TargetHUD,
            Totems,
            Watermark
        )

        modules.forEach { it.reflectSettings() }
    }

    fun getModulesThroughPredicate(predicate: Predicate<Module>): List<Module> = modules.filter { predicate.test(it) }

}