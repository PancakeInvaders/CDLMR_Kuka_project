package imerir.CDLMR.appRobotClientSide;

import java.util.ArrayList;

import imerir.CDLMR.trajectoire.SvgMaison;
import imerir.CDLMR.trajectoire.Trajectoire;
import imerir.CDLMR.trajectoire.Trajectoire.Type;
import imerir.CDLMR.trajectoire.Vector2;

public class SvgHandler
{
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
