package scrapper;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

public class CsvGenerator {

	public CsvGenerator() {
	}

	public String listtoCsv(LinkedList<Info> list, String keyword) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Calendar cal = Calendar.getInstance();
		String fileName = dateFormat.format(cal.getTime());

		FileWriter writer = null;
		try {
			writer = new FileWriter("Linkedin_" + keyword + "_list_" + fileName + ".csv");

			writer.append("Linkedin_Profile_URL");
			writer.append(",");
			writer.append("First_Name");
			writer.append(",");
			writer.append("Last_Name");
			writer.append(",");
			writer.append("Email_ID");
			writer.append(",");
			writer.append("Contact_Number");
			writer.append(",");
			writer.append("Location");
			writer.append(",");
			writer.append("Industry");
			writer.append(",");
			writer.append("Designation");
			writer.append(",");
			writer.append("Company_Name");
			writer.append(",");
			writer.append("Company_Size");
			writer.append(",");
			writer.append("\n");

			Iterator it = list.iterator();

			while (it.hasNext()) {
				Info info = (Info) it.next();
				writer.append(commaSkiping(info.getLink()));
				writer.append(",");
				writer.append(commaSkiping(info.getFirstName()));
				writer.append(",");
				writer.append(commaSkiping(info.getSecondName()));
				writer.append(",");
				writer.append(commaSkiping(info.getEmail()));
				writer.append(",");
				writer.append(commaSkiping(info.getPhone()));
				writer.append(",");
				writer.append(commaSkiping(info.getLocation()));
				writer.append(",");
				writer.append(commaSkiping(info.getIndustry()));
				writer.append(",");
				writer.append(commaSkiping(info.getCurrentJobTitle()));
				writer.append(",");
				writer.append(commaSkiping(info.getCurrentCompany()));
				writer.append(",");
				writer.append(commaSkiping(info.getCompanySize()));
				writer.append("\n");
			}

			writer.flush();
			writer.close();
			return "Done";

		} catch (IOException e) {
			// e.printStackTrace();
			return "Error" + e.getMessage();
		}

	}

	protected String commaSkiping(String text) {
		String newText = text;
		if (newText.contains(","))
			if (!newText.startsWith("\"") && !newText.endsWith("\""))
				newText = "\"" + newText + "\"";
		return newText;
	}

}
