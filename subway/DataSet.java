package subway;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import subway.model.Route;
import subway.model.Station;

public class DataSet {
	public static LinkedHashSet<List<Station>> LineSet = new LinkedHashSet<List<Station>>();
	public DataSet(String pathname) throws IOException{
        File filename = new File(pathname); 
        InputStreamReader reader = new InputStreamReader( new FileInputStream(filename)); 
        BufferedReader br = new BufferedReader(reader); 
        String content=br.readLine();
        int linenum=Integer.parseInt(content);
        for(int i=0;i<linenum;i++) {
        	content=br.readLine();
        	List<Station> line=new ArrayList<Station>();
        	String[] linearea=content.split(" "); 
        	String linename=linearea[0];
        	for(int j=1;j<linearea.length;j++) {
        		int flag=0;
        		for(List<Station> l:LineSet) {
        			for(int k=0;k<l.size();k++) {
        				if(l.get(k).getName().equals(linearea[j])) {  
        					List<String> newline=l.get(k).getLine();
        					newline.add(linename);
        					l.get(k).setLine(newline);
        					line.add(l.get(k));
        					flag=1;
        					break;
        				}
        			}
        			if(flag==1)
        				break;
        		}
        		if(j==linearea.length-1&&linearea[j].equals(linearea[1])) {
        			line.get(0).getLinkStations().add(line.get(line.size()-1));
        			line.get(line.size()-1).getLinkStations().add(line.get(0));
        			flag=1;
        		}
        		if(flag==0)
        			line.add(new Station(linearea[j],linename));
        	}
        	for(int j=0;j<line.size();j++) {
        		List<Station> newlinkStations=line.get(j).getLinkStations();
        		if(j==0) {
        			newlinkStations.add(line.get(j+1));
        			line.get(j).setLinkStations(newlinkStations);
        		}
        		else if(j==line.size()-1) {
        			newlinkStations.add(line.get(j-1));
        			line.get(j).setLinkStations(newlinkStations);
        		}
        		else {
        			newlinkStations.add(line.get(j+1));
        			newlinkStations.add(line.get(j-1));
        			line.get(j).setLinkStations(newlinkStations);
        		}
        	}
        	LineSet.add(line); 
        }
        br.close();
	}
	
	private static List<Station> analysisList = new ArrayList<>();
	private static HashMap<Station, Route> routeMap = new HashMap<>();
	private static Station getNextStation() {
        int min=Integer.MAX_VALUE;
        Station rets = null;
        Set<Station> stations = routeMap.keySet();
        for (Station station : stations) {
            if (analysisList.contains(station)) {
                continue;
            }
            Route route = routeMap.get(station);
            if (route.getDistance() < min) {
                min = route.getDistance();
                rets = route.getEnd();
            }
        }
        return rets;
    }	
	private static List<String> getSameLine(List<String> line1,List<String> line2) {
		List<String> sameline=new ArrayList<String>();
		for(String lineB:line1) {
			for(String lineA:line2) {
				if(lineA.equals(lineB))
					sameline.add(lineA);
			}
		}
		return sameline;
	}
	public static List<Station> getLineStation(String linename){
		int flag=1;
		for (List<Station> l:DataSet.LineSet) {
			flag=1;
			for(Station s :l) {
				if(!s.getLine().contains(linename))
					flag=0;
			}
			if(flag==1)
				return l;
		}	
		return null;
	}
	public static Route shortestPath(Station start, Station end) {
		for(List<Station> l:DataSet.LineSet) {
			for(int k=0;k<l.size();k++) {
				Route route = new Route();
				route.setStart(start);
				route.setEnd(l.get(k));
				route.setDistance(Integer.MAX_VALUE);
				route.setLinechange(0);
				routeMap.put(l.get(k), route);
			}
		}
		for(Station s:start.getLinkStations()) {
			routeMap.get(s).setDistance(1);
			routeMap.get(s).setPassStations(start);
			List<String> samelines=getSameLine(start.getLine(),s.getLine());
			routeMap.get(s).setLine(samelines.get(0));
		}
		routeMap.get(start).setDistance(0);
		
		analysisList.add(start);
        Station nextstation = getNextStation();
        while(nextstation!=null) {
        	for(Station s:nextstation.getLinkStations()) {
        		if(routeMap.get(nextstation).getDistance()+1<routeMap.get(s).getDistance()) {
        			routeMap.get(s).setDistance(routeMap.get(nextstation).getDistance()+1);
        			routeMap.get(s).setPassStations(nextstation);
        			List<String> samelines=getSameLine(nextstation.getLine(),s.getLine());
        			if(!samelines.contains(routeMap.get(nextstation).getLine())) {
        				routeMap.get(s).setLine(samelines.get(0));
        				routeMap.get(s).setLinechange(1);
        			}
        			else {
        				routeMap.get(s).setLine(routeMap.get(nextstation).getLine());
        			}
        		}
        	}
        	analysisList.add(nextstation); 
        	nextstation = getNextStation();
        }    
        return routeMap.get(end);
    }
	
	public static List<String> getPath(Route r){
		List<String> path=new ArrayList<String>();
		Stack<Station> tmp=new Stack<Station>();
		Station s=r.getPassStations();
		while(!s.equals(r.getStart())) {
			tmp.push(s);
			s=routeMap.get(s).getPassStations();
		}
		path.add(r.getStart().getName());
		while(!tmp.empty()) {
			if(routeMap.get(tmp.peek()).getLinechange()==1) {
				path.add("->"+routeMap.get(tmp.peek()).getLine());
				path.add(tmp.pop().getName());
			}
			else
				path.add(tmp.pop().getName());
		}
		if(r.getLinechange()==1) {
			path.add("->"+r.getLine());
			path.add(r.getEnd().getName());
		}
		else
		    path.add(r.getEnd().getName());
		return path;
	}
}
