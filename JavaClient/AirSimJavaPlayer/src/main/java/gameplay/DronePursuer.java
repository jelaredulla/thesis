package gameplay;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.net.UnknownHostException;

public class DronePursuer extends DronePlayer {
	private double minR; // minimum turning radius
	private double captureL; // capture radius
	private DronePlayer target;
	private boolean caught = false;

	DronePursuer(String ip, int port, double v, double r, double l, DronePlayer e) throws UnknownHostException {
		super(ip, port, v);
		
		minR = r;
		captureL = l;
		setTarget(e);
	}
	
	
	public void setTarget(DronePlayer e) {
		target = e;
	}
	
	public boolean targetCaught() {
		return (position.distance(target.getPos()) <= captureL);
	}
	
	public void steer(double dtheta) {
		theta += dtheta*0.01f;
		//theta = Math.atan2(Math.sin(theta), Math.cos(theta));
		
		System.out.println(theta);
	}
	
	public void stalk() {
		Vector3r ePos = target.getPos();
				
		// differences in x, y coords in global frame
		double xDiff = (ePos.getX() - position.getX());
		double yDiff = (ePos.getY() - position.getY());
		
		System.out.print("xDiff: "+xDiff);
		System.out.println("yDiff: "+yDiff);
		
		// x, y coords of evader wrt pursuer
		double x = xDiff*Math.cos(theta) - yDiff*Math.sin(theta);
		double y = -xDiff*Math.sin(theta) + yDiff*Math.cos(theta);
		
		System.out.print("x: "+x);
		System.out.println("y: "+y);
		
		// control variable, which is effectively turning radius
		double phi;
		if (y == 0) {
			if (x < 0) {
				phi = 1;
			} else {
				phi = 0;
			}
		} else {
			phi = Math.signum(y);
		}
		
		double dtheta = phi*(maxV/minR);
		
		steer(dtheta);
		super.move();
	}
	
	public void linearPredictStalk() {
		Vector3r ePos = target.getPos();
		
		// differences in x, y coords in global frame
		double xDiff = (ePos.getX() - position.getX());
		double yDiff = (ePos.getY() - position.getY());
		
		double eTheta = target.getTheta();
		double v_e = estimateVelocity(); 
		double a_s = v_e*Math.sin(eTheta);
		double a_c = v_e*Math.cos(eTheta);
		
		double K = (-a_s*yDiff + a_c*xDiff) / maxV;
		double c = Math.sqrt(xDiff*xDiff + yDiff*yDiff);
		double d = Math.atan2(-yDiff, xDiff);
		
		double desiredTheta = Math.acos(K/c) + d;
		desiredTheta = Math.atan2(Math.sin(desiredTheta), Math.cos(desiredTheta));
		
		double phi = Math.signum(desiredTheta - theta);
		double dtheta = phi*(maxV/minR);
		
		steer(dtheta);
		super.move();
	}
	
	public double estimateVelocity() {
		Line2D lastMove = target.getLastMovement();
		return lastMove.getP1().distance(lastMove.getP2()) / dt;
	}

}
