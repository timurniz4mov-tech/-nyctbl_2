package com.desertapocalypse.client;

import com.desertapocalypse.thirst.ClientData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/** Полоска жажды (капли) над голодом и строка информации сверху экрана. */
public final class HudOverlay {
    private static final String[] DROP = {
            "   #   ",
            "   #   ",
            "  ###  ",
            "  ###  ",
            " ##### ",
            "#######",
            "#######",
            "#######",
            " ##### "
    };
    private static final int COLOR_FULL = 0xFF3FA9F5;
    private static final int COLOR_EMPTY = 0xFF26323F;

    public static final IGuiOverlay THIRST = (gui, graphics, partialTick, width, height) -> {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.player == null) {
            return;
        }
        if (mc.player.isCreative() || mc.player.isSpectator()) {
            return;
        }
        float thirst = ClientData.thirst;
        if (thirst < 0.0F) {
            return;
        }
        int top = height - gui.rightHeight;
        gui.rightHeight += 10;

        for (int i = 0; i < 10; i++) {
            int left = width / 2 + 91 - i * 8 - 9 + 1;
            drawDrop(graphics, left, top, COLOR_EMPTY, 6);
            if (thirst >= (i + 1) * 2) {
                drawDrop(graphics, left, top, COLOR_FULL, 6);
            } else if (thirst >= i * 2 + 1) {
                drawDrop(graphics, left, top, COLOR_FULL, 3);
            }
        }
    };

    public static final IGuiOverlay INFO = (gui, graphics, partialTick, width, height) -> {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.player == null) {
            return;
        }
        int day = ClientData.day;
        if (day <= 0) {
            return;
        }
        float edge = ClientData.edge;
        Component text;
        int color;
        if (edge <= 0.0F) {
            text = Component.translatable("hud.desertapocalypse.inside", String.valueOf(day));
            color = 0xFFFF9A3C;
        } else {
            text = Component.translatable("hud.desertapocalypse.outside",
                    String.valueOf((int) edge), String.valueOf(day));
            color = 0xFFFFFFFF;
        }
        graphics.drawCenteredString(mc.font, text, width / 2, 2, color);
        if (ClientData.storm) {
            graphics.drawCenteredString(mc.font, Component.translatable("hud.desertapocalypse.storm"),
                    width / 2, 12, 0xFFFF5544);
        }
    };

    private static void drawDrop(GuiGraphics graphics, int left, int top, int color, int maxColumn) {
        for (int row = 0; row < DROP.length; row++) {
            String line = DROP[row];
            int start = line.indexOf('#');
            int end = Math.min(line.lastIndexOf('#'), maxColumn);
            if (start < 0 || end < start) {
                continue;
            }
            graphics.fill(left + start, top + row, left + end + 1, top + row + 1, color);
        }
    }

    private HudOverlay() {}
}
