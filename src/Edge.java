public class Edge {

  public double weight;
  public Vertex source;
  public Vertex target;

  public Edge(Vertex vertex1, Vertex vertex2, double weight) {
    source = vertex1;
    target = vertex2;
    this.weight = weight;
  }

  public String toString() {
    return source + " - " + target;
  }
}