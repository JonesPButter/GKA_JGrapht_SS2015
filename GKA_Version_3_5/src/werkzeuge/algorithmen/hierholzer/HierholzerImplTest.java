package werkzeuge.algorithmen.hierholzer;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
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

//    @Test
    public void testHierholzerAlgorithmWithWrongGraphs()
    {
        boolean isWrongGraph = false;
        Graph<Vertex,MyWeightedEdge> graph = new Pseudograph<>(MyWeightedEdge.class);
        HierholzerImpl hierholzer;
        
        // Test 1. empty Graph
        try{
            hierholzer = new HierholzerImpl(graph);
        }catch (Exception ex){
            isWrongGraph=true;
        }
        assertTrue(isWrongGraph);
        
        // Test 2. only one Node
        isWrongGraph = false;
        Vertex v1 = Vertex.createVertex("V1", 0, 0, 0);
        graph.addVertex(v1);
        try{
            hierholzer = new HierholzerImpl(graph);
        }catch (Exception ex){
            isWrongGraph=true;
        }
        assertTrue(isWrongGraph);
        
        // Test 3. two Nodes, one Edge
        isWrongGraph = false;
        Vertex v2 = Vertex.createVertex("V2", 0, 0, 0);
        graph.addVertex(v2);
        graph.addEdge(v1, v2);
        try{
            hierholzer = new HierholzerImpl(graph);
        }catch (Exception ex){
            isWrongGraph=true;
        }
        assertTrue(isWrongGraph);
    }
    
    @Test
    public void testHierholzerAlgorithmForSmallGraph()
    {
        Graph<Vertex,MyWeightedEdge> graph = new Pseudograph<>(MyWeightedEdge.class);
        
        Vertex v0 = Vertex.createVertex("v0", 0, 0, 0);
        Vertex v1 = Vertex.createVertex("v1", 0, 0, 0);
        Vertex v2 = Vertex.createVertex("v2", 0, 0, 0);
        Vertex v3 = Vertex.createVertex("v3", 0, 0, 0);
        
        Vertex v4 = Vertex.createVertex("v4", 0, 0, 0);
        Vertex v5 = Vertex.createVertex("v5", 0, 0, 0);
        
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        
        graph.addVertex(v4); // neu
        graph.addVertex(v5); // neu
        
//        graph.addVertex(Vertex.createVertex("v9", 0, 0, 0));
        
        graph.addEdge(v0, v1);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v0);
        
        graph.addEdge(v2, v4); // neu
        graph.addEdge(v4, v5); // neu
        graph.addEdge(v5, v2); // neu
        
        Set<MyWeightedEdge> edges = graph.edgeSet();
        int anzahlEdges = edges.size();
        
        HierholzerImpl hierholzer = new HierholzerImpl(graph);
        
        /*
         * 1. Wir erhalten eine Tour
         * 2. Die Anzahl der Kanten der Tour entspricht der Anzahl der Kanten des ursprünglichen Graphen
         * 3. Die Tour beinhaltet alle Kanten des ursprünglichen Graphen
         * 4. Die Tour ist eine Kantenfolge innerhalb des Graphen
         */
        assertTrue(!hierholzer.getEulertour().isEmpty()); 
        assertTrue(hierholzer.getEulertour().size() == anzahlEdges);        
        assertTrue(hierholzer.getEulertour().containsAll(edges)); 
        assertTrue(isKantenfolge(hierholzer.getEulertour(),graph));
    }


//    @Test
    public void testHierholzerAlgorithm()
    {
        Graph<Vertex,MyWeightedEdge> graph = new Pseudograph<>(MyWeightedEdge.class);
        
        Vertex v1 = Vertex.createVertex("v1", 0, 0, 0);
        Vertex v2 = Vertex.createVertex("v2", 0, 0, 0);
        Vertex v3 = Vertex.createVertex("v3", 0, 0, 0);        
        Vertex v4 = Vertex.createVertex("v4", 0, 0, 0);
        Vertex v5 = Vertex.createVertex("v5", 0, 0, 0);
        Vertex v6 = Vertex.createVertex("v6", 0, 0, 0);
        
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);        
        graph.addVertex(v4); 
        graph.addVertex(v5); 
        graph.addVertex(v6);
        
