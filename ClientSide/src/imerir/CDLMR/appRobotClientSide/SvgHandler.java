package imerir.CDLMR.appRobotClientSide;

//package imerir.CDLMR.appRobotClientSide;

import java.io.*;

import org.jdom2.*;
import org.jdom2.input.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import imerir.CDLMR.trajectoire.*;
import imerir.CDLMR.trajectoire.Trajectoire.Type;

public class SvgHandler
{
	// Cette fonction utilise la librairie JDOM afin de pouvoir lire le fichier XML et test la taille du fichier avant de lancer le traitement de son contenu.
   public SvgMaison createSvgMaison(String filePath) throws IOException, SizeException, JDOMException
   {
	  System.out.println("entered createSvgMaison");

	  SvgMaison svgMaison;
	  ArrayList<Trajectoire> tab;
	  Element racine;

	  System.out.println("1");
	  //Lecture du document XML
	  org.jdom2.Document document = null;
	  System.out.println("2");

      SAXBuilder sxb = new SAXBuilder();
	  System.out.println("3");

      document = sxb.build(new File(filePath));
	  System.out.println("4");

      racine = document.getRootElement();
	  System.out.println("5");

	  // Test de la taille du fichier
	  Pattern p = Pattern.compile("([0-9]+).*");
	  Matcher m = p.matcher("900mm");
	  System.out.println(m.matches());
	  System.out.println(m.group(1));

	  String strWidth = racine.getAttributeValue("width");
	  Matcher mwidth = p.matcher(strWidth);
	  System.out.println(strWidth);
	  System.out.println(mwidth.matches());
	  System.out.println(mwidth.group(1));

	  String strHeight = racine.getAttributeValue("height");
	  Matcher mheight = p.matcher(strHeight);
	  System.out.println(strHeight);
	  System.out.println(mheight.matches());
	  System.out.println(mheight.group(1));

	  if((int)Double.parseDouble(mwidth.group(1))>297 || (int)Double.parseDouble(mheight.group(1))>210)
	  {
		  System.out.println("too big");

		  throw(new SizeException("Size Out"));
	  }
	  
	  // Lancement de la généraration du code maison compréhenssible par le robot
      tab=extractTrajectories(new ArrayList<Trajectoire>(), racine, (int)Double.parseDouble(racine.getAttributeValue("height")));

      svgMaison = new SvgMaison(tab);

      return svgMaison;
   }

   /*public static void main(String[] args)
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
   }*/

