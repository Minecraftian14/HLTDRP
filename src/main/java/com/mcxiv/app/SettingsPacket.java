package com.mcxiv.app;

import com.mcxiv.logger.formatted.FLog;
import com.mcxiv.logger.tables.Table;
import com.mcxiv.logger.util.StringsConsumer;

import java.util.Map;

public class SettingsPacket {

    public boolean isPluginEnabled = true;
    public boolean isProjectViewEnabled = true;
    public float updatePerSecondRate = 0.5f;

    public SettingsPacket() {
    }

    public SettingsPacket(SettingsPacket settingsPacket) {
        isPluginEnabled = settingsPacket.isPluginEnabled;
        isProjectViewEnabled = settingsPacket.isProjectViewEnabled;
        updatePerSecondRate = settingsPacket.updatePerSecondRate;
    }

    public void fromStorage(Map<String, Object> settings) {
        isPluginEnabled = (boolean) settings.getOrDefault("isPluginEnabled", true);
        isProjectViewEnabled = (boolean) settings.getOrDefault("isProjectViewEnabled", true);
        updatePerSecondRate = (float) settings.getOrDefault("UpdatePerSecondRate", 0.5f);
    }

    public void toStorage(Map<String, Object> settings) {
        settings.put("isPluginEnabled", isPluginEnabled);
        settings.put("isProjectViewEnabled", isProjectViewEnabled);
        settings.put("UpdatePerSecondRate", updatePerSecondRate);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        FLog log = FLog.getNew((StringsConsumer) st -> {
            for (String s : st) {
                builder.append(s);
            }
        });
        log.setDecorationType(HLTDRPPlugin.LOG.getDecorationType());
        Table.stripped()
                .title("Plugin Settings Detail")
                .head("Setting Name", "Status")
                .row("Plugin Activity", (isPluginEnabled ? "enabled" : "disabled"))
                .row("Project View", (isProjectViewEnabled ? "enabled" : "disabled"))
                .row("Update Rate", updatePerSecondRate+" rps")
                .formatTitle("@d0c600#0u")
                .formatHead("@4d2b00", "@713f00")
                .format("@a25900", "@d97700")
                .create(log);
        return builder.toString();
    }
}
