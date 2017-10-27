package gameplay;

import java.net.UnknownHostException;

import gameplay.AirSimStructures.Vector3r;

public class ChauffeurGOTCPursuer extends ChauffeurDronePlayer implements Pursuer {
	private double captureL; // capture radius
	private DronePlayer target;
	
	ChauffeurGOTCPursuer(String ip, int port, double v, double r, double l, DronePlayer e) throws UnknownHostException {
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
		float eVel = target.getMaxV();
		
		double relativeTheta = target.getTheta() - theta;
		
		// control variable, which is effectively turning radius
		double phi = Math.signum(eVel - maxV*Math.sin(relativeTheta));

		steer(phi);
		super.move();		
	}
}
