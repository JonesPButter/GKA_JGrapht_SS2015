package werkzeuge.subwerkzeuge.eulerCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;

import materialien.MyWeightedEdge;
import materialien.Vertex;
import materialien.Graph.UndirectedAttributedGraph;
import werkzeuge.ObservableSubwerkzeug;

public class EulerCreator extends ObservableSubwerkzeug {
	EulerCreatorUI _ui;
	int _knotenAnzahl;
	List<Vertex> _vertexList;
	Map<Vertex, Integer> _vertexMap;
	
	UndirectedAttributedGraph<Vertex, MyWeightedEdge> _graph;
	
	public EulerCreator() {
		
		_ui = new EulerCreatorUI();
		_knotenAnzahl = 0;
		
		
		_graph = new UndirectedAttributedGraph<>(MyWeightedEdge.class);
		
		
	}
	
	public void registiereListener() {
		
		 _ui.getOkButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String knontenAnzahl = _ui.getKnotenanzahl().getText();
				
//				if(eingabeKorrekt(knontenAnzahl)) {
//					
//				}
			}
		});
	}
	
	
	public void createVertex(int knotenAnzahl) {
		
		
	}
	
    public void showUI()
    {
        _ui.getDialog().setVisible(true);
        
//	        registiereListener();
    }

	public Graph<Vertex, MyWeightedEdge> getEulerGraph() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
