package chrLTR;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FullChromosomes {
	private File fullChrs;
	public ArrayList<String> fullChrsList = new ArrayList<String>();
	private Scanner fullChrsScanner;
	public HashMap<String, String> chrNameToChr = new HashMap<String, String>();
	FullChromosomes(File fullChrs) throws FileNotFoundException{
		this.fullChrs = fullChrs;
		fullChrsScanner = new Scanner(fullChrs);
		while(fullChrsScanner.hasNextLine()) {
			fullChrsList.add(fullChrsScanner.nextLine());
		}		
		for(int i = 1; i<fullChrsList.size(); i+=2) {
			chrNameToChr.put(fullChrsList.get(i-1), fullChrsList.get(i));
		}	
	}
}
