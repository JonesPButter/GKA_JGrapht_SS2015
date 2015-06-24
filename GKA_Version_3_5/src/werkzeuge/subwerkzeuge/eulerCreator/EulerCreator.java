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

	
	
	Pseudograph<Vertex, MyWeightedEdge> _graph;
	public EulerCreator() {
		
		_ui = new EulerCreatorUI();
		_knotenAnzahl = 0;
		_vertexList  = new ArrayList<>();
		

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
					creatEulerGraph(_knotenAnzahl);
					informiereUeberAenderung(_graph);
				}
			}
		});
	}
	
	
	/**
	 * prüft ob die Eingabe der UI korrekt ist und gibt dann einen bool zurück
	 * @param anzahl
	 * @return boolean
	 */
	public boolean eingabeKorrekt(String anzahl)
	{
		try 
		{
			
			if(Integer.parseInt(anzahl) > 0) 
			{
				_knotenAnzahl = Integer.parseInt(anzahl);
				return true;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return false;
	}

	/**
	 * Diese Methode erstellt den ganzen Graphen mit der gewünschten Kantenanzahl. Und speichert den im _graph.
	 * @param knotenAnzahl
	 */
	public void creatEulerGraph(int knotenAnzahl) 
	{
		_graph = new Pseudograph<Vertex, MyWeightedEdge>(MyWeightedEdge.class);
		_vertexList = new LinkedList<Vertex>();
		_vertexMap = new HashMap<Vertex, Integer>();
		
		createVertex(knotenAnzahl);
		createEdges(knotenAnzahl);
		
		goOverAllVertieses(_graph);
		
	}
	
	/**
	 * es werden demm Graphen _graph die Knoten hinzu gefügt. Da das Attribute egal ist, wrid es immer 0 sein.
	 * @param knotenAnzahl
	 */
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
	
	
	/**
	 * In deiser Mehtode werden alle Kanten erzeugt.
	 * Zuerst werden solange Kreise gezogen bis, der Graph zusammen hängend ist. 
	 * @param knotenAnzahl
	 */
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
		while(!isGraphConnected(_graph)) 
		{
			CircleSize = randomGenerator.nextInt(knotenAnzahl -3) +3;
			for(int i = 0; i < CircleSize; i++)
			{
				while(source.equals(target))
				{
					target = _vertexList.get(randomGenerator.nextInt(knotenAnzahl));
				}
				if(i == CircleSize -1)
				{

					target = start;				
				}
				if(!_graph.containsEdge(source, target) && !source.equals(target)) 
				{
					_graph.addEdge(source, target);
				}
				parent = source;
				source = target;					
				
			}
		}
		
	}
	
	/**
	 * zeigt die UI 
	 */
    public void showUI()
    {
        _ui.getDialog().setVisible(true);
    }

    /**
     * diese Methode prüft ob der Graph ein eulerGraph ist oder ist. Und gibt einen boolean zurück
     * @param graph
     * @return boolean
     */
    public boolean isEulergraph(Graph<Vertex, MyWeightedEdge> graph){
    	
    	if(!isGraphConnected(graph))
    	{
    		return false;
    	}

    	if(!onlyEvenDegreesOfVertices(graph))
    	{
    		return false;
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

    /**
     * gitb einen Boolean zurück, ob der Graph zusammen hängend ist
     * @param graph
     * @return
     */
	public boolean isGraphConnected(Graph<Vertex, MyWeightedEdge> graph)
	{
		if(GraphTests.isConnected(graph)) 
		{
			return true;
		}
		return false;
		
	}

    /**
     * gibt alle Nachbarn eines Knote im Graphen zurück. Im Set
     * @param graph
     * @param n
     * @return Set<Vertex>
     */
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
    
    /**
     * Diese Methode schaut, on alle Knoten eine gerade Anzahl an Kanten hat.
     * @param graph
     * @return
     */
    public boolean goOverAllVertieses(Graph<Vertex, MyWeightedEdge> graph)
    {
    	boolean result = false;
    	
    	Set<Vertex> set = graph.vertexSet();
    	Vertex source = null;
    	Vertex target = null;
    	
    	for(Vertex ver : set)
    	{
    		if(getNeighbours(_graph, ver).size() % 2 == 1) 
    		{
    			if(source == null)
    			{
    				source = ver;
    			}
    			else if(source != null && target == null)
    			{
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
   
    /**
     * gibt den Graphen zurück
     * @return Graph<Vertex, MyWeightedEdge>
     */
	public Graph<Vertex, MyWeightedEdge> getEulerGraph() {

		return _graph;
	}
	
		
	
}
