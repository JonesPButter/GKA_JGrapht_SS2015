package werkzeuge.algorithmen.prim;

import java.util.PriorityQueue;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;

public class SimplePrimImpl
{

    Graph<Vertex,MyWeightedEdge> _graph;
    PriorityQueue<Vertex> _prioQueue;
    
    public SimplePrimImpl(Graph<Vertex, MyWeightedEdge> graph)
    {
        _graph = graph;
//        _prioQueue = new PriorityQueue<>()
        _prioQueue.addAll(_graph.vertexSet());
//        _prioQueue.
    }

    public Graph<Vertex, MyWeightedEdge> getGraph()
    {
        return _graph;
    }

}
