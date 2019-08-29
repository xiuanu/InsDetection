package ins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class noMsPositionRealign {
		static String shdir="/home/xie/eclipse-workspace/ins";
		static String temppath="/media/xie/000E60FD000E74AA/ins/sra/temp";
		
		ArrayList<samUnit> nomspositionrealign(String fasta,String unmatchfq1,String unmatchfq2,int pos,int insertsize) throws IOException {
			File file=mkdir(pos);
			realign(fasta, pos, unmatchfq1, unmatchfq2);
			ArrayList<samUnit> asu=findposms(pos,insertsize);
			deleteFile(file);
			return asu;
		}
		
		ArrayList<samUnit> findposms(int pos,int insertsize) throws IOException {
			BufferedReader br = new BufferedReader(new FileReader(temppath+pos+"/temp.sam"));
			String line=br.readLine();
			br.close();
			if(line==null) return null;
			br=new BufferedReader(new FileReader(temppath+pos+"/temp.sam"));
			line=null;
			ArrayList<String> as=new ArrayList<String>();
			ArrayList<samUnit> asu=new ArrayList<samUnit>();
			while((line = br.readLine()) != null){
				if(line.startsWith("@")){
					continue;
				}else{
					String[] temp = line.split("\t");
					int flag=Integer.valueOf(temp[1]);
					if(((flag&4)==4)||((flag&8)==8))
						continue;
					else {
						as.add(line);
					}
				}
			}
			br.close();
			for(String u:as) {
				samUnit s=new samUnit(u);
				if(s.state!=null && s.state.equals("MS") && s.splitpos>=995 && s.splitpos<=1006 && s.isize<insertsize*2 && s.isize>-insertsize*2)
						asu.add(s);
					
			}
			for(samUnit s:asu) {
				int m=s.splitpos-s.pos;
				s.pos=pos-1000+s.pos;
				s.splitpos=s.pos+m;
				s.mpos=pos-1000+s.mpos;
			}
			return asu;
		}
		
		void realign(String fastafile,int pos,String unmatchfq1,String unmatchfq2) throws IOException{
			BufferedReader br1 = new BufferedReader(new FileReader(fastafile));
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(temppath+pos+"/temp.fa"));
			StringBuilder sb=new StringBuilder();
			String line1=null;
			while((line1=br1.readLine())!=null) {
				if(line1.startsWith(">")) {
					bw1.write(line1);
					bw1.newLine();
					continue;
				}
				sb.append(line1);
				if(sb.length()>pos+1000)
					break;
				
			}
			br1.close();
			bw1.write(sb.substring(pos-1000,pos+1000));
			bw1.newLine();
			bw1.close();
			ProcessBuilder pb = new ProcessBuilder("./bwa.sh",temppath+pos+"/temp.fa",unmatchfq1,unmatchfq2,temppath+pos+"/temp.sam");
			pb.directory(new File(shdir));
			int runningStatus = 0;
			String s = null;
	        try {
	            Process p = pb.start();
	            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
	            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	            while ((s = stdInput.readLine()) != null) {
//	                System.out.println(s);
	            }
	            while ((s = stdError.readLine()) != null) {
//	            	System.out.println(s);
	            }
	            try {
	                runningStatus = p.waitFor();
	            } catch (InterruptedException e) {
	            	System.out.println(e);
	            }

	        } catch (IOException e) {
	        	System.out.println(e);
	        }
	        if (runningStatus != 0) {
	        	System.out.println(this.getClass().getName()+" error status: "+runningStatus);
	        }
	        
		}
		
		File mkdir(int pos) {
			File file =new File(temppath+pos); 
			if (!file .exists() && !file .isDirectory()) { 
				file .mkdir(); 
			}
			return file;
		}
		
		void deleteFile(File file){
			if (file.exists()) {
				if (file.isFile()) {
				file.delete(); 
				} else if (file.isDirectory()) {
				File[] files = file.listFiles(); 
				for (int i = 0;i < files.length;i ++) {
				this.deleteFile(files[i]);
				} 
				file.delete();
				files=null;
				} 
				} else { 
					System.out.println("所删除的文件不存在");
				} 
			file=null;
		}
		
		public static void main(String[] args) throws IOException {
			noMsPositionRealign n=new noMsPositionRealign();
//			n.realign("/media/xie/0009A639000F3A82/ins/test1/test1.fa", 7935, "/media/xie/0009A639000F3A82/ins/test1/11.fq", "/media/xie/0009A639000F3A82/ins/test1/22.fq");
//			n.findposms(7935,500);
			ArrayList<samUnit> asu=n.nomspositionrealign("/media/xie/0009A639000F3A82/ins/test1/test1.fa", "/media/xie/0009A639000F3A82/ins/test1/11.fq", "/media/xie/0009A639000F3A82/ins/test1/22.fq", 7935, 500);
			for(samUnit s:asu) {
				System.out.println(s.toString());
				System.out.println(s.splitpos);
			}
		}
}
