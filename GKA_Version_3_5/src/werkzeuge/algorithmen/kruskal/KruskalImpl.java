package werkzeuge.algorithmen.kruskal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import materialien.MyWeightedEdge;
import materialien.MyWeightedEdgeComparator;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.jgrapht.graph.WeightedPseudograph;

import werkzeuge.algorithmen.bfs.BreadthFirstSearchImpl;
import werkzeuge.algorithmen.dijkstra.DijkstraImpl;

public class KruskalImpl
{

    private Graph<Vertex, MyWeightedEdge> _eingabeGraph;
    private Graph<Vertex, MyWeightedEdge> _kruskalGraph;
    private List<MyWeightedEdge> _edges;
    private List<Vertex> _vertexList;
    long startTime;
    long endTime;
    int _kantenAnzahl;
    private int _graphAccesses;
    
    /**
     * Eine Implementation des Kruskal Algorithmus
     * @param graph Der Graph auf dem der Kruskal angewendet werden soll
     * @require (graph instanceof WeightedPseudograph)
     */
    public KruskalImpl(Graph<Vertex, MyWeightedEdge> graph)
    {
        assert (graph instanceof WeightedPseudograph) : "Vorbedingung verletzt: (graph instanceof WeightedPseudograph)";
        assert isGraphConnected(graph) == true : "Vorbedingung verletzt: isGraphConnected(graph) == true";
        
        _eingabeGraph = graph;
        _kruskalGraph = new WeightedPseudograph<>(MyWeightedEdge.class);
        _vertexList = new ArrayList<>();
        _edges = new ArrayList<>();
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
        startTime = System.nanoTime();
    	init();
    	
        for(MyWeightedEdge e : _edges)
        {
            Vertex v1 = _eingabeGraph.getEdgeSource(e);
            Vertex v2 = _eingabeGraph.getEdgeTarget(e);
            if(!erzeugtKreis(v1,v2,_kruskalGraph))
            {
                _kantenAnzahl++;
                _kruskalGraph.addEdge(v1, v2, e);
            }
        }
        
        endTime = System.nanoTime();
    }

    private void init()
    {       
        _vertexList.addAll(_eingabeGraph.vertexSet());
        vertexListHinzufuegen(); // fügt dem neuen Graphen alle Knoten des Eingabegraphen hinzu
        
        _edges.addAll(_eingabeGraph.edgeSet());
        Collections.sort(_edges,new MyWeightedEdgeComparator()); // Kanten aufsteigend sortieren     
    }

    private boolean erzeugtKreis(Vertex v1, Vertex v2,
            Graph<Vertex, MyWeightedEdge> kruskalGraph)
    {
        BreadthFirstSearchImpl bfs = new BreadthFirstSearchImpl(kruskalGraph);
        List<Vertex> shortestPath = bfs.findShortestWay(kruskalGraph, v1, v2);
        if(!shortestPath.isEmpty())
        {
            return true;
        }      
        return false;
    }

    public Graph<Vertex, MyWeightedEdge> getGraph()
    {
        return _kruskalGraph;
    }
    
    public long getTime()
    {
        return endTime-startTime;
    }
    
    public double getWeglaenge()
    {
        double laenge = 0;
        for(MyWeightedEdge edge : _kruskalGraph.edgeSet())
        {
            laenge += edge.getEdgeWeight();
        }
        return laenge;
    }

    public int getAnzahlBenoetigteKanten()
    {
        int laenge = 0;
        for(MyWeightedEdge edge : _kruskalGraph.edgeSet())
        {
            laenge +=1;
        }
        return laenge;
    }

    public int getAccesses()
    {
        return _graphAccesses;
    }

    public boolean isGraphConnected(Graph<Vertex, MyWeightedEdge> graph)
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
    	
//    	System.out.println("Connected: " + result);
    	return result;
    	
    	
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
