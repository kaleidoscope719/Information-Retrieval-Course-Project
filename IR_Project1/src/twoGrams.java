import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

//import WordFreqs.MapComparator;
public class twoGrams {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//HashMap<String,Integer> twoGram = new HashMap<String,Integer>(tokenizeFileAndCompute(new File("src/pride-and-prejudice.txt")));
		HashMap<String,Integer> twoGram = new HashMap<String,Integer>(tokenizeFileAndCompute(new File("src/test.txt")));
		print(twoGram);	
		}
	
	public static HashMap<String,Integer> tokenizeFileAndCompute (File TextFile) {
		HashMap<String,Integer> twoGrams = new HashMap<String,Integer>();
		ArrayList<String> list=new ArrayList<String>();
		RandomAccessFile input = null;		
		try{			
			input = new RandomAccessFile(TextFile,"r");
			long fileLength = TextFile.length();
			int n = (int)(fileLength/Integer.MAX_VALUE);
			FileChannel fc = input.getChannel();
			long segment = TextFile.length()/(n+1);
			
			for(int i=0;i<=n;i++){				
				if(i<n){
					MappedByteBuffer mapBuff = fc.map(FileChannel.MapMode.READ_ONLY, segment*i, segment);
					int bufferSize = 20*1024*1024;
					int contentLength = 0;
					byte[] content = new byte[bufferSize];
					for(int offset = 0;offset<segment;offset+=bufferSize){
						if(segment - offset>=bufferSize){
							contentLength = bufferSize;
							for(int j = 0;j<bufferSize;j++)
								content[j] = mapBuff.get(offset+j);
						}else{
							contentLength = (int) (segment - offset);
							for(int j =0;j<segment-offset;j++)
								content[j] = mapBuff.get(offset+j);
						}
						
						list.addAll(Arrays.asList(new String(content,0,contentLength).replaceAll("[^0-9A-Za-z]"," ").toLowerCase().split("\\s+")));
						twoGrams = combineHashMap(twoGrams,computeTwoGramFrequencies(list));
						
						list.clear();
						
					}
				}else{
					MappedByteBuffer mapBuff = fc.map(FileChannel.MapMode.READ_ONLY, segment*i, TextFile.length()-segment*i);
					int bufferSize = 20*1024*1024;
					int contentLength = 0;
					byte[] content = new byte[bufferSize];
					for(int offset = 0;offset<segment;offset+=bufferSize){
						if(segment - offset>=bufferSize){
							contentLength = bufferSize;
							for(int j = 0;j<bufferSize;j++)
								content[j] = mapBuff.get(offset+j);
						}else{
							contentLength = (int) (segment - offset);
							for(int j =0;j<segment-offset;j++)
								content[j] = mapBuff.get(offset+j);
					}
						String temp = new String(content,0,contentLength);
						list.addAll(Arrays.asList(temp.replaceAll("[^0-9A-Za-z]"," ").toLowerCase().split("\\s+")));
						twoGrams = combineHashMap(twoGrams,computeTwoGramFrequencies(list));
						print(twoGrams);
						list.clear();
					}
				}
			}							
		
			System.out.println("fileLength = "+fileLength);
			System.out.println("list.size() = "+list.size());
			input.close();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(input!=null){
				try{
					input.close();
				}catch(IOException e1){
					e1.printStackTrace();
				}
			}
		}
		return twoGrams;
	}
	
	
	public static HashMap<String,Integer> computeTwoGramFrequencies(ArrayList<String> tokenList){
		HashMap<String,Integer> twoGram = new HashMap<String,Integer>();
		StringBuilder sb = new StringBuilder();
		Iterator<ArrayList<String>> iter = new NgramIterator(2,tokenList);
		while(iter.hasNext()){
			ArrayList<String> temp = iter.next();
			sb.append(temp.get(0)+temp.get(1));
			String two_gram = sb.toString();
			sb.delete(0, sb.length());
			twoGram.put(two_gram, twoGram.containsKey(two_gram)? (twoGram.get(two_gram)+1):1);
		}
		return twoGram;
	}
	
	public static void print(HashMap<String,Integer> result){
		ArrayList<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(result.entrySet());
		Collections.sort(entryList,new MapComparator());
		for(Map.Entry<String, Integer> entry:entryList)
			System.out.println(entry.getKey()+" - "+entry.getValue());
//		for(int i=0;i<100;i++)
//			{
//				System.out.println(entryList.get(i).getKey()+" - "+entryList.get(i).getValue());
//			}		
	}
	
	public static HashMap<String,Integer> combineHashMap(HashMap<String,Integer> hash1, HashMap<String,Integer> hash2){
		Iterator iter = hash2.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry temp = (Map.Entry) iter.next();
			String word = (String)temp.getKey();
			Integer count = (Integer)temp.getValue();
			hash1.put(word, hash1.containsKey(word)? hash1.get(word)+count:count);
					}
		return hash1;
	}
	
	public static class NgramIterator implements Iterator<ArrayList<String>>{
		int pos = 0,n;
		ArrayList<String> wordsList;
		public NgramIterator(int n,ArrayList<String> list){
			this.n=n;
			wordsList = list;
		}
		public boolean hasNext(){
			return pos<wordsList.size()-n+1;
		}
		public ArrayList<String> next(){
			ArrayList<String> nGram = new ArrayList<String>();
			for(int i=pos;i<pos+n;i++)
				nGram.add((i>pos? " ":"")+ wordsList.get(i));
			pos++;
			return nGram;				
		}
		public void remove(){
			throw new UnsupportedOperationException();
		}
	}
	
	static class MapComparator implements Comparator <Map.Entry<String, Integer>>{
		public int compare (Map.Entry<String,Integer> o1, Map.Entry<String,Integer>o2) {
			return o2.getValue().intValue() - o1.getValue().intValue();
			}
		}

}
