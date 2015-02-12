import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.MappedByteBuffer;
import java.util.*;
//import java.util.regex.*;
//import java.nio.charset.Charset;
//import java.nio.file.Files;
//import java.nio.file.Paths;
public class Utilities {
	public static int j = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//File input = new File("src/pride-and-prejudice.txt");
		File input = new File("/Users/xuruoyun/Downloads/testbig.txt");
		tokenizeFile(input);
		
	}
	public static ArrayList<String> tokenizeFile (File TextFile) {
		ArrayList<String> list=new ArrayList<String>();
		RandomAccessFile input = null;		
		try{
			input = new RandomAccessFile(TextFile,"r");
			long fileLength = TextFile.length();
			int n = (int)(fileLength/Integer.MAX_VALUE);
			System.out.println("fileLength = "+fileLength);
			System.out.println("n = "+n);
			FileChannel fc = input.getChannel();
			System.out.println("fc.size= "+5/3+" "+5/2+" "+2/3);
			System.out.println("fc.size/n+1= "+fc.size()/(n+1));
			for(int i=0;i<=n;i++){
				long segment = fc.size()/(n+1);
				if(i<n){
					MappedByteBuffer mapBuff = fc.map(FileChannel.MapMode.READ_ONLY, segment*i, segment);
					int bufferSize = 20*1024*1024;
					byte[] content = new byte[bufferSize];
					for(int offset = 0;offset<segment;offset+=bufferSize){
						if(segment - offset>=bufferSize){
							for(int j = 0;j<bufferSize;j++)
								content[j] = mapBuff.get(offset+j);
						}else{
							for(int j =0;j<segment-offset;j++)
								content[j] = mapBuff.get(offset+j);
					}
						list.addAll(Arrays.asList(new String(content,0,bufferSize).replaceAll("[^0-9A-Za-z]"," ").toLowerCase().split("\\s+")));	
						print(list);
						list.clear();//clear list for every buffer input
					}
				}else{
					MappedByteBuffer mapBuff = fc.map(FileChannel.MapMode.READ_ONLY, segment*i, fc.size()-segment*i);
					int bufferSize = 20*1024*1024;
					byte[] content = new byte[bufferSize];
					for(int offset = 0;offset<segment;offset+=bufferSize){
						if(segment - offset>=bufferSize){
							for(int j = 0;j<bufferSize;j++)
								content[j] = mapBuff.get(offset+j);
						}else{
							for(int j =0;j<segment-offset;j++)
								content[j] = mapBuff.get(offset+j);
					}
						list.addAll(Arrays.asList(new String(content,0,bufferSize).replaceAll("[^0-9A-Za-z]"," ").toLowerCase().split("\\s+")));
						print(list);
						list.clear();
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
		return list;
	}
	
	public static void print (List<String> Token){
		j++;
		//System.out.println(Token.get(i++));
		int size = Token.size();
		System.out.println("asdfg");
		for(int i =0;i<size;i++){
			System.out.println(Token.get(i));
		}
	}
}

