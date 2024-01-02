package com.accbdd.dimasctetracompat.loot;

import com.accbdd.dimasctetracompat.DimensionalAscension;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;

public class HasScorching implements LootItemCondition {
    @Override
	public LootItemConditionType getType() {
		return DimensionalAscension.HAS_BLOCK_ENTITY.get();
	}

	@Override
	public boolean test(LootContext context) {
        if (!context.hasParam(LootContextParams.TOOL)) {
            return false;
        };

        ItemStack tool = context.getParam(LootContextParams.TOOL);
        if (tool.isEmpty() || !(tool.getItem() instanceof ModularItem))
			return false;
		return ((ModularItem) tool.getItem()).getEffectLevel(tool, ItemEffect.get("dimasctetracompat:scorching")) > 0;
	}

	public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<HasBlockEntity> {
		@Override
		public void serialize(JsonObject json, HasBlockEntity condition, JsonSerializationContext context) {
			// No extra data required
		}

		@Override
		public HasBlockEntity deserialize(JsonObject json, JsonDeserializationContext context) {
			return new HasBlockEntity();
		}
	}
}