package werkzeuge;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.Pseudograph;

//import werkzeuge.algorithmen.BreadthFirstSearchImpl;
import werkzeuge.subwerkzeuge.GraphManager;
import werkzeuge.subwerkzeuge.filehandler.GraphFileReader;
import werkzeuge.subwerkzeuge.filehandler.GraphFileWriter;
import fachwerte.RadioButtonGroup;
//import org.jgrapht.Graph;
//import org.jgrapht.graph.DefaultEdge;

/**
 * Der GraphVisualizer ist die Hauptverwaltung des Programms.
 * Hier werden die anderen Subwerkzeuge verwaltet und die Graphen an die UI gesetzt.
 * Außerdem werden die Usereingaben abgefangen und an die Subwerkzeuge zur dortigen verarbeitung 
 * weitergeleitet. Ändern sich die Werte der Subwerkzeuge bekommt der GraphVisualizer dies mit, da er
 * als Observer darauf wartet, dass diese sich ändern.
 * @author Christopher Wolter, Jonas Johannsen
 *
 */
public class MainController
{
    // *************************** Subwerkzeuge **************************************
    private final GraphManager _graphManager;
    private final GraphFileReader _fileReader;
    private final GraphFileWriter _fileWriter;
    
    // *************************** Felder ********************************************
    private RadioButtonGroup _buttons;
    DefaultGraphCell selectedCell;
    private final MainControllerUI _ui;
    
    /**
     * Das Hauptwerkzeug. Hier werden alle anderen Werkzeuge verwaltet
     * und die Graphen an die UI gehängt.
     */
    public MainController()
    {
        _graphManager = GraphManager.create();
        _fileReader = GraphFileReader.create();
        _fileWriter = GraphFileWriter.create();
        _ui = new MainControllerUI();
        
        registriereSubwerkzeugObserver();
        registriereGraphObserver();
        initialisiereUIListener();
    }
 
    /**
     * Zeigt die UI an.
     */
    public void showUI()
    {
        _ui.getFrame().setVisible(true);
    }
    
    /*
     * Lädt den File den der User auswählt
     */
    private void loadFile()
    {
        _ui.getGraphPanel().removeAll();

        //TODO
        // Meine Algorithmusidee:
        // _fileReader liest den File ein, den der User ausgesucht hat im JFileChooser.
        // wir holen uns aus dem _jfileReader die ArrayList<String> und übergeben sie dem GraphManager,
        // damit der daraus einen vernünftigen Graphen bauen kann. Diesen holen wir uns anschließend
        // aus dem GraphManager und setzen ihn hier an der UI.

        try
        {
            _fileReader.loadFile();
            List<String> graphData = _fileReader.getFileData();
            
            if(graphData != null) // noch nicht richtig! nullpointerException wird schon vorher geworfen! TODO
            {
                _graphManager.loadGraph(graphData);                
            }
        }
        catch (IOException e)
        {
        	
        	e.printStackTrace();
            JOptionPane.showMessageDialog(_ui.getFrame(), "Der Computer mag Sie nicht. "
                    + "Ein IO Fehler ist aufgetreten. "
                    + "Bitte versuchen Sie es erneut oder wenden Sie sich an den Kundenservice.");
        }
    }
    
    
    private void  safeFile()
    {
    	List<String> graphArray = new ArrayList<>();

    	try
    	{
	    		graphArray = _graphManager.getSaveGraphinArray(_graphManager.getGraph());
	    		_fileWriter.saveFile(graphArray);
    	}
    	catch(NullPointerException e)
    	{
    	       JOptionPane.showMessageDialog(_ui.getFrame(), "Wirklich??? Das tust du mir an. WIESO?"
    	    		   + "Erstelle erstmal einen Graphen. SOFORT!!! "
    	    		   + "oder wenden Sie sich an den Kundenservice");
    		
    	}
//    	}

    	
    }
    
    
    private void aktualisiereGraph()
    {
        _ui.getGraphPanel().removeAll();
        JGraph graph = _graphManager.getGraphVisualization();
        _ui.getGraphPanel().add(graph);
        _ui.getFrame().pack();
        _ui.getGraphPanel().repaint();
        
    }
    
    private Vertex getVertexTargetUserInput()
    {
        boolean bool = false;
        boolean falscheEingabe = true;
        Vertex vertexTarget = null;
        String vertexTargetString = "";
        Set<Vertex> vertexes = _graphManager.getGraph().vertexSet();
        while(!bool)
        {
            bool = true;
            vertexTargetString = JOptionPane.showInputDialog("Geben Sie einen Namen für den Zielknoten ein:");
            if(vertexTargetString == null)
            {
                return null;
            }
            for(Vertex v : vertexes)
            {
                if(v.getName().equals(vertexTargetString))
                {
                    vertexTarget = v;
                    falscheEingabe = false; // alles richtig gemacht!
                }
            }   
           if(falscheEingabe){
               JOptionPane.showMessageDialog(_ui.getFrame(), "Fehlerhafte Eingabe. "
                    + "\nDieser Knoten existiert nicht."
                    + "\nDer Name darf nicht leer sein.");
               bool = false;
           }
        }
        return vertexTarget;
    }
    
