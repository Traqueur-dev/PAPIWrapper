package fr.traqueur.placeholders.api;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * The base placeholder class
 */
public abstract class BasePlaceholder {

    /**
     * The identifier
     */
    private final String identifier;
    /**
     * The description
     */
    private final String description;
    /**
     * The arguments
     */
    private final List<String> arguments;

    /**
     * The base placeholder constructor
     * @param identifier the identifier
     * @param description the description
     * @param arguments the arguments
     */
    public BasePlaceholder(String identifier, String description, List<String> arguments) {
        this.identifier = identifier;
        this.description = description;
        this.arguments = arguments;
    }

    /**
     * Apply the placeholder
     * @param player the player
     * @param arguments the arguments
     * @return the result
     */
    public abstract String apply(Player player, Player player2, List<String> arguments);

    /**
     * Get the identifier
     * @return the identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Get the description
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get the arguments
     * @return the arguments
     */
    public List<String> getArguments() {
        return this.arguments;
    }
}
