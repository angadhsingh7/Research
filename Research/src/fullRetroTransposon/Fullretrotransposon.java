package fullRetroTransposon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.*;

public class Fullretrotransposon {
	static HashMap<String, ArrayList<ArrayList<String[]>>> combinedMap = new HashMap<String, ArrayList<ArrayList<String[]>>>();

	public static void loadCombined(String filename) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			// >chr1:50539099-50539347
			// >chr1:50506632-50506880
			// TGAGAATAGCAACCAGGATTGGTTAAGCGTTTCTACCTGCAGAGCAGGCCGCATTGATCTATTTATATAGGCAGAGAACTGTCGAGGATAACATCCTAGGGGGGTAACTCTGCCTAACAGAATCTGCTATATTTTAGGCTAATCGATAGTAGAGTTATCCCCTGAATATCTACGTAGTTATCTTATCAGATAAGCTACATTTACACAGCAGAGGCCTACGACCAGATTACAAGCTTTTCAATCTAACA

			String line1 = reader.readLine(); // These come in groups of 5 lines, line 4 and 5 are empty
			while (line1 != null) {
				String line2 = reader.readLine();
				String line3 = reader.readLine();
				String line4 = reader.readLine();
				String line5 = reader.readLine();

				String [] line1toks = line1.split(":");
				String [] line2toks = line2.split(":");
				String chrname = line1toks[0];
				String chrname2 = line2toks[0];
				if(!chrname.equals(chrname2)) {
					System.out.println("DIFF CHR" + chrname + ":" + chrname2);
					line1 = reader.readLine();
					continue;
				}
				String [] orignums = line1toks[1].split("-");
				String [] orignumssecondline = line2toks[1].split("-");

				ArrayList<ArrayList<String[]>> numpairs = combinedMap.get(chrname);
				if (numpairs == null) {
					numpairs = new ArrayList<ArrayList<String[]>>();
					combinedMap.put(chrname, numpairs);
				}
				ArrayList<String[]> firstpair = new ArrayList<String[]>();
				firstpair.add(orignums);
				firstpair.add(orignumssecondline);

				numpairs.add(firstpair);

				System.out.println("combinedMap " + chrname + " : " + combinedMap.get(chrname).size());

				line1 = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static HashMap<String, String[]> bedMap = new HashMap<String, String[]>();

	public static void loadBedFile(String filename) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			//chr1 50539099 50544986 50539099 50539347 50544737 50544986 95 --- --- --- --- 50539475 50539495 - 85 50539099 50544984

			String line = reader.readLine();
			while (line != null) {
				String [] line1toks = line.trim().split("\t");
				//System.out.println(line1toks[0]);
				line = reader.readLine();
				String [] pair = new String[3];
				pair[0] = ">" + line1toks[0];
				pair[1] = line1toks[1];
				pair[2] = line1toks[2];
				bedMap.put(line1toks[1], pair);
				bedMap.put(line1toks[2], pair);
				bedMap.put(line1toks[3], pair);
				bedMap.put(line1toks[4], pair);
				bedMap.put(line1toks[5], pair);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void processChr(FileWriter writer, String chrname, char [] chrseq) {
		System.out.println("processChr : " + chrname + " chrseq : " + chrseq.length);

		try {
			ArrayList<ArrayList<String[]>> numpairs = combinedMap.get(chrname);
			if (numpairs != null && numpairs.size() > 0) {
				for (int i = 0; i < numpairs.size(); i++) {
					System.out.println(numpairs.get(i).get(0)[0] + " : " +  numpairs.get(i).get(0)[1]);
					// For each number in the start-end pair
					// Lookup bedMap to map it to "CHR" "START" and "END";
					String [] chr_start_end = bedMap.get(numpairs.get(i).get(0)[0]);
					if (chr_start_end == null) {
						chr_start_end = bedMap.get(numpairs.get(i).get(0)[1]);
					}

					String [] chr_start_end_second_line = bedMap.get(numpairs.get(i).get(1)[0]);
					if (chr_start_end_second_line == null) {
						chr_start_end_second_line = bedMap.get(numpairs.get(i).get(1)[1]);
					}

					if (chr_start_end != null) {
						//writer.write("FIRST LINE CHR/START/END" + chr_start_end[0] + "/" + chr_start_end[1] + "/" + chr_start_end[2] + "\n");
						writer.write(chr_start_end[0] + ":" + chr_start_end[1] + "-" + chr_start_end[2] + "\n");
						int start = Integer.parseInt(chr_start_end[1]);
						int end = Integer.parseInt(chr_start_end[2]);
						writer.write(new String (chrseq, start-5, end-start+5) + "\n");
//						for (int n = start; n <= end; n=n+80) {
//							if (n+80 <= end) {
//								writer.write(new String (chrseq, n, 80) + "\n");
//							} else {
//								writer.write(new String (chrseq, n, end-n) + "\n");
//							}
//						}
					}

					if (chr_start_end_second_line != null) {
						//writer.write("FIRST LINE CHR/START/END" + chr_start_end[0] + "/" + chr_start_end[1] + "/" + chr_start_end[2] + "\n");
						writer.write(chr_start_end_second_line[0] + ":" + chr_start_end_second_line[1] + "-" + chr_start_end_second_line[2] + "\n");
						int start = Integer.parseInt(chr_start_end_second_line[1]);
						int end = Integer.parseInt(chr_start_end_second_line[2]);
						writer.write(new String (chrseq, start-5, end-start+5) + "\n");
//						for (int n = start; n <= end; n=n+80) {
//							if (n+80 <= end) {
//								writer.write(new String (chrseq, n, 80) + "\n");
//							} else {
//								writer.write(new String (chrseq, n, end-n) + "\n");
//							}
//						}
					}
					writer.write("\n\n");

					// if (chr_start_end_second_line != null) {
					// writer.write("SECOND LINE CHR/START/END" + chr_start_end_second_line[0] + "/" + chr_start_end_second_line[1] + "/" + chr_start_end_second_line[2] + "\n");
					// writer.write(chr_start_end_second_line[0] + ":" + chr_start_end_second_line[1] + "-" + chr_start_end_second_line[2] + "\n");
					// writer.write(new String (chrseq, Integer.parseInt(chr_start_end_second_line[1]), Integer.parseInt(chr_start_end_second_line[2])) + "\n");
					// }
				}
			} else {
				System.out.println("numpairs is empty for " + chrname);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadGenomeFile(String filename) {

		BufferedReader reader;
		try {
			File file = new File("C:\\Users\\angad\\OneDrive\\Desktop\\LTRTesting\\test.txt");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Fullretrotransposon.loadCombined(args[0]);
		Fullretrotransposon.loadBedFile(args[1]);
		Fullretrotransposon.loadGenomeFile(args[2]);
	}
}
