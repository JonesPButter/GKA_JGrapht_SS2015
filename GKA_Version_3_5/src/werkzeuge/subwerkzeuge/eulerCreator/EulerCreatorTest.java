package werkzeuge.subwerkzeuge.eulerCreator;

import static org.junit.Assert.*;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.junit.Before;
import org.junit.Test;

public class EulerCreatorTest {

	EulerCreator _eulerCreator;
	
	@Before
	public void setUp() throws Exception 
	{
		_eulerCreator = new EulerCreator();
		
	}
	
	@Test
	public void testEulerCreaotr() {
		_eulerCreator.creatEulerGraph(100); 
		
		Graph<Vertex, MyWeightedEdge> graph = _eulerCreator.getEulerGraph();
		
		assertFalse(graph == null);
		assertTrue(graph.vertexSet().size() == 100);
		assertTrue(_eulerCreator.isGraphConnected(graph));
		assertTrue(_eulerCreator.isEulergraph(graph));	
		assertTrue(onlyEvenDegreesOfVertices(graph));
		
	}
	
	@Test
	public void testEulerCreator2() {
		_eulerCreator.creatEulerGraph(100); 
		
		Graph<Vertex, MyWeightedEdge> graph = _eulerCreator.getEulerGraph();
		
		assertFalse(graph == null);
		assertTrue(graph.vertexSet().size() == 100);
		assertTrue(_eulerCreator.isGraphConnected(graph));
		assertTrue(_eulerCreator.isEulergraph(graph));	
		assertTrue(onlyEvenDegreesOfVertices(graph));
		
	}
	
	
    private boolean onlyEvenDegreesOfVertices(Graph<Vertex, MyWeightedEdge> graph)
    {
        int edgeCounter;        
        for(Vertex v : graph.vertexSet())
        {
            edgeCounter = 0;
            for(MyWeightedEdge edge : graph.edgesOf(v))
            {
                edgeCounter++;
            }
            if((edgeCounter % 2) != 0)
            {
                return false;
            }
        }
        return true;
    }
	

	
}
