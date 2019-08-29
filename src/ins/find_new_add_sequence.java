package ins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class find_new_add_sequence {
		void findnewaddsequence(String positionfile,String matchfile,String pos_seq_file,int insertsize_avg,int insertsize_sd,String fasta,String unmatchfq1,String unmatchfq2) throws IOException {
			BufferedReader br_pos = new BufferedReader(new FileReader(positionfile));
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(pos_seq_file));
			ArrayList<ArrayList<positionUnit>> ps=new ArrayList<ArrayList<positionUnit>>();
			String line_pos=null;
			int count=0;
			while((line_pos=br_pos.readLine())!=null) {
				String temp[]=line_pos.split("\t");
				positionUnit p=new positionUnit(Integer.valueOf(temp[0]),temp[1]);
				if(count==0) {
					ArrayList<positionUnit> p1=new ArrayList<positionUnit>();
					p1.add(p);
					ps.add(p1);
					count++;
				}
				else {
					ps.get(ps.size()-1).add(p);
					count++;
					if(count==20)
						count=0;
				}
			}
			br_pos.close();
			int core_number=Runtime.getRuntime().availableProcessors();
			ExecutorService pool = Executors.newFixedThreadPool(core_number*4);
			ArrayList<Future<String>> arrf=new ArrayList<Future<String>>();
			for(ArrayList<positionUnit> ap:ps) {
				for(positionUnit p:ap) {
					Callable<String> c = new multithreading_find_new_add_sequence(p, matchfile, insertsize_avg, fasta, unmatchfq1, unmatchfq2);
					arrf.add(pool.submit(c));
				}
				for(int i=0;i<ap.size();i++) {
					while(true) {
						if(arrf.get(i).isDone()&&!arrf.get(i).isCancelled())
						{
							String s;
							try {
								s = arrf.get(i).get();
								if(s!=null)
								{
									bw.write(s);
									bw.newLine();
								}
							} catch (InterruptedException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
							break;
						} else
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						}
				}
				arrf.clear();
			}
			bw.flush();
			bw.close();
			pool.shutdown();
		}
		
		
		
		public static void main(String[] args) throws IOException {
			find_new_add_sequence f=new find_new_add_sequence();
			f.findnewaddsequence("/media/xie/0009A639000F3A82/ins/102/40_1/14_pos.txt", "/media/xie/0009A639000F3A82/ins/102/40_1/14times.htos.sam", "/media/xie/0009A639000F3A82/ins/102/40_1/14_pos_add.txt",500,50,
					"//media/xie/0009A639000F3A82/ins/102/40_1/14.fa", "/media/xie/0009A639000F3A82/ins/102/40_1/14_r1.fq", "/media/xie/0009A639000F3A82/ins/102/40_1/14_r2.fq");
		}

}


