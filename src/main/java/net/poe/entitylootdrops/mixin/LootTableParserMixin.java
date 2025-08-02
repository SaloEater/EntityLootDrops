package net.poe.entitylootdrops.mixin;

import fzzyhmstrs.emi_loot.mixins.BinomialLootNumberProviderAccessor;
import fzzyhmstrs.emi_loot.mixins.LootPoolAccessor;
import fzzyhmstrs.emi_loot.mixins.LootPoolEntryAccessor;
import fzzyhmstrs.emi_loot.mixins.UniformLootNumberProviderAccessor;
import fzzyhmstrs.emi_loot.parser.LootTableParser;
import fzzyhmstrs.emi_loot.server.MobLootTableSender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;
import net.poe.entitylootdrops.LootTablePools2;
import net.poe.entitylootdrops.SetItemCountFunctionNumberProviderAccessor;
import net.poe.entitylootdrops.lootdrops.LootConfig;
import net.poe.entitylootdrops.lootdrops.model.EntityDropEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;

@Mixin(value = LootTableParser.class, remap = false)
public abstract class LootTableParserMixin {
    @Shadow
    public static String currentTable;

    @Final
    @Shadow
    private static Map<ResourceLocation, MobLootTableSender> mobSenders;

    @Shadow
    private static MobLootTableSender parseMobLootTable(LootTable lootTable, ResourceLocation id, ResourceLocation mobId) {
        return null;
    }

    @Unique
    private static Map<String, Boolean> entitylootdrops$entitiesDone = new HashMap<>();

    @Inject(
        method = "parseLootTables",
        at = @At("TAIL")
    )
    private static void parseLootTables(LootDataManager manager, Map<LootDataId<?>, ?> tables, CallbackInfo ci) {
        ForgeRegistries.ENTITY_TYPES.forEach(type -> {
            if (entitylootdrops$entitiesDone.containsKey(type.getDescriptionId())) {
                return;
            }
            var mobTableId = type.getDefaultLootTable();
            LootTable mobTable = manager.getLootTable(mobTableId);
            if (mobTable == LootTable.EMPTY) {
                ResourceLocation mobId = ForgeRegistries.ENTITY_TYPES.getKey(type);
                int size = addLootPool(mobTable, mobId);
                if (size > 0) {
                    currentTable = mobTableId.toString();
                    mobSenders.put(mobTableId, parseMobLootTable(mobTable, mobTableId, mobId));
                }
            }
        });
    }

    private static int addLootPool(LootTable mobTable, ResourceLocation mobTableId) {
        List<LootPool> lootPools = new ArrayList<>();
        for (EntityDropEntry drop : LootConfig.getNormalDrops()) {
            if (drop.getEntityId().equals(mobTableId.toString()) && drop.hasItem()) {
                addToLootPool(lootPools, drop);
            }
        }

        lootPools.forEach(mobTable::addPool);
        return lootPools.size();
    }

    @Inject(
        method = "parseMobLootTable",
        at = @At("HEAD")
    )
    private static void $parseMobLootTable(LootTable lootTable, ResourceLocation lootTableId, ResourceLocation mobId, CallbackInfoReturnable<MobLootTableSender> cir) {
        entitylootdrops$entitiesDone.put(mobId.toString(), true);
        List<LootPool> lootPools = new ArrayList<>();
        lootPools.addAll(Arrays.asList(((LootTablePools2) lootTable).getPools()));

        // Phase 1: Handle vanilla drop modifications (only check applicable drops)
        boolean shouldCancelVanillaDrops = false;
        for (EntityDropEntry drop : LootConfig.getNormalDrops()) {
            if (drop.getEntityId().equals(mobId.toString())) {
                if (!drop.isAllowDefaultDrops()) {
                    shouldCancelVanillaDrops = true;
                }
            }
        }

        if (shouldCancelVanillaDrops) {
            lootPools.clear();
        }

        // Phase 3: Process extra vanilla drops
        for (EntityDropEntry drop : LootConfig.getNormalDrops()) {
            if (drop.getEntityId().equals(mobId.toString()) && drop.getExtraDropChance() > 0) {
                addExtraDrop(lootPools, drop);
            }
        }

        // Phase 4: Process custom drops
        for (EntityDropEntry drop : LootConfig.getNormalDrops()) {
            if (drop.getEntityId().equals(mobId.toString()) && drop.hasItem()) {
                addToLootPool(lootPools, drop);
            }
        }
        ((LootTablePools2) lootTable).setPools(lootPools.toArray(new LootPool[0]));
    }

