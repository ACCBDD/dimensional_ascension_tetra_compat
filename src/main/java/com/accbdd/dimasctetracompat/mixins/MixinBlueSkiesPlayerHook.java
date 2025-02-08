package com.accbdd.dimasctetracompat.mixins;

import com.accbdd.dimasctetracompat.DimensionalAscension;
import com.legacy.blue_skies.asm_hooks.PlayerHooks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;

@Mixin(PlayerHooks.class)
public class MixinBlueSkiesPlayerHook {
    /**
     * @author ACCBDD
     */
    @Inject(method = "isNerfableTool", at = @At("HEAD"), cancellable = true, remap = false)
    private static void isNerfableTool(ItemStack stack, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        Item item = stack.getItem();
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(item);
        if (registryName != null && "tetra".equals(registryName.getNamespace())) {
            if (stack.getItem() instanceof ModularItem modularItem) {
                if (modularItem.getEffectLevel(stack, ItemEffect.get("dimasctetracompat:fairweather")) > 0) {
                    cir.setReturnValue(false);
                } else {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
