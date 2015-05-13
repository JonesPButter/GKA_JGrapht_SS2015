package werkzeuge.subwerkzeuge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import materialien.MyWeightedEdge;
import materialien.Vertex;
import materialien.Graph.DirectedAttributedGraph;
import materialien.Graph.DirectedAttributedWeightedGraph;
import materialien.Graph.UndirectedAttributedGraph;
import materialien.Graph.UndirectedAttributedWeightedGraph;

import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.WeightedPseudograph;

public class GraphSaver {

	
	/**
	 * wandelt eine Graph in eine Liste von Strings
	 * @param graph
	 * @return
	 */
	public List<String> graphToArray(Graph<Vertex, MyWeightedEdge> graph)
	{
		
		List<String> result = new ArrayList<String>();
		String allAttributes = "";
		
		if(graph instanceof DirectedPseudograph)
		{
			
			allAttributes = "#directed";
			result.add(allAttributes);
			result.addAll(unweightedGraphInArray(graph));
			
		}
		else if(graph instanceof DirectedWeightedPseudograph)
		{
			
			allAttributes = "#directed #weighted";	
			result.add(allAttributes);
			result.addAll(weightedGraphInArray(graph));
			
		}
		else if(graph instanceof DirectedAttributedGraph)
		{
			
			allAttributes = "#directed #attributed";
			result.add(allAttributes);
			result.addAll(unweightedAttributedGraphInArray(graph));
			
		}
		else if(graph instanceof DirectedAttributedWeightedGraph)
		{
			
			allAttributes = "#directed #weighted #attributed";
			result.add(allAttributes);
			result.addAll(weightedAttributedGraphInArray(graph));
			
		}
		else if(graph instanceof UndirectedAttributedWeightedGraph)
		{
			
			allAttributes = "#attributed #weighted";			
			result.add(allAttributes);
			result.addAll(weightedAttributedGraphInArray(graph));
		}
		else if(graph instanceof WeightedPseudograph)
		{
			
			allAttributes = "#weighted";
			result.add(allAttributes);
			result.addAll(weightedGraphInArray(graph));
		}
		else if(graph instanceof UndirectedAttributedGraph)
		{
			result.add("#attributed");
			result.add(allAttributes);
			result.addAll(unweightedAttributedGraphInArray(graph));
			
		}
		else if(graph instanceof Pseudograph)
		{
			
			allAttributes = "";			
			result.add(allAttributes);
			result.addAll(unweightedGraphInArray(graph));
			
		}
				

	
		return result;
	
	}
	
	
	/**
	 * wandelten einen gewichteten Graphen in eine Liste von Strings um 
	 * und gibt die Liste zurück
	 * @param graph
	 * @return
	 */
	public List<String> unweightedGraphInArray(Graph<Vertex, MyWeightedEdge> graph)
	{
		List<String> result = new ArrayList<String>();
		Set<Vertex> vertexSet = graph.vertexSet();
		List<Vertex> vertexlist = new LinkedList<Vertex>();
		vertexlist.addAll(vertexSet);
		Set<MyWeightedEdge> edgeSet = graph.edgeSet();
	
		for(MyWeightedEdge dwe : edgeSet)
		{
			Vertex source  = (Vertex) graph.getEdgeSource(dwe);
			Vertex target = (Vertex) graph.getEdgeTarget(dwe);

				
			String edgeString = source.getName() + " , " + target.getName();
		
			if(vertexlist.contains((Vertex) graph.getEdgeSource(dwe)))
			{
				vertexlist.remove((Vertex) graph.getEdgeSource(dwe));
			}
			
			if(vertexlist.contains((Vertex) graph.getEdgeTarget(dwe)))
			{
				vertexlist.remove((Vertex) graph.getEdgeTarget(dwe));
			}
			result.add(edgeString);
			
			
				
		}
		if(!vertexlist.isEmpty())
		{
			Iterator<Vertex> it = vertexlist.iterator();
			while(it.hasNext())
			{
				String sourceA = it.next().toString();
				result.add(sourceA);
			}
		}
		return result;
	}
	
	
	
