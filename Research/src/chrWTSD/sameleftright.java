package chrWTSD;
/*sameleftright.java*/
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.*;

public class sameleftright {
	static class BedLine {
		String SeqID;
		int RT_start, RT_end, L_LTR_start, L_LTR_end, R_LTR_start, R_LTR_end;
		public String toString() {
			return
					" SeqID: " + SeqID + " RT_start : " + RT_start + " RT_end : " + RT_end +
					" L_LTR_start : " + L_LTR_start + " L_LTR_end : " + L_LTR_end +
					" R_LTR_start : " + R_LTR_start + " R_LTR_end : " + R_LTR_end;
		}
	}

	static HashMap<String, ArrayList<BedLine>> bedMap = new HashMap<String, ArrayList<BedLine>>();

	public static void loadBedFile(String filename) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			//chr1    50539099    50544986    50539099    50539347    50544737    50544986    95    ---    ---    ---    ---    50539475    50539495    -    85    50539099    50544984

			String line = reader.readLine();
			System.out.println(line);
			// Skip lines like :
			// SeqID    Retrotransposon    Left_LTR    Right_LTR   	 Left_TSD    Right_TSD    Polypurine Tract   	 TG    CA
			// and
			//     Start    End    Start    End    Start    End    ID    Start    End    Start    End    Start    End    Strand    Purine%    Start    End
			while (line != null) {
				String [] line1toks = line.trim().split("\t");
				//System.out.println(line);
				if ((-1 == line.indexOf("SeqID")) && (-1 == line.indexOf("Start"))) {
					String chr = ">" + line1toks[0];
					BedLine bedline = new BedLine();
					bedline.SeqID = chr;
					bedline.RT_start = Integer.parseInt(line1toks[1]);
					bedline.RT_end = Integer.parseInt(line1toks[2]);
					bedline.L_LTR_start = Integer.parseInt(line1toks[3]);
					bedline.L_LTR_end = Integer.parseInt(line1toks[4]);
					bedline.R_LTR_start = Integer.parseInt(line1toks[5]);
					bedline.R_LTR_end = Integer.parseInt(line1toks[6]);
					ArrayList<BedLine> v = bedMap.get(chr);
					if (v == null) {
						bedMap.put(chr, new ArrayList<BedLine>());
					}
					bedMap.get(chr).add(bedline);
					//System.out.println(bedline);
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void processChr(FileWriter writer, String chrname, char [] chrseq) {
		System.out.println("processChr : " + chrname + " chrseq : " + chrseq.length);
		try {
			ArrayList<BedLine> bedlines = bedMap.get(chrname);
			if (bedlines != null && bedlines.size() > 0) {
				System.out.println("bedlines.size():" + bedlines.size());
				for (int i = 0; i < bedlines.size(); i++) {
					BedLine bedline = bedlines.get(i);
					String leftLTRSeq = new String (chrseq, bedline.L_LTR_start, bedline.L_LTR_end - bedline.L_LTR_start);
					String rightLTRSeq = new String (chrseq, bedline.R_LTR_start, bedline.R_LTR_end - bedline.R_LTR_start);
					// System.out.print("leftLTRSeq:" + leftLTRSeq);
					// System.out.print("rightLTRSeq:" + rightLTRSeq);
					if (leftLTRSeq.equals(rightLTRSeq)) {
						// System.out.println("leftLTRSeq:" + leftLTRSeq);
						// System.out.println("rightLTRSeq:" + rightLTRSeq);
						String RTplus5 = new String(chrseq, bedline.RT_start - 5, (bedline.RT_end - bedline.RT_start + 10));
						//						System.out.println("RTplus5:" + RTplus5);
//						if(RTplus5.length()<=5000) {
							writer.write(chrname + ":" + bedline.RT_start + "-" + bedline.RT_end + "\n");
							writer.write(RTplus5 + "\n");
							writer.write("\n");
//						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadGenomeFile(String filename) {

		BufferedReader reader;
		try {
			File file = new File(filename.substring(0, filename.length()-3) + "MATCHING.fa");
			file.createNewFile();
			FileWriter writer = new FileWriter(file);

			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			String chrName = null;
			StringBuffer genome = new StringBuffer();
			while (line != null) {
				if (line.charAt(0) == '>') {
					if (chrName != null) {
						System.out.println("New : " + chrName + " : " + genome.length());
						processChr(writer, chrName, genome.toString().toCharArray());
						genome = new StringBuffer();
						chrName = line;
					} else {
						chrName = line;
					}
				} else {
					genome.append(line);
				}
				line = reader.readLine();
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) { //C:\Users\angad\OneDrive\Desktop\LTRTesting\fullChromosomes\B73_full_length_LTR.fa 
		//C:\Users\angad\OneDrive\Desktop\Zm-B73-REFERENCE-NAM-5.0Detector.bed
		sameleftright.loadBedFile(args[0]);
		sameleftright.loadGenomeFile(args[1]);
	}
}
