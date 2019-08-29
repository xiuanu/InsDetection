package ins;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class insertsize {
	public int AVG;
	public int SD;
	void calc_stats(String sam_file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(sam_file));
		String line=null;
		long sum=0;
		Pattern pattern = Pattern.compile("^[\\d]{1,}M$");
		ArrayList<Integer> insertlist=new ArrayList<Integer>();
		while((line = br.readLine()) != null){
			if(line.startsWith("@")){
				continue;
			}else{
				String[] temp = line.split("\t");
				int insertsize = Integer.parseInt(temp[8]);
				String cigar = temp[5];
				int q=Integer.parseInt(temp[1]);
				Matcher matcher = pattern.matcher(cigar);
				if(insertsize>0 && q==99 && matcher.find()) 
					{
						insertlist.add(insertsize);
					}
			}
		}
		br.close();
		for(int i=0;i<insertlist.size();i++)
		{
			sum+=insertlist.get(i);
		}
		

		this.AVG=(int)(sum/insertlist.size());
		long dVar=0;
		for(int i=0;i<insertlist.size();i++)
		{
			dVar+=(insertlist.get(i)-this.AVG)*(insertlist.get(i)-this.AVG);
			
		}
		System.out.println(dVar);
		dVar=dVar/insertlist.size();
		System.out.println(dVar);
		this.SD=(int)Math.sqrt(dVar);
		System.out.println(this.AVG+" "+this.SD);
		
	}
	public static void main(String[] args) throws IOException {
		insertsize i=new insertsize();
		i.calc_stats("/media/xie/0009A639000F3A82/内部插入仿真/2.sam");
	}
	
}
