package com.modintro.restfulclient.model;

/**
 * UpdateListener is notified when a TableModel
 * is updated. Uses the Observer/Observable design
 * pattern.
 * 
 * The TableModel and JTable relationship doens't
 * allow for intercepting table updates through
 * the JTable. This interface allows the for updates
 * to be processed before modifying the table model.
 * 
 * @author Craig Spencer <craigspencer@modintro.com>
 *
 */
public interface UpdateListener {
	public void update(Object value, int row, int col);
}
