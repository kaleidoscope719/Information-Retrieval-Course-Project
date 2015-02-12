import java.io.*;
import java.util.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Palindrome {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//ArrayList<String> tokenList = Utilities.tokenizeFile(new File("src/pride-and-prejudice.txt"));
		HashMap<String,Integer> Palindromes = tokenizeFile(new File("src/test.txt"));
		print(Palindromes);		
	}
	
	public static HashMap<String,Integer> tokenizeFile (File TextFile) {
		//ArrayList<String> list=new ArrayList<String>();
		HashMap<String,Integer> Palindromes = new HashMap<String,Integer>();
		RandomAccessFile input = null;		
		try{
			input = new RandomAccessFile(TextFile,"r");
			long fileLength = TextFile.length();
			int n = (int)(fileLength/Integer.MAX_VALUE);
			System.out.println("fileLength = "+fileLength);
			System.out.println("n = "+n);
			FileChannel fc = input.getChannel();
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<=n;i++){
				long segment = fc.size()/(n+1);
				if(i<n){
					MappedByteBuffer mapBuff = fc.map(FileChannel.MapMode.READ_ONLY, segment*i, segment);
					int bufferSize = 50*1024;
					byte[] content = new byte[bufferSize];
					for(int offset = 0;offset<segment;offset+=bufferSize){
						if(segment - offset>=bufferSize){
							for(int j = 0;j<bufferSize;j++)
								content[j] = mapBuff.get(offset+j);
						}else{
							for(int j =0;j<segment-offset;j++)
								content[j] = mapBuff.get(offset+j);
					}
						sb.append(new String(content,0,bufferSize).replaceAll("[^0-9A-Za-z]"," ").replaceAll("\\s+"," ").toLowerCase()+" ");	
						Palindromes = combineHashMap(Palindromes,Palindromes(sb.toString()));
						sb.delete(0, sb.length());
						
					
					}
				}else{
					MappedByteBuffer mapBuff = fc.map(FileChannel.MapMode.READ_ONLY, segment*i, fc.size()-segment*i);
					int bufferSize = 50*1024;
					byte[] content = new byte[bufferSize];
					for(int offset = 0;offset<segment;offset+=bufferSize){
						if(segment - offset>=bufferSize){
							for(int j = 0;j<bufferSize;j++)
								content[j] = mapBuff.get(offset+j);
						}else{
							for(int j =0;j<segment-offset;j++)
								content[j] = mapBuff.get(offset+j);
					}
						//sb.append(new String(content,0,bufferSize).replaceAll("[^0-9A-Za-z\\s+]"," ").toLowerCase()+" ");
						
						sb.append(new String(content,0,bufferSize).replaceAll("[^0-9A-Za-z]"," ").replaceAll("\\s+"," ").toLowerCase()+" ");
						Palindromes = combineHashMap(Palindromes,Palindromes(sb.toString()));
						sb.delete(0, sb.length());
					}
				}
			}

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
		System.out.println(Palindromes.size());
		return Palindromes;
	}
	

	public static HashMap<String,Integer> Palindromes(String list){
		HashMap<String,Integer> Palindromes = new HashMap<String,Integer>();
		int length = list.length();		
		for (int j=0;j<length;j++){
			int l=j-1;
			int r=j+1;
			while(l>= 0&&r<=length-1){
				if(list.charAt(l)==' '&&list.charAt(r)==' '){
					Palindromes.put(list.substring(l+1, r), Palindromes.containsKey(list.substring(l+1, r))?Palindromes.get(list.substring(l+1, r))+1:1);
					l--;r++;
				}
				else if(list.charAt(l)==' '&&list.charAt(r)!=' ')
					l--;
				else if(list.charAt(r)==' '&&list.charAt(l)!=' ')
					r++;
				else if(list.charAt(r)==list.charAt(l)){
					l--;r++;
				}
				else 
					break;
			}
		}		
		for (int k=0;k<length-1;k++){
			int m=k+1;
			int l=k-1;
			int r=m+1;
			if(list.charAt(k)==list.charAt(m)){
				while(l>=0&&r<=length-1){
					if(list.charAt(l)==' '&&list.charAt(r)==' '){
						Palindromes.put(list.substring(l+1, r), Palindromes.containsKey(list.substring(l+1, r))?Palindromes.get(list.substring(l+1, r))+1:1);
						l--;r++;
					}
					else if(list.charAt(l)==' '&&list.charAt(r)!=' ')
						l--;
					else if(list.charAt(r)==' '&&list.charAt(l)!=' ')
						r++;
					else if(list.charAt(r)==list.charAt(l))
					{
						l--;r++;
					}
					else 
						break;
				}
			}
		}
		return Palindromes;
	}	
	
public static void print(HashMap<String,Integer> palindrome){
		
		ArrayList<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(palindrome.entrySet());
		Collections.sort(entryList,new MapComparator());
		for(Map.Entry<String, Integer> entry:entryList)
			if(entry.getKey().toString().length()>1)
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
	
	public static boolean isPalindrome(String str){
		int length = str.length();		
		if(length<=1) return true;
		else{
			int start = 0;
			int end = length -1;
			while(start<end){
				if(str.charAt(start++)!=str.charAt(end--))
				{
					return false;
				}
			}
		} return true;
	}
	
	static class MapComparator implements Comparator <Map.Entry<String, Integer>>{
		public int compare (Map.Entry<String,Integer> o1, Map.Entry<String,Integer>o2) {
			return o2.getValue().intValue() - o1.getValue().intValue();
			}
		}
}
