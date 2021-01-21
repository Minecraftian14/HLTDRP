package com.mcxiv.app;

import com.mcxiv.logger.decorations.Decoration;
import com.mcxiv.logger.decorations.Format;
import com.mcxiv.logger.decorations.TagDecoration;
import com.mcxiv.logger.formatted.FLog;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import net.mountainblade.modular.annotations.Implementation;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

import static com.mcxiv.app.HLTDRPPlugin.LOG;

@Implementation(authors = "Minecraftian14", version = "3.0.0")
@Format({":: :@5800d0#Fbu:RPC Plugin  ::", ":: :@01034b#Fn%40s: ::"})
public class HLTDRPPlugin extends H2DPluginAdapter {
    public static final String CLASS_NAME = HLTDRPPlugin.class.getName();

    public static final FLog LOG = FLog.getNew();

    public HLTDRPPlugin() {
        super(CLASS_NAME);
        Decoration.setDecoration(TagDecoration::new);
        LOG.prt("", "Plugin Instantiated!");
    }

    private SettingsPacket settingsPacket;

    @Override
    public void initPlugin() {
        facade.registerMediator(new Med(this));
        HLTDRPSettings settings = new HLTDRPSettings(facade, this);

        settingsPacket = new SettingsPacket();
        settingsPacket.fromStorage(getStorage());
        settings.setSettings(settingsPacket);
        facade.sendNotification(MsgAPI.ADD_PLUGIN_SETTINGS, settings);

        RPCTool.init(settingsPacket);

        LOG.prt("", "Plugin Initialised!");
    }

}

@Format({":: :@5800d0#Fbu:RPC Plugin  ::", ":: :@01034b#Fn%40s: ::"})
class Med extends Mediator {
    public static final String NAME = Med.class.getName();

    private HLTDRPPlugin plugin;

    public Med(HLTDRPPlugin plugin) {
        super(NAME);
        this.plugin = plugin;
        LOG.prt("", "Mediator Initialised!");
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                MsgAPI.SCENE_LOADED, MsgAPI.DISPOSE
        };
    }

    @Override
    public void handleNotification(INotification notification) {
        if (MsgAPI.SCENE_LOADED.equals(notification.getName()))
            RPCTool.onProjectOpen(plugin.getAPI().getCurrentProjectVO().projectName);

        else if (MsgAPI.DISPOSE.equals(notification.getName()))
            RPCTool.onApplicationClose();

    }
}

