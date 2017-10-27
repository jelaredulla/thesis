package gameplay;

public interface Pursuer {
	public void setTarget(DronePlayer e);
	public boolean targetCaught();
	public void pursue();
}
