package net.jomcraft.noitemdespawn.mixins.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.jomcraft.noitemdespawn.InterfaceEntityItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = EntityItem.class, priority = 1000)
public abstract class MixinEntityItemClient extends Entity implements InterfaceEntityItem {

    private boolean glowing = false;

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    public MixinEntityItemClient(World worldIn) {
        super(worldIn);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBrightnessForRender(float paramFloat) {
        if (this.glowing) return 15728880;
        return super.getBrightnessForRender(paramFloat);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canRenderOnFire() {
        return this.glowing ? true : isBurning();
    }
}