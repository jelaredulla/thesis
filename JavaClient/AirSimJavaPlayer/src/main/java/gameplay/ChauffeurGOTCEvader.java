package gameplay;

import java.awt.geom.Point2D;
import java.net.UnknownHostException;

import gameplay.AirSimStructures.Vector3r;

public class ChauffeurGOTCEvader extends ChauffeurDronePlayer implements Evader {
		private double pVel;
		
		ChauffeurGOTCEvader(String ip, int port, double v, double r, double l, DronePlayer p) throws UnknownHostException {
			super(ip, port, v, r);
			
			setCaptureL(l);
			setOpponent(p);
		}
		
		ChauffeurGOTCEvader(String ip, int port, double v, double r, double l) throws UnknownHostException {
			super(ip, port, v, r);
			
			setCaptureL(l);
		}
		
		ChauffeurGOTCEvader(String ip, int port, double v, double r, DronePlayer p) throws UnknownHostException {
			super(ip, port, v, r);
			
			setOpponent(p);
		}
		
		ChauffeurGOTCEvader(String ip, int port, double v, double r) throws UnknownHostException {
			super(ip, port, v, r);
		}
		
		@Override
		public void setOpponent(DronePlayer p) {
			super.setOpponent(p);
			pVel = opponent.getMaxV();
		}
		
		public void evade() {
			Point2D relativePos = getCurrentRelativePos();
			double x = relativePos.getX();
			double y = relativePos.getY();
			
			// relative angle
			double relativeTheta = Math.atan2(y, x) + Math.PI;
						
			// control variable, which is effectively turning radius
			double phi = -Math.signum(maxV - pVel*Math.sin(relativeTheta));
			steer(phi);
			super.move();
		}

		public void evadeOld() {
			Point2D pPos = getCurrentRelativePos();

			// relative angle
			double relativeTheta = Math.atan2(pPos.getY(), pPos.getY());
						
			// control variable, which is effectively turning radius
			double phi = -Math.signum(maxV - pVel*Math.sin(relativeTheta));

			System.out.println(relativeTheta);
			System.out.println(phi);
			
			steer(phi);
			move();		
		}
	}
