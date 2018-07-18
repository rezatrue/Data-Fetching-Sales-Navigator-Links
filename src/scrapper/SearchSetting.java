package scrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SearchSetting {
	private String industries;
	private String companySize;
	
	public SearchSetting() {
		super();
	}

	public void setSettings(String html) {
		Document doc = Jsoup.parse(html);
//		Elements I = doc.select("li.facet.I"); // for Industry
//		Elements SC = doc.select("li.facet.CS"); // for Company Size
//		Elements G = doc.select("li.facet.G"); // for Geography

		String [] selectors = {"li.facet.I", "li.facet.CS"};
		for (String string : selectors) {
			StringBuilder sb = new StringBuilder("");
			Elements elementsI = doc.select(string);
			for (Element element : elementsI) {
				Elements industries = element.select("ul.selected-values-container label span.pill-text");
				int num = industries.size();
				int count = 0;
					for (Element element2 : industries) {
						count++;
						//System.out.print(element2.text());
						sb.append(element2.text());
						if(count < num)sb.append(" / ");
					}
				}
			if (string=="li.facet.I") this.industries = sb.toString();
			else if (string=="li.facet.CS") this.companySize = sb.toString();
		}
		
		
	}

	public String getIndustries() {
		return industries;
	}

	public String getCompanySize() {
		return companySize;
	}
	
	
}
