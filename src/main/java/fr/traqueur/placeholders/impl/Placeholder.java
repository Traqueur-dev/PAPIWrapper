package fr.traqueur.placeholders.impl;

import fr.traqueur.placeholders.api.BasePlaceholder;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiFunction;

/**
 * A placeholder that can be used to replace a string with a value
 */
public class Placeholder extends BasePlaceholder {

    /**
     * The function of the placeholder
     */
    private final BiFunction<Player, List<String> ,String> function;

    /**
     * Create a new placeholder
     *
     * @param identifier the identifier of the placeholder
     * @param description the description of the placeholder
     * @param function the function of the placeholder
     * @param arguments the arguments of the placeholder
     */
    public Placeholder(String identifier, String description, BiFunction<Player, List<String> ,String> function, List<String> arguments) {
        super(identifier, description, arguments);
        this.function = function;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public String apply(Player player, Player player2, List<String> arguments) {
        return this.function.apply(player, arguments);
    }
}
