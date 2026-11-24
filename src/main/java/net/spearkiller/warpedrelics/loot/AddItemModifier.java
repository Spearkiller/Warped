package net.spearkiller.warpedrelics.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class AddItemModifier extends LootModifier {

    public static final MapCodec<AddItemModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst).and(
                    ItemStack.CODEC.fieldOf("item").forGetter(e -> e.item)).apply(inst, AddItemModifier::new));
    private final ItemStack item;


    public AddItemModifier(LootItemCondition[] conditionsIn, ItemStack i) {
        super(conditionsIn);
        this.item = i;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {
        for(LootItemCondition cond: this.conditions){
            if (!cond.test(lootContext)) return generatedLoot;
        }

        generatedLoot.add(this.item.copy());

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
