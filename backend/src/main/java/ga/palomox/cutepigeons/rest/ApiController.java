package ga.palomox.cutepigeons.rest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ga.palomox.cutepigeons.domain.Pigeon;
import ga.palomox.cutepigeons.domain.User;
import ga.palomox.cutepigeons.domain.query.QPigeon;
import ga.palomox.cutepigeons.domain.query.QUser;
import ga.palomox.cutepigeons.model.Post;
import ga.palomox.cutepigeons.security.KetoPermsManager;
import ga.palomox.cutepigeons.security.KratosIdManager;
import ga.palomox.cutepigeons.security.KratosIdentity;
import ga.palomox.cutepigeons.service.IPostService;
import ga.palomox.lightrest.rest.annotations.Authenticated;
import ga.palomox.lightrest.rest.annotations.Mapping;
import ga.palomox.lightrest.rest.annotations.PathVariable;
import ga.palomox.lightrest.rest.annotations.Relationship;
import ga.palomox.lightrest.rest.annotations.RestController;
import ga.palomox.lightrest.rest.annotations.Weigh;
import ga.palomox.lightrest.rest.model.ResponseEntity;
import sh.ory.ApiException;
import sh.ory.model.SelfServiceLoginFlow;
import sh.ory.model.SubmitSelfServiceLoginFlowBody;
import sh.ory.model.SuccessfulSelfServiceLoginWithoutBrowser;

@RestController
public class ApiController {
	private IPostService pigeonService;
	private KratosIdManager identityManager; 

	public ApiController(IPostService pigeonService, KratosIdManager identityManager) {
		this.pigeonService = pigeonService;
		this.identityManager = identityManager;
	}

	@Mapping(path = "api/v2/posts/{id}/image", protocol = "GET")
	public ResponseEntity<?> getImage(@PathVariable(name = "id") int id) {
		Optional<Post> post = pigeonService.getPostById(id);
		if (post.isEmpty()) {
			// Error
			return ResponseEntity
					.badRequest(String.format("{\"status\": \"error\", \"error\": \"Pigeon %s doesn't exist!\"}", id));
		}
		String contentType;
		byte[] media;

		try {
			URL url = new URL(post.get().getPigeonUrl());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("HEAD");
			HttpURLConnection.setFollowRedirects(true);
			connection.connect();
			contentType = connection.getContentType();
			media = IOUtils.toByteArray(connection.getInputStream());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.of(400);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.of(400);
		}
		ResponseEntity<byte[]> responseEntity = ResponseEntity.ok(media);
		responseEntity.setContentType(contentType);
		return responseEntity;
	}

	@Mapping(path = "api/v2/posts", protocol = "GET")
	public ResponseEntity<Map<String, List<Post>>> getPigeons() {
		HashMap<String, List<Post>> map = new HashMap<>();
		map.put("posts", pigeonService.getPosts());
		ResponseEntity<Map<String, List<Post>>> response = ResponseEntity.of(200);
		response.body(map);
		return response;
	}

	@Mapping(path = "api/v2/posts/{id}", protocol = "GET")
	public ResponseEntity<Post> getPigeonById(@PathVariable(name = "id") String id) {
		Optional<Post> pigeonOpt = pigeonService.getPostById(Integer.parseInt(id));
		if (pigeonOpt.isEmpty()) {
			return ResponseEntity.of(404);
		}
		ResponseEntity<Post> response = ResponseEntity.of(200);
		response.body(pigeonOpt.get());
		return response;
	}

	@Authenticated
	@Relationship(namespace = "groups", relation = "member", object = "admin", isPathVar = false)
	@Mapping(path = "api/v2/posts/new", protocol = "POST")
	@Weigh(10)
	public ResponseEntity<?> addPigeonAdmin(Request baseRequest,
			KratosIdentity identity) {

		if (baseRequest.getContentLength() == 0) {
			return ResponseEntity.badRequest("{\"error\": \"You have to add a body specifying the pigeon to add\"}");
		}

		String bodyString;
		try {
			bodyString = baseRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException e) {
			return ResponseEntity.of(500);
		}

		Pigeon pigeonToAdd = new Gson().fromJson(bodyString, Pigeon.class);
		if (pigeonToAdd.getUrl() == null || pigeonToAdd.getUrl().equals("")) {
			return ResponseEntity.badRequest("{\"status\": \"Error\", \"error\": \"You have to send a pigeon URL\"}");
		}
		pigeonToAdd.setUser(new QUser().uuid.eq(identity.getId()).findOne());
		try {
			pigeonService.addPost(pigeonToAdd);
		} catch (ApiException e) {
			e.printStackTrace();
			return ResponseEntity.of(500);
		}
		return ResponseEntity.ok("{\"status\": \"Added the post\", \"id\":" + pigeonToAdd.getId() + "}");
	}