    /*
     * Fängt die Usereingabe ab, wenn ein neuer Knoten erstellt wird
     */
    private String getVertexUserInput()
    {
        boolean bool = false;
        String name = "";
        Set<Vertex> vertexes = _graphManager.getGraph().vertexSet();
        while(!bool)
        {
            bool = true;
            name = JOptionPane.showInputDialog("Geben Sie einen Namen für den Knoten ein:");
            if(name == null)
            {
                return name = "";
            }
            for(Vertex v : vertexes)
            {
                if(v.getName().equals(name) || name.equals(""))
                {
                    JOptionPane.showMessageDialog(_ui.getFrame(), "Fehlerhafte Eingabe. "
                            + "\nEs dürfen keine Knoten mit dem gleichen Namen existieren."
                            + "\nDer Name darf nicht leer sein.");
                    bool = false;               
                }
            }   
        }
        return name;
    }
  
    private double getVertexAttributuserInput() throws NullPointerException
    {
        double attr =0;
        String attribut = "";
        boolean bool = false;
        while(!bool)
        {
            bool = true;
            attribut = JOptionPane.showInputDialog("Geben Sie eine Zahl als Attribut ein:");
            if(attribut == null)
            {
                throw new NullPointerException("Dialog wurde abgebrochen");
            }
            if(!attribut.matches("(-?[0.0-9.9]+)"))
            {
                JOptionPane.showMessageDialog(_ui.getFrame(), "Fehlerhafte Eingabe. "
                        + "\n Bitte geben Sie eine gültige Zahl ein."
                        + " Beispiele: 5.67 || -0.5 || 42");
                bool = false;               
            }
        }
        
        attr = Double.parseDouble(attribut);
        
        return attr;
    }
    
    private double getEdgeWeightUserInput() throws NullPointerException
    {
        double attr =0;
        String attribut = "";
        boolean bool = false;
        while(!bool)
        {
            bool = true;
            attribut = JOptionPane.showInputDialog("Geben Sie eine Gewichtung ein:");
            if(attribut == null)
            {
                throw new NullPointerException("Dialog wurde abgebrochen");
            }
            if(!attribut.matches("(-?[0.0-9.9]+)"))
            {
                JOptionPane.showMessageDialog(_ui.getFrame(), "Fehlerhafte Eingabe. "
                        + "\n Bitte geben Sie eine gültige Gewichtung ein."
                        + " Beispiele: 5.67 || -0.5 || 42");
                bool = false;               
            }
        }
        
        attr = Double.parseDouble(attribut);
        
        return attr;
    }
    /*
     * Hier werden die Listener für den Graphen registriert.
     * Alles was der user in der UI am Graphen mit der Mouse ändert,
     * wird hier abgehört.
     */
    private void registriereGraphObserver()
    {
        _graphManager.getGraphVisualization().addMouseListener(new MouseListener()
        {
            
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if(selectedCell != null)
                {
                    if(selectedCell.getUserObject() instanceof Vertex)
                    {
                        Vertex vertex = (Vertex) selectedCell.getUserObject();
                            vertex.setX(e.getX());
                            vertex.setY(e.getY()); 
                    } 
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e)
            {
                selectedCell = 
                        (DefaultGraphCell) _graphManager.getGraphVisualization().getSelectionCellAt(new Point(e.getX(), e.getY()));
                
            }
            
            @Override
            public void mouseExited(MouseEvent e)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void mouseEntered(MouseEvent e)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void mouseClicked(MouseEvent e)
            {
                selectedCell = 
                        (DefaultGraphCell) _graphManager.getGraphVisualization().getSelectionCellAt(new Point(e.getX(), e.getY()));
                JRadioButtonMenuItem selectedButton = _buttons.getClickedButton();
                if(selectedButton != null)
                {
                    if(selectedButton.equals(_ui.getMenuRadioButton_Vertex()))
                    {
                        String name = getVertexUserInput();
                        if(!name.equals(""))
                        {   
                            double attr =0;
                            if(_graphManager.isGraphAttributed())
                            {
                                try
                                {
                                    attr = getVertexAttributuserInput(); 
                                }
                                catch (Exception e2)
                                {

                                }
                            }
                            _graphManager.addVertex(name,attr,e.getX(),e.getY()); 
                        }                      
                    }
                    else if(selectedCell != null && selectedButton.equals(_ui.getMenuRadioButton_Delete()))
                    {
                        if(selectedCell.getUserObject() instanceof Vertex)
                        {
                            Vertex vertex = (Vertex) selectedCell.getUserObject();
                            _graphManager.removeVertex(vertex);   
                        }
                        else if(selectedCell.getUserObject() instanceof MyWeightedEdge)
                        {
                            MyWeightedEdge edge = (MyWeightedEdge) selectedCell.getUserObject();
                            _graphManager.removeEdge(edge);
                        }
                    }
                    else if(selectedCell != null && selectedButton.equals(_ui.getMenuRadioButton_Edge())){
                        if(selectedCell!= null && selectedCell.getUserObject() instanceof Vertex)
                        {
                            Vertex vertexTarget = getVertexTargetUserInput();
                            if(!vertexTarget.equals(""))
                            {
                                _graphManager.addEdge((Vertex)selectedCell.getUserObject(),vertexTarget);
                                double attr =0;
                                if(_graphManager.isGraphWeighted())
                                {
                                    try
                                    {
                                        attr = getVertexAttributuserInput();
                                        MyWeightedEdge edge = _graphManager.getGraph().getEdge((Vertex)selectedCell.getUserObject(),vertexTarget);
                                        ((Pseudograph<Vertex, MyWeightedEdge>)_graphManager.getGraph()).setEdgeWeight(edge, attr); 
                                       
                                    }
                                    catch (Exception e2)
                                    {

                                    }
                                }
                            }
                        }
                    }
                }             
            }
        });
    }
    /*
     * registriert die Listener an den Subwerkzeugen
     * Ändert sich der Graph im GraphManager, so wird hier der Observer 
     * informiert
     */
    private void registriereSubwerkzeugObserver()
    {
        _graphManager.registriereObserver(new SubwerkzeugObserver()
        {
            
            @Override
            public void reagiereAufAenderung(Object o)
            {
                if(o.equals("Graph changed!"))
                {
                    aktualisiereGraph();
                }
            }
            
            @Override
            public void reagiereAufAenderung()
            {
                // TODO Auto-generated method stub
                
            }
        });
        
    }

