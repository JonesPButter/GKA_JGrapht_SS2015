package werkzeuge.algorithmen.kruskal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import materialien.MyWeightedEdge;
import materialien.MyWeightedEdgeComparator;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.jgrapht.graph.WeightedPseudograph;

import werkzeuge.algorithmen.dijkstra.DijkstraImpl;

public class KruskalImpl
{

    private Graph<Vertex, MyWeightedEdge> _eingabeGraph;
    private Graph<Vertex, MyWeightedEdge> _kruskalGraph;
    private DijkstraImpl _dijkstraAlgorithm;
    private List<MyWeightedEdge> _edges;
    private List<Vertex> _vertexList;
    
    /**
     * Eine Implementation des Kruskal Algorithmus
     * @param graph Der Graph auf dem der Kruskal angewendet werden soll
     * @require (graph instanceof WeightedPseudograph)
     */
    public KruskalImpl(Graph<Vertex, MyWeightedEdge> graph)
    {
        assert (graph instanceof WeightedPseudograph) : "Vorbedingung verletzt: (graph instanceof WeightedPseudograph)";
        
        _eingabeGraph = graph;
        _kruskalGraph = new WeightedPseudograph<>(MyWeightedEdge.class);
        _vertexList = new ArrayList<>();
        _edges = new ArrayList<>();
        
        _vertexList.addAll(_eingabeGraph.vertexSet());
        vertexListHinzufuegen(); // fügt dem neuen Graphen alle Knoten des Eingabegraphen hinzu
        
        _edges.addAll(_eingabeGraph.edgeSet());
        Collections.sort(_edges,new MyWeightedEdgeComparator()); // Kanten aufsteigend sortieren
        
        startAlgorithm();
    }

    /*
     * Fügt dem Kruskalgraphen alle Knoten aus dem Eingabegraphen hinzu
     */
    private void vertexListHinzufuegen()
    {
        for(Vertex v : _vertexList)
        {
            _kruskalGraph.addVertex(v);
        }        
    }

    private void startAlgorithm()
    {
        for(MyWeightedEdge e : _edges)
        {
            Vertex v1 = _eingabeGraph.getEdgeSource(e);
            Vertex v2 = _eingabeGraph.getEdgeTarget(e);
            if(!erzeugtKreis(v1,v2,_kruskalGraph))
            {
                _kruskalGraph.addEdge(v1, v2, e);
            }
        }
        
    }

    private boolean erzeugtKreis(Vertex v1, Vertex v2,
            Graph<Vertex, MyWeightedEdge> kruskalGraph)
    {
        _dijkstraAlgorithm = new DijkstraImpl(kruskalGraph);
        _dijkstraAlgorithm.findShortestWay(v1, v2);
        if(_dijkstraAlgorithm.getWeglaenge() != 0)
        {
            return true;
        }
        return false;
    }

    public Graph<Vertex, MyWeightedEdge> getGraph()
    {
        return _kruskalGraph;
    }

}