	@Authenticated
	@Mapping(path = "api/v2/posts/new", protocol = "POST")
	@Weigh(1)
	public ResponseEntity<String> addPigeon(Request baseRequest, 
			KratosIdentity identity) {

		if (baseRequest.getContentLength() == 0) {
			return ResponseEntity.badRequest("{\"error\": \"You have to add a body specifying the post to adds\"}");
		}

		String bodyString;
		try {
			bodyString = baseRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException e) {
			return ResponseEntity.of(500);
		}

		Pigeon pigeonToAdd = new Gson().fromJson(bodyString, Pigeon.class);
		if (pigeonToAdd.getUrl() == null || pigeonToAdd.getUrl().equals("")) {
			return ResponseEntity.badRequest("{\"status\": \"Error\", \"error\": \"You have to send a pigeon URL\"}");
		}
		pigeonToAdd.setUser(new QUser().uuid.eq(identity.getId()).findOne());
		try {
			pigeonService.addPostToModQueue(pigeonToAdd);
		} catch (ApiException e) {
			e.printStackTrace();
			return ResponseEntity.of(500);
		}
		return ResponseEntity
				.ok("{\"status\": \"Added the post to the moderation queue\", \"id\":" + pigeonToAdd.getId() + "}");

	}

	@Relationship(object = "admin", namespace = "groups", relation = "member", isPathVar = false)
	@Authenticated
	@Mapping(path = "api/v2/posts/{id}", protocol = "DELETE")
	@Weigh(10)
	public ResponseEntity<String> removePigeonAdmin(@PathVariable(name = "id") String idString) {
		int id = Integer.valueOf(String.valueOf(idString));

		Optional<Pigeon> pigeonOpt = pigeonService.getPigeonById(id);

		if (pigeonOpt.isEmpty()) {
			ResponseEntity<String> response = ResponseEntity.of(400);
			return response.body("{\"status\": \"Error\", \"error\": \"Can't find pigeon with id '" + id + "'\"}");
		}
		try {
			pigeonService.removePost(pigeonOpt.get());
		} catch (ApiException e) {
			e.printStackTrace();
			return ResponseEntity.of(500);
		}
		ResponseEntity<String> response = ResponseEntity.of(200);
		return response.body("{\"status\": \"Success\", \"id\":" + id + "}");
	}

	@Relationship(object = "id", namespace = "pigeons", relation = "writer", isPathVar = true)
	@Authenticated
	@Mapping(path = "api/v2/posts/{id}", protocol = "DELETE")
	@Weigh(1)
	public ResponseEntity<String> removePigeon(@PathVariable(name = "id") String idString) {
		int id = Integer.valueOf(String.valueOf(idString));

		Optional<Pigeon> pigeonOpt = pigeonService.getPigeonById(id);

		if (pigeonOpt.isEmpty()) {
			ResponseEntity<String> response = ResponseEntity.of(400);
			return response.body("{\"status\": \"Error\", \"error\": \"Can't find pigeon with id '" + id + "'\"}");
		}
		try {
			pigeonService.removePost(pigeonOpt.get());
		} catch (ApiException e) {
			e.printStackTrace();
			return ResponseEntity.of(500);
		}
		ResponseEntity<String> response = ResponseEntity.of(200);
		return response.body("{\"status\": \"Success\", \"id\":" + id + "}");
	}

	@Mapping(path = "api/v2/user", protocol = "PUT")
	@Authenticated
	public ResponseEntity<String> changeUserSettings(Request request, KratosIdentity identity) {
		/*
		 * { "displayName": "name" }
		 */
		String body;
		try {
			body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.of(500);
		}
		JsonObject jsonBody = new Gson().fromJson(body, JsonObject.class);
		if (!jsonBody.has("displayName")) {
			return ResponseEntity
					.badRequest("{\"error\": \"Your request doesn't contain the displayName you want to set.\"}");
		}
		String displayName = jsonBody.get("displayName").getAsString();
		new User(identity.getId(), displayName).save();
		return ResponseEntity.ok("{}");
	}

