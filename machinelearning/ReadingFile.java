package machinelearning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ReadingFile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//String address = "K:/Master's/4thSEM/DC/connectivity1.txt";
		String address = args[0];
		File file = new File(address);
		//File tempFile = new File("K:/Master's/4thSEM/DC/connectivity2.txt");
		File tempFile = new File(args[1]);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		String currentLine;
		while((currentLine = reader.readLine()) != null){
			String trimmedLine = currentLine.trim();
			if(trimmedLine.equals("") || trimmedLine.charAt(0) == '#'){
				continue;
			}else{
				writer.write(trimmedLine + System.getProperty("line.separator"));
			}
		}
		reader.close();
		writer.close();
	}

}
