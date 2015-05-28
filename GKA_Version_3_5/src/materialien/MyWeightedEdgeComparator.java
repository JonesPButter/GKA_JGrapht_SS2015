package materialien;

import java.util.Comparator;

public class MyWeightedEdgeComparator implements Comparator<MyWeightedEdge>
{
    @Override
    public int compare(MyWeightedEdge o1, MyWeightedEdge o2)
    {
        return o1.compareTo(o2);
    }
}
