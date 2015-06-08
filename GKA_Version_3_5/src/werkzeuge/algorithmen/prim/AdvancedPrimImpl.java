package werkzeuge.algorithmen.prim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.jgrapht.graph.WeightedPseudograph;
import org.jgrapht.util.FibonacciHeap;
import org.jgrapht.util.FibonacciHeapNode;

public class AdvancedPrimImpl
{

    Graph<Vertex,MyWeightedEdge> _eingabeGraph;
    Graph<Vertex,MyWeightedEdge> _advancedPrimGraph;

    FibonacciHeap<Vertex> _fibHeap;
    Map<Vertex,FibonacciHeapNode<Vertex>> _fibInhalt;
    List<Vertex> _simplePrimGraphVertices;
    Map<Vertex,Double> _schlüssel;
    long startTime;
    long endTime;
    private int _graphAccesses;
    
    public AdvancedPrimImpl(Graph<Vertex, MyWeightedEdge> graph) throws IllegalArgumentException
    {
        if(!isGraphConnected(graph)) throw new IllegalArgumentException("Der Graph ist nicht zusammenhängend.");
//        assert graph.vertexSet().size() > 1 : "Vorbedingung verletzt: graph.vertexSet().size() > 1";
//        assert graph.edgeSet().size() > 0 : "Vorbedingung verletzt: graph.edgeSet().size() > 0";
//        assert isGraphConnected(graph) == true : "Vorbedingung verletzt: isGraphConnected(graph)";
        
        _eingabeGraph = graph;
        _advancedPrimGraph = new WeightedPseudograph<>(MyWeightedEdge.class);
        _simplePrimGraphVertices = new ArrayList<Vertex>(_eingabeGraph.vertexSet());
        _schlüssel = new HashMap<>();
        _fibInhalt = new HashMap<>();
        _fibHeap = new FibonacciHeap<>();
        startAlgorithm();
        
    }
    
    private void startAlgorithm()
    {
        startTime = System.nanoTime();
        for(Vertex v : _simplePrimGraphVertices)
        {
            _graphAccesses++;
            _schlüssel.put(v, Double.POSITIVE_INFINITY);
//            _advancedPrimGraph.addVertex(v);
        }        
        Vertex start = _eingabeGraph.vertexSet().iterator().next();
        _schlüssel.put(start,0.0);
        _advancedPrimGraph.addVertex(start);
//        System.out.println("Start: " + start);
        insertNeighboursIntoHeap(start);
        
        while(!_fibHeap.isEmpty())//for(int i=0; i<_eingabeGraph.vertexSet().size()-1;i++)
        {
//            System.out.println("****************************************");
//            System.out.println(_fibHeap);
            _graphAccesses++;
            Vertex minVertex = _fibHeap.removeMin().getData();
            Vertex target = getBestNeighbour(minVertex);
            MyWeightedEdge edge =  getMinimumEdgeFor(minVertex,target);
//            System.out.println("Minvertex: " + minVertex);
//            System.out.println("Target: " + target);
//            System.out.println("Edge: " + edge);
            
            _advancedPrimGraph.addVertex(minVertex);
            _advancedPrimGraph.addEdge(minVertex, target, edge);
            
            insertNeighboursIntoHeap(minVertex);
        }
        endTime = System.nanoTime();
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

    private Vertex getBestNeighbour(Vertex minVertex)
    {
//        System.out.println("_________ BESTNEIGHBOUR ______________");
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
            
            if(_advancedPrimGraph.containsVertex(child))
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

    private void insertNeighboursIntoHeap(Vertex start)
    {
        Vertex source;
        Vertex target;
        Vertex child = null;
        double kantenGewicht;
        for(MyWeightedEdge edge : _eingabeGraph.edgesOf(start))
        {
            _graphAccesses++;
            source = _eingabeGraph.getEdgeSource(edge);
            target = _eingabeGraph.getEdgeTarget(edge);
            child = null;
            kantenGewicht = edge.getEdgeWeight();
            
            if(source.equals(start))
            {
                child = target;
            }
            else if(target.equals(start))
            {
                child = source;
            }
            
            if(_advancedPrimGraph.containsVertex(child)) continue;
            if(!_fibInhalt.containsKey(child))// && kantenGewicht < _schlüssel.get(child))
            {
//                System.out.println("Child: " + child + " mit Gewicht: " + kantenGewicht + " wird in Heap geschrieben");
                _fibInhalt.put(child,new FibonacciHeapNode<Vertex>(child));
                _schlüssel.put(child, kantenGewicht);
                _fibHeap.insert(_fibInhalt.get(child), kantenGewicht);                
            }
            else if(_schlüssel.get(child) > kantenGewicht)
            {
//                System.out.println("alter Wert von Child größer als der Neue, also Gewichtung ändern: " + child + "Gewicht: " + kantenGewicht);
                _fibHeap.decreaseKey(_fibInhalt.get(child), kantenGewicht);
                _schlüssel.put(child, kantenGewicht);
            }            
        }
    }
    
    public Graph<Vertex, MyWeightedEdge> getGraph()
    {
        return _advancedPrimGraph;
    }
    
    public long getTime()
    {
        return endTime-startTime;
    }
    
    public double getWeglaenge()
    {
        double laenge = 0;
        for(MyWeightedEdge edge : _advancedPrimGraph.edgeSet())
        {
            laenge += edge.getEdgeWeight();
        }
        return laenge;
    }

    public int getAnzahlBenoetigteKanten()
    {
        int laenge = 0;
        for(MyWeightedEdge edge : _advancedPrimGraph.edgeSet())
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
        
//      System.out.println("Connected: " + result);
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
