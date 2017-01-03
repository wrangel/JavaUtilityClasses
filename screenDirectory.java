import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

/**
 * Screen a folder for all contained files, write out a text file with the file list.
 * 
 * @author Matthias Wettstein <matthias.wettstein@zoho.com>
 */
public class SD{
	
* @param args[0] Path of the directory which needs to be screened
	 * @param args[1] Path where "currentFile.txt" is saved to (containing a list of the screened files) 
	 * @param args[2] Method switch: "file" for file screening, "dir" for directory screening, "both" for both screenings
	public static void main(String[] args){	
		try{
			File[] files = new File(args[0]).listFiles();
			try{
				saveFiles(showFiles(files), args[1]);
			} catch(NullPointerException e){
				System.out.println("No files in directory");
				System.exit(0);
	    	} 
		} catch(ArrayIndexOutOfBoundsException f){
    		System.out.println("Wrong number of inputs - aborting process");
    		System.exit(0);
		} catch(IllegalArgumentException g){
			System.out.println("Missing inputs - aborting process");
			System.exit(0);
    	} 		
	}
	
	/**
	 * Recursively catch file names in a folder
	 * 
	 * @param files List of elements in a folder
	 * @return fileNameList File name list
	 */
	public static ArrayList<String> showFiles(File[] files) throws NullPointerException{ 
		// create filename container
		ArrayList<String> fileNameList = new ArrayList<String>();
		// recursively screen through files and append them to container
		for (File file : files){
			String fileName = file.getName();
			if (file.isDirectory()){
		        	showFiles(file.listFiles()); // Calls same method again.
		        } else{
		        	//System.out.println("File: " + fileName);
		            fileNameList.add(fileName);
		        }
		    }
		return fileNameList;
	}
	
	/**
	 * Save the file list 
	 * 
	 * @param fileNameList File name list
	 * @param outputPathString Path where "currentFile.txt" is saved to (containing a list of the screened files) 
	 */
	public static void saveFiles(ArrayList<String> fileNameList, String outputPathString){
		String outputFileName = outputPathString+"currentFile.txt";
		try{
			FileWriter writer;
			writer = new FileWriter(outputFileName);
			for(String fN : fileNameList){
				//System.out.println("writing starting");
				System.out.println(fN);
				writer.write(fN + System.getProperty("line.separator"));
				//System.out.println("writing done");
			}
			writer.close();
		} catch(IOException e){
			e.printStackTrace();
	    }
	}	
}