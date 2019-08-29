package ins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class confirmPosition {
	void confirmposition(String add_sequence_file,String confirmfile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(add_sequence_file));
		ArrayList<AddSeqUnit> as=new ArrayList<AddSeqUnit>();
		String line_pos=null;
		while((line_pos=br.readLine())!=null) {
			String temp[]=line_pos.split("\t");
			AddSeqUnit p=new AddSeqUnit(Integer.valueOf(temp[0]),temp[1]);
			as.add(p);
		}
		br.close();
		Collections.sort(as);
		BufferedWriter bw = new BufferedWriter(new FileWriter(confirmfile));
		int sum=0;
		for(AddSeqUnit s:as) {
			int pos1=s.pos+s.seqlength+sum+1;
			sum=sum+s.seqlength;
			bw.write(String.valueOf(pos1));
			bw.write("\t");
			bw.write("11");
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}
	public static void main(String[] args) throws IOException {
		confirmPosition c=new confirmPosition();
		c.confirmposition("/media/xie/0009A639000F3A82/ins/test2/pos_add.txt", "/media/xie/0009A639000F3A82/ins/test2/confirm.txt");
	}
}




