package com.mcxiv.app;

import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTextField;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import games.rednblack.h2d.common.view.SettingsNodeValue;
import games.rednblack.h2d.common.view.ui.StandardWidgetsFactory;
import main.generalLogger.LOGGER;
import org.puremvc.java.interfaces.IFacade;

import static com.mcxiv.app.HLTDRPPlugin.format;

public class HLTDRPSettings extends SettingsNodeValue<SettingsPacket> {

    private final VisCheckBox cbx_pluginOn;
    private final VisCheckBox cbx_projName;
    private final VisTextField fie_updtRate;

    private final H2DPluginAdapter plugin;
    private boolean loaded = false;

    public HLTDRPSettings(IFacade facade, H2DPluginAdapter plugin) {
        super("Discord RPC", facade);
        this.plugin = plugin;

        // Instantiation
        cbx_pluginOn = StandardWidgetsFactory.createCheckBox("Enable Discord RPC: ");
        StandardWidgetsFactory.addTooltip(cbx_pluginOn, "If you dont want the plugin to show you're using HyperLap2D,\nor some bug is causing issues, you can disable the plugin here.");

        cbx_projName = StandardWidgetsFactory.createCheckBox("Show Project Name: ");
        StandardWidgetsFactory.addTooltip(cbx_projName, "If you want to keep your upcoming project a secret from your fans,\nyou might want to hide the project name from being visible to everyone!");

        fie_updtRate = StandardWidgetsFactory.createTextField();
        StandardWidgetsFactory.addTooltip(fie_updtRate, "This rate is measured as number of runs done per second.\n0.5 rps means RPC will update every 2 seconds.");

        // Adding Widgets
        getContentTable().add("HyperLap2D Discord RPC Plugin").left().row();

        getContentTable().addSeparator().colspan(2);

        getContentTable().add(cbx_pluginOn).left().padTop(17).padLeft(10).row();
        getContentTable().add(cbx_projName).left().padTop(10).padLeft(10).row();
        getContentTable().add(StandardWidgetsFactory.createLabel("Update Rate (rps): ", "default", Align.right)).left().padTop(10).padLeft(10);
        getContentTable().add(fie_updtRate).left().padTop(10).padLeft(10).row();

    }

    @Override
    public void translateSettingsToView() {
        loaded = true;
        cbx_pluginOn.setChecked(getSettings().isPluginEnabled);
        cbx_projName.setChecked(getSettings().isProjectViewEnabled);
        fie_updtRate.setText("" + getSettings().updatePerSecondRate);
    }

    @Override
    public void translateViewToSettings() {
        getSettings().isPluginEnabled = cbx_pluginOn.isChecked();
        getSettings().isProjectViewEnabled = cbx_projName.isChecked();

        try {
            getSettings().updatePerSecondRate = Float.parseFloat(fie_updtRate.getText());
        } catch (NumberFormatException e1) {
            LOGGER.error(format, "Error for", fie_updtRate.getText());
            try {
                getSettings().updatePerSecondRate = Float.parseFloat(fie_updtRate.getText().replaceAll("[^0-9.,]", ""));
                LOGGER.notice(format, "Error fixed as", getSettings().updatePerSecondRate);
            } catch (NumberFormatException e2) {
                getSettings().updatePerSecondRate = 0.5f;
                LOGGER.notice(format, "Error set to default", getSettings().updatePerSecondRate);
            }
        }

        RPCTool.updateSettings(getSettings());
        getSettings().toStorage(plugin.getStorage());
        facade.sendNotification(MsgAPI.SAVE_EDITOR_CONFIG);
    }

    @Override
    public boolean validateSettings() {
        try {
            return loaded && (
                    getSettings().isPluginEnabled != cbx_pluginOn.isChecked() ||
                            getSettings().isProjectViewEnabled != cbx_projName.isChecked() ||
                            getSettings().updatePerSecondRate != Float.parseFloat(fie_updtRate.getText())
            );
        } catch (NumberFormatException e) {
            LOGGER.general("Unfortunate Error For", e, "at", fie_updtRate.getText());
        }
        return false;
    }

}
