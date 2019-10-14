package subway.model;

public class Route {
	private Station start;
    private Station end;
    private int distance;
    private Station passStations;
    private String line;
    private int linechange;
    public Station getStart() {
        return start;
    }
    public void setStart(Station star) {
        this.start = star;
    }
    public Station getEnd() {
        return end;
    }
    public void setEnd(Station end) {
        this.end = end;
    }
    public int getDistance() {
        return distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    } 
    public Station getPassStations() {
		return passStations;
	}
	public void setPassStations(Station passStations) {
		this.passStations = passStations;
	}
	public Route(Station start, Station end, int distance) {
        this.start = start;
        this.end = end;
        this.distance = distance;
    }
    public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public int getLinechange() {
		return linechange;
	}
	public void setLinechange(int linechange) {
		this.linechange = linechange;
	}
	public Route() {

    }
	
}
