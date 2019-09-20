# PolyRegions
## Table of Contents
* [Overview](#overview)
* [Examples](#examples)
* [Using the API](#using-the-api)
* [How it Works](#how-it-works)

## Overview
PolyRegions is an API for creating polygon and polyhedron regions. The regions can be irregularly shaped, they do not have to be regular polygons/polyhedrons. Polygon regions ignore the z-dimension (up/down) whereas polyhedron regions are 3D spaces.

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

#### Polyhedron regions:
To create a polyhedron region you need to define all of the faces of the polyhedron (at least 3 faces are required). Like a polygon, a polyhedron face is defined by 3+ vetices:
```java
PolyhedronFace[] faces = new PolyhedronFace[3];
faces[0] = new PolyhedronFace(face1Vertices);
faces[1] = new PolyhedronFace(face2Vertices);
faces[2] = new PolyhedronFace(face3Vertices);
PolygonHedron polyhedronRegion = new PolygonHedron(faces);
```

## How it Works
