package plot;

import java.awt.Point;

public interface ArrowPaneObserver {
	
	public void endShoot(ArrowPane sender, Point end);
	
	public void repaintArrows();
	
	public void correctArrowhead(Arrowhead arrow, ArrowPane sender, ArrowPane receiver);

}
