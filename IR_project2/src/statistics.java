import java.io.*;
import java.util.*;

public class statistics {
	public static void main(String args[]){
		try{
		int count=0;
		String filePath = "/Users/xuruoyun/Documents/dataIR3/subDomain.txt";
		File file = new File(filePath);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		StringBuilder sb = new StringBuilder();
		String line=null;
		while((line=br.readLine())!=null){
			count++;
		}
		System.out.println(count);
		br.close();
		fr.close();
	}catch(Exception e){
		System.out.println("error");
	}
	}
}
