package werkzeuge.algorithmen.bfs;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

class BreadthFirstSearchUserInputUI
{
    final String algorithmName = "Breadth First Search Algorithm";
    private JDialog _dialog;
    private JButton _okButton;
    private JTextField _vertexSource;
    private JTextField _vertexTarget;
    
    public BreadthFirstSearchUserInputUI()
    {
        _dialog = new JDialog();
        _dialog.setSize(300, 200);
        _dialog.setLocationRelativeTo(null);
        
        JPanel northPanel = new JPanel();
        JLabel algorithmNameLabel = new JLabel(algorithmName);
        northPanel.add(algorithmNameLabel);
        _dialog.add(northPanel,BorderLayout.NORTH);
        
        JPanel southPanel = new JPanel();
        _okButton = new JButton("OK");
        southPanel.add(_okButton);
        _dialog.add(southPanel,BorderLayout.SOUTH);
        
        JPanel centerPanel = new JPanel(new GridLayout(2,2));
        JLabel vertexSourceLabel = new JLabel("Startvertex:");
        JLabel vertexTargetLabel = new JLabel("Zielvertex:");
        _vertexSource = new JTextField(10);
        _vertexTarget = new JTextField(10);
        centerPanel .add(vertexSourceLabel);
        centerPanel .add(_vertexSource);
        centerPanel .add(vertexTargetLabel);
        centerPanel .add(_vertexTarget);
        _dialog.add(centerPanel,BorderLayout.CENTER);
        
        _dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        _dialog.setResizable(false);
        _dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
    }

    public JDialog getDialog()
    {
        return _dialog;
    }

    public JButton getOkButton()
    {
        return _okButton;
    }

    public JTextField getVertexSource()
    {
        return _vertexSource;
    }

    public JTextField getVertexTarget()
    {
        return _vertexTarget;
    }
    
    
    
}