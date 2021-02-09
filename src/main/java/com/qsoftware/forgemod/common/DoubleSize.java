package com.qsoftware.forgemod.common;

import com.qsoftware.forgemod.common.interfaces.Formattable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.text.TextFormatting;

@AllArgsConstructor
public class DoubleSize implements Formattable {
    public double width;
    public double height;

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toFormattedString() {
        return TextFormatting.GOLD.toString() + this.width + TextFormatting.GRAY + " x " + TextFormatting.GOLD + this.height;
    }
}
