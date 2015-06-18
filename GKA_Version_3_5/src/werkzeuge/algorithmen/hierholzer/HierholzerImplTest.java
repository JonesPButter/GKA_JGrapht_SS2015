package werkzeuge.algorithmen.hierholzer;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.jgrapht.graph.Pseudograph;
import org.junit.Before;
import org.junit.Test;

import werkzeuge.algorithmen.fleury.FleuryImpl;
import werkzeuge.subwerkzeuge.GraphManager;
import werkzeuge.subwerkzeuge.eulerCreator.EulerCreator;

public class HierholzerImplTest
{

    GraphManager _manager;
    EulerCreator _eulerCreator;
    
    String _home;
    char _seperator;
    
    @Before
    public void setUp() throws Exception
    {
        _manager = GraphManager.create();
        _eulerCreator = new EulerCreator();
        _seperator = java.io.File.separatorChar;
        _home = System.getProperty("user.home");
    }

    @Test
    public void testHierholzerAlgorithmForSmallGraph()
    {
        Graph<Vertex,MyWeightedEdge> graph = new Pseudograph<>(MyWeightedEdge.class);
        
        Vertex v0 = Vertex.createVertex("v0", 0, 0, 0);
        Vertex v1 = Vertex.createVertex("v1", 0, 0, 0);
        Vertex v2 = Vertex.createVertex("v2", 0, 0, 0);
        Vertex v3 = Vertex.createVertex("v3", 0, 0, 0);
        
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(v0, v1);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v0);
        
        Set<MyWeightedEdge> edges = graph.edgeSet();
        int anzahlEdges = edges.size();
    }
    
//    @Test
    public void testGetEulertour()
    {
        _eulerCreator.creatEulerGraph(100);
        Graph<Vertex,MyWeightedEdge> graph = _eulerCreator.getEulerGraph();
        
        HierholzerImpl hierholzer = new HierholzerImpl(graph);
        Set<MyWeightedEdge> eulerTourSet = new HashSet<>(hierholzer.getEulertour());
                
        /*
         * 1. Wir erhalten eine Tour
         * 2. Die Anzahl der Kanten der Tour entspricht der Anzahl der Kanten des ursprünglichen Graphen
         * 3. Die Tour beinhaltet alle Kanten des ursprünglichen Graphen
         */
        assertTrue(!hierholzer.getEulertour().isEmpty()); 
        assertTrue(hierholzer.getEulertour().size() == graph.edgeSet().size());        
        assertTrue(hierholzer.getEulertour().containsAll(graph.edgeSet())); 
        
    }

}
