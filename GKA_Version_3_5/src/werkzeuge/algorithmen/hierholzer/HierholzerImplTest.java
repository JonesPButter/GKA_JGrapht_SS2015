package werkzeuge.algorithmen.hierholzer;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.jgrapht.graph.Pseudograph;
import org.junit.Before;
import org.junit.Test;

import werkzeuge.algorithmen.GraphTester;
import werkzeuge.subwerkzeuge.GraphManager;
import werkzeuge.subwerkzeuge.eulerCreator.EulerCreator;

public class HierholzerImplTest extends GraphTester
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
    public void testEdgeCases()
    {
        boolean isWrongGraph = false;
        Graph<Vertex,MyWeightedEdge> graph = new Pseudograph<>(MyWeightedEdge.class);
        
        // ********************** Test 1. empty Graph **********************
        try{
            new HierholzerImpl(graph);
        }catch (Exception ex){
            isWrongGraph=true;
        }
        assertTrue(isWrongGraph);
        
        // ********************** Test 2. only one Node **********************
        isWrongGraph = false;
        Vertex v1 = Vertex.createVertex("V1", 0, 0, 0);
        graph.addVertex(v1);
        try{
            new HierholzerImpl(graph);
        }catch (Exception ex){
            isWrongGraph=true;
        }
        assertTrue(isWrongGraph);
        
        // ********************** Test 3. two Nodes, one Edge **********************
        isWrongGraph = false;
        Vertex v2 = Vertex.createVertex("V2", 0, 0, 0);
        graph.addVertex(v2);
        graph.addEdge(v1, v2);
        try{
            new HierholzerImpl(graph);
        }catch (Exception ex){
            isWrongGraph=true;
        }
        assertTrue(isWrongGraph);
    }
    
    @Test
    public void testHierholzerTwoNodesTwoEdges()
    {
        Graph<Vertex,MyWeightedEdge> graph = new Pseudograph<>(MyWeightedEdge.class);
       
        // **************** Testgraph ***************
        //    V0  
        //    ( )
        //     V1
        
        Vertex v0 = Vertex.createVertex("v0", 0, 0, 0);
        Vertex v1 = Vertex.createVertex("v1", 0, 0, 0);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addEdge(v0, v1);
        graph.addEdge(v1, v0);
        Set<MyWeightedEdge> edges = graph.edgeSet();
        int anzahlEdges = edges.size();
        
        HierholzerImpl hierholzer = new HierholzerImpl(graph);
        List<MyWeightedEdge> eulerkreis = hierholzer.getEulerkreis();
        MyWeightedEdge startKante = eulerkreis.get(0);
        MyWeightedEdge endKante = eulerkreis.get(eulerkreis.size()-1);
        
        /*
         * 1. Wir erhalten eine Tour
         * 2. Die Anzahl der Kanten der Tour entspricht der Anzahl der Kanten des ursprünglichen Graphen
         * 3. Die Tour beinhaltet alle Kanten des ursprünglichen Graphen
         * 4. Die Tour ist eine Kantenfolge innerhalb des Graphen
         * 5. Start und Endpunkt sind identisch
         */
        assertTrue(!hierholzer.getEulerkreis().isEmpty()); 
        assertTrue(hierholzer.getEulerkreis().size() == anzahlEdges);        
        assertTrue(hierholzer.getEulerkreis().containsAll(edges)); 
        assertTrue(isKantenfolge(hierholzer.getEulerkreis(),graph));
        assertTrue(isConnected(startKante,endKante,graph)); 
        
    }
    
    @Test
    public void testHierholzerForSmallGraph()
    {    
        Graph<Vertex,MyWeightedEdge> graph = new Pseudograph<>(MyWeightedEdge.class);
        
        // ***************************************Testgraph1******************************************************

        //    V0 -- V1
        //   /      /
        //   V3 -- V2 -- V5
        //          \   /
        //            V4
        
        
        Vertex v0 = Vertex.createVertex("v0", 0, 0, 0);
        Vertex v1 = Vertex.createVertex("v1", 0, 0, 0);
        Vertex v2 = Vertex.createVertex("v2", 0, 0, 0);
        Vertex v3 = Vertex.createVertex("v3", 0, 0, 0);       
        Vertex v4 = Vertex.createVertex("v4", 0, 0, 0);
        Vertex v5 = Vertex.createVertex("v5", 0, 0, 0);
        // Knoten hinzufügen
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);  
        graph.addVertex(v4);
        graph.addVertex(v5);   
        // Kanten hinzufügen
        graph.addEdge(v0, v1);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v0);     
        graph.addEdge(v2, v4);
        graph.addEdge(v4, v5);
        graph.addEdge(v5, v2);
        
        Set<MyWeightedEdge> edges = graph.edgeSet();
        int anzahlEdges = edges.size();
      
        HierholzerImpl hierholzer = new HierholzerImpl(graph);
        List<MyWeightedEdge> eulerkreis = hierholzer.getEulerkreis();
        MyWeightedEdge startKante = eulerkreis.get(0);
        MyWeightedEdge endKante = eulerkreis.get(eulerkreis.size()-1);
        
        /*
         * 1. Wir erhalten eine Tour
         * 2. Die Anzahl der Kanten der Tour entspricht der Anzahl der Kanten des ursprünglichen Graphen
         * 3. Die Tour beinhaltet alle Kanten des ursprünglichen Graphen
         * 4. Die Tour ist eine Kantenfolge innerhalb des Graphen
         * 5. Start und Endpunkt sind identisch
         */
        assertTrue(!hierholzer.getEulerkreis().isEmpty()); 
        assertTrue(hierholzer.getEulerkreis().size() == anzahlEdges);        
        assertTrue(hierholzer.getEulerkreis().containsAll(edges)); 
        assertTrue(isKantenfolge(hierholzer.getEulerkreis(),graph));
        assertTrue(isConnected(startKante,endKante,graph));       
    }


    @Test
    public void testFleuryAlgorithmForSmallGraphExtended()
    {
        Graph<Vertex,MyWeightedEdge> graph = new Pseudograph<>(MyWeightedEdge.class);
        // ***************************************Testgraph2******************************************************
        //    V0 -- V1   V6 -- V7
        //   /      /     \  /
        //   V3 -- V2  --  V5
        //           \    /
        //             V4
  
        Vertex v0 = Vertex.createVertex("v0", 0, 0, 0);
        Vertex v1 = Vertex.createVertex("v1", 0, 0, 0);
        Vertex v2 = Vertex.createVertex("v2", 0, 0, 0);
        Vertex v3 = Vertex.createVertex("v3", 0, 0, 0);       
        Vertex v4 = Vertex.createVertex("v4", 0, 0, 0);
        Vertex v5 = Vertex.createVertex("v5", 0, 0, 0);
        Vertex v6 = Vertex.createVertex("v6", 0, 0, 0);
        Vertex v7 = Vertex.createVertex("v7", 0, 0, 0);
        // Knoten hinzufügen
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);       
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);
        graph.addVertex(v7);
        // Kanten hinzufügen
        graph.addEdge(v0, v1);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v0);       
        graph.addEdge(v2, v4);
        graph.addEdge(v4, v5);
        graph.addEdge(v5, v2);
        graph.addEdge(v5, v6);
        graph.addEdge(v6, v7);
        graph.addEdge(v7, v5);
        
        Set<MyWeightedEdge> edges = graph.edgeSet();
        int anzahlEdges = edges.size();
        
        HierholzerImpl hierholzer = new HierholzerImpl(graph);
        List<MyWeightedEdge> eulerkreis = hierholzer.getEulerkreis();
        MyWeightedEdge startKante = eulerkreis.get(0);
        MyWeightedEdge endKante = eulerkreis.get(eulerkreis.size()-1);
        
        /*
         * 1. Wir erhalten eine Tour
         * 2. Die Anzahl der Kanten der Tour entspricht der Anzahl der Kanten des ursprünglichen Graphen
         * 3. Die Tour beinhaltet alle Kanten des ursprünglichen Graphen
         * 4. Die Tour ist eine Kantenfolge innerhalb des Graphen
         * 5. Start und Endpunkt sind identisch
         */
        assertTrue(!hierholzer.getEulerkreis().isEmpty()); 
        assertTrue(hierholzer.getEulerkreis().size() == anzahlEdges);        
        assertTrue(hierholzer.getEulerkreis().containsAll(edges)); 
        assertTrue(isKantenfolge(hierholzer.getEulerkreis(),graph));
        assertTrue(isConnected(startKante,endKante,graph)); 
        
    }
    
    @Test
    public void testRandomEulergraph()
    {
        int anzahlTests = 20;
        for(int i=0; i<anzahlTests;i++)
        {
            _eulerCreator.creatEulerGraph(500);
            Graph<Vertex,MyWeightedEdge> graph = _eulerCreator.getEulerGraph();             
            Set<MyWeightedEdge> edges = graph.edgeSet();
            int anzahlEdges = edges.size();
            
            HierholzerImpl hierholzer = new HierholzerImpl(graph);
            List<MyWeightedEdge> eulerkreis = hierholzer.getEulerkreis();
            MyWeightedEdge startKante = eulerkreis.get(0);
            MyWeightedEdge endKante = eulerkreis.get(eulerkreis.size()-1);
            
            /*
             * 1. Wir erhalten eine Tour
             * 2. Die Anzahl der Kanten der Tour entspricht der Anzahl der Kanten des ursprünglichen Graphen
             * 3. Die Tour beinhaltet alle Kanten des ursprünglichen Graphen
             * 4. Die Tour ist eine Kantenfolge innerhalb des Graphen
             * 5. Start und Endpunkt sind identisch
             */
            assertTrue(!hierholzer.getEulerkreis().isEmpty()); 
            assertTrue(hierholzer.getEulerkreis().size() == anzahlEdges);        
            assertTrue(hierholzer.getEulerkreis().containsAll(edges)); 
            assertTrue(isKantenfolge(hierholzer.getEulerkreis(),graph));
            assertTrue(isConnected(startKante,endKante,graph)); 
            
        }
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
        List<MyWeightedEdge> eulerkreis = hierholzer.getEulerkreis();
        MyWeightedEdge startKante = eulerkreis.get(0);
        MyWeightedEdge endKante = eulerkreis.get(eulerkreis.size()-1);
        
        /*
         * 1. Wir erhalten eine Tour
         * 2. Die Anzahl der Kanten der Tour entspricht der Anzahl der Kanten des ursprünglichen Graphen
         * 3. Die Tour beinhaltet alle Kanten des ursprünglichen Graphen
         * 4. Die Tour ist eine Kantenfolge innerhalb des Graphen
         * 5. Start und Endpunkt sind identisch
         */
        assertTrue(!hierholzer.getEulerkreis().isEmpty()); 
        assertTrue(hierholzer.getEulerkreis().size() == anzahlEdges);        
        assertTrue(hierholzer.getEulerkreis().containsAll(edges)); 
        assertTrue(isKantenfolge(hierholzer.getEulerkreis(),graph));
        assertTrue(isConnected(startKante,endKante,graph)); 
        
    }
}
