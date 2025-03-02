package fr.traqueur.placeholders.api;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PlaceholdersManager {

    private final String prefix;
    private final Map<String, BiFunction<Player, List<String> ,String>> placeholders;
    private final Map<String, TriFunction<Player,Player,List<String>, String>> relationalPlaceholders;

    protected PlaceholdersManager(String prefix) {
        this.prefix = prefix.toLowerCase();
        placeholders = new HashMap<>();
        relationalPlaceholders = new HashMap<>();
    }

    protected void register(String identifier, BiFunction<Player, List<String> ,String> function) {
        this.placeholders.put(identifier, function);
    }

    protected void register(String identifier, TriFunction<Player, Player,List<String>, String> function) {
        this.relationalPlaceholders.put(identifier, function);
    }

    protected String setPlaceholders(Player player, String text) {
        if(text.contains("%")) {
            String[] split = text.split("%");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                if(i % 2 == 0) {
                    builder.append(split[i]);
                } else {
                    String placeholder = split[i];
                    placeholder = placeholder.replace(this.prefix+"_", "");
                    builder.append(onPlaceholderRequest(player, placeholder));
                }
            }
            return builder.toString();
        }
        return text;
    }

    protected String setRelationalPlaceholders(Player player, Player player1, String text) {
        if(text.contains("%")) {
            String[] split = text.split("%");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                if(i % 2 == 0) {
                    builder.append(split[i]);
                } else {
                    String placeholder = split[i];
                    placeholder = placeholder.replace("rel_" + this.prefix+"_", "");
                    builder.append(onPlaceholderRequest(player, player1, placeholder));
                }
            }
            return builder.toString();
        }
        return text;
    }

    protected String onPlaceholderRequest(Player player, String params) {
        for (Map.Entry<String, BiFunction<Player, List<String>, String>> stringFunctionEntry : this.placeholders.entrySet()) {
            if(params.startsWith(stringFunctionEntry.getKey())) {
                List<String> list = extractParams(params, stringFunctionEntry.getKey());
                return ChatColor.translateAlternateColorCodes('&', stringFunctionEntry.getValue().apply(player, list));
            }
        }
        return "Error";
    }

    protected String onPlaceholderRequest(Player player, Player player1, String params) {
        for (Map.Entry<String, TriFunction<Player, Player, List<String>, String>> stringBiFunctionEntry : this.relationalPlaceholders.entrySet()) {
            if(params.startsWith(stringBiFunctionEntry.getKey())) {
                List<String> list = extractParams(params, stringBiFunctionEntry.getKey());
                return ChatColor.translateAlternateColorCodes('&', stringBiFunctionEntry.getValue().apply(player, player1, list));
            }
        }
        return "Error";
    }

    private List<String> extractParams(String params, String key) {
        String internalParams = params.replace(key +"_", "");
        List<String> list = new ArrayList<>();
        Matcher matcher = Pattern.compile("%[^%]+%|[^_%]+").matcher(internalParams);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }
}