package fr.traqueur.placeholders.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholdersHook extends PlaceholderExpansion implements Relational {



    private final JavaPlugin plugin;
    private final String prefix;
    private final Map<String, BiFunction<Player, List<String> ,String>> placeholders;
    private final Map<String, TriFunction<Player,Player,List<String>, String>> relationalPlaceholders;
    private String splitter = "_";

    protected PlaceholdersHook(JavaPlugin plugin, String prefix) {
        this.plugin = plugin;
        this.prefix = prefix;
        placeholders = new HashMap<>();
        relationalPlaceholders = new HashMap<>();
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return this.prefix;
    }

    @NotNull
    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @NotNull
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Nullable
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        for (Map.Entry<String, BiFunction<Player, List<String>, String>> stringFunctionEntry : this.placeholders.entrySet()) {
            if(params.startsWith(stringFunctionEntry.getKey())) {
                List<String> list = extractParams(player, null, params, stringFunctionEntry.getKey());
                return ChatColor.translateAlternateColorCodes('&', stringFunctionEntry.getValue().apply(player, list));
            }
        }
        return "Error";
    }

    @Override
    public String onPlaceholderRequest(Player player, Player player1, String params) {
        for (Map.Entry<String, TriFunction<Player, Player, List<String>, String>> stringBiFunctionEntry : this.relationalPlaceholders.entrySet()) {
            if(params.startsWith(stringBiFunctionEntry.getKey())) {
                List<String> list = extractParams(player, player1, params, stringBiFunctionEntry.getKey());
                return ChatColor.translateAlternateColorCodes('&', stringBiFunctionEntry.getValue().apply(player, player1, list));
            }
        }
        return "Error";
    }

    protected void register(String identifier, BiFunction<Player, List<String> ,String> function) {
        this.placeholders.put(identifier, function);
    }

    protected void register(String identifier, TriFunction<Player, Player,List<String>, String> function) {
        this.relationalPlaceholders.put(identifier, function);
    }

    private List<String> extractParams(Player player, Player player2, String params, String key) {
        String internalParams = params.replaceFirst(key + "_?", "");
        List<String> list = new ArrayList<>();
        Matcher matcher = Pattern.compile("[^_{}]+|\\{[^}]*}").matcher(internalParams);
        while (matcher.find()) {
            String match = matcher.group();
            if(match.startsWith("{") && match.endsWith("}")) {
                match = match.replace("{", "%").replace("}", "%");
                if(player2 == null) {
                    match = Placeholders.parse(player, match);
                } else {
                    match = Placeholders.parse(player, player2, match);
                }
            }
            list.add(match);
        }
        return list;
    }
}