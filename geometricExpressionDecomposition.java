import java.io.*;
import static java.lang.Math.*;

/**
 * Given an positive integer x, calculate the decomposition of all positive integers up to (x*2-1) into unique elements
 * 
 * @author Matthias Wettstein <matthias.wettstein@zoho.com>
 *
 */
public class GeometricExpressionDecomposition {
	
	/**
	 * 
	 * @param args A member of the geometric expression (x)
	 */
	public static void main(String[] args) {
		try{
			int maxListNumber = Integer.parseInt(args[0]);
			double testMaxPower = log(maxListNumber)/log(2);
			if(maxListNumber<1 || testMaxPower != (int)testMaxPower)System.out.println("Invalid number argument: Number must be on interval [2^0, 2^1, ... , 2^(x-1), 2^x]");
			else{
				StringBuilder[] result = decomposeInteger(testMaxPower);
				generateCsvFile(result);
			}
		} catch(IllegalArgumentException e){
			System.out.println("Invalid number argument");
		}
	}
	
	/**
	 * Decompose 
	 * 
	 * @param testMaxPower The maximum exponent to formulate the decomposition.
	 * @return elementList The sum as well as its decomposition
	 */
	private static StringBuilder[] decomposeInteger(double testMaxPower){
		final int MAXPOWER = (int)(testMaxPower);
		// define maximum possible sum
		int maxSum = (int)(2*pow(2, MAXPOWER)-1);
		// create range of possible sumRange
		int[] sumRange = new int[maxSum];
		for(int i=0; i<sumRange.length; i+=1)sumRange[i] = i+1;
		// create element list
		StringBuilder[] elementList = new StringBuilder[maxSum];
		for(int j=0; j<elementList.length; j+=1) elementList[j] = new StringBuilder();
		// create binary sequence
		int[] binarySequence = new int[MAXPOWER+1];
		int power = MAXPOWER;
		for (int k=0; k<binarySequence.length; k+=1){
			binarySequence[k] = (int)(pow(2, power));
			power-=1;
		}
		// loop through sumRange 
		for(int l=0; l<sumRange.length; l+=1){	
			// initialize sum
			int sum = l+1;
			// loop through binary sequence
			elementList[l].append(sum).append(",");
			for(int element:binarySequence){
				// move on to the next sum if sum is not divisible by element
				if(sum/element<1) continue;
				elementList[l].append(element).append(",");
				// update the sum
				sum %= element;
				// move on to the next sum if modulus is 0
				if(sum == 0){
					elementList[l].setLength(elementList[l].length() - 1);
					continue;
				}		
			}
		}
		for(StringBuilder e : elementList) System.out.println(e);
		return elementList;
	}
	
	/**
	 * Write the decomposition into a csv file
	 * 
	 * @param elementListResult The sum as well as its decomposition 
	 */
	private static void generateCsvFile(StringBuilder[] elementListResult){
		String filePath = System.getProperty("user.home")+"/";
		String fileName = "geometricExpressionDecomposition"+".csv";
		File fileLocation = new File(filePath + fileName); 
		try{
		    FileWriter writer = new FileWriter(fileLocation);
		    for(StringBuilder eLR:elementListResult){
		    	writer.append(eLR.toString());
		    	writer.write("\r\n");
		    }
		    writer.flush();
		    writer.close();
		    System.out.println("Your result is saved as '"+fileName+"' in folder '"+filePath+"'.");
		}
		catch(IOException e){
		     e.printStackTrace();
		} 
	}	

}