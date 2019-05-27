package de.etas.tef.config.listener;

public interface IActionListener
{
	void receivedAction(int type, int compositeID, Object content);
}
