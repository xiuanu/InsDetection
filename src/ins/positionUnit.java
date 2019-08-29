package ins;

import java.util.ArrayList;

public class positionUnit implements Comparable<positionUnit> {
	int position;
	boolean sm;
	boolean ms;
	public int ms_max=0;			//变异位置内的所有序列中未必对上的序列的最大长度；
	public int ms_min=0;			//变异位置内的所有序列中未必对上的序列的最小长度；
	public int sm_max=0;
	public int sm_min=0;
	public ArrayList<samUnit> arr_ms=null;	//变异位置内对应的所有sam单元
	public ArrayList<samUnit> arr_sm=null;
	public ArrayList<samUnit> arr_mim=null;
	public ArrayList<samUnit> arr_mdm=null;
	public positionUnit(int position,String state) {
		this.position=position;
		if(state.equals("MS")||state.equals("10")) 
			{
				this.ms=true;
				this.sm=false;
			}
		else{
			if(state.equals("SM")||state.equals("01")) {
			this.sm=true;
			this.ms=false;
			}
			else if(state.equals("11")) {
				this.sm=true;
				this.ms=true;
				arr_ms=new ArrayList<samUnit>();
				arr_sm=new ArrayList<samUnit>();
			}
		}
	}
	public positionUnit(int position) {
		this.position=position;
	}
	boolean comparaposition(int otherposition) {
		if((this.position-6)<=otherposition && (this.position+6)>=otherposition)
				return true;
		else return false;
	}
	
	void changestate(int position,String state) {
		if(this.ms==true) {
			if(state.equals("SM")&& this.sm==false) this.sm=true;
		}
		else {
			if(state.equals("MS")) {
				this.ms=true;
				this.position=position;
			}
			else {
				if(state.equals("SM")&& this.sm==false) this.sm=true;
			}
			
		}
	}
	@Override
	public int compareTo(positionUnit o) {
		if(this.position>o.position) return 1;
		else return -1;
	}
}
