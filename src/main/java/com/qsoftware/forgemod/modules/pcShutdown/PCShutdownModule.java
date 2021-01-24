package com.qsoftware.forgemod.modules.pcShutdown;

import com.qsoftware.forgemod.QForgeMod;
import com.qsoftware.forgemod.client.gui.modules.ModuleCompatibility;
import com.qsoftware.forgemod.common.Module;
import com.qsoftware.forgemod.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class PCShutdownModule extends Module {
    @Override
    public void onEnable() {
        if (QForgeMod.isClientSide()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @Override
    public void onDisable() {
        if (QForgeMod.isClientSide()) {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    @Override
    public boolean canDisable() {
        return true;
    }

    @Override
    public @NotNull String getName() {
        return "pc_shutdown";
    }

    @Override
    public @NotNull ModuleCompatibility getCompatibility() {
        if (QForgeMod.isClientSide()) {
            return ModuleCompatibility.FULL;
        } else if (QForgeMod.isServerSide()) {
            return ModuleCompatibility.PARTIAL;
        } else {
            return ModuleCompatibility.NONE;
        }
    }

    @Override
    public boolean isDefaultEnabled() {
        return false;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        long win = mc.getMainWindow().getHandle();

        boolean k1 = InputMappings.isKeyDown(win, GLFW.GLFW_KEY_LEFT_CONTROL);
        boolean k2 = InputMappings.isKeyDown(win, GLFW.GLFW_KEY_RIGHT_CONTROL);
        boolean k3 = InputMappings.isKeyDown(win, GLFW.GLFW_KEY_LEFT_ALT);
        boolean k4 = InputMappings.isKeyDown(win, GLFW.GLFW_KEY_RIGHT_ALT);
        boolean k5 = InputMappings.isKeyDown(win, GLFW.GLFW_KEY_INSERT);

        if ((k1 || k2) && (k3 || k4) && k5 && !(mc.currentScreen instanceof ConfirmShutdownScreen)) {
            mc.displayGuiScreen(new ConfirmShutdownScreen(mc.currentScreen));
        }
    }
}