package ga.palomox.cutepigeons.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ga.palomox.cutepigeons.model.Pigeon;
import ga.palomox.cutepigeons.service.IPigeonService;

@RestController
@RequestMapping("/api/v1")
public class ApiController{
	
	@Autowired
	private IPigeonService pigeonService;
	
	@GetMapping("/getPigeons")
	public ResponseEntity<?> getPigeons(){
		Map<String, List<Pigeon>> map = new HashMap<>();
		map.put("pigeons", pigeonService.getPigeons());
		return ResponseEntity.ok(map);
	}
	@GetMapping("/getPigeonById")
	public ResponseEntity<Pigeon> getPigeonById(@RequestParam int id){
		Optional<Pigeon> pigeonOpt = pigeonService.getPigeonById(id);
		if(pigeonOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(pigeonOpt.get());
	}
	public ResponseEntity<?> addPigeon(@RequestBody Pigeon pigeon){
		if(pigeonService.getPigeonById(pigeon.getId()).isPresent()) {
			return ResponseEntity.badRequest().body("Pigeon already exists");
		}
		pigeonService.addPigeon(pigeon);
		return ResponseEntity.ok().build();
	}
}
