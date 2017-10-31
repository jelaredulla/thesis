package gameplay;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class ChauffeurPursuer extends ChauffeurDronePlayer implements Pursuer {
	private double captureL; // capture radius
	private DronePlayer target;
	
	private Point2D relativePos;
	private List<Point2D> relativeTrajectory;
	
	ChauffeurPursuer(String ip, int port, double v, double r, double l, DronePlayer e) throws UnknownHostException {
		super(ip, port, v, r);
		
		captureL = l;
		relativeTrajectory = new ArrayList<Point2D>();
		
		setTarget(e);
	}
	
	public void setTarget(DronePlayer e) {
		target = e;
	}
	
	public boolean targetCaught() {
		return (position.distance(target.getPos()) <= captureL);
	}
	
	@Override
	public void updatePositionData() {
		super.updatePositionData();
		relativeTrajectory = getRelativePath(target);
	}
	
	public List<Point2D> getRelativeTrajectory() {
		return relativeTrajectory;
	}
	
	public Point2D getCurrentRelativePos() {
		return getRelativePos(target.get2DPos());
	}
	
	public void pursuePlain() {
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
		
	public void pursue() {
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
			
			if (target instanceof AgileDronePlayer) {
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
