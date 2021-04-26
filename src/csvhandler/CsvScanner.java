package csvhandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import pojo.People;

public class CsvScanner {

	//........
	protected String location = null;
	public CsvScanner() {
		// default location present directory
		location = "";
	}
	
	public int transferDataCsvToDb(String filePath) {
		return 0;
	}
	
	// Quotes "" creates problem when file load second time
	// we need to remove those
	protected String removingQuotes(String text) {
		String newText = text;

			if (text.startsWith("\"")) {
				System.out.println("newText > > >");
				newText = text.substring(1, text.length());
				System.out.println("newText ::: " + newText);
			}
				
		return newText;
	}
	//......................
	


	// use third party OpenCSV library
	// source :
	// https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE = '"';

	
	public LinkedList<?> dataScan(String filePath){
		return new LinkedList<>();
	}
	
/*	
	private LinkedList<Info> list = null;	
	public LinkedList<Info> dataScan(String filePath) {
		location = filePath.substring(0, filePath.lastIndexOf("\\")+1);
		list = new LinkedList<>();
		Info info = null;
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filePath));
			boolean rightFormat = false;
			if (scanner.hasNext()) {
				List<String> line = parseLine(scanner.nextLine());

				if ("Linkedin_Profile_URL" != line.get(0).toString() || "First_Name" != line.get(1).toString()
						|| "Last_Name" != line.get(2).toString() || "Email_ID" != line.get(3).toString()
						|| "Contact_Number" != line.get(4).toString() || "Location" != line.get(5).toString()
						|| "Industry" != line.get(6).toString() || "Designation" != line.get(7).toString()
						|| "Company_Name" != line.get(8).toString() || "Company_Size" != line.get(9).toString()) {
					System.out.println("WRONG FILE");
					rightFormat = true;
				}

			}
			if (!scanner.hasNext())
				System.out.println("EMPTY FILE");
			
			if(rightFormat) {
				while (scanner.hasNext()) {
					List<String> line = parseLine(scanner.nextLine());
					info = new Info(removingQuotes(line.get(0)), removingQuotes(line.get(1)), removingQuotes(line.get(2)), removingQuotes(line.get(3)),
							removingQuotes(line.get(4)), removingQuotes(line.get(5)), removingQuotes(line.get(6)), removingQuotes(line.get(7)), 
							removingQuotes(line.get(8)), removingQuotes(line.get(9)));
					list.add(info);
					System.out.println("[Linkedin_Profile_URL= " + line.get(0) + ", First_Name= " + line.get(1)
							+ " , Last_Name=" + line.get(2) + ", Email_ID= " + line.get(3) + ", Contact_Number= "
							+ line.get(4) + " , Location=" + line.get(5) + ", Industry= " + line.get(6) + ", Designation= "
							+ line.get(7) + " , Company_Name=" + line.get(8) + ", Company_Size= " + line.get(9) + "]");
	
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		scanner.close();
		return list;
	}
*/
	
	public List<String> parseLine(String cvsLine) {
		return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
	}

	public List<String> parseLine(String cvsLine, char separators) {
		return parseLine(cvsLine, separators, DEFAULT_QUOTE);
	}

	public List<String> parseLine(String cvsLine, char separators, char customQuote) {

		List<String> result = new ArrayList<>();

		// if empty, return!
		if (cvsLine == null && cvsLine.isEmpty()) {
			return result;
		}

		if (customQuote == ' ') {
			customQuote = DEFAULT_QUOTE;
		}

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuffer curVal = new StringBuffer();
		boolean inQuotes = false;
		boolean startCollectChar = false;
		boolean doubleQuotesInColumn = false;

		char[] chars = cvsLine.toCharArray();

		for (char ch : chars) {

			if (inQuotes) {
				startCollectChar = true;
				if (ch == customQuote) {
					inQuotes = false;
					doubleQuotesInColumn = false;
				} else {

					// Fixed : allow "" in custom quote enclosed
					if (ch == '\"') {
						if (!doubleQuotesInColumn) {
							curVal.append(ch);
							doubleQuotesInColumn = true;
						}
					} else {
						curVal.append(ch);
					}

				}
			} else {
				if (ch == customQuote) {

					inQuotes = true;

					// Fixed : allow "" in empty quote enclosed
					if (chars[0] != '"' && customQuote == '\"') {
						curVal.append('"');
					}

					// double quotes in column will hit this!
					if (startCollectChar) {
						curVal.append('"');
					}

				} else if (ch == separators) {

					result.add(curVal.toString());

					curVal = new StringBuffer();
					startCollectChar = false;

				} else if (ch == '\r') {
					// ignore LF characters
					continue;
				} else if (ch == '\n') {
					// the end, break!
					break;
				} else {
					curVal.append(ch);
				}
			}

		}

		result.add(curVal.toString());

		return result;
	}

}
