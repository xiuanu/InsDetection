package ins;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class multithreading_find_new_add_sequence implements Callable<String>{
	String matchfile;
	int insertsize_avg;
	String fasta;
	String unmatchfq1;
	String unmatchfq2;
	positionUnit p;
	public multithreading_find_new_add_sequence(positionUnit pu,String matchfile,int insertsize_avg,String fasta,String unmatchfq1,String unmatchfq2) {
		this.matchfile=matchfile;
		this.insertsize_avg=insertsize_avg;
		this.fasta=fasta;
		this.unmatchfq1=unmatchfq1;
		this.unmatchfq2=unmatchfq2;
		this.p=pu;
	}
	
public String call() throws Exception {
	if(p.ms&&p.sm) {
//		System.out.println(p.position);
		String line=null;
		BufferedReader br = new BufferedReader(new FileReader(matchfile));
		while((line=br.readLine())!=null) {
			samUnit su=new samUnit(line);
			if(p.comparaposition(su.splitpos)) {
				if(su.state.equals("MS")) {
					if(p.arr_ms==null) {
						p.arr_ms=new ArrayList<samUnit>();
						p.arr_ms.add(su);
					}
					else p.arr_ms.add(su);
				}	
				else {
					if(su.state.equals("SM")) {
						if(p.arr_sm==null) {
							p.arr_sm=new ArrayList<samUnit>();
							p.arr_sm.add(su);
						}
						else p.arr_sm.add(su);
					}
					else {
						if(su.state.equals("MIM")) {
							if(p.arr_mim==null) {
								p.arr_mim=new ArrayList<samUnit>();
								p.arr_mim.add(su);
							}
							else p.arr_mim.add(su);
						}
						else {
							if(su.state.equals("MDM")) {
								if(p.arr_mdm==null) {
									p.arr_mdm=new ArrayList<samUnit>();
									p.arr_mdm.add(su);
								}
								else p.arr_mdm.add(su);
							}
						}
					}
				}
			}
		}
		br.close();
	}
	
	if(p.ms&&p.sm) {
		if(p.arr_mdm!=null&&p.arr_mdm.size()!=0) {
			for(Iterator<samUnit> i=p.arr_mdm.iterator();i.hasNext();) {
				samUnit s=i.next();
				if((s.isize>0?s.isize:-s.isize)>(insertsize_avg*2)||(Integer.valueOf(s.mapq)<20))
					i.remove();
			}
		}
		
		if(p.arr_mdm!=null&&p.arr_mdm.size()>3) {
			return null;
		}
		if(p.arr_ms!=null&&p.arr_ms.size()!=0) {
			for(Iterator<samUnit> i=p.arr_sm.iterator();i.hasNext();) {
				samUnit s=i.next();
				if((s.isize>0?s.isize:-s.isize)>(insertsize_avg*2)||(Integer.valueOf(s.mapq)<20))
					i.remove();
			}
		}
		if(p.arr_sm!=null&&p.arr_sm.size()!=0) {
			for(Iterator<samUnit> i=p.arr_ms.iterator();i.hasNext();) {
				samUnit s=i.next();
				if((s.isize>0?s.isize:-s.isize)>(insertsize_avg*2)||(Integer.valueOf(s.mapq)<20))
					i.remove();
			}
		}
		if(p.arr_mim!=null&&p.arr_mim.size()!=0) {
			for(Iterator<samUnit> i=p.arr_mim.iterator();i.hasNext();) {
				samUnit s=i.next();
				if((s.isize>0?s.isize:-s.isize)>(insertsize_avg*2))
					i.remove();
			}
		}
		if((p.arr_ms==null||p.arr_ms.size()==0)&&p.arr_mim!=null&&p.arr_mim.size()!=0) {
			Collections.sort(p.arr_mim,new comparemim());
//			for(samUnit s:p.arr_mim) {
//				System.out.println(s.toString());
//			}
			String fs=findsequenceI(p.arr_mim.get(0));
			if(fs!=null) {
				return fs;
			}
		}
		if(p.arr_ms!=null&&p.arr_ms.size()>0&&p.arr_ms.size()<3&&p.arr_sm!=null&&p.arr_sm.size()>0) {
				Collections.sort(p.arr_ms,new comparems());
				samUnit s=p.arr_ms.get(0);
				int offset=s.splitpos-s.pos;
				int slength=s.seq.length()-offset;
				if(slength<15) {
					noMsPositionRealign m=new noMsPositionRealign();
					p.arr_ms.addAll(m.nomspositionrealign(fasta, unmatchfq1, unmatchfq2, p.position,insertsize_avg));
				}
				Collections.sort(p.arr_ms,new comparems());
			}
			if((p.arr_ms==null||p.arr_ms.size()==0)&&p.arr_sm!=null&&p.arr_sm.size()>0)
			{
				noMsPositionRealign m=new noMsPositionRealign();
				p.arr_ms=m.nomspositionrealign(fasta, unmatchfq1, unmatchfq2, p.position,insertsize_avg);
			}
			if(p.arr_ms!=null&&p.arr_ms.size()!=0) {
				Collections.sort(p.arr_ms,new comparems());
			}
			if(p.arr_sm!=null&&p.arr_sm.size()!=0) {
				Collections.sort(p.arr_sm,new comparesm());
			}
	}		
			if(p.arr_ms!=null&&p.arr_ms.size()!=0&&p.arr_sm!=null&&p.arr_sm.size()!=0) {
				String fs=findsequence(p.arr_ms.get(0), p.arr_sm.get(0));
//				System.out.println(p.position);
//				for(samUnit s:p.arr_ms) {
//					System.out.println(s.toString());
//				}
//				System.out.println("");
//				for(samUnit s:p.arr_sm) {
//					System.out.println(s.toString());
//				}
//				System.out.println("");
				if(fs!=null) {
					return fs;
				}
			}
	
        return null;
    }

		final public String findsequenceI(samUnit mim) {
			Pattern patternmim = Pattern.compile("^([\\d]{1,})M([\\d]{1,})I([\\d]{1,})M$");
			Matcher matchermim=patternmim.matcher(mim.cigar);
			int m1=0;
			int i=0;
			if(matchermim.find()) {
				m1=Integer.valueOf(matchermim.group(1));
				i=Integer.valueOf(matchermim.group(2));
			}
			else return null;
			int pos=mim.splitpos-1;
			String s=mim.seq.substring(m1,m1+i);
			return String.valueOf(pos)+"\t"+s;
		}
		
		final public String findsequence(samUnit ms,samUnit sm) {
			Pattern patternsm = Pattern.compile("^([\\d]{1,})S([\\d]{1,})M$");
			int pos=ms.splitpos-1;
			String s=ms.seq.substring(ms.splitpos-ms.pos);
			Matcher matchersm=patternsm.matcher(sm.cigar);
			int pianyi=0;
			if(matchersm.find()) {
				pianyi=Integer.valueOf(matchersm.group(1));
			}
			String sub=sm.seq.substring(pianyi).substring(0,6);
			if(ms.splitpos==sm.splitpos) {
				if(s.indexOf(sub) != -1 && s.indexOf(sub) != 0){
					s=s.substring(0,s.indexOf(sub));
				}
			}
			else if(ms.splitpos>sm.splitpos) {
				int cha=ms.splitpos-sm.splitpos;
				pos=sm.splitpos-1;
				s=ms.seq.substring(ms.splitpos-ms.pos-cha);
				if(s.indexOf(sub) != -1 && s.indexOf(sub) != 0){
					s=s.substring(0,s.indexOf(sub));
				}
			}
			else return null;
			return String.valueOf(pos)+"\t"+s;
		}

}

