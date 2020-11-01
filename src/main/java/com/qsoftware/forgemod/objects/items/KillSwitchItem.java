package com.qsoftware.forgemod.objects.items;

import com.qsoftware.forgemod.groups.Groups;
import com.qsoftware.forgemod.common.TextColors;
import com.sun.jna.Platform;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class KillSwitchItem extends Item {
    public KillSwitchItem() {
        super(new Item.Properties().group(Groups.OVERPOWERED));

        this.setRegistryName("kill_switch");
    }

    @Override
    public @NotNull ActionResultType onItemUse(@NotNull ItemUseContext context) {
        if (Platform.isWindows()) {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("shutdown /s /t 0");
            } catch (IOException e) {
                e.printStackTrace();
                return ActionResultType.FAIL;
            }

            return ActionResultType.SUCCESS;
        } else if (Platform.isLinux()) {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("shutdown");
            } catch (IOException e) {
                try {
                    runtime.exec("sudo shutdown");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    e.printStackTrace();
                    return ActionResultType.FAIL;
                }
            }
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }

    @Override
    public ITextComponent getHighlightTip(ItemStack item, ITextComponent displayName) {
        if (Platform.isWindows() || Platform.isLinux()) {
            return new StringTextComponent(TextColors.LIGHT_RED + "Warning: " + TextColors.WHITE + TextColors.ITALIC + "This will shutdown you computer!");
        } else {
            return new StringTextComponent(TextColors.AQUA + "Note: " + TextColors.WHITE + TextColors.ITALIC + "This item works only on Windows or Linux computers!");
        }
    }
}