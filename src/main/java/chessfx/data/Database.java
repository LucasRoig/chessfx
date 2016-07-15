package chessfx.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import chessfx.core.ChessColors;
import chessfx.core.game.IOpening;

public class Database implements Serializable {
	private List<ChangeListener> listenerList = new ArrayList<>();
	private static final long serialVersionUID = 1L;
	private Hashtable<Integer ,IOpening> openings = new Hashtable<>();
	private int nextOpeningId = 0;

	public Collection<IOpening> getOpenings() {
		return openings.values();
	}

	public void addOpening(IOpening o) {
		o.setId(this.nextOpeningId);
		++this.nextOpeningId;
		openings.put(o.getId(), o);
	}
	
	public void removeOpening(IOpening o){
		openings.remove(o.getId());
	}
}
