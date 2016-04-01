public class Edge implements Comparable<Edge> {

  public int x1;
  public int x2;
  public int y1;
  public int y2;
  public double weight;
  public Vertex v1;
  public Vertex v2;

  public Edge(Vertex vertex1, Vertex vertex2, double weight) {
    v1 = vertex1;
    v2 = vertex2;
    x1 = vertex1.x;
    y1 = vertex1.y;
    x2 = vertex2.x;
    y2 = vertex2.y;
    this.weight = weight;
  }

  @Override
  public int compareTo(Edge o) {
    return Double.compare(weight, o.weight);
  }

  public String toString() {
    return v1 + " - " + v2;
  }
}