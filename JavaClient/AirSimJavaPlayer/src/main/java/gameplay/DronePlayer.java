package gameplay;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import gameplay.AirSimStructures.Vector3r;


public class DronePlayer extends MultirotorClient implements Pursuer, Evader {
	public final static float dt = 20f;
	protected Vector3r position; // (x, y, z) position in fixed global frame
	protected double theta; // orientation in fixed global frame, wrt x-axis
	protected double maxV; // maximum velocity, m/s
	private ArrayList<Point2D> path = new ArrayList<Point2D>(); // history of player movement
	
	DronePlayer(String ip, int port, double v) throws UnknownHostException {
		super(ip, port);
		
		position = new Vector3r(0f, 0f, 0f);
		theta = 0;
		maxV = v;
		
		// TODO Auto-generated constructor stub
	}
	
	public void updatePositionData() {
		position = getPosition();
		path.add(get2DPos());
	}
	
	public Vector3r getPos() {
		return position;
	}
	
	public Point2D getRelativePos(Point2D otherPos) {		
//		// differences in x, y coords in global frame
//		double xDiff = (otherPos.getX() - position.getX());
//		double yDiff = (otherPos.getY() - position.getY());
//		
//		// x, y coords of evader wrt pursuer
//		double x = xDiff*Math.cos(theta) - yDiff*Math.sin(theta);
//		double y = -xDiff*Math.sin(theta) + yDiff*Math.cos(theta);
//		
//		return new Point2D.Double(xDiff, yDiff);
		
		double x_e = otherPos.getX();
		double y_e = otherPos.getY();
		double x_p = position.getX();
		double y_p = position.getY();
		
		double x = (x_e-x_p)*Math.cos(theta) + (y_e-y_p)*Math.sin(theta);
		double y = -(x_e-x_p)*Math.sin(theta) + (y_e-y_p)*Math.cos(theta);
		
		return new Point2D.Double(x, y);
	}
	
	public List<Point2D> getRelativePath(DronePlayer other) {
		List<Point2D> otherPath = other.getPath();
		
		List<Point2D> relativePath = new ArrayList<Point2D>();
		
		for (Point2D p : otherPath) {
			relativePath.add(getRelativePos(p));
		}
		
		return relativePath;		
	}
		
	public double getTheta() {
		return theta;
	}
	
	public float getMaxV() {
		return (float) maxV;
	}
	
	// no longer swapped!!!
	// x, y swapped for plotting purposes ONLY!!!
	public Point2D get2DPos() {
		return new Point2D.Double(position.getX(), position.getY());
	}
	
	public List<Point2D> getPath() {
		return path;
	}
	
	public Line2D getLastMovement() {
		int size = path.size();
		if (size < 2) {
			return null;
		}
		
		Point2D prev = path.get(size - 2);
		Point2D current = path.get(size - 1);
		
		return new Line2D.Float(prev, current);
	}
	
	public void steer(double control) {
		// TODO Auto-generated method stub
		
	}
	
	public void move() {
		// TODO Auto-generated method stub
		
	}

	public void setHunter(DronePlayer e) {
		// TODO Auto-generated method stub
		
	}

	public boolean isCaught() {
		// TODO Auto-generated method stub
		return false;
	}

	public void evade() {
		// TODO Auto-generated method stub
		
	}

	public void setTarget(DronePlayer e) {
		// TODO Auto-generated method stub
		
	}

	public boolean targetCaught() {
		// TODO Auto-generated method stub
		return false;
	}

	public void pursue() {
		// TODO Auto-generated method stub
		
	}
}

