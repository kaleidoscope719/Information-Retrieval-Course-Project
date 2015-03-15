import java.util.*;
import java.io.*;
public class indexSearch {
	public static String filePath="/Users/yizuo/Documents/irp3m1test/indexM2.txt";
//	public static String filePath="/Users/yizuo/Documents/irp3m1test/111.txt";
	public static int numOfReturnPages=5;
	public static ArrayList<String> main(String[] args) throws IOException{
		String querytext="machine learning";
		ArrayList<String> queryToken=tokenizeText(querytext);
		HashMap<String,ArrayList<ArrayList<String>>> queryMap=indexForQuery(queryToken);
		HashMap<Integer,Double> resultMap=returnList(queryMap);
		List<Map.Entry<Integer,Double>> resultList=sortAndPopResult(resultMap);
		ArrayList<String> result=retrieveURL(resultList);
//		result+=retrieveText(sortAndPopResult(resultList));
		for(String s:result)
			System.out.println(s);
		return result;
	}
	public static ArrayList<String> tokenizeText(String text){
		ArrayList<String> textToken=new ArrayList<String>(Arrays.asList(text.toLowerCase().split(" ")));
//		for(String s:textToken)
//			System.out.println(s);
		return textToken;
	}
	
	public static HashMap<String,ArrayList<ArrayList<String>>> indexForQuery(ArrayList<String> queryToken) throws IOException{
		HashMap<String,ArrayList<ArrayList<String>>> queryList=new HashMap<String, ArrayList<ArrayList<String>>>();
		Collections.sort(queryToken);
		File file=new File(filePath);
		FileReader fr= new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		String line=null;
		line=br.readLine();
		if(line==null)
			return null;
		int c=0;
		for(int i=0;i<queryToken.size();i++){
//			System.out.println(queryToken.get(i));
			ArrayList<ArrayList<String>> dataSet1=new ArrayList<ArrayList<String>>();
			ArrayList<String> lineToken=tokenizeText(line);
			while(!lineToken.get(0).equals(queryToken.get(i))){
				line=br.readLine();
				if(line==null)
					break;
				lineToken=tokenizeText(line);
			}
			if(line==null)
				break;
			line=br.readLine();
			lineToken=tokenizeText(line);
			while(lineToken.get(0).matches("[0-9]+")){
				dataSet1.add(lineToken);
				line=br.readLine();
				if(line==null)
					break;
				lineToken=tokenizeText(line);
			}
			queryList.put(queryToken.get(i), dataSet1);
			if(line==null)
				break;
		}
		return queryList;
	}
	public static HashMap<Integer,Double> returnList(HashMap<String,ArrayList<ArrayList<String>>> queryList){
		HashMap<Integer,Double> res=new HashMap<Integer, Double>();
		if(queryList.isEmpty()||queryList==null){
//			System.out.println("s");
			return null;
		}
		for(String word:queryList.keySet()){
			ArrayList<ArrayList<String>> temp=queryList.get(word);
			HashMap<Integer,Double> t=new HashMap<Integer, Double>();
			for(int i=0;i<temp.size();i++){
//				System.out.println("s");
				t.put(Integer.parseInt(temp.get(i).get(0)),Double.parseDouble(temp.get(i).get(1)));
			}
			if(res.isEmpty()){
				res=t;
				continue;
			}
			HashMap<Integer,Double> res1=new HashMap<Integer, Double>(res);
			for(Integer docId:res1.keySet()){
				if(!t.containsKey(docId))
					res.remove(docId);
				else
					res.put(docId, computeScore(res.get(docId),t.get(docId)));
			}
		}
//		System.out.println(res.size());
		return res;
	}
	public static Double computeScore(Double d1,Double d2){
		return d1+d2;
	}
	public static List<Map.Entry<Integer,Double>> sortAndPopResult(HashMap<Integer,Double> res){
		List<Map.Entry<Integer,Double>> result= new ArrayList<Map.Entry<Integer, Double>>();
		result.addAll(res.entrySet());
		Collections.sort(result, new MapComparator());
		List<Map.Entry<Integer,Double>> temp = new ArrayList<Map.Entry<Integer, Double>>();
		for(int i=0;i<numOfReturnPages;i++){
			if(i<result.size()) temp.add(result.get(i));
		}
		//numOfReturnPages;
//		System.out.println(temp.size());
		return temp;
	}
	public static ArrayList<String> retrieveURL(List<Map.Entry<Integer,Double>> resultList) throws IOException{
		File f = new File("/Users/yizuo/Documents/dataIR5/url.txt");
//		System.out.println(resultList.get(0).getKey());
		ArrayList<Integer> docIds = new ArrayList<Integer>();
		ArrayList<String> result= new ArrayList<String>();
		
		for(int i=0;i<resultList.size();i++){
			docIds.add(resultList.get(i).getKey());
			//docIds.add(Integer.entry.getKey());
		}
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String currentLine = null;
		String str[] = null;
		while((currentLine = br.readLine())!=null){
			str = currentLine.split("\\s+");
			if(docIds.contains(Integer.parseInt(str[1]))){
				result.add(str[4].substring(6));
			}
		}
		
		fr.close();
		br.close();
		return result;
	}
	public static ArrayList<String> retrieveText(List<Map.Entry<Integer,Double>> resultList,String result){
		ArrayList<String> res=new ArrayList<String>();
		return res;
	}
	public static ArrayList<String> retrieveTitle(List<Map.Entry<Integer,Double>> resultList) throws IOException{
		File f = new File("");
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<Integer> docIds = new ArrayList<Integer>();
		ArrayList<String> pageTitle = new ArrayList<String>();
		for(int i=0;i<resultList.size();i++){
			docIds.add(resultList.get(i).getKey());
		}
		String currentLine = null;
		for(int i=0;i<docIds.size();i++){
			while((currentLine = br.readLine())!=null){
				if(currentLine.startsWith(docIds.get(i).toString())){
					currentLine = currentLine.substring(docIds.get(i).toString().length());
					currentLine = currentLine.trim();
					pageTitle.add(currentLine);
				}
			}
		}
		fr.close();
		br.close();
		return pageTitle;		
	}
	static class MapComparator implements Comparator <Map.Entry<Integer, Double>>{
		public int compare (Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
			Double res=(o2.getValue() - o1.getValue());
			if(res>0)
				return 1;
			else if(res==0)
				return 0;
			else
				return -1;
		}
		
	}
	
	
	
	
	
	
//	public static HashMap<Integer,ArrayList<String>> aproximateMatch(ArrayList<String> queryToken) throws IOException{
//		HashMap<Integer,ArrayList<String>> dataSet=null;
//		Collections.sort(queryToken);
//		File file=new File(filePath);
//		FileReader fr= new FileReader(file);
//		BufferedReader br=new BufferedReader(fr);
//		String line=null;
//		line=br.readLine();
//		if(line==null)
//			return null;
//		//
//		for(int i=0;i<queryToken.size();i++){
//			HashMap<Integer,ArrayList<String>> dataSet1=null;
//			ArrayList<String> lineToken=tokenizeText(line);
//			while(lineToken.get(0)!=queryToken.get(i)){
//				line=br.readLine();
//				if(line==null)
//					break;
//				lineToken=tokenizeText(line);
//			}
//			if(line==null)
//				break;
//			while(lineToken.get(0)==queryToken.get(i)){
//				dataSet1.put(Integer.parseInt(lineToken.get(1)), lineToken);
//				line=br.readLine();
//				if(line==null)
//					break;
//				lineToken=tokenizeText(line);
//			}
//			dataSet=merge(dataSet,dataSet1);
//			if(line==null)
//				break;
//		}
//		return sort(dataSet);
//	}
//	public static HashMap<Integer,ArrayList<String>> merge(HashMap<Integer,ArrayList<String>> h1,HashMap<Integer,ArrayList<String>>h2){
//		return h1;
//	}
//	public static HashMap<Integer,ArrayList<String>> sort(HashMap<Integer,ArrayList<String>> h){
//		return h;
//	}
//	public static HashMap<Integer,String> retrieveURL(HashMap<Integer,ArrayList<String>> h){
//		HashMap<Integer,String> 
//		
//	}
}
