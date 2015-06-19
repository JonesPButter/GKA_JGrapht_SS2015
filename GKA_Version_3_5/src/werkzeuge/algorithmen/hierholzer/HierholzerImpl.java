package werkzeuge.algorithmen.hierholzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.jgrapht.experimental.GraphTests;

public class HierholzerImpl
{

    Graph<Vertex,MyWeightedEdge> _graph;
    List<MyWeightedEdge> _eulerTour;
    List<List<MyWeightedEdge>> _eulerKreise;
    List<MyWeightedEdge> _edgeSeen;
    List<Vertex> _schnittKnoten;
    
    public HierholzerImpl(Graph<Vertex, MyWeightedEdge> graph)
    {
        _graph = graph;
        _edgeSeen = new ArrayList<>();
        if(!GraphTests.isConnected(graph) || !onlyEvenDegreesOfVertices(graph))
        {
            throw new IllegalArgumentException("Der Graph ist entweder nicht zusammenhängend"
                    + " oder es haben nicht alle Knoten einen geraden Knotengrad");
        }
        _eulerTour  = new ArrayList<>();
        _eulerKreise = new ArrayList<>();
        _schnittKnoten = new ArrayList<>();
        startAlgorithm();
    }
    
    private void startAlgorithm()
    {
        /*
         * Schritt 1: Man wähle einen beliebigen Knoten v0 in G und setze W0 = v0.
         */
        Vertex v0 = _graph.vertexSet().iterator().next(); 
        Vertex tempVertex = v0;
        
        /*
         * Schritt 2: Man erstelle so viele Unterkreise, bis alle Kanten von einem 
         *                          Unterkreis durchlaufen wurden.
         */
        while(!_edgeSeen.containsAll(_graph.edgeSet()))
        {
            List<MyWeightedEdge> eulerKreis = getEulerKreisFor(tempVertex);
            _eulerKreise.add(eulerKreis);
            
            tempVertex = getNextNodeWithBiggerDegreeThan0(tempVertex,eulerKreis);
        }
        
        System.out.println(_edgeSeen);
  
        /*
         * Schritt 3: Den Eulerkreis zusammenbauen
         */ 
        erstelleEulertour(v0,_eulerKreise.get(0));
        System.out.println(_eulerTour);
    }
    
    /*
     * Schritt 3: Nun erhält man den Eulerkreis, indem man mit
     *              dem ersten Unterkreis beginnt und bei jedem
     *              Schnittpunkt mit einem anderen Unterkreis,
     *              den letzteren einfügt, und danach den ersten
     *              Unterkreis wieder bis zu einem weiteren
     *              Schnittpunkt oder dem Endpunkt fortsetzt.
     */
    private void erstelleEulertour(Vertex start, List<MyWeightedEdge> eulerKreis)
    {
        Vertex tempVertex = start;
        for(MyWeightedEdge edge : eulerKreis)
        {
            if(!_eulerTour.contains(edge))
            {
                _eulerTour.add(edge);
                Vertex target = getNeighbour(_graph,edge,tempVertex);
                if(_schnittKnoten.contains(target))
                {
                    _schnittKnoten.remove(target);
                    List<MyWeightedEdge> nextEulerkreis = getCreatedEulerkreisWhereStartIs(target);
                    erstelleEulertour(target,nextEulerkreis);
                }               
                tempVertex = target;
            }
        }
    }
    
    private List<MyWeightedEdge> getCreatedEulerkreisWhereStartIs(Vertex vertex)
    {
        List<MyWeightedEdge> resultList = null;
        
        for(List<MyWeightedEdge> eulerkreis : _eulerKreise)
        {
            for(MyWeightedEdge edge : eulerkreis)
            {
                if(_eulerTour.contains(edge))
                {
                    break;
                }
               
                Vertex source = _graph.getEdgeSource(edge);
                Vertex target = _graph.getEdgeTarget(edge);
                if(source.equals(vertex) || target.equals(vertex))
                {
                    return eulerkreis;
                }
            }
        }        
        return resultList;
    }

