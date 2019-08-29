package ins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class fastaToLow {
	public void tolow(String fasta,String out) throws IOException {
		BufferedReader br1 = new BufferedReader(new FileReader(fasta));
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(out));
		String line=null;
		while((line = br1.readLine())!=null) {
			if(line.startsWith(">")) {
				bw1.write(line);
				bw1.newLine();
				continue;
			}
			char a[]=line.toCharArray();
			for(int i=0;i<a.length;i++)
			{
				if(a[i]=='A') 
					a[i]='a';
				if(a[i]=='T') 
					a[i]='t';
				if(a[i]=='G') 
					a[i]='g';
				if(a[i]=='C') 
					a[i]='c';
			}
			String str=new String(a);
			bw1.write(str);
			str=null;
			bw1.newLine();
		}
		bw1.flush();
		br1.close();
		bw1.close();
	}
}

