package gametheory;

import net.java.games.input.*;
import visualiser.GlobalFieldWindow;

public class ControllerHandler {
	private Controller gamepad = null;
	
	public ControllerHandler() {
        Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for(int i = 0; i < ca.length; i++){

            /* Get the name of the controller */
            //System.out.println(ca[i].getName() + ": " + ca[i].getType());
            
            if (ca[i].getType() == Controller.Type.GAMEPAD) {
            	System.out.println("Gamepad found!");
            	gamepad = ca[i];
            	break;
            }
        }
	}
	
	public boolean gamepadSet() {
		return gamepad != null;
	}
	
	public double pollLeftJoyStick() {
		gamepad.poll();
		
    	Component[] components = gamepad.getComponents();
    	 
    	float y = -components[0].getPollData();
    	float x = components[1].getPollData();
    	
    	double pose = Math.atan2((double) x, (double) y);
    	return pose;
	}
}