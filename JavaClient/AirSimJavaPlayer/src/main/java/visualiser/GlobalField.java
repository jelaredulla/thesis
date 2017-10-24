package visualiser;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.util.*;

public class GlobalField extends JPanel implements KeyListener {
	
	private int panelWidth;
	private int panelHeight;
	
	private double boundary;
	private double captureL;
	
	public final static double headerAngle = Math.PI/18;
	public final static double headerLength = 1;

	// current positions and poses
	private Point2D pPos = null;
	private double pTheta;
	private Point2D ePos = null;
	private double eTheta;
	
	private HashMap<String, List<Line2D>> pursuerPaths = new HashMap<String, List<Line2D>>();
	//private List<Line2D> pursuerPath = new ArrayList<Line2D>();
	private List<Line2D> evaderPath = new ArrayList<Line2D>();
	
	private JFrame frame;
	
	private char keyPressed = 0;

	/**
	 * Constructor
	 */
	public GlobalField(int panelSize, double b, double l) {
		panelWidth = panelSize;
		panelHeight = panelSize;
		boundary = b;
		captureL = l;
		
		frame = new JFrame("Playing Field");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(panelWidth, panelHeight);
		frame.add(this);
		frame.addKeyListener(this);
		frame.setVisible(true);
	}
	
	public int scaleWorldToPanelWidth(double value) {
		return (int) ((panelWidth*(value+boundary))/(2*boundary));
	}
	
	public int scaleWorldToPanelHeight(double value) {
		return (int) ((panelHeight*(value+boundary))/(2*boundary));
	}
	
	/**
	 * Sets pursuer segments to draw
	 * @param input List of Point2D
	 */
	public void setPursuerPath(List<Point2D> input, String name) {
		if (!pursuerPaths.containsKey(name)) {
			pursuerPaths.put(name, new ArrayList<Line2D>());
		}
		
		List<Line2D> path = pursuerPaths.get(name);
		
		clearPursuer(name);
		
		for (int i = 0; i < input.size() - 1; i++) {
			path.add(new Line2D.Float(input.get(i), input.get(i + 1)));
		}
	}
	
	/**
	 * Sets evader segments to draw
	 * @param input List of Point2D
	 */
	public void setEvaderPath(List<Point2D> input) {
		clearEvader();
		for (int i = 0; i < input.size() - 1; i++) {
			evaderPath.add(new Line2D.Float(input.get(i), input.get(i + 1)));
		}
	}

	/**
	 * Adds pursuer segment to draw, coordinates between 0 and 1
	 * @param input Line2D
	 */
	public void addPursuerSegment(Line2D input, String name) {
		if (!pursuerPaths.containsKey(name)) {
			pursuerPaths.put(name, new ArrayList<Line2D>());
		}
		
		pursuerPaths.get(name).add(input);
	}
	
	/**
	 * Adds evader segment to draw, coordinates between 0 and 1
	 * @param input Line2D
	 */
	public void addEvaderSegment(Line2D input) {
		evaderPath.add(input);
	}
	
	public void setPursuerState(Point2D pos, double d) {
		pPos = pos;
		pTheta = d;
	}
	
	public void setEvaderState(Point2D pos, double theta) {
		ePos = pos;
		eTheta = theta;
	}


	public void clearAll() {
		for (String name : pursuerPaths.keySet()) {
			clearPursuer(name);
		}
		
		clearEvader();
		//pPos = null;
		//ePos = null;
	}
	
	public void clearPursuer(String name) {
		pursuerPaths.get(name).clear();
	}

	public void clearEvader() {
		evaderPath.clear();
	}

	/**
	 * Call this to refresh the window
	 */
	public void repaint() {
		if (frame != null) {
			frame.repaint();
		}
	}
	
