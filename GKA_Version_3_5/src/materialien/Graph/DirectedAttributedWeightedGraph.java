package materialien.Graph;


import org.jgrapht.graph.DirectedWeightedPseudograph;


	public class DirectedAttributedWeightedGraph<Vertex, MyWeightedEdge> extends DirectedWeightedPseudograph<Vertex, MyWeightedEdge>
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DirectedAttributedWeightedGraph(
				Class<? extends MyWeightedEdge> edgeClass) {
			super(edgeClass);

		}

		
	}
