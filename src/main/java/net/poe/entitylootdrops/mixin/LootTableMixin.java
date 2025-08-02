package net.poe.entitylootdrops.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.poe.entitylootdrops.LootTablePools2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LootTable.class, remap = false)
public class LootTableMixin implements LootTablePools2 {
    @Unique
    LootPool[] entity_loot_drops$pools;

    @Override
    public LootPool[] getPools() {
        return this.entity_loot_drops$pools;
    }

    @Override
    public void setPools(LootPool[] pools) {
        entity_loot_drops$pools = pools;
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void entity_loot_drops$initPools(LootContextParamSet p_287716_, ResourceLocation p_287737_, LootPool[] pools, LootItemFunction[] p_287663_, CallbackInfo ci) {
        this.entity_loot_drops$pools = pools;
    }
}
