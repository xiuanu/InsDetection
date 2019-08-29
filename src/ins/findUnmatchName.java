package ins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class findUnmatchName {
	public void findunmatchname(String samfile,String unmatchnamefile,int insertsize) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(samfile));
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(unmatchnamefile));
		ArrayList<String> as=new ArrayList<String>();
		String line = null;
		boolean flagUnmatch=false;
		while((line = br.readLine()) != null){
			if(line.startsWith("@")){
				continue;
			}else{
				String[] temp = line.split("\t");
				String name=temp[0];
				if(as.isEmpty()) {
					int insert=Integer.valueOf(temp[8]);
					insert=insert>0?insert:-insert;
					if(insert>insertsize*2)
						flagUnmatch=true;
					as.add(line);
				}
				else {
					String pre=as.get(0);
					String[] temp_pre = pre.split("\t");
					String name_pre=temp_pre[0];
					if(name.equals(name_pre)) {
						int insert=Integer.valueOf(temp[8]);
						insert=insert>0?insert:-insert;
						if(insert>insertsize*2)
							flagUnmatch=true;
						as.add(line);
					}
					else {
						if(flagUnmatch==true)
						{
							String[] temp1=as.get(0).split("\t");
							bw1.write(temp1[0]);
							bw1.newLine();
							flagUnmatch=false;
						}
						as.clear();
						int insert=Integer.valueOf(temp[8]);
						insert=insert>0?insert:-insert;
						if(insert>insertsize*2)
							flagUnmatch=true;
						as.add(line);	
					}
				}
			}
			
		}
		if(!as.isEmpty()) {
			if(flagUnmatch==true)
			{
				String[] temp1=as.get(0).split("\t");
				bw1.write(temp1[0]);
				bw1.newLine();
			}
		}
		bw1.flush();
		br.close();
		bw1.close();
	}
	public static void main(String[] args) throws IOException {
		findUnmatchName f=new findUnmatchName();
		f.findunmatchname("/media/xie/0009A639000F3A82/ins/test3/test3.sam", "/media/xie/0009A639000F3A82/ins/test3/unmatchname.txt", 500);
	}
}
