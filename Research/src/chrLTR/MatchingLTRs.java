package chrLTR;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MatchingLTRs {
	private File MatchingLTR;
	private Scanner MatchingLTRScanner;
	public ArrayList<String> MatchingLTRList, keySet;
	public HashMap<String, String> matchingLTRs = new HashMap<String, String>();
	MatchingLTRs(File MatchingLTR) throws FileNotFoundException{
		this.MatchingLTR = MatchingLTR;
		MatchingLTRScanner = new Scanner(MatchingLTR);
		MatchingLTRList = new ArrayList<String>();
		keySet = new ArrayList<String>();
		while(MatchingLTRScanner.hasNextLine()) {
			MatchingLTRList.add(MatchingLTRScanner.nextLine());
		}
		for(int i = 0; i<MatchingLTRList.size(); i+=5) {
			matchingLTRs.put(MatchingLTRList.get(i), MatchingLTRList.get(i+1));
			keySet.add(MatchingLTRList.get(i));
		}
	}
}
