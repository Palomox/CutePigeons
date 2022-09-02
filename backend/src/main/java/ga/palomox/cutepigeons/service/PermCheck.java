package ga.palomox.cutepigeons.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import sh.ory.ApiClient;
import sh.ory.ApiException;
import sh.ory.api.ReadApi;
import sh.ory.api.V0alpha2Api;
import sh.ory.model.GetCheckResponse;
import sh.ory.model.Session;

@Service
public class PermCheck implements IPermCheck{
	
	@Autowired
	private V0alpha2Api oryApi;
	
    @Value("${ory.keto.baseUrl}")
    private String ketoBaseurl;

	
	private ReadApi readApi = new ReadApi(new ApiClient().setBasePath(ketoBaseurl));
	
	@Override
	public boolean hasPerm(RequestEntity<?> request, String relation, String namespace, String _object) {
		Session session= null;
		try {
			session = oryApi.toSession(null, request.getHeaders().getFirst("cookie"));
		} catch (ApiException e) {
			e.printStackTrace();
			return false; 
		}
		
		if(session == null) {
			return false;
		}
		
		if(session.getActive()) {
			UUID userId = session.getIdentity().getId();
			return this.hasPerm(userId, relation, namespace, _object);
		}else {
			return false; 
		}
	}

	@Override
	public boolean hasPerm(UUID userId, String relation, String namespace, String _object) {
		GetCheckResponse response;
		try {
			response = readApi.getCheck(namespace, _object, relation, userId.toString(), null, null, null, null);
		} catch (ApiException e) {
			e.printStackTrace();
			return false; 
		}
		return response.getAllowed();
	}

	@Override
	public boolean isGroupMember(UUID userId, String group) {
		return this.hasPerm(userId, "member", "groups", group);
	}

	@Override
	public boolean isGroupMember(RequestEntity<?> request, String group) {
		return this.hasPerm(request, "member", "groups", group);
	}

	@Override
	public boolean isAuthenticated(RequestEntity<?> request) {
		Session session= null;
		try {
			session = oryApi.toSession(null, request.getHeaders().getFirst("cookie"));
		} catch (ApiException e) {
			e.printStackTrace();
			return false; 
		}
		
		if(session == null) {
			return false;
		}
		
		return session.getActive();

	}

}
