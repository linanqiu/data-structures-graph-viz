
/*
 * Vertex class implements functionality of a vertex component of a graph.
 * the class contains instance variables and methods associated with this 
 * component.
 * 
 * written by Sasha Beltinova, sab2229
 */

import java.util.ArrayList;

public class Vertex implements Comparable<Vertex> {

  private String name;
  private int x;
  private int y;
  private boolean isKnown;
  private double dist; // total distance from origin point
  private Vertex prevVertex;
  private ArrayList<Edge> adjCities;

  public Vertex(String name, int x, int y) {

    this.name = name;
    this.x = x;
    this.y = y;
    adjCities = new ArrayList<Edge>();
    prevVertex = null;

  }

  public void setKnown(boolean known) {
    isKnown = known;
  }

  public void setDist(double totalDist) {
    dist = totalDist;
  }

  public void setPrevVert(Vertex v) {
    prevVertex = v;
  }

  public void addEdge(Edge edge) {
    adjCities.add(edge);
  }

  public String getCityName() {
    return name;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isKnown() {
    return isKnown;
  }

  public double getDist() {
    return dist;
  }

  public Vertex getPrevVert() {
    return prevVertex;
  }

  public ArrayList<Edge> getAdjacent() {
    return adjCities;
  }

  public int compareTo(Vertex v) {
    if (this.dist < v.dist) {
      return -1;
    } else if (this.dist > v.dist) {
      return 1;
    }

    else {
      return 0;
    }
  }

  public String toString() {
    return name;
  }
}