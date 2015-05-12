package werkzeuge.algorithmen.aStern;

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

import werkzeuge.ObservableSubwerkzeug;

public class ASternImpl extends ObservableSubwerkzeug
{
    Graph<Vertex,MyWeightedEdge> _graph; 
    Vertex _rootVertex;
    Vertex _targetVertex;
    Map<Vertex,String> _okMap; 
    Map<Vertex,String> _falseMap;
    Map<Vertex,Vertex> _vorgaengerMap; // map<key,val>
    Map<Vertex,Double> _schätzwerte; // berechnet Schätzwert
    int _benoetigteKanten;
    double _wegLaenge;
    ASternUI _ui;
    
    /**
     * 
     * @param graph Der zu traversierende Graph
     * 
     * @require ((graph instanceof UndirectedAttributedWeightedGraph) || (graph instanceof WeightedPseudograph))
     * 
     * @throws IllegalArgumentException
     */
    public ASternImpl(Graph<Vertex,MyWeightedEdge> graph)
    {
        if(!(graph instanceof UndirectedAttributedWeightedGraph))
        {
            throw new IllegalArgumentException("Dies ist kein gültiger Graph, um den Dijkstra-Algorithmus auszuführen");
        }
        
        _graph = graph;
        _okMap = new HashMap<>();
        _falseMap = new HashMap<Vertex, String>();
        _vorgaengerMap = new HashMap<Vertex, Vertex>();
        _benoetigteKanten =0;
        _wegLaenge=0;
        _schätzwerte  = new HashMap<>();
        _ui = new ASternUI();
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
        tempSource.visit();
        tempSource.setPartOfShortestWay();
        _vorgaengerMap.put(tempSource, tempSource); // Der Vorgänger von Source ist Source.      
        tempSource.setEntfernungVomStartVertex(0);
        _schätzwerte.put(tempSource, tempSource.getAttr());
        _okMap.put(tempSource, "ok");
        
        while(tempSource != null && (!_okMap.containsKey(target)||!_falseMap.isEmpty()))
        {
            calculateNeighboursDistance(tempSource); // fügt die Nachbarn in die FalseMap und berechnet Entfernung
            
            // TODO Liefert falsches Ergebnis für Beispiel Aufgabe 25 S.40 Foliensatz 4,
            //wenn man an den Knoten V4 noch einen Knoten V10 mit dem Attribut 7 hängt und
            //von dem auf v8 mit der 3 geht. Genauso beim Dijkstra
            tempSource = getVertexWithSmallestF(_falseMap);
            _okMap.put(tempSource, "ok");
            _falseMap.remove(tempSource);
            
        }
        
        calculateShortestWay(shortestWay,source,target);
        return shortestWay;
    }

    /*
     * Liefert immer den Vertex mit dem geringsten Schätzwert
     */
    private Vertex getVertexWithSmallestF(Map<Vertex, String> falseMap)
    {
        Set<Vertex> falseMapSet = falseMap.keySet();
        Vertex result = null;
        double f;
        for(Vertex v : falseMapSet)
        {
            f = (v.getAttr()+ _schätzwerte.get(v));
            if(result == null)
            {
                result = v;
            }
            if(f < ((result.getAttr() + _schätzwerte.get(result))))
            {
                result = v;
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
        
    private void calculateNeighboursDistance(Vertex source)
    {
        Set<Vertex> neighbours = getUndirectedAdjacentNodes(source);
        double entf;
        double schätzwertF;
        
        for(Vertex child : neighbours)
        {             
            MyWeightedEdge e = _graph.getEdge(source, child);           
            entf = source.getEntfernungVomStartVertex() + e.getEdgeWeight(); // entf = 0 + Kanntengewicht
            schätzwertF = entf + child.getAttr();
            
            
            if(_falseMap.containsKey(child)) // wenn Child in _falseMap 
            {
                if(child.getEntfernungVomStartVertex() > entf) // wenn alter Weg vom Child länger dauert, als der Weg über diesen Knoten
                {
                    _vorgaengerMap.put(child, source); // neuen Vorgänger für child
                    child.setEntfernungVomStartVertex(entf);  // neue Entfernung für child
                    _schätzwerte.put(child, schätzwertF); // neuen Schätzwert für child
                }
            }
            else{
                _falseMap.put(child,"false");
                _vorgaengerMap.put(child, source);
                child.setEntfernungVomStartVertex(entf);
                _schätzwerte.put(child, schätzwertF);
                child.visit();
            }                 
//            if(child.isVisited())
//            {
//                if(child.getEntfernungVomStartVertex() > entf)
//                {
//                    _vorgaengerMap.put(child, source);
//                    child.setEntfernungVomStartVertex(entf); 
//                    _schätzwerte.put(child, schätzwertF);
//                }
//            }
//            else if(!child.isVisited())
//            {
//                _vorgaengerMap.put(child, source);
//                child.setEntfernungVomStartVertex(entf);
//                _schätzwerte.put(child, schätzwertF);
//            }
//            child.visit();
        }
    }

    // Liefert alle Nachbarn, die noch nicht in der OKMap stehen
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
               if(!_okMap.containsKey(neighbour))
               {
                   adjacentNodes.add(neighbour);   
               }
           } else if(neighbour.equals(n))
           {
               if(!_okMap.containsKey(source))
               {
                   adjacentNodes.add(source);
               }
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
                    JOptionPane.showMessageDialog(null, "Der Kürzeste Weg von: "+ _rootVertex +" nach: "+ _targetVertex +" lautet: "
                            + shortestW + ".\n Anzahl der benötigten Kanten: " + _benoetigteKanten
                            + "\n Die Entfernung der beiden Knoten beträgt: " + _wegLaenge);
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
