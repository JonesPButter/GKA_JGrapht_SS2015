package materialien;

public class Vertex implements Comparable<Vertex>
{
    String _name;
    double _attr;
    double _minEdgeWeight;
    int _xPos, _yPos;
    double _entfernungVomStartVertex;
    boolean _isVisited;
    boolean _isTarget;
    boolean _isPartOfShortestWay;
    
    private Vertex(String name, double attr, int x, int y)
    {
        _name = name;
        _attr = attr;
        _xPos = x;
        _yPos = y;
        _minEdgeWeight = -1;
        _isVisited = false;
        _isTarget = false;
        _isPartOfShortestWay = false;
        _entfernungVomStartVertex = -1;
    }
    
    public static Vertex createVertex(String name, double attr,int x, int y)
    {
        return new Vertex(name,attr,x,y);
    }
    
    public boolean equals(Object o)
    {
        if(o instanceof Vertex)
        {
            if(((Vertex)o).getName().equals(_name))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Prüfen, ob der Vertex schon besucht wurde
     * @return True, wenn er besucht wurde
     */
    public boolean isVisited()
    {
        return _isVisited;
    }
    
    /**
     * prüft ob der Vertex als Ziel markiert wurde
     * @return
     */
    public boolean isTarget()
    {
        return _isTarget;
    }
    
    /**
     * Vertex als Ziel aktivieren.
     */
    public void setTarget()
    {
        _isTarget = true;
    }
    
    /**
     * Den Vertex als Ziel deaktivieren.
     */
    public void untarget()
    {
        _isTarget = false;
    }
    
    /**
     * Setzt den neuen Wert für die minimale Kante, die an dem Vertexobjekt hängt
     * @param min
     */
    public void setMinEdgeWeight(double min)
    {
        _minEdgeWeight = min;
    }
    
    public void setNewEdgeWeight(double min)
    {
    	if(_minEdgeWeight > min || _minEdgeWeight == -1)
    	{
    		_minEdgeWeight = min;
    	}
    }
    
    /**
     * Den Vertex als besucht makieren.
     */
    public void visit()
    {
        _isVisited = true;
    }
    
    /**
     * Den vertex als besucht demakieren.
     */
    public void unvisit()
    {
        _isVisited = false;
    }
    
    /**
     * Liefert den namen des vertex
     * @return
     */
    public String getName()
    {
        return _name;
    }
    
    /**
     * Liefert das Attribut des Vertex
     * @return
     */
    public double getAttr()
    {
        return _attr;
    }
    
    /**
     * Setzt den Namen am vertex
     * @param name
     */
    public void setName(String name){
        _name = name;
    }
    
    /**
     * Setzt das Attribut am Vertex
     * @param attr
     */
    public void setAttr(int attr)
    {
        _attr = attr;
    }
    
    /**
     * Setzt die x Koordinate des Vertex
     * @param x
     */
    public void setX(int x)
    {
        _xPos = x;
    }
    
    /**
     * Setzt die Y Koordinate des Vertex
     * @param y
     */
    public void setY(int y)
    {
        _yPos = y;
    }
    
    /**
     * Liefert die X Koordinate des Vertex
     * @return
     */
    public int getX()
    {
        return _xPos;
    }
    
    /**
     * Liefert die Y Koordinate des Vertex
     * @return
     */
    public int getY()
    {
        return _yPos;
    }
    
    /**
     * Liefert den Wert der Kante mit dem minimalen Kantengewicht, die an diesem Vertexobjekt hängt 
     * @return -1, wenn es keine Kante gibt
     */
    public double getMinEdgeWeight()
    {
        return _minEdgeWeight;
    }
    
    @Override
    public String toString()
    {
        return _name + "(" + _attr + ")";
    }
    
    /**
     * Liefert die Entfernung vom Startvertex
     * (Für die kürzeste Weg berechnung)
     * @return
     */
    public double getEntfernungVomStartVertex()
    {
        return _entfernungVomStartVertex;
    }
    
    /**
     * Setzt die Entfernung, die der Vertex vom Startvertex entfernt ist.
     * (Für die kürzeste Weg berechnung)
     * @param entfernung
     */
    public void setEntfernungVomStartVertex(double entfernung)
    {
        _entfernungVomStartVertex = entfernung;
    }

    public boolean isPartOfShortestWay()
    {
        return _isPartOfShortestWay;
    }
    
    public void setPartOfShortestWay()
    {
        _isPartOfShortestWay = true;
    }
    
    public void isNotPartOfShortestWay()
    {
        _isPartOfShortestWay = false;
    }
    

    
    public void reset()
    {
        unvisit();
        untarget();
        setEntfernungVomStartVertex(0);
        isNotPartOfShortestWay();
    }

    @Override
    public int compareTo(Vertex v)
    {
        Double thisMin = (Double)this._minEdgeWeight;
        return thisMin.compareTo((Double)v.getMinEdgeWeight());
    }
}
