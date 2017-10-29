package gameplay;

import java.net.UnknownHostException;

public class AgileDronePlayer extends DronePlayer {
	AgileDronePlayer(String ip, int port, double v) throws UnknownHostException {
		super(ip, port, v);
	}

	public void move() {		
		Vector3r vel = new Vector3r((float) (maxV*Math.cos(theta)), (float) (maxV*Math.sin(theta)), 0f);
		moveByVelocityZ(vel, new Vector3r(0, 0, Simulator.planeHeight), dt, DrivetrainType.MaxDegreeOfFreedom,
				new YawMode(false, (float) theta));
		
//		moveByAngle(-0.1f, 0f, -5f, (float) theta, dt);

		updatePositionData();
	}
	
	public void steer(double d) {
		// player is agile, so angle can be changed abruptly
		theta = d;
	}
	
	public void steerInBox(double boxSide) {
		double x = position.getX();
		double y = position.getY();
		
		double xDir = Math.signum(Math.cos(theta));
		double yDir = Math.signum(Math.sin(theta));
		
		double dir = theta;
		if (((x <= -boxSide) && (xDir < 0)) || ((x >= boxSide) && (xDir > 0))) {
			dir = Math.PI - theta;
		} else if (((y <= -boxSide) && (yDir < 0)) || ((y >= boxSide) && (yDir > 0))) {
			dir = -theta;
		}
		
		steer((Math.atan2(Math.sin(dir), Math.cos(dir))));
	}
}
