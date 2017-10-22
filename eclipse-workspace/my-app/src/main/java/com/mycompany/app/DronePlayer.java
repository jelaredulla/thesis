package com.mycompany.app;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.app.AirSimMessages.YawMode;




public class DronePlayer extends MultirotorClient {
	public final static float dt = 20f;
	protected Vector3r position; // (x, y, z) position in fixed global frame
	protected float theta; // orientation in fixed global frame
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
	
	public Point2D get2DPos() {
		return new Point2D.Float(position.getX(), position.getY());
	}
	
	public float getTheta() {
		return theta;
	}
	
	public float getMaxV() {
		return (float) maxV;
	}
	
	
	public void move() {
		
		Vector3r vel = new Vector3r((float) (maxV*Math.sin(theta)), (float) (maxV*Math.cos(theta)), 0f);
		moveByVelocityZ(vel, position, dt, DrivetrainType.ForwardOnly, new YawMode(true, 0f));
		
		try {
			Thread.sleep((long) (dt*1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		updatePositionData();
	}
	
	public void steer(float dir) {
		theta = dir;
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
	
	public List<Point2D> getPath() {
		return path;
	}
	
	public void steerInBox() {
		double x = position.getX();
		double y = position.getY();
		
		double xDir = Math.signum(Math.sin(theta));
		double yDir = Math.signum(Math.cos(theta));
		
		double dir = theta;
		double boxR = 0.05;
		if (((x <= boxR) && (xDir < 0)) || ((x >= (1 - boxR)) && (xDir > 0))) {
			dir = -theta;
		} else if (((y <= boxR) && (yDir < 0)) || ((y >= (1 - boxR)) && (yDir > 0))) {
			dir = Math.PI - theta;
		}
		
		steer((float) (Math.atan2(Math.sin(dir), Math.cos(dir))));
	}
	

}

