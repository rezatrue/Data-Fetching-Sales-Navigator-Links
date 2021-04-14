package pojo;

public class Info {
	private String link, first_name, second_name, email, phone,  location, industry, currentJobTitle, currentCompany,companySize;
	public Info() {
	}
	public Info(String link, String first_name, String second_name, String email,
			String phone, String location, String industry,
			String currentJobTitle, String currentCompany,String companySize) {
		super();
		this.link = link;
		this.first_name = first_name;
		this.second_name = second_name;
		this.email = email;
		this.phone = phone;
		this.location = location;
		this.industry = industry;
		this.currentJobTitle = currentJobTitle;
		this.currentCompany = currentCompany;
		this.companySize = companySize;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getFirstName() {
		return first_name;
	}
	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}
	public String getSecondName() {
		return second_name;
	}
	public void setSecondName(String second_name) {
		this.second_name = second_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getCurrentJobTitle() {
		return currentJobTitle;
	}
	public void setCurrentJobTitle(String currentJobTitle) {
		this.currentJobTitle = currentJobTitle;
	}
	public String getCurrentCompany() {
		return currentCompany;
	}
	public void setCurrentCompany(String currentCompany) {
		this.currentCompany = currentCompany;
	}
	public String getCompanySize() {
		return companySize;
	}
	public void setCompanySize(String companySize) {
		this.companySize = companySize;
	}
	
	
	
}
