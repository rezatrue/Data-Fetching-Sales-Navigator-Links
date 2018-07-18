package scrapper;

import java.io.File;
import java.util.LinkedList;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class SalesNavigatorParser extends Parser {
//	public String baseUrl = "https://www.linkedin.com/";
//	public LinkedList<Info> list = null;
	private String industries;
	private String companySize;
	
	public SalesNavigatorParser(){
		super();
	}
	
	public LinkedList<Info> parse(String html){
		list = new LinkedList<Info>();
		
		getSettings(html);
		try {
			Document doc = Jsoup.parse(html, baseUrl);
			
			// person info
			Elements elements = doc.select("div.content-wrapper");
		    ListIterator<Element> it = elements.listIterator();
		    Element element = it.next();
			
			while(element != null){
				Info info = new Info();
				Elements name = element.select("h4.name a");
		  		//System.out.println("name Link :- "+name.attr("abs:href").toString());
		  		String longLink = name.attr("abs:href").toString().trim();
		  		if(longLink.toLowerCase().contains("&pagekey")) info.setLink(commaSkiping(longLink.substring(0, longLink.indexOf("&pageKey"))));
		  		else info.setLink(commaSkiping(longLink));
		  		//System.out.println("name title :- "+name.attr("title").toString());
		  		
				
		  		String fullname = name.text();
		  		if (fullname.contains(" ")) {
		  			String fname = fullname.substring(0, fullname.indexOf(' '));
			  		info.setFirstName(commaSkiping(fname));
		  			String lname = fullname.substring(fullname.indexOf(' ')+1, fullname.length());
					info.setSecondName(commaSkiping(lname));
		  			//System.out.println("Frist name :- "+ fname + " Last name :- "+ lname );
				} else {
			  		info.setFirstName(commaSkiping(fullname));
				}
		  		
		  		Elements come = element.select("span.company-name");
		  		String comp1 = come.text().toString();
		  		String comp2 = come.attr("title").toString();
		  		Elements come1 = element.select("a.company-name.company-link");
		  		String comp3 = come1.text().toString();
		  		String comp4 = come1.attr("title").toString();
		  		String company = comp1;
		  		if(company.length()<comp2.length()) company = comp2;
		  		if(company.length()<comp3.length()) company = comp3;
		  		if(company.length()<comp4.length()) company = comp4;
		  		//System.out.println("company :- "+ company);
				info.setCurrentCompany(commaSkiping(company));
		  		
		  		
				info.setIndustry(commaSkiping(""));
				
				Elements job = element.select("div.info p.info-value:eq(0)");
		  		//System.out.println("Designation :- "+job.text());
		  		info.setCurrentJobTitle(commaSkiping(job.text()));
		  		
		  		Elements location = element.select("div.info p.info-value:eq(2)");
		  		//System.out.println("location :- "+location.text());
				info.setLocation(commaSkiping(location.text()));

				info.setIndustry(commaSkiping(this.industries));
				info.setCompanySize(commaSkiping(this.companySize));

				info.setEmail("");
				info.setPhone("");
				
				list.add(info);
				
				element = it.next();
				
			}
		} catch (Exception e) {
			//System.out.println("error-"+e.getMessage());
			//e.printStackTrace();
		}
	System.out.println("parser list size -- "+ list.size());
	return list;	
	}
	
	public void getSettings(String html){
		// company size & industry
		Document doc = Jsoup.parse(html, baseUrl);
		String [] selectors = {"li.facet.I", "li.facet.CS"};
		for (String string : selectors) {
			StringBuilder sb = new StringBuilder("");
			Elements elements = doc.select(string);
			for (Element element : elements) {
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
	
	
//	public String commaSkiping(String text){
//		String newText = text;
//		if (newText.contains(",")) {
//			//newText = newText.trim().replace("," , " ");
//			newText = "\"" +newText + "\"";
//
//		}
//		return newText;
//	}
	
	
	
}
