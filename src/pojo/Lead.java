package pojo;

public class Lead {
	private String link, firstName, lastName, address, serviceAge, currentJobTitle, currentCompany,companyProfile;
	public Lead() {
	}
	public Lead(String link, String firstName, String lastName, String address, String serviceAge,
			String currentJobTitle, String currentCompany, String companyProfile) {
		super();
		this.link = link;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.serviceAge = serviceAge;
		this.currentJobTitle = currentJobTitle;
		this.currentCompany = currentCompany;
		this.companyProfile = companyProfile;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getServiceAge() {
		return serviceAge;
	}
	public void setServiceAge(String serviceAge) {
		this.serviceAge = serviceAge;
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
	public String getCompanyProfile() {
		return companyProfile;
	}
	public void setCompanyProfile(String companyProfile) {
		this.companyProfile = companyProfile;
	}
	
	
	
	
}
