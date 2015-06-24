package werkzeuge.algorithmen.fleury;

import java.util.ArrayList;
import java.util.List;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.jgrapht.experimental.GraphTests;

import werkzeuge.algorithmen.bfs.BreadthFirstSearchImpl;

public class FleuryImpl
{
    
    Graph<Vertex,MyWeightedEdge> _graph;
    List<MyWeightedEdge> _eulerKreis;
    long startTime;
    long endTime;
    private int _graphAccesses;

    /**
     * Eine Implementation des Fleury-Algorithmus 
     *          zur Berechnung von Eulerkreisen innerhalb eines Graphen
     *          
     * @param graph Der zu untersuchende Graph
     * 
     * @requires graph.vertexSet().size() >= 2
     * @requires GraphTests.isConnected(graph)
     * @requires onlyEvenDegreesOfVertices(graph)
     * 
     * @throws IllegalArgumentException
     */
    public FleuryImpl(Graph<Vertex, MyWeightedEdge> graph) throws IllegalArgumentException
    {
        if(graph.vertexSet().size() < 2 || !GraphTests.isConnected(graph) || !onlyEvenDegreesOfVertices(graph))
        {
            throw new IllegalArgumentException("Der Graph ist entweder nicht zusammenhängend"
                    + " oder es haben nicht alle Knoten einen geraden Knotengrad");
        }
        _graph = graph;
        _eulerKreis  = new ArrayList<>();
        _graphAccesses = 0;
        startAlgorithm();
    }

    // Startet den Algorithmus
    private void startAlgorithm()
    {
        startTime = System.nanoTime();
        /*
         * Schritt 1: Man wähle einen beliebigen Knoten v0 in G und setze W0 = v0.
         */
        Vertex v0 = _graph.vertexSet().iterator().next(); 
        Vertex tempVertex = v0;
        
        while(!_eulerKreis.containsAll(_graph.edgeSet()))
        {
            _graphAccesses++;
            /*
             * Schritt 2: Eine passende Kante wählen, für die gilt: 
             *                            a)- sie ist keine Schnittkante
             *                              - es sei denn, es gibt keine Alternative
             *                            b)- sie ist noch nicht in der Eulertour
             *                              (da wir die Kanten immer aus dem Graphen entfernen,
             *                               kann dieser Fall gar nicht eintreten)
             */
            MyWeightedEdge nextEdge = getNextEdge(tempVertex);
         // Kante wird entfernt, damit wir den Suchalgorithmus zur Berechnung 
         //                                   einer möglichen Schnittkante anwenden können
            _graph.removeEdge(nextEdge); 
            _eulerKreis.add(nextEdge);
            
            // tempVertex bestimmen
            Vertex source = _graph.getEdgeSource(nextEdge);
            Vertex target = _graph.getEdgeTarget(nextEdge);
            if(source.equals(tempVertex))
            {
                tempVertex = target;
            }
            else tempVertex = source;
        }
        endTime = System.nanoTime();
    }

    /*
     * Liefert die nächste mögliche Kante für den Eulerkreis.
     * Für die Kante muss gelten, dass sie keine Schnittkante ist, es sei denn,
     * es gibt keine Alternative.
     * 
     * @param tempVertex Der Knoten von dem aus wir die nächste Kante suchen
     * 
     */
    private MyWeightedEdge getNextEdge(Vertex tempVertex)
    {
        MyWeightedEdge resultEdge = null;
        List<MyWeightedEdge> edges = new ArrayList<>(_graph.edgesOf(tempVertex));
        for(int i=0;i<edges.size();i++)
        {
            _graphAccesses++;
            MyWeightedEdge edge = edges.get(i);
            // Die Kante ist keine Schnittkante oder es gibt keine andere Alternative
            if(!isSchnittkante(edge,tempVertex) || i==edges.size()-1){
                resultEdge = edge;
                break;
            }
        }        
        return resultEdge;
    }


    /*
     * Prüft, ob es sich bei einer Kante um eine Schnittkante handelt
     * 
     * @param edge Die Kante die überprüft werden soll
     * @param tempVertex Der Knoten von dem aus wir kommen
     */
    private boolean isSchnittkante(MyWeightedEdge edge, Vertex tempVertex)
    {
        boolean result = false;
        Vertex neighbour;
        Vertex source = _graph.getEdgeSource(edge);
        Vertex target = _graph.getEdgeTarget(edge);
        // Hier wird nur der Nachbar gesucht
        if(source.equals(tempVertex))
        {
            neighbour = target;
        }
        else neighbour = source;
       
        /*
         * 1.) Kante entfernen, um zu prüfen, ob es noch einen Alternativen Weg
         *                                       von neighbour zu tempVertex zurück gibt
         * 2.) Finden wir keinen kürzesten Weg von Neighbour zurück zu tempVertex,
         *                                       handelt es sich um eine Schnittkante
         * 3.) In jedem Fall wird die Kante wieder hinzugefügt 
         *                                (Hier soll ja nur geprüft werden, was wäre wenn...)
         * 
         */
        _graph.removeEdge(edge); 
        BreadthFirstSearchImpl bfs = new BreadthFirstSearchImpl(_graph);
        if(bfs.findShortestWay(_graph, neighbour, tempVertex).isEmpty())
        {
            result = true;
        }
        _graphAccesses+= bfs.getAnzahlZugriffe();
        _graph.addEdge(tempVertex, neighbour,edge);
        return result;
    }


    /**
     * Liefert den endgültigen Eulerkreis
     * @return Den Eulerkreis
     */
    public List<MyWeightedEdge> getEulerkreis()
    {
        return _eulerKreis;
    }

    /*
     * Prüft, ob alle Knoten innerhalb eines Graphen einen geraden Knotengrad besitzen
     */
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
    @SuppressWarnings("unused")
    private int vertexDegreeFor(Vertex vertex,Graph<Vertex, MyWeightedEdge> graph)
    {
        int result =0;
        for(MyWeightedEdge edge : graph.edgesOf(vertex))
        {
            result++;
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
