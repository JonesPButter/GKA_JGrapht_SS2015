package werkzeuge.subwerkzeuge;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.WeightedPseudograph;
import org.junit.Test;

public class GraphBuilderTest 
{
	
	public List<String> _graphArray; 
	public GraphBuilder _builder;
	public Graph<Vertex,MyWeightedEdge> _graphForComparing;
	public Graph<Vertex, MyWeightedEdge> _graphEqualsClass;
	public Graph<Vertex, MyWeightedEdge> _graphEqualsGraph;

	
	
	public GraphBuilderTest()
	{

	}
	
	@Test
	public void graphDirectedClassTest()
	{
		_builder = new GraphBuilder();
		
		assertNull(_graphEqualsClass);
		assertNull(_graphEqualsGraph);
		assertNull(_graphForComparing);
		
		_graphArray =  new ArrayList<String>();
		
		_graphForComparing =  new DirectedPseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class );
		
		_graphArray.add("1,2");
		_graphArray.add("2,1");
		_graphArray.add("1,3");
		_graphArray.add(0, "#directed");
		
		_graphEqualsClass = _builder.createGraph(_graphArray, _graphEqualsClass);
		_graphEqualsGraph = _builder.createGraph(_graphArray, _graphEqualsGraph);

		_graphForComparing = _builder.createGraph(_graphArray, _graphForComparing);
		
