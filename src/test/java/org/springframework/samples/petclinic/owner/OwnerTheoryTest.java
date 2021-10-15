package org.springframework.samples.petclinic.owner;

import org.junit.Assume;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import static org.junit.jupiter.api.Assertions.*;


import java.util.Arrays;
import java.util.List;

@RunWith(Theories.class)
public class OwnerTheoryTest {

	@DataPoints
	public static List<String> petNames() {
		return Arrays.asList("pishul", "hapu", null);
	}

	@Theory
	public void testGetPet(String petName) {
		Assume.assumeTrue(petName != null);
		Pet pet = new Pet();
		Owner owner = new Owner();
		pet.setName(petName);
		owner.addPet(pet);
		assertEquals(pet, owner.getPet(petName));
	}
}
