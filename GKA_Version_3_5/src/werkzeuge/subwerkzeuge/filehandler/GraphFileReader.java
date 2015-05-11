package werkzeuge.subwerkzeuge.filehandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import exceptions.FalschesFileformatException;

public class GraphFileReader
{   
    File _choosenFile;
    final JFileChooser _fileChooser;
    
    private GraphFileReader()
    {
        _choosenFile = null;
        _fileChooser = new JFileChooser();
    }
    
    /**
     * Liefert den GraphFileReader
     * @return
     */
    public static GraphFileReader create()
    {
        return new GraphFileReader();
    }
    
    /**
     * Lädt einen neuen File
     * @throws FalschesFileformatException Nur die Endung ".graph" ist erlaubt!
     */
    public void loadFile() 
    {
        boolean bool = false;
        while(!bool)
        {
            int returnVal = _fileChooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = _fileChooser.getSelectedFile();
                if(selectedFile.toString().endsWith(".graph"))
                {
                    _choosenFile = _fileChooser.getSelectedFile(); 
                    bool = true;
                }
                else JOptionPane.showMessageDialog(null, 
                        "Sie dürfen nur Files mit der Endung '.graph' öffnen. Bitte prüfen Sie Ihre Eingabe.");
                
            }
            else if(returnVal == JFileChooser.CANCEL_OPTION)
            {
                bool = true;
            }
        }
    }
    
    /**
     * Liefert den vom User ausgewählten File
     * ACHTUNG! Kann null sein!
     * @return
     */
    public File getChoosenFile()
    {
        return _choosenFile;
    }

    /**
     * 
     * @return List<String> mit den Werten wie der Graph aufgebaut ist.
     * @throws IOException
     */
    public List<String> getFileData() throws IOException
    {
        List<String> dataList = new ArrayList<String>();
        Path java_path = Paths.get(_choosenFile.getPath());
        dataList = Files.readAllLines(java_path);
        return dataList;
    }
}
