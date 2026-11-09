package com.desertapocalypse.event;

import com.desertapocalypse.DesertApocalypseMod;
import com.desertapocalypse.ModItems;
import com.desertapocalypse.entity.AbstractDesertMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DesertApocalypseMod.MOD_ID)
public final class CombatEvents {

    /** Клинок Солнца наносит +50% урона тварям пустыни. */
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof AbstractDesertMob)) {
            return;
        }
        if (event.getSource().getEntity() instanceof LivingEntity attacker
                && attacker.getMainHandItem().is(ModItems.SUN_BLADE.get())) {
            event.setAmount(event.getAmount() * 1.5F);
        }
    }

    private CombatEvents() {}
}
