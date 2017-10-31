package gameplay;

import java.awt.geom.Point2D;

public class CommonOps {
	public static Point2D inTurningCircle(DronePlayer a, ChauffeurDronePlayer b) {
		Point2D aPos = a.get2DPos();
		Point2D bPos = b.get2DPos();
		double bTheta = b.getTheta();
		double minR = b.getMinR();
		
		Point2D leftCentre = new Point2D.Double(bPos.getX() + minR*Math.cos(bTheta + Math.PI/2),
				bPos.getY() + minR*Math.sin(bTheta + Math.PI/2));
		Point2D rightCentre = new Point2D.Double(bPos.getX() + minR*Math.cos(bTheta - Math.PI/2),
				bPos.getY() + minR*Math.sin(bTheta - Math.PI/2));
		
//		System.out.println("in turn");
//		System.out.println(bPos);
//		System.out.println(bTheta);
//		System.out.println(leftCentre);
//		System.out.println(rightCentre);
		
		if (aPos.distance(leftCentre) <= minR) {
			return leftCentre;
		} else if (aPos.distance(rightCentre) <= minR) {
			return rightCentre;
		}
		
		return null;
	}
}
