package com.desertapocalypse.item;

import com.desertapocalypse.world.ApocalypseData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Компас пустыни: ПКМ показывает направление и расстояние до Сердца Пустыни. */
public class DesertCompassItem extends Item {
    private static final String[] ARROWS = {"↑", "↗", "→", "↘", "↓", "↙", "←", "↖"};

    public DesertCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            if (level.dimension() != Level.OVERWORLD) {
                player.displayClientMessage(Component.translatable("message.desertapocalypse.compass.dimension"), true);
            } else {
                ApocalypseData data = ApocalypseData.get(serverLevel);
                if (!data.started) {
                    player.displayClientMessage(Component.translatable("message.desertapocalypse.compass.none"), true);
                } else {
                    double dx = data.center.getX() + 0.5D - player.getX();
                    double dz = data.center.getZ() + 0.5D - player.getZ();
                    double bearing = Math.toDegrees(Math.atan2(-dx, dz));
                    float relative = Mth.wrapDegrees((float) bearing - player.getYRot());
                    int index = Math.floorMod(Math.round(relative / 45.0F), 8);
                    int distance = Mth.floor(Math.sqrt(dx * dx + dz * dz));
                    player.displayClientMessage(Component.translatable("message.desertapocalypse.compass",
                            ARROWS[index], String.valueOf(distance)), true);
                }
            }
            player.getCooldowns().addCooldown(this, 20);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
