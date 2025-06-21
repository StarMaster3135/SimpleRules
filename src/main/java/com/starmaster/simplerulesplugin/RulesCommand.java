package com.starmaster.simplerulesplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RulesCommand implements CommandExecutor, TabCompleter {
    
    private final SimpleRulesPlugin plugin;
    
    public RulesCommand(SimpleRulesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("rules")) {
            return false;
        }
        
        // Handle subcommands
        if (args.length > 0) {
            String subCommand = args[0].toLowerCase();
            
            switch (subCommand) {
                case "reload":
                    return handleReload(sender);
                case "help":
                    return handleHelp(sender);
                default:
                    sender.sendMessage(Component.text("Unknown subcommand! Use /rules help for available commands.")
                            .color(NamedTextColor.RED));
                    return true;
            }
        }
        
        // Check if sender has permission to view rules
        if (!sender.hasPermission("rules.view")) {
            sender.sendMessage(Component.text("You don't have permission to view the rules!")
                    .color(NamedTextColor.RED));
            return true;
        }
        
        displayRules(sender);
        return true;
    }
    
    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("rules.admin")) {
            sender.sendMessage(Component.text("You don't have permission to reload the configuration!")
                    .color(NamedTextColor.RED));
            return true;
        }
        
        plugin.reloadPluginConfig();
        sender.sendMessage(Component.text("SimpleRulesPlugin configuration reloaded successfully!")
                .color(NamedTextColor.GREEN));
        return true;
    }
    
    private boolean handleHelp(CommandSender sender) {
        sender.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                .color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("          SimpleRulesPlugin Help")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD));
        sender.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                .color(NamedTextColor.GOLD));
        
        sender.sendMessage(Component.text("/rules")
                .color(NamedTextColor.AQUA)
                .append(Component.text(" - Display server rules").color(NamedTextColor.WHITE)));
        
        if (sender.hasPermission("rules.admin")) {
            sender.sendMessage(Component.text("/rules reload")
                    .color(NamedTextColor.AQUA)
                    .append(Component.text(" - Reload plugin configuration").color(NamedTextColor.WHITE)));
        }
        
        sender.sendMessage(Component.text("/rules help")
                .color(NamedTextColor.AQUA)
                .append(Component.text(" - Show this help message").color(NamedTextColor.WHITE)));
        
        return true;
    }
    
    private void displayRules(CommandSender sender) {
        // Send rules header with better centering
        sender.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                .color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("                    SERVER RULES")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD));
        sender.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                .color(NamedTextColor.GOLD));
        
        // Get rules from config
        List<String> rules = plugin.getConfig().getStringList("rules");
        
        if (rules.isEmpty()) {
            sender.sendMessage(Component.text("No rules have been configured yet!")
                    .color(NamedTextColor.YELLOW));
        } else {
            for (int i = 0; i < rules.size(); i++) {
                Component ruleNumber = Component.text((i + 1) + ". ")
                        .color(NamedTextColor.AQUA)
                        .decorate(TextDecoration.BOLD);
                Component ruleText = Component.text(rules.get(i))
                        .color(NamedTextColor.WHITE);
                
                sender.sendMessage(ruleNumber.append(ruleText));
            }
        }
        
        // Send footer
        sender.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                .color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("Please follow these rules to keep our server enjoyable for everyone!")
                .color(NamedTextColor.YELLOW));
                
        // Add clickable help text
        if (sender instanceof Player) {
            sender.sendMessage(Component.text("Type ")
                    .color(NamedTextColor.GRAY)
                    .append(Component.text("/rules help")
                            .color(NamedTextColor.AQUA)
                            .clickEvent(ClickEvent.runCommand("/rules help")))
                    .append(Component.text(" for more commands").color(NamedTextColor.GRAY)));
        }
        
        sender.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                .color(NamedTextColor.GOLD));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subCommands = new ArrayList<>();
            subCommands.add("help");
            
            if (sender.hasPermission("rules.admin")) {
                subCommands.add("reload");
            }
            
            String input = args[0].toLowerCase();
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(input)) {
                    completions.add(subCommand);
                }
            }
        }
        
        return completions;
    }
}