	/**
	 * Overloaded JPanel method
	 */
	public void paint(Graphics g) {
		
		// Update size in case user has resized window
		panelHeight = this.getHeight();
		panelWidth = this.getWidth();
		
		// White background
		g.setColor(Color.gray);
		g.fillRect(0, 0, panelWidth, panelHeight);

		int x1, y1, x2, y2;

		
		// Draw evader path
		for (Line2D l : evaderPath) {
			x1 = scaleWorldToPanelWidth(l.getX1());
			y1 = scaleWorldToPanelHeight(l.getY1());
			x2 = scaleWorldToPanelWidth(l.getX2());
			y2 = scaleWorldToPanelHeight(l.getY2());
			g.setColor(Color.blue);
			g.drawLine(x1, panelHeight - y1, x2, panelHeight - y2);
		}
		
		for (List<Line2D> path : pursuerPaths.values()) {
			// Draw pursuer path
			for (Line2D l : path) {
				x1 = scaleWorldToPanelWidth(l.getX1());
				y1 = scaleWorldToPanelHeight(l.getY1());
				x2 = scaleWorldToPanelWidth(l.getX2());
				y2 = scaleWorldToPanelHeight(l.getY2());
				g.setColor(Color.red);
				g.drawLine(x1, panelHeight - y1, x2, panelHeight - y2);
			}
			
			// Draw pursuer capture radius
			if (!path.isEmpty()) {
				Line2D lastMovement = path.get(path.size() - 1);
				x2 = scaleWorldToPanelWidth(lastMovement.getX2());
				y2 = scaleWorldToPanelHeight(lastMovement.getY2());
				int rx = (int) ((captureL/(2*boundary)) * panelWidth); 
				int ry = (int) ((captureL/(2*boundary)) * panelHeight); 
				g.setColor(Color.orange);
				g.drawOval(x2 - rx, panelHeight - (y2 + ry), 2*rx, 2*ry);
			}
		}
			
		if (pPos != null) {
			g.setColor(Color.red);
			drawStateTriangle(pPos, pTheta, g);
		}
		
		if (ePos != null) {
			g.setColor(Color.blue);
			drawStateTriangle(ePos, eTheta, g);
		}
	}

	private void drawStateTriangle(Point2D pos, double pTheta2, Graphics g) {
		double x = pos.getX();
		double y = pos.getY();
	
		double[] xCoords = {x, x + headerLength*Math.sin(pTheta2 + Math.PI + headerAngle),
				x + headerLength*Math.sin(pTheta2 + Math.PI - headerAngle)};
		double[] yCoords = {y, y + headerLength*Math.cos(pTheta2 + Math.PI + headerAngle),
				y + headerLength*Math.cos(pTheta2 + Math.PI - headerAngle)};
		
		int[] xCoordsPixels = new int[3];
		int[] yCoordsPixels = new int[3];
		for (int i = 0; i < 3; i++) {
			xCoordsPixels[i] = scaleWorldToPanelWidth(xCoords[i]);
			yCoordsPixels[i] = panelHeight - scaleWorldToPanelHeight(yCoords[i]);
		}
		
		g.drawPolygon(xCoordsPixels, yCoordsPixels, 3);
	}
	
	/**
	 * Waits until user presses a key, then returns the key pressed
	 * @return key pressed as char
	 */
	public char waitKey() {
		keyPressed = 0;
		while (keyPressed == 0) {
			
			// Wait
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return keyPressed;		
	}
	
	/**
	 * Waits until user presses a key, then returns the key pressed
	 * Will return 0 if timeout occurs
	 * @param timeout Max time to wait in milliseconds
	 * @return key pressed as char
	 */
	public char waitKey(long timeout) {
		long currentTime = System.currentTimeMillis();
		long startTime = System.currentTimeMillis();
		keyPressed = 0;
		while (currentTime - startTime < timeout && keyPressed == 0) {
			
			// Wait
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentTime = System.currentTimeMillis();
		}
		return keyPressed;
	}
	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		keyPressed = e.getKeyChar();
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}