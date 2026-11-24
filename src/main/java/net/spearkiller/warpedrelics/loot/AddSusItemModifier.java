package net.spearkiller.warpedrelics.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class AddSusItemModifier extends LootModifier {

    public static final MapCodec<AddSusItemModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst).and(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(e -> e.item)).apply(inst, AddSusItemModifier::new));
    private final Item item;


    public AddSusItemModifier(LootItemCondition[] conditionsIn, Item i) {
        super(conditionsIn);
        this.item = i;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {
        for(LootItemCondition cond: this.conditions){
            if (!cond.test(lootContext)) return generatedLoot;
        }

        if (lootContext.getRandom().nextFloat() < (1/12f)) {
            generatedLoot.clear();
            generatedLoot.add(new ItemStack(this.item));
        }

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}


/*public class AddSusItemModifier extends LootModifier {

    float replaceChance;
    public static final Supplier<Codec<AddSusItemModifier>> CODEC = Suppliers.memoize( () ->
            RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item)).apply(inst, AddSusItemModifier::new)));
    private final Item item;
    public AddSusItemModifier(LootItemCondition[] conditionsIn, Item i) {
        super(conditionsIn);
        this.item = i;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {

        for(LootItemCondition cond: this.conditions){
            if (!cond.test(context)) return generatedLoot;
        }

        if (context.getRandom().nextFloat() < (1/12f)) {
            generatedLoot.clear();
            generatedLoot.add(new ItemStack(this.item));
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}*/
