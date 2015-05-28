package werkzeuge.algorithmen.dijkstra;

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
import org.jgrapht.alg.DijkstraShortestPath;
import org.junit.Before;
import org.junit.Test;

import werkzeuge.subwerkzeuge.GraphManager;
import werkzeuge.subwerkzeuge.BigGraph.BigGraphImpl;

public class DijkstraImplTest
{
    GraphManager _manager;
    DijkstraShortestPath<Vertex, MyWeightedEdge> _jgraphtDijkstra;
    DijkstraImpl _dijkstraAlg;
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
    public void testCities() throws IOException
    {
        // Graph bsp3.graph laden
        loadCityGraph();
        
        // Algorithmus anwenden        
        String husum = "Husum";
        String hamburg = "Hamburg";
        
        // Test 1.) Husum - Hamburg
        _dijkstraAlg = new DijkstraImpl(_graph);
        _jgraphtDijkstra = new DijkstraShortestPath<Vertex, MyWeightedEdge>(_graph, getVertex(husum),getVertex(hamburg));
        _dijkstraAlg.findShortestWay(getVertex(husum), getVertex(hamburg));         

        assertTrue(_dijkstraAlg.getWeglaenge() == _jgraphtDijkstra.getPathLength());
    }
 
    @Test
    public void testCities_02() throws IOException
    {
        // Graph bsp3.graph laden
        loadCityGraph();
        
        // Algorithmus anwenden

        String minden = "Minden";
        String hamburg = "Hamburg";

//        // Test 2.) Minden - Hamburg
        _dijkstraAlg = new DijkstraImpl(_graph);
        _jgraphtDijkstra = new DijkstraShortestPath<Vertex, MyWeightedEdge>(_graph, getVertex(minden),getVertex(hamburg));
        _dijkstraAlg.findShortestWay(getVertex(minden), getVertex(hamburg)); 
        

        assertTrue(_dijkstraAlg.getWeglaenge() == _jgraphtDijkstra.getPathLength());
    }
    
    @Test
    public void testCities_03() throws IOException
    {
        // Graph bsp3.graph laden
        loadCityGraph();
        
        // Algorithmus anwenden

        String münster = "Münster";
        String hamburg = "Hamburg";

        // Test 3.) Münster - Hamburg
        _dijkstraAlg = new DijkstraImpl(_graph);
        _jgraphtDijkstra = new DijkstraShortestPath<Vertex, MyWeightedEdge>(_graph, getVertex(münster),getVertex(hamburg));
        _dijkstraAlg.findShortestWay(getVertex(münster), getVertex(hamburg)); 
        

        assertTrue(_dijkstraAlg.getWeglaenge() == _jgraphtDijkstra.getPathLength());

    }
    
    @Test
    public void testInBigGraph_01()
    {
        // Graph bsp3.graph laden
        loadBigGraph();
        
        int random = new Random().nextInt(28)+1;
        // Algorithmus anwenden
        
        Vertex start = getVertex("V" + random);
        Vertex ziel = getVertex("Ziel");

        // Test 3.) Münster - Hamburg
        _dijkstraAlg = new DijkstraImpl(_graph);
        _jgraphtDijkstra = new DijkstraShortestPath<Vertex, MyWeightedEdge>(_graph, start,ziel);
        _dijkstraAlg.findShortestWay(start, ziel); 
        
        System.out.println("unsere Länge: " +_dijkstraAlg.getWeglaenge()+ " und JGraphT: " + _jgraphtDijkstra.getPathLength() );
        assertTrue(_dijkstraAlg.getWeglaenge() == _jgraphtDijkstra.getPathLength());

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
