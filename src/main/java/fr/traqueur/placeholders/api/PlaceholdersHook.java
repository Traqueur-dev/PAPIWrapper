package fr.traqueur.placeholders.api;

import fr.traqueur.placeholders.impl.Placeholder;
import fr.traqueur.placeholders.impl.RelationalPlaceholder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is a placeholder hook for PlaceholderAPI.
 */
public class PlaceholdersHook extends PlaceholderExpansion implements Relational {

    private final JavaPlugin plugin;
    private final String prefix;
    private final Map<String, BasePlaceholder> placeholders;
    private final Map<String, BasePlaceholder> relationalPlaceholders;

    /**
     * Create a new placeholder hook.
     * @param plugin The plugin.
     * @param prefix The placeholder prefix.
     */
    protected PlaceholdersHook(JavaPlugin plugin, String prefix) {
        this.plugin = plugin;
        this.prefix = prefix;
        placeholders = new HashMap<>();
        relationalPlaceholders = new HashMap<>();
    }

    /**
     * Return the placeholder identifier.
     * @return The placeholder identifier.
     */
    @NotNull
    @Override
    public String getIdentifier() {
        return this.prefix;
    }

    /**
     * Return the plugin author.
     * @return The plugin author.
     */
    @NotNull
    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    /**
     * Return the plugin version.
     * @return The plugin version.
     */
    @NotNull
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    /**
     * Return the parsed value of the placeholder.
     * @param player The player.
     * @param params The parameters of the placeholder.
     * @return the parsed value of the placeholder.
     */
    @Nullable
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        for (Map.Entry<String, BasePlaceholder> stringFunctionEntry : this.placeholders.entrySet()) {
            if(params.startsWith(stringFunctionEntry.getKey())) {
                List<String> list = extractParams(player, null, params, stringFunctionEntry.getKey());
                return ChatColor.translateAlternateColorCodes('&', stringFunctionEntry.getValue().apply(player, null, list));
            }
        }
        return "Error";
    }

    /**
     * Return the parsed value of the relational placeholder.
     * @param player The player.
     * @param player1 The second player.
     * @param params The parameters of the placeholder.
     * @return the parsed value of the relational placeholder.
     */
    @Override
    public String onPlaceholderRequest(Player player, Player player1, String params) {
        for (Map.Entry<String, BasePlaceholder> stringBiFunctionEntry : this.relationalPlaceholders.entrySet()) {
            if(params.startsWith(stringBiFunctionEntry.getKey())) {
                List<String> list = extractParams(player, player1, params, stringBiFunctionEntry.getKey());
                return ChatColor.translateAlternateColorCodes('&', stringBiFunctionEntry.getValue().apply(player, player1, list));
            }
        }
        return "Error";
    }

    /**
     * Register a placeholder with a function that takes a player and a list of parameters.
     * @param identifier The placeholder identifier.
     * @param function The function that takes a player and a list of parameters.
     */
    protected void register(String identifier, BiFunction<Player, List<String> ,String> function, String description, String... args) {
        this.placeholders.put(identifier, new Placeholder(identifier, description, function, Arrays.asList(args)));
    }

    /**
     * Register a relational placeholder with a function that takes two players and a list of parameters.
     * @param identifier The placeholder identifier.
     * @param function The function that takes two players and a list of parameters.
     */
    protected void register(String identifier, TriFunction<Player, Player,List<String>, String> function, String description, String... args) {
        this.relationalPlaceholders.put(identifier, new RelationalPlaceholder(identifier, description, function, Arrays.asList(args)));
    }

    /**
     * Extract the parameters from the placeholder string.
     * @param player The player.
     * @param player2 The second player.
     * @param params The placeholder string.
     * @param key The placeholder key.
     * @return The list of parameters.
     */
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

    public String getPrefix() {
        return prefix;
    }

    public @NotNull Map<String, BasePlaceholder> getRegisteredPlaceholders() {
        return placeholders;
    }

    public Map<String, BasePlaceholder> getRegisteredRelationalPlaceholders() {
        return relationalPlaceholders;
    }
}