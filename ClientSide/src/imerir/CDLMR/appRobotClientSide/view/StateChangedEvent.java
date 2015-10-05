package imerir.CDLMR.appRobotClientSide.view;

import java.util.EventObject;

import imerir.CDLMR.appRobotClientSide.model.Etat;

public class StateChangedEvent extends EventObject{

	/**
	 *
	 */
	private static final long serialVersionUID = 4015739971188465220L;

	private Etat newState;

	public StateChangedEvent(Object source, Etat etat){
		super(source);

		this.newState = etat;
	}

	public Etat getNewState() {
		return newState;
	}

}