		assertNotNull(_graphEqualsClass);
		assertTrue(_graphEqualsGraph.toString().equals(_graphForComparing.toString()));
	}
	
	
	@Test
	public void graphWeightedDircetedClass()
	{
		_builder = new GraphBuilder();
	
		assertNull(_graphEqualsClass);
		assertNull(_graphEqualsGraph);
		assertNull(_graphForComparing);

		_graphArray =  new ArrayList<String>();
		_graphForComparing =  new DirectedWeightedPseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class );
		
		_graphArray.add("1:12,2:23:56");
		_graphArray.add("2,1:12::67");
		_graphArray.add("1:12,3:34::78");
		_graphArray.add(0, "#directed #weighted");
		
		
		_graphEqualsClass = _builder.createGraph(_graphArray, _graphEqualsClass);
		_graphEqualsGraph = _builder.createGraph(_graphArray, _graphEqualsGraph);

		_graphForComparing = _builder.createGraph(_graphArray, _graphForComparing);
		
		assertNotNull(_graphEqualsClass);
		assertTrue(_graphEqualsGraph.toString().equals(_graphForComparing.toString()));
	}
	
	@Test
	public void graphAttributedDircetedClass()
	{
		_builder = new GraphBuilder();
		
		assertNull(_graphEqualsClass);
		assertNull(_graphEqualsGraph);
		assertNull(_graphForComparing);	
		
		_graphArray =  new ArrayList<String>();
		_graphForComparing =  new DirectedPseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class );
		
		_graphArray.add("1:12,2:23");
		_graphArray.add("2:23,1:12");
		_graphArray.add("1:12,3:34");
		_graphArray.add(0, "#directed #attributed");
		
		_graphEqualsClass = _builder.createGraph(_graphArray, _graphEqualsClass);
		_graphEqualsGraph = _builder.createGraph(_graphArray, _graphEqualsGraph);

		_graphForComparing = _builder.createGraph(_graphArray, _graphForComparing);

		assertNotNull(_graphEqualsClass);
		assertTrue(_graphEqualsGraph.toString().equals(_graphForComparing.toString()));
	}
	
	@Test
	public void graphAttributedWeightedDircetedClass()
	{
		_builder = new GraphBuilder();
	
		assertNull(_graphEqualsClass);
		assertNull(_graphEqualsGraph);
		assertNull(_graphForComparing);
		
		_graphArray =  new ArrayList<String>();
		
		_graphForComparing =  new DirectedWeightedPseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class );
		
		_graphArray.add("1:12,2:23::98");
		_graphArray.add("2:23,1:12::87");
		_graphArray.add("1:12,3:34::76");
		_graphArray.add(0, "#directed #attributed #weighted");

		_graphEqualsClass = _builder.createGraph(_graphArray, _graphEqualsClass);
		_graphEqualsGraph = _builder.createGraph(_graphArray, _graphEqualsGraph);

		_graphForComparing = _builder.createGraph(_graphArray, _graphForComparing);

		assertNotNull(_graphEqualsClass);
		
		assertTrue(_graphEqualsGraph.toString().equals(_graphForComparing.toString()));
	}
	
	
	@Test
	public void graphUndirecetClassTest()
	{
		_builder = new GraphBuilder();
		
		
		assertNull(_graphEqualsClass);
		assertNull(_graphEqualsGraph);
		assertNull(_graphForComparing);
		
		_graphArray =  new ArrayList<String>();
		_graphForComparing =  new Pseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class );

		_graphArray.add("1,2");
		_graphArray.add("2,1");
		_graphArray.add("1,3");
		
		_graphEqualsClass = _builder.createGraph(_graphArray, _graphEqualsClass);
		_graphEqualsGraph = _builder.createGraph(_graphArray, _graphEqualsGraph);

		_graphForComparing = _builder.createGraph(_graphArray, _graphForComparing);

		assertNotNull(_graphEqualsClass);
		
		assertTrue(_graphEqualsGraph.toString().equals(_graphForComparing.toString()));
	}

	
	@Test
	public void graphUndirecetWeihtedClassTest()
	{
		_builder = new GraphBuilder();

		assertNull(_graphEqualsClass);
		assertNull(_graphEqualsGraph);
		assertNull(_graphForComparing);

		_graphArray =  new ArrayList<String>();
		_graphForComparing =  new WeightedPseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class );
		
		_graphArray.add("1,2");
		_graphArray.add("2,1");
		_graphArray.add("1,3");
		_graphArray.add(0, "#weighted");
		
		_graphEqualsClass = _builder.createGraph(_graphArray, _graphEqualsClass);
		_graphEqualsGraph = _builder.createGraph(_graphArray, _graphEqualsGraph);

		_graphForComparing = _builder.createGraph(_graphArray, _graphForComparing);

		assertNotNull(_graphEqualsClass);
		
		assertTrue(_graphEqualsGraph.toString().equals(_graphForComparing.toString()));

	}

	
	@Test
	public void graphUndirecetAttributedClassTest()
	{
		_builder = new GraphBuilder();
		
		assertNull(_graphEqualsClass);
		assertNull(_graphEqualsGraph);
		assertNull(_graphForComparing);
		
		_graphArray =  new ArrayList<String>();
		_graphForComparing =  new Pseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class );
		
		_graphArray.add("1:12,2:23");
		_graphArray.add("2:23,1:12");
		_graphArray.add("1:12,3:34");
		_graphArray.add(0, "#attributed");
		
		_graphEqualsClass = _builder.createGraph(_graphArray, _graphEqualsClass);
		_graphEqualsGraph = _builder.createGraph(_graphArray, _graphEqualsGraph);

		_graphForComparing = _builder.createGraph(_graphArray, _graphForComparing);

		assertNotNull(_graphEqualsClass);
		
		assertTrue(_graphEqualsGraph.toString().equals(_graphForComparing.toString()));

	}
	
	@Test
	public void graphUndirecetWeightedAttributedClassTest()
	{

		_builder = new GraphBuilder();
		
		assertNull(_graphEqualsClass);
		assertNull(_graphEqualsGraph);
		assertNull(_graphForComparing);
		
		_graphArray =  new ArrayList<String>();
		_graphForComparing =  new WeightedPseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class );
		
		_graphArray.add("1:21,2:21::123");
		_graphArray.add("2:21,1:21::213");
		_graphArray.add("1:21,3:49::324");
		_graphArray.add(0, "#attributed #weighted");
		
		_graphEqualsClass = _builder.createGraph(_graphArray, _graphEqualsClass);
		
		_graphEqualsGraph = _builder.createGraph(_graphArray, _graphEqualsGraph);

		_graphForComparing = _builder.createGraph(_graphArray, _graphForComparing);
		
		

		
		System.out.println(_graphEqualsClass.getClass());
		System.out.println(_graphEqualsGraph.getClass());
		System.out.println(_graphForComparing.getClass());
		
		

		assertNotNull(_graphEqualsClass);
		
		assertTrue(_graphEqualsGraph.toString().equals(_graphForComparing.toString()));
	}
}
