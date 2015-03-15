package com;
import java.util.*;
import java.io.*;
public class indexSearch {
	public static String filePath="/Users/yizuo/Documents/irp3m1test/indexM2.txt";
//	public static String filePath="/Users/yizuo/Documents/irp3m1test/111.txt";
	public static int numOfReturnPages=5;
	public static List<Map.Entry<Integer,Double>> main(String[] args) throws IOException{
//		String querytext="jiaozi";
		String querytext=args[0];
		ArrayList<String> queryToken=tokenizeText(querytext);
		HashMap<String,ArrayList<ArrayList<String>>> queryMap=indexForQuery(queryToken);
//		System.out.print(queryMap.size());
		HashMap<Integer,Double> resultMap=returnListWithTfidf(queryMap);
//		resultMap = returnListWithVectorS(queryMap,queryToken);
		HashMap<Integer,Double> titleScore = returnListWithTitleScore(resultMap,queryToken);
		resultMap = combineScore(resultMap,titleScore);
		List<Map.Entry<Integer,Double>> resultList=sortAndPopResult(resultMap);
//		if(resultList.size()==0) System.out.println("zero");
		return resultList;
//		ArrayList<String> result=retrieveURL(resultList);
////		result+=retrieveText(sortAndPopResult(resultList));
//		for(String s:result)
//			System.out.println(s);
//		return result;
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
		int start=0, num=0;
		for(start=0;start<queryToken.size();start++){
			try{
				num = Integer.parseInt(queryToken.get(start));
				break;
			}catch(Exception e){
				continue;
			}
		}
		for(int i=num==0?0:start+1;i<queryToken.size();i++){
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
//		System.out.print(queryList.size()+"\n");
		return queryList;
	}
	public static HashMap<Integer,Double> returnListWithTfidf(HashMap<String,ArrayList<ArrayList<String>>> queryList){
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
	
	public static HashMap<Integer,Double> returnListWithTitleScore(HashMap<Integer,Double> score,ArrayList<String> queryToken) throws IOException{
		HashMap<Integer,Double> res = new HashMap<Integer,Double>();
		File file = new File("/Users/yizuo/Documents/dataIR5/title.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while((line=br.readLine())!=null){
			String[] temp = line.toLowerCase().split("\\s+");
			int key=0;
			try{
				key = Integer.parseInt(temp[0]);
			}catch(Exception e){
				continue;
			}
			

			if(score.containsKey(key)){
				double count=0.0;
				for(int i=0;i<temp.length;i++){
//					System.out.println(temp[i]);
					if(queryToken.contains(temp[i]))
						count +=1;
				}
//				if(count>=1)
//					System.out.println(key);
				res.put(Integer.parseInt(temp[0]), count);
			}
		}
		return res;
	}
	public static HashMap<Integer,Double> returnListWithVectorS(HashMap<String,ArrayList<ArrayList<String>>> queryList, ArrayList<String> queryToken){
		HashMap<Integer,Double> res=new HashMap<Integer, Double>();
		HashMap<String,Double> queryVector = new HashMap<String,Double>();
		HashMap<String,HashMap<String,Double>> documentVector = new HashMap<String,HashMap<String,Double>>();
		int len = queryToken.size();
		for(String word:queryList.keySet()){
			double count = 0.0;
			for(String word1:queryToken){
				if(word==word1)
					count++;
			}
			queryVector.put(word, count/(double)len);
			for(int i=0;i<queryList.get(word).size();i++){
				if(!documentVector.containsKey(queryList.get(word).get(i).get(0))){
					HashMap<String,Double> temp = new HashMap<String,Double>();
					documentVector.put(queryList.get(word).get(i).get(0), temp);
				}
//				System.out.println(queryList.get(word).get(i).get(1));
				documentVector.get(queryList.get(word).get(i).get(0)).put(word, Double.parseDouble(queryList.get(word).get(i).get(1)));
			}
		}
		res = computeVectorS(queryVector,documentVector);
		return res;
	}
	
	public static HashMap<Integer,Double> computeVectorS(HashMap<String,Double> queryVector,HashMap<String,HashMap<String,Double>> documentVector){
		HashMap<Integer,Double> res = new HashMap<Integer,Double>();
		for(String docID:documentVector.keySet()){
			double vectorScore = 0.0;
			double queryVectorLength = 0.0;
			double documentVectorLength = 0.0;
			for(String key:queryVector.keySet()){
				queryVectorLength += queryVector.get(key)*queryVector.get(key);
				if(!documentVector.get(docID).containsKey(key))
					continue;
//				System.out.println(docID+" "+documentVector.get(docID).get(key));
				vectorScore += queryVector.get(key)*documentVector.get(docID).get(key);
				documentVectorLength += documentVector.get(docID).get(key)*documentVector.get(docID).get(key);
				
			}
			if(documentVectorLength==0.0)
				continue;
			vectorScore = vectorScore/Math.sqrt(queryVectorLength)/Math.sqrt(documentVectorLength);
			res.put(Integer.parseInt(docID), vectorScore);
		}
		return res;
	}
	
	public static HashMap<Integer,Double> combineScore(HashMap<Integer,Double> score,HashMap<Integer,Double> titleScore) throws IOException{
		File file = new File("/Users/yizuo/Documents/irp3m1test/pageRank.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while((line=br.readLine())!=null){
			String[] temp = line.split("\\s+");
			if(score.containsKey(Integer.parseInt(temp[0]))){
//				System.out.println(temp[1]);
				score.put(Integer.parseInt(temp[0]), score.get(Integer.parseInt(temp[0]))+Math.sqrt(Math.sqrt(Double.parseDouble(temp[1])))+10*titleScore.get(Integer.parseInt(temp[0])));
			}
//			ArrayList<String> temp = tokenizeText(line);
//			if(score.containsKey(Integer.parseInt(temp.get(0)))){
//				score.put(Integer.parseInt(temp.get(0)),score.get(Integer.parseInt(temp.get(0))+Double.parseDouble(temp.get(1))));
//			}
		}
		return score;
	}
	
	public static Double computeScore(double d1,double d2){
		double res=0.0;
		res=(double)(d1*100000000+d2*100000000)/100000000.0;
		return res;
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
//		File f = new File("src/url.txt");
//		System.out.println(resultList.get(0).getKey());
		ArrayList<Integer> docIds = new ArrayList<Integer>();
		ArrayList<String> result= new ArrayList<String>();
		String[] temp=new String[resultList.size()];
		
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
					temp[docIds.indexOf(Integer.parseInt(str[1]))]=str[4].substring(6);
					System.out.print(str[4].substring(6)+"\n");
//					result.add(str[4].substring(6));
				}
			}
		result=new ArrayList<String>(Arrays.asList(temp));
		fr.close();
		br.close();
		return result;
	}
	public static ArrayList<String> retrieveText(List<Map.Entry<Integer,Double>> resultList,String query) throws IOException{
		ArrayList<String> result=new ArrayList<String>();
		String[] temp=new String[resultList.size()];
		String line="";
		ArrayList<String> queryToken=new ArrayList<String>(Arrays.asList(query.toLowerCase().split(" ")));
		for(int i=0;i<resultList.size();i++){
			String text="";
			String textRes="";
			File file=new File("/Users/yizuo/Documents/dataIR5/text/text"+resultList.get(i).getKey()+".txt");
			FileReader fr=new FileReader(file);
			BufferedReader br=new BufferedReader(fr);
			
			while((line=br.readLine())!=null){
				text+=line;
				}
				ArrayList<String> textToken=new ArrayList<String>(Arrays.asList(text.toLowerCase().split("\\s+")));
				int j=textToken.indexOf(queryToken.get(0));
				int s=((j-20)<0)?0:(j-20);
				int e=((j+20)>=textToken.size())?textToken.size():(j+20);
				for(int k=s;k<e;k++){
					textRes+=textToken.get(k)+" ";
				}
				result.add("... "+textRes+" ...");
				fr.close();
				br.close();
//				temp[i]+=line;
		}
			
		return result;
	}
	public static ArrayList<String> retrieveTitle(List<Map.Entry<Integer,Double>> resultList) throws IOException{
		File f = new File("/Users/yizuo/Documents/dataIR5/title.txt");
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<Integer> docIds = new ArrayList<Integer>();
		ArrayList<String> pageTitle = new ArrayList<String>();
		String[] temp=new String[resultList.size()];
		for(int i=0;i<resultList.size();i++){
			docIds.add(resultList.get(i).getKey());
		}
		String currentLine = null;
		
			while((currentLine = br.readLine())!=null){
				String[] tempTitle = currentLine.toLowerCase().split("\\s+");
				int numID = 0;
				try{
					numID = Integer.parseInt(tempTitle[0]);
				}catch(Exception e){
					continue;
				}
				for(int i=0;i<docIds.size();i++){
				if(numID == docIds.get(i)){
//					System.out.print(numID+"\n");
					currentLine = currentLine.substring(docIds.get(i).toString().length());
					currentLine = currentLine.trim();
					temp[i]=currentLine;
				}
			}
		}
		fr.close();
		br.close();
		pageTitle=new ArrayList<String>(Arrays.asList(temp));
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
