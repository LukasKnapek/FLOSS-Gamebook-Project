package mabufudyne.gbdesigner.gui;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import mabufudyne.gbdesigner.core.StoryPiece;
import mabufudyne.gbdesigner.core.StoryPieceManager;

public class CanvasPainter {
	
	private static ArrayList<ArrayList<StoryPiece>> hierarchyModel;
	private static HashMap<StoryPiece, StoryPieceFigure> spShapes;
		
	public static void paintVisual(FigureCanvas canvas, Figure canvasRoot, XYLayout canvasLayout) {
				
		StoryPieceManager.getInstance().sortStoryPieces();
		ArrayList<StoryPiece> storyPieces = StoryPieceManager.getInstance().getAllStoryPieces();
		ArrayList<StoryPiece> drawnStoryPieces = new ArrayList<>();
		
		hierarchyModel = new ArrayList<ArrayList<StoryPiece>>();
		spShapes = new HashMap<StoryPiece, StoryPieceFigure>();
		
		StoryPiece firstSP = storyPieces.get(0);
		createVisualModel(firstSP, 0, drawnStoryPieces);
		
		int yStart = 10;
		for (ArrayList<StoryPiece> level : hierarchyModel) {
			StoryPieceFigureContainer container = new StoryPieceFigureContainer();
			for (StoryPiece sp : level) {
				
				StoryPieceFigure spShape = spShapes.get(sp);
				container.add(spShapes.get(sp));

				for (StoryPiece choice : sp.getChoicesTexts().keySet()) {
					connectFigures(spShape, spShapes.get(choice), canvasRoot);
				}
				
			}
			canvasRoot.add(container, new Rectangle(0, yStart,canvas.getClientArea().width, -1));
			yStart += 70;
		}
	}
	
	private static void createVisualModel(StoryPiece sp, int level, ArrayList<StoryPiece> drawnStoryPieces) {
		ArrayList<StoryPiece> hierarchyLevel = null;
		if (!drawnStoryPieces.contains(sp)) {
			try {
				hierarchyLevel = hierarchyModel.get(level);
			}
			catch(IndexOutOfBoundsException e) {
				hierarchyLevel = new ArrayList<StoryPiece>();
				hierarchyModel.add(level, hierarchyLevel);
			}

			hierarchyLevel.add(sp);
			StoryPieceFigure figure = new StoryPieceFigure(sp.getOrder() + ". " + sp.getTitle());
			spShapes.put(sp, figure);
			drawnStoryPieces.add(sp);
		}
		else {
			return;
		}
		for (StoryPiece choice : sp.getChoicesTexts().keySet()) {
			createVisualModel(choice, level + 1, drawnStoryPieces);
		}
	}
	
	private static void connectFigures(IFigure source, IFigure target, Figure canvas) {
		
		PolylineConnection c = new PolylineConnection();
		ChopboxAnchor sourceAnchor = new ChopboxAnchor(source);
		ChopboxAnchor targetAnchor = new ChopboxAnchor(target);
		c.setSourceAnchor(sourceAnchor);
		c.setTargetAnchor(targetAnchor);
		
		PolygonDecoration decoration = new PolygonDecoration();
		c.setTargetDecoration(decoration);

		canvas.add(c);
	}
	
}
