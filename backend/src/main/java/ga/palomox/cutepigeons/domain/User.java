package ga.palomox.cutepigeons.domain;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.ebean.Model;

@Entity
@Table(name="cutepigeons_users")
public class User extends Model{
	@Id
	private UUID uuid; 
	private String displayName;
	
	@OneToMany(mappedBy = "cutepigeonsUser", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Pigeon> pigeons;
	
	public User(UUID uuid, String displayName) {
		this.uuid = uuid;
		this.displayName = displayName;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	
}
