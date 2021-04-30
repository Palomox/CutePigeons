package ga.palomox.cutepigeons;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ga.palomox.cutepigeons.model.Pigeon;
import ga.palomox.cutepigeons.service.IPigeonService;

@SpringBootTest
class CutepigeonsBackendApplicationTests {
	Pigeon p = new Pigeon(1, "test");
	@Autowired
	IPigeonService pigeonService;
	@Test
	void contextLoads() {
	}
	@Test
	void testAddingAndGettingPigeon() {
		Pigeon returned = pigeonService.addPigeon(p);
		assertTrue(returned.equals(p));
		assertTrue(pigeonService.getPigeonById(1).get().equals(p));
		assertTrue(pigeonService.getPigeons().size()==1);
		assertTrue(pigeonService.getPigeons().get(0).equals(p));
	}

}
