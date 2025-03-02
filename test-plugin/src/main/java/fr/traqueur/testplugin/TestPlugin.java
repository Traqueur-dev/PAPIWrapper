package fr.traqueur.testplugin;

import fr.traqueur.placeholders.api.Placeholders;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Placeholders.load(this);
        Placeholders.register("test", (player, args) -> {
            if(args.isEmpty()) {
                return "§cErreur: §7Aucun argument spécifié.";
            } else {
                return "§aTest: §7" + args.getFirst();
            }
        });
        Placeholders.register("test", (player, player1, args) -> {
            if(args.isEmpty()) {
                return "§cErreur: §7Aucun argument spécifié.";
            } else {
                return "§aTest: §7" + args.getFirst();
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}