package werkzeuge.subwerkzeuge.eulerCreator;



	import java.awt.BorderLayout;
	import java.awt.Dialog;
	import java.awt.GridLayout;

	import javax.swing.JButton;
	import javax.swing.JDialog;
	import javax.swing.JLabel;
	import javax.swing.JPanel;
	import javax.swing.JTextField;
	import javax.swing.WindowConstants;

	public class EulerCreatorUI
	{
	    final String algorithmName = "Euler Graph";
	    private JDialog _dialog;
	    private JButton _okButton;
	    private JTextField _kantenanzahl;
	    private JTextField _knotenanzahl;
	    
	    public EulerCreatorUI()
	    {
	    	//Dialog Einstellungen
	        _dialog = new JDialog();
	        _dialog.setSize(300, 200);
	        _dialog.setLocationRelativeTo(null);
	        
	        JPanel northPanel = new JPanel();
	        JLabel bigGraphLabel = new JLabel(algorithmName);
	        northPanel.add(bigGraphLabel);
	        _dialog.add(northPanel,BorderLayout.NORTH);
	        
	        JPanel southPanel = new JPanel();
	        _okButton = new JButton("OK");
	        southPanel.add(_okButton);
	        _dialog.add(southPanel,BorderLayout.SOUTH);
	        
	        JPanel centerPanel = new JPanel(new GridLayout(2,2));
	        JLabel knotenLabel = new JLabel("Knotenanzahl:");
	        _kantenanzahl = new JTextField(10);
	        _knotenanzahl = new JTextField(10);
	        centerPanel .add(knotenLabel);
	        centerPanel .add(_knotenanzahl);

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


	    public JTextField getKnotenanzahl()
	    {
	        return _knotenanzahl;
	    }
	    
	    
	    
	

}
