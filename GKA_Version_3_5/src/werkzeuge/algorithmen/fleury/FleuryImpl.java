package werkzeuge.algorithmen.fleury;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;

import werkzeuge.algorithmen.bfs.BreadthFirstSearchImpl;

public class FleuryImpl
{
    
    Graph<Vertex,MyWeightedEdge> _graph;
    List<MyWeightedEdge> _eulerTour;

    public FleuryImpl(Graph<Vertex, MyWeightedEdge> graph) throws IllegalArgumentException
    {
        if(!isConnected(graph) || !onlyEvenDegreesOfVertices(graph))
        {
            throw new IllegalArgumentException("Der Graph ist entweder nicht zusammenhängend"
                    + " oder es haben nicht alle Knoten einen geraden Knotengrad");
        }
        _graph = graph;
        _eulerTour  = new ArrayList<>();
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
            /*
             * Schritt 2: Eine passende Kante wählen, für die gilt: 
             *                            a)- sie ist keine Schnittkante
             *                              - es sei denn, es gibt keine Alternative
             *                            b)- sie ist noch nicht in der Eulertour
             *                              (da wir die Kanten immer aus dem Graphen entfernen,
             *                               kann dieser Fall gar nicht eintreten)
             */
            MyWeightedEdge nextEdge = getNextEdge(tempVertex);
            _graph.removeEdge(nextEdge);
            _eulerTour.add(nextEdge);
            
            // tempVertex bestimmen
            Vertex source = _graph.getEdgeSource(nextEdge);
            Vertex target = _graph.getEdgeTarget(nextEdge);
            if(source.equals(tempVertex))
            {
                tempVertex = target;
            }
            else tempVertex = source;
        }
    }

    private MyWeightedEdge getNextEdge(Vertex tempVertex)
    {
        MyWeightedEdge resultEdge = null;
        List<MyWeightedEdge> edges = new ArrayList<>(_graph.edgesOf(tempVertex));
        for(int i=0;i<edges.size();i++)
        {
            MyWeightedEdge edge = edges.get(i);
            // Die Kante ist keine Schnittkante oder es gibt keine andere Alternative
            if(!isSchnittkante(edge,tempVertex) || i==edges.size()-1){
                resultEdge = edge;
                break;
            }
        }        
        return resultEdge;
    }


    private boolean isSchnittkante(MyWeightedEdge edge, Vertex tempVertex)
    {
        boolean result = false;
        Vertex neighbour;
        Vertex source = _graph.getEdgeSource(edge);
        Vertex target = _graph.getEdgeTarget(edge);
        // Hier wird nur der Nachbar gesucht
        if(source.equals(tempVertex))
        {
            neighbour = target;
        }
        else neighbour = source;
       
        /*
         * 1.) Kante entfernen, um zu prüfen, ob es noch einen Alternativen Weg
         *                                       von neighbour zu tempVertex zurück gibt
         * 2.) Finden wir keinen kürzesten Weg von Neighbour zurück zu tempVertex,
         *                                       handelt es sich um eine Schnittkante
         * 3.) In jedem Fall wird die Kante wieder hinzugefügt 
         *                                (Hier soll ja nur geprüft werden, was wäre wenn...)
         * 
         */
        _graph.removeEdge(edge); 
        BreadthFirstSearchImpl bfs = new BreadthFirstSearchImpl(_graph);
        if(bfs.findShortestWay(_graph, neighbour, tempVertex).isEmpty())
        {
            result = true;
        }
        
        _graph.addEdge(source, neighbour,edge);
        return result;
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
