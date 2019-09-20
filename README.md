# PolyRegions
## Table of Contents
* [Overview](#overview)
* [Examples](#examples)
* [Using the API](#using-the-api)
* [How it Works](#how-it-works)

## Overview
PolyRegions is an API for creating polygon and polyhedron regions. The regions can be irregularly shaped, they do not have to be regular polygons/polyhedrons. Polygon regions ignore the y-dimension (up/down) whereas polyhedron regions are enclosed 3D spaces.

## Examples

<p align="center">
  Polygon Region&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;Polyhedron Region
</p>  

<img src="https://i.imgur.com/kLQzIyR.gif" width="425" height="240"/> <img src="https://i.imgur.com/JbcytCi.gif" width="425" height="240"/>

## Using the API
#### Polygon regions:
To create a polygon region you need the locations of the vertices of the polygon (at least 3 vertices are required):
```java
Location[] vetices = ...
PolygonRegion polygonRegion = new PolygonRegion(vetices);
```
To check if a location is in a region, use the contains method:
```java
if (polygonRegion.contains(location)) {
    ...
}
```

#### Polyhedron regions:
To create a polyhedron region you need to define all of the faces of the polyhedron (at least 3 faces are required). Like a polygon, a polyhedron face is defined by 3+ vetices:
```java
PolyhedronFace[] faces = new PolyhedronFace[3];
faces[0] = new PolyhedronFace(face1Vertices);
faces[1] = new PolyhedronFace(face2Vertices);
faces[2] = new PolyhedronFace(face3Vertices);
PolygonHedron polyhedronRegion = new PolygonHedron(faces);
```
PolyhedronRegion has a contains method for checking if a location is in the region as well as a forEachBlock method:
```java
if (polyhedronRegion.contains(location)) {
   ...
}
```
```java
polyhedronRegion.forEachBlock(block -> block.setType(Material.STONE));
```

## How it Works
To check if the a given location is inside a region, the [even-odd rule](https://en.wikipedia.org/wiki/Point_in_polygon) is used. This rule states that if a ray cast from the point intersects the polygon border an even number of times, then the point is outside the polygon, if the number of intersections is odd then the point is inside the region.

![](https://i.imgur.com/vREvsoL.png)

This algorithm also works for polyhedrons, you just count how many times a ray from the point intersects the faces of the polyhedron.
