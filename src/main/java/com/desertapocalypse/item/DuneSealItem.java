package com.desertapocalypse.item;

import com.desertapocalypse.ApocalypseConfig;
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

/** Печать дюн: ПКМ - граница пустыни отступает на 15 блоков. */
public class DuneSealItem extends Item {
    public static final double PUSH_BACK = 15.0D;

    public DuneSealItem(Properties properties) {
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
        double min = ApocalypseConfig.START_RADIUS.get();
        data.radius = Math.max(min, data.radius - PUSH_BACK);
        data.setDirty();
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 1.0F, 1.0F);
        player.displayClientMessage(Component.translatable("message.desertapocalypse.seal",
                String.valueOf((int) PUSH_BACK)).withStyle(ChatFormatting.GOLD), false);
        return InteractionResultHolder.sidedSuccess(stack, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.desertapocalypse.dune_seal.desc").withStyle(ChatFormatting.GOLD));
    }
}
