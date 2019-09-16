package us.donut.polygonregions;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class PolygonRegion {

    private List<Corner> corners = new ArrayList<>();

    public void addCorner(Location loc) {
        corners.add(new Corner(loc.getX(), loc.getZ()));
    }

    public boolean contains(Location loc) {

        // if the number of intersected sides is odd, the polygon contains the location
        int intersectedSides = 0;

        // loop through all the corners
        for (int i = 0; i < corners.size(); i++) {

            // get 2 connected corners
            Corner corner1 = corners.get(i);
            Corner corner2 = corners.get(i + 1 < corners.size() ? i + 1 : 0);

            // setup variables
            double x1 = corner1.x;
            double x2 = corner2.x;
            double z1 = corner1.z;
            double z2 = corner2.z;

            // (x,z) = intersection between horizontal line at location's z-coord and the line connecting the corners
            double x; // don't know yet
            double z = loc.getZ();

            // calculate slope of the line connecting the corners
            // m = (z2 - z1) / (x2 - x1)
            double deltaZ = z2 - z1;
            double deltaX = x2 - x1;

            if (deltaZ == 0) {
                // horizontal line at location is parallel to the line connecting the corners so can't intersect
                continue;
            }

            if (deltaX == 0) {
                // vertical line so intersection x-coord is the same as the corner's x-coord
                x = x1;
            } else {
                // calculate intersection x-coord
                // z - z1 = m(x - x1)
                // x = x1 + ((z - z1) / m)
                double m = deltaZ / deltaX;
                x = x1 + ((z - z1) / m);
            }

            if (z >= Math.min(z1, z2) && z <= Math.max(z1, z2)) { // if z-coord is between the two corners
                if (x >= Math.min(x1, x2) && x <= Math.max(x1, x2)) { // if x-coord is between the two corners
                    if (x >= loc.getX()) { // we only care about intersections to the right of the location in question
                        intersectedSides++;
                    }
                }
            }

        }

        // return whether or not the number of intersected sides is odd
        return intersectedSides % 2 == 1;
    }

    public static class Corner {

        private double x;
        private double z;

        public Corner(double x, double z) {
            this.x = x;
            this.z = z;
        }
    }
}
