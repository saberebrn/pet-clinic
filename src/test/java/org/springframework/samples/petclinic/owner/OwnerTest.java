package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {
	private Owner owner;

	@BeforeEach
	private void initOwner(){
		this.owner = new Owner();
	}

	@Test
	public void removePetStateVerification(){
		Pet pet = new Pet();
		pet.setName("Jackson");
		this.owner.addPet(pet);
		this.owner.removePet(pet);

		assertNull(this.owner.getPet("Jackson"));
		assertEquals(this.owner.getPets() , new ArrayList<Pet>());
		assertEquals(pet.getOwner() , this.owner);
		assertEquals(this.owner.getPets().size() , 0);
	}

	@Test
	public void removePetBehaviorVerificationIsNew(){
		Pet mockPet = mock(Pet.class);
		when(mockPet.isNew()).thenReturn(true);
		this.owner.addPet(mockPet);
		this.owner.removePet(mockPet);

		verify(mockPet , times(1)).isNew();
		verify(mockPet , times(1)).setOwner(this.owner);
	}

	@Test
	public void addPetStateVerification(){
		Pet pet = new Pet();
		pet.setName("Jackson");
		this.owner.addPet(pet);

		List<Pet> ownerPets = new ArrayList<>();
		ownerPets.add(pet);

		assertNotNull(this.owner.getPet("Jackson"));
		assertEquals(this.owner.getPets() , ownerPets);
		assertEquals(pet.getOwner() , this.owner);
		assertEquals(this.owner.getPets().size() , 1);
	}

	@Test
	public void addPetBehaviorVerificationIsNotNew(){
		Pet mockPet = mock(Pet.class);
		when(mockPet.isNew()).thenReturn(false);

		this.owner.addPet(mockPet);

		verify(mockPet , times(1)).isNew();
		verify(mockPet , times(1)).setOwner(this.owner);
	}


}
