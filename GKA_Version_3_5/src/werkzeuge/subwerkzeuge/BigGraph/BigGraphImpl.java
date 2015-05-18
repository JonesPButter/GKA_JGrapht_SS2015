package werkzeuge.subwerkzeuge.BigGraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
//import java.util.HashSet;
//import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
//import java.util.Set;



import javax.swing.JOptionPane;

//import org.jgrapht.DirectedGraph;
//import org.jgrapht.Graph;
//import org.jgrapht.UndirectedGraph;
//import org.jgrapht.alg.NeighborIndex;



import werkzeuge.ObservableSubwerkzeug;
//import werkzeuge.SubwerkzeugObserver;
import materialien.MyWeightedEdge;
import materialien.Vertex;
import materialien.Graph.UndirectedAttributedWeightedGraph;
//import werkzeuge.ObservableSubwerkzeug;

public class BigGraphImpl extends ObservableSubwerkzeug{

	BigGraphUI _ui;
	int _kantenAnzahl;
	int _knotenAnzahl;
	List<Vertex> _vertexList;
	Map<Vertex, Integer> _vertexMap;
	
	UndirectedAttributedWeightedGraph<Vertex, MyWeightedEdge> _graph;
	
	public BigGraphImpl()
	{
		_ui = new BigGraphUI();
		registiereListener();

	}
	
	
	/**
	 * registiert am OK Button einen Listener, der die Eingaben prüft.
	 */
	 public void registiereListener()
	 {
		 _knotenAnzahl = 100;
		 _kantenAnzahl = 6000;

		 _ui.getOkButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Big Graph");
				String knotenAnzahl = _ui.getKnotenanzahl().getText();
				String kantenAnzahl = _ui.getKantenanzahl().getText();
				
				if(eingabenKorrekt(knotenAnzahl, kantenAnzahl))
				{
					_ui.getDialog().dispose();
					createBigGraph();
					_graph = getBigGraph();
					informiereUeberAenderung(_graph);						
				
				}
				else
				{
				   JOptionPane.showMessageDialog(_ui.getDialog(),
                            "Die Eingaben waren nicht korrekt."
                            + " Die Vertexes konnten nicht gefunden werden. "
                            + "Bitte überprüfen Sie Ihre Eingaben.");
				}
				
			}
		});
	 }
	 /**
	  * prüft ob die Eingaben in der UI korrekt sind. Ist müssen Zahlen sein und größer 0
	  * @param knotenAnzahl
	  * @param kantenAnzahl
	  * @return boolean
	  */
	 private boolean eingabenKorrekt(String knotenAnzahl, String kantenAnzahl)
	 {
		 
		 
		 if(false)//!knotenAnzahl.matches("(?[0-9]+)") || !kantenAnzahl.matches("(?[0-9]+)"))
		 {
			 JOptionPane.showMessageDialog(null , "Fehlerhafte Eingabe. "
                     + "\n Bitte geben Sie eine gültige Zahl ein."
                     + " Beispiele: 5|| 8 || 42");
		}
		 else if(Integer.parseInt(kantenAnzahl) > 0 && Integer.parseInt(knotenAnzahl) > 0)
		 {
			 _knotenAnzahl = Integer.parseInt(knotenAnzahl);
			 _kantenAnzahl = Integer.parseInt(kantenAnzahl);
			 return true;
		 }
		 return false;
	 }
	 
	 //Erstellt einen Graphen mit X Knoten und y Kanten. Die theotetisch beliebig groß sein können.

	 public void createBigGraph()
	 {
//		 if( _knotenAnzahl >= 0)
//			 return;
		 _graph =  new UndirectedAttributedWeightedGraph<Vertex, MyWeightedEdge>(MyWeightedEdge.class);
		 _vertexList = new LinkedList<Vertex>();
		 _vertexMap = new HashMap<Vertex, Integer>();
		 
		createVertex();
		 
		setRandomValues(_knotenAnzahl);
		 
		calculateHeuristic();
		
		addVertices();
		
		addEdges(_kantenAnzahl);

	 }
	 
	 public void setRandomValues(int KnotenAnzahl)
	 {
		 Random randomGenerator = new Random();
		 
		 int maxKnotenValAnzahl = KnotenAnzahl * 15;
		 int vertexValue;
		 
		 for(Vertex ver : _vertexList)
		 {
			vertexValue = randomGenerator.nextInt(maxKnotenValAnzahl);
			
			_vertexMap.put(ver, vertexValue);
		 }
		 
		 
		 //Vllt. hinzufügen von nur verschieden Attr 
		
		
	 }
	 
	 public void createVertex()
	 {
		 Random randomGenerator = new Random();
		 String vertexName; 
		 int  x;
		 int  y;
		 Vertex vertex;
		 for(int i = 1; i <=  _knotenAnzahl; i++)
		 {
			 x = randomGenerator.nextInt((1200)+1); 
			 y = randomGenerator.nextInt((800)+1);
			 vertexName = "V" + i;
			 vertex = Vertex.createVertex(vertexName, 0, x, y);
			 
			 
			 _vertexList.add(vertex);
		 }
		 
	 }
	 
	 
	 public void calculateHeuristic()
	 {
		Vertex target = _vertexList.get(_vertexList.size() -1);
		target.setAttr(0);
		
		int targetVal = _vertexMap.get(target);
		
		int SourceVal;
		int attribute;
		
		
		for(Vertex ver : _vertexList)
		{
			SourceVal = _vertexMap.get(ver);
			attribute = Math.abs(targetVal - SourceVal);
			
			if(!ver.equals(target))
			{
				ver.setAttr(attribute);
			}
		}
		
	 }
	 
	 public void addEdges(int edges)
	 {
		Random randomGenerator = new Random();
		int random;
		MyWeightedEdge edge;
		int min;
		int max;
		int weight;
		for(int i = 0; i < edges; i++)
		{
			random = randomGenerator.nextInt(_vertexList.size());
			Vertex v1 = _vertexList.get(random);
			random = randomGenerator.nextInt(_vertexList.size());
			Vertex v2 = _vertexList.get(random);
			
			min = (int) Math.abs(v1.getAttr() -v2.getAttr());
			max = (min + 1) * 15;
			
			weight = randomGenerator.nextInt(max - min) + min;
			
			 edge = (MyWeightedEdge) _graph.addEdge(v1,v2);
			 
    	     ((UndirectedAttributedWeightedGraph<Vertex, MyWeightedEdge>)_graph).setEdgeWeight(edge, weight );
			
		}
		
	 }
	 
	 public void addVertices()
	 {
		 for(Vertex ver : _vertexList)
		 {
			 _graph.addVertex(ver);
		 }
	 }
	 
	 
	 
	/**
	 * durch diese Methode wird die UI angezeigt.
	 */
    public void showUI()
    {
        _ui.getDialog().setVisible(true);
        
//	        registiereListener();
    }

    /**
     * gibt den Big Graphen zurück
     * @return _graph 
     */
	 public UndirectedAttributedWeightedGraph<Vertex, MyWeightedEdge> getBigGraph()
	 {
		 return _graph;
	 }

}
