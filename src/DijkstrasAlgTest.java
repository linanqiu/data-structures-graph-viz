/*
 * DijkstrasAlgTest class contains the main method for implementation
 * of Dijkstra's algorithm.
 * 
 * written by Sasha Beltinova, sab2229
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.*;

public class DijkstrasAlgTest {

    public static void main(String[] args) throws FileNotFoundException {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // set up input
        ArrayList<String> cityNames = new ArrayList<String>();
        ArrayList<Integer> xy = new ArrayList<Integer>();
        Scanner cityPairs = new Scanner(new File(args[0]));
        Scanner cityXY = new Scanner(new File(args[1]));

        ArrayList<String> pairs = new ArrayList<String>();
        ArrayList<Double> distances = new ArrayList<Double>();

        while (cityPairs.hasNext()) {
            pairs.add(cityPairs.next());
            pairs.add(cityPairs.next());
            distances.add(cityPairs.nextDouble());
        }

        while (cityXY.hasNext()) {
            String city = cityXY.next();
            if (pairs.contains(city)) {
                cityNames.add(city);
                xy.add(cityXY.nextInt() / 2);
                /*
                 * Flips the y-coordinates to correspond with real xy layout,
                 * where (0,0) is the lower left corner (by comparison, in the
                 * default JFrame layout, (0,0) is in the upper left corner,
                 * thus points on a grid appear inverted about y axis).
                 */
                xy.add((int) (screenSize.height * 0.75)
                        - (cityXY.nextInt() / 2));
            }

        }

        // make the graph
        final Graph graph = new Graph(cityNames, xy, pairs, distances);

        // set up the GUI
        final JFrame frame = new JFrame("Dijkstra's Algorithm");
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel instruction = new JLabel("Pick Origin and Destination Cities");
        final JComboBox firstCity = new JComboBox(cityNames.toArray());
        final JComboBox secondCity = new JComboBox(cityNames.toArray());
        JButton goButton = new JButton("Find shortest path!");

        // add functionality to button: implement Dijkstra's Algorithm
        // and repaint the graph
        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object origin = (String) firstCity.getSelectedItem();
                Object destination = (String) secondCity.getSelectedItem();
                try {
                    graph.dijkstraAlg(origin.toString(), destination.toString());
                    graph.repaint();

                    if (origin.equals(destination)) {
                        JOptionPane.showMessageDialog(frame,
                                "Good news: you don't have to travel, "
                                        + "you're already there!",
                                "The Shortest Path", JOptionPane.PLAIN_MESSAGE);
                    } else {

                        JOptionPane.showMessageDialog(frame,
                                "The shortest path is from " + graph.getPath()
                                        + "\n\nThe total distance is "
                                        + graph.getShortestDist(),
                                "The Shortest Path", JOptionPane.PLAIN_MESSAGE);
                    }
                } catch (UnderflowException e1) {
                    e1.printStackTrace();
                }
            }

        });

        frame.setLayout(new BorderLayout());
        panel.add(instruction);
        panel.add(firstCity);
        panel.add(secondCity);
        panel.add(goButton);
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.add(graph);
        frame.setSize(screenSize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
