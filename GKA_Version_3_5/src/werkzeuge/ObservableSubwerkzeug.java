package werkzeuge;

import java.util.HashSet;
import java.util.Set;

public abstract class ObservableSubwerkzeug
{
    Set<SubwerkzeugObserver> _observer;
    
    public ObservableSubwerkzeug()
    {
        _observer = new HashSet<SubwerkzeugObserver>();
    }
    
    public void registriereObserver(SubwerkzeugObserver observer)
    {
        _observer.add(observer);
    }
    
    public void loescheObserver(SubwerkzeugObserver observer)
    {
        _observer.remove(observer);
    }
    
    public void informiereUeberAenderung()
    {
        for(SubwerkzeugObserver o : _observer)
        {
            o.reagiereAufAenderung();
        }
    }
    
    public void informiereUeberAenderung(Object o)
    {
        for(SubwerkzeugObserver ob : _observer)
        {
            ob.reagiereAufAenderung(o);
        }
    }
}
