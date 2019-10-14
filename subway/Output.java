package subway;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import subway.model.Route;
import subway.model.Station;

public class Output {
	
	public static void main(String[] args) throws IOException {
		   if(args[0].equals("-map"))
			   new DataSet(args[1]);
		   else if(args[0].equals("-a")) {
			   if(args[2].equals("-map"))
				   new DataSet(args[3]);
			   else return;
			   List<Station> stations=DataSet.getLineStation(args[1]);
			   String content="";
			   if(stations==null)
				   content="不存在该地铁线路";
			   else {
			       for(int i=0;i<stations.size();i++) {
				       if(i==stations.size()-1&&stations.get(i).getLinkStations().contains(stations.get(0)))
					       content=content+stations.get(i).getName()+" ";
				       else
					       content=content+stations.get(i).getName()+" ";
			       }
			   }
			   if(args[4].equals("-o")) {
			        printToTxt(content,args[5]);
			   }
			   else return;
		   }
		   else if(args[0].equals("-b")) {
			   if(args[3].equals("-map"))
				   new DataSet(args[4]);
			   else return;
			   Station start=null;
			   Station end=null;
			   for(List<Station> l:DataSet.LineSet){
					for(int k=0;k<l.size();k++) {
		                if(l.get(k).getName().equals(args[1])) {
		                	start=l.get(k);
		                }
		                if(l.get(k).getName().equals(args[2])) {
		                	end=l.get(k);
		                }
					}
				}
			   String context="";
			   if(start==null||end==null)
				   context="站点名不存在";
			   else {
				   Route r=DataSet.shortestPath(start,end);
	               List<String> path=DataSet.getPath(r);
	               context="共"+(r.getDistance()+1)+"站\n";
	               for(String s:path)
	        	       context=context+s+"\n";
			   }
			   if(args[5].equals("-o")) {
			       printToTxt(context,args[6]);
		       }
		       else 
			       return;
		   }
	}
    public static void printToTxt(String content,String path) throws IOException {
	    File file = new File(path);
        if(!file.exists()){
    	    file.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(file);
	    byte[]  bytes = content.getBytes("UTF-8");
	    outputStream.write(bytes);
	    outputStream.close();
    }

}
