package gameplay;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


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
		
	public double getTheta() {
		return theta;
	}
	
	public float getMaxV() {
		return (float) maxV;
	}
	
	// x, y swapped for plotting purposes ONLY!!!
	public Point2D get2DPos() {
		return new Point2D.Float(position.getY(), position.getX());
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

