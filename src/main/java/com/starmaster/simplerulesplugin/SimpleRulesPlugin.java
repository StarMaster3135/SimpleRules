package com.starmaster.simplerulesplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class SimpleRulesPlugin extends JavaPlugin {
    
    private static SimpleRulesPlugin instance;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config if it doesn't exist
        saveDefaultConfig();
        
        // Register the rules command
        RulesCommand rulesCommand = new RulesCommand(this);
        getCommand("rules").setExecutor(rulesCommand);
        getCommand("rules").setTabCompleter(rulesCommand);
        
        getLogger().info("SimpleRulesPlugin v" + getDescription().getVersion() + " has been enabled!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("SimpleRulesPlugin has been disabled!");
    }
    
    public static SimpleRulesPlugin getInstance() {
        return instance;
    }
    
    public void reloadPluginConfig() {
        reloadConfig();
        getLogger().info("Configuration reloaded!");
    }
}