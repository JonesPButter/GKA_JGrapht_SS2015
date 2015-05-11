package fachwerte;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JRadioButtonMenuItem;

public class RadioButtonGroup
{

    Set<JRadioButtonMenuItem> _buttons;
    
    public RadioButtonGroup()
    {
        _buttons = new HashSet<JRadioButtonMenuItem>();
    }
    
    public void add(JRadioButtonMenuItem button)
    {
        _buttons.add(button);
    }
    
    public void delete(JRadioButtonMenuItem button)
    {
        _buttons.remove(button);
    }
    
    public void buttonClicked(JRadioButtonMenuItem button)
    {
        for(JRadioButtonMenuItem b : _buttons)
        {
            if(!b.equals(button))
            {
                b.setSelected(false);
            }
        }
    }
    
    public JRadioButtonMenuItem getClickedButton()
    {
        for(JRadioButtonMenuItem b : _buttons)
        {
            if(b.isSelected())
            {
                return b;
            }
        }
        return null;
    }
}
