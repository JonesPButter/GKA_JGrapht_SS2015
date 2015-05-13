package werkzeuge.algorithmen;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import materialien.Vertex;


public class AlgorithmConsole
{
    private JFrame _frame;
    private JLabel _rootVertex;
    private JLabel _targetVertex;
    private JLabel _shortestW;
    private JLabel _graphAccesses;
    private JLabel _benoetigteKanten;
    private JLabel _wegLaenge;
 
    public AlgorithmConsole(Vertex rootVertex,
            Vertex targetVertex, String shortestW, String graphAccesses,
            String benoetigteKanten, String wegLaenge)
    {
        _frame = new JFrame("AlgorithmConsole");        
        JPanel panel = new JPanel(new GridLayout(7, 1));

        _rootVertex = new JLabel("Start: " + rootVertex.toString());
        _targetVertex = new JLabel("Ziel: "+ targetVertex.toString());
        _shortestW = new JLabel("Der kürzeste Weg: " + shortestW);
        _graphAccesses = new JLabel("Zugriffe: "+graphAccesses);
        _benoetigteKanten = new JLabel("Anzahl benötigter Kanten: " + benoetigteKanten);
        _wegLaenge = new JLabel("Weglänge: "+wegLaenge);
        
        _frame.add(panel);
        panel.add(_rootVertex);
        panel.add(_targetVertex);
        panel.add(_shortestW);
        panel.add(_graphAccesses);
        panel.add(_benoetigteKanten);
        panel.add(_wegLaenge);
        
        _frame.pack();
        _frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        _frame.setLocationRelativeTo(null);
    }
    
    public void start()
    {
        _frame.setVisible(true);
    }
    
}
