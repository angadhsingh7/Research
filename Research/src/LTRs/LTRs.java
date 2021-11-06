package LTRs;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LTRs {
	public static void main(String[] args) throws IOException {
		Path path = Paths.get("C:\\Users\\angad\\OneDrive\\Desktop\\LTR Testing\\Converter");

		Stream<Path> subPath =  Files.walk(path, 1);

		List<String> subPathList = subPath.filter(Files::isRegularFile).map(p->p.toString()).collect(Collectors.toList());



		int numOfFiles = subPathList.size();

		for(int i = 0; i<numOfFiles; i++) {
			FileWriter writer = new FileWriter(subPathList.get(i) + "OUTPUT.fa");
			//Input
			File input = new File(subPathList.get(i));  																		//args[0] = inputFile must be declared when running
			Scanner scanInput = new Scanner(input);
			ArrayList<String> g = new ArrayList<String>();
			while(scanInput.hasNextLine()){
					g.add(scanInput.nextLine());
				}
			Map<String, String> x = new HashMap<String, String>();
			for(int j = 1; j<g.size(); j+=2) {
				if(x.containsKey(g.get(j))) {
					if(!g.get(j-1).equals(x.get(g.get(j)))){
						writer.write(g.get(j-1) + "\n");
						writer.write(x.get(g.get(j)) + "\n");
						writer.write(g.get(j) + "\n");
						writer.write("\n");
						writer.write("\n");	
					}
				}
				else {
					x.put(g.get(j), g.get(j-1));
				}
			}
			writer.close();
			}
	}
}
