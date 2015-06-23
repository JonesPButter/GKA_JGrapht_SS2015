package materialien;

import org.jgrapht.graph.DefaultWeightedEdge;

public class MyWeightedEdge extends DefaultWeightedEdge implements Comparable<MyWeightedEdge>
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    public String toString()
    {
        return "("+this.getSource()+"-" + this.getTarget() +" : " +  this.getWeight()+")";
    }
    
    public double getEdgeWeight()
    {
        return this.getWeight();
    }

    @Override
    public int compareTo(MyWeightedEdge o)
    {
        
        Double thisWeight =  (Double)this.getEdgeWeight();
        return thisWeight.compareTo((Double)((MyWeightedEdge)o).getEdgeWeight());
    }
    
}
