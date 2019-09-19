package us.donut.polyregions;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class PolyhedronFace extends Plane {

    private Location[] vertices;

    public PolyhedronFace(Location... vertices) {
        super(vertices);
        this.vertices = vertices;
    }

    @Override
    public boolean contains(Location point) {
        if (!super.contains(point)) {
            return false;
        }

        int intersections = 0;

        double x0 = point.getX();
        double y0 = point.getY();
        double z0 = point.getZ();

        Vector ray = vertices[1].toVector().subtract(vertices[0].toVector());
        double a = ray.getX();
        double b = ray.getY();
        double c = ray.getZ();

        for (int i = 0; i < vertices.length; i++) {
            Location vertex1 = vertices[i];
            Location vertex2 = vertices[i + 1 < vertices.length ? i + 1 : 0];
            double x1 = vertex1.getX();
            double y1 = vertex1.getY();
            double z1 = vertex1.getZ();
            double x2 = vertex2.getX();
            double y2 = vertex2.getY();
            double z2 = vertex2.getZ();

            Vector borderVector = vertex2.toVector().subtract(vertex1.toVector());
            double d = borderVector.getX();
            double e = borderVector.getY();
            double f = borderVector.getZ();

            double uNumerator;
            double uDenominator;

            if ((a != 0 && e != 0) || (b != 0 && d != 0)) {
                uNumerator = a * (y0 - y1) + b * (x1 - x0);
                uDenominator = a * e - b * d;
            } else if ((a != 0 && f != 0) || (c != 0 && d != 0)) {
                uNumerator = a * (z0 - z1) + c * (x1 - x0);
                uDenominator = a * f - c * d;
            } else if ((b != 0 && f != 0) || (c != 0 && e != 0)) {
                uNumerator = b * (z0 - z1) + c * (y1 - y0);
                uDenominator = b * f - c * e;
            } else {
                if (point.toVector().subtract(vertex1.toVector()).crossProduct(borderVector).lengthSquared() == 0) {
                    return false;
                }
                continue;
            }

            double u = uNumerator / uDenominator;
            double intersectionX = x1 + u * d;
            double intersectionY = y1 + u * e;
            double intersectionZ = z1 + u * f;

            if (Math.abs(intersectionX - x0) < Vector.getEpsilon()
                    && Math.abs(intersectionY - y0) < Vector.getEpsilon()
                    && Math.abs(intersectionZ - z0) < Vector.getEpsilon()) {
                return false;
            }

            if (intersectionX >= Math.min(x1, x2) && intersectionX <= Math.max(x1, x2)
                    && intersectionY >= Math.min(y1, y2) && intersectionY <= Math.max(y1, y2)
                    && intersectionZ >= Math.min(z1, z2) && intersectionZ <= Math.max(z1, z2)
                    && sameSign(a, intersectionX - x0) && sameSign(b, intersectionY - y0) && sameSign(c, intersectionZ - z0)) {
                intersections++;
            }
        }

        return intersections % 2 == 1;
    }

    private boolean sameSign(double num1, double num2) {
        boolean isNum1Zero = Math.abs(num1) < Vector.getEpsilon();
        boolean isNum2Zero = Math.abs(num2) < Vector.getEpsilon();
        return isNum1Zero || isNum2Zero ? isNum1Zero && isNum2Zero : num1 < 0 == num2 < 0;
    }

    public Location[] getVertices() {
        return vertices;
    }
}
