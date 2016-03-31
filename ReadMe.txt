Sasha Beltinova
Hwk 5

The Dijkstra's Algorithm

This program was written to implement the Dikstra's Algorithm for finding 
the shortest path between two points. The program uses command line argument 
for input of points/coordinates and distances between said points, and creates 
a GUI window to render the resulting map.

The program allows user to choose the origin and destination cities and then 
calculates and draws the shortest bath between said points.

To run this program, compile the source code, provide the text file containing
city pairs as a command line argument[0] and the text file containing 
xy coordinates as a command line argument[1], and run DijkstrasAlgTest.java.

NOTE: Eclipse could not find an underflow exception used in Weiss's BinaryHeap
code, so I went ahead and wrote a simple one (it is provided with the submitted
hwk directory).
***********

The DijsktrasAlgTest class

This class scans the files provided in argument line and parses them into 
appropriate array lists, i.e. an array list containing just the city names,
and arraylist containing their respective coordinates, etc. The class then
constructs a GUI, rendering the resulting map, and asks user to select the
origin and the destination cities to calculate the shortest distance. This
distance is produced both as a highlighted route on the map and as a list
of cities and total distance in a pop-up window. 

User can select the city pairs multiple times without having to restart the 
program.
*************

the Graph

This class constructs the actual graph and renders it in a window using 
Graphics2D class (specifics of graph rendering, i.e. whether to draw its edges
or not, depends on the type of graph: sparse graphs get rendered in their
entirety, whereas complete graphs are rendered only as vertices).

Depending on the type of algorithm implemented, Dijkstra's or Kruskal's, 
the graph is repainted accordingly, either highlighting a route or a tree.

I decided to store the graph vertices in a hashtable, rather than in an array 
as suggested in our textbook, because doing so allowed for a constant-time
fetching of the vertices when iterating through them.

I chose to implement the algorithms as methods of the Graph class because
it made sense to have all of the variables (i.e. lists of cities, coordinates,
path lengths, etc.) handled by the same class. While it is possible to have
a separate class that implemented these algorithms, passing the many variables
back and forth between classes seemed cumbersome and inefficient.

Dijkstra's Algorithm routine is based on Weiss's pseudocode provided in the 
textbook. The routine uses priority queue implementation with the help of
Weiss's Binary Heap class (class is provided with the rest of the files).
***************

the Vertex

This class implements a simple vertex component of a graph and contains the appropriate instance
variables (e.g. xy coordinates, labels, etc) and methods (setter and getter 
methods for the IVs).
***************

the Edge

This class implements a simple edge component of a graph. It contains the 
necessary variables and setter and getter methods for those variables.
I chose to make the IVs of this class public because doing so made for simpler,
easier-to-read syntax when handing edges.
***************

The Kruskal's Algorithm

This program was written to implement the Kruskal's Algorithm for finding the 
minimum spanning tree (MST). The program uses command line argument for 
points/coordinates input and creates a GUI window to render the graph.
Upon command, it calculates the MST and renders it in a window.

To run this program, compile the source code, provide the text file containing
xy coordinates as a command line argument[0], and run KruskalsAlgTest.java.

NOTE: this program uses the same Graph, Vertex, and Edge classes as 
the Disktra's Algorithm program.
