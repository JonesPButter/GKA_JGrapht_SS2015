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
    List<Vertex> _eulerTour;
    List<List<Vertex>> _eulerKreise;
    List<MyWeightedEdge> _edgeSeen;
    List<Vertex> _vertexSeen;
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
        _vertexSeen = new ArrayList<>();
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
//        _vertexSeen.add(tempVertex);
        
        /*
         * Schritt 2: Man erstelle so viele Unterkreise, bis alle Kanten von einem 
         *                          Unterkreis durchlaufen wurden.
         */
        while(!_edgeSeen.containsAll(_graph.edgeSet()))
        {
            List<Vertex> eulerKreis = getEulerKreisFor(tempVertex);
            _eulerKreise.add(eulerKreis);
            
            tempVertex = getNextNodeWithBiggerDegreeThan0(tempVertex,_vertexSeen);
        }
          
        /*
         * Schritt 3: Den Eulerkreis zusammenbauen
         */ 
        _eulerTour.add(v0);
        erstelleEulertour(v0,_eulerKreise.get(0));
        System.out.println("Eulertour: " + _eulerTour);
    }
    
    /*
     * Schritt 3: Nun erhält man den Eulerkreis, indem man mit
     *              dem ersten Unterkreis beginnt und bei jedem
     *              Schnittpunkt mit einem anderen Unterkreis,
     *              den letzteren einfügt, und danach den ersten
     *              Unterkreis wieder bis zu einem weiteren
     *              Schnittpunkt oder dem Endpunkt fortsetzt.
     */
    private void erstelleEulertour(Vertex start, List<Vertex> eulerKreis)
    {
//        Vertex tempVertex = start;
        for(int i=1;i<eulerKreis.size();i++)
        {
            Vertex vertex = eulerKreis.get(i);
            _eulerTour.add(vertex);
            if(_schnittKnoten.contains(vertex))
            {
                _schnittKnoten.remove(vertex);
                List<Vertex> nextEulerkreis = getCreatedEulerkreisWhereStartIs(vertex);
                erstelleEulertour(vertex,nextEulerkreis);
            }               
//            tempVertex = vertex;
        }
    }
    
    /*
     * TODO Kommentieren!!!!!!!
     */
    private List<Vertex> getCreatedEulerkreisWhereStartIs(Vertex vertex)
    {
        List<Vertex> resultList = null;
        
        for(List<Vertex> eulerkreis : _eulerKreise)
        {
            if(eulerkreis.get(0).equals(vertex))
            {
                resultList = eulerkreis;
                break;
            }
        }        
        _eulerKreise.remove(resultList);
        return resultList;
    }

    private Vertex getNextNodeWithBiggerDegreeThan0(Vertex tempVertex,
            List<Vertex> eulerKreis)
    {
        Vertex result = null;
        for(Vertex v : eulerKreis)
        {
            if(vertexDegreeFor(v,_graph) > 0)
            {
                result = v;
                _schnittKnoten.add(result);
                break;
            }
        }    
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
        } else{
            neighbour = source;        
        }
        return neighbour;
    }

    private List<Vertex> getEulerKreisFor(Vertex start)
    {
        List<Vertex> eulerKreis = new ArrayList<>();
        Vertex tempVertex = start;
        eulerKreis.add(tempVertex); // TODO Startknoten in den Eulerkreis
        boolean kreis = false;
        while(!kreis)
        {
            /*
             * 1.) Suchen der nächsten Kante für den Eulerkreis
             *              - es können nur Kanten ausgewählt werden, die noch nicht betrachtet
             *                  wurden, also noch nicht in einem anderen Eulerkreis vorkommen
             * 2.) Hinzufügen der Kante in den Eulerkreis
             */
            MyWeightedEdge edge = getNextEdgeFor(tempVertex);
            Vertex neighbour = getNeighbour(_graph,edge,tempVertex);     
            eulerKreis.add(neighbour);
            _vertexSeen.add(neighbour);
            
            if(eulerKreis.size()>1)
            {
                if(start.equals(neighbour))
                {
                    kreis = true;
                }                
            }
            tempVertex = neighbour;;
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
        List<MyWeightedEdge> eulertour = new ArrayList<>();
        for(int i=0;i<_eulerTour.size();i++)
        {
            if(i+1 < _eulerTour.size())
            {
                MyWeightedEdge edge = _graph.getEdge(_eulerTour.get(i), _eulerTour.get(i+1));    
                eulertour.add(edge);
            }
        }
        return eulertour;
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
