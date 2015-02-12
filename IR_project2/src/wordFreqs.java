import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;




public class wordFreqs {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		HashMap<String,Integer> frequencies = wordFreqs.readFile("/Users/xuruoyun/Documents/dataIR3/text/");
		ArrayList<Map.Entry<String, Integer>> entryList = wordFreqs.sort(frequencies);
		wordFreqs.fileWrite("src/domain_wordFreqs_result.txt", entryList);
//		for(int i=0;i<500;i++)
//			System.out.println(entryList.get(i).getKey()+" - "+entryList.get(i).getValue());
//		for(int i=0;i<25;i++){
//			System.out.println()
//		}
	}

	public static HashMap<String,Integer> readFile(String folderPath) throws IOException{
		File folder = new File(folderPath);
		File[] fileList= folder.listFiles();
		FileReader fr = new FileReader("src/stop_words.txt");
	    BufferedReader br = new BufferedReader(fr);
		
	    String currentLine = null;
	    HashMap<String,Integer> _wordFreqs = new HashMap<String,Integer>();
	    HashSet<String> stopWords = new HashSet<String>();
	    ArrayList<String> wordList = new ArrayList<String>();
	    //String currentLine = null;
	    while((currentLine=br.readLine())!=null){
	    	//System.out.println(br.readLine());
	    	stopWords.addAll(Arrays.asList(currentLine.split(",")));
	    }
	    //System.out.println("stoplist: "+stopWords.size());
	    int i=1;
		for(File path:fileList){
			if(!path.equals("/Users/xuruoyun/Documents/dataIR2/text/text9955.txt")){
				fr = new FileReader(path);
			    br = new BufferedReader(fr);		    
			    while (br.ready()) {
			    	wordList.clear();
			    	//sb = null;
			    	//StringBuilder sb = new StringBuilder();
			    	//if(sb!=null) sb.delete(0, sb.length());
			    	while((currentLine=br.readLine())!=null) {
			    		//System.out.println(currentLine);
			    		//sb.append(currentLine+" ");
			    	
			    	//System.out.println(sb.toString());
			    	wordList.addAll(Arrays.asList(currentLine.toLowerCase().replaceAll("[^A-Za-z]", " ").split("\\s+")));
			    	
			    	
			    	
			    	}
			    	System.out.println(i);
			    	wordList.removeAll(stopWords);
			    	_wordFreqs = wordFreqs.frequencies(wordList,_wordFreqs);
			    }
			    i++;
			}
			}
			//System.out.println(path);
		    
		return _wordFreqs;
	}
	public static HashMap<String,Integer> frequencies(ArrayList<String> wordList, HashMap<String,Integer>wordFreqs){
		for(String word:wordList){
			if(word.length()>1)
			wordFreqs.put(word, wordFreqs.containsKey(word)? wordFreqs.get(word)+1:1);
		}
		return wordFreqs;
	}
	
	public static ArrayList<Map.Entry<String, Integer>> sort(HashMap<String, Integer> wordFreqs){
		
		ArrayList<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>();
		if(wordFreqs == null) return entryList;
		entryList.addAll(wordFreqs.entrySet());
		Collections.sort(entryList,new MapComparator());
		return entryList;
	}
	static class MapComparator implements Comparator <Map.Entry<String, Integer>>{
		public int compare (Map.Entry<String,Integer> o1, Map.Entry<String,Integer>o2) {
			return o2.getValue().intValue() - o1.getValue().intValue();
		}
	}
	
	static void fileWrite(String filePath, ArrayList<Map.Entry<String, Integer>> resultList){//string or file?
		FileWriter fw = null;
		BufferedWriter bw = null;
		try{
			System.out.println("fileWrite");
			File f = new File(filePath);
			fw = new FileWriter(filePath);
			bw = new BufferedWriter(fw);
			if(!f.exists()){
				f.createNewFile();
				System.out.println("no file exists \nfile created");
			}
			
			for(int i=0;i<500;i++){
				//System.out.println(resultList.get(i).getKey()+" - "+resultList.get(i).getValue());
				bw.write(resultList.get(i).getKey().toString()+" - "+resultList.get(i).getValue().toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
			System.out.println("fileWrite-end");
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			System.out.print("error");
		}finally{
			if(fw != null){
				try{
					bw.close();
					fw.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
}