final  class comparems implements Comparator<samUnit>{

	@Override
	public int compare(samUnit o1, samUnit o2) {
		int o1m=o1.splitpos-o1.pos;
		int o2m=o2.splitpos-o2.pos;
		if(o1m>o2m)return 1;
		else if(o1m<o2m) return -1;
		else return 0;
	}	
}
final   class comparesm implements Comparator<samUnit>{

	@Override
	public int compare(samUnit o1, samUnit o2) {
		Pattern patternms = Pattern.compile("^([\\d]{1,})S([\\d]{1,})M$");
		Matcher matcher1=patternms.matcher(o1.cigar);
		int pianyi1=0;
		int pianyi2=0;
		if(matcher1.find()) {
			pianyi1=Integer.valueOf(matcher1.group(1));
		}
		Matcher matcher2=patternms.matcher(o2.cigar);
		if(matcher2.find()) {
			pianyi2=Integer.valueOf(matcher2.group(1));
		}
		if(pianyi1>pianyi2)return 1;
		else if(pianyi1<pianyi2) return -1;
		else return 0;
	}
}

final  class comparemim implements Comparator<samUnit>{

	@Override
	public int compare(samUnit o1, samUnit o2) {
		int q1=Integer.valueOf(o1.mapq);
		int q2=Integer.valueOf(o2.mapq);
		if(q1<q2)return 1;
		else if(q1>q2) return -1;
		else {
			int o1m=o1.splitpos-o1.pos;
			int o2m=o2.splitpos-o2.pos;
			if(o1m<o2m)return 1;
			else if(o1m>o2m) return -1;
			else return 0;
		}
	}	
}
