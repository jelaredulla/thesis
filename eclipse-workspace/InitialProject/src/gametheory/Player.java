package gametheory;

import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class Player {
	public final static double dt = 0.001; // time step
	protected Point2D pos; // position
	protected double theta; // orientation
	protected double maxV; // maximum velocity
	private ArrayList<Point2D> path = new ArrayList<Point2D>(); // history of player movement
	
	public Player(Point2D init, double pose, double v) {
		pos = init;
		theta = pose;
		maxV = v;
		
		path.add((Point2D) pos.clone());
	}
	
	public void move() {
		double v_x, v_y;
		v_x = maxV*Math.sin(theta);
		v_y = maxV*Math.cos(theta);
		
		pos.setLocation(pos.getX() + v_x*dt, pos.getY() + v_y*dt);
		path.add((Point2D) pos.clone());
		
		//System.out.println("Point is " + pos);
	}
	
	public void steer(double dir) {
		theta = dir;
	}
	
	public Point2D getPos() {
		return pos;
	}
	
	public double getTheta() {
		return theta;
	}
	
	public Line2D getLastMovement() {
		int size = path.size();
		if (size < 2) {
			return null;
		}
		
		Point2D prev = path.get(size - 2);
		Point2D current = path.get(size - 1);
		
		return new Line2D.Double(prev, current);
	}
	
	public List<Point2D> getPath() {
		return path;
	}
	
	public void steerInBox() {
		double x = pos.getX();
		double y = pos.getY();
		
		double xDir = Math.signum(Math.sin(theta));
		double yDir = Math.signum(Math.cos(theta));
		
		double dir = theta;
		double boxR = 0.05;
		if (((x <= boxR) && (xDir < 0)) || ((x >= (1 - boxR)) && (xDir > 0))) {
			dir = -theta;
		} else if (((y <= boxR) && (yDir < 0)) || ((y >= (1 - boxR)) && (yDir > 0))) {
			dir = Math.PI - theta;
		}
		
		steer(Math.atan2(Math.sin(dir), Math.cos(dir)));
	}
}
