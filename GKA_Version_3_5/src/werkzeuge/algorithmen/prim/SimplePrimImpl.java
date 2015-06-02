package werkzeuge.algorithmen.prim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import materialien.MyVertexComparator;
import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.jgrapht.graph.WeightedPseudograph;

public class SimplePrimImpl
{

    Graph<Vertex,MyWeightedEdge> _eingabeGraph;
    Graph<Vertex,MyWeightedEdge> _simplePrimGraph;

    PriorityQueue<Vertex> _prioQueue;
    List<Vertex> _simplePrimGraphVertices;
    
    public SimplePrimImpl(Graph<Vertex, MyWeightedEdge> graph)
    {
        assert graph.vertexSet().size() > 0 : "Vorbedingung verletzt: graph.vertexSet().size() > 0";
        assert graph.edgeSet().size() > 0 : "Vorbedingung verletzt: graph.edgeSet().size() > 0";
        
        _eingabeGraph = graph;
        _simplePrimGraph = new WeightedPseudograph<>(MyWeightedEdge.class);
        _simplePrimGraphVertices = new ArrayList<Vertex>(_eingabeGraph.vertexSet());
        _prioQueue = new PriorityQueue<>(new MyVertexComparator());     
        
        
        startAlgorithm();
    }
    
    private void startAlgorithm()
    {
        Vertex tempVertex = _simplePrimGraphVertices.get(0);
        Map<Vertex,Vertex> vorgaenger = new HashMap<>();
        Vertex nextVertex;
        _simplePrimGraph.addVertex(tempVertex);
        do{
            System.out.println("******************************** ");
            System.out.println("Graphknoten = " + _simplePrimGraph.vertexSet());
            System.out.println("TempVertex: " + tempVertex);
            fuegeNachbarnInQueue(tempVertex,vorgaenger); // Queue wird um alle Vertices erweitert, die Kind von tempvertex sind und noch nicht in der Queue stehen
            nextVertex = _prioQueue.remove();
            System.out.println("NextVertex: " + tempVertex);
            _simplePrimGraph.addVertex(nextVertex);
            
            _simplePrimGraph.addEdge(vorgaenger.get(nextVertex),nextVertex, _eingabeGraph.getEdge(vorgaenger.get(nextVertex), nextVertex));                

            
        }while(!_prioQueue.isEmpty());
        
        _eingabeGraph = _simplePrimGraph;
    }


    private void fuegeNachbarnInQueue(Vertex tempVertex, Map<Vertex,Vertex> vorgaenger)
    {
        Set<Vertex> vertices = getUndirectedAdjacentNodes(tempVertex);
        for(Vertex child : vertices)
        {
            if(!_prioQueue.contains(child) && !_simplePrimGraph.containsVertex(child))
            {
                System.out.println("Child hinzugefügt: " + child);
                _prioQueue.add(child);
                vorgaenger.put(child,tempVertex);
            }
        }    
        System.out.println("QUEUE: " + _prioQueue);
    }

    private MyWeightedEdge getBestEdge(Vertex tempVertex)
    {
        MyWeightedEdge result = null;
        
        Set<MyWeightedEdge> edges = _eingabeGraph.edgesOf(tempVertex);
        
        for(MyWeightedEdge v : edges)
        {
            if(result == null)
            {
                result = v;
            }
            else if(result.getEdgeWeight()>v.getEdgeWeight())
            {
                result = v;
            }
        }        
        return result;
    }

    private boolean mitKnotenVerbunden(MyWeightedEdge e)
    {
        Vertex source = null;
        Vertex target = null;
        
        for(Vertex v : _simplePrimGraphVertices)
        {
            source = _simplePrimGraph.getEdgeSource(e);
            target = _simplePrimGraph.getEdgeTarget(e);
            if(v.equals(source) || v.equals(target))
            {
                return true;
            }
        }
        return false;
    }

    private Set<Vertex> getUndirectedAdjacentNodes(Vertex n)
    {
        Set<Vertex> adjacentNodes= new HashSet<Vertex>();
        Set<MyWeightedEdge> edges= _eingabeGraph.edgesOf(n);
       for(MyWeightedEdge edge : edges)
       {
//           _graphAccesses++;
           Vertex source = _eingabeGraph.getEdgeSource(edge);
           Vertex neighbour = _eingabeGraph.getEdgeTarget(edge);
           System.out.println("Source: " + source);
           System.out.println("Neighbour: " + neighbour);
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
    
    public Graph<Vertex, MyWeightedEdge> getGraph()
    {
        return _simplePrimGraph;
    }

}
