package scrapper;

public class Company {
	private String comUrl, comName, comHeadquarters, comWebsite, comFounded, comSize ,comIndustry, comType;
	
	public Company() {
	}

	public Company(String comUrl, String comName, String comHeadquarters, String comWebsite, String comFounded,
			String comSize, String comIndustry, String comType) {
		this.comUrl = comUrl;
		this.comName = comName;
		this.comHeadquarters = comHeadquarters;
		this.comWebsite = comWebsite;
		this.comFounded = comFounded;
		this.comSize = comSize;
		this.comIndustry = comIndustry;
		this.comType = comType;
	}

	public String getComUrl() {
		return comUrl;
	}

	public void setComUrl(String comUrl) {
		this.comUrl = comUrl;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getComHeadquarters() {
		return comHeadquarters;
	}

	public void setComHeadquarters(String comHeadquarters) {
		this.comHeadquarters = comHeadquarters;
	}

	public String getComWebsite() {
		return comWebsite;
	}

	public void setComWebsite(String comWebsite) {
		this.comWebsite = comWebsite;
	}

	public String getComFounded() {
		return comFounded;
	}

	public void setComFounded(String comFounded) {
		this.comFounded = comFounded;
	}

	public String getComSize() {
		return comSize;
	}

	public void setComSize(String comSize) {
		this.comSize = comSize;
	}

	public String getComIndustry() {
		return comIndustry;
	}

	public void setComIndustry(String comIndustry) {
		this.comIndustry = comIndustry;
	}

	public String getComType() {
		return comType;
	}

	public void setComType(String comType) {
		this.comType = comType;
	}

	
	
	
}
