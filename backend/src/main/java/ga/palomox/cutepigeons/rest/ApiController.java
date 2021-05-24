package ga.palomox.cutepigeons.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ga.palomox.cutepigeons.model.Pigeon;
import ga.palomox.cutepigeons.service.IPigeonService;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class ApiController{
	
	@Autowired
	private IPigeonService pigeonService;
	

	@GetMapping("/public/getPigeons")
	public ResponseEntity<?> getPigeons(){
		Map<String, List<Pigeon>> map = new HashMap<>();
		map.put("pigeons", pigeonService.getPigeons());
		return ResponseEntity.ok(map);
	}

	@GetMapping("/public/getPigeonById")
	public ResponseEntity<Pigeon> getPigeonById(@RequestParam int id){
		Optional<Pigeon> pigeonOpt = pigeonService.getPigeonById(id);
		if(pigeonOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(pigeonOpt.get());
	}
	@PostMapping("/admin/addPigeon")
	public ResponseEntity<?> addPigeon(RequestEntity<String> request){
		if(!request.hasBody()) {
			return ResponseEntity.badRequest().body("{\"error\": \"You have to add a body specifying the pigeon to delete\"}");
		}
		JacksonJsonParser parser = new JacksonJsonParser();
		Map<String, Object> body = parser.parseMap(request.getBody());
		Pigeon pigeonToAdd;
		if(body.get("url") == null) {
			return ResponseEntity.badRequest().body("{\"error\": \"You have to send a pigeon URL\"}");
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
			return ResponseEntity.badRequest().body("{\"error\": \"Pigeon already exists\"}");
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
			return ResponseEntity.badRequest().body("{\"error\": \"Can't find pigeon with id '"+id+"'\"}");
		}
		pigeonService.removePigeon(id);
		return ResponseEntity.ok("{\"status\": \"Success\", \"id\":"+id+"}");
	}
	
}
