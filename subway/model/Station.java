package subway.model;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private String name;
    private List<String> line=new ArrayList<String>();
    private List<Station> linkStations = new ArrayList<Station>();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getLine() {
		return line;
	}
	public void setLine(List<String> line) {
		this.line = line;
	}
	public List<Station> getLinkStations() {
        return linkStations;
    }
    public void setLinkStations(List<Station> linkStations) {
        this.linkStations = linkStations;
    }


    public Station(String name, String line) {
        this.name = name;
        this.line.add(line);
    }

    public Station(String name) {
        this.name = name;
    }

    public Station (){ 

    }
    
}
