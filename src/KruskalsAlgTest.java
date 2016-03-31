/*
 * KruskalsAlgTest class contains the main method for implementation
 * of Kruskal's algorithm.
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

public class KruskalsAlgTest {

    public static void main(String[] args) throws FileNotFoundException {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // set up input
        ArrayList<String> cityNames = new ArrayList<String>();
        ArrayList<Integer> xy = new ArrayList<Integer>();

        Scanner cityxy = new Scanner(new File(args[0]));

        while (cityxy.hasNext()) {
            cityNames.add(cityxy.next());
            xy.add(cityxy.nextInt() / 2);
            /*
             * Flips the y-coordinates to correspond with real xy layout, where
             * (0,0) is the lower left corner (by comparison, in the default
             * JFrame layout, (0,0) is in the upper left corner, thus points on
             * a grid appear inverted about y axis).
             */
            xy.add((int) (screenSize.height * 0.75) - (cityxy.nextInt()) / 2);
        }

        // make graph
        final Graph graph = new Graph(cityNames, xy, null, null);

        // set up the GUI
        final JFrame frame = new JFrame("Kruskal's Algorithm");
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Please click 'Go!' to find a "
                + "MinSpanning Tree!");
        JButton goButton = new JButton("Go!");
        
        // add functionality to button: implement Kruskal's Algorithm
        // and repaint the graph
        goButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    graph.KruskalsAlg();
                    graph.repaint();
                    JOptionPane.showMessageDialog(frame,
                            "The MST contains the following edges: "
                                    + graph.getMST() + "\n\nTotal distance is "
                                    + graph.getShortestDist(),
                            "Minimum Spanning Tree", JOptionPane.PLAIN_MESSAGE);
                } catch (UnderflowException e1) {
                    e1.printStackTrace();
                }
            }
        });

        frame.setLayout(new BorderLayout());
        panel.add(label);
        panel.add(goButton);
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.add(graph);
        frame.setSize(screenSize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
