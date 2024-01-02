package com.accbdd.dimasctetracompat.loot.modifier;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.items.ItemHandlerHelper;

public class ScorchingLootModifier extends LootModifier {
    public ScorchingLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    public static final Codec<ScorchingLootModifier> CODEC = RecordCodecBuilder.create(instance ->
		codecStart(instance).apply(instance, ScorchingLootModifier::new)
	);

    @Nonnull
	@Override
	public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		return new ObjectArrayList<>(generatedLoot.stream()
				.map(stack -> trySmelt(stack, context.getLevel()))
				.toList()
		);
	}

    @Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}

    public static ItemStack trySmelt(ItemStack stack, Level world) {
		return world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), world)
			.map(SmeltingRecipe::getResultItem)
			.filter(itemStack -> !itemStack.isEmpty())
			.map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
			.orElse(stack);
	}
}
