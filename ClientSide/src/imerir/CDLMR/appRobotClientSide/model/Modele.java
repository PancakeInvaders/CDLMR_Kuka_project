package imerir.CDLMR.appRobotClientSide.model;

import javax.swing.event.EventListenerList;

import imerir.CDLMR.appRobotClientSide.view.StateChangedEvent;
import imerir.CDLMR.appRobotClientSide.view.StateListener;
import imerir.CDLMR.trajectoire.*;
import imerir.CDLMR.trajectoire.Trajectoire.Type;

public class Modele
{
	private EventListenerList listeners;

	private Etat etat;

	public Modele()
	{
		listeners = new EventListenerList();
		this.etat=new Etat();

		Trajectoire t = new Trajectoire( Type.LINE , _courbe)
	}

	public int getEtat()
	{
		return this.etat.getEtat();
	}

	public void setEtat(int n)
	{
		System.out.println("entered setEtat");

		this.etat.setEtat(n);
		fireStateChanged();

	}

	private void fireStateChanged() {

		System.out.println("entered fireStateChanged");


		// TODO Auto-generated method stub
		StateListener[] listenerList = (StateListener[])listeners.getListeners(StateListener.class);

		for(StateListener listener : listenerList){

			System.out.println(" in the foreach");


			listener.etatChanged(new StateChangedEvent(this, getEtat()));
		}

	}

	public void addStateListener(StateListener stl){

		listeners.add( StateListener.class, stl);
	}

}
