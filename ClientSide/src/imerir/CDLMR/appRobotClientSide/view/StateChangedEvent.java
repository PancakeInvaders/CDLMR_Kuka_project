package imerir.CDLMR.appRobotClientSide.view;

import java.util.EventObject;

public class StateChangedEvent extends EventObject{

	/**
	 *
	 */
	private static final long serialVersionUID = 4015739971188465220L;

	private int newState;

	public StateChangedEvent(Object source, int newState){
		super(source);

		this.newState = newState;
	}

	public int getNewState() {
		return newState;
	}

}
