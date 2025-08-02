package net.poe.entitylootdrops;

import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public interface SetItemCountFunctionNumberProviderAccessor {
    NumberProvider getNumberProvider();
    void setNumberProvider(NumberProvider value);
}
