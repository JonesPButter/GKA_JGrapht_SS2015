package werkzeuge.subwerkzeuge;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import materialien.MyWeightedEdge;
import materialien.Vertex;
import materialien.Graph.DirectedAttributedGraph;
import materialien.Graph.DirectedAttributedWeightedGraph;
import materialien.Graph.UndirectedAttributedGraph;
import materialien.Graph.UndirectedAttributedWeightedGraph;

import org.jgraph.JGraph;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.graph.WeightedPseudograph;

import werkzeuge.ObservableSubwerkzeug;
import werkzeuge.SubwerkzeugObserver;
import werkzeuge.algorithmen.aStern.ASternImpl;
import werkzeuge.algorithmen.bfs.BreadthFirstSearchImpl;
import werkzeuge.algorithmen.dijkstra.DijkstraImpl;
import werkzeuge.subwerkzeuge.BigGraph.BigGraphImpl;

/**
 * Der GraphManager ist für die Verwaltung des Graphen zuständig. 
 * Er lagert die Aufgaben zur Erstellung der Graphen an den GraphBuilder
 * aus und wandelt den erstellten jGrapht Graphen dann in einen 
 * anzeigbaren JGraph um. 
 * @author Chris Wolter, Jonas Johannsen
 *
 */
public class GraphManager extends ObservableSubwerkzeug
{

    private JGraph _visualizationGraph;
    private Graph<Vertex, MyWeightedEdge> _graph;
    private JGraphModelAdapter<Vertex, MyWeightedEdge> _modelAdapter; // wird benötigt um die JGrapht graphen mittel JGraph anzuzeigen
    private GraphBuilder _graphBuilder;
    private GraphSaver _graphSaver;
    private BigGraphImpl _bigGraph;
    
    private GraphManager()
    {
        _visualizationGraph = new JGraph();
        _modelAdapter = null;
        _graph = null;
        _graphBuilder = new GraphBuilder();
        _graphSaver =  new GraphSaver();
        setCurrentGraphSettings();
    }
    
    public static GraphManager create()
    {
        return new GraphManager();
    }
    
    public List<String> getSaveGraphinArray(Graph<Vertex, MyWeightedEdge> saveGraph)
    {
    	List<String> result = new ArrayList<String>();
    	result = _graphSaver.graphToArray(saveGraph);
    	

    	return result;
    }
    
    public void loadGraph(List<String> graphData)
    { 		
    	_graph = _graphBuilder.createGraph(graphData,_graph);
  
                
        //Visualisierung des eben erstellten Graphen mittels eines Adapters
        _modelAdapter = _graphBuilder.getModelAdapter(_graph);  
        if(_modelAdapter != null)
        {
        	_visualizationGraph.setModel(_modelAdapter);
        }else System.out.println("Nullpointer");
        
        //TODO layout
//        if(_graph instanceof Pseudograph<Vertex, MyWeightedEdge>)
//        {
//            _graphBuilder.layout((DirectedGraph<Vertex, MyWeightedEdge>) _graph, _modelAdapter, _visualizationGraph);
//        }
        
        
        informiereUeberAenderung("Graph changed!"); 
    }
    
    public void createBigGraph()
    {
    	
    	_bigGraph = new BigGraphImpl();
    	_graph = _bigGraph.getBigGraph();
    	 _modelAdapter = _graphBuilder.getModelAdapter(_graph);  
         _visualizationGraph.setModel(_modelAdapter);//TODO
         informiereUeberAenderung("Graph changed!");   
    }
    
    public void createDefaultDirectedGraph()
    {
        _graph = _graphBuilder.createDefaultDirectedGraph(_graph);
        //Visualisierung des eben erstellten Graphen mittels eines Adapters
        _modelAdapter = _graphBuilder.getModelAdapter(_graph);  
        _visualizationGraph.setModel(_modelAdapter);
      
        //TODO layout
//        _graphBuilder.layout((DirectedGraph<Vertex, MyWeightedEdge>) _graph, _modelAdapter, _visualizationGraph);
        informiereUeberAenderung("Graph changed!");         
    }
    
    public void createDefaultUndirectedGraph()
    {
        _graph = _graphBuilder.createDefaultUndirectedGraph(_graph);
        //Visualisierung des eben erstellten Graphen mittels eines Adapters
        _modelAdapter = _graphBuilder.getModelAdapter(_graph);  
        _visualizationGraph.setModel(_modelAdapter);

        informiereUeberAenderung("Graph changed!");          
    }
    
