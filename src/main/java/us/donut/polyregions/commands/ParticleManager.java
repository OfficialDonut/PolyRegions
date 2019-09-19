package us.donut.polyregions.commands;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import us.donut.polyregions.PolyRegionsPlugin;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager {

    private static List<Location> particleLocations = new ArrayList<>();

    public static void runParticleTask(PolyRegionsPlugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Location particleLocation : particleLocations) {
                particleLocation.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1, new Particle.DustOptions(Color.BLUE, 1));
            }
        }, 0, 3);
    }

    public static void createParticlesBetween(Location loc1, Location loc2) {
        Vector vector = loc2.toVector().subtract(loc1.toVector()).normalize().multiply(0.3);
        double magnitude = vector.length();
        double distance = loc1.distance(loc2);
        double distanceTravelled = 0;
        while (distanceTravelled < distance) {
            particleLocations.add(loc1 = loc1.clone().add(vector));
            distanceTravelled += magnitude;
        }
    }
}
