package com.accbdd.mixins;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.legacy.blue_skies.asm_hooks.PlayerHooks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(PlayerHooks.class)
public class MixinBlueSkiesPlayerHook {

    @Inject(method = "isNerfableTool", at = @At("HEAD"), remap=false)
    protected void checkIfBlueSkiesTetraTool(@Nullable ItemStack stack, @Nullable BlockState state, CallbackInfo ci) {
        System.out.print(stack.serializeNBT());
    }
}
