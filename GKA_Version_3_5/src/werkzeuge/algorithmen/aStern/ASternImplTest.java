package werkzeuge.algorithmen.aStern;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Set;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.junit.Before;
import org.junit.Test;

import werkzeuge.algorithmen.dijkstra.DijkstraImpl;
import werkzeuge.subwerkzeuge.GraphManager;
import werkzeuge.subwerkzeuge.BigGraph.BigGraphImpl;


public class ASternImplTest
{
    GraphManager _manager;
    ASternImpl _aSternAlg;
    Graph<Vertex,MyWeightedEdge> _graph;
    
    String _home;
    char _seperator;

    @Before
    public void setUp() throws Exception
    {
        _manager = GraphManager.create();
        _seperator = java.io.File.separatorChar;
        _home = System.getProperty("user.home");
        

    }

    @Test
    public void testCities_01() throws IOException
    {
        // Graph bsp3.graph laden
        loadCityGraph();
        
        // Algorithmus anwenden        
        Vertex start = getVertex("Husum");
        Vertex target = getVertex("Hamburg");
        
        // Test 1.) Husum - Hamburg
        DijkstraImpl dijkstraAlg = new DijkstraImpl(_graph);
        dijkstraAlg.findShortestWay(start,target);
        _aSternAlg = new ASternImpl(_graph);
        _aSternAlg.findShortestWay(start,target);    

        assertTrue(dijkstraAlg.getWeglaenge() == _aSternAlg.getWeglaenge());
    }
    
    @Test
    public void testCities_02() throws IOException
    {
        // Graph bsp3.graph laden
        loadCityGraph();
        
        // Algorithmus anwenden
        Vertex start = getVertex("Minden");
        Vertex target = getVertex("Hamburg");
        
        // Test 2.) Minden - Hamburg
        DijkstraImpl dijkstraAlg = new DijkstraImpl(_graph);
        dijkstraAlg.findShortestWay(start,target);
        _aSternAlg = new ASternImpl(_graph);
        _aSternAlg.findShortestWay(start,target);    

        assertTrue(dijkstraAlg.getWeglaenge() == _aSternAlg.getWeglaenge());
    }
    
    @Test
    public void testCities_03() throws IOException
    {
        // Graph bsp3.graph laden
        loadCityGraph();
        
        // Algorithmus anwenden
        Vertex start = getVertex("Münster");
        Vertex target = getVertex("Hamburg");

        // Test 3.) Münster - Hamburg
        DijkstraImpl dijkstraAlg = new DijkstraImpl(_graph);
        dijkstraAlg.findShortestWay(start,target);
        _aSternAlg = new ASternImpl(_graph);
        _aSternAlg.findShortestWay(start,target);    

        assertTrue(dijkstraAlg.getWeglaenge() == _aSternAlg.getWeglaenge());
    }
    
    @Test
    public void testInBigGraph_01()
    {
        loadBigGraph();
        int random = new Random().nextInt(28)+1;
        // Algorithmus anwenden
        Vertex start = getVertex("V" + random);
        Vertex target = getVertex("Ziel");

        // Test 3.) Münster - Hamburg
        DijkstraImpl dijkstraAlg = new DijkstraImpl(_graph);
        dijkstraAlg.findShortestWay(start,target);
        _aSternAlg = new ASternImpl(_graph);
        _aSternAlg.findShortestWay(start,target);    

        assertTrue(dijkstraAlg.getWeglaenge() == _aSternAlg.getWeglaenge());
    }
    
    @Test
    public void testInBigGraph_02()
    {
        loadBigGraph();
        int random = new Random().nextInt(28)+1;
        // Algorithmus anwenden
        Vertex start = getVertex("V" + random);
        Vertex target = getVertex("Ziel");

        // Test 3.) Münster - Hamburg
        DijkstraImpl dijkstraAlg = new DijkstraImpl(_graph);
        dijkstraAlg.findShortestWay(start,target);
        _aSternAlg = new ASternImpl(_graph);
        _aSternAlg.findShortestWay(start,target);    

        assertTrue(dijkstraAlg.getWeglaenge() == _aSternAlg.getWeglaenge());
    }
    
    @Test
    public void testInBigGraph_03()
    {
        loadBigGraph();
        int random = new Random().nextInt(28)+1;
        // Algorithmus anwenden
        Vertex start = getVertex("V" + random);
        Vertex target = getVertex("Ziel");

        // Test 3.) Münster - Hamburg
        DijkstraImpl dijkstraAlg = new DijkstraImpl(_graph);
        dijkstraAlg.findShortestWay(start,target);
        _aSternAlg = new ASternImpl(_graph);
        _aSternAlg.findShortestWay(start,target);    

        assertTrue(dijkstraAlg.getWeglaenge() == _aSternAlg.getWeglaenge());
    }
    
    @Test
    public void testInBigGraph_04()
    {
        loadBigGraph();
        int random = new Random().nextInt(28)+1;
        // Algorithmus anwenden
        Vertex start = getVertex("V" + random);
        Vertex target = getVertex("Ziel");

        // Test 3.) Münster - Hamburg
        DijkstraImpl dijkstraAlg = new DijkstraImpl(_graph);
        dijkstraAlg.findShortestWay(start,target);
        _aSternAlg = new ASternImpl(_graph);
        _aSternAlg.findShortestWay(start,target);    

        assertTrue(dijkstraAlg.getWeglaenge() == _aSternAlg.getWeglaenge());
    }
    
    // ..............Hilfsmethoden ..............
    
    
    private void loadCityGraph() throws IOException
    {
        // **** File laden ****
        File graph_Cities = new java.io.File(_home+_seperator+"Desktop"+ _seperator +"bspGraphen" + _seperator + "bsp3.graph");
        Path java_path = Paths.get(graph_Cities.getPath());
        List<String> dataList = Files.readAllLines(java_path); //throws Exception;
        
        // **** Graphen bauen ****
        _manager.loadGraph(dataList);  
        _graph = _manager.getGraph();       
    }
    
    private void loadBigGraph()
    {
        BigGraphImpl big = new BigGraphImpl();
        big.createBigGraph(30, 90);
        _graph = big.getBigGraph();
    }
    
    private Vertex getVertex(String vertexName)
    {
        Vertex res = null;
        Set<Vertex> vertexes = _graph.vertexSet();
        for(Vertex v : vertexes)
        {
            if(v.getName().equals(vertexName))
            {
                return v;
            }
        }
        return res;
    }
}
