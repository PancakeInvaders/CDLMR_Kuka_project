public class BezierCurve {

	Vector2 points[];
	
	BezierCurve(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4)
	{
		points = new Vector2[4];
		
	    points[0] = p1;
	    points[1] = p2;
	    points[2] = p3;
	    points[3] = p4;
	}

	Vector2 get(double step)
	{
	    double minusStep3 = Math.pow(1-step, 3);
	    double minusStep2 = Math.pow(1-step, 2);
	    
	    double step3 = Math.pow(step, 3);
	    double step2 = Math.pow(step, 2);
	    
	    double _3minusStep = 3 * step * minusStep2;
	    double _3step2 = 3 * step2 * (1-step);
	    
	    
	    Vector2 result = new Vector2();
	    
	    result.x = minusStep3 * points[0].x + _3minusStep * points[1].x
	        + _3step2 * points[2].x + step3 * points[3].x;
	    
	    result.y = minusStep3 * points[0].y + _3minusStep * points[1].y
	        + _3step2 * points[2].y + step3 * points[3].y;
	    	    
	    return result;
	}
	
	Vector2[] getTrajectory(int nbPoint)
	{
		double step = (1.0 / ((double)nbPoint + 0.0));

		double[] stepper = new double[nbPoint];
		stepper[0] = 0.0;

		for (int iter = 1; iter < nbPoint; ++iter)
		{
			stepper[iter] = stepper[iter - 1] + step;
		}

		Vector2[] intermediary = new Vector2[nbPoint+1];

		int iter;
		for (iter = 0; iter < nbPoint; ++iter)
		{
			intermediary[iter] = get(stepper[iter]);
		}
		
		intermediary[iter] = points[3];

		return intermediary;
	}
	
	  public static void main(String[] args) {
		  Vector2 p1 = new Vector2(1,1);
		  Vector2 p2 = new Vector2(1,10);
		  Vector2 p3 = new Vector2(10,10);
		  Vector2 p4 = new Vector2(10,1);
		  BezierCurve curve = new BezierCurve(p1,p2,p3,p4);
		  
		  
		  Vector2 pget = new Vector2();
		  pget = curve.get(1.5);
		  //pget.afficher();
		  
		  Vector2[] pTrajectoire;
		  pTrajectoire = curve.getTrajectory(10);
		  
		  for (int i = 0; i < pTrajectoire.length; i++) {
			  pTrajectoire[i].afficher();
		}
		  

	  }
}
