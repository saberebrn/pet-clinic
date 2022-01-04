package bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import static org.junit.jupiter.api.Assertions.*;


public class SavePetSteps {

	@Autowired
	PetService petService;

	@Autowired
	PetRepository petRepository;

	@Autowired
	OwnerRepository ownerRepository;

	@Autowired
	PetTypeRepository petTypeRepository;

	@Autowired
	PetTimedCache petTimedCache;


	private Pet pet, foundPetByUsingFindPet;
	private Owner owner;
	private PetType petType;
	private Integer ownerID, petID, differentID;

	@Before("@SavePet")
	public void setupPetType() {
		this.petType = new PetType();
		this.petType.setName("Dog");
		this.petTypeRepository.save(this.petType);
	}

	@Given("There exists a Pet named {string} and an Owner")
	public void thereIsOneNamedPet(String petName) {
		this.owner = new Owner();
		this.owner.setFirstName("Jake");
		this.owner.setLastName("Robertson");
		this.owner.setAddress("321 Elm Street");
		this.owner.setCity("Chicago");
		this.owner.setTelephone("1234567");
		this.ownerRepository.save(this.owner);

		this.ownerID = this.owner.getId();

		this.pet = new Pet();
		this.pet.setName(petName);
		this.pet.setType(this.petType);

		this.owner.addPet(this.pet);

		this.petRepository.save(this.pet);

		this.petID = this.pet.getId();
	}

	@When("Saving the Pet")
	public void savePetIsExecuted() {
		this.petService.savePet(this.pet, this.owner);
	}

	@Then("The Pet is added to the Owner's pets")
	public void petIsAddedToOwnerSuccessfully() {
		assertEquals(this.ownerID, this.pet.getOwner().getId());
	}

	@Then("The pet is stored in cache")
	public void petIsInCache() {
		assertEquals(this.petTimedCache.get(this.petID).getId(), this.pet.getId());
	}

}
