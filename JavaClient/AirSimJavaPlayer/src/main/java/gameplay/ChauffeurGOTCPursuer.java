package gameplay;

import java.net.UnknownHostException;

import gameplay.AirSimStructures.Vector3r;

public class ChauffeurGOTCPursuer extends ChauffeurDronePlayer implements Pursuer {
	private double captureL; // capture radius
	private DronePlayer target;
	private float eVel;
	
	ChauffeurGOTCPursuer(String ip, int port, double v, double r, double l, DronePlayer e) throws UnknownHostException {
		super(ip, port, v, r);
		
		captureL = l;
		setTarget(e);
	}
	
	public void setTarget(DronePlayer e) {
		target = e;
		eVel = target.getMaxV();
	}
	
	public boolean targetCaught() {
		return (position.distance(target.getPos()) <= captureL);
	}

	public void pursue() {		
		Vector3r ePos = target.getPos();
		
		// differences in x, y coords in global frame
		double xDiff = (ePos.getX() - position.getX());
		double yDiff = (ePos.getY() - position.getY());
		
		// x, y coords of evader wrt pursuer
		double x = xDiff*Math.cos(theta) - yDiff*Math.sin(theta);
		double y = -xDiff*Math.sin(theta) + yDiff*Math.cos(theta);

		// relative angle
		double relativeTheta = Math.atan2(y, x);
					
		// control variable, which is effectively turning radius
		double phi = -Math.signum(eVel*Math.sin(relativeTheta) - maxV);

		System.out.println(relativeTheta);
		System.out.println(phi);
		
		steer(phi);
		move();		
	}
}
