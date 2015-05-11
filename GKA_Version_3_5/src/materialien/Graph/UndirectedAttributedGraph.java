package materialien.Graph;

import org.jgrapht.graph.Pseudograph;


public class UndirectedAttributedGraph<Vertex, MyWeightedEdge> extends Pseudograph<Vertex, MyWeightedEdge>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UndirectedAttributedGraph(Class<? extends MyWeightedEdge> edgeClass) {
		super(edgeClass);
		
	}

}
