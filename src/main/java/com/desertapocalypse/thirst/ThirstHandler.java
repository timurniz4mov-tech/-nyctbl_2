package com.desertapocalypse.thirst;

import com.desertapocalypse.ApocalypseConfig;
import com.desertapocalypse.DesertApocalypseMod;
import com.desertapocalypse.ModItems;
import com.desertapocalypse.ModNetwork;
import com.desertapocalypse.world.ApocalypseData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DesertApocalypseMod.MOD_ID)
public final class ThirstHandler {

    /** Вызывается каждый тик сервера для каждого игрока. */
    public static void tick(ServerPlayer player, ApocalypseData data) {
        if (ApocalypseConfig.THIRST_ENABLED.get()
                && player.isAlive() && !player.isCreative() && !player.isSpectator()) {
            applyThirst(player, data);
        }
        if (player.tickCount % 10 == 0) {
            sync(player);
        }
    }

    private static void applyThirst(ServerPlayer player, ApocalypseData data) {
        Level level = player.level();
        boolean overworld = level.dimension() == Level.OVERWORLD;
        boolean inDesert = overworld && data.started && data.isInside(player.getX(), player.getZ());

        float thirst = Thirst.get(player);
        float baseDrain = Thirst.MAX / (ApocalypseConfig.THIRST_DRAIN_SECONDS.get() * 20.0F);

        float multiplier = 1.0F;
        if (inDesert) {
            multiplier += 1.0F;
            if (level.isDay() && level.canSeeSky(player.blockPosition())) {
                multiplier += 0.5F;
            }
        }
        if (level.dimension() == Level.NETHER) {
            multiplier += 1.0F;
        }
        if (player.isSprinting()) {
            multiplier += 1.0F;
        }
        if (player.isInWater()) {
            multiplier = 0.0F;
        }
        thirst -= baseDrain * multiplier;

        // пить воду: присесть, стоя в воде
        if (player.isInWater() && player.isShiftKeyDown() && thirst < Thirst.MAX) {
            thirst += 0.15F;
            if (player.tickCount % 8 == 0) {
                level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK,
                        SoundSource.PLAYERS, 0.4F, 1.0F);
            }
        }
        // дождь утоляет жажду
        if (level.isRainingAt(player.blockPosition())) {
            thirst += 0.02F;
        }

        Thirst.set(player, thirst);
        thirst = Thirst.get(player);

        if (player.tickCount % 20 == 0) {
            if (thirst <= 6.0F) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, true, false, true));
            }
            if (thirst <= 3.0F) {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0, true, false, true));
            }
        }
        if (thirst <= 0.0F && player.tickCount % 40 == 0) {
            player.hurt(player.damageSources().generic(), 1.0F);
        }
    }

    public static void sync(ServerPlayer player) {
        ApocalypseData data = ApocalypseData.get(player.serverLevel());
        float thirst = ApocalypseConfig.THIRST_ENABLED.get() ? Thirst.get(player) : -1.0F;
        int day = 0;
        float edge = 0.0F;
        if (data.started && player.level().dimension() == Level.OVERWORLD) {
            day = data.dayNumber();
            edge = (float) data.distanceToEdge(player.getX(), player.getZ());
        }
        ModNetwork.sendToPlayer(player, new ThirstSyncPacket(thirst, day, edge));
    }

    public static void giveStarterKit(ServerPlayer player) {
        if (!ApocalypseConfig.GIVE_STARTER_KIT.get() || Thirst.hasKit(player)) {
            return;
        }
        Thirst.setKit(player, true);
        give(player, new ItemStack(ModItems.DESERT_COMPASS.get()));
        for (int i = 0; i < 2; i++) {
            give(player, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
        }
    }

    private static void give(ServerPlayer player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    /** Напитки и еда утоляют жажду. */
    @SubscribeEvent
    public static void onFinishUsing(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ItemStack stack = event.getItem();
        float add = 0.0F;
        if (stack.is(Items.POTION)) {
            add = PotionUtils.getPotion(stack) == Potions.WATER ? 8.0F : 3.0F;
        } else if (stack.is(Items.MILK_BUCKET)) {
            add = 6.0F;
        } else if (stack.is(Items.MELON_SLICE)) {
            add = 2.0F;
        } else if (stack.is(Items.SWEET_BERRIES) || stack.is(Items.GLOW_BERRIES)) {
            add = 1.0F;
        }
        if (add > 0.0F) {
            Thirst.add(player, add);
            sync(player);
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player fresh = event.getEntity();
        Thirst.setKit(fresh, Thirst.hasKit(original));
        if (event.isWasDeath()) {
            Thirst.set(fresh, Thirst.MAX);
        } else {
            Thirst.set(fresh, Thirst.get(original));
        }
    }

    private ThirstHandler() {}
}
