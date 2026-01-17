package net.jomcraft.noitemdespawn.mixins.common;

import net.jomcraft.noitemdespawn.NoItemDespawn;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {

    @Inject(method = "getEntityLifespan", at = @At(value = "RETURN"), cancellable = true, remap = false)
    public void NID_getEntityLifespan(ItemStack itemStack, World world, CallbackInfoReturnable<Integer> cir) {
        if(!NoItemDespawn.blacklist.contains(itemStack.getItem().getUnlocalizedName())){
            cir.setReturnValue(NoItemDespawn.despawnTime == -1 ? Short.MAX_VALUE : NoItemDespawn.despawnTime);
        }
    }
}