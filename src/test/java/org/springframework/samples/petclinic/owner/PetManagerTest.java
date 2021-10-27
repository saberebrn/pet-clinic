package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.utility.SimpleDI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PetManagerTest {
	private PetManager petManager;

	@Mock
	private PetTimedCache petTimedCache;

	@Mock
	private Logger logger;

	@Mock
	private OwnerRepository ownerRepository;

	@BeforeEach
	private void petManagerInstance(){
		this.petManager = new PetManager(petTimedCache , ownerRepository , logger);
	}


	@Test
	public void testFindOwnerExists(){
		Integer testId = 5;
		Owner mockOwner = mock(Owner.class);
		mockOwner.setId(testId);
		when(this.ownerRepository.findById(testId)).thenReturn(mockOwner);
		assertEquals(this.petManager.findOwner(testId) , mockOwner);
	}

	@Test
	public void testFindOwnerExistsLogger(){
		Integer testId = 7;
		Owner mockOwner = mock(Owner.class);
		mockOwner.setId(testId);
		when(this.ownerRepository.findById(testId)).thenReturn(mockOwner);
		this.petManager.findOwner(testId);
		verify(this.logger).info("find owner {}", testId);
	}

	@Test
	public void testFindOwnerNotExists(){
		Integer testId = 5; Integer wrongId = 15;
		Owner mockOwner = mock(Owner.class);
		mockOwner.setId(testId);
		when(this.ownerRepository.findById(testId)).thenReturn(mockOwner);
		assertNotEquals(this.petManager.findOwner(wrongId) , mockOwner);
	}

	@Test
	public void testNewPetAdded(){
		Owner spyOwner = spy(Owner.class);
		this.petManager.newPet(spyOwner);
		verify(spyOwner).addPet(any(Pet.class));
	}

	@Test
	public void testNewPetLogger(){
		Owner spyOwner = spy(Owner.class);
		Integer ownerId = spyOwner.getId();
		this.petManager.newPet(spyOwner);
		verify(this.logger).info("add pet for owner {}", ownerId);
	}

	
}
