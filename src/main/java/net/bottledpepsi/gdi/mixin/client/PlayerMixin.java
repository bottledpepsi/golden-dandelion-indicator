package net.bottledpepsi.gdi.mixin.client;

import net.minecraft.world.entity.AgeableMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.bottledpepsi.gdi.impl.IGoldenDandelionMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (!this.level().isClientSide())
            return;

        boolean holdingDandelion = this.getMainHandItem().getItem().equals(Items.GOLDEN_DANDELION.asItem())
                || this.getOffhandItem().getItem().equals(Items.GOLDEN_DANDELION.asItem());
        if (!holdingDandelion)
            return;

        this.level().getEntitiesOfClass(AgeableMob.class, this.getBoundingBox().inflate(10.0)).forEach(mob -> {
            if (mob instanceof IGoldenDandelionMob baby && mob.isBaby() && !mob.canAgeUp()) {
                baby.showGoldenParticle();
            }
        });
    }
}
