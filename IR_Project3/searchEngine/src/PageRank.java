import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
//import java.util.Random;


public class PageRank {
	public static int maxDocId = 143218;
	static double df = 0.85;//damping factor
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		HashMap<Integer,HashSet<Integer>> outLinks = outgoingLinks("workspace/outgoinglinks");
//		HashMap<Integer,HashSet<Integer>> outLinks = outgoingLinks("IR_project3/outgoinglinks");
		
		//HashMap<Integer,HashSet<Integer>> inLinks = incomingLinks(outLinks);
		System.out.println("link map size: "+outLinks.size());
		//System.out.println("link map size: "+inLinks.size());
				//new HashMap<Integer,HashSet<Integer>>(); 
//		 Random r=new Random(); 
//		  for(int i=0;i<10;i++){ 
//		    System.out.println(r.nextInt(10)); 
//		  } 
		HashMap<Integer,Double> pageRank = computeRanking(outLinks);
//		normalization(pageRank);
		printRank(pageRank);
//		for(Map.Entry entry:pageRank.entrySet()){
//			if(((Integer)entry.getKey())==10120)
//			System.out.println("doc:"+entry.getKey()+" - "+entry.getValue());
//		}
		

	}
	public static HashMap<Integer,HashSet<Integer>> outgoingLinks (String folderPath) throws IOException{
		HashMap<Integer,HashSet<Integer>> links = new HashMap<Integer,HashSet<Integer>>();
		String path = null;
		String curLine = null;
		File f = null;
		FileReader fr = null;
		BufferedReader br = null;
		HashSet<Integer> temp = null;
		for(int i=0;i<=maxDocId;i++){
//		for(int i=0;i<=3;i++){
			path = folderPath+"/"+i+".txt";
			f = new File(path);
			if(!f.exists()) continue;
			System.out.println(i);
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			while((curLine = br.readLine())!=null){
				if(curLine.startsWith("-")){
//					System.out.println(curLine);
					continue;
				}
//				if(curLine.startsWith("74665")||curLine.startsWith("74673"))
//					System.out.println(curLine);
				if(links.containsKey(i)){
					links.get(i).add(Integer.parseInt(curLine.split("\\s+")[0]));
				}else{
					temp = new HashSet<Integer>();
					temp.add(Integer.parseInt(curLine.split("\\s+")[0]));
					links.put(i, temp);
				}
			}
//			System.out.println(i);
			fr.close();
			br.close();
		}
		return filterLinks(links);
	}
	//filter out nodes' outgoing links which don't exist in the corpus
	public static HashMap<Integer,HashSet<Integer>> filterLinks( HashMap<Integer,HashSet<Integer>> links){
		Iterator iter = links.entrySet().iterator();
		HashSet<Integer> linkSet = new HashSet<Integer>(links.keySet());
		HashSet<Integer> temp = null;
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			temp = (HashSet)entry.getValue();
//			System.out.print("before:"+((HashSet)entry.getValue()).size());
			temp.retainAll(linkSet);
//			System.out.println("after:"+((HashSet)entry.getValue()).size());
		}
		return links;
	}
