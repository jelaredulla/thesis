package gameplay;

import java.net.UnknownHostException;

import gameplay.AirSimStructures.Vector3r;

public class ChauffeurGOTCEvader extends ChauffeurDronePlayer implements Evader {
		private double captureL; // capture radius
		private DronePlayer hunter;
		private float pVel;
		
		ChauffeurGOTCEvader(String ip, int port, double v, double r, double l, DronePlayer p) throws UnknownHostException {
			super(ip, port, v, r);
			
			captureL = l;
			setHunter(p);
		}
		
		ChauffeurGOTCEvader(String ip, int port, double v, double r, double l) throws UnknownHostException {
			super(ip, port, v, r);
			
			captureL = l;
		}
		
		public void setHunter(DronePlayer p) {
			hunter = p;
			pVel = hunter.getMaxV();
		}
		
		public boolean isCaught() {
			return (position.distance(hunter.getPos()) <= captureL);
		}

		public void evade() {
			Vector3r pPos = hunter.getPos();
			double pTheta = hunter.getTheta();
			
			// differences in x, y coords in global frame
			double xDiff = (position.getX() - pPos.getX());
			double yDiff = (position.getY() - pPos.getY());
			
			// x, y coords of evader wrt pursuer
			double x = xDiff*Math.cos(pTheta) - yDiff*Math.sin(pTheta);
			double y = -xDiff*Math.sin(pTheta) + yDiff*Math.cos(pTheta);

			// relative angle
			double relativeTheta = Math.atan2(y, x);
						
			// control variable, which is effectively turning radius
			double phi = -Math.signum(maxV - pVel*Math.sin(relativeTheta));

			System.out.println(relativeTheta);
			System.out.println(phi);
			
			steer(phi);
			move();		
		}
	}
