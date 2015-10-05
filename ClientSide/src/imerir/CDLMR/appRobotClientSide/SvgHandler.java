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
	  System.out.println("entered createSvgMaison");

	  SvgMaison svgMaison;
	  ArrayList<Trajectoire> tab;
	  Element racine;

	  System.out.println("1");

	  org.jdom2.Document document = null;
	  System.out.println("2");

      SAXBuilder sxb = new SAXBuilder();
	  System.out.println("3");

      document = sxb.build(new File(filePath));
	  System.out.println("4");

      racine = document.getRootElement();
	  System.out.println("5");

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
          	System.out.println("found a circle");

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
        	 System.out.println("found an ellipse");

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
        	System.out.println("found a line");

          	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();

        	int _x1 = Integer.parseInt(courant.getAttributeValue("x1"));
            int _y1 = Integer.parseInt(courant.getAttributeValue("y1"));
            int _x2 = Integer.parseInt(courant.getAttributeValue("x2"));
            int _y2 = Integer.parseInt(courant.getAttributeValue("y2"));

         	arrayTemp.add(new Vector2(_x1 ,_y1));
         	arrayTemp.add(new Vector2(_x2,_y2));

         	trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
         }
         else if(courant.getName().equals("polyline"))
         {
        	System.out.println("found polyline");

          	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
          	
          	String _points = courant.getAttributeValue("points");
          	String[] tabLocal = _points.split(" ");
          	String[] tabLocalTemp;
          	
          	int cpt=0;
          	
          	while(cpt<tabLocal.length)
          	{
          		tabLocalTemp=tabLocal[cpt].split(",");
          		arrayTemp.add(new Vector2(Integer.parseInt(tabLocalTemp[0]) ,Integer.parseInt(tabLocalTemp[1])));
          		cpt=cpt+1;
          	}
          	

          	trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
         	
        	//ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
         else if(courant.getName().equals("polygon"))
         {
          	System.out.println("found polygon");
          	
          	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
          	
          	String _points = courant.getAttributeValue("points");
          	String[] tabLocal = _points.split(" ");
          	String[] tabLocalTemp;
          	
          	int cpt=0;
          	
          	while(cpt<tabLocal.length)
          	{
          		tabLocalTemp=tabLocal[cpt].split(",");
          		arrayTemp.add(new Vector2(Integer.parseInt(tabLocalTemp[0]) ,Integer.parseInt(tabLocalTemp[1])));
          		cpt=cpt+1;
          	}
          	
          	tabLocalTemp=tabLocal[0].split(",");
      		arrayTemp.add(new Vector2(Integer.parseInt(tabLocalTemp[0]) ,Integer.parseInt(tabLocalTemp[1])));
          	

          	trajectories.add(new Trajectoire(Type.LINE, arrayTemp));

        	//ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
         else if(courant.getName().equals("path"))
         {
          	System.out.println("found path");

          	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
          	
          	String _points = courant.getAttributeValue("d");
          	String[] tabLocal = _points.split(" ");
          	String[] tabLocalTemp;
          	int xM=0;
          	int yM=0;
          	int xRef=0;
          	int yRef=0;
          	
          	int cpt=0;
          	
          	while(cpt<tabLocal.length)
          	{
          		tabLocalTemp=tabLocal[cpt].split(",");
          		if(tabLocalTemp[0].charAt(0)=='M')
          		{
          			xM=xRef=Integer.parseInt(tabLocalTemp[0].substring(1));
          			yM=yRef=Integer.parseInt(tabLocalTemp[1]);
          		}
          		else if(tabLocalTemp[0].charAt(0)=='m')
          		{
          			xM=xRef=Integer.parseInt(xRef+tabLocalTemp[0].substring(1));
          			yM=yRef=Integer.parseInt(yRef+tabLocalTemp[1]);
          		}
          		else if(tabLocalTemp[0].charAt(0)=='L')
          		{
          			arrayTemp.add(new Vector2(xRef ,yRef));
          			arrayTemp.add(new Vector2(Integer.parseInt(tabLocalTemp[0].substring(1)) ,Integer.parseInt(tabLocalTemp[1])));
          			xRef=Integer.parseInt(xRef+tabLocalTemp[0].substring(1));
          			yRef=Integer.parseInt(yRef+tabLocalTemp[1]);
          		}
          		else if(tabLocalTemp[0].charAt(0)=='l')
          		{
          			arrayTemp.add(new Vector2(xRef ,yRef));
          			arrayTemp.add(new Vector2(Integer.parseInt(tabLocalTemp[0].substring(1))+xRef ,Integer.parseInt(tabLocalTemp[1])+yRef));
          			xRef=Integer.parseInt(xRef+tabLocalTemp[0].substring(1));
          			yRef=Integer.parseInt(yRef+tabLocalTemp[1]);
          		}
          		else if(tabLocalTemp[0].charAt(0)=='H')
          		{
          			arrayTemp.add(new Vector2(xRef ,yRef));
          			arrayTemp.add(new Vector2(Integer.parseInt(tabLocalTemp[0].substring(1)) ,yRef));
          			xRef=Integer.parseInt(tabLocalTemp[0].substring(1));
          		}
          		else if(tabLocalTemp[0].charAt(0)=='h')
          		{
          			arrayTemp.add(new Vector2(xRef ,yRef));
          			arrayTemp.add(new Vector2(Integer.parseInt(tabLocalTemp[0].substring(1))+xRef ,yRef));
          			xRef=Integer.parseInt(tabLocalTemp[0].substring(1)+xRef);
          		}
          		else if(tabLocalTemp[0].charAt(0)=='V')
          		{
          			arrayTemp.add(new Vector2(xRef ,yRef));
          			arrayTemp.add(new Vector2(xRef ,Integer.parseInt(tabLocalTemp[1])));
          			yRef=Integer.parseInt(tabLocalTemp[1]);
          		}
          		else if(tabLocalTemp[0].charAt(0)=='v')
          		{
          			arrayTemp.add(new Vector2(xRef ,yRef));
          			arrayTemp.add(new Vector2(xRef ,Integer.parseInt(tabLocalTemp[1])+yRef));
          			yRef=Integer.parseInt(tabLocalTemp[1])+yRef;
          		}
          		else if(tabLocalTemp[0].charAt(0)=='Z' || tabLocalTemp[0].charAt(0)=='z')
          		{
          			arrayTemp.add(new Vector2(xRef ,yRef));
          			arrayTemp.add(new Vector2(yM ,xM));
          		}
          		else if(tabLocalTemp[0].charAt(0)=='q')
          		{
          			trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
          			arrayTemp = new ArrayList<Vector2>();
          			int axTemp = xRef;
          			int ayTemp = yRef;
          			int bxTemp = Integer.parseInt(tabLocalTemp[0].substring(1))+xRef;
          			int byTemp = Integer.parseInt(tabLocalTemp[0]+yRef);
          			cpt++;
          			tabLocalTemp=tabLocal[cpt].split(",");
          			int cxTemp = Integer.parseInt(tabLocalTemp[0].substring(1))+xRef;
          			int cyTemp = Integer.parseInt(tabLocalTemp[0]+yRef);
          			
          			float uax = (axTemp-bxTemp)/50;
          			float uay = (ayTemp-byTemp)/50;
          			float ucx = (cxTemp-bxTemp)/50;
          			float ucy = (cyTemp-byTemp)/50;
          			
          			int cptTemp=1;
          			float p1x;
          			float p1y;
          			float p2x;
          			float p2y;
          			float fragPx;
          			float fragPy;
          			
          			arrayTemp.add((new Vector2(cxTemp ,cyTemp)));
          			
          			while(cptTemp<49)
          			{
          				
          				p1x=axTemp+uax*cpt;
          				p1y=ayTemp+uay*cpt;
          				p2x=axTemp+ucx*cpt;
          				p2y=byTemp+ucy*cpt;
          				
          				fragPx=(p1x-p2x)/50;
          				fragPy=(p1y-p2y)/50;
              			
              			arrayTemp.add((new Vector2((int)(p1x+fragPx*cpt) ,(int)(p1y+fragPy*cpt))));
          				
          				cptTemp++;
          			}
          			
          			arrayTemp.add(new Vector2(Integer.parseInt(tabLocalTemp[0].substring(1)) ,Integer.parseInt(tabLocalTemp[1])));
          			
          			trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
          			arrayTemp = new ArrayList<Vector2>();
          			
          			xRef=cxTemp;
          			yRef=cyTemp;
          		}
          		else if(tabLocalTemp[0].charAt(0)=='Q')
          		{
          			trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
          			arrayTemp = new ArrayList<Vector2>();
          			int axTemp = xRef;
          			int ayTemp = yRef;
          			int bxTemp = Integer.parseInt(tabLocalTemp[0].substring(1));
          			int byTemp = Integer.parseInt(tabLocalTemp[0]);
          			cpt++;
          			tabLocalTemp=tabLocal[cpt].split(",");
          			int cxTemp = Integer.parseInt(tabLocalTemp[0].substring(1));
          			int cyTemp = Integer.parseInt(tabLocalTemp[0]);
          			
          			float uax = (axTemp-bxTemp)/50;
          			float uay = (ayTemp-byTemp)/50;
          			float ucx = (cxTemp-bxTemp)/50;
          			float ucy = (cyTemp-byTemp)/50;
          			
          			int cptTemp=1;
          			float p1x;
          			float p1y;
          			float p2x;
          			float p2y;
          			float fragPx;
          			float fragPy;
          			
          			arrayTemp.add((new Vector2(cxTemp ,cyTemp)));
          			
          			while(cptTemp<49)
          			{
          				
          				p1x=axTemp+uax*cpt;
          				p1y=ayTemp+uay*cpt;
          				p2x=axTemp+ucx*cpt;
          				p2y=byTemp+ucy*cpt;
          				
          				fragPx=(p1x-p2x)/50;
          				fragPy=(p1y-p2y)/50;
              			
              			arrayTemp.add((new Vector2((int)(p1x+fragPx*cpt) ,(int)(p1y+fragPy*cpt))));
          				
          				cptTemp++;
          			}
          			
          			arrayTemp.add(new Vector2(Integer.parseInt(tabLocalTemp[0].substring(1)) ,Integer.parseInt(tabLocalTemp[1])));
          			
          			trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
          			arrayTemp = new ArrayList<Vector2>();
          			
          			xRef=cxTemp;
          			yRef=cyTemp;
          		}
          		else
          		{
          			arrayTemp.add(new Vector2(Integer.parseInt(tabLocalTemp[0])+xRef ,Integer.parseInt(tabLocalTemp[1])+yRef));
          		}
          		cpt=cpt+1;
          	}
          	

          	trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
          	
        	//ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
      }

      System.out.println("returning from extractTrajectories");

      return trajectories;
   }

   /*
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
	*/
}
