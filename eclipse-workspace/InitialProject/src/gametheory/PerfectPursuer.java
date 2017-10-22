package gametheory;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class PerfectPursuer extends Player {
	private double minR; // minimum turning radius
	private double captureL; // capture radius
	private Player target;
	private boolean caught = false;
	
	public PerfectPursuer(Point2D init, double pose, double v, double r, double l, Player e) {
		super(init, pose, v);
		minR = r;
		captureL = l;
		setTarget(e);
	}
	
	public void setTarget(Player e) {
		target = e;
	}
	
	public boolean targetCaught() {
		return (pos.distance(target.getPos()) <= captureL);
	}
	
	public void steer(double dtheta) {
		theta += dtheta*dt;
		theta = Math.atan2(Math.sin(theta), Math.cos(theta));
	}
	
	public void stalk() {
		Point2D ePos = target.getPos();
				
		// differences in x, y coords in global frame
		double xDiff = (ePos.getX() - pos.getX());
		double yDiff = (ePos.getY() - pos.getY());
		
		// x, y coords of evader wrt pursuer
		double x = xDiff*Math.cos(theta) - yDiff*Math.sin(theta);
		double y = yDiff*Math.sin(theta) + yDiff*Math.cos(theta);
		
		// control variable, which is effectively turning radius
		double phi;
		if (x == 0) {
			if (y < 0) {
				phi = 1;
			} else {
				phi = 0;
			}
		} else {
			phi = Math.signum(x);
		}
		
		double dtheta = phi*(maxV/minR);
		
		steer(dtheta);
		super.move();
	}
	
	public void linearPredictStalk() {
		Point2D ePos = target.getPos();
		
		// differences in x, y coords in global frame
		double xDiff = (ePos.getX() - pos.getX());
		double yDiff = (ePos.getY() - pos.getY());
		
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