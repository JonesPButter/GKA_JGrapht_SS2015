package werkzeuge.subwerkzeuge.filehandler;

//import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.Writer;
import java.util.List;
import java.util.logging.FileHandler;

import javax.swing.JFileChooser;
//import javax.swing.filechooser.FileFilter;

public class GraphFileWriter
{
	
	final JFileChooser _fileChooser;
	FileHandler _fileHandler;
	
    private GraphFileWriter()
    {
        _fileChooser = new JFileChooser();
    }
    
    public static GraphFileWriter create()
    {
        return new GraphFileWriter();
    }
    
    
    public void saveFile(List<String> graphArray)
    {
    	int userSelection = _fileChooser.showSaveDialog(null);
    	String graphInString = "";
    	
    	for(int i = 0; i<graphArray.size(); i++)
    	{
    		graphInString = graphInString + graphArray.get(i) + "\n";
    	}
    	
    
    	if(userSelection == JFileChooser.APPROVE_OPTION)
    	{
    		try {
    			
    				if(_fileChooser.getSelectedFile().toString().endsWith(".graph"))
    				{
    					FileWriter fwriter = new FileWriter(_fileChooser.getSelectedFile());
        				fwriter.write(graphInString);
        				fwriter.close();
    				}
    				else
    				{
    					FileWriter fwriter = new FileWriter(_fileChooser.getSelectedFile() + ".graph");
        				fwriter.write(graphInString);
        				fwriter.close();
    				}

    				_fileChooser.setAcceptAllFileFilterUsed(false);

    			
			} catch (IOException e) {
//				e.printStackTrace();
//				System.out.println("Fehlschlag im FileWriter");
			}
    		
    	}
    	
    }
    
    public void formatInArrayList()
    {
    	
    }
}
