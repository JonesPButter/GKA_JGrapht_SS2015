package werkzeuge.algorithmen.prim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import junit.framework.Assert;
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
        assert isGraphConnected(graph) == true : "Vorbedingung verletzt: isGraphConnected(graph) == true";
        
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
            _simplePrimGraph.addVertex(v);
        }
        
        Vertex tempVertex = _simplePrimGraphVertices.get(0);      
        _schlüssel.put(tempVertex, 0.0); // schlüssel[r] = 0
        _prioQueue.add(tempVertex);
        double kantenGewicht;
        MyWeightedEdge edge = null;
        Vertex child= null;
        
        while(!_prioQueue.isEmpty())
        {   
            _graphAccesses++;
            tempVertex = _prioQueue.remove();           
            
            for(MyWeightedEdge e : _eingabeGraph.edgesOf(tempVertex))
            {
                _graphAccesses++;
                kantenGewicht = e.getEdgeWeight();
                if(_eingabeGraph.getEdgeSource(e).equals(tempVertex))
                {
                    child = _eingabeGraph.getEdgeTarget(e);
                } 
                else
                {
                    child = _eingabeGraph.getEdgeSource(e);
                }
                if(_prioQueue.contains(child) && kantenGewicht < _schlüssel.get(child))
                {
                    vorgaenger.put(child, tempVertex);
                    _schlüssel.put(child, kantenGewicht);
                    
                }
                else if(vorgaenger.get(child) == null)
                {
                    vorgaenger.put(child, tempVertex);
                    _schlüssel.put(child, kantenGewicht);
                    _prioQueue.add(child);
                }
            }            
        }

        endTime = System.nanoTime();
        System.out.println(endTime);
        long erg = endTime - startTime;
        System.out.println("Erg: "  + erg);
        for(Vertex v : _simplePrimGraphVertices)
        {
            _graphAccesses++;
            if(vorgaenger.get(v) != null)
            {
                _kantenAnzahl++;
                edge = _eingabeGraph.getEdge(v, vorgaenger.get(v));
                _simplePrimGraph.addEdge(v, vorgaenger.get(v), edge);
            }           
        }
        _eingabeGraph = _simplePrimGraph;
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
        return _kantenAnzahl-1;
    }

    public int getAccesses()
    {
        return _graphAccesses;
    }

    
}
