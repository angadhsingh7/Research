package chrLTR;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class chrLTR {
	public static void main(String[] args) throws IOException, InterruptedException {
		File fullChrs = new File(args[2]);
		FullChromosomes fullChroms = new FullChromosomes(fullChrs);
		
		File chrDir = new File(args[0]);
		ChromosomeDirectory chrDirectory = new ChromosomeDirectory(chrDir);
		
		File matchingLTRs = new File(args[1]);
		MatchingLTRs matchLTR = new MatchingLTRs(matchingLTRs);
		
		FileWriter writer = new FileWriter(args[0] + "fullChromosomes.fa");
		
		for(int i = 0; i<matchLTR.keySet.size(); i++) {
			String y = chrDirectory.LTRtoRetro.get(matchLTR.keySet.get(i));
			String y1 = chrDirectory.LTRtoRetro.get(matchLTR.matchingLTRs.get(matchLTR.keySet.get(i)));
			String z = fullChroms.chrNameToChr.get(chrDirectory.LTRtoRetro.get(matchLTR.keySet.get(i)));
			String z1 = fullChroms.chrNameToChr.get(chrDirectory.LTRtoRetro.get(matchLTR.matchingLTRs.get(matchLTR.keySet.get(i))));
			if(y != null && y1 != null) {
				writer.write(y + '\n' + z + '\n');
				writer.write(y1 + '\n' + z1 + '\n' + '\n');
			}
		}
		writer.close();
	}
}
