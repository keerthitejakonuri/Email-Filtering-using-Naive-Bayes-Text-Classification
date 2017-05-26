package machinelearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

class Label{
	public int class_num;
	public int no_of_docs;
	public int total_word_count;
	public int total_unique_word_count;
	public HashMap<String,Integer> map;
	public Label(int class_num){
		this.class_num = class_num;
		map = new HashMap<String,Integer>();
	}
}

public class NaiveBayesForTextClassification {
	
	public static String parseToken(String token){
		token = token.trim();
		return token;
	}
	
	public static HashMap<String,Integer> parseDirectory(File sub, Label label, StopWords sw) throws IOException{
		FileReader reader;
		HashMap<String,Integer> map =  new HashMap<>();
		for(File file : sub.listFiles()){
			reader = new FileReader(file);
			BufferedReader br=new BufferedReader(reader);
			String line, token;
			StringTokenizer st;
			while((line=br.readLine())!=null){
				line = line.replaceAll("\\<.*?>", ""); //remove sgml tags
				line = line.replaceAll("\\/", " ");    //remove /
				line = line.replaceAll("\\d+.*", "");  // remove digits
				line = line.replaceAll("\\'s", " ");   //remove 's
				line = line.replaceAll("\\'", ""); 
				line = line.replaceAll("\\(", ""); 
				line = line.replaceAll("\\)", ""); 
				line = line.replaceAll("\\,", " ");    //remove ,
				line = line.replaceAll("\\.", "");     //remove .
				line = line.replaceAll("\\-", " ");    //remove -
				line = line.toLowerCase();			   //convert to lower case
				st = new StringTokenizer(line," \n\t,:;?{}()[]+ ");
				while(st.hasMoreTokens()){
					token = st.nextToken();
					token = parseToken(token);
					if(sw.stop(token)){
						
					}else{
						if(map.containsKey(token)){
							map.put(token, map.get(token)+1);
							label.total_word_count++;
						}else{
							map.put(token, 1);
							label.total_word_count++;
							label.total_unique_word_count++;
						}
					}
				}
			}
			br.close();
		}
		return map;
	}
	
	public static Label getClassDetails(File sub,int class_num, StopWords sw)throws IOException{
		Label label = new Label(class_num);
		label.no_of_docs = getNoOfFiles(sub);
		label.map = parseDirectory(sub,label,sw);
		//System.out.println("No of documents are "+label.no_of_docs);
		return label;
	}
	
	public static int getNoOfFiles(File file){
		return file.list().length;
	}
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		
		// processing Train Data set==============================================================================
		StopWords sw = new StopWords();
		//File TrainDirectory = new File("K:/Master's/4thSEM/ML/Assignments/4/train_dataset1");
		File TrainDirectory = new File(args[0]);
		String[] subDirectories = TrainDirectory.list();
		HashMap<String,Integer> subDirectoryToClass = new HashMap<String,Integer>();
		Label[] classes = new Label[subDirectories.length];
		double[] probOfClasses = new double[classes.length];
		int class_num = 0, Total_No_Of_Doc = 0;
		for(String subDirectorie : subDirectories){
			File sub = new File(args[0]+"/"+subDirectorie);
			if(sub.isDirectory()){
				subDirectoryToClass.put(subDirectorie, class_num);
				classes[class_num] = getClassDetails(sub,class_num,sw);
				class_num++;
				System.out.println(subDirectorie+" is class-"+class_num);
			}
		}
		for(Label l : classes){
			System.out.println("No of Docs : Class : Unique Word Count : Total Word Count");
			System.out.println(l.no_of_docs +"\t\t "+l.class_num+"\t\t "+l.total_unique_word_count+"\t\t\t "+l.total_word_count);
			Total_No_Of_Doc += l.no_of_docs;
		}
		for(Label l : classes){
			//System.out.println(l.no_of_docs +" "+l.class_num+" "+l.total_unique_word_count+" "+l.total_word_count);
			probOfClasses[l.class_num] = (double)l.no_of_docs/(double)Total_No_Of_Doc;
			//System.out.println("Probablity of class "+l.class_num+" is "+probOfClasses[l.class_num]);
		}
		
		//===========================================================================================================
		
		//File TestDirectory = new File("K:/Master's/4thSEM/ML/Assignments/4/test_dataset1");
		File TestDirectory = new File(args[1]);
		String[] TestSubDirectories = TestDirectory.list();
		HashMap<String,Integer> testSubDirectoryToClass = new HashMap<String, Integer>();
		//Label[] TestClassess = new Label[TestSubDirectories.length];
		
