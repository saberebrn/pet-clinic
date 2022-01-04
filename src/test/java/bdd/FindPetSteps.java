package bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;

import static org.junit.jupiter.api.Assertions.*;


public class FindPetSteps {

	@Autowired
	PetService petService;

	@Autowired
	PetRepository petRepository;

	@Autowired
	OwnerRepository ownerRepository;

	@Autowired
	PetTypeRepository petTypeRepository;


	private Pet pet, foundPetByUsingFindPet;
	private Owner owner;
	private PetType petType;
	private Integer petID, differentID;

	@Before("@FindPet")
	public void setupOwnerAndPetType() {
		this.owner = new Owner();
		this.owner.setFirstName("Jake");
		this.owner.setLastName("Robertson");
		this.owner.setAddress("321 Elm Street");
		this.owner.setCity("Chicago");
		this.owner.setTelephone("1234567");
		this.ownerRepository.save(this.owner);

		this.petType = new PetType();
		this.petType.setName("Dog");
		this.petTypeRepository.save(this.petType);
	}


	@Given("There exists a Pet named {string}")
	public void thereIsOneNamedPet(String petName) {
		this.pet = new Pet();
		this.pet.setName(petName);
		this.pet.setType(this.petType);
		this.owner.addPet(this.pet);
		this.petRepository.save(this.pet);

		this.petID = this.pet.getId();
	}

	@When("Searched for the Pet by its ID")
	public void findPetIsExecuted() {
		this.foundPetByUsingFindPet = this.petService.findPet(this.petID);
	}

	@Then("The Pet named {string} with the same ID is found successfully")
	public void petIsFoundSuccessfully(String petName) {
		assertEquals(this.foundPetByUsingFindPet.getName(), petName);
		assertEquals(this.foundPetByUsingFindPet.getId(), this.petID);
	}

	@Then("The Pet returned is not null")
	public void petNotNull() {
		assertNotNull(this.foundPetByUsingFindPet);
	}

	@When("Searched for the Pet by a different ID from its ID")
	public void findPetIsExecutedWithDifferentID() {
		this.differentID = this.petID - 5;
		this.foundPetByUsingFindPet = this.petService.findPet(this.differentID);
	}

	@Then("The Pet found does not have the same ID")
	public void petIsNotFoundWithTheSameID() {
		assertNotEquals(this.foundPetByUsingFindPet.getId(), this.petID);
	}

	@Then("The Pet found is not named {string}")
	public void petIsNotFoundWithTheSameName(String petName) {
//		System.out.println("Function Name: " + this.foundPetByUsingFindPet.getName());
//		System.out.println("Name: " + petName);
//		System.out.println("Function ID: " + this.foundPetByUsingFindPet.getId());
//		System.out.println("ID: " + this.petID);

		assertNotEquals(this.foundPetByUsingFindPet.getName(), petName);
	}
}
