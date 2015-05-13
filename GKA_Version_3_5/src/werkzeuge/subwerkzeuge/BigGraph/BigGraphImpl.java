package werkzeuge.subwerkzeuge.BigGraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;


import materialien.MyWeightedEdge;
import materialien.Vertex;
import materialien.Graph.UndirectedAttributedWeightedGraph;


public class BigGraphImpl {

	BigGraphUI _ui;
	int _kantenAnzahl;
	int _knotenAnzahl;
	UndirectedAttributedWeightedGraph<Vertex, MyWeightedEdge> _graph;
	
	
	
	public BigGraphImpl()
	{
		_ui = new BigGraphUI();
		showUI();
		registiereListener();

	}
	
	
	/**
	 * registiert am OK Button einen Listener, der die Eingaben prüft.
	 */
	 public void registiereListener()
	 {
		 _knotenAnzahl = 100;
		 _kantenAnzahl = 6000;
//		 showUI();
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
		 
		 
		 if(!knotenAnzahl.matches("(?[0-9]+)") || !kantenAnzahl.matches("(?[0-9]+)"))
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
		 
		_graph = new UndirectedAttributedWeightedGraph<Vertex, MyWeightedEdge>(MyWeightedEdge.class);
		int x;
		int y;
		int attr;
		Vertex vertex;
		String vertexName;
		List<Vertex> vertexList = new LinkedList<Vertex>();
		
		//For Schleife um die Knoten zu erstellen. Mit random Attributen.
		//Außerdem werden die Knoten(Vertex) in eine Liste gespeichtert, da ein Set-Zugriff aufwendiger ist.
		
		Random randomGenerator = new Random();
		for(int i = 0; i < _knotenAnzahl ; i++)
		{
			
			x = randomGenerator.nextInt((1200)+1);
		    y  = randomGenerator.nextInt((1000)+1);
		    attr = randomGenerator.nextInt((400));
		    vertexName = "V" + i;
		    //Hier wird der Knoten erstellt.
			vertex= Vertex.createVertex(vertexName, attr, x, y);
			vertexList.add(vertex);
			_graph.addVertex(vertex); 
		}

		int weight;
		Vertex source;
		Vertex target;
		MyWeightedEdge edge;
			
		//for Schleife um die Kanten zu erstellen. Dazu wercen aus der Liste zwei random Knoten ausgewählt und verbunden.
		//Die Kanten bekommen auch durch den RandomGenerator zufällige Attribute-
		for(int j = 0; j < _kantenAnzahl; j++)
		{
			weight = randomGenerator.nextInt(6000);
			x = randomGenerator.nextInt(_knotenAnzahl);
			y = randomGenerator.nextInt(_knotenAnzahl);
			source = (Vertex) vertexList.get(x);
			target = (Vertex) vertexList.get(y);
			//Hier wird die Kante erstellt.
			 edge = (MyWeightedEdge) _graph.addEdge(source,target);
      	     ((UndirectedAttributedWeightedGraph<Vertex, MyWeightedEdge>)_graph).setEdgeWeight(edge, weight);
			
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