	@Mapping(path = "api/v2/modqueue/pigeons", protocol = "GET")
	@Authenticated
	@Relationship(isPathVar = false, namespace = "groups", relation = "member", object = "moderator")
	public ResponseEntity<List<Post>> getModList() {
		ResponseEntity<List<Post>> response = ResponseEntity.of(200);
		response.body(pigeonService.getModQueue());
		return response;
	}

	@Mapping(path = "api/v2/modqueue/pigeons/{id}", protocol = "POST")
	@Authenticated
	@Relationship(isPathVar = false, namespace = "groups", relation = "member", object = "moderator")
	public ResponseEntity<String> doModAction(@PathVariable(name = "id") String id, Request request) {
		/*
		 * { "allow": boolean }
		 */
		String bodyString;
		try {
			bodyString = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.of(500);
		}
		JsonObject body = new Gson().fromJson(bodyString, JsonObject.class);
		if (!body.has("allow")) {
			return ResponseEntity.badRequest("{\"error\": \"Missing allow property in the body\"}");
		}

		boolean allow = body.get("allow").getAsBoolean();

		Optional<Pigeon> pigeonOpt = pigeonService.getPigeonById(Integer.valueOf(id));
		if (pigeonOpt.isEmpty()) {
			return ResponseEntity.badRequest("{\"error\": \"The pigeon you specified doesn't exist\"}");
		}
		Pigeon pigeon = pigeonOpt.get();

		if (allow) {
			pigeon.setAllowed(true);
			pigeon.save();
			return ResponseEntity.ok("{\"message\": \"Pigeon allowed\"}");
		} else {
			pigeon.delete();
			return ResponseEntity.ok("{\"message\": \"Pigeon deleted\"}");

		}

	}
	@Mapping(path = "api/v2/user/token", protocol = "GET")
	public ResponseEntity<String> getToken() {
		SelfServiceLoginFlow login;
		try {
			login = this.identityManager.getApi().initializeSelfServiceLoginFlowWithoutBrowser(false, null, null);
		} catch (ApiException e) {
			return ResponseEntity.badRequest(String.format("{\"error\":\"%s\"}", e.getMessage()));
		}
		return ResponseEntity.ok("{\"flow\": \""+ login.getId() +"\"}");
	}
	
	@Mapping(path = "api/v2/user/token/get", protocol = "GET")
	public ResponseEntity<String> claimToken(Request request){
		String stringBody;
		try {
			stringBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException e) {
			return ResponseEntity.badRequest(String.format("{\"error\":\"%s\"}", e.getMessage()));
		}
		JsonObject body = new Gson().fromJson(stringBody, JsonObject.class);
		
		if(body.has("flow") && body.has("id") && body.has("password")) {
			SubmitSelfServiceLoginFlowBody submittedBody =  new SubmitSelfServiceLoginFlowBody().method("password").identifier(body.get("id").getAsString()).password(body.get("password").getAsString());
			try {
				SuccessfulSelfServiceLoginWithoutBrowser login = this.identityManager.getApi().submitSelfServiceLoginFlow(body.get("flow").getAsString(), submittedBody, null, null);
				String token = login.getSessionToken();
				return ResponseEntity.ok("{\"token\":\""+ token +"\"}");
			} catch (ApiException e) {
				e.printStackTrace();
				return ResponseEntity.of(500);
			}
		} else {
			return ResponseEntity.badRequest("{\"error\": \"You need to specify all the information\"}");
		}
		
	}
	
	@Mapping(path = "api/v2/user/token/refresh", protocol = "GET")
	public ResponseEntity<String> refreshToken(Request request){
		String stringBody;
		try {
			stringBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException e) {
			return ResponseEntity.badRequest(String.format("{\"error\":\"%s\"}", e.getMessage()));
		}
		JsonObject body = new Gson().fromJson(stringBody, JsonObject.class);

		if(body.has("token")) {
			SelfServiceLoginFlow login;
			try {
				login = this.identityManager.getApi().initializeSelfServiceLoginFlowWithoutBrowser(true, null, body.get("token").getAsString());
			} catch (ApiException e) {
				return ResponseEntity.badRequest(String.format("{\"error\":\"%s\"}", e.getMessage()));
			}
			return ResponseEntity.ok("{\"flow\": \""+ login.getId() +"\"}");
		} 
		return ResponseEntity.badRequest("{\"error\":\"You have to include your current token in the payload\"}");
	}


}
