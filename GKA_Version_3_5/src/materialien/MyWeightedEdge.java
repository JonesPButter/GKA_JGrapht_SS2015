package materialien;

import org.jgrapht.graph.DefaultWeightedEdge;

public class MyWeightedEdge extends DefaultWeightedEdge
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String toString()
    {
        return this.getWeight()+"";
    }
    
    public double getEdgeWeight()
    {
        return this.getWeight();
    }
}
