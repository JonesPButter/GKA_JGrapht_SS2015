package materialien.Graph;

import org.jgrapht.graph.DirectedPseudograph;

public class DirectedAttributedGraph<Vertex, MyWeightedEdge> extends DirectedPseudograph<Vertex, MyWeightedEdge>
{

	public DirectedAttributedGraph(Class<? extends MyWeightedEdge> edgeClass)
	{
		super(edgeClass);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
}
