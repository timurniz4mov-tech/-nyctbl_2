package com.desertapocalypse;

import com.desertapocalypse.entity.DesertColossusEntity;
import com.desertapocalypse.entity.DesertMasterEntity;
import com.desertapocalypse.entity.DesertStalkerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DesertApocalypseMod.MOD_ID);

    /** Песчаный скиталец: враждебный, ростом с игрока (1.8). */
    public static final RegistryObject<EntityType<DesertStalkerEntity>> DESERT_STALKER =
            ENTITY_TYPES.register("desert_stalker", () -> EntityType.Builder
                    .of(DesertStalkerEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(8)
                    .build(DesertApocalypseMod.MOD_ID + ":desert_stalker"));

    /** Песчаный колосс: враждебный великан ростом 4.2 блока. */
    public static final RegistryObject<EntityType<DesertColossusEntity>> DESERT_COLOSSUS =
            ENTITY_TYPES.register("desert_colossus", () -> EntityType.Builder
                    .of(DesertColossusEntity::new, MobCategory.MONSTER)
                    .sized(1.4F, 4.2F)
                    .clientTrackingRange(10)
                    .build(DesertApocalypseMod.MOD_ID + ":desert_colossus"));

    /** Мастер пустыни: торговец. */
    public static final RegistryObject<EntityType<DesertMasterEntity>> DESERT_MASTER =
            ENTITY_TYPES.register("desert_master", () -> EntityType.Builder
                    .of(DesertMasterEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(10)
                    .build(DesertApocalypseMod.MOD_ID + ":desert_master"));

    private ModEntities() {}
}
