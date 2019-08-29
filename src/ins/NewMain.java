package ins;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import output.MutationOutput;

public class NewMain {
		public static void main(String[] args) throws Exception {

			int i=Integer.valueOf(args[2]);
			int seqlength=100;
			int insertsize=500;
			int insertsize_sd=50;
			String tempdir=args[0];
			String properties=args[1];
			Properties pps = new Properties();
			boolean flag=true;
			boolean deletefile=true;
			try {
					InputStream in = new BufferedInputStream (new FileInputStream(properties));  
					pps.load(in);
			    }catch (IOException e) {
			    	e.printStackTrace();
			        }
			noMsPositionRealign.temppath=pps.getProperty("tempdir");
			noMsPositionRealign.shdir=pps.getProperty("shdir");
			if(i==1) {
					findMatchH fmh=new findMatchH();
					fmh.findmatchH(tempdir+"/"+i+"times.sam", tempdir+"/"+i+"times.findh.sam");
					fmh=null;
					htos h=new htos();
					h.htos_samfile(tempdir+"/"+i+"times.findh.sam", tempdir+"/"+i+"times.findhtos.sam", seqlength);
					h.htos_samfile(tempdir+"/"+i+"times.sam", tempdir+"/"+i+"times.htos.sam", seqlength);
					h=null;
					findPosition fp=new findPosition();
					fp.findposition(tempdir+"/"+i+"times.findhtos.sam", tempdir+"/"+i+"_pos.txt");
					fp=null;
					findUnmatchName fun=new findUnmatchName();
					fun.findunmatchname(tempdir+"/"+i+"times.sam", tempdir+"/"+i+"_unmatchname.list", insertsize);
					fun=null;
					extractUnmatchReads eur=new extractUnmatchReads();
					eur.extractunmatchreads(tempdir+"/"+i+"_unmatchname.list", tempdir+"/r1.fq", tempdir+"/"+i+"_r1.fq", pps.getProperty("shdir"));
					eur.extractunmatchreads(tempdir+"/"+i+"_unmatchname.list", tempdir+"/r2.fq", tempdir+"/"+i+"_r2.fq", pps.getProperty("shdir"));
					eur=null;
					find_new_add_sequence_first fnds=new find_new_add_sequence_first();
					fnds.findnewaddsequence(tempdir+"/"+i+"_pos.txt", tempdir+"/"+i+"times.htos.sam", tempdir+"/"+i+"_pos_add.txt", insertsize, insertsize_sd, tempdir+"/low.fa", tempdir+"/"+i+"_r1.fq", tempdir+"/"+i+"_r2.fq");
					fnds=null;
					int next=i+1;
					confirmPosition cp=new confirmPosition();
					cp.confirmposition(tempdir+"/"+i+"_pos_add.txt", tempdir+"/"+next+"_pos.txt");
					cp=null;
					finalAdd fa=new finalAdd();
					flag=fa.finaladd(tempdir+"/low.fa", tempdir+"/"+i+"_pos_add.txt", tempdir+"/"+next+".fa");
					if(deletefile) {
					DeleteFile.delete(tempdir+"/"+i+"times.sam");
					DeleteFile.delete(tempdir+"/"+i+"times.findh.sam");
					DeleteFile.delete(tempdir+"/"+i+"times.findhtos.sam");
					DeleteFile.delete(tempdir+"/"+i+"times.htos.sam");
					DeleteFile.delete(tempdir+"/"+i+"_r1.fq");
					DeleteFile.delete(tempdir+"/"+i+"_r2.fq");
					}

				}
				else {
					htos h=new htos();
					h.htos_samfile(tempdir+"/"+i+"times.sam", tempdir+"/"+i+"times.htos.sam", seqlength);
					h=null;
					findUnmatchName fun=new findUnmatchName();
					fun.findunmatchname(tempdir+"/"+i+"times.sam", tempdir+"/"+i+"_unmatchname.list", insertsize);
					fun=null;
					extractUnmatchReads eur=new extractUnmatchReads();
					eur.extractunmatchreads(tempdir+"/"+i+"_unmatchname.list", tempdir+"/r1.fq", tempdir+"/"+i+"_r1.fq", pps.getProperty("shdir"));
					eur.extractunmatchreads(tempdir+"/"+i+"_unmatchname.list", tempdir+"/r2.fq", tempdir+"/"+i+"_r2.fq", pps.getProperty("shdir"));
					eur=null;
					find_new_add_sequence fnds=new find_new_add_sequence();
					fnds.findnewaddsequence(tempdir+"/"+i+"_pos.txt", tempdir+"/"+i+"times.htos.sam", tempdir+"/"+i+"_pos_add.txt", insertsize, insertsize_sd,tempdir+"/"+i+".fa", tempdir+"/"+i+"_r1.fq", tempdir+"/"+i+"_r2.fq");
					fnds=null;
					int next=i+1;
					confirmPosition cp=new confirmPosition();
					cp.confirmposition(tempdir+"/"+i+"_pos_add.txt", tempdir+"/"+next+"_pos.txt");
					cp=null;
					finalAdd fa=new finalAdd();
					flag=fa.finaladd(tempdir+"/"+i+".fa", tempdir+"/"+i+"_pos_add.txt", tempdir+"/"+next+".fa");
					if(deletefile) {
					DeleteFile.delete(tempdir+"/"+i+"times.sam");
					DeleteFile.delete(tempdir+"/"+i+"times.htos.sam");
					DeleteFile.delete(tempdir+"/"+i+"_r1.fq");
					DeleteFile.delete(tempdir+"/"+i+"_r2.fq");
					}
				}
			System.out.println("第"+i+"次迭代完成");
			i++;
			if(flag==false) {
			MutationOutput mo=new MutationOutput();
			mo.output(tempdir+"/"+i+".fa", tempdir+"/ins.txt");
			System.exit(1);
			}
		
		}

}
