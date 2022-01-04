package bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;

import static org.junit.jupiter.api.Assertions.*;


public class NewPetSteps {

	@Autowired
	PetService petService;

	@Autowired
	OwnerRepository ownerRepository;

	private Owner owner;
	private Owner differentOwner;
	private Integer ownerID;
	private Pet pet;

	@Given("There exists one Owner with a certain ID")
	public void thereIsOneOwnerWithCertainID() {
		this.owner = new Owner();
		this.owner.setFirstName("Jake");
		this.owner.setLastName("Johnson");
		this.owner.setAddress("321 Elm Street");
		this.owner.setCity("Chicago");
		this.owner.setTelephone("1234567");
		this.ownerRepository.save(this.owner);

		this.ownerID = this.owner.getId();
	}

	@When("new Pet is added to the Owner")
	public void newPetIsExecuted() {
		this.pet = this.petService.newPet(this.owner);
	}

	@Then("The Owner will have a new Pet")
	public void ownerWillHaveNewPet() {
		assertEquals(this.ownerID, this.pet.getOwner().getId());
	}

	@Then("Returned Pet will not be null")
	public void petNotNull() {
		assertNotNull(this.pet);
	}

	@Given("an Owner exists with first name {string} last name {string}")
	public void thereIsOwnerWithCertainName(String firstName, String lastName) {
		this.differentOwner = new Owner();
		this.differentOwner.setFirstName(firstName);
		this.differentOwner.setLastName(lastName);
		this.differentOwner.setAddress("123 Sunset Street");
		this.differentOwner.setCity("LA");
		this.differentOwner.setTelephone("2345678");
		this.ownerRepository.save(this.differentOwner);
	}

	@When("new Pet is added to the very first Owner")
	public void newPetAddedToFirstOwnerDefined() {
		this.pet = this.petService.newPet(this.owner);
	}

	@Then("The different Owner will not have the new Pet")
	public void differentOwnerWillNotHaveTheNewPet() {
		assertNotEquals(this.pet.getOwner().getId(), this.differentOwner.getId());
	}
}
