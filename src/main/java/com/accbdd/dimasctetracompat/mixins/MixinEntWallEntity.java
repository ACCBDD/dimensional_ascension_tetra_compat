package com.accbdd.dimasctetracompat.mixins;

import com.accbdd.dimasctetracompat.DimensionalAscension;
import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntWallEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntWallEntity.class)
public abstract class MixinEntWallEntity extends LivingEntity {
    protected MixinEntWallEntity(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void onHurtCheck(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getEntity() instanceof LivingEntity) {
            ItemStack stack = ((LivingEntity)source.getEntity()).getMainHandItem();
            if(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().equals("tetra:modular_double")) {
                if (stack.canPerformAction(ToolAction.get("axe_dig")))
                    cir.setReturnValue(super.hurt(source, amount));
                DimensionalAscension.LOGGER.info(ToolAction.getActions().toString());
            }
        }
    }
}
