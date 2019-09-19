package us.donut.polyregions;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import us.donut.polyregions.commands.CheckerCommand;
import us.donut.polyregions.commands.ParticleManager;
import us.donut.polyregions.commands.PolygonCommand;
import us.donut.polyregions.commands.PolyhedronCommand;

public class PolyRegionsPlugin extends JavaPlugin {

    private PolygonCommand polygonCommand;
    private PolyhedronCommand polyhedronCommand;
    private CheckerCommand checkerCommand;

    @Override
    public void onEnable() {
        getCommand("polygon").setExecutor(polygonCommand = new PolygonCommand(this));
        getCommand("polyhedron").setExecutor(polyhedronCommand = new PolyhedronCommand(this));
        getCommand("regionchecker").setExecutor(checkerCommand = new CheckerCommand(this));
        Bukkit.getPluginManager().registerEvents(polygonCommand, this);
        Bukkit.getPluginManager().registerEvents(polyhedronCommand, this);
        ParticleManager.runParticleTask(this);
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled!");
    }

    public PolygonCommand getPolygonCommand() {
        return polygonCommand;
    }

    public PolyhedronCommand getPolyhedronCommand() {
        return polyhedronCommand;
    }

    public CheckerCommand getCheckerCommand() {
        return checkerCommand;
    }
}
