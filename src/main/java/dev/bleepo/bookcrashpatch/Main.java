package dev.bleepo.bookcrashpatch;

import dev.bleepo.bookcrashpatch.events.book;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("AntiBookCrash by Bleepo is loaded");
        getServer().getPluginManager().registerEvents(new book(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Bai");
    }
}
