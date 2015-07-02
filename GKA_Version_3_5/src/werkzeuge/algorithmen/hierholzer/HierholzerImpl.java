package werkzeuge.algorithmen.hierholzer;

import java.util.ArrayList;
import java.util.List;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.jgrapht.experimental.GraphTests;

public class HierholzerImpl
{

    Graph<Vertex,MyWeightedEdge> _graph;
    List<Vertex> _eulerKreis;
    List<List<Vertex>> _unterKreise;
    List<MyWeightedEdge> _edgeSeen;
    List<Vertex> _vertexSeen;
    List<Vertex> _schnittKnoten;
    private int _graphAccesses;
    private long endTime;
    private long startTime;
    
    public HierholzerImpl(Graph<Vertex, MyWeightedEdge> graph)
    {
        _graph = graph;
        _edgeSeen = new ArrayList<>();
        if(graph.vertexSet().size() < 1 || !GraphTests.isConnected(graph) || !onlyEvenDegreesOfVertices(graph))
        {
            throw new IllegalArgumentException("Der Graph ist entweder nicht zusammenhängend"
                    + " oder es haben nicht alle Knoten einen geraden Knotengrad");
        }
        _vertexSeen = new ArrayList<>();
        _eulerKreis  = new ArrayList<>();
        _unterKreise = new ArrayList<>();
        _schnittKnoten = new ArrayList<>();
        startAlgorithm();
    }
    
    private void startAlgorithm()
    {
        startTime = System.nanoTime();
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
            _graphAccesses++;
            List<Vertex> eulerKreis = getEulerKreisFor(tempVertex);
            _unterKreise.add(eulerKreis);
            
            // Den Knoten finden, bei dem wir den nächsten Unterkreis starten
            // Diesen als Schnittknoten makieren, damit wir das später beim erstellen des Eulerkreises
            //                                                                      überprüfen können
            tempVertex = getNextNodeWithBiggerDegreeThan0(_vertexSeen);
            _schnittKnoten.add(tempVertex); 
        }
          
        /*
         * Schritt 3: Den Eulerkreis zusammenbauen
         */ 
        _eulerKreis.add(v0);
        erstelleEulerkreis(_unterKreise.get(0));
        System.out.println("Eulertour: " + _eulerKreis);
        endTime = System.nanoTime();
    }
    
    /*
     * Schritt 3: Nun erhält man den Eulerkreis, indem man mit
     *              dem ersten Unterkreis beginnt und bei jedem
     *              Schnittpunkt mit einem anderen Unterkreis,
     *              den letzteren einfügt, und danach den ersten
     *              Unterkreis wieder bis zu einem weiteren
     *              Schnittpunkt oder dem Endpunkt fortsetzt.
     */
    private void erstelleEulerkreis(List<Vertex> unterKreis)
    {
        for(int i=1;i<unterKreis.size();i++) // Durch den Unterkreis laufen
        {
            Vertex vertex = unterKreis.get(i);
            _eulerKreis.add(vertex);    // Knoten hinzufügen
            if(_schnittKnoten.contains(vertex))
            {
                _schnittKnoten.remove(vertex); // Demakieren
                List<Vertex> nextUnterkreis = getCreatedUnterkreisWhereStartIs(vertex,_unterKreise);
                _unterKreise.remove(nextUnterkreis); // muss nicht nochmal überprüft werden
                erstelleEulerkreis(nextUnterkreis); // Rekursionsaufruf
            }               
        }
    }
    
    /*
     * Liefert den Eulerkreis aus der Liste aller Eulerkreise für den gilt,
     * dass "vertex" der Startknoten ist
     */
    private List<Vertex> getCreatedUnterkreisWhereStartIs(Vertex vertex, List<List<Vertex>> eulerKreise)
    {
        List<Vertex> resultList = null;
        
        for(List<Vertex> eulerkreis : eulerKreise)
        {
            if(eulerkreis.get(0).equals(vertex))
            {
                resultList = eulerkreis;
                break;
            }
        }        
        eulerKreise.remove(resultList);
        return resultList;
    }

    private Vertex getNextNodeWithBiggerDegreeThan0(List<Vertex> vertexSeen)
    {
        Vertex result = null;
        for(Vertex v : vertexSeen)
        {
            if(vertexDegreeFor(v,_graph) > 0)
            {
                result = v;
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
            _graphAccesses++;
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
            _graphAccesses++;
            if(!_edgeSeen.contains(edge))
            {
                resultEdge = edge;
                break;                
            }
        }
        _edgeSeen.add(resultEdge);
        return resultEdge;
    }

    public List<MyWeightedEdge> getEulerkreis()
    {       
        List<MyWeightedEdge> eulertour = new ArrayList<>();
        for(int i=0;i<_eulerKreis.size();i++)
        {
            if(i+1 < _eulerKreis.size())
            {
                MyWeightedEdge edge = getEdgeFor(eulertour,_eulerKreis.get(i), _eulerKreis.get(i+1));    
                eulertour.add(edge);
            }
        }
        return eulertour;
    }
    
    /*
     * Liefert die nächste kante für einen Knoten unter der Bedingung,
     * dass die Kante noch nicht in der Eulertour vorkommt
     */
    private MyWeightedEdge getEdgeFor( List<MyWeightedEdge> eulertour,Vertex vertex, Vertex vertex2)
    {
        MyWeightedEdge result = null;
        for(MyWeightedEdge edge : _graph.getAllEdges(vertex, vertex2))
        {
            if(!eulertour.contains(edge))
            {
                result = edge;
                break;
            }
        }
        return result;
    }

    private boolean onlyEvenDegreesOfVertices(Graph<Vertex, MyWeightedEdge> graph)
    {
        int edgeCounter;
        
        for(Vertex v : graph.vertexSet())
        {
            edgeCounter = vertexDegreeFor(v,graph);
            if( edgeCounter == 0 || (edgeCounter%2) != 0)
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

    public long getTime()
    {
        return endTime-startTime;
    }
    
    public int getAnzahlZugriffe()
    {
        return _graphAccesses;
    }
    
    public Graph<Vertex, MyWeightedEdge> getGraph()
    {
        return _graph;
    }
}
