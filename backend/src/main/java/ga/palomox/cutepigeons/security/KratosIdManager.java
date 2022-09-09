package ga.palomox.cutepigeons.security;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.jetty.server.Request;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ga.palomox.lightrest.rest.IdentityManager;
import sh.ory.ApiClient;
import sh.ory.ApiException;
import sh.ory.api.V0alpha2Api;
import sh.ory.model.Session;

public class KratosIdManager implements IdentityManager<KratosIdentity> {

	private V0alpha2Api ory;

	public KratosIdManager(String basePath) {
		ApiClient apiClient = new ApiClient().setBasePath(basePath);
		ory = new V0alpha2Api(apiClient);
	}

	public V0alpha2Api getApi() {
		return this.ory;
	}

	@Override
	public Optional<KratosIdentity> loadIdentity(Request baseRequest) {
		// We attempt to do Token based auth, as it's easier to discard the case
		Session session = null;
		if (baseRequest.getHeader("Authorization") != null) {
			// We do token auth
			String auth = baseRequest.getHeader("Authorization");
			if (auth.contains("Bearer")) {
				String token = auth.split(" ")[1];
				try {
					session = this.ory.toSession(token, null);
				} catch (ApiException e) {
					return Optional.empty();
				}
			}
		} else {
			// Otherwise, we do cookie auth
			String cookie = baseRequest.getHeader("cookie");
			if (cookie.contains("ory_session")) {
				try {
					session = this.ory.toSession(null, cookie);
				} catch (ApiException e) {
					return Optional.empty();
				}

			}
		}
		if (session == null || !session.getActive()) {
			return Optional.empty();
		}
		UUID id = session.getIdentity().getId();

		JsonObject tree = new Gson().toJsonTree(session.getIdentity().getTraits()).getAsJsonObject();

		String name = tree.get("username").getAsString();

		return Optional.of(new KratosIdentity(id, name));

	}

	@Override
	public Class<?> getIdentityClass() {
		return KratosIdentity.class;
	}

}
