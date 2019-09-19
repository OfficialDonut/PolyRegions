package us.donut.polyregions;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Plane {

    private Vector normalVector;
    private Location pointOnPlane;

    public Plane(Location... points) {
        if (points.length < 3) {
            throw new IllegalArgumentException("At least 3 points are needed to define a plane");
        } else if (points.length > 3 && !arePointsCoplanar(points)) {
            throw new IllegalArgumentException("Points are not coplanar");
        }
        Vector vector1OnPlane = points[1].toVector().subtract(points[0].toVector());
        Vector vector2OnPlane = points[2].toVector().subtract(points[0].toVector());
        normalVector = vector1OnPlane.crossProduct(vector2OnPlane).normalize();
        pointOnPlane = points[0];
    }

    public static boolean arePointsCoplanar(Location... points) {
        Plane temp = new Plane(points[0], points[1], points[2]);
        for (int i = 3; i < points.length; i++) {
            if (!temp.contains(points[i])) {
                return false;
            }
        }
        return true;
    }

    public boolean contains(Location point) {
        return Math.abs(normalVector.getX() * (point.getX() - pointOnPlane.getX())
                + normalVector.getY() * (point.getY()  - pointOnPlane.getY())
                + normalVector.getZ() * (point.getZ() - pointOnPlane.getZ())) < Vector.getEpsilon();
    }

    public Vector getNormalVector() {
        return normalVector;
    }

    public Location getPointOnPlane() {
        return pointOnPlane;
    }
}
