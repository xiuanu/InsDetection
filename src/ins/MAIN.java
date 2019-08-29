package ins;

import output.MutationOutput;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class MAIN {
	public static void main(String[] args) throws Exception {
		int i=1;
		MAIN ma=new MAIN();
		int seqlength=100;
		int insertsize=500;
		int insertsize_sd=50;
//		String fasta="/media/xie/0009A639000F3A82/ins/genome.fa";
//		String fq1="/media/xie/0009A639000F3A82/ins/102/5_7/x1.fq";
//		String fq2="/media/xie/0009A639000F3A82/ins/102/5_7/x2.fq";
//		String tempdir="/media/xie/0009A639000F3A82/ins/102/5_7";
		String fasta=args[0];
		String fq1=args[1];
		String fq2=args[2];
		String tempdir=args[3];
		String properties=args[4];
		Properties pps = new Properties();
		boolean deletefile=true;
		try {
				InputStream in = new BufferedInputStream (new FileInputStream(properties));  
				pps.load(in);
		    }catch (IOException e) {
		    	e.printStackTrace();
		        }
		noMsPositionRealign.temppath=pps.getProperty("tempdir");
		noMsPositionRealign.shdir=pps.getProperty("shdir");
		fastaToLow ftl=new fastaToLow();
		ftl.tolow(fasta, tempdir+"/low.fa");
		ftl=null;
		fastqRename fr=new fastqRename();
		fr.fastqrename(fq1, fq2, tempdir+"/r1.fq",tempdir+"/r2.fq");
		fr=null;
		while(i<30) {
			if(i==1) {
				ma.bwash(tempdir+"/low.fa",tempdir+"/r1.fq",tempdir+"/r2.fq",tempdir+"/"+i+"times.sam",pps.getProperty("shdir"));
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
				boolean flag=fa.finaladd(tempdir+"/low.fa", tempdir+"/"+i+"_pos_add.txt", tempdir+"/"+next+".fa");
				if(deletefile) {
				DeleteFile.delete(tempdir+"/"+i+"times.sam");
				DeleteFile.delete(tempdir+"/"+i+"times.findh.sam");
				DeleteFile.delete(tempdir+"/"+i+"times.findhtos.sam");
				DeleteFile.delete(tempdir+"/"+i+"times.htos.sam");
				DeleteFile.delete(tempdir+"/"+i+"_r1.fq");
				DeleteFile.delete(tempdir+"/"+i+"_r2.fq");
				}
				if(flag==false) break;

			}
			else {
				ma.bwash_Y(tempdir+"/"+i+".fa",tempdir+"/r1.fq",tempdir+"/r2.fq",tempdir+"/"+i+"times.sam",pps.getProperty("shdir"));
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
				boolean flag=fa.finaladd(tempdir+"/"+i+".fa", tempdir+"/"+i+"_pos_add.txt", tempdir+"/"+next+".fa");
				if(deletefile) {
				DeleteFile.delete(tempdir+"/"+i+"times.sam");
				DeleteFile.delete(tempdir+"/"+i+"times.htos.sam");
				DeleteFile.delete(tempdir+"/"+i+"_r1.fq");
				DeleteFile.delete(tempdir+"/"+i+"_r2.fq");
				}
				if(flag==false) break;
			}
			System.out.println("第"+i+"次迭代完成");
			i++;
		}
		MutationOutput mo=new MutationOutput();
		mo.output(tempdir+"/"+i+".fa", tempdir+"/ins.txt");
	}
	
	public   void bwash(String fasta,String fq1,String fq2,String out,String shdir) {
		ProcessBuilder pb = new ProcessBuilder("./bwa.sh",fasta,fq1,fq2,out);
		pb.directory(new File(shdir));
		int runningStatus = 0;
		String s = null;
        try {
            Process p = pb.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdInput.readLine()) != null) {
//                System.out.println(s);
            }
            while ((s = stdError.readLine()) != null) {
            	System.out.println(s);
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
        	System.out.println(" error status: "+runningStatus);
        }
        s=null;
	}
	
	public  void bwash_Y(String fasta,String fq1,String fq2,String out,String shdir) {
		ProcessBuilder pb = new ProcessBuilder("./bwa_Y.sh",fasta,fq1,fq2,out);
		pb.directory(new File(shdir));
		int runningStatus = 0;
		String s = null;
        try {
            Process p = pb.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdInput.readLine()) != null) {
//                System.out.println(s);
            }
            while ((s = stdError.readLine()) != null) {
            	System.out.println(s);
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
        	System.out.println(" error status: "+runningStatus);
        }
        s=null;
	}
}
