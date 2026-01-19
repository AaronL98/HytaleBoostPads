package io.github.AaronL98.HytaleBoostPads;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import io.github.AaronL98.HytaleBoostPads.events.BoostPadSystem;

public class HytaleBoostPads extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public HytaleBoostPads(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("%s version %s has loaded", this.getName(), this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
    }

    // In `start` because we want to register system using Player component after other plugins have had chance to register
    @Override
    protected void start() {
        this.getEntityStoreRegistry().registerSystem(new BoostPadSystem());
    }
}
