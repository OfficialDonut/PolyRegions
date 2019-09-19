package us.donut.polyregions;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class PolygonRegion implements PolyRegion {

    private Location[] vertices;

    public PolygonRegion(Location... vertices) {
        if (vertices.length < 3) {
            throw new IllegalArgumentException("At least 3 points are needed to define a polygon");
        }
        this.vertices = vertices;
    }

    public boolean contains(Location point) {
        int intersections = 0;

        for (int i = 0; i < vertices.length; i++) {
            Location vertex1 = vertices[i];
            Location vertex2 = vertices[i + 1 < vertices.length ? i + 1 : 0];
            double x0 = vertex1.getX();
            double z0 = vertex1.getZ();
            double x1 = vertex2.getX();
            double z1 = vertex2.getZ();

            Vector borderVector = vertex2.toVector().subtract(vertex1.toVector());
            double deltaZ = borderVector.getZ();
            double deltaX = borderVector.getX();

            if (deltaZ != 0) {
                double intersectionZ = point.getZ();
                double intersectionX = deltaX == 0 ? x0 : x0 + (intersectionZ - z0) / (deltaZ / deltaX);

                if (Math.abs(intersectionX - x0) < Vector.getEpsilon()) {
                    return false;
                }

                if (intersectionX > point.getX()
                        && intersectionX >= Math.min(x0, x1) && intersectionX <= Math.max(x0, x1)
                        && intersectionZ >= Math.min(z0, z1) && intersectionZ <= Math.max(z0, z1)) {
                    intersections++;
                }
            } else if (point.getZ() == z0) {
                return false;
            }
        }

        return intersections % 2 == 1;
    }

    public Location[] getVertices() {
        return vertices;
    }
}
