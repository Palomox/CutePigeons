package ga.palomox.cutepigeons.model;

public class Post {
	private int pigeonId;
	private String pigeonUrl;
	private String userDisplayName;
	
	public Post(int pigeonId, String pigeonUrl, String userDisplayName) {
		this.pigeonId = pigeonId;
		this.pigeonUrl = pigeonUrl;
		this.userDisplayName = userDisplayName;
	}

	public int getPigeonId() {
		return pigeonId;
	}

	public String getPigeonUrl() {
		return pigeonUrl;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}
	
	
}
