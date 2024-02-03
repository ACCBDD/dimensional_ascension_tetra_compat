package com.accbdd.dimasctetracompat.mixins;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.accbdd.dimasctetracompat.DimensionalAscensionConfig;
import com.legacy.blue_skies.asm_hooks.PlayerHooks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

@Mixin(PlayerHooks.class)
public class MixinBlueSkiesPlayerHook {

    private static final Set<Item> NERFED_ITEMS_CACHE = new HashSet<>();
    private static final String[] NERFABLE_ITEM_NAMES = new String[]{"axe", "pickaxe", "shovel", "hoe", "hammer", "mattock", "pickadze", "excavator", "kama", "scythe", "sword", "dagger", "cleaver", "rapier", "saber", "scabbard", "scimitar", "shortsword", "greatsword", "katana", "spear"};

    private static final List<? extends String> BLUE_SKIES_MATERIALS = DimensionalAscensionConfig.BLUE_SKIES_MATERIALS.get();
    private static final Set<String> BLUE_SKIES_MATERIALS_SET = new HashSet<>(BLUE_SKIES_MATERIALS);

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
            CompoundTag nbt = (CompoundTag) stack.serializeNBT().get("tag");
            switch (registryName.toString()) {
                case "tetra:modular_double":
                    String right_head = nbt.getString("double/head_right");                    
                    String right_material = nbt.getString(right_head + "_material");
                    if (BLUE_SKIES_MATERIALS_SET.contains(right_material.substring(right_material.lastIndexOf("/")+1))) {
                        return false;
                    }

                    String left_head = nbt.getString("double/head_left");
                    String left_material = nbt.getString(left_head + "_material");
                    if (BLUE_SKIES_MATERIALS_SET.contains(left_material.substring(left_material.lastIndexOf("/")+1))) {
                        return false;
                    }
                    return true;
                case "tetra:modular_single":
                    String head = nbt.getString("single/head");
                    String material = nbt.getString(head + "_material");
                    if (BLUE_SKIES_MATERIALS_SET.contains(material.substring(material.lastIndexOf("/")+1))) {
                        return false;
                    }
                    return true;
                case "tetra:modular_sword":
                    String blade = nbt.getString("sword/blade");
                    String blade_material = nbt.getString(blade + "_material");
                    if (BLUE_SKIES_MATERIALS_SET.contains(blade_material.substring(blade_material.lastIndexOf("/")+1))) {
                        return false;
                    }
                    return true;
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
