package com.accbdd.dimasctetracompat.mixins;

import com.legacy.blue_skies.entities.hostile.boss.StarlitCrusherEntity;
import com.legacy.blue_skies.entities.util.base.SkiesBossEntity;
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

import com.accbdd.dimasctetracompat.DimensionalAscension;

@Mixin(StarlitCrusherEntity.class)
public abstract class MixinStarlitCrusherEntity extends SkiesBossEntity {

    public MixinStarlitCrusherEntity(EntityType<? extends SkiesBossEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void onHurtCheck(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        DimensionalAscension.LOGGER.info("MIXIN TRIGGERED!");
        if (source.getEntity() instanceof LivingEntity) {
            ItemStack stack = ((LivingEntity)source.getEntity()).getMainHandItem();
            if(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().equals("tetra:modular_double")) {
                DimensionalAscension.LOGGER.info("MODULAR DOUBLE DETECTED");
                if (stack.canPerformAction(ToolAction.get("axe_dig")))
                    cir.setReturnValue(super.hurt(source, amount));
                DimensionalAscension.LOGGER.info(ToolAction.getActions().toString());
            }
        }
    }
}
