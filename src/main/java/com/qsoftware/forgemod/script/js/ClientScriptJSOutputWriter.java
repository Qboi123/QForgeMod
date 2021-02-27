package com.qsoftware.forgemod.script.js;

import com.qsoftware.forgemod.network.Network;
import com.qsoftware.forgemod.network.ScriptResponsePacket;
import com.qsoftware.forgemod.script.js.ui.ScriptJSClientGui;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.IOException;
import java.io.Writer;

public class ClientScriptJSOutputWriter extends Writer {
    private char[] cache = new char[65536];
    private final ClientPlayerEntity player;
    private boolean isErr;

    public ClientScriptJSOutputWriter(ClientPlayerEntity player, boolean isErr) {
        this.player = player;
        this.isErr = isErr;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (len >= 0) System.arraycopy(cbuf, 0, cache, off, len - 1);
    }

    @Override
    public void flush() {
        ScriptJSClientGui.getInstance().printScriptMessage(new StringTextComponent((isErr ? TextFormatting.RED.toString() : "") + new String(cache).replaceAll("\u0000", "")));
        cache = new char[]{};
    }

    @Override
    public void close() throws IOException {
        throw new IOException("Closing script output is not allowed.");
    }
}
