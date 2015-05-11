package werkzeuge.algorithmen.bfs;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.swing.JOptionPane;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;

import werkzeuge.ObservableSubwerkzeug;

public class BreadthFirstSearchImpl extends ObservableSubwerkzeug
{
    BreadthFirstSearchUserInputUI _ui;
    Graph<Vertex,MyWeightedEdge> _graph; 
    Vertex _rootVertex;
    Vertex _targetVertex;
    int _benoetigteKanten;
    
    public BreadthFirstSearchImpl(Graph<Vertex,MyWeightedEdge> graph)
    {
        _ui = new BreadthFirstSearchUserInputUI();
        _graph = graph;
        _benoetigteKanten = 0;
        registriereListenerAnUI();
        faerbungenZuruecksetzen();
    }

    public void faerbungenZuruecksetzen()
    {
        Set<Vertex> vertexes = _graph.vertexSet();
        for(Vertex v : vertexes)
        {
            v.reset();
        }
        
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
    
    /**
     * 
     * @param graph Der zu traversierende Graph
     * @param source Der Startvertex
     * @param target Der Zielvertex
     * @return Der kürzeste Weg vom Startvertex zum Zielvertex
     */
    public List<Vertex> findShortestWay(Graph<Vertex,MyWeightedEdge> graph, Vertex source, Vertex target)
    {
        assert graph != null : "Vorbedingung verletzt: graph != null";
        assert source != null : "Vorbedingung verletzt: graph != null";
        assert target != null : "Vorbedingung verletzt: graph != null";
        
        List<Vertex> shortestWay = new ArrayList<Vertex>();
        
        Queue<Vertex> q = new LinkedList<>();
        source.visit();      // setze Startvertex auf "gesehen"
        q.add(source);       
  
        if(source.equals(target))
        {
            shortestWay.add(source);
            return shortestWay;
        }
        
        boolean targetGefunden = false;
        while(!q.isEmpty() && !targetGefunden)
        {

            Vertex root = q.remove();
            for(Vertex child : getAdjacentNodes(root))
            {
                if(!child.isVisited())
                {
                    child.setEntfernungVomStartVertex((root.getEntfernungVomStartVertex() + 1));                      
                    child.visit();   
                    q.add(child);
                }
                if(child.isTarget())
                {
                    targetGefunden = true;   
                    break;
                }
            }  
        }    
        
        Vertex currentVertex = target;
        
        int i = (int) target.getEntfernungVomStartVertex();
        if(i>0)shortestWay.add(target); // wenn i nicht größer als 0 ist, dann sind wir ja schon längst beim Ziel angekommen
        while(i>0) // wir gehen von hinten (vom Target) zum StartVertex
        {                        
            for(Vertex neighbour : getBackwardsAdjacentNodes(currentVertex))
            {          
                if(neighbour.getEntfernungVomStartVertex() == i-1 && neighbour.isVisited())
                {
                    shortestWay.add(0,neighbour);
                    neighbour.setPartOfShortestWay(); // für die Färbung
                    currentVertex = neighbour;
                    ++_benoetigteKanten;   
                    break;
                }
            }
            i--;
        }
//        Collections.reverse(shortestWay);
        return shortestWay;        
    }

    private Set<Vertex> getBackwardsAdjacentNodes(Vertex targetVertex)
    {
        Set<Vertex> adjacentNodes = new HashSet<>();
        
        if(_graph instanceof DirectedGraph)
        {
            Set<MyWeightedEdge> edges = ((DirectedGraph<Vertex, MyWeightedEdge>) _graph).incomingEdgesOf(targetVertex);
            for(MyWeightedEdge edge : edges)
            {
//                _graphAccesses++;
                Vertex neighbour = _graph.getEdgeSource(edge);
                adjacentNodes.add(neighbour);
            }
        }
        else return getUndirectedAdjacentNodes(targetVertex);
        return adjacentNodes;
    }

    private Set<Vertex> getAdjacentNodes(Vertex v)
    {
        if(_graph instanceof DirectedGraph)
        {
            return getDirectedAdjacentNodes(v);
        }
        else if(_graph instanceof UndirectedGraph)
        {
            return getUndirectedAdjacentNodes(v);
        }
        else return new HashSet<Vertex>();
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
    
    private Set<Vertex> getDirectedAdjacentNodes(Vertex n)
    {
        Set<Vertex> adjacentNodes= new HashSet<Vertex>();
        Set<MyWeightedEdge> edges= ((DirectedGraph<Vertex,MyWeightedEdge>)_graph).outgoingEdgesOf(n);
       for(MyWeightedEdge edge : edges)
       {
//           _graphAccesses++;
           Vertex neighbour = _graph.getEdgeTarget(edge);
           adjacentNodes.add(neighbour);
       }
       return adjacentNodes;
        
    }

    private void registriereListenerAnUI()
    {
        _ui.getOkButton().addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                
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
                    JOptionPane.showMessageDialog(null, "Der Kürzeste Weg lautet: "
                            + shortestW + ". Anzahl der benötigten Kanten: " + _benoetigteKanten);
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
}
