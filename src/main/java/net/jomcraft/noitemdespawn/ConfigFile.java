package net.jomcraft.noitemdespawn;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public final class ConfigFile implements Serializable {
    private transient final Configuration config;

    public int cooldown;

    public ConfigFile(File file) {
        config = new Configuration(file);
        addConfigValues();
    }

    private void addConfigValues() {
        config.setCategoryComment("Main", "Settings which apply as main configuration of the mod.");
        cooldown = config.get("Main", "Despawn-cooldown", 6000, "The custom despawn-cooldown of dropped items.").getInt();
        config.save();
    }

    public void syncConfiguration() {
        config.load();
        addConfigValues();
        config.save();
    }

    public Configuration getInstance() {
        return config;
    }

    public List<IConfigElement> getCategories() {
        List<IConfigElement> elements = new ArrayList<IConfigElement>();

        for (String s : config.getCategoryNames()) {
            IConfigElement element = new ConfigElement(config.getCategory(s));
            for (IConfigElement e : (List<IConfigElement>) element.getChildElements()) {
                elements.add(e);
            }

        }
        return elements;
    }

}
