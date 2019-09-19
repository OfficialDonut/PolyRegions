package us.donut.polyregions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

public class PolyhedronRegion implements PolyRegion {

    private PolyhedronFace[] faces;
    private double xMin;
    private double yMin;
    private double zMin;
    private double xMax;
    private double yMax;
    private double zMax;

    public PolyhedronRegion(PolyhedronFace... faces) {
        if (faces.length < 3) {
            throw new IllegalArgumentException("At least 3 faces are needed to define a polyhedron");
        }
        this.faces = faces;

        Location initialVertex = faces[0].getVertices()[0];
        xMin = xMax = initialVertex.getX();
        yMin = yMax = initialVertex.getY();
        zMin = zMax = initialVertex.getZ();
        for (PolyhedronFace face : faces) {
            for (Location vertex : face.getVertices()) {
                xMin = Math.min(xMin, vertex.getX());
                yMin = Math.min(yMin, vertex.getY());
                zMin = Math.min(zMin, vertex.getZ());
                xMax = Math.max(xMax, vertex.getX());
                yMax = Math.max(yMax, vertex.getY());
                zMax = Math.max(zMax, vertex.getZ());
            }
        }
    }

    public boolean contains(Location point) {
        int intersections = 0;

        for (PolyhedronFace face : faces) {
            if (face.contains(point)) {
                return false;
            }

            Location pointOnPlane = face.getPointOnPlane();
            Vector normalVector = face.getNormalVector();
            double x0 = pointOnPlane.getX();
            double y0 = pointOnPlane.getY();
            double z0 = pointOnPlane.getZ();
            double a = normalVector.getX();
            double b = normalVector.getY();
            double c = normalVector.getZ();

            if (a != 0) {
                double t = -1 * (a * (point.getX() - x0) + b * (point.getY() - y0) + c * (point.getZ() - z0)) / a;
                double intersectionX = point.getX() + t;
                Location intersectionPoint = point.clone();
                intersectionPoint.setX(intersectionX);

                if (intersectionX > point.getX() && face.contains(intersectionPoint)) {
                    intersections++;
                }
            }
        }

        return intersections % 2 == 1;
    }

    public void forEachBlock(Consumer<Block> action) {
        Location loc = new Location(faces[0].getVertices()[0].getWorld(), 0, 0, 0);
        for (double x = xMin; x < xMax; x++) {
            for (double y = yMin; y < yMax; y++) {
                for (double z = zMin; z < zMax; z++) {
                    loc.setX(x);
                    loc.setY(y);
                    loc.setZ(z);
                    if (contains(loc)) {
                        action.accept(loc.getBlock());
                    }
                }
            }
        }
    }

    public PolyhedronFace[] getFaces() {
        return faces;
    }
}
