package gameplay;

import java.net.UnknownHostException;

public class ChauffeurBangBangPursuer extends ChauffeurDronePlayer implements Pursuer {
	private double captureL; // capture radius
	private DronePlayer target;
	
	ChauffeurBangBangPursuer(String ip, int port, double v, double r, double l, DronePlayer e) throws UnknownHostException {
		super(ip, port, v, r);
		
		captureL = l;
		setTarget(e);
	}
	
	public void setTarget(DronePlayer e) {
		target = e;
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
		

		steer(phi);
		super.move();
	}
}
