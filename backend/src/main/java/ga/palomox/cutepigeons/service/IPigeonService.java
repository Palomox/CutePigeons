package ga.palomox.cutepigeons.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ga.palomox.cutepigeons.model.Pigeon;

@Service
public interface IPigeonService {
	Optional<Pigeon> getPigeonById(int id);
	List<Pigeon> getPigeons();
	Pigeon addPigeon(Pigeon pigeon);
	List<Pigeon> addPigeons(Pigeon...pigeons);
}
