package pojo;

public class Job {
	private String jobTitle, jobLink, company, companyLink, Location, jobDescription;
	public Job() {
	}
	public Job(String jobTitle, String jobLink, String company, String companyLink, String location, String jobDescription) {
		super();
		this.jobTitle = jobTitle;
		this.jobLink = jobLink;
		this.company = company;
		this.companyLink = companyLink;
		Location = location;
		this.jobDescription = jobDescription;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	
	public String getJobLink() {
		return jobLink;
	}
	public void setJobLink(String jobLink) {
		this.jobLink = jobLink;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanyLink() {
		return companyLink;
	}
	public void setCompanyLink(String companyLink) {
		this.companyLink = companyLink;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	
	
	
}
