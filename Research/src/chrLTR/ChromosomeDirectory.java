package chrLTR;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ChromosomeDirectory {
	private File ChromosomeDirectories;
	public ArrayList<String> ChrDirList = new ArrayList<String>();
	private Scanner ChrDirScanner;
	public HashMap<String, String> LTRtoRetro = new HashMap<String, String>();
	ChromosomeDirectory(File ChromosomeDirectories) throws FileNotFoundException{
		this.ChromosomeDirectories = ChromosomeDirectories;
		ChrDirScanner = new Scanner(ChromosomeDirectories);
		while(ChrDirScanner.hasNextLine()) {
			String x = ChrDirScanner.nextLine();
			if(x.length() > 0) ChrDirList.add(x);
		}
		for(int i = 2; i<ChrDirList.size(); i++) {
			String[] token = ChrDirList.get(i).split("	");
				LTRtoRetro.put(">"+token[0]+":"+token[3]+"-"+token[4], ">"+token[0]+":"+token[1]+"-"+token[2]);
		}
	}
}