    /*
     * Initialisiert die Listener an den GUI-Elementen
     * Hier werden die restlichen Listener an den Buttons der UI regisitriert
     * 
     */
    private void initialisiereUIListener()
    {
        JMenuItem load = _ui.getMenuItemFile_Load();
        load.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                loadFile();
            }
        });
        
        
        JMenuItem save = _ui.getMenuItemFile_Safe();
        save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				safeFile();
			}
		});
        
        JMenuItem bfsAlgorithm = _ui.getMenuItemAlgorithms_BreadthFirst();
        bfsAlgorithm.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _graphManager.startBreadthFirstSearch();                
            }
        });
        
        JMenuItem dijkstraAlgorithm = _ui.getMenuItemAlgorithms_Dijkstra();
        dijkstraAlgorithm.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _graphManager.startDijkstraAlgorithm();                
            }
        });
        
        JMenuItem aSternAlgorithm = _ui.getMenuItemAlgorithms_AStern();
        aSternAlgorithm.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _graphManager.startASternAlgorithm();                
            }
        });
        
        JMenuItem createBigGraphButton = _ui.getMenuItemCreate_BigGraph();
        createBigGraphButton.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _graphManager.createBigGraph();;
                aktualisiereGraph();
            }
        });
        
        JMenuItem createDirectedGraphButton = _ui.getMenuItemCreate_DirectedGraph();
        createDirectedGraphButton.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _graphManager.createDefaultDirectedGraph();
                aktualisiereGraph();
            }
        });
        
        JMenuItem createUndirectedGraphButton = _ui.getMenuItemCreate_UndirectedGraph();
        createUndirectedGraphButton.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _graphManager.createDefaultUndirectedGraph();
                aktualisiereGraph();
            }
        });
        _buttons = new RadioButtonGroup();
        JRadioButtonMenuItem addVertexButton = _ui.getMenuRadioButton_Vertex();
        JRadioButtonMenuItem addEdgeButton = _ui.getMenuRadioButton_Edge();
        JRadioButtonMenuItem deleteButton = _ui.getMenuRadioButton_Delete();
        _buttons.add(addVertexButton);
        _buttons.add(addEdgeButton);
        _buttons.add(deleteButton);
        
        addVertexButton.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                if(addVertexButton.isSelected())
                {
                    _buttons.buttonClicked(addVertexButton); 
                }                
            }
        });
        
        addEdgeButton.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                if(addEdgeButton.isSelected())
                {
                    _buttons.buttonClicked(addEdgeButton); 
                }   
            }
        });
        
        deleteButton.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                if(deleteButton.isSelected())
                {
                    _buttons.buttonClicked(deleteButton); 
                }   
            }
        });
    }


}
