package com.timvisee.dungeonmaze.update;

import com.timvisee.dungeonmaze.Core;
import com.timvisee.dungeonmaze.server.ServerType;
import com.timvisee.dungeonmaze.service.Service;
import com.timvisee.dungeonmaze.util.MinecraftUtils;

public class UpdateCheckerService extends Service {

    /** Service name. */
    private static final String SERVICE_NAME = "Updater";

    /** Initialization flag. */
    private boolean init = false;

    /** Update checker instance. */
    private UpdateChecker updateChecker;

    /**
     * Initialize the service.
     *
     * @return True on success, false on failure. True will also be returned if the service was initialized already.
     */
    @Override
    public boolean init() {
        // TODO: Always start the updater, handle this option later?

        // Check whether the update checker should be enabled on startup
        if(Core.getConfigHandler().enableUpdateCheckerOnStartup)
            setupUpdateChecker();

        // Set the initialization flag
        this.init = true;
        return true;
    }

    /**
     * Check whether the service is initialized.
     *
     * @return True if the service is initialized, false otherwise.
     */
    @Override
    public boolean isInit() {
        return this.init;
    }

    /**
     * Destroy the service. The destruction won't be forced.
     *
     * @param force True to force the destruction. This wil re-destroy the service even if it isn't initialized.
     *              This will also force the initialization state to be set to false even if an error occurred while
     *              destroying.
     *
     * @return True on success, false on failure. True will also be returned if the service wasn't initialized. False
     * might be returned if force is set to true, even though the initialization state is set to false.
     */
    @Override
    public boolean destroy(boolean force) {
        // Reset the initialization flag
        this.init = false;

        // Shutdown the update checker
        shutdownUpdateChecker();
        return true;
    }

    /**
     * Get the name of the service.
     *
     * @return Service name.
     */
    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    /**
     * Set up the update checker.
     */
    public void setupUpdateChecker() {
        // Make sure the updater isn't initialized already
        if(isUpdateCheckerSetup())
            return;

        // Get the server type this plugin is running on
        ServerType serverType = MinecraftUtils.getServerType();

        // Determine the update checker type to use
        UpdateCheckerType updateCheckerType;
        switch(serverType) {
        case BUKKIT:
            updateCheckerType = UpdateCheckerType.BUKKIT;
            break;

        default:
            updateCheckerType = UpdateCheckerType.UNIVERSAL;
        }

        // Set up the update checker
        this.updateChecker = new UpdateChecker();

        // Start the update checker
        this.updateChecker.start(updateCheckerType);
    }

    /**
     * Check whether the update checker has been set up.
     *
     * @return True if the update checker has been set up, false if not.
     */
    public boolean isUpdateCheckerSetup() {
        return this.updateChecker != null;
    }

    /**
     * Shutdown the update checker if it has been set up.
     */
    public void shutdownUpdateChecker() {
        // TODO: Properly shut down the update checker!

        this.updateChecker = null;
    }

    /**
     * Get the update checker.
     *
     * @return Update checker instance.
     */
    public UpdateChecker getUpdateChecker() {
        // Initialize the update checker if it hasn't been initialized yet
        setupUpdateChecker();

        // Return the update checker instance
        return this.updateChecker;
    }
}
