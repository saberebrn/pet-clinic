package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.utility.SimpleDI;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PetManagerTest {
	/*
		1. Model
		2. Verification Type
		3. Test Approach
	*/

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


	/*
		1. Mock , Stub
		2. State Verification
		3. Mockisty
	*/
	@Test
	public void testFindOwnerExists(){
		Integer testId = 5;
		Owner mockOwner = mock(Owner.class);

		when(mockOwner.getId()).thenReturn(testId);
		System.out.println("mockOwner ID is: " + mockOwner.getId());

		when(this.ownerRepository.findById(testId)).thenReturn(mockOwner);

		assertEquals(this.petManager.findOwner(testId) , mockOwner);
	}

	/*
		1. Mock , stub
		2. Behavior Verification
		3. Mockisty
	*/
	@Test
	public void testFindOwnerExistsLogger(){
		Integer testId = 7;
		Owner mockOwner = mock(Owner.class);

		when(mockOwner.getId()).thenReturn(testId);
		System.out.println("mockOwner ID is: " + mockOwner.getId());

		when(this.ownerRepository.findById(testId)).thenReturn(mockOwner);
		this.petManager.findOwner(testId);

		verify(this.logger).info("find owner {}", testId);
	}

	/*
		1. Mock , Stub
		2. State Verification
		3. Mockisty
	*/
	@Test
	public void testFindOwnerNotExists(){
		Integer testId = 5; Integer wrongId = 15;
		Owner mockOwner = mock(Owner.class);

		when(mockOwner.getId()).thenReturn(testId);
		System.out.println("mockOwner ID is: " + mockOwner.getId());

		when(this.ownerRepository.findById(testId)).thenReturn(mockOwner);

		assertNotEquals(this.petManager.findOwner(wrongId) , mockOwner);
	}

	/*
		1. Spy
		2. Behavior Verification
		3. Mockisty
	*/
	@Test
	public void testNewPetAdded(){
		int numTimes = 0;
		Owner spyOwner = spy(Owner.class);
		this.petManager.newPet(spyOwner);

		verify(spyOwner).addPet(any(Pet.class));
		verify(spyOwner , times(numTimes)).removePet(any(Pet.class));
	}

	/*
		1. Spy
		2. Behavior Verification
		3. Mockisty
	*/
	@Test
	public void testNewPetLogger(){
		Owner spyOwner = spy(Owner.class);
		Integer ownerId = spyOwner.getId();

		this.petManager.newPet(spyOwner);

		verify(this.logger).info("add pet for owner {}", ownerId);
	}

	/*
		1. Mock , Stub
		2. State Verification
		3. Mockisty
	*/
	@Test
	public void testFindPetExists(){
		Integer testKey = 5;
		Pet mockPet = mock(Pet.class);

		when(mockPet.getId()).thenReturn(testKey);
		System.out.println("mockPet ID is: " + mockPet.getId());

		when(this.petTimedCache.get(testKey)).thenReturn(mockPet);

		assertEquals(this.petManager.findPet(testKey) , mockPet);
	}

	/*
		1. Mock , Stub
		2. Behavior Verification
		3. Mockisty
	*/
	@Test
	public void testFindPetLogger(){
		Integer testKey = 5;
		Pet mockPet = mock(Pet.class);

		when(mockPet.getId()).thenReturn(testKey);
		System.out.println("mockPet ID is: " + mockPet.getId());

		when(this.petTimedCache.get(testKey)).thenReturn(mockPet);
		this.petManager.findPet(testKey);

		verify(this.logger).info("find pet by id {}" , testKey);
	}

	/*
		1. Mock , Stub
		2. State Verification
		3. Mockisty
	*/
	@Test
	public void testFindPetNotExists(){
		Integer testKey = 5; Integer wrongKey = 10;
		Pet mockPet = mock(Pet.class);

		when(mockPet.getId()).thenReturn(testKey);
		System.out.println("mockPet ID is: " + mockPet.getId());

		when(this.petTimedCache.get(testKey)).thenReturn(mockPet);

		assertNotEquals(this.petManager.findPet(wrongKey) , mockPet);
	}

	/*
		1. Spy , Mock
		2. Behavior Verification
		3. Mockisty
	*/
	@Test
	public void testSavePet(){
		Pet mockPet = mock(Pet.class);
		Owner spyOwner = spy(Owner.class);

		this.petManager.savePet(mockPet , spyOwner);

		verify(this.petTimedCache).save(mockPet);
		verify(spyOwner).addPet(mockPet);
	}

	/*
		1. Spy , Mock , Stub
		2. Behavior Verification
		3. Mockisty
	*/
	@Test
	public void testSavePetLogger(){
		Integer testId = 5;
		Pet mockPet = mock(Pet.class);
		Owner spyOwner = spy(Owner.class);

		when(mockPet.getId()).thenReturn(testId);
		System.out.println("mockPet ID is: " + mockPet.getId());

		this.petManager.savePet(mockPet , spyOwner);

		verify(this.logger).info("save pet {}", mockPet.getId());
	}

	/*
		1. Mock , Stub
		2. State Verification
		3. Mockisty
	*/
	@Test
	public void testGetOwnerPetsExists(){
		int ownerId = 3;
		Owner mockOwner = mock(Owner.class);
		when(mockOwner.getId()).thenReturn(ownerId);

		int numPets = 5;
		List<Pet> mockPets = new ArrayList<>();
		for(int i = 0 ; i < numPets ; i++){
			mockPets.add(mock(Pet.class));
		}

		when(mockOwner.getPets()).thenReturn(mockPets);
		when(this.ownerRepository.findById(ownerId)).thenReturn(mockOwner);

		assertEquals(this.petManager.getOwnerPets(mockOwner.getId()) , mockPets);
		assertEquals(this.petManager.getOwnerPets(mockOwner.getId()).size() , numPets);
	}

	/*
		1. Mock , Stub
		2. Behavior Verification
		3. Mockisty
	*/
	@Test
	public void testGetOwnerPetsExistsLogger(){
		int ownerId = 3;
		Owner mockOwner = mock(Owner.class);
		when(mockOwner.getId()).thenReturn(ownerId);

		int numPets = 5;
		List<Pet> mockPets = new ArrayList<>();
		for(int i = 0 ; i < numPets ; i++){
			mockPets.add(mock(Pet.class));
		}

		when(mockOwner.getPets()).thenReturn(mockPets);
		when(this.ownerRepository.findById(ownerId)).thenReturn(mockOwner);

		this.petManager.getOwnerPets(mockOwner.getId());

		verify(this.logger).info("finding the owner's pets by id {}", mockOwner.getId());
	}

	/*
		1. Mock , Stub
		2. State Verification
		3. Mockisty
	*/
	@Test
	public void testGetOwnerPetsNotExists(){
		int ownerId = 3; int wrongId = 7;
		Owner mockOwner = mock(Owner.class);
		when(mockOwner.getId()).thenReturn(ownerId);

		int numPets = 5;
		List<Pet> mockPets = new ArrayList<>();
		for(int i = 0 ; i < numPets ; i++){
			mockPets.add(mock(Pet.class));
		}

		when(mockOwner.getPets()).thenReturn(mockPets);
		when(this.ownerRepository.findById(ownerId)).thenReturn(mockOwner);

		assertThrows(NullPointerException.class , () -> {
			this.petManager.getOwnerPets(wrongId);
		});
	}

	/*
		1. Mock , Stub
		2. State Verification
		3. Mockisty
	*/
	@Test
	public void testGetOwnerPetTypesExists(){
		int ownerId = 3;
		Owner mockOwner = mock(Owner.class);
		when(mockOwner.getId()).thenReturn(ownerId);

		int numPets = 5;
		List<Pet> mockPets = new ArrayList<>();
		Set<PetType> mockPetTypes = new HashSet<>();
		for(int i = 0 ; i < numPets ; i++){
			Pet mockPet = mock(Pet.class);
			PetType mockPetType = mock(PetType.class);
			when(mockPet.getType()).thenReturn(mockPetType);
			mockPets.add(mockPet);
			mockPetTypes.add(mockPetType);
		}

		when(mockOwner.getPets()).thenReturn(mockPets);
		when(this.ownerRepository.findById(ownerId)).thenReturn(mockOwner);

		assertEquals(this.petManager.getOwnerPetTypes(mockOwner.getId()) , mockPetTypes);
		assertEquals(this.petManager.getOwnerPetTypes(mockOwner.getId()).size() , numPets);
	}

	/*
		1. Mock , Stub
		2. State Verification
		3. Mockisty
	*/
	@Test
	public void testGetOwnerPetTypesNotExists(){
		int ownerId = 3; int wrongId = 7;
		Owner mockOwner = mock(Owner.class);
		when(mockOwner.getId()).thenReturn(ownerId);

		int numPets = 5;
		List<Pet> mockPets = new ArrayList<>();
		Set<PetType> mockPetTypes = new HashSet<>();
		for(int i = 0 ; i < numPets ; i++){
			Pet mockPet = mock(Pet.class);
			PetType mockPetType = mock(PetType.class);
			when(mockPet.getType()).thenReturn(mockPetType);
			mockPets.add(mockPet);
			mockPetTypes.add(mockPetType);
		}

		when(mockOwner.getPets()).thenReturn(mockPets);
		when(this.ownerRepository.findById(ownerId)).thenReturn(mockOwner);

		assertThrows(NullPointerException.class , () -> {
			this.petManager.getOwnerPetTypes(wrongId);
		});
	}

	/*
		1. Mock , Stub
		2. Behavior Verification
		3. Mockisty
	*/
	@Test
	public void testGetOwnerPetTypesExistsLogger(){
		int ownerId = 3;
		Owner mockOwner = mock(Owner.class);
		when(mockOwner.getId()).thenReturn(ownerId);

		int numPets = 5;
		List<Pet> mockPets = new ArrayList<>();
		Set<PetType> mockPetTypes = new HashSet<>();
		for(int i = 0 ; i < numPets ; i++){
			Pet mockPet = mock(Pet.class);
			PetType mockPetType = mock(PetType.class);
			when(mockPet.getType()).thenReturn(mockPetType);
			mockPets.add(mockPet);
			mockPetTypes.add(mockPetType);
		}

		when(mockOwner.getPets()).thenReturn(mockPets);
		when(this.ownerRepository.findById(ownerId)).thenReturn(mockOwner);

		this.petManager.getOwnerPetTypes(mockOwner.getId());

		verify(this.logger).info("finding the owner's petTypes by id {}", ownerId);
	}

	/*
		1. Mock , Stub
		2. State Verification
		3. Mockisty
	*/
	@Test
	public void testGetVisitsBetween(){
		int year = 2021;
		int monthStart = 10; int monthEnd = 10;
		int dayStart = 15; int dayEnd = 25;

		LocalDate dateStart = LocalDate.of(year , monthStart , dayStart);
		LocalDate dateEnd = LocalDate.of(year , monthEnd , dayEnd);

		int petId = 7;
		Pet mockPet = mock(Pet.class);
		when(mockPet.getId()).thenReturn(petId);

		List<Visit> mockVisits = new ArrayList<>();
		int numVisits = 5;
		for(int i = 0 ; i < numVisits ; i++){
			mockVisits.add(mock(Visit.class));
		}

		when(mockPet.getVisitsBetween(dateStart , dateEnd)).thenReturn(mockVisits);
		when(this.petTimedCache.get(petId)).thenReturn(mockPet);

		assertEquals(this.petManager.getVisitsBetween(mockPet.getId() , dateStart , dateEnd) , mockVisits);
		assertEquals(this.petManager.getVisitsBetween(mockPet.getId() , dateStart , dateEnd).size() , numVisits);
	}

	/*
		1. Mock , Stub
		2. Behavior Verification
		3. Mockisty
	*/
	@Test
	public void testGetVisitsBetweenLogger(){
		int year = 2021;
		int monthStart = 10; int monthEnd = 10;
		int dayStart = 15; int dayEnd = 25;

		LocalDate dateStart = LocalDate.of(year , monthStart , dayStart);
		LocalDate dateEnd = LocalDate.of(year , monthEnd , dayEnd);

		int petId = 7;
		Pet mockPet = mock(Pet.class);
		when(mockPet.getId()).thenReturn(petId);

		List<Visit> mockVisits = new ArrayList<>();
		int numVisits = 5;
		for(int i = 0 ; i < numVisits ; i++){
			mockVisits.add(mock(Visit.class));
		}

		when(mockPet.getVisitsBetween(dateStart , dateEnd)).thenReturn(mockVisits);
		when(this.petTimedCache.get(petId)).thenReturn(mockPet);

		this.petManager.getVisitsBetween(mockPet.getId() , dateStart , dateEnd);

		verify(this.logger).info("get visits for pet {} from {} since {}", petId, dateStart, dateEnd);
	}
}