	/**
	 * wandelten einen gewichteten, attributed Graphen in eine Liste von Strings um 
	 * und gibt die Liste zurück
	 * @param graph
	 * @return
	 */
	public List<String> unweightedAttributedGraphInArray(Graph<Vertex, MyWeightedEdge> graph)
	{
		List<String> result = new ArrayList<String>();
		Set<Vertex> vertexSet = graph.vertexSet();
		List<Vertex> vertexlist = new LinkedList<Vertex>();
		vertexlist.addAll(vertexSet);
		Set<MyWeightedEdge> edgeSet = graph.edgeSet();
	
		for(MyWeightedEdge dwe : edgeSet)
		{
			Vertex source  = (Vertex) graph.getEdgeSource(dwe);
			Vertex target = (Vertex) graph.getEdgeTarget(dwe);
			
		
				String edgeString = source.getName() + ":" + source.getAttr() + " , " + target.getName() + ":" + target.getAttr();
				
				if(vertexlist.contains((Vertex) graph.getEdgeSource(dwe)))
				{
					vertexlist.remove((Vertex) graph.getEdgeSource(dwe));
				}
				
				if(vertexlist.contains((Vertex) graph.getEdgeTarget(dwe)))
				{
					vertexlist.remove((Vertex) graph.getEdgeTarget(dwe));
				}
				result.add(edgeString);
		}
				
		
		if(!vertexlist.isEmpty())
		{
			Iterator<Vertex> it = vertexlist.iterator();
			while(it.hasNext())
			{
				String sourceA = it.next().toString();
				result.add(sourceA);
			}
		}
		return result;
	}
	
	
	
	
	
	/**
	 * wandelt ein  gerichten und gewichteten Graph  in eine Liste von String ums
	 * und gibt dies zurck
	 * @param graph
	 * @return List<String>
	 */
	public List<String> weightedGraphInArray(Graph<Vertex, MyWeightedEdge> graph)
	{
	
		List<String> result = new ArrayList<String>();
		Set<Vertex> vertexSet = graph.vertexSet();
		List<Vertex> vertexlist = new LinkedList<Vertex>();
		vertexlist.addAll(vertexSet);
		Set<MyWeightedEdge> edgeSet = graph.edgeSet();
		
		
		for(MyWeightedEdge dwe : edgeSet)
		{
			Vertex source  = (Vertex) graph.getEdgeSource(dwe);
			Vertex target = (Vertex) graph.getEdgeTarget(dwe);
			int weighing = (int) graph.getEdgeWeight(dwe);
			
		
			String edgeString = source.getName() + " , " + target.getName() +"::"+ weighing;
			
			if(vertexlist.contains((Vertex) graph.getEdgeSource(dwe)))
			{
				vertexlist.remove((Vertex) graph.getEdgeSource(dwe));
			}
			
			if(vertexlist.contains((Vertex) graph.getEdgeTarget(dwe)))
			{
				vertexlist.remove((Vertex) graph.getEdgeTarget(dwe));
			}
			result.add(edgeString);
			
		}
			if(!vertexlist.isEmpty())
			{
				Iterator<Vertex> it = vertexlist.iterator();
				while(it.hasNext())
				{
					String sourceA = it.next().toString();
					result.add(sourceA);
				}
				
			}
		return result;
	
	}
/**	
 * speichert gewichtete Graphen in ein Strings, welcher später die .graph datei wird. 
 * 
 * @param grapnte bekommt einen String, die später die List sein wird.
 * @return eine Liste von String List<String>
 */
	public List<String> weightedAttributedGraphInArray(Graph<Vertex, MyWeightedEdge> graph)
	{
	
		List<String> result = new ArrayList<String>();
		Set<Vertex> vertexSet = graph.vertexSet();
		List<Vertex> vertexlist = new LinkedList<Vertex>();
		vertexlist.addAll(vertexSet);
		Set<MyWeightedEdge> edgeSet = graph.edgeSet();
		
		//Geht über jede Kante und Speichert diese als String,
		//diese wird später der List von String (result) hinzu gefügt.
		//Zum schluss werden die Knoten aus der Liste entfernt.
		for(MyWeightedEdge dwe : edgeSet)
		{
			Vertex source  = (Vertex) graph.getEdgeSource(dwe);
			Vertex target = (Vertex) graph.getEdgeTarget(dwe);
			int weighing = (int) graph.getEdgeWeight(dwe);		
			
			String edgeString = source.toString() + ":" + source.getAttr() + " , " + target.toString() + ":" + target.getAttr() +"::"+ weighing;
			
			if(vertexlist.contains((Vertex) graph.getEdgeSource(dwe)))
			{
				vertexlist.remove((Vertex) graph.getEdgeSource(dwe));
			}
			
			if(vertexlist.contains((Vertex) graph.getEdgeTarget(dwe)))
			{
				vertexlist.remove((Vertex) graph.getEdgeTarget(dwe));
			}
			result.add(edgeString);


		}
		//Wenn die List noch nicht leer ist. Habe diese Knoten keine Knte und werden einzelt gespeichert
		if(!vertexlist.isEmpty())
		{
			Iterator<Vertex> it = vertexlist.iterator();
			while(it.hasNext())
			{
				String sourceA =  it.next().toString();
				result.add(sourceA);
			}
			
		}
		return result;
	
	}	
}