   // Fonction "parser" de svg
   private ArrayList<Trajectoire> extractTrajectories(ArrayList<Trajectoire> trajectories, Element racineActuel, int hauteur)
   {
	  // La fonction récupère et traite chacunes des balises contenu dans la balise svg
	  System.out.println("entered extractTrajectories");

      List<Element> listElements = racineActuel.getChildren();

      // Pour chacune des balises contenu dans la balise svg
      Iterator<Element> i = listElements.iterator();
      while(i.hasNext())
      {
         Element courant = (Element)i.next();
         courant.getChildren();
         
         // Si le svg contient une balise g on va regarder ce que cette balise g contient
         if(courant.getName().equals("g"))
         {
        	 System.out.println("about to enter extractTrajectories of the G");
        	 trajectories = extractTrajectories(trajectories, courant, hauteur);
         }
         // La balise rect indique le dessin d'un rectangle
         else if(courant.getName().equals("rect"))
         {

        	System.out.println("found a rect");


        	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();

        	// On récupère les information contenu dans le fichier
        	int _x = (int)Double.parseDouble(courant.getAttributeValue("x"));
            int _y = (int)Double.parseDouble(courant.getAttributeValue("y"));
            int _width = (int)Double.parseDouble(courant.getAttributeValue("width"));
            int _height = (int)Double.parseDouble(courant.getAttributeValue("height"));
            
            // On calcule les quatres points et on s'y rend dans le bon ordre.
         	arrayTemp.add(new Vector2(_x ,hauteur-_y));
         	arrayTemp.add(new Vector2(_x + _width,hauteur-_y));
         	arrayTemp.add(new Vector2(_x + _width,hauteur-_y+_height));
         	arrayTemp.add(new Vector2(_x, hauteur-_y+_height));
         	// On retourne au premier point afin de fermer la figure.
         	arrayTemp.add(new Vector2(_x, hauteur-_y));

         	// Envois de la trajectoire au Robot
         	trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
         }
         /*else if(courant.getName().equals("circle"))
         {
          	System.out.println("found a circle");

          	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();

        	int _cx = (int)Double.parseDouble(courant.getAttributeValue("cx"));
            int _cy = (int)Double.parseDouble(courant.getAttributeValue("cy"));
            int _r = (int)Double.parseDouble(courant.getAttributeValue("r"));

         	arrayTemp.add(new Vector2(_cx-_r ,hauteur-_cy));
         	arrayTemp.add(new Vector2(_cx,hauteur-_cy+_r));
         	arrayTemp.add(new Vector2(_cx+_r ,hauteur-_cy));

         	trajectories.add(new Trajectoire(Type.CIRCLE, arrayTemp));

         	arrayTemp = new ArrayList<Vector2>();

         	arrayTemp.add(new Vector2(_cx+_r ,hauteur-_cy));
         	arrayTemp.add(new Vector2(_cx,hauteur-_cy-_r));
         	arrayTemp.add(new Vector2(_cx-_r ,hauteur-_cy));

         	trajectories.add(new Trajectoire(Type.CIRCLE, arrayTemp));
         }*/
         // La balise circle indique le dessin d'un cercle
         else if(courant.getName().equals("circle"))
         {
          	System.out.println("found a circle");

          	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
          	
          	// On récupère les information contenu dans le fichier
        	int _cx = (int)Double.parseDouble(courant.getAttributeValue("cx"));
            int _cy = (int)Double.parseDouble(courant.getAttributeValue("cy"));
            int _r = (int)Double.parseDouble(courant.getAttributeValue("r"));
            double alpha=0;
            double incr=Math.PI/180;
            
            // On calcule 360 points afin d'obtenir la forme du cercle.
            while(alpha<=Math.PI*2)
            {
            	arrayTemp.add(new Vector2((int)(_r*Math.cos(alpha)+_cx),hauteur-(int)(_r*Math.sin(alpha)+_cy)));

            	alpha=alpha+incr;
            }

            // Envois de la trajectoire au Robot
         	trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
         }
         /*else if(courant.getName().equals("ellipse"))
         {
        	 System.out.println("found an ellipse");

         	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();

        	int _cx = (int)Double.parseDouble(courant.getAttributeValue("cx"));
            int _cy = (int)Double.parseDouble(courant.getAttributeValue("cy"));
            int _rx = (int)Double.parseDouble(courant.getAttributeValue("rx"));
            int _ry = (int)Double.parseDouble(courant.getAttributeValue("ry"));

         	arrayTemp.add(new Vector2(_cx-_rx ,hauteur-_cy));
         	arrayTemp.add(new Vector2(_cx,hauteur-_cy+_ry));
         	arrayTemp.add(new Vector2(_cx+_rx ,hauteur-_cy));

         	trajectories.add(new Trajectoire(Type.CIRCLE, arrayTemp));

         	arrayTemp = new ArrayList<Vector2>();

         	arrayTemp.add(new Vector2(_cx+_rx ,hauteur-_cy));
         	arrayTemp.add(new Vector2(_cx,hauteur-_cy-_ry));
         	arrayTemp.add(new Vector2(_cx-_rx ,hauteur-_cy));

         	trajectories.add(new Trajectoire(Type.CIRCLE, arrayTemp));
         }*/
         // La balise ellipse indique le dessin d'une ellipse
         else if(courant.getName().equals("ellipse"))
         {
        	 System.out.println("found a ellipse");

           	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
           	
          	// On récupère les information contenu dans le fichier
           	int _cx = (int)Double.parseDouble(courant.getAttributeValue("cx"));
            int _cy = (int)Double.parseDouble(courant.getAttributeValue("cy"));
            int _rx = (int)Double.parseDouble(courant.getAttributeValue("rx"));
            int _ry = (int)Double.parseDouble(courant.getAttributeValue("ry"));
            double alpha=0;
            double incr=Math.PI/180;

            // On calcule 360 points afin d'obtenir la forme de l'ellipse.
            while(alpha<=Math.PI*2)
            {
            	arrayTemp.add(new Vector2((int)(_rx*Math.cos(alpha)+_cx),hauteur-(int)(_ry*Math.sin(alpha)+_cy)));

            	alpha=alpha+incr;
            }

            // Envois de la trajectoire au Robot
          	trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
         }
         // La balise line indique le dessin d'une ligne
         else if(courant.getName().equals("line"))
         {
        	System.out.println("found a line");

          	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();

          	// On récupère les information contenu dans le fichier
        	int _x1 = (int)Double.parseDouble(courant.getAttributeValue("x1"));
            int _y1 = (int)Double.parseDouble(courant.getAttributeValue("y1"));
            int _x2 = (int)Double.parseDouble(courant.getAttributeValue("x2"));
            int _y2 = (int)Double.parseDouble(courant.getAttributeValue("y2"));

            // On calcule les deux points afin d'obtenir la ligne.
         	arrayTemp.add(new Vector2(_x1 ,hauteur-_y1));
         	arrayTemp.add(new Vector2(_x2,hauteur-_y2));

         	// Envois de la trajectoire au Robot
         	trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
         }
         // La balise polyline indique le dessin d'une ligne brisée
         else if(courant.getName().equals("polyline"))
         {
        	System.out.println("found polyline");

          	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
          	// On récupère les information contenu dans le fichier
          	String _points = courant.getAttributeValue("points");
          	String[] tabLocal = _points.split(" ");
          	String[] tabLocalTemp;

            // On calcule les différent points afin d'obtenir la ligne brisée.
          	int cpt=0;

          	while(cpt<tabLocal.length)
          	{
          		tabLocalTemp=tabLocal[cpt].split(",");
          		arrayTemp.add(new Vector2((int)Double.parseDouble(tabLocalTemp[0]) ,hauteur-(int)Double.parseDouble(tabLocalTemp[1])));
          		cpt=cpt+1;
          	}

          	// Envois de la trajectoire au Robot
          	trajectories.add(new Trajectoire(Type.LINE, arrayTemp));

        	//ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
         // La balise polygon indique le dessin d'un polygone
         else if(courant.getName().equals("polygon"))
         {
          	System.out.println("found polygon");

          	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
		    
		     // On récupère les information contenu dans le fichier
          	String _points = courant.getAttributeValue("points");
          	String[] tabLocal = _points.split(" ");
          	String[] tabLocalTemp;

          	int cpt=0;
          	
          	// Calcule des points de la figure
          	while(cpt<tabLocal.length)
          	{
          		tabLocalTemp=tabLocal[cpt].split(",");
          		arrayTemp.add(new Vector2((int)Double.parseDouble(tabLocalTemp[0]) ,hauteur-(int)Double.parseDouble(tabLocalTemp[1])));
          		cpt=cpt+1;
          	}
          	
          	// Ajout du dernier point afin de fermer la figure
          	tabLocalTemp=tabLocal[0].split(",");
      		arrayTemp.add(new Vector2((int)Double.parseDouble(tabLocalTemp[0]) ,hauteur-(int)Double.parseDouble(tabLocalTemp[1])));

      		// Envois de la trajectoire au Robot
          	trajectories.add(new Trajectoire(Type.LINE, arrayTemp));

        	//ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
         }
         // Traitement de la balise path
         else if(courant.getName().equals("path"))
         {
          	System.out.println("found path");

          	ArrayList <Vector2> arrayTemp = new ArrayList<Vector2>();
          	
          	// Initialisation
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
          			System.out.println("M found");
          			xM=xRef=(int)Double.parseDouble(tabLocalTemp[0].substring(1)+xM);
          			yM=yRef=(int)Double.parseDouble(tabLocalTemp[1]+yM);
          		}
          		else if(tabLocalTemp[0].charAt(0)=='m')
          		{
          			xM=xRef=(int)Double.parseDouble(xRef+tabLocalTemp[0].substring(1)+xRef);
          			yM=yRef=(int)Double.parseDouble(yRef+tabLocalTemp[1]+yRef);
          		}
          		else if(tabLocalTemp[0].charAt(0)=='L')
          		{
          			arrayTemp.add(new Vector2(xRef ,hauteur+yRef));
          			arrayTemp.add(new Vector2((int)Double.parseDouble(tabLocalTemp[0].substring(1)) + xM ,hauteur-(int)Double.parseDouble(tabLocalTemp[1])+yM));
          			xRef=(int)Double.parseDouble(xRef+tabLocalTemp[0].substring(1));
          			yRef=(int)Double.parseDouble(yRef+tabLocalTemp[1]);
          		}
          		else if(tabLocalTemp[0].charAt(0)=='l')
          		{
          			arrayTemp.add(new Vector2(xRef ,hauteur+yRef));
          			arrayTemp.add(new Vector2((int)Double.parseDouble(tabLocalTemp[0].substring(1))+xRef ,hauteur-(int)Double.parseDouble(tabLocalTemp[1])+yRef));
          			xRef=(int)Double.parseDouble(xRef+tabLocalTemp[0].substring(1));
          			yRef=(int)Double.parseDouble(yRef+tabLocalTemp[1]);
          		}
          		else if(tabLocalTemp[0].charAt(0)=='H')
          		{
          			arrayTemp.add(new Vector2(xRef ,hauteur-yRef));
          			arrayTemp.add(new Vector2((int)Double.parseDouble(tabLocalTemp[0].substring(1))+xM ,hauteur-yRef));
          			xRef=(int)Double.parseDouble(tabLocalTemp[0].substring(1)+xM);
          		}
          		else if(tabLocalTemp[0].charAt(0)=='h')
          		{
          			arrayTemp.add(new Vector2(xRef ,hauteur-yRef));
          			arrayTemp.add(new Vector2((int)Double.parseDouble(tabLocalTemp[0].substring(1))+xRef ,hauteur-yRef));
          			xRef=(int)Double.parseDouble(tabLocalTemp[0].substring(1)+xRef);
          		}
          		else if(tabLocalTemp[0].charAt(0)=='V')
          		{
          			arrayTemp.add(new Vector2(xRef ,hauteur-yRef));
          			arrayTemp.add(new Vector2(xRef ,hauteur-(int)Double.parseDouble(tabLocalTemp[1])+yM));
          			yRef=(int)Double.parseDouble(tabLocalTemp[1])+yM;
          		}
          		else if(tabLocalTemp[0].charAt(0)=='v')
          		{
          			arrayTemp.add(new Vector2(xRef ,hauteur-yRef));
          			arrayTemp.add(new Vector2(xRef ,hauteur-(int)Double.parseDouble(tabLocalTemp[1])+yRef));
          			yRef=(int)Double.parseDouble(tabLocalTemp[1])+yRef;
          		}
          		else if(tabLocalTemp[0].charAt(0)=='Z' || tabLocalTemp[0].charAt(0)=='z')
          		{
          			arrayTemp.add(new Vector2(xRef ,hauteur-yRef));
          			arrayTemp.add(new Vector2(yM ,hauteur-xM));
          		}
          		else if(tabLocalTemp[0].charAt(0)=='q')
          		{
          			System.out.println("q found");
          			trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
          			arrayTemp = new ArrayList<Vector2>();
          			int axTemp = xRef;
          			int ayTemp = yRef;
          			int bxTemp = (int)Double.parseDouble(tabLocalTemp[0].substring(1))+xRef;
          			int byTemp = (int)Double.parseDouble(tabLocalTemp[0]+yRef);
          			cpt++;
          			tabLocalTemp=tabLocal[cpt].split(",");
          			int cxTemp = (int)Double.parseDouble(tabLocalTemp[0].substring(1))+xRef;
          			int cyTemp = (int)Double.parseDouble(tabLocalTemp[0]+yRef);

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

          			arrayTemp.add((new Vector2(cxTemp ,hauteur-cyTemp)));

          			while(cptTemp<49)
          			{

          				p1x=axTemp+uax*cpt;
          				p1y=ayTemp+uay*cpt;
          				p2x=axTemp+ucx*cpt;
          				p2y=byTemp+ucy*cpt;

          				fragPx=(p1x-p2x)/50;
          				fragPy=(p1y-p2y)/50;

              			arrayTemp.add((new Vector2((int)(p1x+fragPx*cpt) ,hauteur-(int)(p1y+fragPy*cpt))));

          				cptTemp++;
          			}

          			arrayTemp.add(new Vector2((int)Double.parseDouble(tabLocalTemp[0].substring(1)) ,hauteur-(int)Double.parseDouble(tabLocalTemp[1])));

          			trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
          			arrayTemp = new ArrayList<Vector2>();

          			xRef=cxTemp;
          			yRef=cyTemp;
          		}
          		else if(tabLocalTemp[0].charAt(0)=='Q')
          		{
          			System.out.println("Q found");

          			trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
          			arrayTemp = new ArrayList<Vector2>();
          			int axTemp = xRef;
          			int ayTemp = yRef;
          			int bxTemp = (int)Double.parseDouble(tabLocalTemp[0].substring(1)+xM);
          			int byTemp = (int)Double.parseDouble(tabLocalTemp[0]+yM);
          			cpt++;
          			tabLocalTemp=tabLocal[cpt].split(",");
          			int cxTemp = (int)Double.parseDouble(tabLocalTemp[0].substring(1)+xM);
          			int cyTemp = (int)Double.parseDouble(tabLocalTemp[0]+yM);

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

          			arrayTemp.add((new Vector2(cxTemp ,hauteur-cyTemp)));

          			while(cptTemp<49)
          			{

          				p1x=axTemp+uax*cpt;
          				p1y=ayTemp+uay*cpt;
          				p2x=axTemp+ucx*cpt;
          				p2y=byTemp+ucy*cpt;

          				fragPx=(p1x-p2x)/50;
          				fragPy=(p1y-p2y)/50;

              			arrayTemp.add((new Vector2((int)(p1x+fragPx*cpt) ,hauteur-(int)(p1y+fragPy*cpt))));

          				cptTemp++;
          			}

          			arrayTemp.add(new Vector2((int)Double.parseDouble(tabLocalTemp[0].substring(1)) ,hauteur-(int)Double.parseDouble(tabLocalTemp[1])));

          			trajectories.add(new Trajectoire(Type.LINE, arrayTemp));
          			arrayTemp = new ArrayList<Vector2>();

          			xRef=cxTemp;
          			yRef=cyTemp;
          		}
          		else
          		{
          			System.out.println("point found");
          			arrayTemp.add(new Vector2((int)Double.parseDouble(tabLocalTemp[0])+xRef ,hauteur-(int)Double.parseDouble(tabLocalTemp[1])+yRef));
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
