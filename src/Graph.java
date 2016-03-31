/*
 * Graph class implements functionality of a graph, using Edge and Vertex
 * classes. This class also contains a method for implementing Dijkstra's 
 * algorithm for finding the shortest path between two points as well as a
 * method for implementing Kruskal's algorithm for finding the minimum spanning
 * tree in a given graph.
 * 
 * written by Sasha Beltinova, sab2229
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;

public class Graph extends JPanel {

    private ArrayList<String> names;
    private ArrayList<Edge> edges;

    private ArrayList<Vertex> path;
    private ArrayList<Edge> mst;

    private Hashtable<String, Vertex> cities;
    private BinaryHeap<Edge> heap;

    private boolean printAllEdges;
    private double totalDistance;
    private double shortestDistance;
    private String route;
    
    public Graph(ArrayList<String> cityNames, ArrayList<Integer> xy,
            ArrayList<String> pairs, ArrayList<Double> distances) {

        names = cityNames;
        cities = new Hashtable<String, Vertex>();

        // makes a hash of cities and their xy coordinates
        for (int i = 0; i < names.size(); i++) {
            Vertex city = new Vertex(names.get(i), xy.get(i * 2), xy
                    .get(i * 2 + 1));
            cities.put(names.get(i), city);
        }

        // adds edges between specified cities if city pairs are provided to
        // constructor (in a sparse graph)
        if (pairs != null) {

            printAllEdges = true;
            for (int j = 0; j < pairs.size(); j += 2) {

                Vertex v1 = cities.get(pairs.get(j));
                Vertex v2 = cities.get(pairs.get(j + 1));
                Edge edge1 = new Edge(v1, v2, distances.get(j / 2));
                Edge edge2 = new Edge(v2, v1, distances.get(j / 2));
                v1.addEdge(edge1);
                v2.addEdge(edge2);
                totalDistance += distances.get(j / 2);
            }

        // otherwise, assume this is a complete graph
        } else {
            printAllEdges = false;
            edges = new ArrayList<Edge>();

            for (int j = 0; j < names.size(); j++) {
                Vertex v1 = cities.get(names.get(j));

                for (int k = j + 1; k < names.size(); k++) {
                    Vertex v2 = cities.get(names.get(k));
                    Edge edge = new Edge(v1, v2, getEuclidDist(v1, v2));
                    v1.addEdge(edge);
                    edges.add(edge);
                }
            }
        }

    }

    public void paint(Graphics g) {
        
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < names.size(); i++) {
            
            Vertex vertex = cities.get(names.get(i));
            String label = vertex.getCityName();
            int intX = vertex.getX();
            int intY = vertex.getY();
            ArrayList<Edge> adjacent = vertex.getAdjacent();

            // if this is a sparse graph, draw all edges, then draw vertices
            if (printAllEdges) {
                for (int j = 0; j < adjacent.size(); j++) {
                    Edge edge = adjacent.get(j);
                    g2.drawLine((edge.x1 + 5), (edge.y1 + 5), (edge.x2 + 5),
                            (edge.y2 + 5));
                }
            }
            
            // if complete graph, draw vertices only
            g2.drawString(label, intX + 15, intY + 10);
            g2.drawOval(intX, intY, 10, 10);
            g2.setColor(Color.PINK);
            g2.fillOval(intX, intY, 10, 10);
            g2.setColor(Color.BLACK);
        }

        /* if @path param is not null -> performed Dijkstra's Algorithm,
         * show shortest path
        */
        if (path != null) {

            g2.setColor(Color.RED);
            for (int k = 0; k < path.size() - 1; k++) {
                Vertex v1 = path.get(k);
                Vertex v2 = path.get(k + 1);
                g2.drawLine(v1.getX() + 5, v1.getY() + 5, v2.getX() + 5, v2
                        .getY() + 5);
            }
        }
        
        // reset path to allow for new input
        path = null;

        /* if @mst param is not null -> performed Kruskal's Algorithm,
         * show minimum spannign tree
        */
        if (mst != null) {

            g2.setColor(Color.magenta);
            for (int e = 0; e < mst.size(); e++) {
                g2.drawLine(mst.get(e).x1 + 5, mst.get(e).y1 + 5,
                        mst.get(e).x2 + 5, mst.get(e).y2 + 5);
            }
        }

    }

    /*
     * method for implementing Dijkstra's Algorithm using priority queue,
     * based on pseudocode for the same algorithm by Mark Allen Weiss. 
     */
    public void dijkstraAlg(String startCity, String destCity)
            throws UnderflowException {

        printAllEdges = true; // sparse graph, draw all edges
        path = new ArrayList<Vertex>();
        BinaryHeap<Vertex> priorityQ  = new BinaryHeap<Vertex>(names.size());
        Vertex source = cities.get(startCity);
        Vertex destination = cities.get(destCity);
        route = "";

        // reset all vertices
        for (int i = 0; i < names.size(); i++) {
            Vertex v = cities.get(names.get(i));
            v.setKnown(false);
            v.setDist(totalDistance);
            v.setPrevVert(null);
        }

        source.setDist(0);
        source.setKnown(true);
        priorityQ.insert(source);

        while (!priorityQ.isEmpty()) {

            Vertex v = priorityQ.deleteMin();
            ArrayList<Edge> list = v.getAdjacent();

            for (int i = 0; i < list.size(); i++) {

                Edge edge = list.get(i);
                Vertex v2 = edge.v2;

                if (!v2.isKnown()) {
                    double dist = edge.weight;

                    if ((v.getDist() + dist) < v2.getDist()) {
                        v2.setDist(v.getDist() + dist);
                        v2.setPrevVert(v);

                        priorityQ.insert(v2);
                    }
                }
            }
        }
        shortestDistance = destination.getDist();   
        setPath(destination);
    }
    
    /*
     * method for implementing Kruskal's Algorithm for finding the minimum
     * spanning tree, based on pseudocode for the same algorithm by
     * Mark Allen Weiss.
     */
    public void KruskalsAlg() throws UnderflowException {

        printAllEdges = false; // complete graph, don't draw all edges
        mst = new ArrayList<Edge>();
        heap = new BinaryHeap<Edge>(edges.toArray());

        while (mst.size() < (cities.size() - 1)) {

            Edge edge = heap.deleteMin();
            Vertex v1 = edge.v1;
            Vertex v2 = edge.v2;
            int v1Height = 0; // keeps track of height for each vertex
            int v2Height = 0;

            // if both vertices are the roots, the edge is accepted
            if (v1.getPrevVert() == null && v2.getPrevVert() == null) {
                v2.setPrevVert(v1);
                mst.add(edge);
               
            //otherwise, find respective roots and compare    
            } else {              
                while (v1.getPrevVert() != null) {
                    v1 = v1.getPrevVert();
                    v1Height++; 
                }
                while (v2.getPrevVert() != null) {
                    v2 = v2.getPrevVert();
                    v2Height++;
                }
                if (v1 != v2) {
                    // root of 'shorter' vertex is unioned with root of
                    // 'taller' vertex
                    if (v1Height < v2Height) {
                        v1.setPrevVert(v2);
                    } else {
                        v2.setPrevVert(v1);
                    }
                    mst.add(edge);
                }
            }
        }
    }
    
    // method to print shortest path, based on pseudocode by Mark Allen Weiss
    private void setPath(Vertex v) {

        if (v.getPrevVert() != null) {
            setPath(v.getPrevVert());
            path.add(v.getPrevVert());
            route += " to ";
        }
        path.add(v);
        route += v;
    }

    public String getPath() {
        return route;
    }
    
    public double getShortestDist(){
        return shortestDistance;
    }
    
    public String getMST(){       
        String tree = "";
        for (int j = 0; j < mst.size(); j++) {
            tree += "\n" + mst.get(j);
            shortestDistance += mst.get(j).weight;
        }      
        return tree;
    }

    // this method computes Euclidean distance between two vertices
    private double getEuclidDist(Vertex v1, Vertex v2) {      
        double distX = Math.abs(v1.getX() - v2.getX()); // base
        double distY = Math.abs(v1.getY() - v2.getY()); // height
        double sqrSum = (Math.pow(distX, 2) + (Math.pow(distY, 2)));
        double hypotenuse = Math.sqrt(sqrSum);
        return hypotenuse;
    }
}
