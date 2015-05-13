package werkzeuge.algorithmen.dijkstra;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import materialien.MyWeightedEdge;
import materialien.Vertex;
import materialien.Graph.UndirectedAttributedWeightedGraph;

import org.jgrapht.Graph;
import org.jgrapht.graph.WeightedPseudograph;

import werkzeuge.ObservableSubwerkzeug;
import werkzeuge.algorithmen.AlgorithmConsole;

public class DijkstraImpl extends ObservableSubwerkzeug
{
    Graph<Vertex,MyWeightedEdge> _graph; 
    Vertex _rootVertex;
    Vertex _targetVertex;
    Map<Vertex,String> _okMap; 
    Map<Vertex,String> _falseMap;
    Map<Vertex,Vertex> _vorgaengerMap; // map<key,val>
    int _benoetigteKanten;
    int _graphAccesses;
    double _wegLaenge;
    DijkstraUI _ui;
    
    /**
     * 
     * @param graph Der zu traversierende Graph
     * 
     * @require ((graph instanceof UndirectedAttributedWeightedGraph) || (graph instanceof WeightedPseudograph))
     * 
     * @throws IllegalArgumentException
     */
    public DijkstraImpl(Graph<Vertex,MyWeightedEdge> graph)
    {
        if(!((graph instanceof UndirectedAttributedWeightedGraph) || (graph instanceof WeightedPseudograph)))
        {
            throw new IllegalArgumentException("Dies ist kein gültiger Graph, um den Dijkstra-Algorithmus auszuführen");
        }
        
        _graph = graph;
        _okMap = new HashMap<>();
        _falseMap = new HashMap<Vertex, String>();
        _vorgaengerMap = new HashMap<Vertex, Vertex>();
        _benoetigteKanten =0;
        _graphAccesses = 0;
        _wegLaenge=0;
        _ui = new DijkstraUI();
        faerbungenZuruecksetzen();
        registriereListenerAnUI();
    }

    
    
    
    
    
    
    
    
    public List<Vertex> findShortestWay(Graph<Vertex,MyWeightedEdge> graph,Vertex source,Vertex target)
    {
        // Preconditions
        assert graph != null : "Vorbedingung verletzt: graph != null";
        assert source != null : "Vorbedingung verletzt: graph != null";
        assert target != null : "Vorbedingung verletzt: graph != null";
        
        List<Vertex> shortestWay = new ArrayList<Vertex>();
        Vertex tempSource = source;

        // nur für die spätere Färbung
        source.visit();
        source.setPartOfShortestWay();
        
        // Initialisierung
        _vorgaengerMap.put(source, source); // Der Vorgänger von Source ist Source.      
        _falseMap.put(source, "false");      
        
        do{
            _graphAccesses++;
            
            // 1.) Suche unter den Knoten v(i) mit OK(i) = false einen Knoten v(h) mit dem kleinsten Wert von Entf(i)
            tempSource = getVertexWithShortestDist(_falseMap); 
            
            // 2.) Setze OK(h) = true
            _okMap.put(tempSource, "OK");
            _falseMap.remove(tempSource);
            
            // 3.) Für alle Knoten v(j) mit OK(j) = false, für die die Kante v(h),v(j) exisitiert die Entfernung und gegebenenfalls den Vorgänger neuberechnen
            calculateNeighboursDistance(tempSource);
        } while(!_falseMap.isEmpty());         
            
        if(_okMap.containsKey(target))
        {
            calculateShortestWay(shortestWay,source,target);            
        }
        return shortestWay;
    }
    
    /*
     * liefert den Knoten mit OK = false mit dem kleinsten Wert von Entf
     */
    private Vertex getVertexWithShortestDist(Map<Vertex, String> falseMap)
    {
        _graphAccesses++;
        Set<Vertex> vertexes = falseMap.keySet();
        Vertex result = null;
        for(Vertex v : vertexes)
        {
            if(result == null)
            {
                result = v;
            }
            else
            {
                if(result.getEntfernungVomStartVertex() > v.getEntfernungVomStartVertex())
                {
                    result = v;
                }
            }
        }
        return result;
    }









    private void calculateShortestWay(List<Vertex> shortestWay, Vertex source, Vertex target)
    {
        boolean startVertex = false;
        Vertex tempTarget = target;
        Vertex vorgänger = null;
        shortestWay.add(tempTarget);
        _wegLaenge = tempTarget.getEntfernungVomStartVertex();
        while(!startVertex)
        {
            tempTarget.setPartOfShortestWay();
            _benoetigteKanten++;
            vorgänger = _vorgaengerMap.get(tempTarget);
            shortestWay.add(0,vorgänger);
            if(vorgänger.equals(source))
            {
                startVertex = true;
            }
            tempTarget = vorgänger;
        }        
    }

