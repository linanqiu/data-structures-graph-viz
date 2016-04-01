import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Display extends JFrame {

  private JPanel contentPane;
  private JTextField mstCityTextField;
  private JTextField txtCityxytxt;
  private JTextField txtCitypairstxt;
  private GraphPanel panel;
  private JComboBox<String> weightedStartCityComboBox;
  private JComboBox<String> weightedEndCityComboBox;
  private JComboBox<String> unweightedEndCityComboBox;
  private JComboBox<String> unweightedStartCityComboBox;
  private Graph graph;
  private JCheckBox chckbxShowEdgeWeights;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Display frame = new Display();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public static Graph readGraph(String vertexFile, String edgeFile) {

    Graph graph = new Graph();
    try {
      String line;
      // Read in the vertices
      BufferedReader vertexFileBr = new BufferedReader(new FileReader(vertexFile));
      while ((line = vertexFileBr.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length != 3) {
          vertexFileBr.close();
          throw new IOException("Invalid line in vertex file " + line);
        }
        String cityname = parts[0];
        int x = Integer.valueOf(parts[1]);
        int y = Integer.valueOf(parts[2]);
        Vertex vertex = new Vertex(cityname, x, y);
        graph.addVertex(vertex);
      }
      vertexFileBr.close();
      // Now read in the edges
      BufferedReader edgeFileBr = new BufferedReader(new FileReader(edgeFile));
      while ((line = edgeFileBr.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length != 3) {
          edgeFileBr.close();
          throw new IOException("Invalid line in edge file " + line);
        }
        graph.addUndirectedEdge(parts[0], parts[1], Double.parseDouble(parts[2]));
      }
      edgeFileBr.close();
    } catch (IOException e) {
      System.err.println("Could not read the graph: " + e);
      return null;
    }
    return graph;
  }

  /**
   * Create the frame.
   */
  public Display() {
    setTitle("Data Structures Graph Visualizer");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(50, 50, 900, 700);
    setMinimumSize(new Dimension(800, 600));
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    GridBagLayout gbl_contentPane = new GridBagLayout();
    gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
    gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
    gbl_contentPane.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
    contentPane.setLayout(gbl_contentPane);

    graph = readGraph("cityxy.txt", "citypairs.txt");

    panel = new GraphPanel(graph);
    GridBagConstraints gbc_panel = new GridBagConstraints();
    gbc_panel.gridwidth = 7;
    gbc_panel.insets = new Insets(0, 0, 7, 0);
    gbc_panel.fill = GridBagConstraints.BOTH;
    gbc_panel.gridx = 0;
    gbc_panel.gridy = 0;
    contentPane.add(panel, gbc_panel);

    JSeparator separator = new JSeparator();
    separator.setBackground(Color.WHITE);
    separator.setForeground(Color.GRAY);
    GridBagConstraints gbc_separator = new GridBagConstraints();
    gbc_separator.fill = GridBagConstraints.BOTH;
    gbc_separator.gridwidth = 6;
    gbc_separator.insets = new Insets(0, 0, 5, 0);
    gbc_separator.gridx = 0;
    gbc_separator.gridy = 2;
    contentPane.add(separator, gbc_separator);
    
        chckbxShowEdgeWeights = new JCheckBox("Show Edge Weights");
        chckbxShowEdgeWeights.addItemListener(new ItemListener() {
          public void itemStateChanged(ItemEvent e) {
            repaint();
          }
        });
        
            JLabel lblReloadGraph = new JLabel("Load / Reload Graph");
            GridBagConstraints gbc_lblReloadGraph = new GridBagConstraints();
            gbc_lblReloadGraph.anchor = GridBagConstraints.EAST;
            gbc_lblReloadGraph.insets = new Insets(0, 0, 5, 5);
            gbc_lblReloadGraph.gridx = 0;
            gbc_lblReloadGraph.gridy = 3;
            contentPane.add(lblReloadGraph, gbc_lblReloadGraph);
        
            txtCityxytxt = new JTextField();
            txtCityxytxt.setText("cityxy.txt");
            GridBagConstraints gbc_txtCityxytxt = new GridBagConstraints();
            gbc_txtCityxytxt.gridwidth = 2;
            gbc_txtCityxytxt.insets = new Insets(0, 0, 5, 5);
            gbc_txtCityxytxt.fill = GridBagConstraints.HORIZONTAL;
            gbc_txtCityxytxt.gridx = 1;
            gbc_txtCityxytxt.gridy = 3;
            contentPane.add(txtCityxytxt, gbc_txtCityxytxt);
            txtCityxytxt.setColumns(10);
        
            txtCitypairstxt = new JTextField();
            txtCitypairstxt.setText("citypairs.txt");
            GridBagConstraints gbc_txtCitypairstxt = new GridBagConstraints();
            gbc_txtCitypairstxt.gridwidth = 2;
            gbc_txtCitypairstxt.insets = new Insets(0, 0, 5, 5);
            gbc_txtCitypairstxt.fill = GridBagConstraints.HORIZONTAL;
            gbc_txtCitypairstxt.gridx = 3;
            gbc_txtCitypairstxt.gridy = 3;
            contentPane.add(txtCitypairstxt, gbc_txtCitypairstxt);
            txtCitypairstxt.setColumns(10);
        
            JButton btnReloadGraph = new JButton("Load / Reset");
            GridBagConstraints gbc_btnReloadGraph = new GridBagConstraints();
            gbc_btnReloadGraph.fill = GridBagConstraints.HORIZONTAL;
            gbc_btnReloadGraph.insets = new Insets(0, 0, 5, 0);
            gbc_btnReloadGraph.gridx = 5;
            gbc_btnReloadGraph.gridy = 3;
            contentPane.add(btnReloadGraph, gbc_btnReloadGraph);
            
                btnReloadGraph.addMouseListener(new MouseAdapter() {
                  @Override
                  public void mouseReleased(MouseEvent e) {
                    // update JPanel
                    updateGraphPanel();
                  }
                });
        
        JLabel lblEuclideanCosts = new JLabel("Euclidean Costs");
        GridBagConstraints gbc_lblEuclideanCosts = new GridBagConstraints();
        gbc_lblEuclideanCosts.anchor = GridBagConstraints.EAST;
        gbc_lblEuclideanCosts.insets = new Insets(0, 0, 5, 5);
        gbc_lblEuclideanCosts.gridx = 0;
        gbc_lblEuclideanCosts.gridy = 4;
        contentPane.add(lblEuclideanCosts, gbc_lblEuclideanCosts);
        chckbxShowEdgeWeights.setSelected(true);
        GridBagConstraints gbc_chckbxShowEdgeWeights = new GridBagConstraints();
        gbc_chckbxShowEdgeWeights.anchor = GridBagConstraints.EAST;
        gbc_chckbxShowEdgeWeights.gridwidth = 4;
        gbc_chckbxShowEdgeWeights.insets = new Insets(0, 0, 5, 5);
        gbc_chckbxShowEdgeWeights.gridx = 1;
        gbc_chckbxShowEdgeWeights.gridy = 4;
        contentPane.add(chckbxShowEdgeWeights, gbc_chckbxShowEdgeWeights);
    
    JButton btnComputeAllEuclidean = new JButton("Compute All Euclidean Costs");
    GridBagConstraints gbc_btnComputeAllEuclidean = new GridBagConstraints();
    gbc_btnComputeAllEuclidean.fill = GridBagConstraints.BOTH;
    gbc_btnComputeAllEuclidean.insets = new Insets(0, 0, 5, 0);
    gbc_btnComputeAllEuclidean.gridx = 5;
    gbc_btnComputeAllEuclidean.gridy = 4;
    contentPane.add(btnComputeAllEuclidean, gbc_btnComputeAllEuclidean);
    
    btnComputeAllEuclidean.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        panel.graph.computeAllEuclideanCosts();
        repaint();
      }
    });

    JLabel lblGetUnweightedShortest = new JLabel("Get Unweighted Shortest Path");
    GridBagConstraints gbc_lblGetUnweightedShortest = new GridBagConstraints();
    gbc_lblGetUnweightedShortest.anchor = GridBagConstraints.EAST;
    gbc_lblGetUnweightedShortest.insets = new Insets(0, 0, 5, 5);
    gbc_lblGetUnweightedShortest.gridx = 0;
    gbc_lblGetUnweightedShortest.gridy = 5;
    contentPane.add(lblGetUnweightedShortest, gbc_lblGetUnweightedShortest);

    JLabel lblStart = new JLabel("Start");
    GridBagConstraints gbc_lblStart = new GridBagConstraints();
    gbc_lblStart.insets = new Insets(0, 0, 5, 5);
    gbc_lblStart.anchor = GridBagConstraints.EAST;
    gbc_lblStart.gridx = 1;
    gbc_lblStart.gridy = 5;
    contentPane.add(lblStart, gbc_lblStart);

    unweightedStartCityComboBox = new JComboBox<>();
    unweightedStartCityComboBox.setToolTipText("");
    GridBagConstraints gbc_unweightedStartCityComboBox = new GridBagConstraints();
    gbc_unweightedStartCityComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_unweightedStartCityComboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_unweightedStartCityComboBox.gridx = 2;
    gbc_unweightedStartCityComboBox.gridy = 5;
    contentPane.add(unweightedStartCityComboBox, gbc_unweightedStartCityComboBox);

    JLabel lblEnd = new JLabel("End");
    GridBagConstraints gbc_lblEnd = new GridBagConstraints();
    gbc_lblEnd.insets = new Insets(0, 0, 5, 5);
    gbc_lblEnd.anchor = GridBagConstraints.EAST;
    gbc_lblEnd.gridx = 3;
    gbc_lblEnd.gridy = 5;
    contentPane.add(lblEnd, gbc_lblEnd);

    unweightedEndCityComboBox = new JComboBox<>();
    GridBagConstraints gbc_unweightedEndCityComboBox = new GridBagConstraints();
    gbc_unweightedEndCityComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_unweightedEndCityComboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_unweightedEndCityComboBox.gridx = 4;
    gbc_unweightedEndCityComboBox.gridy = 5;
    contentPane.add(unweightedEndCityComboBox, gbc_unweightedEndCityComboBox);

    JButton btnDrawUnweightedShortest = new JButton("Draw Unweighted");
    btnDrawUnweightedShortest.setForeground(Color.RED);
    GridBagConstraints gbc_btnDrawUnweightedShortest = new GridBagConstraints();
    gbc_btnDrawUnweightedShortest.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnDrawUnweightedShortest.insets = new Insets(0, 0, 5, 0);
    gbc_btnDrawUnweightedShortest.gridx = 5;
    gbc_btnDrawUnweightedShortest.gridy = 5;
    contentPane.add(btnDrawUnweightedShortest, gbc_btnDrawUnweightedShortest);

    JLabel lblGetWeightedShortest_1 = new JLabel("Get Weighted Shortest Path");
    GridBagConstraints gbc_lblGetWeightedShortest_1 = new GridBagConstraints();
    gbc_lblGetWeightedShortest_1.anchor = GridBagConstraints.EAST;
    gbc_lblGetWeightedShortest_1.insets = new Insets(0, 0, 5, 5);
    gbc_lblGetWeightedShortest_1.gridx = 0;
    gbc_lblGetWeightedShortest_1.gridy = 6;
    contentPane.add(lblGetWeightedShortest_1, gbc_lblGetWeightedShortest_1);

    JLabel lblStart_1 = new JLabel("Start");
    GridBagConstraints gbc_lblStart_1 = new GridBagConstraints();
    gbc_lblStart_1.insets = new Insets(0, 0, 5, 5);
    gbc_lblStart_1.anchor = GridBagConstraints.EAST;
    gbc_lblStart_1.gridx = 1;
    gbc_lblStart_1.gridy = 6;
    contentPane.add(lblStart_1, gbc_lblStart_1);

    weightedStartCityComboBox = new JComboBox<>();
    weightedStartCityComboBox.setToolTipText("");
    GridBagConstraints gbc_weightedStartCityComboBox = new GridBagConstraints();
    gbc_weightedStartCityComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_weightedStartCityComboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_weightedStartCityComboBox.gridx = 2;
    gbc_weightedStartCityComboBox.gridy = 6;
    contentPane.add(weightedStartCityComboBox, gbc_weightedStartCityComboBox);

    JLabel lblEnd_1 = new JLabel("End");
    GridBagConstraints gbc_lblEnd_1 = new GridBagConstraints();
    gbc_lblEnd_1.insets = new Insets(0, 0, 5, 5);
    gbc_lblEnd_1.anchor = GridBagConstraints.EAST;
    gbc_lblEnd_1.gridx = 3;
    gbc_lblEnd_1.gridy = 6;
    contentPane.add(lblEnd_1, gbc_lblEnd_1);

    weightedEndCityComboBox = new JComboBox<>();
    GridBagConstraints gbc_weightedEndCityComboBox = new GridBagConstraints();
    gbc_weightedEndCityComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_weightedEndCityComboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_weightedEndCityComboBox.gridx = 4;
    gbc_weightedEndCityComboBox.gridy = 6;
    contentPane.add(weightedEndCityComboBox, gbc_weightedEndCityComboBox);

    JButton btnDrawWeightedShortest = new JButton("Draw Weighted");
    btnDrawWeightedShortest.setForeground(Color.GREEN);
    GridBagConstraints gbc_btnDrawWeightedShortest = new GridBagConstraints();
    gbc_btnDrawWeightedShortest.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnDrawWeightedShortest.insets = new Insets(0, 0, 5, 0);
    gbc_btnDrawWeightedShortest.gridx = 5;
    gbc_btnDrawWeightedShortest.gridy = 6;
    contentPane.add(btnDrawWeightedShortest, gbc_btnDrawWeightedShortest);

    JLabel lblGetMinimumSpanning = new JLabel("Get Minimum Spanning Tree");
    GridBagConstraints gbc_lblGetMinimumSpanning = new GridBagConstraints();
    gbc_lblGetMinimumSpanning.anchor = GridBagConstraints.EAST;
    gbc_lblGetMinimumSpanning.insets = new Insets(0, 0, 5, 5);
    gbc_lblGetMinimumSpanning.gridx = 0;
    gbc_lblGetMinimumSpanning.gridy = 7;
    contentPane.add(lblGetMinimumSpanning, gbc_lblGetMinimumSpanning);

    mstCityTextField = new JTextField();
    GridBagConstraints gbc_mstCityTextField = new GridBagConstraints();
    gbc_mstCityTextField.gridwidth = 4;
    gbc_mstCityTextField.insets = new Insets(0, 0, 5, 5);
    gbc_mstCityTextField.fill = GridBagConstraints.HORIZONTAL;
    gbc_mstCityTextField.gridx = 1;
    gbc_mstCityTextField.gridy = 7;
    contentPane.add(mstCityTextField, gbc_mstCityTextField);
    mstCityTextField.setColumns(10);

    JButton btnDrawMst = new JButton("Draw MST");
    btnDrawMst.setForeground(Color.BLUE);
    GridBagConstraints gbc_btnDrawMst = new GridBagConstraints();
    gbc_btnDrawMst.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnDrawMst.insets = new Insets(0, 0, 5, 0);
    gbc_btnDrawMst.gridx = 5;
    gbc_btnDrawMst.gridy = 7;
    contentPane.add(btnDrawMst, gbc_btnDrawMst);

    JLabel lblPanda = new JLabel("Panda 2016 <http://linanqiu.github.io/>");
    lblPanda.setForeground(UIManager.getColor("TextField.light"));
    lblPanda.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        try {
          Desktop.getDesktop().browse(new URI("http://linanqiu.github.io/"));
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        } catch (URISyntaxException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });

    JSeparator separator_1 = new JSeparator();
    separator_1.setForeground(Color.GRAY);
    separator_1.setBackground(Color.WHITE);
    GridBagConstraints gbc_separator_1 = new GridBagConstraints();
    gbc_separator_1.fill = GridBagConstraints.BOTH;
    gbc_separator_1.gridwidth = 6;
    gbc_separator_1.insets = new Insets(0, 0, 5, 0);
    gbc_separator_1.gridx = 0;
    gbc_separator_1.gridy = 8;
    contentPane.add(separator_1, gbc_separator_1);
    GridBagConstraints gbc_lblPanda = new GridBagConstraints();
    gbc_lblPanda.gridwidth = 3;
    gbc_lblPanda.anchor = GridBagConstraints.EAST;
    gbc_lblPanda.gridx = 3;
    gbc_lblPanda.gridy = 9;
    contentPane.add(lblPanda, gbc_lblPanda);

    btnDrawUnweightedShortest.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        String startCity = unweightedStartCityComboBox.getItemAt(unweightedStartCityComboBox.getSelectedIndex());
        String endCity = unweightedEndCityComboBox.getItemAt(unweightedEndCityComboBox.getSelectedIndex());
        System.out.println("Calculating shortest unweighted path for " + startCity + " to " + endCity);
        List<Edge> unweightedPath = graph.getUnweightedShortestPath(startCity, endCity);
        panel.overlayEdges.put("unweighted", unweightedPath);
        repaint();
      }
    });

    btnDrawWeightedShortest.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        String startCity = weightedStartCityComboBox.getItemAt(weightedStartCityComboBox.getSelectedIndex());
        String endCity = weightedEndCityComboBox.getItemAt(weightedEndCityComboBox.getSelectedIndex());
        System.out.println("Calculating shortest weighted path for " + startCity + " to " + endCity);
        List<Edge> weightedPath = graph.getWeightedShortestPath(startCity, endCity);
        panel.overlayEdges.put("weighted", weightedPath);
        repaint();
      }
    });

    btnDrawMst.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        String startCity = mstCityTextField.getText();
        System.out.println("Calculating minimum spanning tree starting at " + startCity);
        List<Edge> weightedPath = graph.getMinimumSpanningTree(startCity);
        panel.overlayEdges.put("mst", weightedPath);
        repaint();
      }
    });

    updateGraphPanel();
  }

  private void updateGraphPanel() {
    String vertexFile = txtCityxytxt.getText();
    String edgeFile = txtCitypairstxt.getText();
    graph = readGraph(vertexFile, edgeFile);
    panel.graph = graph;
    System.out.println("Constructing new file from " + vertexFile + " and " + edgeFile);
    System.out.println("Data read: " + panel.graph.getVertices());

    String[] cityNames = graph.getVertices().parallelStream().map(v -> v.name).sorted().toArray(String[]::new);

    unweightedStartCityComboBox.setModel(new DefaultComboBoxModel<>(cityNames));
    unweightedEndCityComboBox.setModel(new DefaultComboBoxModel<>(cityNames));
    weightedStartCityComboBox.setModel(new DefaultComboBoxModel<>(cityNames));
    weightedEndCityComboBox.setModel(new DefaultComboBoxModel<>(cityNames));

    panel.overlayEdges.put("weighted", new LinkedList<Edge>());
    panel.overlayEdges.put("unweighted", new LinkedList<Edge>());
    panel.overlayEdges.put("mst", new LinkedList<Edge>());

    mstCityTextField.setText(cityNames[0]);

    repaint();
  }

  public class GraphPanel extends JPanel {

    // graph layout parameters
    public static final int VERTEX_RADIUS = 10;
    public static final int SPACE = 3;

    public static final int MARGIN_X = 50;
    public static final int MARGIN_Y = 50;

    public static final int DEFAULT_THICKNESS = 1;

    // scale factors
    public float xFactor, yFactor;

    public Graph graph;

    public HashMap<String, List<Edge>> overlayEdges;

    public GraphPanel(Graph graph) {
      this.graph = graph;
      overlayEdges = new HashMap<>();
      overlayEdges.put("weighted", new LinkedList<Edge>());
      overlayEdges.put("unweighted", new LinkedList<Edge>());
      overlayEdges.put("mst", new LinkedList<Edge>());
    }

    public void paintComponent(Graphics g) {
      // make everything smooth like butter
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

      // scale the graph
      int minX = 0;
      int maxX = 1;
      int minY = 0;
      int maxY = 1;
      for (Vertex v : graph.getVertices()) {
        if (v.x < minX)
          minX = v.x;
        if (v.x > maxX)
          maxX = v.x;
        if (v.y < minY)
          minY = v.y;
        if (v.y > maxY)
          maxY = v.y;
      }
      xFactor = (this.getBounds().width - 2 * MARGIN_X) / (float) (maxX - minX);
      yFactor = (this.getBounds().height - 2 * MARGIN_Y) / (float) (maxY - minY);
      super.paintComponent(g2); // paint the panel
      paintGraph(g2); // paint the graph
    }

    public void paintGraph(Graphics g) {
      for (Vertex v : graph.getVertices()) {
        for (Edge edge : v.adjacentEdges) {
          paintEdge(g, edge.source, edge.target, edge.weight, Color.LIGHT_GRAY, DEFAULT_THICKNESS, 255);
        }
      }
      for (Vertex v : graph.getVertices()) {
        paintVertex(g, v);
      }
      for (String overlayType : overlayEdges.keySet()) {
        if (overlayType.equals("unweighted")) {
          for (Edge edge : overlayEdges.get(overlayType)) {
            paintEdge(g, edge.source, edge.target, edge.weight, Color.RED, 8, 50);
          }
        }
        if (overlayType.equals("weighted")) {
          for (Edge edge : overlayEdges.get(overlayType)) {
            paintEdge(g, edge.source, edge.target, edge.weight, Color.GREEN, 8, 50);
          }
        }
        if (overlayType.equals("mst")) {
          for (Edge edge : overlayEdges.get(overlayType)) {
            paintEdge(g, edge.source, edge.target, edge.weight, Color.BLUE, 8, 50);
          }
        }
      }
    }

    public void paintVertex(Graphics g, Vertex v) {
      Graphics2D g2 = (Graphics2D) g;

      int x = Math.round(xFactor * (float) v.x + (float) MARGIN_X);
      int y = Math.round(yFactor * (float) v.y + (float) MARGIN_Y);
      g2.setColor(Color.LIGHT_GRAY);
      Stroke oldStroke = g2.getStroke();
      g2.setStroke(new BasicStroke(4));
      g2.drawOval(x - VERTEX_RADIUS / 2, y - VERTEX_RADIUS / 2, VERTEX_RADIUS, VERTEX_RADIUS);
      g2.setStroke(oldStroke);
      g2.setColor(Color.LIGHT_GRAY);
      g2.fillOval(x - VERTEX_RADIUS / 2, y - VERTEX_RADIUS / 2, VERTEX_RADIUS, VERTEX_RADIUS);
      g2.setColor(Color.DARK_GRAY);
      g2.drawString(v.name, x - v.name.length() * 8 / 2, y + VERTEX_RADIUS / 2);
    }

    public void paintEdge(Graphics g, Vertex u, Vertex v, double weight, Color color, int thickness,
        int alpha) {
      Graphics2D g2 = (Graphics2D) g;
      int x1 = Math.round(xFactor * (float) u.x + (float) MARGIN_X);
      int y1 = Math.round(yFactor * (float) u.y + (float) MARGIN_Y);
      int x2 = Math.round(xFactor * (float) v.x + (float) MARGIN_X);
      int y2 = Math.round(yFactor * (float) v.y + (float) MARGIN_Y);
      g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
      Stroke oldStroke = g2.getStroke();
      g2.setStroke(new BasicStroke(thickness));
      g2.drawLine(x1, y1, x2, y2);
      g2.setStroke(oldStroke);
      if (chckbxShowEdgeWeights.isSelected()) {
        Font oldFont = g2.getFont();
        g2.setFont(new Font("Helvetica", Font.PLAIN, 8));
        g2.setColor(Color.GRAY);
        g2.drawString(String.format("%.1f", weight), (x1 + x2) / 2, (y1 + y2) / 2);
        g2.setFont(oldFont);
      }
    }
  }
}
