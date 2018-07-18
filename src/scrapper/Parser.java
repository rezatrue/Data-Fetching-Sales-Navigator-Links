package scrapper;

import java.util.LinkedList;

public class Parser {
	protected String baseUrl = "https://www.linkedin.com/";
	protected LinkedList<Info> list = null;

	protected LinkedList<Info> parse(String html) {
		return list;
	}

	protected String commaSkiping(String text) {
		String newText = text;
		// if (newText.contains(",")) {
		// newText = "\"" +newText + "\"";
		// }
		return newText;
	}

}
