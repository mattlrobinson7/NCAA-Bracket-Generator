
public class Team {
	public int seed;
	public String name;
	
	public Team(int seed, String name) {
		this.seed = seed;
		this.name = name;
	}
	
	public String toString() {
		return "#" + seed + " " + name + " | ";
	}
}