    private static void addExtraDrop(List<LootPool> lootPools, EntityDropEntry drop) {
        var addedAmount = Math.max(drop.getExtraAmountMin(), drop.getExtraAmountMax());
        for (LootPool pool : lootPools) {
            var functions = ((LootPoolAccessor) pool).getFunctions();
            for (LootItemFunction function : functions) {
                if (function instanceof SetItemCountFunction setItemCountFunction) {
                    SetItemCountFunctionNumberProviderAccessor accessor = (SetItemCountFunctionNumberProviderAccessor) setItemCountFunction;
                    var numberProvider = accessor.getNumberProvider();
                    accessor.setNumberProvider(addToNumberProvider(numberProvider, addedAmount));
                }
            }
            var entries = ((LootPoolAccessor) pool).getEntries();
            for  (var entry : entries) {
                if (entry instanceof LootItem lootItem) {
                    functions = ((LootPoolSingletonContainerAccessor) lootItem).getFunctions();
                    for (LootItemFunction function : functions) {
                        if (function instanceof SetItemCountFunction setItemCountFunction) {
                            SetItemCountFunctionNumberProviderAccessor accessor = (SetItemCountFunctionNumberProviderAccessor) setItemCountFunction;
                            var numberProvider = accessor.getNumberProvider();
                            accessor.setNumberProvider(addToNumberProvider(numberProvider, addedAmount));
                        }
                    }
                }
            }
        }
    }

    private static NumberProvider addToNumberProvider(NumberProvider numberProvider, int addedAmount) {
        if (numberProvider instanceof UniformGenerator) {
            var min = getGeneratorMin(numberProvider);
            var max = getGeneratorMax(numberProvider) + addedAmount;
            return UniformGenerator.between(min, max);
        } else if (numberProvider instanceof BinomialDistributionGenerator binomialDistributionGenerator) {
            var max = getGeneratorMax(binomialDistributionGenerator) + addedAmount;
            return UniformGenerator.between(0, max);
        } else if (numberProvider instanceof ConstantValue  constantValue) {
            return ConstantValue.exactly(constantValue.getFloat(null) + addedAmount);
        }
        return numberProvider;
    }

    private static float getGeneratorMin(NumberProvider numberProvider) {
        if (numberProvider instanceof UniformGenerator) {
            return getGeneratorMin(((UniformLootNumberProviderAccessor)numberProvider).getMin());
        } else if (numberProvider instanceof BinomialDistributionGenerator) {
            return 0;
        } else if (numberProvider instanceof ConstantValue  constantValue) {
            return constantValue.getFloat(null);
        }
        return 0;
    }

    private static float getGeneratorMax(NumberProvider numberProvider) {
        if (numberProvider instanceof UniformGenerator) {
            return getGeneratorMax(((UniformLootNumberProviderAccessor)numberProvider).getMax());
        } else if (numberProvider instanceof BinomialDistributionGenerator) {
            return getGeneratorMax(((BinomialLootNumberProviderAccessor)numberProvider).getN());
        } else if (numberProvider instanceof ConstantValue  constantValue) {
            return constantValue.getFloat(null);
        }
        return 0;
    }

    private static void addToLootPool(List<LootPool> lootPools, EntityDropEntry drop) {
        var lootItem = lootTableItem(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(drop.getItemId())));
        var builder = lootPool().add(lootItem);
        builder.apply(SetItemCountFunction.setCount(UniformGenerator.between(drop.getMinAmount(), drop.getMaxAmount())));
        if (drop.isRequirePlayerKill()){
            builder.when(LootItemKilledByPlayerCondition.killedByPlayer());
        }
        if (drop.getDropChance() < 100) {
            builder.when(LootItemRandomChanceCondition.randomChance(drop.getDropChance() / 100));
        }
        lootPools.add(builder.build());
    }
}
