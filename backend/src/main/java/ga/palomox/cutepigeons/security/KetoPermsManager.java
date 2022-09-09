package ga.palomox.cutepigeons.security;

import java.util.UUID;

import ga.palomox.lightrest.rest.permissions.PermissionsManager;
import sh.ory.ApiClient;
import sh.ory.ApiException;
import sh.ory.api.ReadApi;
import sh.ory.api.WriteApi;
import sh.ory.model.RelationQuery;

public class KetoPermsManager implements PermissionsManager<UUID> {
	
	private ReadApi readApi;
	private WriteApi writeApi;
	public KetoPermsManager(String basePath, String apiToken) {
		ApiClient apiClient = new ApiClient().setBasePath(basePath);
		apiClient.setBearerToken(apiToken);
		this.readApi = new ReadApi(apiClient);
		this.writeApi = new WriteApi(apiClient);
	}
	public void allow(UUID userId, String relationship, String namespace, String object) throws ApiException {
		writeApi.createRelationTuple(new RelationQuery()._object(object).namespace(namespace).subjectId(userId.toString()).relation(relationship));
	}
	
	public WriteApi getWriteApi() {
		return this.writeApi;
	}
	
	@Override
	public boolean isAllowed(UUID userId, String relationship, String namespace, String object) {
		try {
			boolean check = this.readApi.getCheck(namespace, object, relationship, userId.toString(), null, null, null, null).getAllowed();

			return this.readApi.getCheck(namespace, object, relationship, userId.toString(), null, null, null, null).getAllowed();
		} catch (ApiException e) {
			e.printStackTrace();
			return false;
		}
	}

}
