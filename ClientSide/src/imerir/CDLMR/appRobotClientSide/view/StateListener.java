package imerir.CDLMR.appRobotClientSide.view;

import java.util.EventListener;

public interface StateListener extends EventListener
{
	void etatChanged(StateChangedEvent e);
}
