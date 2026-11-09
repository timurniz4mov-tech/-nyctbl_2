package com.desertapocalypse.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/** Клинок Солнца: +50% урона по тварям пустыни (см. CombatEvents). */
public class SunBladeItem extends SwordItem {
    public SunBladeItem(Properties properties) {
        super(SunBladeTier.INSTANCE, 3, -2.4F, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.desertapocalypse.sun_blade.desc").withStyle(ChatFormatting.GOLD));
    }
}
