package ins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;


public class findPosition {
	ArrayList<positionUnit> findposition(String matchfile,String positionfile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(matchfile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(positionfile));
		ArrayList<samUnit> as=new ArrayList<samUnit>();
		ArrayList<positionUnit> ps=new ArrayList<positionUnit>();
		String line=null;
		String state=null;
		boolean smflag=false;
		boolean msflag=false;
		Pattern patternms = Pattern.compile("^[\\d]{1,}M[\\d]{1,}S$");
		Pattern patternsm = Pattern.compile("^[\\d]{1,}S[\\d]{1,}M$");
		while((line = br.readLine()) != null){
			if(line.startsWith("@")){
				continue;
			}else{
				samUnit s=new samUnit(line);
				if(as.isEmpty()) {
					if(patternsm.matcher(s.cigar).find())
						smflag=true;
					if(patternms.matcher(s.cigar).find())
						msflag=true;
					as.add(s);
				}
				else {
					samUnit pre=as.get(0);
					if(s.name.equals(pre.name)) {
						if(patternsm.matcher(s.cigar).find())
							smflag=true;
						if(patternms.matcher(s.cigar).find())
							msflag=true;
						as.add(s);
					}
					else {
						if(smflag&&msflag) {
							for(int i=0;i<as.size();i++) {
								samUnit u=as.get(i);
								if(patternsm.matcher(u.cigar).find()||patternms.matcher(u.cigar).find()) {
									if(patternsm.matcher(u.cigar).find()) state="SM";
									else state="MS";
								    boolean flag=false;
									for(int x=0;x<ps.size();x++) {
										positionUnit psu=ps.get(x);
										if(psu.comparaposition(u.splitpos)) {
											psu.changestate(u.splitpos, state);
											flag=true;
											break;
										}
									}
									if(flag==false&&u.splitpos!=-1) {
										ps.add(new positionUnit(u.splitpos, state));
									}
								}
							}
						}
						smflag=false;
						msflag=false;
						as.clear();
						if(patternsm.matcher(s.cigar).find())
							smflag=true;
						if(patternms.matcher(s.cigar).find())
							msflag=true;
						as.add(s);	
					}
				}
			}
			
		}
		if(!as.isEmpty()) {
			if(smflag&&msflag) {							
				for(int i=0;i<as.size();i++) {
				samUnit u=as.get(i);
				if(patternsm.matcher(u.cigar).find()||patternms.matcher(u.cigar).find()) {
					if(patternsm.matcher(u.cigar).find()) state="SM";
					else state="MS";
				    boolean flag=false;
					for(int x=0;x<ps.size();x++) {
						positionUnit psu=ps.get(x);
						if(psu.comparaposition(u.splitpos)) {
							psu.changestate(u.splitpos, state);
							flag=true;
							break;
						}
					}
					if(flag==false&&u.splitpos!=-1) {
						ps.add(new positionUnit(u.splitpos, state));
					}
				}
				}
			}
		}
		Collections.sort(ps);
		for(positionUnit i:ps) {
			if(i.ms&i.sm) {
				bw.write(String.valueOf(i.position)+"\t"+"11");
				bw.newLine();
			}
//			if(i.ms==true&&i.sm==false)
//			{
//				bw.write(String.valueOf(i.position)+"\t"+"10");
//				bw.newLine();
//			}
//			if(i.ms==false&&i.sm==true)
//			{
//				bw.write(String.valueOf(i.position)+"\t"+"01");
//				bw.newLine();
//			}
			}
		bw.flush();
		bw.close();
		br.close();
		as=null;
		return ps;
//		for(int i=0;i<ps.size();i++) {
//			System.out.println(ps.get(i).position+" "+ps.get(i).ms+" "+ps.get(i).sm);
//		}
	}
	public static void main(String[] args) throws IOException {
		findPosition f=new findPosition();
		f.findposition("/media/xie/0009A639000F3A82/内部插入仿真/test1/findhtos.sam", "/media/xie/0009A639000F3A82/内部插入仿真/test1/pos.txt");
	}
}
