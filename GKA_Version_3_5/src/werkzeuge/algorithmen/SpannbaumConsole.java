package werkzeuge.algorithmen;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import materialien.Vertex;

public class SpannbaumConsole
{
    private JFrame _frame;
    private JLabel _graphAccesses;
    private JLabel _benoetigteKanten;
    private JLabel _wegLaenge;
    private JLabel _zeit;
 
    public SpannbaumConsole(String graphAccesses,String kantenGewicht, String algorithm, String benoetigteKanten, String zeit)
    {
        _frame = new JFrame("AlgorithmConsole - " + algorithm);        
        JPanel panel = new JPanel(new GridLayout(7, 1));

        _graphAccesses = new JLabel("Zugriffe: "+graphAccesses);
        _benoetigteKanten = new JLabel("Anzahl benötigter Kanten: " + benoetigteKanten);
        _wegLaenge = new JLabel("Summe aller Kantengewichte: "+kantenGewicht);
        _zeit = new JLabel("Gebrauchte Zeit: " + zeit);
        
        _frame.add(panel);
        panel.add(_graphAccesses);
        panel.add(_benoetigteKanten);
        panel.add(_wegLaenge);
        panel.add(_zeit);
        
        _frame.pack();
        _frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        _frame.setLocationRelativeTo(null);
    }
    
    public void start()
    {
        _frame.setVisible(true);
    }
    
}