//        graph.addVertex(Vertex.createVertex("v9", 0, 0, 0));
        
        graph.addEdge(v3, v4);
        graph.addEdge(v3, v4);
        graph.addEdge(v3, v2);      
        graph.addEdge(v3,v6);       
        graph.addEdge(v3, v1); 
        graph.addEdge(v3, v5); 
        graph.addEdge(v4, v5);        
        graph.addEdge(v4, v2); 
        graph.addEdge(v2, v6);        
        graph.addEdge(v1, v2);
        
        Set<MyWeightedEdge> edges = graph.edgeSet();
        int anzahlEdges = edges.size();
        
        HierholzerImpl hierholzer = new HierholzerImpl(graph);
        
        /*
         * 1. Wir erhalten eine Tour
         * 2. Die Anzahl der Kanten der Tour entspricht der Anzahl der Kanten des Graphen
         * 3. Die Tour beinhaltet alle Kanten des ursprünglichen Graphen
         * 4. Die Tour ist eine Kantenfolge innerhalb des Graphen
         */
        assertTrue(!hierholzer.getEulertour().isEmpty()); 
        assertTrue(hierholzer.getEulertour().size() == anzahlEdges);        
        assertTrue(hierholzer.getEulertour().containsAll(edges)); 
        assertTrue(isKantenfolge(hierholzer.getEulertour(),graph));
    }
    
    @Test
    public void testGetEulertour()
    {
        _eulerCreator.creatEulerGraph(6);
        Graph<Vertex,MyWeightedEdge> graph = _eulerCreator.getEulerGraph();
        
        HierholzerImpl hierholzer = new HierholzerImpl(graph);
        Set<MyWeightedEdge> eulerTourSet = new HashSet<>(hierholzer.getEulertour());
                
        /*
         * 1. Wir erhalten eine Tour
         * 2. Die Anzahl der Kanten der Tour entspricht der Anzahl der Kanten des ursprünglichen Graphen
         * 3. Die Tour beinhaltet alle Kanten des ursprünglichen Graphen
         * 4. Die Tour ist eine Kantenfolge innerhalb des Graphen
         */
        assertTrue(!hierholzer.getEulertour().isEmpty()); 
        assertTrue(hierholzer.getEulertour().size() == graph.edgeSet().size());        
//        assertTrue(hierholzer.getEulertour().containsAll(graph.edgeSet())); 
        assertTrue(isKantenfolge(hierholzer.getEulertour(),graph));
    }
    
    @Test
    public void testHierholzerAlgorithmForSmallGraph2()
    {    
        Graph<Vertex,MyWeightedEdge> graph = new Pseudograph<>(MyWeightedEdge.class);
        
        // ***************************************Testgraph1******************************************************
        
        Vertex v1 = Vertex.createVertex("v1", 0, 0, 0);
        Vertex v2 = Vertex.createVertex("v2", 0, 0, 0);
        Vertex v3 = Vertex.createVertex("v3", 0, 0, 0);       
        Vertex v4 = Vertex.createVertex("v4", 0, 0, 0);
        Vertex v5 = Vertex.createVertex("v5", 0, 0, 0);
        Vertex v6 = Vertex.createVertex("v6", 0, 0, 0);
        
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);
        
        graph.addEdge(v3, v1);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v6);
        
        graph.addEdge(v3, v4);
        graph.addEdge(v4, v6);
        graph.addEdge(v2, v4);
        
        graph.addEdge(v6, v2);
        graph.addEdge(v3, v5);
        graph.addEdge(v5, v1);
        graph.addEdge(v1, v3);
        graph.addEdge(v4, v6);
        
        Set<MyWeightedEdge> edges = graph.edgeSet();
        int anzahlEdges = edges.size();
        
        HierholzerImpl hierholzer = new HierholzerImpl(graph);
        
        System.out.println("T: "+hierholzer.getEulertour());
        System.out.println("K: "+edges);
        /*
         * 1. Wir erhalten eine Tour
         * 2. Die Anzahl der Kanten der Tour entspricht der Anzahl der Kanten des ursprünglichen Graphen
         * 3. Die Tour beinhaltet alle Kanten des ursprünglichen Graphen
         * 4. Die Tour ist eine Kantenfolge innerhalb des Graphen
         */
        assertTrue(!hierholzer.getEulertour().isEmpty()); 
        assertTrue(hierholzer.getEulertour().size() == anzahlEdges);        
//        assertTrue(hierholzer.getEulertour().containsAll(graph.edgeSet())); 
        assertTrue(isKantenfolge(hierholzer.getEulertour(),graph));
    }

    
    /*
     * Prüft, ob die aufeinanderfolgenden Kanten innerhalb der Eulertour
     * miteinander verbunden sind.
     */
    private boolean isKantenfolge(List<MyWeightedEdge> eulertour,
            Graph<Vertex, MyWeightedEdge> graph)
    {
        /*
         * 1. Über alle Kanten iterieren
         * 2. Jede Kante, bis auf die letzte, mit dem Nachfolger vergleichen
         *      - sind die Kanten nicht miteinander über einen Knoten verbunden,
         *        kann es sich nicht um eine Kantenfolge handeln
         */
        for(int i=0;i<eulertour.size();i++)
        {
            if(i+1<eulertour.size())
            {
                if(!isConnected(eulertour.get(i),eulertour.get(i+1),graph))
                {
                    return false;
                }
            }
        }    
        return true;
    }


    /*
     * Prüft, ob zwei Kanten miteinander verbunden sind
     */
    private boolean isConnected(MyWeightedEdge first,
            MyWeightedEdge second, Graph<Vertex, MyWeightedEdge> graph)
    {
        Vertex source1 = graph.getEdgeSource(first);
        Vertex target1 = graph.getEdgeTarget(first);
        
        Vertex source2 = graph.getEdgeSource(second);
        Vertex target2 = graph.getEdgeTarget(second);
        
        if(source1.equals(source2) || source1.equals(target2) || 
                target1.equals(source2) || target1.equals(target2))
        {
            return true;
        }        
        return false;
    }

}
