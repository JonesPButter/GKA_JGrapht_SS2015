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
    Map<Vertex,Double> _schlüssel;
    long startTime;
    long endTime;
    int _kantenAnzahl;
    private int _graphAccesses;
    
    public SimplePrimImpl(Graph<Vertex, MyWeightedEdge> graph)
    {
        assert graph.vertexSet().size() > 0 : "Vorbedingung verletzt: graph.vertexSet().size() > 0";
        assert graph.edgeSet().size() > 0 : "Vorbedingung verletzt: graph.edgeSet().size() > 0";
        assert isGraphConnected(graph) : "Vorbedingung verletzt: isGraphConnected(graph)";
        
        _eingabeGraph = graph;
        _simplePrimGraph = new WeightedPseudograph<>(MyWeightedEdge.class);
        _simplePrimGraphVertices = new ArrayList<Vertex>(_eingabeGraph.vertexSet());
        _schlüssel = new HashMap<>();
        _prioQueue = new PriorityQueue<>(new MyVertexComparator(_schlüssel));

        _kantenAnzahl=0;
        startAlgorithm();
    }
    
    private void startAlgorithm()
    {
        startTime = System.nanoTime();
        System.out.println(startTime);
        Map<Vertex,Vertex> vorgaenger = new HashMap<>();
        for(Vertex v : _simplePrimGraphVertices)
        {
            _graphAccesses++;
            _schlüssel.put(v, Double.POSITIVE_INFINITY);
            vorgaenger.put(v, null);
        }
        
        Vertex tempVertex = _eingabeGraph.vertexSet().iterator().next();      
        _schlüssel.put(tempVertex, 0.0); // schlüssel[r] = 0
        _simplePrimGraph.addVertex(tempVertex);
        insertNeighboursIntoQueue(tempVertex);
        
        while(!_prioQueue.isEmpty())
        {   
            _graphAccesses++;         
            
            Vertex minVertex = _prioQueue.remove();
            Vertex target = getBestNeighbour(minVertex);
            MyWeightedEdge edge =  getMinimumEdgeFor(minVertex, target);
            System.out.println("Minvertex: " + minVertex);
            System.out.println("Target: " + target);
            System.out.println("Edge: " + edge);
            
            _simplePrimGraph.addVertex(minVertex);
            _simplePrimGraph.addEdge(minVertex, target, edge);
            
            insertNeighboursIntoQueue(minVertex);        
        }

        endTime = System.nanoTime();
    }
    

    private Vertex getBestNeighbour(Vertex minVertex)
    {
        System.out.println("_________ BESTNEIGHBOUR ______________");
        Vertex source;
        Vertex target;
        Vertex child = null;
        Vertex result = null;
        double kantenGewicht = Double.POSITIVE_INFINITY;
        for(MyWeightedEdge edge : _eingabeGraph.edgesOf(minVertex))
        {
            _graphAccesses++;
            source = _eingabeGraph.getEdgeSource(edge);
            target = _eingabeGraph.getEdgeTarget(edge);
            if(source.equals(minVertex))
            {
                child = target;
            }
            else if(target.equals(minVertex))
            {
                child = source;
            }
            
            if(_simplePrimGraph.containsVertex(child))
            {
                if(kantenGewicht >= edge.getEdgeWeight())
                {
                    kantenGewicht = edge.getEdgeWeight();
                    result = child;
                }
            }                 
        }
        return result;
    }

    private void insertNeighboursIntoQueue(Vertex tempVertex)
    {
        Vertex source;
        Vertex target;
        Vertex child = null;
        double kantenGewicht;
        for(MyWeightedEdge edge : _eingabeGraph.edgesOf(tempVertex))
        {
            _graphAccesses++;
            source = _eingabeGraph.getEdgeSource(edge);
            target = _eingabeGraph.getEdgeTarget(edge);
            child = null;
            kantenGewicht = edge.getEdgeWeight();
            
            if(source.equals(tempVertex))
            {
                child = target;
            }
            else if(target.equals(tempVertex))
            {
                child = source;
            }
            
            if(_simplePrimGraph.containsVertex(child)) continue;
            if(!_prioQueue.contains(child))// && kantenGewicht < _schlüssel.get(child))
            {
                System.out.println("Child: " + child + " mit Gewicht: " + kantenGewicht + " wird in Heap geschrieben");
                _schlüssel.put(child, kantenGewicht);
                _prioQueue.add(child);                
            }
            else if(_schlüssel.get(child) > kantenGewicht)
            {
                System.out.println("alter Wert von Child größer als der Neue, also Gewichtung ändern: " + child + "Gewicht: " + kantenGewicht);
                _prioQueue.remove(child);
                _schlüssel.put(child, kantenGewicht);
                _prioQueue.add(child);
            }            
        }
        
    }

    private MyWeightedEdge getMinimumEdgeFor(Vertex minVertex, Vertex target)
    {
        MyWeightedEdge minEdge = _eingabeGraph.getEdge(minVertex, target);
        double kantenGewicht = minEdge.getEdgeWeight();
        for(MyWeightedEdge edge :_eingabeGraph.getAllEdges(minVertex, target))
        {
            if(edge.getEdgeWeight()<kantenGewicht)
            {
                minEdge = edge;
            }
        }
        return minEdge;
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
//
//    private Set<Vertex> getUndirectedAdjacentNodes(Vertex n)
//    {
//        Set<Vertex> adjacentNodes= new HashSet<Vertex>();
//        Set<MyWeightedEdge> edges= _eingabeGraph.edgesOf(n);
//       for(MyWeightedEdge edge : edges)
//       {
////           _graphAccesses++;
//           Vertex source = _eingabeGraph.getEdgeSource(edge);
//           Vertex neighbour = _eingabeGraph.getEdgeTarget(edge);
////           System.out.println("Source: " + source);
////           System.out.println("Neighbour: " + neighbour);
//           if(source.equals(n))
//           {
//               adjacentNodes.add(neighbour);               
//           } else if(neighbour.equals(n))
//           {
//               adjacentNodes.add(source);
//           }
//       }
//       return adjacentNodes;
//        
//    }
    
    public Graph<Vertex, MyWeightedEdge> getGraph()
    {
        return _simplePrimGraph;
    }
    
    public long getTime()
    {
        return endTime-startTime;
    }
    
    public double getWeglaenge()
    {
        double laenge = 0;
        for(MyWeightedEdge edge : _simplePrimGraph.edgeSet())
        {
            laenge += edge.getEdgeWeight();
        }
        return laenge;
    }

    public int getAnzahlBenoetigteKanten()
    {
        int laenge = 0;
        for(MyWeightedEdge edge : _simplePrimGraph.edgeSet())
        {
            laenge += 1;
        }
        return laenge;
    }

    public int getAccesses()
    {
        return _graphAccesses;
    }

    
}
