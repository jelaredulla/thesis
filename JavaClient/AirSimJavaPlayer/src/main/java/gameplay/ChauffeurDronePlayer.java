package gameplay;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.net.UnknownHostException;

import gameplay.AirSimStructures.DrivetrainType;
import gameplay.AirSimStructures.Vector3r;
import gameplay.AirSimStructures.YawMode;

public class ChauffeurDronePlayer extends DronePlayer {
	final static double deltaT = 0.1; // time step for angle change
	protected double minR; // minimum turning radius
	
	ChauffeurDronePlayer(String ip, int port, double v, double r) throws UnknownHostException {
		super(ip, port, v);
		
		minR = r;
	}
	
	public double getMinR() {
		return minR;
	}
	
	public void steer(double phi) {
		double dtheta = phi*(maxV/minR);
		theta += dtheta*deltaT;
	}
	
	public void move() {
		Vector3r vel = new Vector3r((float) (maxV*Math.cos(theta)), (float) (maxV*Math.sin(theta)), 0f);
		moveByVelocityZ(vel, new Vector3r(0, 0, Simulator.planeHeight), dt, DrivetrainType.MaxDegreeOfFreedom,
				new YawMode());
		
//		moveByAngle(-0.1f, 0f, -5f, (float) theta, dt);

		updatePositionData();
	}

}
