import java.io.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * <p>
 * Copy a defined number of rows and columns out of a XLSX file and store them into a TXT file.
 * <p>
 * This serves as a preprocessing step for subsequent data wrangling with limited memory.
 * <p>
 * @author Matthias Wettstein <matthias.wettstein@zoho.com>
 */
public class XtT{
	
	public static void readFile(File inputFile, File outputFile, int rowLimit, int colLimit) throws IOException {	
	    FileInputStream fis = new FileInputStream(inputFile);
	    FileOutputStream fos = new FileOutputStream(outputFile);
	    System.out.println("Input file found");
	    XSSFWorkbook book = new XSSFWorkbook(fis);
	    XSSFSheet sheet = book.getSheetAt(0); 
	    
	    String delimiter = "\t";
	    String fixedInput = "__NA__";
	    
	    XSSFCellStyle cs = book.createCellStyle();
	    XSSFDataFormat df = book.createDataFormat();
	    cs.setDataFormat(df.getFormat("yyyy-M-dd"));
	    
	    /*int rowNum = sheet.getLastRowNum() + 1; 
	    int colNum = sheet.getRow(0).getLastCellNum(); */ 
	    System.out.println("Screening "+rowLimit+" rows and "+colLimit+" columns");
	    StringBuffer data = new StringBuffer();
	    
	    for(int i = 0; i < rowLimit; i++){
            XSSFRow row = sheet.getRow(i);
            if(row == null)continue; /* not run */
        	for(int j = 0; j < colLimit; j++){
            	XSSFCell cell = row.getCell(j);	
            	if(cell == null){
            		row.createCell(j).setCellValue(fixedInput);
            		data.append(fixedInput+delimiter);
                } else{
                	switch (cell.getCellType()) {
    				case Cell.CELL_TYPE_BOOLEAN:
    					data.append(cell.getBooleanCellValue()+delimiter);
    					break;
    				case Cell.CELL_TYPE_NUMERIC:
    					if (DateUtil.isCellDateFormatted(cell)){
    						cell.setCellStyle(cs);
    						data.append(cell+delimiter); //data.append(cell.getDateCellValue());
    					} else data.append(cell.getNumericCellValue()+delimiter);	 			
    					break;
    				case Cell.CELL_TYPE_STRING:
    					data.append(cell.getRichStringCellValue()+delimiter); // also possible: getStringCellValue
    					break;
    				case Cell.CELL_TYPE_BLANK:
    					data.append(fixedInput+delimiter);
    					break;
                    case Cell.CELL_TYPE_FORMULA:
                    	data.append(cell.getCachedFormulaResultType() + delimiter); // alas cached results are not correct 
                    	break;
                	case Cell.CELL_TYPE_ERROR:
                		data.append(fixedInput + delimiter);
                		break;                		
    				default:
    					data.append(fixedInput+delimiter);
    				}
                }          	          
            }
        	data.append("\r\n"); 
	    }
	    fos.write(data.toString().getBytes());
	    fos.close();
	    System.out.println("File successfully converted!");
    }
	
	/**
	 * @param args[0] is the path of the directory where input XLSX file is located and output TXT file is placed
	 * @param args[1] is the name of both input XLSX file and output TXT file
	 * @param args[2] is the number of rows to be screened in the XLSX file
	 * @param args[3] is the number of columns to be screened in the XLSX file
	 */
	public static void main(String[] args)  {	
		System.out.println("Arguments: "+args.length);
		for(String s:args) System.out.println(s);
		try{
			String inputFileName = args[0].trim()+"\\"+args[1].trim()+".xlsx";
			String outputFileName = args[0].trim()+"\\"+args[1].trim()+".txt"; 
		    File inputFile = new File(inputFileName);
		    File outputFile = new File(outputFileName);
		    int maxNumRows = Integer.parseInt(args[2]);
		    int maxNumCols = Integer.parseInt(args[3]);
		    try{
		    	readFile(inputFile, outputFile, maxNumRows, maxNumCols);
		    } catch(IOException e){
		    	e.printStackTrace();
		    }
		} catch(IllegalArgumentException e){
			System.out.println("Incorrect inputs - aborting process");
		} catch(ArrayIndexOutOfBoundsException f){	
			System.out.println("Missing inputs - aborting process");
		}	
	}
}		