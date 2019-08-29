package ins;

public class Prepare {
	public static void main(String[] args) throws Exception {
		String fasta=args[0];
		String fq1=args[1];
		String fq2=args[2];
		String tempdir=args[3];
		fastaToLow ftl=new fastaToLow();
		ftl.tolow(fasta, tempdir+"/low.fa");
		ftl=null;
		fastqRename fr=new fastqRename();
		fr.fastqrename(fq1, fq2, tempdir+"/r1.fq",tempdir+"/r2.fq");
		fr=null;
	}
}