		int test_actual_class_num = 0, correct = 0, TotalTestDocs = 0;
		for(String TestSubDirectorie: TestSubDirectories){
			File testSub = new File(args[1]+"/"+TestSubDirectorie);
			if(testSub.isDirectory()){
				if(subDirectoryToClass.containsKey(TestSubDirectorie))
					testSubDirectoryToClass.put(TestSubDirectorie, subDirectoryToClass.get(TestSubDirectorie));
				else
					testSubDirectoryToClass.put(TestSubDirectorie, test_actual_class_num);
				correct += getCorrectValues(testSub,classes,probOfClasses,test_actual_class_num,sw);
				test_actual_class_num++;
				TotalTestDocs+= testSub.listFiles().length;
			}
		}
		System.out.println("Accuracy of Naive Bayes for the given dataset is :"+(double)correct/(double)TotalTestDocs*100.0+" %");
	}
	
	public static int getCorrectValues(File testSub, Label[] classes, double[] probOfClasses, int class_num, StopWords sw)throws IOException{
		int correct = 0;	
		for(File file : testSub.listFiles()){
				HashMap<String,Integer> map = parseFile(file,sw);
				int val = predictClass(map,classes,probOfClasses);
				if(val == class_num){
					correct+=1;
				}
			}
		//System.out.println("No of class "+class_num+" predicted correct are "+correct);
		return correct;
	}
	
	public static HashMap<String,Integer> parseFile(File file, StopWords sw) throws IOException{
		FileReader reader;
		HashMap<String,Integer> map =  new HashMap<>();
			reader = new FileReader(file);
			BufferedReader br=new BufferedReader(reader);
			String line, token;
			StringTokenizer st;
			while((line=br.readLine())!=null && !line.toLowerCase().contains("lines")){}
			while((line=br.readLine())!=null){
				line = line.replaceAll("\\<.*?>", ""); //remove sgml tags
				line = line.replaceAll("\\/", " ");    //remove /
				line = line.replaceAll("\\d+.*", "");  // remove digits
				line = line.replaceAll("\\'s", " ");   //remove 's
				line = line.replaceAll("\\'", ""); 
				line = line.replaceAll("\\(", ""); 
				line = line.replaceAll("\\)", ""); 
				line = line.replaceAll("\\,", " ");    //remove ,
				line = line.replaceAll("\\.", "");     //remove .
				line = line.replaceAll("\\-", " ");    //remove -
				line = line.toLowerCase();			   //convert to lower case
				st = new StringTokenizer(line," \n\t,:;?{}()[]+ ");
				while(st.hasMoreTokens()){
					token = st.nextToken();
					token = parseToken(token);
					if(sw.stop(token)){
						
					}else{
						if(map.containsKey(token)){
							map.put(token, map.get(token)+1);
							//total_word_count++;
						}else{
							map.put(token, 1);
							//total_word_count++;
							//total_unique_word_count++;
						}
					}
				}
		}
		br.close();
		return map;
	}
	
	private static int predictClass(HashMap<String, Integer> map, Label[] classes, double[] probOfClasses) {
		double[] probOfClassGivenDocument = new double[classes.length];
		for(int i = 0; i < probOfClassGivenDocument.length; i++){
			probOfClassGivenDocument[i] = probOfClasses[i];
		}
		for(Label l: classes){
			for(Map.Entry<String, Integer> e: map.entrySet()){
				String key = e.getKey();
				Integer val = e.getValue();
				double Tct = 1.0,denominator = 1.0;
				
				if(l.map.containsKey(key)){
					Tct = l.map.get(key) + 1;
					//System.out.println(Tct);
				}else{
					Tct = 1;
					//System.out.println(Tct);
				}
				denominator = l.total_word_count;
				//System.out.println(denominator);
				probOfClassGivenDocument[l.class_num] *= Math.pow((double)Tct/(double)denominator,1/val);
				//System.out.println(probOfClassGivenDocument[l.class_num]);
			}
		}
		double max = Double.MIN_VALUE;
		int pos = -1;
		for(int i = 0; i < probOfClassGivenDocument.length; i++){
			//System.out.println(probOfClassGivenDocument[i]);
			if(max < probOfClassGivenDocument[i]){
				max = probOfClassGivenDocument[i];
				pos = i;
			}
		}
		return pos;
	}
}
