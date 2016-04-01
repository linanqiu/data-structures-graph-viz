import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Graph {

  // Keep a fast index to nodes in the map
  private Map<String, Vertex> vertexNames;

  /**
   * Construct an empty Graph.
   */
  public Graph() {
    vertexNames = new HashMap<String, Vertex>();
  }

  public void addVertex(Vertex v) {
    if (vertexNames.containsKey(v.name))
      throw new IllegalArgumentException("Cannot create new vertex with existing name.");
    vertexNames.put(v.name, v);
  }

  public Collection<Vertex> getVertices() {
    return vertexNames.values();
  }

  public Vertex getVertex(String s) {
    return vertexNames.get(s);
  }

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

  /****************************
   * Your code follow here. *
   ****************************/

  public void addUndirectedEdge(String s, String t, double cost) {
    addEdge(s, t, cost);
    addEdge(t, s, cost);
  }

  public double computeEuclideanCost(double ux, double uy, double vx, double vy) {
    return Math.sqrt(Math.pow(ux - vx, 2) + Math.pow(uy - vy, 2));
  }

  public void computeAllEuclideanCosts() {
    for (Vertex u : getVertices())
      for (Edge uv : u.adjacentEdges) {
        Vertex v = uv.v2;
        uv.weight = computeEuclideanCost(u.x, u.y, v.x, v.y);
      }
  }

  /** BFS */
  public void doBfs(String s) {
    LinkedList<Vertex> queue = new LinkedList<>();
    for (Vertex u : getVertices()) {
      u.known = false;
      u.dist = Double.POSITIVE_INFINITY;
      u.prevVertex = null;
    }

    Vertex v;
    Vertex u = vertexNames.get(s);
    u.known = true;
    queue.offer(u);

    while (!queue.isEmpty()) {
      u = queue.poll();
      for (Edge uv : u.adjacentEdges) {
        v = uv.v2;
        if (!v.known) {
          v.prevVertex = u;
          v.dist = u.dist + 1.0;
          v.known = true;
          queue.offer(v);
        }
      }
    }
  }

  public List<Edge> getUnweightedShortestPath(String s, String t) {
    doBfs(s);
    List<Edge> result = new LinkedList<>();

    Vertex v = vertexNames.get(t);
    // Follow backpointers and insert new edges
    while (v.prevVertex != null) {
      result.add(new Edge(v.prevVertex, v, 0.0));
      v = v.prevVertex;
    }
    return result;
  }

  /** Dijkstra's */
  public void doDijkstra(String s) {
    PriorityQueue<CostVertex> queue = new PriorityQueue<>();
    for (Vertex u : getVertices()) {
      u.known = false;
      u.dist = Double.POSITIVE_INFINITY;
      u.prevVertex = null;
    }

    Vertex v;
    Vertex u = vertexNames.get(s);
    u.known = true;
    u.dist = 0.0;
    queue.offer(new CostVertex(0.0, u));

    CostVertex next;
    while (!queue.isEmpty()) {
      next = queue.poll();
      u = next.vertex;
      u.known = true;
      for (Edge uv : u.adjacentEdges) {
        v = uv.v2;
        if (!v.known && (u.dist + uv.weight < v.dist)) {
          v.prevVertex = u;
          v.dist = uv.weight + u.dist;
          queue.offer(new CostVertex(v.dist, v));
        }
      }
    }
  }

  public List<Edge> getWeightedShortestPath(String s, String t) {
    doDijkstra(s);

    List<Edge> result = new LinkedList<>();

    Vertex v = vertexNames.get(t);

    // Follow backpointers and insert new edges
    while (v.prevVertex != null) {
      result.add(new Edge(v.prevVertex, v, 1.0));
      v = v.prevVertex;
    }
    return result;
  }

  /** Prim's */
  public void doPrim(String s) {
    PriorityQueue<CostVertex> queue = new PriorityQueue<>();
    for (Vertex u : getVertices()) {
      u.known = false;
      u.dist = Double.POSITIVE_INFINITY;
      u.prevVertex = null;
    }

    Vertex v;
    Vertex u = vertexNames.get(s);
    u.known = true;
    u.dist = 0.0;
    queue.offer(new CostVertex(0.0, u));

    CostVertex next;
    while (!queue.isEmpty()) {
      next = queue.poll();
      u = next.vertex;
      u.known = true;
      for (Edge uv : u.adjacentEdges) {
        v = uv.v2;
        if (!v.known && (uv.weight < v.dist)) {
          v.prevVertex = u;
          v.dist = uv.weight;
          queue.offer(new CostVertex(v.dist, v));
        }
      }
    }
  }

  public List<Edge> getMinimumSpanningTree(String s) {
    doPrim(s);

    List<Edge> result = new LinkedList<>();

    for (Vertex v : getVertices()) {
      if (v.prevVertex != null)
        result.add(new Edge(v, v.prevVertex, 1.0));
    }

    return result;
  }

  /*************************/

  public void printAdjacencyList() {
    for (String u : vertexNames.keySet()) {
      StringBuilder sb = new StringBuilder();
      sb.append(u);
      sb.append(" -> [ ");
      for (Edge e : vertexNames.get(u).adjacentEdges) {
        sb.append(e.v2.name);
        sb.append("(");
        sb.append(e.weight);
        sb.append(") ");
      }
      sb.append("]");
      System.out.println(sb.toString());
    }
  }

  private class CostVertex implements Comparable<CostVertex> {
    double cost;
    Vertex vertex;

    public CostVertex(double cost, Vertex vertex) {
      this.cost = cost;
      this.vertex = vertex;
    }

    public int compareTo(CostVertex o) {
      if (o.cost > this.cost)
        return -1;
      if (o.cost < this.cost)
        return 1;
      return 0;
    }
  }
}
