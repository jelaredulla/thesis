package gameplay;

import java.awt.geom.Point2D;
import java.net.UnknownHostException;
import java.util.Random;

public class AgileEvader extends AgileDronePlayer implements Evader {
	
	AgileEvader(String ip, int port, double v, double l, DronePlayer p) throws UnknownHostException {
		super(ip, port, v);
		
		setCaptureL(l);
		setOpponent(p);
	}
	
	AgileEvader(String ip, int port, double v, double l) throws UnknownHostException {
		super(ip, port, v);
		
		setCaptureL(l);
	}
	
	AgileEvader(String ip, int port, double v, DronePlayer p) throws UnknownHostException {
		super(ip, port, v);
		
		setOpponent(p);
	}
	
	AgileEvader(String ip, int port, double v) throws UnknownHostException {
		super(ip, port, v);
	}
	
	
	

	public void evade() {
		Point2D relativePos = getCurrentRelativePos();
		
		double x = relativePos.getX();
		double y = relativePos.getY();
		
		steer(theta + Math.atan2(y, x) + Math.PI);
		super.move();
	}

	public void evadeJerk() {
		Point2D relativePos = getCurrentRelativePos();
		
		double x = relativePos.getX();
		double y = relativePos.getY();
		
		double relTheta = theta + Math.atan2(y, x) + Math.PI;
		if (opponent instanceof ChauffeurDronePlayer) {
			ChauffeurDronePlayer h = (ChauffeurDronePlayer) opponent;
			if (Math.hypot(x, y) <= (h.getMinR())) {
//				Random signGenerator = new Random();
//				relTheta += ( signGenerator.nextBoolean() ? 1 : -1 )*Math.PI/2;
				relTheta += Math.PI/2;
				steer(relTheta);
				super.move();
				return;
			}
		}
			
		steer(relTheta);
		super.move();		
	}
}

