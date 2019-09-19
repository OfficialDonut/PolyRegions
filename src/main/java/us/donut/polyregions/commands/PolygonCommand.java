package us.donut.polyregions.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import us.donut.polyregions.PolyRegionsPlugin;
import us.donut.polyregions.PolygonRegion;

import java.util.*;

public class PolygonCommand implements CommandExecutor, Listener {

    private PolyRegionsPlugin plugin;
    private Set<UUID> playersMakingPolygons = new HashSet<>();
    private Map<UUID, List<Location>> polygonVertices = new HashMap<>();

    public PolygonCommand(PolyRegionsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (!playersMakingPolygons.contains(uuid)) {
            playersMakingPolygons.add(uuid);
            polygonVertices.put(uuid, new ArrayList<>());
            player.sendMessage(ChatColor.GREEN + "Started polygon region creation...");
        } else {
            player.sendMessage(ChatColor.RED + "You are already creating a polygon region!");
        }

        return true;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (playersMakingPolygons.contains(uuid)) {
            e.setCancelled(true);
            Location loc = e.getBlock().getLocation();
            loc.add(loc.getX() > 0 ? 0.5 : -0.5, 0.5, loc.getZ() > 0 ? 0.5 : -0.5);
            List<Location> vertices = polygonVertices.get(uuid);
            if (vertices.size() >= 3 && loc.equals(vertices.get(0))) {
                ParticleManager.createParticlesBetween(loc, vertices.get(vertices.size() - 1));
                plugin.getCheckerCommand().addRegion(new PolygonRegion(vertices.toArray(new Location[0])));
                playersMakingPolygons.remove(uuid);
                polygonVertices.remove(uuid);
                player.sendMessage(ChatColor.DARK_GREEN + "Created polygon region!");
            } else {
                if (!vertices.contains(loc)) {
                    vertices.add(loc);
                    player.sendMessage(ChatColor.GREEN + "Added vertex to polygon region!");
                    if (vertices.size() > 1) {
                        ParticleManager.createParticlesBetween(loc, vertices.get(vertices.size() - 2));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "This location is already a vertex!");
                }
            }
        }
    }
}
