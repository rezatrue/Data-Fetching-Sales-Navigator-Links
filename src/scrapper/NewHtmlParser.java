package scrapper;

import java.io.File;
import java.util.LinkedList;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import application.MainController;

public class NewHtmlParser extends Parser  {
	private String industries;
	private String companySize;
	
	public NewHtmlParser(){
		super();
	}
	
	public LinkedList<Info> parse(String html){
		list = new LinkedList<Info>();
		getSettings(html);
		try {
			Document doc = Jsoup.parse(html, baseUrl);
			
			//Elements elements = doc.select("div.core-rail.search-results-page");
		    //Elements elements = doc.select("ul.results-list > li.search-result.search-entity.search-result--person.ember-view");
		    Elements elements = doc.select("div.search-result__wrapper");
		    ListIterator<Element> it = elements.listIterator();
		    Element element = it.next();
			
			while(element != null){
				Info info = new Info();
				Elements name = element.select("a span.name");
		  		System.out.println("name :- "+name.text());
				
		  		String fullname = name.text();
		  		if (fullname.contains(" ")) {
		  			String fname = fullname.substring(0, fullname.indexOf(' '));
			  		info.setFirstName(commaSkiping(fname));
			  		String sname = fullname.substring(fullname.indexOf(' ')+1, fullname.length());
					info.setSecondName(commaSkiping(sname));
				} else {
			  		info.setFirstName(commaSkiping(fullname));
				}
		  		
				Elements link = element.select("a");
		  		System.out.println("Link :- "+link.attr("abs:href").toString());

				info.setLink(commaSkiping(link.attr("abs:href").toString()));
				Elements location = element.select("p.subline-level-2");
		  		System.out.println("Location :- "+location.text());
				info.setLocation(commaSkiping(location.text()));
				
				info.setIndustry(commaSkiping(""));
				
				Elements job = element.select("p.search-result__snippets");
		  		System.out.println("job :- "+job.text());
		  		//.............................
		  		Elements job1 = element.select("p.subline-level-1");
		  		System.out.println("job :- "+job1.text());
		  		String job_line1 = job1.text();
		  		String job_sub = job.text();
		  		String jobTitle = "";
		  		String company = "";
		  		if (job_sub.toLowerCase().contains("past:")) job_sub = "";
		  		if(!job_sub.equals("")) {
		  			if(job_sub.contains(" at ")) jobTitle = job_sub.substring(0, job_sub.indexOf(" at ")).trim();
		  			else jobTitle = job_sub;
		  			if(job_sub.contains(" at ")) company = job_sub.substring(job_sub.indexOf(" at ")+4, job_sub.length()).trim();
		  			if(jobTitle.toLowerCase().contains("current:")) jobTitle = jobTitle.substring(("current:").length(), jobTitle.length());
		  		} 
		  		if(jobTitle.length()<1){
		  			if(job_line1.contains(" at ")) jobTitle = job_line1.substring(0, job_line1.indexOf(" at "));
		  			else jobTitle = job_line1;
		  		}
		  		if(company.length()<1){
		  			if(job_line1.contains(" at ")) company = job_line1.substring(job_line1.indexOf(" at ")+4, job_line1.length());
		  		}
		  		System.out.println("job :- "+jobTitle + " Company :- " + company);
				info.setCurrentJobTitle(commaSkiping(jobTitle));
				info.setCurrentCompany(commaSkiping(company));

				info.setIndustry(MainController.industry);
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
	
	
}
