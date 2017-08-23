package flibs.util;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class ActionFactory {
	public static Action basic(Runnable r) {
		return new Action() {
			public void actionPerformed(ActionEvent e) {r.run();}
			public void setEnabled(boolean b) {}
			public void removePropertyChangeListener(PropertyChangeListener listener) {}
			public void putValue(String key, Object value) {}
			public boolean isEnabled() {return true;}
			public Object getValue(String key) {return null;}
			public void addPropertyChangeListener(PropertyChangeListener listener) {}
		};
	}
	
	public static void addActionToKeyStroke(JComponent comp, String name, String stroke, Runnable action) {
		comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(stroke), name);
		comp.getActionMap().put(name, ActionFactory.basic( () -> action.run() ));
	}
}
