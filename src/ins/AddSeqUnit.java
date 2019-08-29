package ins;

public class AddSeqUnit implements Comparable<AddSeqUnit> {
	int pos;
	int seqlength;
	String seq;
	
	public AddSeqUnit(int pos,String seq) {
		this.pos=pos;
		this.seq=seq;
		this.seqlength=seq.length();
	}
	@Override
	public int compareTo(AddSeqUnit o) {
		// TODO 自动生成的方法存根
		if(this.pos>o.pos)return 1;
		return -1;
	}
}
