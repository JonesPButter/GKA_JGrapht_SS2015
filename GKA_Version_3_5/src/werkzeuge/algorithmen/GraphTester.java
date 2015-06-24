package werkzeuge.algorithmen;

import java.util.List;

import materialien.MyWeightedEdge;
import materialien.Vertex;

import org.jgrapht.Graph;

public class GraphTester
{
    
    /*
     * Prüft, ob die aufeinanderfolgenden Kanten innerhalb der Eulertour
     * miteinander verbunden sind.
     */
    protected boolean isKantenfolge(List<MyWeightedEdge> eulertour,
            Graph<Vertex, MyWeightedEdge> graph)
    {
        /*
         * 1. Über alle Kanten iterieren
         * 2. Jede Kante, bis auf die letzte, mit dem Nachfolger vergleichen
         *      - sind die Kanten nicht miteinander über einen Knoten verbunden,
         *        kann es sich nicht um eine Kantenfolge handeln
         */
        for(int i=0;i<eulertour.size();i++)
        {
            if(i+1<eulertour.size()) // Die letzte Kante nicht
            {
                if(!isConnected(eulertour.get(i),eulertour.get(i+1),graph))
                {
                    return false;
                }
            }
        }    
        return true;
    }


    /*
     * Prüft, ob zwei Kanten miteinander verbunden sind
     */
    protected boolean isConnected(MyWeightedEdge first,
            MyWeightedEdge second, Graph<Vertex, MyWeightedEdge> graph)
    {
        Vertex source1 = graph.getEdgeSource(first);
        Vertex target1 = graph.getEdgeTarget(first);
        
        Vertex source2 = graph.getEdgeSource(second);
        Vertex target2 = graph.getEdgeTarget(second);
        
        if(source1.equals(source2) || source1.equals(target2) || 
                target1.equals(source2) || target1.equals(target2))
            return true;
        {
        }        
        return false;
    }

}
