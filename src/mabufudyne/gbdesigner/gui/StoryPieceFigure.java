package mabufudyne.gbdesigner.gui;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public class StoryPieceFigure extends Figure{
	public static Color shapeColor = new Color(null, 255, 255, 255);
	
	public StoryPieceFigure(String title) {
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setBorder(new StoryPieceFigureBorder(2));
		setBackgroundColor(shapeColor);
		setOpaque(true);
		setFont(new Font(null, "Arial", 10, SWT.BOLD));
		
		Label lbTitle = new Label(title);
		add(lbTitle);
	}
	
	// Override LineBorder to set custom insets
	public class StoryPieceFigureBorder extends LineBorder {
		
		public StoryPieceFigureBorder(int i) {
			super(i);
		}

		@Override
		public Insets getInsets(IFigure figure) {
			return new Insets(5,5,5,5);
		}
		
	}
	
}