    private Vertex getNextNodeWithBiggerDegreeThan0(Vertex tempVertex,
            List<MyWeightedEdge> eulerKreis)
    {
        Vertex result = null;
        for(MyWeightedEdge edge : eulerKreis)
        {
            Vertex target = getNeighbour(_graph,edge,tempVertex);
            if(vertexDegreeFor(target,_graph) > 0)
            {
                result = target;
                break;
            }
        }    
        _schnittKnoten.add(result);
        return result;
    }

    private Vertex getNeighbour(Graph<Vertex,MyWeightedEdge> graph, 
            MyWeightedEdge edge, Vertex tempVertex)
    {
        Vertex neighbour = null;       
        Vertex source = graph.getEdgeSource(edge);
        Vertex target = graph.getEdgeTarget(edge);
        if(source.equals(tempVertex))
        {
            neighbour = target;
        } else neighbour = source;        
        return neighbour;
    }

    private List<MyWeightedEdge> getEulerKreisFor(Vertex start)
    {
        List<MyWeightedEdge> eulerKreis = new ArrayList<>();
        Vertex tempVertex = start;
        boolean kreis = false;
        int anzahlKnoten = 1;
        while(!kreis)
        {
            /*
             * 1.) Suchen der nächsten Kante für den Eulerkreis
             *              - es können nur Kanten ausgewählt werden, die noch nicht betrachtet
             *                  wurden, also noch nicht in einem anderen Eulerkreis vorkommen
             * 2.) Hinzufügen der Kante in den Eulerkreis
             */
            MyWeightedEdge edge = getNextEdgeFor(tempVertex);
            eulerKreis.add(edge);           
            
            if(anzahlKnoten >= 2)
            {
                Vertex source = _graph.getEdgeSource(edge);
                Vertex target = _graph.getEdgeTarget(edge);
                if(start.equals(source)|| start.equals(target))
                {
                    kreis = true;
                }                
            }
            tempVertex = getNeighbour(_graph,edge,tempVertex);
            anzahlKnoten++;
        }    
        return eulerKreis;
    }

    /*
     * Liefert die nächst-beste Kante von einem Knoten unter der Bedingung,
     *                                       dass die Kante noch nicht betrachtet wurde.
     */
    private MyWeightedEdge getNextEdgeFor(Vertex start)
    {
        MyWeightedEdge resultEdge = null;
        for(MyWeightedEdge edge : _graph.edgesOf(start))
        {
            if(!_edgeSeen.contains(edge))
            {
                resultEdge = edge;
                break;                
            }
        }
        _edgeSeen.add(resultEdge);
        return resultEdge;
    }

    public List<MyWeightedEdge> getEulertour()
    {       
        return _eulerTour;
    }

    private boolean onlyEvenDegreesOfVertices(Graph<Vertex, MyWeightedEdge> graph)
    {
        int edgeCounter;
        
        for(Vertex v : graph.vertexSet())
        {
            edgeCounter = vertexDegreeFor(v,graph);
            if((edgeCounter%2) != 0)
            {
                return false;
            }
        }
        return true;
    }
    
    /*
     * Liefert den Knotengrad für einen Vertex
     */
    private int vertexDegreeFor(Vertex vertex,Graph<Vertex, MyWeightedEdge> graph)
    {
        int result =0;
        for(MyWeightedEdge edge : graph.edgesOf(vertex))
        {
            if(!_edgeSeen.contains(edge))
            {
                result++;                
            }
        }        
        return result;
    }
    
    private Set<Vertex> getNeighbours(Graph<Vertex, MyWeightedEdge> graph, Vertex n)
    {
        Set<Vertex> adjacentNodes= new HashSet<Vertex>();
        Set<MyWeightedEdge> edges= graph.edgesOf(n);
       for(MyWeightedEdge edge : edges)
       {
           Vertex neighbour = getNeighbour(graph,edge,n);
           adjacentNodes.add(neighbour);               
       }
       return adjacentNodes;        
    }


}
