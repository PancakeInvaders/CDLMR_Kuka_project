package imerir.CDLMR.appRobotClientSide;

import java.io.*;

import org.jdom2.*;
import org.jdom2.input.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import imerir.CDLMR.trajectoire.*;
import imerir.CDLMR.trajectoire.Trajectoire.Type;

public class SvgHandler
{
   public SvgMaison createSvgMaison(String filePath) throws JDOMException, IOException
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
	   SvgHandler jdom = new SvgHandler();
	   try
	   {
		SvgMaison svgm = jdom.createSvgMaison("C:\\Users\\Nathan\\Desktop\\testRect.svg");

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

          	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();

        	int _cx = Integer.parseInt(courant.getAttributeValue("cx"));
            int _cy = Integer.parseInt(courant.getAttributeValue("cy"));
            int _r = Integer.parseInt(courant.getAttributeValue("r"));

         	arrayTemp.add(new Vector2(_cx-_r ,_cy));
         	arrayTemp.add(new Vector2(_cx,_cy+_r));
         	arrayTemp.add(new Vector2(_cx+_r ,_cy));

         	trajectories.add(new Trajectoire(Type.CIRCLE, arrayTemp));
         	
         	arrayTemp = new ArrayList<Vector2>();

         	arrayTemp.add(new Vector2(_cx+_r ,_cy));
         	arrayTemp.add(new Vector2(_cx,_cy-_r));
         	arrayTemp.add(new Vector2(_cx-_r ,_cy));

         	trajectories.add(new Trajectoire(Type.CIRCLE, arrayTemp));
         }
         else if(courant.getName().equals("ellipse"))
         {
         	System.out.println("ellipse not implemented yet");

         	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();

        	int _cx = Integer.parseInt(courant.getAttributeValue("cx"));
            int _cy = Integer.parseInt(courant.getAttributeValue("cy"));
            int _rx = Integer.parseInt(courant.getAttributeValue("rx"));
            int _ry = Integer.parseInt(courant.getAttributeValue("ry"));

         	arrayTemp.add(new Vector2(_cx-_rx ,_cy));
         	arrayTemp.add(new Vector2(_cx,_cy+_ry));
         	arrayTemp.add(new Vector2(_cx+_rx ,_cy));

         	trajectories.add(new Trajectoire(Type.CIRCLE, arrayTemp));
         	
         	arrayTemp = new ArrayList<Vector2>();

         	arrayTemp.add(new Vector2(_cx+_rx ,_cy));
         	arrayTemp.add(new Vector2(_cx,_cy-_ry));
         	arrayTemp.add(new Vector2(_cx-_rx ,_cy));

         	trajectories.add(new Trajectoire(Type.CIRCLE, arrayTemp));
         }
         else if(courant.getName().equals("line"))
         {
          	System.out.println("line not implemented yet");

          	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();

        	int _x1 = Integer.parseInt(courant.getAttributeValue("x1"));
            int _y1 = Integer.parseInt(courant.getAttributeValue("y1"));
            int _x2 = Integer.parseInt(courant.getAttributeValue("x2"));
            int _y2 = Integer.parseInt(courant.getAttributeValue("y2"));

         	arrayTemp.add(new Vector2(_x1 ,_y1));
         	arrayTemp.add(new Vector2(_x2,_y2));

         	trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
         	
        	//ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
         else if(courant.getName().equals("polyline"))
         {
          	System.out.println("polyline not implemented yet");

        	//TODO
        	//ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
         else if(courant.getName().equals("polygon"))
         {
          	System.out.println("polygon not implemented yet");

        	//TODO
        	//ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
         else if(courant.getName().equals("path"))
         {
          	System.out.println("path not implemented yet");

        	//TODO
        	//ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
      }

      System.out.println("returning from extractTrajectories");

      return trajectories;
   }

   /**
	 * l'appli va creer et renvoyer un objet SvgMaison
	 *
	 * @param cheminFichier
	 */
	public SvgMaison creationSvgMaison(String cheminFichier)
	{
		System.out.println("entered creationSvgMaison");


		ArrayList<Trajectoire> trajectoires = new ArrayList<Trajectoire>();

		// remplir trajectoires à partir du fichier

		// TEMP -----------

		ArrayList<Vector2> al = new ArrayList<Vector2>();
		al.add(new Vector2(0, 0));
		al.add(new Vector2(1, 0));
		al.add(new Vector2(0, 1));
		al.add(new Vector2(2, 2));

		trajectoires.add(new Trajectoire(Type.LINE, al ));

		al = new ArrayList<Vector2>();
		al.add(new Vector2(0, 0));
		al.add(new Vector2(1, 3));
		al.add(new Vector2(3, 1));
		al.add(new Vector2(5, 5));

		trajectoires.add(new Trajectoire(Type.SPLINE, al ));

		// ---------------


		SvgMaison sm = new SvgMaison(trajectoires);

		return sm;
	}
}
