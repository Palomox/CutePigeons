package ga.palomox.cutepigeons.rest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ga.palomox.cutepigeons.model.Pigeon;
import ga.palomox.cutepigeons.service.IPigeonService;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class ApiController{
	
	@Autowired
	private IPigeonService pigeonService;
	
	@GetMapping(path = "/public/pigeon/{id}/image")
	public ResponseEntity<?> getImage(@PathVariable int id){
		Optional<Pigeon> pigeon = pigeonService.getPigeonById(id);
		if(pigeon.isEmpty()) {
			//Error
			return ResponseEntity.badRequest().body(String.format("{\"status\": \"error\", \"error\": \"Pigeon %s doesn't exist!\"}", id));
		}
		HttpHeaders headers = new HttpHeaders();
	    InputStream in;
	    byte[] media;
		try {
			in = new URL(pigeon.get().getUrl()).openStream();
		    media = IOUtils.toByteArray(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
		String extension = pigeon.get().getUrl().substring(pigeon.get().getUrl().length()-3, pigeon.get().getUrl().length());
		switch(extension) {
		case "png":
			headers.setContentType(MediaType.IMAGE_PNG);
			break;
		case "jpg":
			headers.setContentType(MediaType.IMAGE_JPEG);
			break;
		default:
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			break;
		}
	    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
	    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
	    return responseEntity;
	}
	@GetMapping("/public/pigeons")
	public ResponseEntity<?> getPigeons(){
		Map<String, List<Pigeon>> map = new HashMap<>();
		map.put("pigeons", pigeonService.getPigeons());
		return ResponseEntity.ok(map);
	}

	@GetMapping("/public/pigeon/{id}")
	public ResponseEntity<Pigeon> getPigeonById(@PathVariable int id){
		Optional<Pigeon> pigeonOpt = pigeonService.getPigeonById(id);
		if(pigeonOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(pigeonOpt.get());
	}
	@PostMapping("/admin/addPigeon")
	public ResponseEntity<?> addPigeon(RequestEntity<String> request){
		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		Jwt userPrincipal = (Jwt) user.getPrincipal();
		System.out.println(userPrincipal.getClaimAsStringList("permissions"));
		if(!userPrincipal.getClaimAsStringList("permissions").contains("write:pigeons")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"status\": \"Error\", \"error\": \"Forbidden\"}");
		}
		if(!request.hasBody()) {
			return ResponseEntity.badRequest().body("{\"error\": \"You have to add a body specifying the pigeon to add\"}");
		}
		JacksonJsonParser parser = new JacksonJsonParser();
		Map<String, Object> body = parser.parseMap(request.getBody());
		Pigeon pigeonToAdd;
		if(body.get("url") == null ||"".equals(body.get("url"))) {
			return ResponseEntity.badRequest().body("{\"status\": \"Error\", \"error\": \"You have to send a pigeon URL\"}");
		}
		if(body.get("id") != null) {
			int id = Integer.parseInt(String.valueOf(body.get("id")));
			String pigeonUrl = String.valueOf(body.get("url"));
			pigeonToAdd = new Pigeon(id, pigeonUrl);
		}else {
			int id = (int) pigeonService.getPigeonRepo().count()+1;
			String pigeonUrl = String.valueOf(body.get("url"));
			pigeonToAdd = new Pigeon(id, pigeonUrl);
		}
		if(pigeonService.getPigeonById(pigeonToAdd.getId()).isPresent()) {
			return ResponseEntity.badRequest().body("{\"status\": \"Error\", \"error\": \"Pigeon already exists\"}");
		}
		Pigeon returned = pigeonService.addPigeon(pigeonToAdd);
		return ResponseEntity.ok("{\"status\": \"Success\", \"id\":"+returned.getId()+"}");
	}
	@GetMapping("/authenticated/userDetails")
	public ResponseEntity<?> getUserDetails(){
		//Check userDetails endpoint not working
		System.out.println(SecurityContextHolder.getContext().getAuthentication());
		return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication());
	}
	@DeleteMapping("/admin/deletePigeon")
	public ResponseEntity<?> removePigeon(RequestEntity<String> request){
		if(!request.hasBody()) {
			return ResponseEntity.badRequest().body("{\"error\": \"You have to add a body specifying the pigeon to delete\"}");
		}
		JacksonJsonParser parser = new JacksonJsonParser();
		Map<String, Object> body = parser.parseMap(request.getBody());
		if(body.get("id") == null) {
			return ResponseEntity.badRequest().body("{\"error\": \"You have to send a pigeon id to delete\"}");
		}
		int id = Integer.valueOf(String.valueOf(body.get("id")));
		if(pigeonService.getPigeonById(id).isEmpty()) {
			return ResponseEntity.badRequest().body("{\"status\": \"Error\", \"error\": \"Can't find pigeon with id '"+id+"'\"}");
		}
		pigeonService.removePigeon(id);
		return ResponseEntity.ok("{\"status\": \"Success\", \"id\":"+id+"}");
	}
	
}
