package net.poe.entitylootdrops.mixin;

import fzzyhmstrs.emi_loot.mixins.BinomialLootNumberProviderAccessor;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.poe.entitylootdrops.SetItemCountFunctionNumberProviderAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({SetItemCountFunction.class})
public abstract class SetItemCountFunctionMixin implements SetItemCountFunctionNumberProviderAccessor {
    @Mutable
    @Final
    @Shadow
    NumberProvider value;

    public NumberProvider getNumberProvider() {
        return value;
    }

    public void setNumberProvider(NumberProvider value) {
        this.value = value;
    }
}
