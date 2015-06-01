package materialien;

import java.util.Comparator;

public class MyVertexComparator implements Comparator<Vertex>
{

    @Override
    public int compare(Vertex o1, Vertex o2)
    {
        return o1.compareTo(o2);
    }

}
