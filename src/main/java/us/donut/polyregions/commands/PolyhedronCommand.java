package us.donut.polyregions.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import us.donut.polyregions.PolyRegion;
import us.donut.polyregions.PolyRegionsPlugin;
import us.donut.polyregions.PolyhedronFace;
import us.donut.polyregions.PolyhedronRegion;

import java.util.*;

public class PolyhedronCommand implements CommandExecutor, Listener {

    private PolyRegionsPlugin plugin;
    private Set<UUID> playersMakingPolyhedrons = new HashSet<>();
    private Map<UUID, List<PolyhedronFace>> polyhedronFaces = new HashMap<>();
    private Map<UUID, List<Location>> faceVertices = new HashMap<>();

    public PolyhedronCommand(PolyRegionsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (args.length == 0) {
            if (!playersMakingPolyhedrons.contains(uuid)) {
                playersMakingPolyhedrons.add(uuid);
                polyhedronFaces.put(uuid, new ArrayList<>());
                faceVertices.put(uuid, new ArrayList<>());
                player.sendMessage(ChatColor.GREEN + "Started polyhedron region creation...");
            } else {
                player.sendMessage(ChatColor.RED + "You are already creating a polyhedron region!");
            }
        } else if (args[0].equalsIgnoreCase("finish")) {
            if (playersMakingPolyhedrons.contains(uuid)) {
                List<PolyhedronFace> faces = polyhedronFaces.get(uuid);
                if (faces.size() >= 3) {
                    plugin.getCheckerCommand().addRegion(new PolyhedronRegion(faces.toArray(new PolyhedronFace[0])));
                    playersMakingPolyhedrons.remove(uuid);
                    polyhedronFaces.remove(uuid);
                    faceVertices.remove(uuid);
                    player.sendMessage(ChatColor.DARK_GREEN + "Created polyhedron region!");
                } else {
                    player.sendMessage(ChatColor.RED + "At least 3 faces are needed to define a polyhedron!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Start creating a polyhedron region with /polyhedron");
            }
        } else if (args[0].equalsIgnoreCase("fill")) {
            PolyRegion region = plugin.getCheckerCommand().getRegionAt(player);
            if (region instanceof PolyhedronRegion) {
                ((PolyhedronRegion) region).forEachBlock(block -> block.setType(Material.STONE));
                player.sendMessage(ChatColor.GREEN + "Filled polyhedron at your location!");
            } else {
                player.sendMessage(ChatColor.RED + "There is no region at your location!");
            }
        } else {
            return false;
        }

        return true;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (playersMakingPolyhedrons.contains(uuid)) {
            e.setCancelled(true);
            Location loc = e.getBlock().getLocation();
            loc.add(loc.getX() > 0 ? 0.5 : -0.5, 0.5, loc.getZ() > 0 ? 0.5 : -0.5);
            List<Location> vertices = faceVertices.get(uuid);
            if (vertices.size() >= 3 && loc.equals(vertices.get(0))) {
                ParticleManager.createParticlesBetween(loc, vertices.get(vertices.size() - 1));
                polyhedronFaces.get(uuid).add(new PolyhedronFace(vertices.toArray(new Location[0])));
                vertices.clear();
                player.sendMessage(ChatColor.DARK_GREEN + "Added face to polyhedron region!");
            } else {
                if (!vertices.contains(loc)) {
                    vertices.add(loc);
                    player.sendMessage(ChatColor.GREEN + "Added vertex to face of polyhedron region!");
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
