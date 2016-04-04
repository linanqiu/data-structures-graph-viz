# Graph Viz

![Screenshot 1](./screenshots/ss_1.png)

## Quick Start

Within `src`, `javac *.java`

Then, just `java Display`

There's no need to use command line arguments. Instead, the file is read from
the GUI.

## Assignment Description

In this assignment, you will implement Dijkstra's algorithm. You are given

- `Vertex.java`, a class representing a vertex
- `Edge.java`, a class representing a single edge between two vertices

You should read through the classes and understand them thoroughly as you will be using them extensively throughout the assignment. **However, you do not need to modify them**. If you do modify them, please document it in your `README`.

- `Graph.java` is the class that contains your Dijkstra's algorithm. You are to fill in the following methods:
    - `public double computeEuclideanDistance(double ux, double uy, double vx, double vy)` computes the Euclidean distance between two points [https://www.wikiwand.com/en/Euclidean_distance](https://www.wikiwand.com/en/Euclidean_distance)
    - `public void computeAllEuclideanDistances()` computes the Euclidean distance for all edges in the graph. This should set the `distance` field for all `Edge`s in the graph.
    - `public void doDijkstra(String s)` performs Dijkstra's algorithm starting from the vertex with the name `s`.
    - `public List<Edge> getDijkstraPath(String s, String t)` gets the `List` of `Edge`s of the shortest path from `Vertex` with name `s` to `Vertex` with name `t`.

You can either test `Graph.java` using the console output (which is good for initial debugging). We also included `Display.java`, a GUI for visualizing the output of your graph. You don't need to care about what's inside `Display.java` at all (though it'd be good if you want to learn about Java GUI). Conceptually, you can imagine `Display.java` doing the following:

- Instantiating a new instance of `Graph` and create vertices / edges from the files `cityxy.txt` and `citypairs.txt`. You can change the file names to play around with different graphs using the **Load / Reset** button. i.e.

```java
Graph graph = new Graph();
String line;
// Read in the vertices
BufferedReader vertexFileBr = new BufferedReader(new FileReader(vertexFile));
while ((line = vertexFileBr.readLine()) != null) {
  String[] parts = line.split(",");
  if (parts.length != 3) {
    vertexFileBr.close();
    throw new IOException("Invalid line in vertex file " + line);
  }
  String cityname = parts[0];
  int x = Integer.valueOf(parts[1]);
  int y = Integer.valueOf(parts[2]);
  Vertex vertex = new Vertex(cityname, x, y);
  graph.addVertex(vertex);
}
vertexFileBr.close();

BufferedReader edgeFileBr = new BufferedReader(new FileReader(edgeFile));
while ((line = edgeFileBr.readLine()) != null) {
  String[] parts = line.split(",");
  if (parts.length != 3) {
    edgeFileBr.close();
    throw new IOException("Invalid line in edge file " + line);
  }
  graph.addUndirectedEdge(parts[0], parts[1], Double.parseDouble(parts[2]));
}
edgeFileBr.close();
```

- When you click on **Compute All Euclidean Distances**, it will call `graph.computeAllEuclideanDistances()`. The GUI will show any updates on the `distance` field of `Edge` objects in the `Graph`.
- Clicking on the button `Draw Dijkstra's Path`, it will call `graph.doDijkstra()` with the starting city name as a parameter, **then** highlight the edges returned by `graph.getDijkstraPath()` between the starting city and the ending city. e.g.

```java
String startingCity = // get text from selected value in dropdown
String endingCity = // get text from selected value in other dropdown
graph.doDijkstra(startingCity);
List<Edge> path = graph.getDijkstraPath(startingCity, endingCity);
// draws path
```
