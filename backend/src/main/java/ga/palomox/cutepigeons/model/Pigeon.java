package ga.palomox.cutepigeons.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pigeons")
public class Pigeon {
	@Column(name = "id")
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "url")
	private String url;
	/**
	 * Empty constructor for hibernate
	 */
	public Pigeon() {
		
	}
	public Pigeon(int id, String url) {
		this.id = id;
		this.url = url;
	}

	public int getId() {
		return id;
	}
	public String getUrl() {
		return url;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pigeon other = (Pigeon) obj;
		if (id != other.id)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	
	
}
