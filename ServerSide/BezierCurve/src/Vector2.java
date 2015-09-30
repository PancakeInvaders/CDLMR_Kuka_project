

public class Vector2 {

	public double x;
	public double y;
	
	Vector2()
	{
		x = 0;
		y = 0;
	}
	
	Vector2(int _x, int _y)
	{
		this.x = _x;
		this.y = _y;
	}
	
	public void afficher(){
		System.out.println("x=" + this.x + "     y=" +this.y);
	}
	
}
