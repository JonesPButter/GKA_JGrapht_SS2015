package werkzeuge.algorithmen;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import materialien.MyWeightedEdge;

public class EulerkreisConsole
{
    private JFrame _frame;
    private JLabel _graphAccesses;
    private JList<Object> _eulerkreis;
    private JLabel _zeit;
 
    public EulerkreisConsole(String graphAccesses,String algorithm,String zeit, List<MyWeightedEdge> eulerkreis)
    {
        _frame = new JFrame("AlgorithmConsole - " + algorithm);        
        JPanel panel = new JPanel(new GridLayout(7, 1));

        _graphAccesses = new JLabel("Zugriffe: "+graphAccesses);
        _eulerkreis = new JList<Object>(eulerkreis.toArray());
        _zeit = new JLabel("Gebrauchte Zeit: " + zeit);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(_eulerkreis);
        
        _frame.add(panel);
        panel.add(new JLabel("Eulerkreis:"));
        panel.add(scrollPane);
        panel.add(_graphAccesses);
        panel.add(_zeit);
        
        _frame.setSize(400,400);
        _frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        _frame.setLocationRelativeTo(null);
    }
    
    public void start()
    {
        _frame.setVisible(true);
    }
    
}
