package com.desertapocalypse.thirst;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ThirstSyncPacket {
    private final float thirst;
    private final int day;
    private final float edge;
    private final boolean storm;

    public ThirstSyncPacket(float thirst, int day, float edge, boolean storm) {
        this.thirst = thirst;
        this.day = day;
        this.edge = edge;
        this.storm = storm;
    }

    public static void encode(ThirstSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeFloat(msg.thirst);
        buf.writeVarInt(msg.day);
        buf.writeFloat(msg.edge);
        buf.writeBoolean(msg.storm);
    }

    public static ThirstSyncPacket decode(FriendlyByteBuf buf) {
        return new ThirstSyncPacket(buf.readFloat(), buf.readVarInt(), buf.readFloat(), buf.readBoolean());
    }

    public static void handle(ThirstSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientData.thirst = msg.thirst;
            ClientData.day = msg.day;
            ClientData.edge = msg.edge;
            ClientData.storm = msg.storm;
        });
        ctx.get().setPacketHandled(true);
    }
}
