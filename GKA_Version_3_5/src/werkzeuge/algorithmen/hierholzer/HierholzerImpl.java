package werkzeuge.algorithmen.hierholzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;

public class HierholzerImpl
{

    Graph<Vertex,MyWeightedEdge> _graph;
    List<MyWeightedEdge> _eulerTour;
    
    public HierholzerImpl(Graph<Vertex, MyWeightedEdge> graph)
    {
        if(!isConnected(graph) || !onlyEvenDegreesOfVertices(graph))
        {
            throw new IllegalArgumentException("Der Graph ist entweder nicht zusammenhängend"
                    + " oder es haben nicht alle Knoten einen geraden Knotengrad");
        }
        _graph = graph;
        _eulerTour  = new ArrayList<>();
        
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
