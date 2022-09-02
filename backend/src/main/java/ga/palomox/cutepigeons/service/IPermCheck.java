package ga.palomox.cutepigeons.service;

import java.util.UUID;

import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

@Service
public interface IPermCheck {
	public boolean hasPerm(RequestEntity<?> request, String relation, String namespace, String _object);
	public boolean hasPerm(UUID userId, String relation, String namespace, String _object);
	public boolean isGroupMember(UUID userId, String group);
	public boolean isGroupMember(RequestEntity<?> request, String group); 
	public boolean isAuthenticated(RequestEntity<?> request);
}