    private void setCurrentGraphSettings()
    {  
      _visualizationGraph.setPreferredSize(new Dimension(1400,880));

      _visualizationGraph.setGridVisible(true); // zeigt ein Grid im Hintergrund des Graphen an
      _visualizationGraph.setSizeable(false);   // der Graph kann nun nicht mehr per Mauskick vergrößert werden
      _visualizationGraph.setBendable(true);
      _visualizationGraph.setEditable(false);
      
      _visualizationGraph.setMoveBeyondGraphBounds(false); // verhidnert, dass das Layout zerstört wird
      _visualizationGraph.setBackground(new Color(240,240,240)); // Hintergrundfarbe 
    }
    
    public void addVertex(String s,double attr, int x, int y)
    {
        _graphBuilder.addVertexAtPosition(s,attr,x,y,_graph);
        _modelAdapter = _graphBuilder.getModelAdapter(_graph);
        _visualizationGraph.setModel(_modelAdapter);

        informiereUeberAenderung("Graph changed!");  
       
    }
    
    public JGraph getGraphVisualization()
    {
        return _visualizationGraph;
    }
    
    public Graph<Vertex, MyWeightedEdge> getGraph()
    {
        return _graph;
    }

    public boolean isGraphAttributed()
    {
        if(_graph instanceof DirectedAttributedGraph || _graph instanceof DirectedAttributedWeightedGraph
                || _graph instanceof UndirectedAttributedGraph || _graph instanceof UndirectedAttributedWeightedGraph)
        {
            return true;
        }
        return false;
    }
    
    public boolean isGraphWeighted()
    {
        if(_graph instanceof WeightedPseudograph || _graph instanceof DirectedAttributedWeightedGraph
                || _graph instanceof DirectedWeightedPseudograph || _graph instanceof UndirectedAttributedWeightedGraph)
        {
            return true;
        }
        return false;
    }
    
    public void addEdge(Vertex vertexSource, Vertex vertexTarget)
    {
        _graphBuilder.addEdge(vertexSource,vertexTarget,_graph);
        _modelAdapter = _graphBuilder.getModelAdapter(_graph);
        _visualizationGraph.setModel(_modelAdapter);

        informiereUeberAenderung("Graph changed!"); 
        
    }

    public void removeVertex(Vertex vertex)
    {
        _graph.removeVertex(vertex);
        _modelAdapter = _graphBuilder.getModelAdapter(_graph);
        _visualizationGraph.setModel(_modelAdapter);

        informiereUeberAenderung("Graph changed!");  
    }

    public void removeEdge(MyWeightedEdge edge)
    {
        _graph.removeEdge(edge);
        try
        {
            _modelAdapter = _graphBuilder.getModelAdapter(_graph);
            _visualizationGraph.setModel(_modelAdapter);            
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }

        informiereUeberAenderung("Graph changed!"); 
    }

    public void startBreadthFirstSearch()
    {
        BreadthFirstSearchImpl bfs = new BreadthFirstSearchImpl(_graph);
        bfs.registriereObserver(new SubwerkzeugObserver()
        {
            
            @Override
            public void reagiereAufAenderung(Object o)
            {
                _modelAdapter = _graphBuilder.getModelAdapter(_graph);
                _visualizationGraph.setModel(_modelAdapter);

                informiereUeberAenderung("Graph changed!");                 
            }
            
            @Override
            public void reagiereAufAenderung()
            {
                // TODO Auto-generated method stub
                
            }
        });
        bfs.showUI();       
    }
    
    public void startDijkstraAlgorithm()
    {
        try
        {
            DijkstraImpl dijkstra = new DijkstraImpl(_graph);
            dijkstra.registriereObserver(new SubwerkzeugObserver()
            {
                
                @Override
                public void reagiereAufAenderung(Object o)
                {
                    _modelAdapter = _graphBuilder.getModelAdapter(_graph);
                    _visualizationGraph.setModel(_modelAdapter);
                    
                    informiereUeberAenderung("Graph changed!");                 
                }
                
                @Override
                public void reagiereAufAenderung()
                {
                    // TODO Auto-generated method stub
                    
                }
            });
            dijkstra.showUI();       
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Dies ist kein gültiger Graph, um den Dijkstra-Algorithmus anzuwenden.");
        }

    }

    public void startASternAlgorithm()
    {
        try
        {
            ASternImpl aStern = new ASternImpl(_graph);
            aStern.registriereObserver(new SubwerkzeugObserver()
            {
                
                @Override
                public void reagiereAufAenderung(Object o)
                {
                    _modelAdapter = _graphBuilder.getModelAdapter(_graph);
                    _visualizationGraph.setModel(_modelAdapter);
                    
                    informiereUeberAenderung("Graph changed!");                 
                }
                
                @Override
                public void reagiereAufAenderung()
                {
                    // TODO Auto-generated method stub
                    
                }
            });
            aStern.showUI();       
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Dies ist kein gültiger Graph, um den AStern-Algorithmus anzuwenden.");
        }
    }
}
