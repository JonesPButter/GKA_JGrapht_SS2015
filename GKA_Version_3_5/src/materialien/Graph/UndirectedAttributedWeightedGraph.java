package materialien.Graph;

import org.jgrapht.graph.WeightedPseudograph;

public class UndirectedAttributedWeightedGraph<Vertex, MyWeightedEdge> extends WeightedPseudograph<Vertex, MyWeightedEdge> {

	public UndirectedAttributedWeightedGraph(
			Class<? extends MyWeightedEdge> edgeClass) {
		super(edgeClass);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
