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

import org.jgrapht.Graph;
import org.jgrapht.graph.Pseudograph;

import materialien.MyWeightedEdge;
import materialien.Vertex;
import materialien.Graph.UndirectedAttributedGraph;
import materialien.Graph.UndirectedAttributedWeightedGraph;
import werkzeuge.ObservableSubwerkzeug;

public class EulerCreator extends ObservableSubwerkzeug {
	EulerCreatorUI _ui;
	int _knotenAnzahl;
	List<Vertex> _vertexList;
	Map<Vertex, Integer> _vertexMap;
	
	
	
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
					createVertex(_knotenAnzahl);
					createEdges(_knotenAnzahl);
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
				System.out.println(_knotenAnzahl);
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
		
		
	}
	
	
	public void createVertex(int knotenAnzahl)
	{
		Random random = new Random();
		String vertexName;
		int x, y;
		Vertex vertex;
		
		for(int i = 1; i < knotenAnzahl; i++) 
		{
			x = random.nextInt((1200) + 1);
			y = random.nextInt((800) + 1);
			
			vertexName = "V" + i;
			
			vertex = Vertex.createVertex(vertexName, 0, x, y);
			
			_graph.addVertex(vertex);
			_graph.addVertex(vertex);
			_vertexList.add(vertex);
			//_vertexMap.put(vertex, 0);
		}
		
	}
	
	
	public void createEdges(int knotenAnzahl) {
		
		Random random  = new Random();
		Vertex source, target;
		MyWeightedEdge edge;
		
		while(!isEulergraph(_graph)) 
		{
			source = _vertexList.get(random.nextInt(knotenAnzahl -1));
			target = _vertexList.get(random.nextInt(knotenAnzahl -1));
			
//			System.out.println("Bla");
			if((source != target) && (getNeighbours(_graph, source).size() % 2 != 0 && getNeighbours(_graph, target).size() % 2 != 0) ||(getNeighbours(_graph, source).size() % 2 == 0 && getNeighbours(_graph, target).size() % 2 == 0) ) 
//			if(source != target)	
			{
				_graph.addEdge(source, target);
				//((UndirectedAttributedGraph<Vertex, MyWeightedEdge>)_graph).setEdgeWeight(edge, 1 );
			}
			
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
    	if(!allVertexHasEvenEdges(graph)) 
    	{
    		System.out.println("False");
    		return false;
    	}
    	System.out.println("True");
    	return true;
    
    }
    
    private boolean allVertexHasEvenEdges(Graph<Vertex, MyWeightedEdge> graph) {
		
    	Set<Vertex> vertexSet = new HashSet<Vertex>();
    	int neighborusCount;
    	
//    	vertexSet = graph.vertexSet();
//    	vertexSet = (Set<Vertex>) _vertexList;
    	vertexSet.addAll(_vertexList);
    	System.out.println(vertexSet.size());
    	for(Vertex ver : vertexSet) 
    	{
    		neighborusCount = 0;
//    		neighborus = getNeighbours(graph, ver).size();
    		//System.out.println(neighborus);
    		for(MyWeightedEdge edge :graph.edgesOf(ver))
    		{
    			neighborusCount++;
    		}
    		
    		if(neighborusCount < 2 || neighborusCount % 2 != 0)
    		{
    			return false;    			
    		}
    	}
    	
    	return true;
    	
	}

	public boolean isGraphConnected(Graph<Vertex, MyWeightedEdge> graph)
    {
    	boolean result = false;

        Vertex start = (Vertex) graph.vertexSet().iterator().next();
        Vertex add;
        Set<Vertex> neighbours = getNeighbours(graph, start);
        Set<Vertex> allVertices = new HashSet<Vertex>();
        allVertices.add(start);
        allVertices.addAll(neighbours);
   
        while(!neighbours.isEmpty())
        {
            while(neighbours.iterator().hasNext())
            {
                add = neighbours.iterator().next();
                for(Vertex v : getNeighbours(graph, add))
                {
                    if(!allVertices.contains(v))
                    {
                        allVertices.add(v);
                        neighbours.add(v);
                    }
                }
                neighbours.remove(add);
            }
        }

        if(allVertices.equals(graph.vertexSet()))
        {
            result = true;
        }

        return result;
        
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
   
    
	public Graph<Vertex, MyWeightedEdge> getEulerGraph() {
		// TODO Auto-generated method stub
		return _graph;
	}
	
}
