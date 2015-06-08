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
    List<Vertex> _okList; 
    List<Vertex> _falseList;
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
        _okList = new ArrayList<>();
        _falseList = new ArrayList<>();
        _vorgaengerMap = new HashMap<Vertex, Vertex>();
        _benoetigteKanten =0;
        _graphAccesses = 0;
        _wegLaenge=0;
        _ui = new DijkstraUI();
        faerbungenZuruecksetzen();
        registriereListenerAnUI();
    }
    
    /**
     * Findet den kürzesten Weg von source nach target
     * @param source Der Startvertex
     * @param target Der Zielvertex
     * @return Den kürzesten Weg von source nach target
     */
    public List<Vertex> findShortestWay(Vertex source,Vertex target)
    {
        // Preconditions
        assert source != null : "Vorbedingung verletzt: graph != null";
        assert target != null : "Vorbedingung verletzt: graph != null";
        
        List<Vertex> shortestWay = new ArrayList<Vertex>();
        Vertex tempSource = source;

        // nur für die spätere Färbung
        source.visit();
        source.setPartOfShortestWay();
        
        // Initialisierung
        _vorgaengerMap.put(source, source); // Der Vorgänger von Source ist Source.      
        _falseList.add(source);  // Source als vorerst einzigen Wert in _falseList schreiben    
        
        do{
            _graphAccesses++;
            
            // 1.) Suche unter den Knoten v(i) mit OK(i) = false einen Knoten v(h) mit dem kleinsten Wert von Entf(i)
            tempSource = getVertexWithShortestDist(_falseList); 
            
            // 2.) Setze OK(h) = true
            _okList.add(tempSource);
            _falseList.remove(tempSource);
            
            // 3.) Für alle Knoten v(j) mit OK(j) = false, für die die Kante v(h),v(j) exisitiert die Entfernung und gegebenenfalls den Vorgänger neuberechnen
            calculateNeighboursDistance(tempSource);
        } while(!_falseList.isEmpty() || !_okList.contains(target));         
            
        if(_okList.contains(target))
        {
            calculateShortestWay(shortestWay,source,target);            
        }
        return shortestWay;
    }
    
    /*
     * liefert den Knoten mit OK = false mit dem kleinsten Wert von Entf
     */
    private Vertex getVertexWithShortestDist(List<Vertex> falseList)
    {
        _graphAccesses++;
        Vertex result = null;
        for(Vertex v : falseList)
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

    /*
     * Berechnet den kürzesten Weg von source nach target und speichert ihn in shortestWay
     */
    private void calculateShortestWay(List<Vertex> shortestWay, Vertex source, Vertex target)
    {
        boolean startVertex = false;
        Vertex tempTarget = target;
        Vertex vorgänger = null;
        shortestWay.add(tempTarget); // target in die Liste hinzufügen
        _wegLaenge = tempTarget.getEntfernungVomStartVertex(); // die Länge des Weges von source nach target
        
        // von Target rückwärts anhand der gespeicherten Vorgänger den Weg finden
        while(!startVertex) // solange wir den startVertex noch nicht erreicht haben
        {
            tempTarget.setPartOfShortestWay();
            _benoetigteKanten++;
            vorgänger = _vorgaengerMap.get(tempTarget);
            shortestWay.add(0,vorgänger); // den Vorgänger dem kürzesten weg hinzufügen (vorne anfügen)
            if(vorgänger.equals(source)) // wenn wir den startvertex gefunden haben, beenden wir die schleife
            {
                startVertex = true;
            }
            tempTarget = vorgänger;
        }        
    }
    
//    private void calculateNeighboursDistance(Vertex source)
//    {
//        Set<Vertex> neighbours = getUndirectedAdjacentNodes(source);
//        double entf=0;
//        _graphAccesses++;
//        for(Vertex child : neighbours)
//        {
//            if(!_okList.contains(child)) // child wird nur angeschaut, wenn es noch nicht in der _okMap steht, damit wir nicht "rückwärts" schauen
//            {                  
//
//                MyWeightedEdge e = _graph.getEdge(source, child); // die Kante zwischen source und child                  
//                entf = source.getEntfernungVomStartVertex() + e.getEdgeWeight(); // entf = 0 + Kanntengewicht
//                
//                if(_falseList.contains(child)) // wenn Child bereits beobachtet wurde (steht also auch in _falseMap)
//                {
//                    if(child.getEntfernungVomStartVertex() > entf) // wenn alter Weg vom Child länger dauert, als der Weg über diesen Knoten
//                    {
//                        _vorgaengerMap.put(child, source); // neuen Vorgänger für child
//                        child.setEntfernungVomStartVertex(entf);  // neue Entfernung für child                  
//                    }
//                }
//                else// if(!child.isVisited()) // child wurde noch nie beobachtet, also einfach Werte berechnen
//                {
//                    _falseList.add(child);
//                    _vorgaengerMap.put(child, source);
//                    child.setEntfernungVomStartVertex(entf);
//                    child.visit();
//                }
//            }
//        }
//    }
    
  private void calculateNeighboursDistance(Vertex source)
  {
      Set<Vertex> neighbours = getUndirectedAdjacentNodes(source);
      double entf=0;
      Vertex start = null;
      Vertex ziel = null;
      Vertex child = null;
      _graphAccesses++;
      for(MyWeightedEdge edge : _graph.edgesOf(source))
      {
          start = _graph.getEdgeSource(edge);
          ziel = _graph.getEdgeTarget(edge);
          if(source.equals(start))
          {
              child = ziel;
          }
          else if(source.equals(ziel))
          {
              child = start;
          }
          
          if(!_okList.contains(child)) // child wird nur angeschaut, wenn es noch nicht in der _okMap steht, damit wir nicht "rückwärts" schauen
          {                                   
              entf = source.getEntfernungVomStartVertex() + edge.getEdgeWeight(); // entf = 0 + Kanntengewicht
              
              if(_falseList.contains(child)) // wenn Child bereits beobachtet wurde (steht also auch in _falseMap)
              {
                  if(child.getEntfernungVomStartVertex() > entf) // wenn alter Weg vom Child länger dauert, als der Weg über diesen Knoten
                  {
                      _vorgaengerMap.put(child, source); // neuen Vorgänger für child
                    child.setEntfernungVomStartVertex(entf);  // neue Entfernung für child                  
                  }
              }
              else// if(!child.isVisited()) // child wurde noch nie beobachtet, also einfach Werte berechnen
              {
                  _falseList.add(child);
                  _vorgaengerMap.put(child, source);
                  child.setEntfernungVomStartVertex(entf);
                  child.visit();
              }
          }
      }
  }
    
    /*
     * Liefert alle Nachbarn von n
     */
    private Set<Vertex> getUndirectedAdjacentNodes(Vertex n)
    {
        Set<Vertex> adjacentNodes= new HashSet<Vertex>();
        Set<MyWeightedEdge> edges= _graph.edgesOf(n);
       for(MyWeightedEdge edge : edges)
       {
//           _graphAccesses++;
           Vertex source = _graph.getEdgeSource(edge);
           Vertex neighbour = _graph.getEdgeTarget(edge);
//           System.out.println("EdgeSource: " + source + " Edgetarget: " + neighbour);
           if(source.equals(n))
           {
               adjacentNodes.add(neighbour);               
           } else if(neighbour.equals(n))
           {
               adjacentNodes.add(source);
           }
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
//                System.out.println("**************** DIJKSTRA ******************** ");
                String vertexSource = _ui.getVertexSource().getText();
                String vertexTarget = _ui.getVertexTarget().getText();
 
                if(eingabenKorrekt(vertexSource,vertexTarget))
                {
                    _ui.getDialog().dispose();
                    String shortestW = findShortestWay(_rootVertex,_targetVertex).toString();  
                    informiereUeberAenderung(_graph);
                    if(shortestW.equals("[]"))
                    {
                        JOptionPane.showMessageDialog(null, "Der kürzeste Weg konnte nicht gefunden werden.");
                    }
                    else
                    {
                        AlgorithmConsole console = new AlgorithmConsole(_rootVertex, _targetVertex, shortestW, ""+_graphAccesses, "" + _benoetigteKanten, ""+_wegLaenge, "Dijkstra");
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
    
    public boolean eingabenKorrekt(String vertexSource, String vertexTarget)
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
    
    public Vertex getRoot()
    {
        return _rootVertex;
    }
    
    public Vertex getTarget()
    {
        return _targetVertex;
    }
    
    public double getWeglaenge()
    {
        return _wegLaenge;
    }
}
