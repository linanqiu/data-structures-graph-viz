/*
 * Edge class implements functionality of a node component of a graph.
 * the class contains instance variables and methods associated with this 
 * component.
 * 
 * written by Sasha Beltinova, sab2229
 */

public class Edge implements Comparable<Edge> {

    public int x1;
    public int x2;
    public int y1;
    public int y2;
    public double weight;
    public Vertex v1;
    public Vertex v2;

    public Edge(Vertex vertex1, Vertex vertex2, double distance) {

        v1 = vertex1;
        v2 = vertex2;
        x1 = vertex1.getX();
        y1 = vertex1.getY();
        x2 = vertex2.getX();
        y2 = vertex2.getY();
        weight = distance;

    }
    
    public Vertex getV1(){
        return v1;
    }

    public Vertex getV2() {
        return v2;
    }

    public int compareTo(Edge e) {
        if (this.weight < e.weight) {
            return -1;
        } else if (this.weight > e.weight) {
            return 1;
        } else
            return 0;
    }

    public String toString() {
        return v1 + " " + v2;
    }

}