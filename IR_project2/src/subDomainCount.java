import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.Map.Entry;
public class subDomainCount {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		subDomainCount.printAll(sort(frequencies(extract_subdomains("src/subDomain.txt"))));
	}
	public static ArrayList<String> extract_subdomains(String pathToFile){
	    //StringBuilder sb = null;
	    ArrayList<String> subDomain = new ArrayList<String>();
		File file = new File(pathToFile);
		FileReader fr =null;
		BufferedReader br = null;
		String currentLine = null;
		try{
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			//sb = new StringBuilder();
			String[] line = null;
			while((currentLine = br.readLine())!=null){
				line = currentLine.toLowerCase().split(" ");
				if(line.length>1)
				subDomain.add(line[1]);
				//sb.append(line[1]+" ");					
			}
			br.close();
			fr.close();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(br!=null||fr!=null){
				try{
					br.close();
					fr.close();
				}catch(IOException e1){
					e1.printStackTrace();
				}				
			}
		}
		return subDomain;
	}
	
	public static HashMap<String,Integer> frequencies(ArrayList<String> subDomain){
		HashMap<String,Integer> domainFreqs = new HashMap<String,Integer>();		
		//test runtime of two ways of traverse
		//long start,end;
		//start = System.currentTimeMillis();
		//wordFreqs.clear();
		//HashMap<String,Integer> temp = new HashMap<String,Integer>();
		for(String domain:subDomain){
			if(domainFreqs.containsKey(domain)){
				domainFreqs.put(domain, domainFreqs.get(domain)+1);
			}else{
				domainFreqs.put(domain, 1);
			}
		}
		return domainFreqs;
		//wordFreqs.addAll(temp.entrySet());
		//System.out.println("wordFreqs size = "+wordFreqs.size());
	}
	
	public static ArrayList<Map.Entry<String, Integer>> sort(HashMap<String, Integer> domainFreqs){
		
		ArrayList<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>();
		entryList.addAll(domainFreqs.entrySet());
		Collections.sort(entryList,new MapComparator());
		return entryList;
	}
	static class MapComparator implements Comparator <Map.Entry<String, Integer>>{
		public int compare (Map.Entry<String,Integer> o1, Map.Entry<String,Integer>o2) {
			return o2.getValue().intValue() - o1.getValue().intValue();
		}
		
	}
	
	public static void printAll(ArrayList<Map.Entry<String, Integer>> entryList){
		for(Map.Entry<String, Integer> mapEntry: entryList){
			System.out.println(mapEntry.getKey()+" - "+ mapEntry.getValue());
		}
	}
}
