package net.poe.entitylootdrops;

import net.minecraft.world.level.storage.loot.LootPool;

public interface LootTablePools2 {
    LootPool[] getPools();

    void setPools(LootPool[] pools);
}
