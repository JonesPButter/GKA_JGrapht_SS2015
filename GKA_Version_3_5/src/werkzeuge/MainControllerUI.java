package werkzeuge;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class MainControllerUI
{
    
    private JFrame _frame;
    private JPanel _contentPane;
    
    private JMenuItem _menuItemFile_Load;
    private JMenuItem _menuItemFile_Safe;
    private JMenuItem _menuItemCreate_DirectedGraph;
    private JMenuItem _menuItemCreate_UndirectedGraph;
    private JMenuItem _menuItemCreate_BigGraph;
    private JMenuItem _menuItemCreate_EulerGraph;
    private JMenuItem _menuItemAlgorithms_BreadthFirst;
    private JMenuItem _menuItemAlgorithms_Dijkstra;
    private JMenuItem _menuItemAlgorithms_AStern;
    private JMenuItem _menuItemAlgorithms_Spannbaeume;
    private JMenuItem _menuItemAlgorithms_TouringProblems;
    
    private JRadioButtonMenuItem _menuRadioButton_Vertex;
    private JRadioButtonMenuItem _menuRadioButton_Edge;
    private JRadioButtonMenuItem _menuRadioButton_Delete;
    private JSlider _speedSlider;
    private JPanel _graphPanel;

    public static int FrameWIDTH = 1650, FrameHEIGHT = 1000, GraphPanelWIDTH,GraphPanelHEIGHT;

    /**
     * Erstellt die UI für das GraphVisualizer Werkzeug.
     * Orientiert an dem Model-View-Controll-Prinzip ist diese Klasse nur für die Darstellung der UI zuständig.
     */
    public MainControllerUI()
    {
        _frame = new JFrame("GraphVisualizer");
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setBounds(100, 100, FrameWIDTH, FrameHEIGHT);
        _frame.setPreferredSize(new Dimension(FrameWIDTH,FrameHEIGHT));
        _contentPane = new JPanel();
        _contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        _contentPane.setLayout(new BorderLayout(0, 0));
        _frame.setLocationRelativeTo(null);
        _frame.setContentPane(_contentPane);
        
        JMenuBar menuBar = new JMenuBar();
        _contentPane.add(menuBar, BorderLayout.NORTH);
        
        JMenu menuFile = new JMenu("File");
        menuBar.add(menuFile);
        
        _menuItemFile_Load = new JMenuItem("load");
        menuFile.add(_menuItemFile_Load);
        
        _menuItemFile_Safe = new JMenuItem("safe");
        menuFile.add(_menuItemFile_Safe);
        
        JMenu menuCreate = new JMenu("Create");
        menuBar.add(menuCreate);
        
        _menuItemCreate_DirectedGraph = new JMenuItem("directed Graph");
        menuCreate.add(_menuItemCreate_DirectedGraph);
        
        _menuItemCreate_UndirectedGraph = new JMenuItem("undirected Graph");
        menuCreate.add(_menuItemCreate_UndirectedGraph);
        
        _menuItemCreate_BigGraph = new JMenuItem("Big Graph");
        menuCreate.add(_menuItemCreate_BigGraph);
        
        _menuItemCreate_EulerGraph = new JMenuItem("Euler Graph");
        menuCreate.add(_menuItemCreate_EulerGraph);
        
        JMenu menuAlgorithms = new JMenu("Algorithms");
        menuBar.add(menuAlgorithms);
        
        _menuItemAlgorithms_BreadthFirst = new JMenuItem("BreadthFirstSearch(BFS)");
        menuAlgorithms.add(_menuItemAlgorithms_BreadthFirst);
        
        _menuItemAlgorithms_Dijkstra = new JMenuItem("Dijkstra");
        menuAlgorithms.add(_menuItemAlgorithms_Dijkstra);
 
        _menuItemAlgorithms_AStern = new JMenuItem("A*");
        menuAlgorithms.add(_menuItemAlgorithms_AStern);
        
        _menuItemAlgorithms_Spannbaeume = new JMenuItem("Spannbäume");
        menuAlgorithms.add(_menuItemAlgorithms_Spannbaeume);
        
        _menuItemAlgorithms_TouringProblems = new JMenuItem("Touren-Probleme");
        menuAlgorithms.add(_menuItemAlgorithms_TouringProblems);
        
        _menuRadioButton_Vertex = new JRadioButtonMenuItem("Vertex");
        _menuRadioButton_Vertex.setMaximumSize(new Dimension(100, 100));
        _menuRadioButton_Vertex.setPreferredSize(new Dimension(80, 22));
        menuBar.add(_menuRadioButton_Vertex);
        
        _menuRadioButton_Edge = new JRadioButtonMenuItem("Edge");
        _menuRadioButton_Edge.setMaximumSize(new Dimension(100, 100));
        _menuRadioButton_Edge.setPreferredSize(new Dimension(80, 22));
        menuBar.add(_menuRadioButton_Edge);
        
        _menuRadioButton_Delete = new JRadioButtonMenuItem("Delete");
        _menuRadioButton_Delete.setMaximumSize(new Dimension(100, 100));
        _menuRadioButton_Delete.setPreferredSize(new Dimension(80, 22));
        menuBar.add(_menuRadioButton_Delete);
        
        JPanel southPanel = new JPanel();
        _contentPane.add(southPanel,BorderLayout.SOUTH);
        
        JLabel jLabelGeschwindigkeit = new JLabel("Geschwindigkeit");
        southPanel.add(jLabelGeschwindigkeit);
        
        _speedSlider = new JSlider();
        _speedSlider.setValue(100);
        _speedSlider.setPaintLabels(true);
        
        Hashtable<Integer, JLabel> lableTable = new Hashtable<>();
        lableTable.put(new Integer(0), new JLabel("0"));
        lableTable.put(new Integer(20),new JLabel("20"));
        lableTable.put(new Integer(40), new JLabel("40"));
        lableTable.put(new Integer(60), new JLabel("60"));
        lableTable.put(new Integer(80), new JLabel("80"));
        lableTable.put(new Integer(100), new JLabel("100"));
        _speedSlider.setLabelTable(lableTable);
        
        southPanel.add(_speedSlider);
        
        _graphPanel = new JPanel();
        _graphPanel.setBorder(new CompoundBorder());
        _graphPanel.setBounds(100,100,FrameWIDTH,FrameHEIGHT);
        _contentPane.add(_graphPanel, BorderLayout.CENTER);
        GraphPanelWIDTH = _graphPanel.getWidth();
        GraphPanelHEIGHT = _graphPanel.getHeight();
//        _graphScrollPane = new JScrollPane(_graphPanel);
//        _graphScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//        _graphScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//        _graphScrollPane.setBorder(new CompoundBorder());
//        _graphScrollPane.setBounds(100, 100, WIDTH, HEIGHT);
//        _contentPane.add(_graphScrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Liefert das Toplevel JFrame der UI
     * @return
     */
    public JFrame getFrame()
    {
        return _frame;
    }
    
    /**
     * Liefert das Graph Panel.
     * Hier werden die Graphen drauf gezeichnet.
     * @return Graph Panel
     */
    public JPanel getGraphPanel()
    {
        return _graphPanel;
    }
    
    /**
     * Liefert den MenuItemButton für das Laden neuer Graphen in das Programm.
     * @return
     */
    public JMenuItem getMenuItemFile_Load()
    {
        return _menuItemFile_Load;
    }

    /**
     * Liefert den MenuItemButton für das Speichern neuer Graphen aus dem Programm.
     * @return
     */
    public JMenuItem getMenuItemFile_Safe()
    {
        return _menuItemFile_Safe;
    }

    /**
     * Liefert den MenuItemButton für das Erstellen neuer gerichteter Graphen
     * @return
     */
    public JMenuItem getMenuItemCreate_DirectedGraph()
    {
        return _menuItemCreate_DirectedGraph;
    }

    /**
     * Liefert den MenuItemButton für das Erstellen neuer ungerichteter Graphen
     * @return
     */
    public JMenuItem getMenuItemCreate_UndirectedGraph()
    {
        return _menuItemCreate_UndirectedGraph;
    }

    /**
     * Liefert den MenuItemButton für das Erstellen eines Big Graphen
     * @return
     */
    public JMenuItem getMenuItemCreate_BigGraph()
    {
        return _menuItemCreate_BigGraph;
    }
    
    /**
     * Liefert den MenuItemButton für das Erstellen eines Euler Graphen
     * @return
     */
    public JMenuItem getMenuItemCreate_EulerGraph()
    {
        return _menuItemCreate_EulerGraph;
    }
    
    /**
     * Liefert den MenuItemButton für das Anwenden des BreadthFirstSearch Algorithmus
     * @return
     */
    public JMenuItem getMenuItemAlgorithms_BreadthFirst()
    {
        return _menuItemAlgorithms_BreadthFirst;
    }

    /**
     * Liefert den MenuItemButton für das Anwenden des Dijkstra Algorithmus
     * @return
     */
    public JMenuItem getMenuItemAlgorithms_Dijkstra()
    {
        return _menuItemAlgorithms_Dijkstra;
    }
    
    /**
     * Liefert den MenuItemButton für das Anwenden des A* Algorithmus
     * @return
     */    
    public JMenuItem getMenuItemAlgorithms_AStern()
    {
        return _menuItemAlgorithms_AStern;
    }
    
    /**
     * Liefert den MenuItemButton für die Anwendung von MST-Algorithmen
     * @return
     */    
    public JMenuItem getMenuItemAlgorithms_Spannbaeume()
    {
        return _menuItemAlgorithms_Spannbaeume;
    }
    
    /**
     * Liefert den MenuItemButton für das Anwenden des Kruskal Algorithmus
     * @return
     */    
    public JMenuItem getMenuItemAlgorithms_TouringProblems()
    {
        return _menuItemAlgorithms_TouringProblems;
    }
    
    /**
     * Liefert den JRadioButtonMenuItem für das Erstellen neuer Knoten
     * @return
     */
    public JRadioButtonMenuItem getMenuRadioButton_Vertex()
    {
        return _menuRadioButton_Vertex;
    }

    /**
     * liefert den JRadioButtonMenuItem für das Erstellen neuer Kanten
     * @return
     */
    public JRadioButtonMenuItem getMenuRadioButton_Edge()
    {
        return _menuRadioButton_Edge;
    }

    /**
     * Liefert den JRadioButtonMenuItem für das Löschen von Graphelementen.
     * @return
     */
    public JRadioButtonMenuItem getMenuRadioButton_Delete()
    {
        return _menuRadioButton_Delete;
    }

    /**
     * Liefert den Slider für die Anzeige der Geschwindigkeit mit der die Algorithmen ausgeführt werden sollen.
     * @return
     */
    public JSlider getSpeedSlider()
    {
        return _speedSlider;
    }

    
    

}
