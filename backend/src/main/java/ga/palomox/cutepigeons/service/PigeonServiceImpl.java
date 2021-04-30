package ga.palomox.cutepigeons.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ga.palomox.cutepigeons.model.Pigeon;
import ga.palomox.cutepigeons.repository.IPigeonRepository;

@Service
public class PigeonServiceImpl implements IPigeonService {
	@Autowired
	private IPigeonRepository pigeonRepo;

	public Optional<Pigeon> getPigeonById(int id) {
		return pigeonRepo.findById(id);
	}

	@Override
	public List<Pigeon> getPigeons() {
		return pigeonRepo.findAll();
	}

	@Override
	public Pigeon addPigeon(Pigeon pigeon) {
		return pigeonRepo.saveAndFlush(pigeon);
	}

	@Override
	public List<Pigeon> addPigeons(Pigeon... pigeons) {
		List<Pigeon> pigeonsReturn = pigeonRepo.saveAll(Arrays.asList(pigeons));
		pigeonRepo.flush();
		return pigeonsReturn;
		
	}

}
