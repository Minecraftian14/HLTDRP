package com.mcxiv.app;

import main.generalLogger.LOGGER;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static main.utlities.ConsoleColors.ANSI.*;

public class RPCTool {

    public static final String format = GREEN_BACKGROUND + UNDERLINED + BOLD + BRIGHT_BLUE + " Discord RPC " + RESET + "\t";

    private static DiscordRichPresence.Builder presence;
    private static ScheduledExecutorService service;

    public static void init(String id) {
        LOGGER.general(format, "Initialising RPC!");

        var handlers = new DiscordEventHandlers.Builder()
                .setReadyEventHandler(RPCTool::onReady)
//                .setJoinGameEventHandler(RPCTool::onProjectOpen)
                .build();


        presence = new DiscordRichPresence.Builder("");
        presence.setBigImage("icon", "Hyper Lap 2D");

        DiscordRPC.discordInitialize(id, handlers, true);
        DiscordRPC.discordRegister(id, "");

        LOGGER.notice(format, "RPC Initialised!");

        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(DiscordRPC::discordRunCallbacks, 1, 2, TimeUnit.SECONDS);

        LOGGER.notice(format, "Executor on duty at rate 0.5 rps!");
    }

    private static final String[] boot_ups = new String[]{"Just Started", "Planning", "Opening up",
            "Getting Ready", "Launched", "Some Stuff"};
    private static final String[] actions = new String[]{"Working in", "Fiddling with", "Brainstorming on",
            "Living with", "Trying out", "Thinking about", "Updating", "Improving", "Testing"};

    public static void onReady(DiscordUser user) {
        presence.setDetails(boot_ups[(int) (Math.random() * boot_ups.length)]);
        presence.setStartTimestamps(System.currentTimeMillis() / 1000);
        DiscordRPC.discordUpdatePresence(presence.build());

        LOGGER.info(format, "Home RPC Updated");
    }

    public static void onProjectOpen(String rawMessage) {
        presence.setDetails(actions[(int) (Math.random() * actions.length)] + " " + rawMessage);
        DiscordRPC.discordUpdatePresence(presence.build());

        LOGGER.info(format, "Project Specific RPC Updated");
    }

    public static void onApplicationClose() {
        service.shutdown();
        DiscordRPC.discordShutdown();

        LOGGER.info(format, "RPC closed!");
    }

}

