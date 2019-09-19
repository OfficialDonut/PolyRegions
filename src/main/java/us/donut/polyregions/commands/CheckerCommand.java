package us.donut.polyregions.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.donut.polyregions.PolyRegion;
import us.donut.polyregions.PolyRegionsPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CheckerCommand implements CommandExecutor {

    private Set<UUID> playersWithCheckerEnabled = new HashSet<>();
    private Set<PolyRegion> regions = new HashSet<>();

    public CheckerCommand(PolyRegionsPlugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (UUID uuid : playersWithCheckerEnabled) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    String message = getRegionAt(player) != null ? ChatColor.GREEN + "You are in a region!" : ChatColor.RED + "You are not in a region!";
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                }
            }
        }, 0, 3);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (playersWithCheckerEnabled.contains(uuid)) {
            playersWithCheckerEnabled.remove(uuid);
            player.sendMessage(ChatColor.RED + "Disabled region checker!");
        } else {
            playersWithCheckerEnabled.add(uuid);
            player.sendMessage(ChatColor.GREEN + "Enabled region checker!");
        }

        return true;
    }

    public PolyRegion getRegionAt(Player player) {
        for (PolyRegion region : regions) {
            if (region.contains(player.getLocation())) {
                return region;
            }
        }
        return null;
    }

    public void addRegion(PolyRegion region) {
        regions.add(region);
    }
}
