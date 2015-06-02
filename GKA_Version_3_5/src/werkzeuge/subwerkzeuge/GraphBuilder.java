	package werkzeuge.subwerkzeuge;
	
	import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import materialien.MyWeightedEdge;
import materialien.Vertex;
import materialien.Graph.DirectedAttributedGraph;
import materialien.Graph.DirectedAttributedWeightedGraph;
import materialien.Graph.UndirectedAttributedGraph;
import materialien.Graph.UndirectedAttributedWeightedGraph;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.WeightedPseudograph;
	
	public class GraphBuilder
	{
	    int anzahlErstellteGraphen; 
	    Map<Object, Object> _cellAttr;
	    public static int graphWIDTH = 60,graphHEIGHT = 30;
	    
	    public GraphBuilder()
	    {
	        anzahlErstellteGraphen = 0;
	        _cellAttr = new HashMap<>();
	    }
	    
	    
	
	    
	    
	   /**
	    * Wandelt die graphData in einen funktionsfähigen Graphen um.
	    * @param graphData
	 * @param graph 
	    * @return
	    */
	    public Graph<Vertex,MyWeightedEdge> createGraph(
	            List<String> graphData, Graph<Vertex,MyWeightedEdge> graph)
	    {
	        // Je nach Attribute in den ersten Listenelementen, wird jetzt ein neuer ListenableGraph erstellt
	        // entweder directed oder undirected. Dies entnehmen wir der List<String>.
	        // Zu testzwecken hier erstmal unten ein einfacher Graph, der nichtmal die Daten aus dem File liest.
	        
	        anzahlErstellteGraphen++;
	        
	        graphData.removeAll(Arrays.asList("", null));
	        graphData.removeAll(Collections.singleton(null));
	        graphData.removeAll(Collections.singleton(""));
	        
	        List<String> GraphInfoArray = graphData;
	        

	        //schaut ob die .GRAPH-Datei bestimmte Typen hat
	        //zuerst ob überhaupt ein Typ vorhanden ist und je nach dem weiter.
	      
	        	
	        
	        
	        
	         if( !GraphInfoArray.get(0).contains("#") &&
	    		 !GraphInfoArray.get(0).contains("#directed") && 
	    		 !GraphInfoArray.get(0).contains("#weighted") &&
	    		 !GraphInfoArray.get(0).contains("#attributed")
	    		 ) 
	        {    
//	             System.out.println("undirectedGraph");
	            graph = undirectedGraph(GraphInfoArray);
	        }
	         else if( !GraphInfoArray.get(0).contains("directed") && 
	        		 GraphInfoArray.get(0).contains("weighted") &&
	        		 !GraphInfoArray.get(0).contains("attributed")) 
	         {
//	             System.out.println("undirectedWeightedGraph");
	             GraphInfoArray.remove(0);
	             graph = undirectedWeightedGraph(GraphInfoArray);
	         }
	         else if(GraphInfoArray.get(0).contains("directed") && 
	        		 !GraphInfoArray.get(0).contains("weighted") &&
	        		 !GraphInfoArray.get(0).contains("attributed") )
	         {
//	             System.out.println("directedGraph");
	             GraphInfoArray.remove(0);
	             graph = directedGraph(GraphInfoArray); 
	             
	         }
	         else if(!GraphInfoArray.get(0).contains("directed") && 
	        		 !GraphInfoArray.get(0).contains("weighted") &&
	        		  GraphInfoArray.get(0).contains("attributed")) 
	         {
//	             System.out.println("undirectedAttributeGraph");
	             GraphInfoArray.remove(0);
	             graph = undirectedAttributeGraph(GraphInfoArray);
	             
	         }
	        else if(  GraphInfoArray.get(0).contains("directed") && 
	        		  GraphInfoArray.get(0).contains("weighted") &&
	        		 !GraphInfoArray.get(0).contains("attributed")) 
	        {
//	            System.out.println("directedWeightedGraph");
	            GraphInfoArray.remove(0);
	            graph = directedWeightedGraph(GraphInfoArray); 
	        } 
	        else if( !GraphInfoArray.get(0).contains("directed") && 
	        		  GraphInfoArray.get(0).contains("weighted") &&
	        		  GraphInfoArray.get(0).contains("attributed")) 
	        {
	            System.out.println("undirectedAttriuteWeightedGraph");
	            GraphInfoArray.remove(0);
	            graph = undirectedAttriuteWeightedGraph(GraphInfoArray); 
	            
	        }
	        else if(  GraphInfoArray.get(0).contains("directed") && 
	        		 !GraphInfoArray.get(0).contains("weighted") &&
	        		  GraphInfoArray.get(0).contains("attributed")) 
	        {
//	        	System.out.println(" directedAttributeGraph");
	        	GraphInfoArray.remove(0);
	            graph = directedAttributeGraph(GraphInfoArray); 
	        }
	        else if( GraphInfoArray.get(0).contains("directed") && 
	        		 GraphInfoArray.get(0).contains("weighted") &&
	        		 GraphInfoArray.get(0).contains("attributed")) 
	        {
//	        	System.out.println("directedAttriuteWeightedGraph");
	        	GraphInfoArray.remove(0);
	        	graph = directedAttriuteWeightedGraph(GraphInfoArray); 
	            
	        }
	  
	
	        return graph;
	    }
	
	
	    /**
	     * schreibt einen Dirketen PSeidographen aus einen List von Strings
	     * @param infoGraph
	     * @return graphs
	     */
	    public static Graph<Vertex, MyWeightedEdge> directedGraph(List<String> infoGraph)
	    {

	    	DirectedGraph<Vertex,MyWeightedEdge> returngraph = new DirectedPseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class );
	       
	    	Random positionGenerator = new Random();
	        int x,y;
	        
	        List<String> alleEcken = new ArrayList<String>();
	        List<Vertex> alleVertex = new ArrayList<Vertex>();
	        
	        for(int i = 0; i < infoGraph.size(); i++)
	        {
	
	     	   String targetVertex = "";
	     	   String sourceVertex = "";
	     	   
	     	   if(infoGraph.get(i).length() == 0)
	      	   {
	      		   infoGraph.remove(i);
	      	   }
	     	   
	     	   boolean isSourceVertexFinished = false;
	     	   boolean isTargetVertexFinished = false;
	     	   
	     	   for(int j = 0; j < infoGraph.get(i).length(); j++)
	     	   {
	     		 
	         	   
	                if(infoGraph.get(i).charAt(j) != ','  &&
	             		   infoGraph.get(i).charAt(j) != ' '  &&
	             		   !isSourceVertexFinished)
	                {
	                    sourceVertex = sourceVertex + infoGraph.get(i).charAt(j);                 
	                }
	                else if(infoGraph.get(i).charAt(j) == ','  &&
	             		   !isSourceVertexFinished)
	                {
	             	   isSourceVertexFinished = true;
	                }
	                else if(infoGraph.get(i).charAt(j) != ',' &&
	             		   infoGraph.get(i).charAt(j) != ' '  &&
	             		   !isTargetVertexFinished)
	                {
	             	   targetVertex = targetVertex +  infoGraph.get(i).charAt(j);            	  
	                }
	
	     	   }

	     	   x = positionGenerator.nextInt((800-200)+1)+200;
	           y = positionGenerator.nextInt((300-50)+1)+50;
	               Vertex vSource = Vertex.createVertex(sourceVertex, 0, x, y);
	           x = positionGenerator.nextInt((800-200)+1)+200;
	           y = positionGenerator.nextInt((300-50)+1)+50;
	               Vertex vTarget = Vertex.createVertex(targetVertex, 0, x, y);
	               
	               if(!alleEcken.contains(targetVertex) &&
	                       targetVertex.length() != 0)
	               {            	
	                   returngraph.addVertex(vTarget);
	                   alleVertex.add(vTarget);
	                   alleEcken.add(targetVertex);
	               
	               }
	               else
	               {
	            	   for(int k = 0; k<alleEcken.size(); k++)
	            	   {
	            		   if(alleEcken.get(k).equals(targetVertex))
	            		   {
	            			   vTarget = alleVertex.get(k);
	            		   }
	            	   }
	               }
	              
	
	               if(!alleEcken.contains(sourceVertex))
	               {            	
	                   returngraph.addVertex(vSource);
	                   alleVertex.add(vSource);
	                   alleEcken.add(sourceVertex);
	               }
	               else
	               {
	            	   for(int k = 0; k<alleEcken.size(); k++)
	            	   {
	            		   if(alleEcken.get(k).equals(sourceVertex))
	            		   {
	            			   vSource = alleVertex.get(k);
	            		   }
	            	   }
	               }
	
	               if(!returngraph.containsVertex(vSource))
	               {            	
	                   returngraph.addVertex(vSource);
	                   alleVertex.add(vSource);
	               }
	               
	               if(vSource.equals(vTarget)){            	   
	            	   returngraph.addEdge(vSource,vSource);
	               }
	               else if(targetVertex.length() == 0)
	               {
	                   
	               }
	               else 
	               {
	            	   returngraph.addEdge(vSource, vTarget);            	   
	               }
	               
	           }
	
	       return returngraph;
	    }
	    
	    
	    /**
	     * erstellen einen gewichteten dirketen PSeudographen und gibt diesen dann zurück
	     * Das macht er aus einer Liste von Strings
	     * @param infoGraph
	     * @return graph
	     */
	    public static Graph<Vertex, MyWeightedEdge> directedWeightedGraph(List<String> infoGraph)
	    {
	   
	    	DirectedWeightedPseudograph<Vertex,MyWeightedEdge> returngraph = new DirectedWeightedPseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class );
	        Random positionGenerator = new Random();
	        int x,y;
	        
	        List<String> alleEcken = new ArrayList<String>();
	        List<Vertex> alleVertex = new ArrayList<Vertex>();
	        
	        for(int i = 0; i < infoGraph.size(); i++)
	        {
	
	     	   String targetEcken = "";
	     	   String sourceEcken = "";
	     	   double weighing = 0;
	     	   
	     	   if(infoGraph.get(i).length() == 0)
	      	   {
	      		   infoGraph.remove(i);
	      	   }
	     	   
	     	   boolean isvar1finisch = true;
	     	   boolean isvar2finisch = true;
	     	   
	     	   for(int j = 0; j < infoGraph.get(i).length(); j++)
	     	   {
	     		 
	         	   
	                if(infoGraph.get(i).charAt(j) != ','  &&
	             		   infoGraph.get(i).charAt(j) != ' '  &&
	             			 infoGraph.get(i).charAt(j) != ':'  &&
	             		   isvar1finisch)
	                {
	                    sourceEcken = sourceEcken + infoGraph.get(i).charAt(j);                 
	                }
	                else if(infoGraph.get(i).charAt(j) == ','  &&
	                		 infoGraph.get(i).charAt(j) != ' '  &&
	                		 infoGraph.get(i).charAt(j) != ':'  &&
	             		   isvar1finisch)
	                {
	             	   isvar1finisch = false;
	                }
	                else if(infoGraph.get(i).charAt(j) != ',' &&
	             		   infoGraph.get(i).charAt(j) != ' '  &&
	             				  infoGraph.get(i).charAt(j) != ':'  &&
	             		   isvar2finisch)
	                {
	             	   targetEcken = targetEcken +  infoGraph.get(i).charAt(j);    
	             	   isvar2finisch = false;
	                }else if(infoGraph.get(i).charAt(j) != ','  &&
	              		   infoGraph.get(i).charAt(j) != ' '  &&
	              			 infoGraph.get(i).charAt(j) != ':')
	                {
	                	if(infoGraph.get(i).charAt(j) == '-')
	                	{
	                		weighing = (-1) *  Double.parseDouble((infoGraph.get(i).charAt(j + 1) + ""));
	                		j++;
	                	}
	                	else
	                	{
	                		weighing = weighing * 10 +  Double.parseDouble((infoGraph.get(i).charAt(j) + ""));                		
	                	}
	
	                }
	
	     	   }
	           x = positionGenerator.nextInt((800-200)+1)+200;
	           y = positionGenerator.nextInt((300-50)+1)+50;
	               Vertex vSource = Vertex.createVertex(sourceEcken, 0, x, y);
	           x = positionGenerator.nextInt((800-200)+1)+200;
	           y = positionGenerator.nextInt((300-50)+1)+50;
	               Vertex vTarget = Vertex.createVertex(targetEcken, 0, x, y);
	               
	               
	               if(!alleEcken.contains(targetEcken) &&  targetEcken.length() != 0)
	               {            	
	            	
	                   returngraph.addVertex(vTarget);
	                   alleVertex.add(vTarget);
	                   alleEcken.add(targetEcken);
	               
	               }
	               else
	               {
	            	   for(int k = 0; k<alleEcken.size(); k++)
	            	   {
	            		   if(alleEcken.get(k).equals(targetEcken))
	            		   {
	            			   vTarget = alleVertex.get(k);
	            		   }
	            	   }
	               }
	              
	
	               if(!alleEcken.contains(sourceEcken))
	               {            	
	                   returngraph.addVertex(vSource);
	                   alleVertex.add(vSource);
	                   alleEcken.add(sourceEcken);
	               }
	               else
	               {
	            	   for(int k = 0; k<alleEcken.size(); k++)
	            	   {
	            		   if(alleEcken.get(k).equals(sourceEcken))
	            		   {
	            			   vSource = alleVertex.get(k);
	            		   }
	            	   }
	               }
	               
	               if(!returngraph.containsVertex(vSource))
	               {            	
	                   returngraph.addVertex(vSource);
	                   alleVertex.add(vSource);
	               }
	               
	               if(vSource.equals(vTarget)){            	   
	            	    MyWeightedEdge edge = returngraph.addEdge(vSource, vSource);
	            	   ((DirectedWeightedPseudograph<Vertex, MyWeightedEdge>)returngraph).setEdgeWeight(edge, weighing);
	               }
	               else if(targetEcken.length() == 0)
	               {
	                   
	               }
	               else
	               {
	            	   MyWeightedEdge edge = returngraph.addEdge(vSource, vTarget);
	            	   ((DirectedWeightedPseudograph<Vertex, MyWeightedEdge>)returngraph).setEdgeWeight(edge, weighing);
	            	   
	               }
	               
	           }
	 
	       return returngraph;
	    }
	    
	    
	    public static Graph<Vertex, MyWeightedEdge> directedAttributeGraph(List<String> infoGraph)
	    {

	    	   
	      DirectedAttributedGraph<Vertex, MyWeightedEdge> returnGraph = new DirectedAttributedGraph<Vertex, MyWeightedEdge>(MyWeightedEdge.class);
	      Random positionGenerator = new Random();
	      int x,y;
	      
	      List<String> alleEcken = new ArrayList<String>();
	      List<Vertex> alleVertex = new ArrayList<Vertex>();
	      
	      for(int i = 0; i < infoGraph.size(); i++)
	      {
	
	   	   String targetEcken = "";
	   	   String targetAttAsString = "";
	   	   Double targetAttribute;
	   	   String sourceEcken = "";
	   	   String sourceAttAsString = "";
	   	   Double  sourceAttribute;
	   	   
	   	   if(infoGraph.get(i).length() == 0)
	    	   {
	    		   infoGraph.remove(i);
	    	   }
	   	   
	   	   boolean isvar1 = true;
	   	   boolean isAttributeVar1 = true;
	   	   boolean isvar2 = true;
	   	   boolean isAttributeVar2 = true;
	   	   
	   	   for(int j = 0; j < infoGraph.get(i).length(); j++)
	   	   {
	   		 
	       	   
	              if(infoGraph.get(i).charAt(j) != ','  &&
	           		   infoGraph.get(i).charAt(j) != ' '  &&
	           		   infoGraph.get(i).charAt(j) != ':'  &&
	           		   isvar1)
	              {
	                  sourceEcken = sourceEcken + infoGraph.get(i).charAt(j);                 
	              }
	              else if(infoGraph.get(i).charAt(j) == ':'  &&
	           		      isvar1)
	              {
	           	   isvar1 = false;
	              }
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	            		  infoGraph.get(i).charAt(j) != ' '  &&
	            		  infoGraph.get(i).charAt(j) != ':'  &&
	           		   		isAttributeVar1)
	              {
	            	  
	            	  sourceAttAsString = sourceAttAsString + infoGraph.get(i).charAt(j);
//	            	  if(infoGraph.get(i).charAt(j) == '-')
//	            	  {
//	            		  sourceAttribute = -1;
//	            	  }
//	           	   sourceAttribute = 10 * sourceAttribute + Integer.parseInt(infoGraph.get(i).charAt(j) + "");            	  
	              }
	              else if(infoGraph.get(i).charAt(j) == ','  &&
	            		  isAttributeVar1)
	              {
	            	  isAttributeVar1 = false;
	              }
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	            		  infoGraph.get(i).charAt(j) != ' '  &&
	            		  infoGraph.get(i).charAt(j) != ':'  &&
	           		   		isvar2)
	              {
	           	   targetEcken = targetEcken +  infoGraph.get(i).charAt(j);            	  
	              }
	              else if(infoGraph.get(i).charAt(j) == ':'  &&
	            		  isvar2)
	              {
	            	  isvar2 = false;
	              }  
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	            		  infoGraph.get(i).charAt(j) != ' '  &&
	            		  infoGraph.get(i).charAt(j) != ':'  &&
	           		   		isAttributeVar2)
	              {
	            	  
	            	  targetAttAsString = targetAttAsString + infoGraph.get(i).charAt(j);
//	            	  if(infoGraph.get(i).charAt(j) == '-')
//	            	  {
//	            		  targetAttribute = -1;
//	            	  }
//	            	  
//	           	   targetAttribute =  10 * targetAttribute +  Integer.parseInt(infoGraph.get(i).charAt(j) + "");            	  
	              }
	              else if(infoGraph.get(i).charAt(j) == ':'  &&
	            		  isAttributeVar2)
	              {
	            	  isAttributeVar2 = false;
	              }
	
	   	   }
	   	   
	   	   targetAttribute = Double.parseDouble(targetAttAsString);
	   	   sourceAttribute = Double.parseDouble(sourceAttAsString); 
	   	   
	       x = positionGenerator.nextInt((800-200)+1)+200;
	       y = positionGenerator.nextInt((300-50)+1)+0;
	           Vertex vSource = Vertex.createVertex(sourceEcken, sourceAttribute, x, y);
	       x = positionGenerator.nextInt((800-200)+1)+200;
	       y = positionGenerator.nextInt((300-50)+1)+50;
	           Vertex vTarget = Vertex.createVertex(targetEcken, targetAttribute, x, y);
	             
	             
	             if(!alleEcken.contains(targetEcken) &&  targetEcken.length() != 0)
	             {            	
	                 returnGraph.addVertex(vTarget);
	                 alleVertex.add(vTarget);
	                 alleEcken.add(targetEcken);
	             
	             }
	             else
	             {
	          	   for(int k = 0; k<alleEcken.size(); k++)
	          	   {
	          		   if(alleEcken.get(k).equals(targetEcken))
	          		   {
	          			   vTarget = alleVertex.get(k);
	          		   }
	          	   }
	             }
	            
	
	             if(!alleEcken.contains(sourceEcken))
	             {            	
	                 returnGraph.addVertex(vSource);
	                 alleVertex.add(vSource);
	                 alleEcken.add(sourceEcken);
	             }
	             else
	             {
	          	   for(int k = 0; k<alleEcken.size(); k++)
	          	   {
	          		   if(alleEcken.get(k).equals(sourceEcken))
	          		   {
	          			   vSource = alleVertex.get(k);
	          		   }
	          	   }
	             }
	             
	
	             if(!returnGraph.containsVertex(vSource))
	             {            	
	                 returnGraph.addVertex(vSource);
	                 alleVertex.add(vSource);
	             }
	             
	             if(vSource.equals(vTarget)){            	   
	          	   returnGraph.addEdge(vSource,vSource);
	             }
	             else if(targetEcken.length() == 0)
	             {
	                 
	             }
	             else
	             {
	          	   returnGraph.addEdge(vSource, vTarget);            	   
	             }
	             
	         }
	           
	
	     return returnGraph;
	  }


	    /**
	     * erstellt einen dirketen PseudoGraphen mit Gewichten und Attributen, aus einer Liste von Strings, her.
	     * und gibt diesen dann zurück
	     * @param infoGraph
	     * @return graph
	     */
	    public static Graph<Vertex, MyWeightedEdge> directedAttriuteWeightedGraph(List<String> infoGraph)
	    {
	      DirectedWeightedPseudograph<Vertex, MyWeightedEdge> returnGraph = new DirectedAttributedWeightedGraph<Vertex, MyWeightedEdge>(MyWeightedEdge.class);
	      Random positionGenerator = new Random();
	      int x,y;
	      
	      List<String> alleEcken = new ArrayList<String>();
	      List<Vertex> alleVertex = new ArrayList<Vertex>();
	
	      
	      for(int i = 0; i < infoGraph.size(); i++)
	      {
	
	   	   String targetEcken = "";
	   	   String targetAttAsString = "";
	   	   double targetAttribute;
	   	   String sourceEcken = "";
	   	   String sourceAttAsString = "";
	   	   double sourceAttribute;
	   	   double weighing = 0;
	   	   
	   	   if(infoGraph.get(i).length() == 0)
	    	   {
	    		   infoGraph.remove(i);
	    	   }
	   	   
	   	   boolean isvar1 = true;
	   	   boolean isAttributeVar1 = true;
	   	   boolean isvar2 = true;
	   	   boolean isAttributeVar2 = true;
	   	   
	   	   for(int j = 0; j < infoGraph.get(i).length(); j++)
	   	   {
	   		 
	       	   
	              if(infoGraph.get(i).charAt(j) != ','  &&
	           		   infoGraph.get(i).charAt(j) != ' '  &&
	           		   infoGraph.get(i).charAt(j) != ':'  &&
	           		   isvar1)
	              {
	                  sourceEcken = sourceEcken + infoGraph.get(i).charAt(j);                 
	              }
	              else if(infoGraph.get(i).charAt(j) == ':'  &&
	           		      isvar1)
	              {
	           	   isvar1 = false;
	              }
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	            		  infoGraph.get(i).charAt(j) != ' '  &&
	            		  infoGraph.get(i).charAt(j) != ':'  &&
	           		   		isAttributeVar1)
	              {
	            	  
	            	  sourceAttAsString = sourceAttAsString + infoGraph.get(i).charAt(j);
	            	  
	              }
	              else if(infoGraph.get(i).charAt(j) == ','  &&
	            		  isAttributeVar1)
	              {
	            	  isAttributeVar1 = false;
	              }
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	            		  infoGraph.get(i).charAt(j) != ' '  &&
	            		  infoGraph.get(i).charAt(j) != ':'  &&
	           		   		isvar2)
	              {
	           	   targetEcken = targetEcken +  infoGraph.get(i).charAt(j);            	  
	              }
	              else if(infoGraph.get(i).charAt(j) == ':'  &&
	            		  isvar2)
	              {
	            	  isvar2 = false;
	              }  
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	            		  infoGraph.get(i).charAt(j) != ' '  &&
	            		  infoGraph.get(i).charAt(j) != ':'  &&
	           		   		isAttributeVar2)
	              {
	            	  targetAttAsString = targetAttAsString + infoGraph.get(i).charAt(j);
	              }
	              else if(infoGraph.get(i).charAt(j) == ':'  &&
	            		  isAttributeVar2)
	              {
	            	  isAttributeVar2 = false;
	              }
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	             		   infoGraph.get(i).charAt(j) != ' '  &&
	             			 infoGraph.get(i).charAt(j) != ':')
	               {
	               	if(infoGraph.get(i).charAt(j) == '-')
	               	{
	               		weighing = (-1) *  Double.parseDouble((infoGraph.get(i).charAt(j + 1) + ""));
	               		j++;
	               	}
	               	else
	               	{
	               		
	               		weighing = weighing * 10 +  Double.parseDouble((infoGraph.get(i).charAt(j) + ""));                		
	               	}
	
	               }
	

	   	   }
	   	   sourceAttribute = Double.parseDouble(sourceAttAsString);
	   	   targetAttribute = Double.parseDouble(targetAttAsString);

	   	   
	       x = positionGenerator.nextInt((800-200)+1)+200;
	       y = positionGenerator.nextInt((300-50)+1)+50;
	           Vertex vSource = Vertex.createVertex(sourceEcken, sourceAttribute, x, y);
	       x = positionGenerator.nextInt((800-200)+1)+200;
	       y = positionGenerator.nextInt((300-50)+1)+50;
	           Vertex vTarget = Vertex.createVertex(targetEcken,  targetAttribute , x, y);
	             
	             
	             if(!alleEcken.contains(targetEcken) &&  targetEcken.length() != 0)
	             {            	

	                 returnGraph.addVertex(vTarget);
	                 alleVertex.add(vTarget);
	                 alleEcken.add(targetEcken);
	             
	             }
	             else
	             {

	          	   for(int k = 0; k<alleEcken.size(); k++)
	          	   {
	          		   if(alleEcken.get(k).equals(targetEcken))
	          		   {
	          			   vTarget = alleVertex.get(k);
	          		   }
	          	   }
	             }
	            
	
	             if(!alleEcken.contains(sourceEcken))
	             {            	
	                 returnGraph.addVertex(vSource);
	                 alleVertex.add(vSource);
	                 alleEcken.add(sourceEcken);
	             }
	             else
	             {

	          	   for(int k = 0; k<alleEcken.size(); k++)
	          	   {
	          		   if(alleEcken.get(k).equals(sourceEcken))
	          		   {
	          			   vSource = alleVertex.get(k);
	          		   }
	          	   }
	             }
	             
	
	             if(!returnGraph.containsVertex(vSource))
	             {            	
	                 returnGraph.addVertex(vSource);
	                 alleVertex.add(vSource);
	             }
	             
	             
	             if(vSource.equals(vTarget)){            	   
	          	   MyWeightedEdge edge =  returnGraph.addEdge(vSource,vSource);
	          	   	((DirectedWeightedPseudograph<Vertex, MyWeightedEdge>)returnGraph).setEdgeWeight(edge, weighing);
	             }
	             else if(targetEcken.length() == 0)
	             {
	                 
	             }
	             else
	             {
	            	 MyWeightedEdge edge = returnGraph.addEdge(vSource, vTarget);   
	          	   	((DirectedWeightedPseudograph<Vertex, MyWeightedEdge>)returnGraph).setEdgeWeight(edge, weighing);
	             }
	             
	         }
	           
	
	     return returnGraph;
	  }
	

	    /**
	     * erstelle einen ungerichteten PseudoGraphen aus eibner Liste von Strings.
	     * Und gibt den Graphen zurück
	     * @param infoGraph
	     * @return graph
	     */
	    public static Graph<Vertex, MyWeightedEdge> undirectedGraph(List<String> infoGraph)
	    {

	        Pseudograph<Vertex, MyWeightedEdge> returnGraph = new Pseudograph<Vertex, MyWeightedEdge>(MyWeightedEdge.class);
	        Random positionGenerator = new Random();
	        int x,y;
	        
	        List<String> alleEcken = new ArrayList<String>();
	        List<Vertex> alleVertex = new ArrayList<Vertex>();
	        
	        for(int i = 0; i < infoGraph.size(); i++)
	        {
	
	     	   String targetEcken = "";
	     	   String sourceEcken = "";
	     	   
	     	   if(infoGraph.get(i).length() == 0)
	      	   {
	      		   infoGraph.remove(i);
	      	   }
	     	   
	     	   boolean isvar1 = true;
	     	   boolean isvar2 = true;
	     	   
	     	   for(int j = 0; j < infoGraph.get(i).length(); j++)
	     	   {
	     		 
	         	   
	                if(infoGraph.get(i).charAt(j) != ','  &&
	             		   infoGraph.get(i).charAt(j) != ' '  &&
	             		   isvar1)
	                {
	                    sourceEcken = sourceEcken + infoGraph.get(i).charAt(j);                 
	                }
	                else if(infoGraph.get(i).charAt(j) == ','  &&
	             		   isvar1)
	                {
	             	   isvar1 = false;
	                }
	                else if(infoGraph.get(i).charAt(j) != ',' &&
	             		   infoGraph.get(i).charAt(j) != ' '  &&
	             		   isvar2)
	                {
	             	   targetEcken = targetEcken +  infoGraph.get(i).charAt(j);            	  
	                }
	
	     	   }

	           x = positionGenerator.nextInt((800-200)+1)+200;
	           y = positionGenerator.nextInt((300-50)+1)+50;
	               Vertex vSource = Vertex.createVertex(sourceEcken, 0, x, y);
	           x = positionGenerator.nextInt((800-200)+1)+200;
	           y = positionGenerator.nextInt((300-50)+1)+50;
	               Vertex vTarget = Vertex.createVertex(targetEcken, 0, x, y);
	               
	               
	               if(!alleEcken.contains(targetEcken) &&  targetEcken.length() != 0)
	               {            	
	                   returnGraph.addVertex(vTarget);
	                   alleVertex.add(vTarget);
	                   alleEcken.add(targetEcken);
	               
	               }
	               else
	               {
	            	   for(int k = 0; k<alleEcken.size(); k++)
	            	   {
	            		   if(alleEcken.get(k).equals(targetEcken))
	            		   {
	            			   vTarget = alleVertex.get(k);
	            		   }
	            	   }
	               }
	              
	
	               if(!alleEcken.contains(sourceEcken))
	               {            	
	                   returnGraph.addVertex(vSource);
	                   alleVertex.add(vSource);
	                   alleEcken.add(sourceEcken);
	               }
	               else
	               {
	            	   for(int k = 0; k<alleEcken.size(); k++)
	            	   {
	            		   if(alleEcken.get(k).equals(sourceEcken))
	            		   {
	            			   vSource = alleVertex.get(k);
	            		   }
	            	   }
	               }
	               if(!returnGraph.containsVertex(vSource))
	               {            	
	                   returnGraph.addVertex(vSource);
	                   alleVertex.add(vSource);
	               }
	               
	               if(vSource.equals(vTarget)){            	   
	            	   returnGraph.addEdge(vSource,vSource);
	               }
	               else if(targetEcken.length() == 0)
	               {
	                   
	               }
	               else
	               {
	            	   returnGraph.addEdge(vSource, vTarget);            	   
	               }
	               
	    
	               {
	            	   
	               }
	               
	           }
	       return returnGraph;
	    }
	    
	    
	    /**
	     * erstellt einen gewichteten ungrichteten Pseudographen her und gibt diesen zurück
	     * @param infoGraph
	     * @return graph
	     */
	    public static Graph<Vertex, MyWeightedEdge> undirectedWeightedGraph(List<String> infoGraph)
	    {
	        WeightedPseudograph<Vertex,MyWeightedEdge> returnGraph = new WeightedPseudograph<Vertex,MyWeightedEdge>( MyWeightedEdge.class );
	        
	        Random positionGenerator = new Random();
	        int x,y;
	        
	        List<String> alleEcken = new ArrayList<String>();
	        List<Vertex> alleVertex = new ArrayList<Vertex>();
	        
	        for(int i = 0; i < infoGraph.size(); i++)
	        {
	
	     	   String targetEcken = "";
	     	   String sourceEcken = "";
	     	   double weighing = 0;
	     	   
	     	   if(infoGraph.get(i).length() == 0)
	      	   {
	      		   infoGraph.remove(i);
	      	   }
	     	   
	     	   boolean isvar1finisch = true;
	     	   boolean isvar2finisch = true;
	     	   
	     	   for(int j = 0; j < infoGraph.get(i).length(); j++)
	     	   {
	         	   
	                if(infoGraph.get(i).charAt(j) != ','  &&
	             		   infoGraph.get(i).charAt(j) != ' '  &&
	             			 infoGraph.get(i).charAt(j) != ':'  &&
	             		   isvar1finisch)
	                {
	                    sourceEcken = sourceEcken + infoGraph.get(i).charAt(j);                 
	                }
	                else if(infoGraph.get(i).charAt(j) == ','  &&
	                		 infoGraph.get(i).charAt(j) != ' '  &&
	                		 infoGraph.get(i).charAt(j) != ':'  &&
	             		   isvar1finisch)
	                {
	             	   isvar1finisch = false;
	                }
	                else if(infoGraph.get(i).charAt(j) != ',' &&
	             		   infoGraph.get(i).charAt(j) != ' '  &&
	             				  infoGraph.get(i).charAt(j) != ':'  &&
	             		   isvar2finisch)
	                {
	             	   targetEcken = targetEcken +  infoGraph.get(i).charAt(j);    
	             	   isvar2finisch = false;
	                }else if(infoGraph.get(i).charAt(j) != ','  &&
	              		   infoGraph.get(i).charAt(j) != ' '  &&
	              			 infoGraph.get(i).charAt(j) != ':')
	                {
	                	if(infoGraph.get(i).charAt(j) == '-')
	                	{
	                		weighing = (-1) *  Double.parseDouble((infoGraph.get(i).charAt(j + 1) + ""));
	                		j++;
	                	}
	                	else
	                	{
	                		weighing = weighing * 10 +  Double.parseDouble((infoGraph.get(i).charAt(j) + ""));                		
	                	}
	
	                }
	
	     	   }
	           x = positionGenerator.nextInt((800-200)+1)+200;
	           y = positionGenerator.nextInt((300-50)+1)+50;
	               Vertex vSource = Vertex.createVertex(sourceEcken, 0, x, y);
	           x = positionGenerator.nextInt((800-200)+1)+200;
	           y = positionGenerator.nextInt((300-50)+1)+50;
	               Vertex vTarget = Vertex.createVertex(targetEcken, 0, x, y);
	               
	               
	               if(!alleEcken.contains(targetEcken) &&  targetEcken.length() != 0)
	               {            	
	            	
	                   returnGraph.addVertex(vTarget);
	                   alleVertex.add(vTarget);
	                   alleEcken.add(targetEcken);
	               
	               }
	               else
	               {
	            	   for(int k = 0; k<alleEcken.size(); k++)
	            	   {
	            		   if(alleEcken.get(k).equals(targetEcken))
	            		   {
	            			   vTarget = alleVertex.get(k);
	            		   }
	            	   }
	               }
	              
	
	               if(!alleEcken.contains(sourceEcken))
	               {            	
	                   returnGraph.addVertex(vSource);
	                   alleVertex.add(vSource);
	                   alleEcken.add(sourceEcken);
	               }
	               else
	               {
	            	   for(int k = 0; k<alleEcken.size(); k++)
	            	   {
	            		   if(alleEcken.get(k).equals(sourceEcken))
	            		   {
	            			   vSource = alleVertex.get(k);
	            		   }
	            	   }
	               }
	
	              
	
	               if(!returnGraph.containsVertex(vSource))
	               {            	
	                   returnGraph.addVertex(vSource);
	                   alleVertex.add(vSource);
	               }
	               
	               
	               if(vSource.equals(vTarget)){            	   
	            	    MyWeightedEdge edge = returnGraph.addEdge(vSource, vSource);
	            	   ((Pseudograph<Vertex, MyWeightedEdge>)returnGraph).setEdgeWeight(edge, weighing);
	               }
	               else if(targetEcken.length() == 0)
	               {
	                   
	               }
	               else
	               {
	            	   MyWeightedEdge edge = returnGraph.addEdge(vSource, vTarget);
	            	   ((Pseudograph<Vertex, MyWeightedEdge>)returnGraph).setEdgeWeight(edge, weighing);
	               }
	               
	           }
	       return returnGraph;
	    }

	    
	    /**
	     * erstellt einen ungrichteten Graphen mit Attributen her und gibt diesen zum Schluss zurück
	     * @param infoGraph
	     * @return
	     */
	    public static Graph<Vertex, MyWeightedEdge> undirectedAttributeGraph(List<String> infoGraph)
	    {

	      UndirectedAttributedGraph<Vertex, MyWeightedEdge> retrunGraph = new UndirectedAttributedGraph<Vertex, MyWeightedEdge>(MyWeightedEdge.class);
	      Random positionGenerator = new Random();
	      int x,y;
	      
	      List<String> alleEcken = new ArrayList<String>();
	      List<Vertex> alleVertex = new ArrayList<Vertex>();
	      
	      for(int i = 0; i < infoGraph.size(); i++)
	      {
	
	   	   String targetEcken = "";
	   	   String targeAttAsString  = "";
	   	   double targetAttribute;
	   	   String sourceEcken = "";
	   	   String sourceAttAsString = "";
	   	   double sourceAttribute;
	   	   
	   	   if(infoGraph.get(i).length() == 0)
	    	   {
	    		   infoGraph.remove(i);
	    	   }
	   	   
	   	   boolean isvar1 = true;
	   	   boolean isAttributeVar1 = true;
	   	   boolean isvar2 = true;
	   	   boolean isAttributeVar2 = true;
	   	   
	   	   for(int j = 0; j < infoGraph.get(i).length(); j++)
	   	   {
	   		 
	       	   
	              if(infoGraph.get(i).charAt(j) != ','  &&
	           		   infoGraph.get(i).charAt(j) != ' '  &&
	           		   infoGraph.get(i).charAt(j) != ':'  &&
	           		   isvar1)
	              {
	                  sourceEcken = sourceEcken + infoGraph.get(i).charAt(j);                 
	              }
	              else if(infoGraph.get(i).charAt(j) == ':'  &&
	           		      isvar1)
	              {
	           	   isvar1 = false;
	              }
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	            		  infoGraph.get(i).charAt(j) != ' '  &&
	            		  infoGraph.get(i).charAt(j) != ':'  &&
	           		   		isAttributeVar1)
	              {
	            	  	sourceAttAsString = sourceAttAsString + infoGraph.get(i).charAt(j);
	              }
	              else if(infoGraph.get(i).charAt(j) == ','  &&
	            		  isAttributeVar1)
	              {
	            	  isAttributeVar1 = false;
	              }
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	            		  infoGraph.get(i).charAt(j) != ' '  &&
	            		  infoGraph.get(i).charAt(j) != ':'  &&
	           		   		isvar2)
	              {
	            	 targetEcken = targetEcken + infoGraph.get(i).charAt(j);
	              }
	              else if(infoGraph.get(i).charAt(j) == ':'  &&
	            		  isvar2)
	              {
	            	  isvar2 = false;
	              }  
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	            		  infoGraph.get(i).charAt(j) != ' '  &&
	            		  infoGraph.get(i).charAt(j) != ':'  &&
	           		   		isAttributeVar2)
	              {
	            	  targeAttAsString = targeAttAsString + infoGraph.get(i).charAt(j);
	              }
	              else if(infoGraph.get(i).charAt(j) == ':'  &&
	            		  isAttributeVar2)
	              {
	            	  isAttributeVar2 = false;
	              }
	
	   	   }
	   	   
	   	   
	   	   sourceAttribute = Double.parseDouble(sourceAttAsString);
	   	   targetAttribute = Double.parseDouble(targeAttAsString); 
	   	   
	       x = positionGenerator.nextInt((800-200)+1)+200;
	       y = positionGenerator.nextInt((300-50)+1)+50;
	           Vertex vSource = Vertex.createVertex(sourceEcken, sourceAttribute, x, y);
	       x = positionGenerator.nextInt((800-200)+1)+200;
	       y = positionGenerator.nextInt((300-50)+1)+50;
	           Vertex vTarget = Vertex.createVertex(targetEcken, targetAttribute, x, y);
	             
	             
	             if(!alleEcken.contains(targetEcken) &&  targetEcken.length() != 0)
	             {            	
	                 retrunGraph.addVertex(vTarget);
	                 alleVertex.add(vTarget);
	                 alleEcken.add(targetEcken);
	             
	             }
	             else
	             {
	            	 
	          	   for(int k = 0; k<alleEcken.size(); k++)
	          	   {
	          		   if(alleEcken.get(k).equals(targetEcken))
	          		   {
	          			   vTarget = alleVertex.get(k);
	          		   }
	          	   }
	             }
	            
	
	             if(!alleEcken.contains(sourceEcken))
	             {            	
	                 retrunGraph.addVertex(vSource);
	                 alleVertex.add(vSource);
	                 alleEcken.add(sourceEcken);
	             }
	             else
	             {
	          	   for(int k = 0; k<alleEcken.size(); k++)
	          	   {
	          		   if(alleEcken.get(k).equals(sourceEcken))
	          		   {
	          			   vSource = alleVertex.get(k);
	          		   }
	          	   }
	             }

	             if(!retrunGraph.containsVertex(vSource))
	             {            	
	                 retrunGraph.addVertex(vSource);
	                 alleVertex.add(vSource);
	             }
	             
	             if(vSource.equals(vTarget)){            	   
	          	   retrunGraph.addEdge(vSource,vSource);
	             }
	             else if(targetEcken.length() == 0)
	             {
	                 
	             }
	             else
	             {
	          	   retrunGraph.addEdge(vSource, vTarget);            	   
	             }
	             
	         }
	           
	
	     return retrunGraph;
	  }
	    
	    
	    /**
	     * erstellt einen gewichteten ungrichteten Graphen mut Attributen her und gibt diesen zurück
	     * @param infoGraph
	     * @return graph
	     */
	    public static Graph<Vertex, MyWeightedEdge> undirectedAttriuteWeightedGraph(List<String> infoGraph)
	    {
	    	   
	      UndirectedAttributedWeightedGraph<Vertex, MyWeightedEdge> returnGraph = new UndirectedAttributedWeightedGraph<Vertex, MyWeightedEdge>(MyWeightedEdge.class);
	      Random positionGenerator = new Random();
	      int x,y;
	      
	      List<String> alleEcken = new ArrayList<String>();
	      List<Vertex> alleVertex = new ArrayList<Vertex>();
	
	      
	      for(int i = 0; i < infoGraph.size(); i++)
	      {
	
	   	   String targetEcken = "";
	   	   String targetAttAsString = "";
	   	   double targetAttribute;
	   	   String sourceEcken = "";
	   	   String sourceAttAsString = "";
	   	   double sourceAttribute;
	   	   double weighing = 0;
	   	   
	   	   if(infoGraph.get(i).length() == 0)
	    	   {
	    		   infoGraph.remove(i);
	    	   }
	   	   
	   	   boolean isvar1 = true;
	   	   boolean isAttributeVar1 = true;
	   	   boolean isvar2 = true;
	   	   boolean isAttributeVar2 = true;
	   	   
	   	   for(int j = 0; j < infoGraph.get(i).length(); j++)
	   	   {
	   		 
	       	   
	              if(infoGraph.get(i).charAt(j) != ','  &&
	           		   infoGraph.get(i).charAt(j) != ' '  &&
	           		   infoGraph.get(i).charAt(j) != ':'  &&
	           		   isvar1)
	              {
	                  sourceEcken = sourceEcken + infoGraph.get(i).charAt(j);                 
	              }
	              else if(infoGraph.get(i).charAt(j) == ':'  &&
	           		      isvar1)
	              {
	           	   isvar1 = false;
	              }
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	            		  infoGraph.get(i).charAt(j) != ' '  &&
	            		  infoGraph.get(i).charAt(j) != ':'  &&
	           		   		isAttributeVar1)
	              {
	            	  sourceAttAsString = sourceAttAsString + infoGraph.get(i).charAt(j);
	              }
	              else if(infoGraph.get(i).charAt(j) == ','  &&
	            		  isAttributeVar1)
	              {
	            	  isAttributeVar1 = false;
	              }
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	            		  infoGraph.get(i).charAt(j) != ' '  &&
	            		  infoGraph.get(i).charAt(j) != ':'  &&
	           		   		isvar2)
	              {
	           	   targetEcken = targetEcken +  infoGraph.get(i).charAt(j);            	  
	              }
	              else if(infoGraph.get(i).charAt(j) == ':'  &&
	            		  isvar2)
	              {
	            	  isvar2 = false;
	              }  
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	            		  infoGraph.get(i).charAt(j) != ' '  &&
	            		  infoGraph.get(i).charAt(j) != ':'  &&
	           		   		isAttributeVar2)
	              {
	            	  	targetAttAsString = targetAttAsString + infoGraph.get(i).charAt(j);
	              }
	              else if(infoGraph.get(i).charAt(j) == ':'  &&
	            		  isAttributeVar2)
	              {
	            	  isAttributeVar2 = false;
	              }
	              else if(infoGraph.get(i).charAt(j) != ','  &&
	             		   infoGraph.get(i).charAt(j) != ' '  &&
	             			 infoGraph.get(i).charAt(j) != ':')
	               {
	               	if(infoGraph.get(i).charAt(j) == '-')
	               	{
	               		weighing = (-1) *  Double.parseDouble((infoGraph.get(i).charAt(j + 1) + ""));
	               		j++;
	               	}
	               	else
	               	{
	               		weighing = weighing * 10 +  Double.parseDouble((infoGraph.get(i).charAt(j) + ""));                		
	               	}
	
	               }
	
	
	   	   }
	   	   
	   	   sourceAttribute = Double.parseDouble(sourceAttAsString);
	   	   targetAttribute = Double.parseDouble(targetAttAsString);
	   	   
	   	   
	       x = positionGenerator.nextInt((800-200)+1)+200;
	       y = positionGenerator.nextInt((300-50)+1)+50;
	           Vertex vSource = Vertex.createVertex(sourceEcken, sourceAttribute, x, y);
	       x = positionGenerator.nextInt((800-200)+1)+200;
	       y = positionGenerator.nextInt((300-50)+1)+50;
	           Vertex vTarget = Vertex.createVertex(targetEcken, targetAttribute, x, y);
	             
	             if(!alleEcken.contains(targetEcken) &&  targetEcken.length() != 0)
	             {            	
	          	
	                 returnGraph.addVertex(vTarget);
	                 alleVertex.add(vTarget);
	                 alleEcken.add(targetEcken);
	             
	             }
	             else
	             {
	          	   for(int k = 0; k<alleEcken.size(); k++)
	          	   {
	          		   if(alleEcken.get(k).equals(targetEcken))
	          		   {
	          			   vTarget = alleVertex.get(k);
	          		   }
	          	   }
	             }
	            
	
	             if(!alleEcken.contains(sourceEcken))
	             {            	
	                 returnGraph.addVertex(vSource);
	                 alleVertex.add(vSource);
	                 alleEcken.add(sourceEcken);
	             }
	             else
	             {
	          	   for(int k = 0; k<alleEcken.size(); k++)
	          	   {
	          		   if(alleEcken.get(k).equals(sourceEcken))
	          		   {
	          			   vSource = alleVertex.get(k);
	          		   }
	          	   }
	             }
	             
	
	             if(!returnGraph.containsVertex(vSource))
	             {            	
	                 returnGraph.addVertex(vSource);
	                 alleVertex.add(vSource);
	             }
	             
	             if(vSource.equals(vTarget)){            	   
	            	MyWeightedEdge edge = returnGraph.addEdge(vSource,vSource);
	          	    ((WeightedPseudograph<Vertex, MyWeightedEdge>)returnGraph).setEdgeWeight(edge, weighing);
	             }
	             else if(targetEcken.length() == 0)
	             {
	                 
	             }
	             else
	             {
	            	 MyWeightedEdge edge = returnGraph.addEdge(vSource, vTarget);
	            	 ((Pseudograph<Vertex, MyWeightedEdge>)returnGraph).setEdgeWeight(edge, weighing);
	             }
	             
	         }
	      
	      
	           
	
	     return returnGraph;
	  }
	
	    public Graph<Vertex,MyWeightedEdge> createDefaultUndirectedGraph(Graph<Vertex, MyWeightedEdge> _graph)
	    {
	        anzahlErstellteGraphen++;
	        
	        Random intgenerator = new Random();
	        int x = intgenerator.nextInt((600-400)+1)+400;
	        int y = intgenerator.nextInt((300-200)+1)+200;
	        
	        _graph = new UndirectedAttributedWeightedGraph<>(MyWeightedEdge.class);
	        
	        Vertex v1 = Vertex.createVertex("V1", 0,x,y);
	        Vertex v2 = Vertex.createVertex("V2", 0,x-100,y-100);
	        
	        
	        _graph.addVertex(v1);
	        _graph.addVertex(v2);
	        _graph.addEdge(v1,v2);    
	        
	        return _graph;
	    }
	public Graph<Vertex, MyWeightedEdge> createDefaultDirectedGraph(Graph<Vertex,MyWeightedEdge> graph)
	{
	    anzahlErstellteGraphen++;
	    
	    Random intgenerator = new Random();
	    int x = intgenerator.nextInt((600-400)+1)+400;
	    int y = intgenerator.nextInt((300-200)+1)+200;
	    
	    graph = new DirectedPseudograph<>(MyWeightedEdge.class);
	    
	    Vertex v1 = Vertex.createVertex("V1", 0,x,y);
	    Vertex v2 = Vertex.createVertex("V2", 0,x-100,y-100);
	    
	    
	    graph.addVertex(v1);
	    graph.addVertex(v2);
	    graph.addEdge(v1,v2);    
	    
	    return graph;
	}
	
	public JGraphModelAdapter<Vertex,MyWeightedEdge> getModelAdapter(Graph<Vertex,MyWeightedEdge> graph)
	{
	    JGraphModelAdapter<Vertex,MyWeightedEdge> modAdapter = new JGraphModelAdapter<>(graph);
	    Set<Vertex> vertexes = graph.vertexSet();
	    for(Vertex o : vertexes)
	    {
	        DefaultGraphCell cell = modAdapter.getVertexCell(o);
	        Map<?, ?> attr = cell.getAttributes();
	        GraphConstants.setBounds(attr, new Rectangle(o.getX(),o.getY(),graphWIDTH,graphHEIGHT));
	        if(o.isVisited() && o.isTarget())
	        {
	            GraphConstants.setBackground(attr, Color.green); 
	        }
	        else if(!o.isVisited() && o.isTarget())
	        {
	            GraphConstants.setBackground(attr, Color.red);
	        }
	        else if(o.isPartOfShortestWay() && !o.isTarget())
	        {
	            GraphConstants.setBackground(attr, Color.CYAN);
	        }
	        else if(o.isVisited() && !o.isTarget())
	        {
	            GraphConstants.setBackground(attr, Color.blue);
	        }
	
	        _cellAttr.put(cell,attr);
	        modAdapter.edit(_cellAttr, null, null, null);
	        
	    }  
	    return modAdapter;
	}
	
	

	
	public void addVertexAtPosition(String s, double attr,int x,int y, Graph<Vertex,MyWeightedEdge> graph)
	{
	    graph.addVertex(Vertex.createVertex(s, attr, x, y));
	}
	
	
	
	public void addEdge(Vertex vertexSource, Vertex vertexTarget,
	        Graph<Vertex, MyWeightedEdge> _graph)
	{
	    _graph.addEdge(vertexSource, vertexTarget);
	    
	}
	
	
	//TODO layout
//	public void layout(DirectedPseudograph<Vertex, MyWeightedEdge> graphModel,JGraphModelAdapter<?, ?> graphAdapter,JGraph graph)
//	   {
//	        List roots = new ArrayList();
//	        for(Vertex v : graphModel.vertexSet())
//	        {
//	            if (graphModel.inDegreeOf(v) == 0) {
//	                roots.add(graphAdapter.getVertexCell(v));
//	            }
//	        }
//	        JGraphLayoutAlgorithm layout = new SugiyamaLayoutAlgorithm();
//	        layout.applyLayout(graph, layout, roots.toArray(), null);
//	   } 
	}
