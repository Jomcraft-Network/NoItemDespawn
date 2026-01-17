package net.jomcraft.noitemdespawn.mixins.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityItem.class, priority = 1001)
public abstract class MixinEntityItemCommon extends Entity {

    @Shadow
    public int age;

    @Shadow
    public int lifespan;

    public MixinEntityItemCommon(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "onUpdate", at = @At(value = "HEAD"), cancellable = true, remap = true)
    public void NID_onUpdate(CallbackInfo ci) {
        if (!this.worldObj.isRemote && (this.age + 1) >= this.lifespan)
        {
            if (this.getDataWatcher().getWatchableObjectItemStack(10) != null && this.lifespan == Short.MAX_VALUE) {
                this.age = 0;
            }
        }
    }
}