import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Graph {

  // Keep a fast index to nodes in the map
  private Map<String, Vertex> vertexNames;

  /**
   * Construct an empty Graph with a map. The map's key is the name of a vertex
   * and the map's value is the vertex object.
   */
  public Graph() {
    vertexNames = new HashMap<String, Vertex>();
  }

  /**
   * Adds a vertex to the graph. Throws IllegalArgumentException if two vertices
   * with the same name are added.
   * 
   * @param v
   *          (Vertex) vertex to be added to the graph
   */
  public void addVertex(Vertex v) {
    if (vertexNames.containsKey(v.name))
      throw new IllegalArgumentException("Cannot create new vertex with existing name.");
    vertexNames.put(v.name, v);
  }

  /**
   * Gets a collection of all the vertices in the graph
   * 
   * @return (Collection<Vertex>) collection of all the vertices in the graph
   */
  public Collection<Vertex> getVertices() {
    return vertexNames.values();
  }

  /**
   * Gets the vertex object with the given name
   * 
   * @param name
   *          (String) name of the vertex object requested
   * @return (Vertex) vertex object associated with the name
   */
  public Vertex getVertex(String name) {
    return vertexNames.get(name);
  }

  /**
   * Adds a directed edge from vertex u to vertex v
   * 
   * @param nameU
   *          (String) name of vertex u
   * @param nameV
   *          (String) name of vertex v
   * @param cost
   *          (double) cost of the edge between vertex u and v
   */
  public void addEdge(String nameU, String nameV, Double cost) {
    if (!vertexNames.containsKey(nameU))
      throw new IllegalArgumentException(nameU + " does not exist. Cannot create edge.");
    if (!vertexNames.containsKey(nameV))
      throw new IllegalArgumentException(nameV + " does not exist. Cannot create edge.");
    Vertex sourceVertex = vertexNames.get(nameU);
    Vertex targetVertex = vertexNames.get(nameV);
    Edge newEdge = new Edge(sourceVertex, targetVertex, cost);
    sourceVertex.addEdge(newEdge);
  }

  /**
   * Adds an undirected edge between vertex u and vertex v by adding a directed
   * edge from u to v, then a directed edge from v to u
   * 
   * @param nameU
   *          (String) name of vertex u
   * @param nameV
   *          (String) name of vertex v
   * @param cost
   *          (double) cost of the edge between vertex u and v
   */
  public void addUndirectedEdge(String nameU, String nameV, double cost) {
    addEdge(nameU, nameV, cost);
    addEdge(nameV, nameU, cost);
  }

  // STUDENT CODE STARTS HERE

  /**
   * Computes the euclidean distance between two points as described by their
   * coordinates
   * 
   * @param ux
   *          (double) x coordinate of point u
   * @param uy
   *          (double) y coordinate of point u
   * @param vx
   *          (double) x coordinate of point v
   * @param vy
   *          (double) y coordinate of point v
   * @return (double) distance between the two points
   */
  public double computeEuclideanDistance(double ux, double uy, double vx, double vy) {
    return Math.sqrt(Math.pow(ux - vx, 2) + Math.pow(uy - vy, 2));
  }

  /**
   * Computes euclidean distance between two vertices as described by their
   * coordinates
   * 
   * @param u
   *          (Vertex) vertex u
   * @param v
   *          (Vertex) vertex v
   * @return (double) distance between two vertices
   */
  public double computeEuclideanDistance(Vertex u, Vertex v) {
    return computeEuclideanDistance(u.x, u.y, v.x, v.y);
  }

  /**
   * Calculates the euclidean distance for all edges in the map using the
   * computeEuclideanCost method.
   */
  public void computeAllEuclideanDistances() {
    for (Vertex u : getVertices())
      for (Edge uv : u.adjacentEdges) {
        Vertex v = uv.target;
        uv.distance = computeEuclideanDistance(u.x, u.y, v.x, v.y);
      }
  }

  public void generateRandomVertices(int n) {
    vertexNames = new HashMap<>();
    Random random = new Random();
    for (int i = 0; i < n; i++) {
      int x = random.nextInt(100);
      int y = random.nextInt(100);
      Vertex vertex = new Vertex(Integer.toString(i), x, y);
      vertexNames.put(Integer.toString(i), vertex);
    }

    for (Vertex u : getVertices()) {
      for (Vertex v : getVertices()) {
        if (u != v) {
          addUndirectedEdge(u.name, v.name, 0);
        }
      }
    }

    computeAllEuclideanDistances();
  }

  public void doNearestNeighbor() {
    for (Vertex vertex : getVertices()) {
      vertex.known = false;
    }

    Vertex start = vertexNames.get("0");
    start.known = true;
    Vertex current = start;

    while (true) {
      List<Edge> unknownEdges = current.adjacentEdges.stream().filter(x -> !x.target.known)
          .collect(Collectors.toList());
      if (unknownEdges.isEmpty()) {
        start.prev = current;
        return;
      }
      Edge shortestEdge = Collections.min(unknownEdges, Comparator.comparing(x -> x.distance));
      shortestEdge.target.prev = shortestEdge.source;
      current = shortestEdge.target;
      current.known = true;
    }
  }

  public List<Edge> getNearestNeighborPath() {
    if (vertexNames.size() == 1) {
      return null;
    }

    List<Edge> edges = new LinkedList<>();

    Vertex start = vertexNames.get("0");
    Vertex current = start.prev;
    edges.add(new Edge(start, current, computeEuclideanDistance(start, current)));

    while (current != start) {
      edges.add(new Edge(current, current.prev, computeEuclideanDistance(current, current.prev)));
      current = current.prev;
    }

    return edges;
  }

  public void doBruteForce() {
    doNearestNeighbor();
  }

  public List<Edge> getBruteForcePath() {
    return getNearestNeighborPath();
  }

  // STUDENT CODE ENDS HERE

  /**
   * Prints out the adjacency list of the graph for debugging
   */
  public void printAdjacencyList() {
    for (String u : vertexNames.keySet()) {
      StringBuilder sb = new StringBuilder();
      sb.append(u);
      sb.append(" -> [ ");
      for (Edge e : vertexNames.get(u).adjacentEdges) {
        sb.append(e.target.name);
        sb.append("(");
        sb.append(e.distance);
        sb.append(") ");
      }
      sb.append("]");
      System.out.println(sb.toString());
    }
  }
}
