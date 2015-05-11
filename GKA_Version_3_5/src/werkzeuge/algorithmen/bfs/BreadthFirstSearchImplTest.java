package werkzeuge.algorithmen.bfs;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.Pseudograph;
import org.junit.Test;

public class BreadthFirstSearchImplTest
{

    BreadthFirstSearchImpl bfs;
    Graph<Vertex,MyWeightedEdge> _graph; 
    
    Vertex dies;
    Vertex vertex2;
    Vertex vertex3;
    Vertex ist;
    Vertex vertex;
    Vertex der;
    Vertex kürzeste;
    Vertex weg;

    @Test
    public void findShortestWayDirectedGraphTest()
    {
        // DirectedPseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class )
        _graph = new DirectedPseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class );
        
        fuelleGraphSetUp(_graph); 
        
        _graph.addEdge(dies, vertex2);
        _graph.addEdge(dies, ist);
        _graph.addEdge(dies,vertex3);
        _graph.addEdge(vertex3, ist);
        _graph.addEdge(ist,vertex2);
        _graph.addEdge(ist, vertex);
        _graph.addEdge(ist,der);
        _graph.addEdge(vertex,der);
        _graph.addEdge(der, kürzeste);
        _graph.addEdge(kürzeste,ist);
        _graph.addEdge(kürzeste, weg);
        
        bfs = new BreadthFirstSearchImpl(_graph);
        
        List<Vertex> shortestWay = bfs.findShortestWay(_graph, dies, weg);
        
        List<Vertex> shortestWaySolution = new ArrayList<>();
        shortestWaySolution.add(dies);
        shortestWaySolution.add(ist);
        shortestWaySolution.add(der);
        shortestWaySolution.add(kürzeste);
        shortestWaySolution.add(weg);
        
        // Test1 Startvertex: dies , Zielvertex: weg -> Lösung: [Dies, ist der kürzeste, Weg]
        assertEquals(shortestWay,shortestWaySolution);
        
        // Test2
        // Wenn es keine Möglichkeit gibt über eine Kannte das Ziel zu erreichen
        // Startvertex: vertex2 , Zielvertex: Ist -> Lösung: [] Endvertex kann nicht gefunden werden!
        bfs.faerbungenZuruecksetzen();
        
        shortestWay = bfs.findShortestWay(_graph, vertex2, ist);
        shortestWaySolution = new ArrayList<>();
        
        assertEquals(shortestWaySolution,shortestWay);
        
        // Test3
        // Wenn Startvertex und Zielvertex identisch sind
        //Startvertex: vertex , Zielvertex: vertex -> Lösung : [vertex]
        bfs.faerbungenZuruecksetzen();
        
        shortestWay = bfs.findShortestWay(_graph, vertex, vertex);
        shortestWaySolution = new ArrayList<>();
        shortestWaySolution.add(vertex);
             
        assertEquals(shortestWaySolution,shortestWay);
        
        // Test4 
        // Wenn der Zielvertex in einer getrennten Komponente ist:
        // Startvertex: dies, Zielvertex: weg -> Lösung : []
        _graph.removeEdge(kürzeste, weg);
        bfs.faerbungenZuruecksetzen();
        
        shortestWay = bfs.findShortestWay(_graph, dies, weg);
        shortestWaySolution = new ArrayList<>();
        
        assertEquals(shortestWaySolution,shortestWay);
    }
    
    @Test
    public void findShortestWayUndirectedGraphTest()
    {
        // DirectedPseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class )
        _graph = new Pseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class );
        
        fuelleGraphSetUp(_graph); 
        
        _graph.addEdge(dies, vertex2);
        _graph.addEdge(dies, ist);
        _graph.addEdge(dies,vertex3);
        _graph.addEdge(vertex3, ist);
        _graph.addEdge(ist,vertex2);
        _graph.addEdge(ist, vertex);
        _graph.addEdge(ist,der);
        _graph.addEdge(vertex,der);
        _graph.addEdge(der, kürzeste);
        _graph.addEdge(kürzeste,ist);
        _graph.addEdge(kürzeste, weg);
        
        bfs = new BreadthFirstSearchImpl(_graph);
        
        List<Vertex> shortestWay = bfs.findShortestWay(_graph, dies, weg);
        
        List<Vertex> shortestWaySolution = new ArrayList<>();
        shortestWaySolution.add(dies);
        shortestWaySolution.add(ist);
        shortestWaySolution.add(kürzeste);
        shortestWaySolution.add(weg);
        
        // Test1 Startvertex: dies , Zielvertex: weg -> Lösung: [Dies, ist der kürzeste, Weg]
        assertEquals(shortestWay,shortestWaySolution);
        
        // Test2
        // Wenn es keine Möglichkeit gibt über eine Kannte das Ziel zu erreichen
        // Startvertex: vertex2 , Zielvertex: Ist -> Lösung: [] Endvertex kann nicht gefunden werden!
        bfs.faerbungenZuruecksetzen();
        
        shortestWay = bfs.findShortestWay(_graph, vertex2, ist);
        shortestWaySolution = new ArrayList<>();
        shortestWaySolution.add(vertex2);
        shortestWaySolution.add(ist);
        
        assertEquals(shortestWaySolution,shortestWay);
        
        // Test3
        // Wenn Startvertex und Zielvertex identisch sind
        //Startvertex: vertex , Zielvertex: vertex -> Lösung : [vertex]
        bfs.faerbungenZuruecksetzen();
        
        shortestWay = bfs.findShortestWay(_graph, vertex, vertex);
        shortestWaySolution = new ArrayList<>();
        shortestWaySolution.add(vertex);
             
        assertEquals(shortestWaySolution,shortestWay);
        
        // Test4 
        // Wenn der Zielvertex in einer getrennten Komponente ist:
        // Startvertex: dies, Zielvertex: weg -> Lösung : []
        _graph.removeEdge(kürzeste, weg);
        bfs.faerbungenZuruecksetzen();
        
        shortestWay = bfs.findShortestWay(_graph, dies, weg);
        shortestWaySolution = new ArrayList<>();
        
        assertEquals(shortestWaySolution,shortestWay);
    }
    
    private void fuelleGraphSetUp(Graph<Vertex,MyWeightedEdge>  graph)
    {      
        dies = Vertex.createVertex("Dies",0,0,0);
        vertex2 = Vertex.createVertex("vertex2",0,0,0);
        vertex3 = Vertex.createVertex("vertex3",0,0,0);
        ist = Vertex.createVertex("ist",0,0,0);
        vertex = Vertex.createVertex("vertex",0,0,0);
        der = Vertex.createVertex("der",0,0,0);
        kürzeste = Vertex.createVertex("kürzeste",0,0,0);
        weg = Vertex.createVertex("weg",0,0,0);
        
        _graph.addVertex(dies);
        _graph.addVertex(vertex2);
        _graph.addVertex(vertex3);
        _graph.addVertex(ist);
        _graph.addVertex(vertex);
        _graph.addVertex(der);
        _graph.addVertex(kürzeste);
        _graph.addVertex(weg);        
    }

    

}
