package com.accbdd.dimasctetracompat.mixins;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.legacy.blue_skies.asm_hooks.PlayerHooks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;

@Mixin(PlayerHooks.class)
public class MixinBlueSkiesPlayerHook {

    private static final Set<Item> NERFED_ITEMS_CACHE = new HashSet<>();
    private static final String[] NERFABLE_ITEM_NAMES = new String[]{"axe", "pickaxe", "shovel", "hoe", "hammer", "mattock", "pickadze", "excavator", "kama", "scythe", "sword", "dagger", "cleaver", "rapier", "saber", "scabbard", "scimitar", "shortsword", "greatsword", "katana", "spear"};

    /**
     * @author ACCBDD
     * @reason uncancellable event, sorry devs!
     */
    @Overwrite(remap = false)
    public static synchronized boolean isNerfableTool(@Nullable ItemStack stack, @Nullable BlockState state) {
        if (stack == null) {
           return false;
        } else if (NERFED_ITEMS_CACHE.size() > 100) {
            NERFED_ITEMS_CACHE.clear();
        }
  
        Item item = stack.getItem();
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(item);
        if (registryName != null && "blue_skies".equals(registryName.getNamespace())) {
            return false;
        } else if (registryName != null && "tetra".equals(registryName.getNamespace())) {
            if (stack.getItem() instanceof ModularItem modularItem) {
                if (modularItem.getEffectLevel(stack, ItemEffect.get("dimasctetracompat:horizoned")) > 0) {
                    return false;
                }
            }
        } else if (registryName == null) {
            return true;
        } else {
            if (!NERFED_ITEMS_CACHE.contains(item)) {
                tryAddToNerfedCache(item, registryName);
            }

            if (!NERFED_ITEMS_CACHE.contains(item)) {
                return false;
            } else {
                return state == null || stack.isCorrectToolForDrops(state);
            }
        }
        return false;
    }

    private static void tryAddToNerfedCache(Item item, ResourceLocation registryName) {
        if (item instanceof TieredItem) {
            TieredItem tiered = (TieredItem)item;
            Tier tier = tiered.getTier();
            if (tier != Tiers.WOOD && tier != Tiers.STONE && tier != Tiers.GOLD) {
                NERFED_ITEMS_CACHE.add(item);
            }
        } else {
            String name = registryName.getPath();
            String[] var3 = NERFABLE_ITEM_NAMES;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String nerfedName = var3[var5];
                if (name.equals(nerfedName) || name.endsWith(nerfedName)) {
                    NERFED_ITEMS_CACHE.add(item);
                    return;
                }
            }
        }
   }
}
