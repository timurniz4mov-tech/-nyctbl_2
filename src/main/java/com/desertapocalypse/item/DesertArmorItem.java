package com.desertapocalypse.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/** Броня странника: каждая часть замедляет трату жажды на 12% (весь комплект - 48% и защита от бури). */
public class DesertArmorItem extends ArmorItem {
    public DesertArmorItem(Type type, Properties properties) {
        super(DesertArmorMaterial.INSTANCE, type, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.desertapocalypse.desert_armor.desc").withStyle(ChatFormatting.GOLD));
    }
}
