package werkzeuge.algorithmen.hierholzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;

public class HierholzerImpl
{

    Graph<Vertex,MyWeightedEdge> _graph;
    List<MyWeightedEdge> _eulerTour;
    List<List<MyWeightedEdge>> _eulerKreise;
    Map<MyWeightedEdge,Boolean> _edgeSeen;
    
    public HierholzerImpl(Graph<Vertex, MyWeightedEdge> graph)
    {
        if(!isConnected(graph) || !onlyEvenDegreesOfVertices(graph))
        {
            throw new IllegalArgumentException("Der Graph ist entweder nicht zusammenhängend"
                    + " oder es haben nicht alle Knoten einen geraden Knotengrad");
        }
        _graph = graph;
        _eulerTour  = new ArrayList<>();
        _eulerKreise = new ArrayList<>();
        _edgeSeen = new HashMap<>();
        startAlgorithm();
    }
    
    private void startAlgorithm()
    {
        /*
         * Schritt 1: Man wähle einen beliebigen Knoten v0 in G und setze W0 = v0.
         */
        Vertex v0 = _graph.vertexSet().iterator().next(); 
        Vertex tempVertex = v0;
        
        while(!_eulerTour.containsAll(_graph.edgeSet()))
        {
            List<MyWeightedEdge> eulerKreis = getEulerKreisFor(tempVertex);
            _eulerKreise.add(eulerKreis);
        }
    }
    
    private List<MyWeightedEdge> getEulerKreisFor(Vertex start)
    {
        List<MyWeightedEdge> eulerKreis = new ArrayList<>();
        Vertex tempVertex = start;
        boolean kreis = false;
        int anzahlKnoten = 1;
        while(!kreis)
        {
            /*
             * 1.) Suchen der nächsten Kante für den Eulerkreis
             *              - es können nur Kanten ausgewählt werden, die noch nicht betrachtet
             *                  wurden, also noch nicht in einem anderen Eulerkreis vorkommen
             * 2.) Hinzufügen der Kante in den Eulerkreis
             */
            MyWeightedEdge edge = getNextEdgeFor(tempVertex);
            eulerKreis.add(edge);           
            
            if(anzahlKnoten >= 2)
            {
                Vertex source = _graph.getEdgeSource(edge);
                Vertex target = _graph.getEdgeTarget(edge);
                if(start.equals(source)|| start.equals(target))
                {
                    kreis = true;
                }                
            }
            anzahlKnoten++;
        }    
        return eulerKreis;
    }

    /*
     * Liefert die nächst-beste Kante von einem Knoten unter der Bedingung,
     *                                       dass die Kante noch nicht betrachtet wurde
     */
    private MyWeightedEdge getNextEdgeFor(Vertex start)
    {
        MyWeightedEdge resultEdge = null;
        for(MyWeightedEdge edge : _graph.edgesOf(start))
        {
            if(!_edgeSeen.containsKey(edge))
            {
                resultEdge = edge;
                break;                
            }
        }
        return resultEdge;
    }

    public List<MyWeightedEdge> getEulertour()
    {       
        return _eulerTour;
    }
    
    public boolean isConnected(Graph<Vertex, MyWeightedEdge> graph)
    {
        boolean result = false;

        Vertex start = (Vertex) graph.vertexSet().iterator().next();
        Vertex add;
        Set<Vertex> neighbours = getNeighbours(graph, start);
        Set<Vertex> allVertices = new HashSet<Vertex>();
        allVertices.add(start);
        allVertices.addAll(neighbours);
   
        while(!neighbours.isEmpty())
        {
            while(neighbours.iterator().hasNext())
            {
                add = neighbours.iterator().next();
                for(Vertex v : getNeighbours(graph, add))
                {
                    if(!allVertices.contains(v))
                    {
                        allVertices.add(v);
                        neighbours.add(v);
                    }
                }
                neighbours.remove(add);
                
            }
        }

        if(allVertices.equals(graph.vertexSet()))
        {
            result = true;
        }
        
//      System.out.println("Connected: " + result);
        return result; 
    }

    private boolean onlyEvenDegreesOfVertices(Graph<Vertex, MyWeightedEdge> graph)
    {
        int edgeCounter;
        
        for(Vertex v : graph.vertexSet())
        {
            edgeCounter = 0;
            for(MyWeightedEdge edge : graph.edgesOf(v))
            {
                edgeCounter++;
            }
            if((edgeCounter%2) != 0)
            {
                return false;
            }
        }
        return true;
    }
    
    private Set<Vertex> getNeighbours(Graph<Vertex, MyWeightedEdge> graph, Vertex n)
    {
        Set<Vertex> adjacentNodes= new HashSet<Vertex>();
        Set<MyWeightedEdge> edges= graph.edgesOf(n);
       for(MyWeightedEdge edge : edges)
       {
           Vertex source = graph.getEdgeSource(edge);
           Vertex neighbour = graph.getEdgeTarget(edge);
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


}
