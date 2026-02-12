package net.jomcraft.noitemdespawn;

import java.lang.reflect.Field;
import java.util.List;
import com.google.common.base.Throwables;
import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiConfigNID extends GuiConfig {

    public GuiConfigNID(GuiScreen parent) {
        this(parent, NoItemDespawn.getConfig().getCategories(), NoItemDespawn.MODID, false, false, "NoItemDespawn - Config");
    }

    private static Field apply;

    static {
        try {
            apply = GuiConfig.class.getDeclaredField("chkApplyGlobally");
            apply.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GuiConfigNID(GuiScreen parentScreen, List<IConfigElement> configElements, String modID, boolean allRequireWorldRestart, boolean allRequireMcRestart, String title) {
        super(parentScreen, configElements, modID, allRequireWorldRestart, allRequireMcRestart, title);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        /*Mainly credits to @bspkrs*/
        if (button.id == 2000) {
            try {
                if ((configID != null || this.parentScreen == null || !(this.parentScreen instanceof GuiConfigNID)) && (this.entryList.hasChangedEntry(true))) {

                    this.entryList.saveConfigElements();

                    if (Loader.isModLoaded(modID)) {
                        ConfigChangedEvent event = new OnConfigChangedEvent(modID, configID, isWorldRunning, false);
                        FMLCommonHandler.instance().bus().post(event);
                        if (!event.getResult().equals(Result.DENY))
                            FMLCommonHandler.instance().bus().post(new PostConfigChangedEvent(modID, configID, isWorldRunning, false));

                        if (this.parentScreen instanceof GuiConfigNID)
                            ((GuiConfigNID) this.parentScreen).needsRefresh = true;
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            this.mc.displayGuiScreen(this.parentScreen);

        } else if (button.id == 2001) {
            this.entryList.setAllToDefault(this.getApply().isChecked());
        } else if (button.id == 2002) {
            this.entryList.undoAllChanges(this.getApply().isChecked());
        }
    }

    private GuiCheckBox getApply() {
        try {
            return (GuiCheckBox) apply.get(this);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
