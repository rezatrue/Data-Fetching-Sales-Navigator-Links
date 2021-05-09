package pojo;

public class People {
	private String link, firstName, lastName, email, address, currentJobTitle, serviceRange, currentCompany, companyLocation, degreeName, fos, institute, dates;
	
	public People() {
	}

	public People(String link, String firstName, String lastName, String email, String address,
			String currentJobTitle, String serviceRange, String currentCompany, String companyLocation,
			String degreeName, String fos, String institute, String dates) {
		super();
		this.link = link;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.address = address;
		this.currentJobTitle = currentJobTitle;
		this.serviceRange = serviceRange;
		this.currentCompany = currentCompany;
		this.companyLocation = companyLocation;
		this.degreeName = degreeName;
		this.fos = fos;
		this.institute = institute;
		this.dates = dates;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCurrentJobTitle() {
		return currentJobTitle;
	}

	public void setCurrentJobTitle(String currentJobTitle) {
		this.currentJobTitle = currentJobTitle;
	}

	public String getServiceRange() {
		return serviceRange;
	}

	public void setServiceRange(String serviceRange) {
		this.serviceRange = serviceRange;
	}

	public String getCurrentCompany() {
		return currentCompany;
	}

	public void setCurrentCompany(String currentCompany) {
		this.currentCompany = currentCompany;
	}

	public String getCompanyLocation() {
		return companyLocation;
	}

	public void setCompanyLocation(String companyLocation) {
		this.companyLocation = companyLocation;
	}

	public String getDegreeName() {
		return degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	public String getFos() {
		return fos;
	}

	public void setFos(String fos) {
		this.fos = fos;
	}

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	
	
	
}
