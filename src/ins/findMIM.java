package ins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class findMIM {
	public void find_mim(String sam_file,String mim_file) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(sam_file));
		BufferedWriter bw = new BufferedWriter(new FileWriter(mim_file));
		String line = null;
		Pattern pattern = Pattern.compile("^[\\d]{1,}M[\\d]{1,}I[\\d]{1,}M$");
		while((line = br.readLine()) != null){
			if(line.startsWith("@")){
				continue;
			}else{
				String[] temp = line.split("\t");
				String cigar = temp[5];
				Matcher matcher = pattern.matcher(cigar);
				if(matcher.find()){
					bw.write(line);
					bw.newLine();
				}
			}
			
		}
		bw.flush();
		br.close();
		bw.close();
		
	}
	public static void main(String[] args) throws Exception {
		findMIM fmim = new findMIM();
		fmim.find_mim("/media/xie/0009A639000F3A82/ins/test_5_6/10times.sam", "/media/xie/0009A639000F3A82/ins/test_5_6/10times.mim.sam");
	}
}
