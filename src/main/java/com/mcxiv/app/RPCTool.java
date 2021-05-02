package com.mcxiv.app;

import com.mcxiv.logger.decorations.Format;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.mcxiv.app.HLTDRPPlugin.LOG;

@Format({":: :@3dae00$Wbu:Discord RPC ::", ":: :@1b4d00#Fn%40s: ::"})
public class RPCTool {

    private static DiscordRichPresence.Builder presence;
    private static ScheduledExecutorService service;
    private static SettingsPacket packet = new SettingsPacket();

    public static void init() {
        if (!packet.isPluginEnabled) {
            LOG.prtf(":$R:").consume("Plugin is not enabled! Please enable plugin if you want to use it");
            return;
        }
        LOG.prt("", "Initialising RPC!");

        var handlers = new DiscordEventHandlers.Builder()
                .setReadyEventHandler(RPCTool::onReady)
//                .setJoinGameEventHandler(RPCTool::onProjectOpen)
                .build();


        presence = new DiscordRichPresence.Builder("");
        presence.setBigImage("icon", "HyperLap2D");

        DiscordRPC.discordInitialize("769535754350886923", handlers, true);
        DiscordRPC.discordRegister("769535754350886923", "");

        LOG.prt("", "RPC Initialised!");

        startExecutor();
        LOG.raw(packet.toString());
    }

    private static void startExecutor() {
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(DiscordRPC::discordRunCallbacks, 500, (long) (packet.updatePerSecondRate * 1000), TimeUnit.MILLISECONDS);

        LOG.prt("", "Executor on duty at rate 0.5 rps!");
    }

    public static void init(SettingsPacket settingsPacket) {
        packet = new SettingsPacket(settingsPacket);
        init();
    }

    private static final String[] boot_ups = new String[]{"Just Started", "Planning", "Opening up",
            "Getting Ready", "Launched", "Some Stuff"};
    private static final String[] actions = new String[]{"Working on", "Fiddling with", "Brainstorming on",
            "Living with", "Trying out", "Thinking about", "Updating", "Improving", "Testing"};

    public static void onReady(DiscordUser user) {
        if (!packet.isPluginEnabled) return;

        presence.setDetails(boot_ups[(int) (Math.random() * boot_ups.length)]);
        presence.setStartTimestamps(System.currentTimeMillis() / 1000);
        DiscordRPC.discordUpdatePresence(presence.build());

        LOG.prt("", "Home RPC Updated");
    }

    public static void onProjectOpen(String rawMessage) {
        if (!packet.isPluginEnabled || !packet.isProjectViewEnabled) return;

        presence.setDetails(actions[(int) (Math.random() * actions.length)] + " " + rawMessage);
        DiscordRPC.discordUpdatePresence(presence.build());

        LOG.prt("", "Project Specific RPC Updated");
    }

    public static void onApplicationClose() {
        if (!packet.isPluginEnabled) return;

        service.shutdown();
        DiscordRPC.discordShutdown();

        LOG.prt("", "RPC closed!");
    }

    public static void updateSettings(SettingsPacket newSets) {

        LOG.prt("", "Settings changing from", packet, "to");
        LOG.raw(newSets.toString());

        onApplicationClose();
        init(newSets);

        /*
        if (packet.isPluginEnabled && !newSets.isPluginEnabled) {
            onApplicationClose();
            return;
        }

        if (!packet.isPluginEnabled && newSets.isPluginEnabled) {
            init(newSets);
            return;
        }

        if (packet.isProjectViewEnabled && !newSets.isProjectViewEnabled) {
            presence.setDetails(boot_ups[(int) (Math.random() * boot_ups.length)]);
            DiscordRPC.discordUpdatePresence(presence.build());
        }

//        if(!packet.isProjectViewEnabled && newSets.isProjectViewEnabled) {
//         // There's nothing to do, it'll automatically update
//        }

        if (packet.updatePerSecondRate != packet.updatePerSecondRate) {
            service.shutdown();
            startExecutor();
        }

        LOGGER.general(format, "Settings changed from", packet, "to", newSets);
        packet = new SettingsPacket(newSets);
        */

    }

}

