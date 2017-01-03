import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copy a file from one folder to another.
 * 
 * @author Matthias Wettstein <matthias.wettstein@zoho.com>
 */
public class CF{
	
	/**
	 * 
	 * @param args[0]: Origin subfolder name
	 * @param args[1]: Origin folder path (containing origin subfolder)
	 * @param args[2]: Origin file name (without extension)
	 * @param args[3]: Target folder path
	 * @param args[4]: Target file name (without extension)
	 * @param args[5]: Target file extension
	 */
	public static void main(String[] args){	
		System.out.println("Arguments: "+args.length);
		for(String s:args) System.out.println(s);
		
		InputStream inStream = null;
		OutputStream outStream = null;
		try{
			String inputFile = args[1].trim()+"\\"+args[0].trim()+"\\"+args[2].trim()+" "+args[0].trim()+args[5].trim();
			String outputFile = args[3].trim()+"\\"+args[4].trim()+args[5].trim();
	    	try{
	    	    File afile = new File(inputFile);
	    	    File bfile = new File(outputFile);
	    	    inStream = new FileInputStream(afile);
	    	    outStream = new FileOutputStream(bfile);
	    	    byte[] buffer = new byte[1024];
	    	    int length;
	    	    //copy the file content in bytes 
	    	    while ((length = inStream.read(buffer)) > 0){
	    	    outStream.write(buffer, 0, length);
	    	    }
	    	    inStream.close();
	    	    outStream.close();
	    	    System.out.println("File is copied successfully!");
	    	} catch(IOException e1){
	    		e1.printStackTrace();
	    	}
		} catch(IllegalArgumentException e){
			System.out.println("Missing inputs - aborting process");
		}	
	}	
}