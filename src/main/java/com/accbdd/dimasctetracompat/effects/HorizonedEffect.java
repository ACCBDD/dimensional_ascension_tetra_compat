package com.accbdd.dimasctetracompat.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;

import java.util.List;
import java.util.Optional;

public class HorizonedEffect {
    public static final ItemEffect horizoned = ItemEffect.get("dimasctetracompat:horizoned");

    @SubscribeEvent
    public void destroyBlock(BlockEvent.BreakEvent event) {
        LevelAccessor levelAccessor = event.getLevel();
        if (levelAccessor instanceof ServerLevel level) {
            Player player = event.getPlayer();
            BlockState state = event.getState();
            BlockPos pos = event.getPos();
            ItemStack stack = player.getMainHandItem();

            if (stack.getItem() instanceof ModularItem item) {
                int effectLevel = item.getEffectLevel(stack, horizoned);

                boolean notCreative = !player.isCreative(); //disable if player in creative
                boolean correctTool = player.hasCorrectToolForDrops(state);
                boolean autosmeltTool = effectLevel > 0;

                if (notCreative && correctTool && autosmeltTool) {
                    if (event.getExpToDrop() > 0) {
                        state.getBlock().popExperience(level, pos, event.getExpToDrop());
                    }

                    List<ItemStack> drops = Block.getDrops(state, level, pos, level.getBlockEntity(pos), player, player.getMainHandItem());

                    for (ItemStack drop : drops) {
                        Container container = new SimpleContainer(drop);
                        Optional<SmeltingRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, container, level);
                        if (recipe.isPresent()) {
                            ItemStack smeltedStack = recipe.get().getResultItem();
                            smeltedStack.setCount(drop.getCount());
                            Block.popResource(level, pos, smeltedStack);
                        } else {
                            Block.popResource(level, pos, drop);
                        }
                    }
                    level.getLevel().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }
}