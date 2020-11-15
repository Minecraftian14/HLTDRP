package com.mcxiv.app;

import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import main.generalLogger.LOGGER;
import net.arikia.dev.drpc.DiscordRPC;
import net.mountainblade.modular.annotations.Implementation;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

import static main.utlities.ConsoleColors.ANSI.*;

@Implementation(authors = "Minecraftian14", version = "1.0.0")
public class HLTDRPPlugin extends H2DPluginAdapter {
    public static final String CLASS_NAME = HLTDRPPlugin.class.getName();

    public static final String format = GREEN_BACKGROUND + UNDERLINED + BOLD + BRIGHT_BLUE + " RPC Plugin " + RESET + " ";

    public HLTDRPPlugin() {
        super(CLASS_NAME);
        LOGGER.general(format, "Plugin Instantiated!");
    }

    @Override
    public void initPlugin() {
        RPCTool.init("769535754350886923");
        facade.registerMediator(new Med(this));
        LOGGER.general(format, "Plugin Initialised!");
    }

}

class Med extends Mediator {
    public static final String NAME = Med.class.getName();

    private HLTDRPPlugin plugin;

    public Med(HLTDRPPlugin plugin) {
        super(NAME);
        this.plugin = plugin;
        LOGGER.general(HLTDRPPlugin.format, "Mediator Initialised!");
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{MsgAPI.SCENE_LOADED, MsgAPI.DISPOSE};
    }

    @Override
    public void handleNotification(INotification notification) {
        if (MsgAPI.SCENE_LOADED.equals(notification.getName()))
            RPCTool.onProjectOpen(plugin.getAPI().getCurrentProjectVO().projectName);

        else if (MsgAPI.DISPOSE.equals(notification.getName()))
            RPCTool.onApplicationClose();

    }
}