//	public static HashMap<Integer,HashSet<Integer>> incomingLinks(HashMap<Integer,HashSet<Integer>> outgoingLinks){
//		HashMap<Integer,HashSet<Integer>> inComingLinks = new HashMap<Integer,HashSet<Integer>>();
//		Iterator iter0 = outgoingLinks.entrySet().iterator();
//		while(iter0.hasNext()){
//			Map.Entry entry = (Map.Entry) iter0.next();
//			Integer docId = (Integer)entry.getKey();
//			HashSet<Integer> links = (HashSet) entry.getValue();
//			Iterator<Integer> iter1 = links.iterator();
//			HashSet<Integer> temp = null;
//			while(iter1.hasNext()){
//				Integer node = iter1.next();
//				if(inComingLinks.containsKey(node)){
//					//inComingLinks.put(node, docId);
//					inComingLinks.get(node).add(docId);
//				}else{
//					temp = new HashSet<Integer>();
//					temp.add(docId);
//					inComingLinks.put(node,temp);
//				}
//			}			
//		}
//		return inComingLinks;
//	}
	public static HashMap<Integer,Double> initRanking(HashMap<Integer,HashSet<Integer>> outLinks,double n){
		HashMap<Integer,Double> rank = new HashMap<Integer,Double>();
		Iterator iter = outLinks.keySet().iterator();
		while(iter.hasNext()){
			rank.put((Integer)iter.next(), n);
		}
		return rank;
	}
	public static void normalization (HashMap<Integer,Double> map){
		double temp = 0;
		for(Map.Entry entry:map.entrySet()){
			temp =(Double) entry.getValue();
			entry.setValue(Math.log10(1+temp));
			
		}
//		HashMap<Integer,Double> nor_map = new HashMap<Integer, Double>();
//		double avg = 0, sum = 0, deviate = 0;
//		int docNum = 0;
////		System.out.println("in nor"+map.size());
//		for(Map.Entry entry:map.entrySet()){
//			docNum+=1;
//			sum += (Double)entry.getValue();
//		}
//		avg = sum/docNum;
//		for(Map.Entry entry:map.entrySet()){
//			double temp = (Double)entry.getValue();
////			System.out.println("in nor"+(temp-avg));
//			entry.setValue(temp-avg);
//		}
//		for(Map.Entry entry:map.entrySet()){
//			deviate += Math.pow((Double)entry.getValue(),2);
//		}
//		deviate = Math.sqrt(deviate/docNum);
//		for(Map.Entry entry:map.entrySet()){
//			double temp = (Double)entry.getValue();
//			entry.setValue(temp/deviate);
//		}
		
//		return nor_map;
	}
	public static double delta (HashMap<Integer,Double> oldPR,HashMap<Integer,Double> newPR){
		double _delta = 0;
		Iterator iter = oldPR.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			Integer docId = (Integer) entry.getKey();
			double pr = (Double)entry.getValue();
			_delta +=Math.pow(pr-newPR.get(docId),2);
		}
		return _delta/oldPR.size();
	}
	public static HashMap<Integer,Double> computeRanking(HashMap<Integer,HashSet<Integer>> outLinks){
		HashMap<Integer,Double> oldPR = initRanking(outLinks,1);
		HashMap<Integer,Double> newPR = initRanking(outLinks,0.15);
		int i =0;
		double _delta = 1;
		while(_delta>0.001){
			i++;
			Iterator iter = outLinks.entrySet().iterator();
			while(iter.hasNext()){//loop over the outLinks map
				Map.Entry entry = (Map.Entry)iter.next();
				Integer docId = (Integer) entry.getKey();// key for map
				HashSet<Integer> out_link = (HashSet)entry.getValue();//value for the key
				Iterator iter0 = out_link.iterator();
				int len = out_link.size();//Num of outgoing links of this node;
				while(iter0.hasNext()){//traverse the set 
					Integer out_link_id = (Integer)iter0.next();
					double pr = 0;
					if(len!=0)
						pr = df*oldPR.get(out_link_id)/len;
					else 
//						pr = df*oldPR.get(out_link_id)/74212;
						pr = df*oldPR.get(out_link_id)/3;
					newPR.put(out_link_id, newPR.get(out_link_id)+pr);
					//newPR.get(out_link_id).add(oldPR.get(docId));
				}			
			}
//			HashMap<Integer,Double> temp = normalization(newPR);
//			System.out.println(temp.size());
			normalization(newPR);
//			for(Map.Entry entry:newPR.entrySet()){
//				if(((Double)entry.getValue())>1)
//				System.out.println(entry.getKey()+" - "+entry.getValue());
//			}
//			System.out.println("======");
			_delta = delta(oldPR,newPR);			
			oldPR = newPR;
			newPR = initRanking(outLinks,0.15);
		}

		
//		Random r=new Random(); 
//		  for(int i=0;i<10;i++){ 
//		    System.out.println(r.nextInt(10)); 
//		  } 
		System.out.println(i);
		return oldPR;
	}
	static void printRank(HashMap<Integer,Double> pageRank) throws IOException{
		File f = new File("workspace/pageRank1.txt");
		if(!f.exists()) {
			f.createNewFile();
			System.out.println("File created.");
		}
		FileWriter fw = new FileWriter(f);
		int i = 0;
		for(Map.Entry entry:pageRank.entrySet()){
			if((Double)entry.getValue()>2) i++;
//			System.out.println(entry.getKey()+" "+entry.getValue());
			fw.write(entry.getKey()+" "+entry.getValue()+"\n");
		}
		System.out.println("greater than 1: " +i);
		fw.flush();
		fw.close();
	}

}
