package ins;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class extractUnmatchReads {
	void extractunmatchreads(String namelist,String fq,String extractfq,String shdir) {
		ProcessBuilder pb = new ProcessBuilder("./seqtk.sh",fq,namelist,extractfq);
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
        	System.out.println(runningStatus);
        }
	}
	public static void main(String[] args) {
		extractUnmatchReads e=new extractUnmatchReads();
		e.extractunmatchreads("/media/xie/0009A639000F3A82/ins/test3/unmatchname.txt", "/media/xie/0009A639000F3A82/ins/test3/2.fq", "/media/xie/0009A639000F3A82/ins/test3/22.fq", "/home/xie/eclipse-workspace/ins");
	}
}
