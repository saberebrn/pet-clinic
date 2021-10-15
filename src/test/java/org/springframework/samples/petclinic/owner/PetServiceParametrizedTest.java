package org.springframework.samples.petclinic.owner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(Parameterized.class)
public class PetServiceParametrizedTest {

	public Integer petId;
	public Pet expectedPet;
	public static PetService petService;
	public static HashMap<Integer, Pet> pets = createParams();

	public PetServiceParametrizedTest(Integer petId, Pet expectedPet) {
		PetTimedCache cache = createCacheMock();
		petService = new PetService(cache, null, LoggerFactory.getLogger(PetService.class));
		this.expectedPet = expectedPet;
		this.petId = petId;
	}


	public static HashMap<Integer, Pet> createParams(){
		HashMap<Integer, Pet> pets = new HashMap<>();
		for(int i = 0; i <= 10; i++){
			Pet pet = new Pet();
			pet.setId(i);
			pet.setName("pishul" + i);
			pets.put(i, pet);
		}
		return pets;
	}

	public PetTimedCache createCacheMock(){
		PetTimedCache cache = mock(PetTimedCache.class);
		for(int i = 0; i <= 10; i++ )
			when(cache.get(i)).thenReturn(pets.get(i));
		return cache;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> parameters() {
		List<Object[]> params = new ArrayList<>();
		for (int i = 0; i <= 10; i++) {
			params.add(new Object[]{i, pets.get(i)});
		}
		return params;
	}

	@Test
	public void testGetPet(){
		assertEquals(expectedPet, petService.findPet(petId));
	}
}
