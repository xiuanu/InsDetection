package ins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class fastqRename {
	public  void fastqrename(String fq1,String fq2,String fq1_out,String fq2_out) throws IOException {
		BufferedReader br1 = new BufferedReader(new FileReader(fq1));
		BufferedReader br2 = new BufferedReader(new FileReader(fq2));
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(fq1_out));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(fq2_out));
		String line1=null;
		String line2=null;
		while((line1 = br1.readLine())!=null) {
			line2=br2.readLine();
			bw1.write(line1);
			bw1.newLine();
			bw2.write(line1);
			bw2.newLine();
			for(int i=0;i<3;i++)
			{
				line1=br1.readLine();
				line2=br2.readLine();
				bw1.write(line1);
				bw1.newLine();
				bw2.write(line2);
				bw2.newLine();
			}
		}
		bw2.flush();
		bw1.flush();
		br1.close();
		br2.close();
		bw2.close();
		bw1.close();
		
	}
}
