package scrapper;

import java.util.LinkedList;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser extends Parser {

	public HtmlParser(){
		super();
	}
	
	public LinkedList<Info> parse(String html){
		list = new LinkedList<Info>();
		
		try {
			Document doc = Jsoup.parse(html, baseUrl);
			Elements elements = doc.select("li[data-li-position]");
			ListIterator<Element> it = elements.listIterator();
			Element element = it.next();
			
			while(element != null){
				Info info = new Info();
				Elements name = element.select("h3 > a");
				//System.out.println(name.text());
				
				String fullname = name.text();
		  		if (fullname.contains(" ")) {
		  			String fname = fullname.substring(0, fullname.indexOf(' '));
			  		info.setFirstName(commaSkiping(fname));
			  		String sname = fullname.substring(fullname.indexOf(' ')+1, fullname.length());
					info.setSecondName(commaSkiping(sname));
				} else {
			  		info.setFirstName(commaSkiping(fullname));
				}
				
				Elements link = element.select("h3 > a");
				//System.out.println(link.attr("href").toString());
				info.setLink(commaSkiping(link.attr("href").toString()));
				String location = element.select("dl.demographic > dd:lt(2)").text();
				//System.out.println(location);
				info.setLocation(commaSkiping(location));
				String industry = element.select("dl.demographic > dd:gt(2)").text();
				//System.out.println(industry);
				info.setIndustry(commaSkiping(industry));
				String current = element.select("p.title").text();
				if (current.toLowerCase().contains(" at ")) {
					String currentJobTitle = current.substring(0, current.indexOf(" at ")).trim();
					//System.out.println(currentJobTitle);
					info.setCurrentJobTitle(commaSkiping(currentJobTitle));
					String currentCompany = current.substring(current.indexOf(" at ")+4, current.length()).trim();
					//System.out.println(currentCompany);
					info.setCurrentCompany(commaSkiping(currentCompany));
					//System.out.println("-x----x----x----x----x----x-");
				}
				
				info.setCompanySize("");
				info.setEmail("");
				info.setPhone("");
				list.add(info);
				
				element = it.next();
				
			}
		} catch (Exception e) {
			//System.out.println("error-"+e.getMessage());
			//e.printStackTrace();
		}
		System.out.println("1returning list size -> "+list.size());

	return list;	
	}
	
	
	
}
