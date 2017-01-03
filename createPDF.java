import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import javax.imageio.ImageIO;
import org.apache.commons.io.DirectoryWalker.CancelException;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * Convert a set of .png files into a single pdf
 * 
 * @author Matthias Wettstein <matthias.wettstein@zoho.com>
 */
public class cPDF{
	
	public static void main(String[] args){	
		pdfCreate();
	}	

	/**
	 * Sort the png files according to date in the directory
	 * 
	 * @param files Listed files in folder containing the PNGs
	 * @return Sorted list of png files
	 */ 
	private static void sortDirectoryFiles(File[] files){
		// sort directory list
		// http://superuser.com/questions/238825/sort-files-by-date-modified-but-folders-always-before-files-in-windows-explorer
		Arrays.sort(files, new Comparator<File>(){
		    public int compare(File f1, File f2) {
		    	return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			}
		});
	}
	
	/**
	 * Analyze and re-arrange file names
	 * 
	 * @param file PNG file
	 * @param path Path containing PNG file
	 * @return
	 */
	private static String[] dissectFile(File file, File path){
		String fileString = file.getName();
		String fileExtension = FilenameUtils.getExtension(fileString);
		String fileName = FilenameUtils.removeExtension(fileString);
		// concatenate the full file path
		String filePath = new File(path, fileString).getPath();
		String[] fileAssembly = {fileString, fileExtension, fileName, filePath};
		return fileAssembly;
	}
	    

	/**
	 * Create the PDF file
	 */
	private static void pdfCreate(){
		// set global naming
		String resultName = "<yourFolderNameContainingThePNGs>"; 
		// get current date
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date today = Calendar.getInstance().getTime(); 
		String reportDate = dateFormat.format(today);
		// get main path
		String mainPath = "<yourPathToThePNGs>";
		// create document
		PDDocument document = new PDDocument();
		// define font
		PDFont font = PDType1Font.COURIER;
		PDColor color = new PDColor(new float[]{0, 0, 1}, PDDeviceRGB.INSTANCE);
		// set global path for looping over (raw material path)
		File materialPath = new File(mainPath+resultName);
		// create directory list
		File[] files = materialPath.listFiles();
		// sort directory files
		sortDirectoryFiles(files);
		// loop through files
		for(File file : files){		
			// select only PNGs
			if(dissectFile(file, materialPath)[1].equals("png")){
				System.out.println("Processing: "+dissectFile(file, materialPath)[0]);
				// create page, add it to document
				PDPage page = new PDPage();
				document.addPage(page); 
				// create page header
				String pageHeader = resultName.concat(" - ").concat(dissectFile(file, materialPath)[2].concat(reportDate));
				try{
					// grab PNGs file
					// http://stackoverflow.com/questions/35397634/pdfbox-v2-write-png-image-to-pdf-file-getting-empty-file
					BufferedImage awtImage = ImageIO.read(new File(dissectFile(file, materialPath)[3]));
					PDImageXObject pdImageXObject = LosslessFactory.createFromImage(document, awtImage);
					// create the page's content stream
					PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND,true,true);
					// embed PNGs into content stream
					contentStream.drawImage(pdImageXObject, 30, 120, awtImage.getWidth()/4, awtImage.getHeight()/4);
					// add text
					contentStream.setFont(font, 16);
					contentStream.setNonStrokingColor(color);
				    contentStream.beginText();
				    contentStream.newLineAtOffset(75, 710);
				    contentStream.showText("Finance Operations Credit & Collections");
				    contentStream.endText();
				    contentStream.beginText();
				    contentStream.newLineAtOffset(75, 690);
				    contentStream.showText(pageHeader);
				    contentStream.endText(); 
					contentStream.close();		
				} catch(IOException e){
					new Exception().printStackTrace();
				} 
			}
		}
		try{
			// save and close PDF
			String outputPath = new File(mainPath, resultName+"_"+reportDate+".pdf").getPath();
			document.save(outputPath);
			document.close();
			System.out.println("\nPDF created");
		} catch(CancelException f){
			new Exception().printStackTrace();
		} catch(IOException e){
			new Exception().printStackTrace();
		} 	
	}	
}