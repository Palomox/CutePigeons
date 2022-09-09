package ga.palomox.cutepigeons.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.ebean.Model;

@Entity
@Table(name = "pigeons")
public class Pigeon extends Model{
	@Id
	@GeneratedValue
	private int id;
	private String url;
	private boolean allowed; 
	@ManyToOne
	private User cutepigeonsUser;
	/**
	 * Empty constructor for ebean
	 */
	public Pigeon() {
		
	}
	public Pigeon(int id, String url, boolean allowed) {
		this.id = id;
		this.url = url;
		this.allowed = allowed; 
	}

	public int getId() {
		return id;
	}
	public String getUrl() {
		return url;
	}
	public User getUser() {
		return this.cutepigeonsUser;
	}
	public void setUser(User user) {
		this.cutepigeonsUser = user;
	}
	public boolean isAllowed() {
		return allowed;
	}
	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}
	
}
