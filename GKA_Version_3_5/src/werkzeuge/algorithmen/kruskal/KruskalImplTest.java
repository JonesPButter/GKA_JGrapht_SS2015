package werkzeuge.algorithmen.kruskal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
import org.junit.Before;
import org.junit.Test;

import werkzeuge.subwerkzeuge.GraphManager;
import werkzeuge.subwerkzeuge.BigGraph.BigGraphImpl;

public class KruskalImplTest
{

    GraphManager _manager;
    KruskalImpl _kruskalAlg;
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
    public void testBigGraph() throws IOException
    {
        // Graph bsp3.graph laden
        boolean bedingungenErfuellt = true;
        while(bedingungenErfuellt)
        {
            bedingungenErfuellt = false;
            loadBigGraph();
            try
            {
                _kruskalAlg = new KruskalImpl(_graph);                
            }
            catch (Exception e)
            {
                
            }
        }
       
        // Test 1.) 
        KruskalMinimumSpanningTree<Vertex, MyWeightedEdge> kruskal = new KruskalMinimumSpanningTree<>(_graph);

        assertTrue(_kruskalAlg.getWeglaenge() == kruskal.getMinimumSpanningTreeTotalWeight());
    }
    
    @Test
    public void testBigGraph1() throws IOException
    {
        // Graph bsp3.graph laden
        boolean bedingungenErfuellt = true;
        while(bedingungenErfuellt)
        {
            bedingungenErfuellt = false;
            loadBigGraph();
            try
            {
                _kruskalAlg = new KruskalImpl(_graph);                
            }
            catch (Exception e)
            {
                bedingungenErfuellt = true;
            }
        }
       
        // Test 1.) 
        KruskalMinimumSpanningTree<Vertex, MyWeightedEdge> kruskal = new KruskalMinimumSpanningTree<>(_graph);

        assertTrue(_kruskalAlg.getWeglaenge() == kruskal.getMinimumSpanningTreeTotalWeight());
    }
    
    @Test
    public void testBigGraph3() throws IOException
    {
        // Graph bsp3.graph laden
        boolean bedingungenErfuellt = true;
        while(bedingungenErfuellt)
        {
            bedingungenErfuellt = false;
            loadBigGraph();
            try
            {
                _kruskalAlg = new KruskalImpl(_graph);                
            }
            catch (Exception e)
            {
                bedingungenErfuellt = true;
            }
        }
       
        // Test 1.) 
        KruskalMinimumSpanningTree<Vertex, MyWeightedEdge> kruskal = new KruskalMinimumSpanningTree<>(_graph);

        assertTrue(_kruskalAlg.getWeglaenge() == kruskal.getMinimumSpanningTreeTotalWeight());
    }
 
    // ..............Hilfsmethoden ..............
    
    
    private void loadBigGraph()
    {
        BigGraphImpl big = new BigGraphImpl();
        big.createBigGraph(100, 6000);
        _graph = big.getBigGraph();
    }
    
    private void loadTestGraph1() throws IOException
    {
        // **** File laden ****
        File graph_1 = new java.io.File(_home+_seperator+"Desktop"+ _seperator +"bspGraphen" + _seperator + "bsp3.graph");
        Path java_path = Paths.get(graph_1.getPath());
        List<String> dataList = Files.readAllLines(java_path); //throws Exception;
        
        // **** Graphen bauen ****
        _manager.loadGraph(dataList);  
        _graph = _manager.getGraph();       
    }
}
