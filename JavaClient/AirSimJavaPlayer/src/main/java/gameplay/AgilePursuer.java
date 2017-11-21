package gameplay;

import java.awt.geom.Point2D;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class AgilePursuer extends AgileDronePlayer implements Pursuer {
	
	private List<Point2D> relativeTrajectory;
	
	AgilePursuer(String ip, int port, double v, double l, DronePlayer e) throws UnknownHostException {
		super(ip, port, v);
		
		relativeTrajectory = new ArrayList<Point2D>();
		
		setOpponent(e);
		setCaptureL(l);
	}
	
	AgilePursuer(String ip, int port, double v, double l) throws UnknownHostException {
		super(ip, port, v);
		
		relativeTrajectory = new ArrayList<Point2D>();
		
		setCaptureL(l);	
	}
	
	AgilePursuer(String ip, int port, double v, DronePlayer e) throws UnknownHostException {
		super(ip, port, v);
		
		relativeTrajectory = new ArrayList<Point2D>();
		
		setOpponent(e);
	}
	
	AgilePursuer(String ip, int port, double v) throws UnknownHostException {
		super(ip, port, v);
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
		
		steer(theta + Math.atan2(y, x));
		super.move();
	}

}
