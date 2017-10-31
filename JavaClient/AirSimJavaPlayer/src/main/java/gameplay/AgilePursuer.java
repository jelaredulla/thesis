package gameplay;

import java.awt.geom.Point2D;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class AgilePursuer extends AgileDronePlayer implements Pursuer {
	private double captureL; // capture radius
	private DronePlayer target;
	
	private List<Point2D> relativeTrajectory;
	
	AgilePursuer(String ip, int port, double v, double l, DronePlayer e) throws UnknownHostException {
		super(ip, port, v);
		// TODO Auto-generated constructor stub
		
		captureL = l;
		relativeTrajectory = new ArrayList<Point2D>();
		
		setTarget(e);
	}
	
	AgilePursuer(String ip, int port, double v, double l) throws UnknownHostException {
		super(ip, port, v);
		// TODO Auto-generated constructor stub
		
		captureL = l;
		relativeTrajectory = new ArrayList<Point2D>();
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
		
	public void pursue() {		
		Point2D relativePos = getCurrentRelativePos();
		
		double x = relativePos.getX();
		double y = relativePos.getY();
		
		steer(theta + Math.atan2(y, x));
		super.move();
	}

}