    private Vertex findNearestVertex(Vertex source)
    {
        Vertex nearestVertex = null;
        
        Set<Vertex> neighbours = getUndirectedAdjacentNodes(source);
        
        // einen RandomVertex als am Wenigsten weit entfernt makieren
        // --> wird anschließend geprüft und gegebenenfalls korrigiert
        for(Vertex child : neighbours)
        {
            if(!_okMap.containsKey(child))
            {
                nearestVertex = child;
                break;
            }
        }  
        
        if(nearestVertex != null)
        {
            for(Vertex child : neighbours)
            {
                if(!_okMap.containsKey(child)) // damit wir nicht "rückwärts" im Graphen schauen
                {
                    if(child.getEntfernungVomStartVertex() < nearestVertex.getEntfernungVomStartVertex())
                    {
                        nearestVertex = child;
                    }
                }
            }           
        }
        
        return nearestVertex;
    }
    
    private void calculateNeighboursDistance(Vertex source)
    {
        Set<Vertex> neighbours = getUndirectedAdjacentNodes(source);
        double entf=0;
        _graphAccesses++;
        for(Vertex child : neighbours)
        {
            if(!_okMap.containsKey(child)) // damit wir nicht "rückwärts" im Graphen schauen
            {                  

                MyWeightedEdge e = _graph.getEdge(source, child);           
                entf = source.getEntfernungVomStartVertex() + e.getEdgeWeight(); // entf = 0 + Kanntengewicht
                
                if(child.isVisited())
                {
                    if(child.getEntfernungVomStartVertex() > entf)
                    {
                        _vorgaengerMap.put(child, source);
                        child.setEntfernungVomStartVertex(entf);                    
                    }
                }
                else if(!child.isVisited())
                {
                    _falseMap.put(child,"false");
                    _vorgaengerMap.put(child, source);
                    child.setEntfernungVomStartVertex(entf);
                    child.visit();
                }
            }
        }
    }

    private Set<Vertex> getUndirectedAdjacentNodes(Vertex n)
    {
        Set<Vertex> adjacentNodes= new HashSet<Vertex>();
        Set<MyWeightedEdge> edges= _graph.edgesOf(n);
       for(MyWeightedEdge edge : edges)
       {
//           _graphAccesses++;
           Vertex source = _graph.getEdgeSource(edge);
           Vertex neighbour = _graph.getEdgeTarget(edge);
           if(source.equals(n))
           {
               adjacentNodes.add(neighbour);               
           } else if(neighbour.equals(n))
           {
               adjacentNodes.add(source);
           }
//           else if(neighbour.equals(_targetVertex))
//           {
//               break;
//           }
       }
       return adjacentNodes;
        
    }
    
    private void faerbungenZuruecksetzen()
    {
        for(Vertex v : _graph.vertexSet())
        {
            v.reset();
        }        
    }

    private void registriereListenerAnUI()
    {
        _ui.getOkButton().addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("**************** DIJKSTRA ******************** ");
                String vertexSource = _ui.getVertexSource().getText();
                String vertexTarget = _ui.getVertexTarget().getText();
 
                if(eingabenKorrekt(vertexSource,vertexTarget))
                {
                    _ui.getDialog().dispose();
                    String shortestW = findShortestWay(_graph,_rootVertex,_targetVertex).toString();  
                    informiereUeberAenderung(_graph);
                    if(shortestW.equals("[]"))
                    {
                        JOptionPane.showMessageDialog(null, "Der kürzeste Weg konnte nicht gefunden werden.");
                    }
                    else
                    {
                        AlgorithmConsole console = new AlgorithmConsole(_rootVertex, _targetVertex, shortestW, ""+_graphAccesses, "" + _benoetigteKanten, ""+_wegLaenge);
                        console.start();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(_ui.getDialog(),
                            "Die Eingaben waren nicht korrekt."
                            + " Die Vertexes konnten nicht gefunden werden. "
                            + "Bitte überprüfen Sie Ihre Eingaben.");
                  
                }
            }
        });
        
    }
    
    public void showUI()
    {
        _ui.getDialog().setVisible(true);
    }
    
    private boolean eingabenKorrekt(String vertexSource, String vertexTarget)
    {
        boolean bool = false;
        boolean sourceOK = false;
        boolean targetOK = false;
        Set<Vertex> vertexes = _graph.vertexSet();

        if(vertexSource.equals("") || vertexTarget.equals(""))
        {
            return false;
        }
        else{
            for(Vertex v : vertexes)
            {
                if(v.getName().equals(vertexSource))
                {
                    sourceOK = true;
                    _rootVertex = v;
                }
                if(v.getName().equals(vertexTarget))
                {
                    targetOK = true;
                    _targetVertex = v;
                    _targetVertex.setTarget();
                }
            }
            if(sourceOK && targetOK)
            {
                bool = true;
            }
        }
        
        return bool;
    }
}
