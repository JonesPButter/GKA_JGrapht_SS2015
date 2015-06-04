package materialien;

import java.util.Comparator;
import java.util.Map;

public class MyVertexComparator implements Comparator<Vertex>
{

    Map<Vertex,Double> _schlüssel; 
    
    public MyVertexComparator(Map<Vertex,Double> schlüssel)
    {
        _schlüssel = schlüssel;
    }
    
    @Override
    public int compare(Vertex o1, Vertex o2)
    {
//        System.out.println(o1 + " mit: " + _schlüssel.get(o1) + " und " + o2 + " mit: " +_schlüssel.get(o2) + " werden verglichen");
        return _schlüssel.get(o1).compareTo(_schlüssel.get(o2));
    }

}
