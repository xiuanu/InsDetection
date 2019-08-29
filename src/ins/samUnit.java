package ins;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class samUnit {
	String name;
	int flag;
	String chr;
	int pos;
	String mapq;
	String cigar;
	String mchr;
	int mpos;
	int isize;
	String seq;
	int splitpos;
	String state=null;
	samUnit(String samline){
		String[] temp = samline.split("\t");
		name=temp[0];
		flag=Integer.parseInt(temp[1]);
		chr=temp[2];
		pos=Integer.parseInt(temp[3]);
		mapq=temp[4];
		cigar=temp[5];
		mchr=temp[6];
		mpos=Integer.parseInt(temp[7]);
		isize=Integer.parseInt(temp[8]);
		seq=temp[9];
		state=cigarstate(cigar);
		splitpos=calsplitpos(pos, cigar, state);
	}
	public static String cigarstate(String cigar) {
		Pattern pattern1 = Pattern.compile("^[\\d]{1,}M[\\d]{1,}H$");
		Pattern pattern2 = Pattern.compile("^[\\d]{1,}H[\\d]{1,}M$");
		Pattern patternms = Pattern.compile("^[\\d]{1,}M[\\d]{1,}S$");
		Pattern patternsm = Pattern.compile("^[\\d]{1,}S[\\d]{1,}M$");
		Pattern patternmim = Pattern.compile("^[\\d]{1,}M[\\d]{1,}I[\\d]{1,}M$");
		Pattern patternmdm = Pattern.compile("^[\\d]{1,}M[\\d]{1,}D[\\d]{1,}M$");
		String state=null;
		if(pattern1.matcher(cigar).find()) {
			return state="MH";
		}
		if(pattern2.matcher(cigar).find()) {
			return state="HM";
		}
		if(patternms.matcher(cigar).find()) {
			return state="MS";
		}
		if(patternsm.matcher(cigar).find()) {
			return state="SM";
		}
		if(patternmim.matcher(cigar).find()) {
			return state="MIM";
		}
		if(patternmdm.matcher(cigar).find()) {
			return state="MDM";
		}
		return state;
	}
	
	public static int calsplitpos(int pos,String cigar,String state) {
		if(state==null) return -1;
		if(state.equals("SM")) return pos;
		if(state.equals("MS")) {
			Pattern patternms = Pattern.compile("^([\\d]{1,})M([\\d]{1,})S$");
			Matcher matcher=patternms.matcher(cigar);
			if(matcher.find()) {
				int pianyi=Integer.valueOf(matcher.group(1));
				return pos+pianyi;
			}
		}
		if(state.equals("MIM")) {
			Pattern patternmim = Pattern.compile("^([\\d]{1,})M([\\d]{1,})I([\\d]{1,})M$");
			Matcher matcher=patternmim.matcher(cigar);
			if(matcher.find()) {
				int pianyi=Integer.valueOf(matcher.group(1));
				return pos+pianyi;
			}
		}
		if(state.equals("MDM")) {
			Pattern patternmdm = Pattern.compile("^([\\d]{1,})M([\\d]{1,})D([\\d]{1,})M$");
			Matcher matcher=patternmdm.matcher(cigar);
			if(matcher.find()) {
				int pianyi=Integer.valueOf(matcher.group(1));
				return pos+pianyi;
			}
		}
		return -1;
	}
	
	public String toString() {
		return name+"\t"+String.valueOf(flag)+"\t"+chr+"\t"+String.valueOf(pos)
			+"\t"+mapq+"\t"+cigar+"\t"+mchr+"\t"+String.valueOf(mpos)+"\t"+String.valueOf(isize)+"\t"+seq;
	}
	

	
	
//	public static void main(String[] args) {
//		samUnit s=new samUnit("sim_chr21_18687_19070_60_24_0	2145	chr21	18487	60	47M53H	=	18738	351	GTTTTTTAAAGGCAATACATCAAACATAAAAAGAGCCCTTTTGTATG	D9GDFGGGGGGGGGEFGFGGGGFGDEGGGFG7FG:GFGGBEDGG>#6	NM:i:0	MD:Z:47	MC:Z:100M	AS:i:47	XS:i:0	SA:Z:chr21,6801,+,47S53M,60,0;");
//		System.out.println(s.toString());
//	}
}
