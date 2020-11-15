package com.mcxiv.app;

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
        return " [ Plugin " + (isPluginEnabled ? "enabled" : "disabled") +
                ", Project Display " + (isPluginEnabled ? "enabled" : "disabled") +
                ", Update Rate " + updatePerSecondRate +
                " ]";
    }
}
