package bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;

import static org.junit.jupiter.api.Assertions.*;


public class FindOwnerSteps {

	@Autowired
	PetService petService;

	@Autowired
	OwnerRepository ownerRepository;

	private Owner owner;
	private Integer ownerID;
	private Integer differentID;
	private Owner foundOwnerByUsingFindOwner;

	@Given("There is one Owner with a certain ID")
	public void thereIsOneOwner() {
		this.owner = new Owner();
		this.owner.setFirstName("Jake");
		this.owner.setLastName("Robertson");
		this.owner.setAddress("321 Elm Street");
		this.owner.setCity("Chicago");
		this.owner.setTelephone("1234567");
		this.ownerRepository.save(this.owner);

		this.ownerID = this.owner.getId();
	}

	@When("Searched for this Owner by his or her ID")
	public void findOwnerIsExecuted() {
		this.foundOwnerByUsingFindOwner = this.petService.findOwner(this.ownerID);
	}

	@Then("The Owner with the same ID is found successfully")
	public void ownerIsFoundSuccessfully() {
		assertEquals(this.foundOwnerByUsingFindOwner.getId(), this.ownerID);
	}

	@When("Searched for this Owner by a different ID from his or her ID")
	public void findOwnerIsExecutedWithDifferentID() {
		this.differentID = this.ownerID - 1;
		this.foundOwnerByUsingFindOwner = this.petService.findOwner(this.differentID);
	}

	@Then("The Owner found is not the one we searched for")
	public void ownerIsNotFound() {
		assertNotEquals(this.foundOwnerByUsingFindOwner.getId(), this.ownerID);
	}
}
