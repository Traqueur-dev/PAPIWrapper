package fr.traqueur.placeholders.api;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * The placeholders class
 */
public class Placeholders {

    private static JavaPlugin plugin;
    private static PlaceholdersHook placeholdersHook;

    /**
     * Extract the placeholders in placeholders.md
     */
    public static void extract() {
        StringBuilder sb = new StringBuilder();
        sb.append("# Available Placeholders\n\n");

        boolean hasPlaceholders = false;

        if(!placeholdersHook.getRegisteredPlaceholders().isEmpty()) {
            hasPlaceholders = true;
            sb.append("| Placeholder | Description |\n");
            sb.append("|-------------|-------------|\n");
            for (BasePlaceholder placeholder : placeholdersHook.getRegisteredPlaceholders().values()) {
                sb.append(writePlaceholder("", placeholder));
            }
        }

        if(!placeholdersHook.getRegisteredPlaceholders().isEmpty()) {
            if(hasPlaceholders) {
                sb.append("\n");
            }
            sb.append("| Relational Placeholder | Description |\n");
            sb.append("|-------------|-------------|\n");
            for (BasePlaceholder placeholder : placeholdersHook.getRegisteredRelationalPlaceholders().values()) {
                sb.append(writePlaceholder("rel_", placeholder));
            }
        }

        if(!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File file = new File(plugin.getDataFolder(),"placeholders.md");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(sb.toString());
        } catch (IOException e) {
            plugin.getLogger().warning("Could not write placeholders.md");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    /**
     * Write a placeholder
     * @param prefix the prefix
     * @param placeholder the placeholder
     * @return the string
     */
    private static String writePlaceholder(String prefix, BasePlaceholder placeholder) {
        StringBuilder sb = new StringBuilder();
        String identifier = placeholder.getIdentifier();
        String description = placeholder.getDescription();
        List<String> arguments = placeholder.getArguments();

        String placeholderFormat = "%" + prefix + placeholdersHook.getPrefix() + "_" + identifier;
        if (!arguments.isEmpty()) {
            placeholderFormat += "_" + arguments.stream().map(arg -> "\\<"+arg+"\\>").collect(Collectors.joining("_"));
        }
        placeholderFormat += "%";

        sb.append("| ").append(placeholderFormat).append(" | ").append(description).append(" |\n");
        return sb.toString();
    }

    /**
     * Load the placeholders
     * @param plugin the plugin
     */
    public static void load(JavaPlugin plugin) {
        load(plugin, plugin.getName().toLowerCase());
    }

    /**
     * Load the placeholders
     * @param plugin the plugin
     * @param prefix the prefix
     */
    public static void load(JavaPlugin plugin, String prefix) {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            throw new IllegalStateException("PlaceholderAPI not found");
        }
        Placeholders.plugin = plugin;
        placeholdersHook = new PlaceholdersHook(plugin, prefix);
        placeholdersHook.register();
    }

    /**
     * Register a placeholder
     * @param identifier the identifier
     * @param function the function
     * @param description the description
     * @param args the args
     */
    public static void register(String identifier, BiFunction<Player, List<String>,String> function, String description, String... args) {
        placeholdersHook.register(identifier, function, description, args);
    }
    /**
     * Register a placeholder
     * @param identifier the identifier
     * @param function the function
     */
    public static void register(String identifier, BiFunction<Player, List<String>,String> function) {
        placeholdersHook.register(identifier, function, "Empty description");
    }


    /**
     * Register a placeholder
     * @param identifier the identifier
     * @param function the function
     * @param description the description
     * @param args the args
     */
    public static void register(String identifier, TriFunction<Player, Player,List<String>, String> function, String description, String... args) {
        placeholdersHook.register(identifier, function, description, args);
    }

    /**
     * Register a placeholder
     * @param identifier the identifier
     * @param function the function
     */
    public static void register(String identifier, TriFunction<Player, Player,List<String>, String> function) {
        placeholdersHook.register(identifier, function, "Empty description");
    }

    /**
     * Parse a text with placeholders
     * @param player the player
     * @param text the text
     * @return the parsed text
     */
    public static String parse(Player player, String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    /**
     * Parse a text with relational placeholders
     * @param player the player
     * @param player2 the player 2
     * @param text the text
     * @return the parsed text
     */
    public static String parse(Player player, Player player2, String text) {
        return PlaceholderAPI.setRelationalPlaceholders(player, player2, text);
    }

    /**
     * Parse a list of text with placeholders
     * @param player the player
     * @param text the text
     * @return the parsed text
     */
    public static List<String> parse(Player player, List<String> text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    /**
     * Parse a list of text with relational placeholders
     * @param player the player
     * @param player2 the player 2
     * @param text the text
     * @return the parsed text
     */
    public static List<String> parse(Player player, Player player2, List<String> text) {
        return PlaceholderAPI.setRelationalPlaceholders(player, player2, text);
    }

}