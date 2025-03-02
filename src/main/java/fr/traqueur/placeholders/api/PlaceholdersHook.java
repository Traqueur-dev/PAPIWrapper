package fr.traqueur.placeholders.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholdersHook extends PlaceholderExpansion implements Relational {

    private final JavaPlugin plugin;
    private final PlaceholdersManager placeholdersManager;

    public PlaceholdersHook(JavaPlugin plugin, PlaceholdersManager placeholdersManager) {
        this.plugin = plugin;
        this.placeholdersManager = placeholdersManager;
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return plugin.getName().toLowerCase();
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
        return this.placeholdersManager.onPlaceholderRequest(player, params);
    }

    @Override
    public String onPlaceholderRequest(Player player, Player player1, String params) {
        return this.placeholdersManager.onPlaceholderRequest(player, player1, params);
    }
}