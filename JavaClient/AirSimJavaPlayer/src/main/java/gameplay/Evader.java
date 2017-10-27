package gameplay;

public interface Evader {
	public void setHunter(DronePlayer e);
	public boolean isCaught();
	public void evade();
}
