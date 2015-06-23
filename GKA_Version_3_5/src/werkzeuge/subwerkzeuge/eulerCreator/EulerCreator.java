package werkzeuge.subwerkzeuge.eulerCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import materialien.MyWeightedEdge;
import materialien.Vertex;


//import org.jGrGraphUtils.JGraphTUtils
import org.jgrapht.Graph;
import org.jgrapht.experimental.GraphTests;
import org.jgrapht.graph.Pseudograph;

import werkzeuge.ObservableSubwerkzeug;

public class EulerCreator extends ObservableSubwerkzeug {
	EulerCreatorUI _ui;
	int _knotenAnzahl;
	List<Vertex> _vertexList;
	Map<Vertex, Integer> _vertexMap;
//	Array <Vertex<Vertex>> _edgesArray;
	
	
//	UndirectedAttributedGraph<Vertex, MyWeightedEdge> _graph;
	Pseudograph<Vertex, MyWeightedEdge> _graph;
	public EulerCreator() {
		
		_ui = new EulerCreatorUI();
		_knotenAnzahl = 0;
		_vertexList  = new ArrayList<>();
		
		
//		_graph = new UndirectedAttributedGraph<>(MyWeightedEdge.class);
		_graph = new Pseudograph<>(MyWeightedEdge.class);
		registiereListener();
		
	}
	
	public void registiereListener() {
		
		 _ui.getOkButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String knontenAnzahl = _ui.getKnotenanzahl().getText();
				
				if(eingabeKorrekt(knontenAnzahl)) {
					_ui.getDialog().dispose();
//					createVertex(_knotenAnzahl);
//					createEdges(_knotenAnzahl);
					creatEulerGraph(_knotenAnzahl);
					//TODO createEulerGraph(_knotenAnzahl);
					informiereUeberAenderung(_graph);
				}
			}
		});
	}
	
	public boolean eingabeKorrekt(String anzahl)
	{
		try 
		{
			
			if(Integer.parseInt(anzahl) > 0) 
			{
				_knotenAnzahl = Integer.parseInt(anzahl);
//				System.out.println(_knotenAnzahl);
				return true;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return false;
	}
	
	public void creatEulerGraph(int knotenAnzahl) 
	{
		_graph = new Pseudograph<Vertex, MyWeightedEdge>(MyWeightedEdge.class);
		_vertexList = new LinkedList<Vertex>();
		_vertexMap = new HashMap<Vertex, Integer>();
		
		createVertex(knotenAnzahl);
		createEdges(knotenAnzahl);
		
		System.out.println(goOverAllVertieses(_graph));
		
		
	}
	
	
	public void createVertex(int knotenAnzahl)
	{
		Random random = new Random();
		String vertexName;
		int x, y;
		Vertex vertex;
		
		for(int i = 1; i < knotenAnzahl +1; i++) 
		{
			x = random.nextInt((1200) + 1);
			y = random.nextInt((800) + 1);
			
			vertexName = "V" + i;
			
			vertex = Vertex.createVertex(vertexName, 0, x, y);
			
			_graph.addVertex(vertex);
			_graph.addVertex(vertex);
			_vertexList.add(vertex);
		}
		
	}
	
	
	public void createEdges(int knotenAnzahl) 
	{
		
		
		Random randomGenerator = new Random();
		Vertex source, target, start;
		Vertex parent = null;
		MyWeightedEdge edge;
		int CircleSize;
		
		start  = _vertexList.get(randomGenerator.nextInt(knotenAnzahl -1));
		source = start;
		target = _vertexList.get(randomGenerator.nextInt(knotenAnzahl -1));
		int testInt = 0;
		while(!isGraphConnected(_graph)) 
		{
			CircleSize = randomGenerator.nextInt(knotenAnzahl -3) +3;
//			System.out.println("CircleSie: " + CircleSize);
			for(int i = 0; i < CircleSize; i++)
			{
				while(source.equals(target))
				{
					target = _vertexList.get(randomGenerator.nextInt(knotenAnzahl));
					
//					System.out.println("Parent: " + parent);
//					System.out.println("Target: " + target);
//					System.out.println("Source: " + source);
//					System.out.println("****************************************");
				}
				
//				System.out.println("***************************DONE***********************");
//				source = _vertexList.get(randomGenerator.nextInt(knotenAnzahl -1));
				if(i == CircleSize -1)
				{
//					System.out.println("Ende");
					target = start;				
				}
				if(!_graph.containsEdge(source, target) && !source.equals(target)) 
				{
					_graph.addEdge(source, target);
				}
				parent = source;
				source = target;					
				
			}
			testInt++;
		}
		
	}
	
    public void showUI()
    {
        _ui.getDialog().setVisible(true);
    }


    public boolean isEulergraph(Graph<Vertex, MyWeightedEdge> graph){
    	
    	if(!isGraphConnected(graph))
    	{
    		return false;
    	}

    	if(onlyEvenDegreesOfVertices(graph))
    	{
//    		System.out.println("False");
    		return false;
    	}
    	return true;
    
    }
    
    private boolean allVertexHasEvenEdges(Graph<Vertex, MyWeightedEdge> graph) {
		
    	Set<Vertex> vertexSet = new HashSet<Vertex>();
    	int neighborusCount;
    	
    	vertexSet.addAll(_vertexList);
//    	System.out.println(vertexSet.size());
    	for(Vertex ver : vertexSet) 
    	{
    		neighborusCount = 0;

    		for(MyWeightedEdge edge : graph.edgesOf(ver) )
    		{
    			neighborusCount++;
    		}
    		
    		if(neighborusCount < 2 || neighborusCount % 2 != 0)
    		{
//    			System.out.println("FAAAAAAAAAAAAAAAAAAALLLLLLLLLLLSSSSSSSSSSE");
    			return false;    			
    		}
    	}
    	return true;
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

	public boolean isGraphConnected(Graph<Vertex, MyWeightedEdge> graph)
	{
		if(GraphTests.isConnected(graph)) 
		{
			return true;
		}
		return false;
		
	}

    
    
    private Set<Vertex> getNeighbours(Graph<Vertex, MyWeightedEdge> graph, Vertex n)
    {
        Set<Vertex> adjacentNodes= new HashSet<Vertex>();
        Set<MyWeightedEdge> edges= graph.edgesOf(n);
       for(MyWeightedEdge edge : edges)
       {
           Vertex source = graph.getEdgeSource(edge);
           Vertex neighbour = graph.getEdgeTarget(edge);
           if(source.equals(n))
           {
               adjacentNodes.add(neighbour);               
           } else if(neighbour.equals(n))
           {
               adjacentNodes.add(source);
           }
       }
       return adjacentNodes;        
    }
    
    public boolean goOverAllVertieses(Graph<Vertex, MyWeightedEdge> graph)
    {
    	boolean result = false;
    	
    	Set<Vertex> set = graph.vertexSet();
    	Vertex source = null;
    	Vertex target = null;
    	
    	for(Vertex ver : set)
    	{
    		System.out.println( getNeighbours(_graph, ver).size() );
    		if(getNeighbours(_graph, ver).size() % 2 == 1) 
    		{
    			if(source == null)
    			{
    				source = ver;
    			}
    			else if(source != null && target == null)
    			{
    				System.out.println("Bla");
    				_graph.addEdge(source, ver);
    				source = null;
    			}
    			
    		}
    		
    	}
    	
    	if(source == null) 
    	{
    		result = true;
    	}
    	return result;
    	
    }
   
    
	public Graph<Vertex, MyWeightedEdge> getEulerGraph() {
		// TODO Auto-generated method stub
		return _graph;
	}
	
		
	
}
