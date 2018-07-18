package scrapper;

import java.util.LinkedList;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SalesNavAccountsParser extends Parser {

	public SalesNavAccountsParser() {
		super();
	}
	
	public LinkedList<Info> parse(String html){
		list = new LinkedList<Info>();
		// testing with company info
		try {
			Document doc = Jsoup.parse(html, baseUrl);
			
			Elements elements = doc.select("div.content-wrapper");
		    ListIterator<Element> it = elements.listIterator();
		    Element element = it.next();
			
			while(element != null){
				Info info = new Info();
				Elements name = element.select("h4.name a");
		  		System.out.println("Company Link :- "+name.attr("abs:href").toString());
		  		String longLink = name.attr("abs:href").toString().trim();
		  		if(longLink.toLowerCase().contains("&pagekey")) info.setLink(commaSkiping(longLink.substring(0, longLink.indexOf("&pageKey"))));
		  		else info.setLink(commaSkiping(longLink));
		  		info.setCurrentCompany(commaSkiping(name.attr("title").toString()));
		  		System.out.println("Company Name :- "+name.attr("title").toString());

		  				  					
				Elements industry = element.select("div.info p.info-value:eq(0)");
		  		System.out.println("Industry :- "+industry.text());
		  		info.setIndustry(commaSkiping(industry.text()));
		  		
		  		Elements location = element.select("div.info p.info-value:eq(1)");
		  		System.out.println("Location :- "+location.text());
		  		String loca = location.text();
		  		if (loca.equalsIgnoreCase("employe")) {
					info.setCompanySize(commaSkiping(loca));
				} else {
			  		info.setLocation(commaSkiping(location.text()));
			  		Elements comSize = element.select("div.info p.info-value:eq(2)");
			  		System.out.println("com_size :- "+comSize.text());
					info.setCompanySize(commaSkiping(comSize.text()));
				}
		  		

				info.setEmail("");
				info.setPhone("");
				info.setSecondName("");
				info.setFirstName("");
				info.setCurrentJobTitle("");
				list.add(info);
				
				element = it.next();
				
			}
		} catch (Exception e) {
			//System.out.println("error-"+e.getMessage());
			//e.printStackTrace();
		}

	return list;	
	}
	
	
}
