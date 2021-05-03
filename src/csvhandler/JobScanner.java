package csvhandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import db.DbJob;
import pojo.Job;



public class JobScanner extends CsvScanner{

	private DbJob db = null;
	public JobScanner() {
		super();
		db = new DbJob();
	}
	
	
	private LinkedList<Job> list = null;

	public LinkedList<Job> dataScan(String filePath) {
		return list;
	}
	
	public int transferDataCsvToDb(String filePath) {
		location = filePath.substring(0, filePath.lastIndexOf("\\")+1);
		Job job = null;
		Scanner scanner = null;
		int insertCount = 0;
		try {
			scanner = new Scanner(new File(filePath));
			boolean rightFormat = false;
			if (scanner.hasNext()) {
				List<String> line = parseLine(scanner.nextLine());

				if ("Job_Link" != line.get(0).toString() || "Job_Title" != line.get(1).toString()
						|| "Company" != line.get(2).toString() || "Company_Link" != line.get(3).toString()
						|| "Location" != line.get(4).toString() || "Job_Description" != line.get(5).toString()) {
					System.out.println("WRONG FILE");
					rightFormat = true;
				}

			}
			if (!scanner.hasNext())
				System.out.println("EMPTY FILE");
			
			if(rightFormat) {
				while (scanner.hasNext()) {
					List<String> line = parseLine(scanner.nextLine());
					job = new Job(removingQuotes(line.get(0)), removingQuotes(line.get(1)), removingQuotes(line.get(2)), removingQuotes(line.get(3)),
							removingQuotes(line.get(4)), removingQuotes(line.get(5)));
					insertCount += db.insert(job) ? 1 : 0;
					System.out.println("[Job_Link= " + line.get(0) + ", Job_Title= " + line.get(1)
							+ " , Company=" + line.get(2) + " , Company_Link=" + line.get(3) + ", Location= " + line.get(4) + ", Job_Description= "
							+ line.get(5) + "]");
	
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		scanner.close();
		return insertCount;
	}

}
