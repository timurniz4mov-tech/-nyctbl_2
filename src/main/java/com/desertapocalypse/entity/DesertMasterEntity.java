package com.desertapocalypse.entity;

import com.desertapocalypse.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.InteractGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;

/**
 * Мастер пустыни: торговец. Меняет золото/алмазы на песчаные монеты и продаёт
 * снаряжение против пустыни и её тварей.
 */
public class DesertMasterEntity extends WanderingTrader {

    public DesertMasterEntity(EntityType<? extends WanderingTrader> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createMasterAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, DesertStalkerEntity.class, 12.0F, 0.6D, 0.6D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, DesertColossusEntity.class, 20.0F, 0.6D, 0.6D));
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.6D));
        this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35D));
        this.goalSelector.addGoal(9, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        // раз в полдня (12000 тиков) товары восстанавливаются
        if (!this.level().isClientSide() && this.tickCount % 12000 == 0 && !this.isTrading()) {
            for (MerchantOffer offer : this.getOffers()) {
                offer.resetUses();
            }
        }
    }

    @Override
    protected void updateTrades() {
        MerchantOffers offers = this.getOffers();

        // --- обмен валюты на песчаные монеты ---
        offers.add(offer(new ItemStack(Items.GOLD_INGOT, 1), coins(4), 24, 2));
        offers.add(offer(new ItemStack(Items.EMERALD, 1), coins(3), 24, 2));
        offers.add(offer(new ItemStack(Items.DIAMOND, 1), coins(12), 8, 5));

        // --- вода и лечение ---
        offers.add(offer(coins(1), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER), 32, 1));
        offers.add(offer(coins(6), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HEALING), 6, 3));

        // --- против враждебных существ ---
        offers.add(offer(coins(14), new ItemStack(ModItems.SUN_BLADE.get()), 2, 10));
        offers.add(offer(coins(10), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH), 4, 5));
        offers.add(offer(coins(12), new ItemStack(Items.SHIELD), 3, 5));

        // --- замедление пустыни ---
        offers.add(offer(coins(18), new ItemStack(ModItems.OASIS_CRYSTAL.get()), 3, 10));
        offers.add(offer(coins(30), new ItemStack(ModItems.DUNE_SEAL.get()), 2, 15));

        // --- броня странника: медленнее тратится жажда ---
        offers.add(offer(coins(8), new ItemStack(ModItems.DESERT_HELMET.get()), 2, 8));
        offers.add(offer(coins(14), new ItemStack(ModItems.DESERT_CHESTPLATE.get()), 2, 8));
        offers.add(offer(coins(12), new ItemStack(ModItems.DESERT_LEGGINGS.get()), 2, 8));
        offers.add(offer(coins(7), new ItemStack(ModItems.DESERT_BOOTS.get()), 2, 8));

        // --- компас, если потерялся ---
        offers.add(offer(coins(6), new ItemStack(ModItems.DESERT_COMPASS.get()), 4, 2));
    }

    private static ItemStack coins(int count) {
        return new ItemStack(ModItems.DESERT_COIN.get(), count);
    }

    private static MerchantOffer offer(ItemStack cost, ItemStack result, int maxUses, int xp) {
        return new MerchantOffer(cost, result, maxUses, xp, 0.05F);
    }
}
