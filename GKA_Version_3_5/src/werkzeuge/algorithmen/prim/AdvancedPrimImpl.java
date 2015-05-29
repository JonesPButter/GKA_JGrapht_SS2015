package werkzeuge.algorithmen.prim;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;

public class AdvancedPrimImpl
{

    Graph<Vertex,MyWeightedEdge> _graph;
    
    public AdvancedPrimImpl(Graph<Vertex, MyWeightedEdge> graph)
    {
        _graph = graph;
    }

    public Graph<Vertex, MyWeightedEdge> getGraph()
    {
        return _graph;
    }

}
