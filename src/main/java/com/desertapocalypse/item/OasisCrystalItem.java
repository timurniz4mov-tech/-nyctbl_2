package com.desertapocalypse.item;

import com.desertapocalypse.world.ApocalypseData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/** Кристалл оазиса: ПКМ - пустыня растёт на 65% медленнее ещё 10 минут (суммируется до 40 минут). */
public class OasisCrystalItem extends Item {
    public OasisCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.success(stack);
        }
        ApocalypseData data = ApocalypseData.get(serverLevel);
        if (!data.started) {
            player.displayClientMessage(Component.translatable("message.desertapocalypse.compass.none"), true);
            return InteractionResultHolder.fail(stack);
        }
        data.slowTicks = Math.min(data.slowTicks + 12000, 48000);
        data.setDirty();
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.2F);
        player.displayClientMessage(Component.translatable("message.desertapocalypse.oasis",
                String.valueOf(data.slowTicks / 1200)).withStyle(ChatFormatting.AQUA), false);
        return InteractionResultHolder.sidedSuccess(stack, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.desertapocalypse.oasis_crystal.desc").withStyle(ChatFormatting.AQUA));
    }
}
