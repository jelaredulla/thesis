package gameplay;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class ChauffeurPursuer extends ChauffeurDronePlayer implements Pursuer {
	private List<Point2D> relativeTrajectory;
	
	ChauffeurPursuer(String ip, int port, double v, double r, double l, DronePlayer e) throws UnknownHostException {
		super(ip, port, v, r);
		
		relativeTrajectory = new ArrayList<Point2D>();
		
		setCaptureL(l);
		setOpponent(e);
	}
	
	ChauffeurPursuer(String ip, int port, double v, double r, double l) throws UnknownHostException {
		super(ip, port, v, r);
		
		relativeTrajectory = new ArrayList<Point2D>();
		
		setCaptureL(l);
	}
	
	ChauffeurPursuer(String ip, int port, double v, double r, DronePlayer e) throws UnknownHostException {
		super(ip, port, v, r);
		
		relativeTrajectory = new ArrayList<Point2D>();
		
		setOpponent(e);
	}
	
	ChauffeurPursuer(String ip, int port, double v, double r) throws UnknownHostException {
		super(ip, port, v, r);
		
		relativeTrajectory = new ArrayList<Point2D>();
	}
	
	@Override
	public void updatePositionData() {
		super.updatePositionData();
		relativeTrajectory = getRelativePath(opponent);
	}
	
	public List<Point2D> getRelativeTrajectory() {
		return relativeTrajectory;
	}
	
	public void pursue() {
		Point2D relativePos = getCurrentRelativePos();
		
		double x = relativePos.getX();
		double y = relativePos.getY();
		
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
		
	public void pursueSwerve() {
		Point2D relativePos = getCurrentRelativePos();
		
		double x = relativePos.getX();
		double y = relativePos.getY();
		
		// control variable, which is effectively turning radius
		double phi;
		if (y == 0) {
			if (x < 0) {
				Random signGenerator = new Random();
				phi = ( signGenerator.nextBoolean() ? 1 : -1 );
			} else {
				phi = 0;
			}
		} else {
			phi = Math.signum(y);
			
			if (opponent instanceof AgileDronePlayer) {
				double distance = Math.hypot(x, y);
				if ((Math.abs(y) > minR/2) && (distance <= 2*minR)) {
					phi = 0;
				}
			}	
		}
		

		steer(phi);
		super.move();
	}
}
