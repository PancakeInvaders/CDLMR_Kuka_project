import java.io.*;

import org.jdom2.*;
import org.jdom2.input.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import imerir.CDLMR.trajectoire.*;
import imerir.CDLMR.trajectoire.Trajectoire.Type;

public class JDOM2
{
   public SvgMaison getSvgMaison(String filePath) throws JDOMException, IOException
   {
	  System.out.println("entered getSvgMaison");
	   
	  SvgMaison svgMaison;
	  ArrayList<Trajectoire> tab;
	  Element racine;
	  org.jdom2.Document document = null;

      SAXBuilder sxb = new SAXBuilder();
      document = sxb.build(new File(filePath));

      racine = document.getRootElement();

      tab=extractTrajectories(new ArrayList<Trajectoire>(), racine);
      
      svgMaison = new SvgMaison(tab);
      

      
      
      return svgMaison;
   }
   
   public static void main(String[] args)
   {
	   JDOM2 jdom = new JDOM2();
	   try
	   {
		SvgMaison svgm = jdom.getSvgMaison("C:\\Users\\Nathan\\Desktop\\testRect.svg");
		
	    System.out.println("affichage du svgMaison:");
	    int i = 0;   
	    for(Trajectoire t : svgm.getTrajectoires()){
	    	
	    	System.out.println("trajectoire n°" + i + "test de type " + t.getType());
	    		    	
	    	for(Vector2 v : t.getCourbe()){
	    		
	    		v.afficher();
	    		
	    	}
	    	
	    	i++;
	    }
		
	   }
	   catch (JDOMException e) {e.printStackTrace();}
	   catch (IOException e) {e.printStackTrace();}  
   }

   private ArrayList<Trajectoire> extractTrajectories(ArrayList<Trajectoire> trajectories, Element racineActuel)
   {
	  System.out.println("entered extractTrajectories");

      List<Element> listElements = racineActuel.getChildren();

      Iterator<Element> i = listElements.iterator();
      while(i.hasNext())
      {
         Element courant = (Element)i.next();
         courant.getChildren();
         
         if(courant.getName().equals("g"))
         {
        	 System.out.println("about to enter extractTrajectories of the G");
        	 trajectories = extractTrajectories(trajectories, courant);
         }
         else if(courant.getName().equals("rect"))
         {
        	 
        	System.out.println("found a rect");

        	 
        	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
        	 
        	int _x = Integer.parseInt(courant.getAttributeValue("x"));
            int _y = Integer.parseInt(courant.getAttributeValue("y"));
            int _width = Integer.parseInt(courant.getAttributeValue("width"));
            int _height = Integer.parseInt(courant.getAttributeValue("height"));
         	
         	arrayTemp.add(new Vector2(_x ,_y));
         	arrayTemp.add(new Vector2(_x + _width,_y));
         	arrayTemp.add(new Vector2(_x + _width,_y+_height));
         	arrayTemp.add(new Vector2(_x, _y+_height));
         	arrayTemp.add(new Vector2(_x, _y));
         	
         	trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
         }
         else if(courant.getName().equals("circle"))
         {
          	System.out.println("circle not implemented yet");

        	//TODO
        	 ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
         else if(courant.getName().equals("ellipse"))
         {
         	System.out.println("ellipse not implemented yet");

        	 //TODO
        	 ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
         else if(courant.getName().equals("line"))
         {
          	System.out.println("line not implemented yet");

        	//TODO
        	 ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
         else if(courant.getName().equals("polyline"))
         {
          	System.out.println("polyline not implemented yet");

        	//TODO
        	 ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
         else if(courant.getName().equals("polygon"))
         {
          	System.out.println("polygon not implemented yet");

        	//TODO
        	 ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
         else if(courant.getName().equals("path"))
         {
          	System.out.println("path not implemented yet");

        	//TODO
        	 ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
      }

      System.out.println("returning from extractTrajectories");

      return trajectories;
   }
}
