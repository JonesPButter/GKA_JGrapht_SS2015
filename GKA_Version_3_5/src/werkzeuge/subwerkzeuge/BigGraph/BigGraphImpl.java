package werkzeuge.subwerkzeuge.BigGraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JOptionPane;

import org.jgraph.graph.Edge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;

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
		registiereListener();

	}
	
	
	 
	 public void registiereListener()
	 {
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
	 private boolean eingabenKorrekt(String knotenAnzahl, String kantenAnzahl)
	 {
		 
		 
		 if(!knotenAnzahl.matches("(?[0-9]+)") || !kantenAnzahl.matches("(?[0-9]+)"))
		 {
			 JOptionPane.showMessageDialog(null , "Fehlerhafte Eingabe. "
                     + "\n Bitte geben Sie eine gültige Zahl ein."
                     + " Beispiele: 5.67 || -0.5 || 42");
		}
		 else if(Integer.parseInt(kantenAnzahl) > 0 && Integer.parseInt(knotenAnzahl) > 0)
		 {
			 _knotenAnzahl = Integer.parseInt(knotenAnzahl);
			 _kantenAnzahl = Integer.parseInt(kantenAnzahl);
			 return true;
		 }
		 return false;
	 }
	 
	 public void createBigGraph()
	 {
		 
		 int x;
			int y;
			int attr;
			List<Vertex> vertexList = new LinkedList<Vertex>();
			
			Random positionGenerator = new Random();
			for(int i = 0; i < _knotenAnzahl; i++)
			{
			    x = positionGenerator.nextInt((800-200)+1)+200;
			    y  = positionGenerator.nextInt((300-50)+1)+50;
			    attr = positionGenerator.nextInt((300+100))-100;
			    String vertexName = "V" + i;
				Vertex vertex= Vertex.createVertex(vertexName, attr, x, y);
				_graph.addVertex(vertex);
				vertexList.add(vertex);
			}

			int weight;
			Vertex source;
			Vertex target;

			
			
			for(int j = 0; j < _kantenAnzahl; j++)
			{
				weight = positionGenerator.nextInt(6000);
				x = positionGenerator.nextInt(_knotenAnzahl);
				y = positionGenerator.nextInt(_knotenAnzahl);
				source = (Vertex) vertexList.get(x);
				target = (Vertex) vertexList.get(y);
				
				 MyWeightedEdge edge = (MyWeightedEdge) _graph.addEdge(source,target);
	      	     ((UndirectedAttributedWeightedGraph<Vertex, MyWeightedEdge>)_graph).setEdgeWeight(edge, weight);
				
			}
	 }
	 
	 public UndirectedAttributedWeightedGraph<Vertex, MyWeightedEdge> getBigGraph()
	 {
		 return _graph;
	 }
}
