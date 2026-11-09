package com.desertapocalypse.entity;

import com.desertapocalypse.ModItems;
import com.desertapocalypse.thirst.Thirst;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Общая основа враждебных существ пустыни: бьют по жажде и роняют песчаные монеты. */
public abstract class AbstractDesertMob extends Monster {
    private final float thirstDrain;
    private final int minCoins;
    private final int maxCoins;

    protected AbstractDesertMob(EntityType<? extends AbstractDesertMob> type, Level level,
                                float thirstDrain, int minCoins, int maxCoins) {
        super(type, level);
        this.thirstDrain = thirstDrain;
        this.minCoins = minCoins;
        this.maxCoins = maxCoins;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.1D, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof ServerPlayer player) {
            Thirst.add(player, -thirstDrain);
        }
        return hit;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        int count = minCoins + this.random.nextInt(maxCoins - minCoins + 1) + looting;
        if (count > 0) {
            this.spawnAtLocation(new ItemStack(ModItems.DESERT_COIN.get(), count));
        }
    }
}
