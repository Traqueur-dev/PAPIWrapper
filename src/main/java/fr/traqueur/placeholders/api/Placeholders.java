package fr.traqueur.placeholders.api;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.function.BiFunction;

public class Placeholders {

    private static PlaceholdersHook placeholdersHook ;

    public static void load(JavaPlugin plugin) {
        load(plugin, plugin.getName().toLowerCase());
    }

    public static void load(JavaPlugin plugin, String prefix) {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            throw new IllegalStateException("PlaceholderAPI not found");
        }
        placeholdersHook = new PlaceholdersHook(plugin, prefix);
        placeholdersHook.register();
    }

    public static void register(String identifier, BiFunction<Player, List<String>,String> function) {
        placeholdersHook.register(identifier, function);
    }

    public static void register(String identifier, TriFunction<Player, Player,List<String>, String> function) {
        placeholdersHook.register(identifier, function);
    }

    public static String parse(Player player, String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public static String parse(Player player, Player player2, String text) {
        return PlaceholderAPI.setRelationalPlaceholders(player, player2, text);
    }

    public static List<String> parse(Player player, List<String> text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public static List<String> parse(Player player, Player player2, List<String> text) {
        return PlaceholderAPI.setRelationalPlaceholders(player, player2, text);
    }

}