package us.donut.polygonregions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private List<PolygonRegion> regions = new ArrayList<>();
    private PolygonRegion currentRegion;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String arg1 = args[0];

        if (arg1.equalsIgnoreCase("create")) {
            currentRegion = new PolygonRegion();
            player.sendMessage("Started polygon creation");
            return true;
        }

        if (arg1.equalsIgnoreCase("corner")) {
            currentRegion.addCorner(player.getLocation());
            player.sendMessage("Added corner to polygon");
            return true;
        }

        if (arg1.equalsIgnoreCase("done")) {
            regions.add(currentRegion);
            currentRegion = null;
            player.sendMessage("Finished polygon creation");
            return true;
        }

        if (arg1.equalsIgnoreCase("check")) {
            for (int i = 0; i < regions.size(); i++) {
                player.sendMessage((i + 1) + ". " + regions.get(i).contains(player.getLocation()));
            }
        }

        return true;
    }
}
