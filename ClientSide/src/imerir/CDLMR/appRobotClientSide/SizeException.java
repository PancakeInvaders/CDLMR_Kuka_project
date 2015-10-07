package imerir.CDLMR.appRobotClientSide;

public class SizeException extends Exception
{

	/* Classe perméttant la levée d'une exception si la taille du dessin es trop importante
	 *
	 */
	private static final long serialVersionUID = 1423189028158230336L;
	
	public SizeException(String jesuisunechaine)
	{
		super(jesuisunechaine);
	}
}
