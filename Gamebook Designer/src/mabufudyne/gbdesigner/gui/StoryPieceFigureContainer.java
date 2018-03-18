package mabufudyne.gbdesigner.gui;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.OrderedLayout;

public class StoryPieceFigureContainer extends Figure {
	
	public StoryPieceFigureContainer() {
		FlowLayout layout = new FlowLayout();
		setLayoutManager(layout);
		layout.setHorizontal(true);
		layout.setMajorAlignment(OrderedLayout.ALIGN_CENTER);
		layout.setMajorSpacing(20);
	}
}