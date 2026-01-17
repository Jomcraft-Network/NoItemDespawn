package net.jomcraft.noitemdespawn;

import java.util.Map;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.DependsOn;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@DependsOn("forge")
@IFMLLoadingPlugin.TransformerExclusions({"net.jomcraft.noitemdespawn"})
public class NoItemDespawnPlugin implements IFMLLoadingPlugin {

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"net.jomcraft.noitemdespawn.NoItemDespawnClassTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;

    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

}
