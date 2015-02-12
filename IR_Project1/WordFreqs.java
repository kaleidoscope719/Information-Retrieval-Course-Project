import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.Map.Entry;
public class WordFreqs {
	
	//public static int n = 1;

	//public static HashMap<String,Integer> wordFreqs = new HashMap<String,Integer>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//HashMap<String,Integer> wordFreqs = new HashMap<String,Integer>();
		//tokenizeFileAndCompute(new File("src/pride-and-prejudice.txt"));
		tokenizeFileAndCompute(new File(args[0]));
	}
	//public static ArrayList<String> tokenizeFile (File TextFile) {
	public static HashMap<String,Integer> tokenizeFileAndCompute (File TextFile) {
		HashMap<String,Integer> wordFreqs = new HashMap<String,Integer>();
		ArrayList<String> list=new ArrayList<String>();
		FileInputStream input = null;		
		try{			
			input = new FileInputStream(TextFile);
			long fileLength = TextFile.length();
			int n = (int)(fileLength/Integer.MAX_VALUE);
			FileChannel fc = input.getChannel();
			long segment = TextFile.length()/(n+1);
			//System.out.println(Integer.MAX_VALUE+" n");
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
						wordFreqs = combineHashMap(computeWordFrequencies(list),wordFreqs);
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
						list.addAll(Arrays.asList(new String(content,0,contentLength).replaceAll("[^0-9A-Za-z]"," ").toLowerCase().split("\\s+")));
						wordFreqs = combineHashMap(computeWordFrequencies(list),wordFreqs);
						print(wordFreqs);
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
		return wordFreqs;
	}
	
	
	public static HashMap<String,Integer> computeWordFrequencies(ArrayList<String> token){
		HashMap<String,Integer> wordFreqs = new HashMap<String,Integer>();
		for(String word:token){	
			if(word.length()>0)
			wordFreqs.put(word, wordFreqs.containsKey(word)? (wordFreqs.get(word)+1):1);			
		}
		return wordFreqs;
	}
	public static void print(HashMap<String,Integer> wordFreqs){
	
		ArrayList<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(wordFreqs.entrySet());
		Collections.sort(entryList,new MapComparator());
		for(Map.Entry<String, Integer> entry:entryList){
			System.out.println(entry.getKey()+" - "+entry.getValue());
		}
//		for(int i=0;i<100;i++)
//		{
//			System.out.println(entryList.get(i).getKey()+" - "+entryList.get(i).getValue());
//		}
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
	
	static class MapComparator implements Comparator <Map.Entry<String, Integer>>{
		public int compare (Map.Entry<String,Integer> o1, Map.Entry<String,Integer>o2) {
			return o2.getValue().intValue() - o1.getValue().intValue();
			}
		}
	
}
