package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = PetController.class,
	includeFilters = {
		@ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = PetService.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = PetTimedCache.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = LoggerConfig.class, type = FilterType.ASSIGNABLE_TYPE),
	}
)

class PetControllerTests {
	@Autowired
	private MockMvc mockMVC;
	@MockBean
	private PetRepository petRepository;
	@MockBean
	private OwnerRepository ownerRepository;

	private static int ownerId = 10;
	private static int petTypeId = 15;
	private static int petId = 20;
	private Owner owner = new Owner();
	private PetType petType = new PetType();
	private Pet pet = new Pet();

	@BeforeEach
	void setupTest(){
		this.owner.setId(this.ownerId);
		this.owner.setFirstName("Fatemeh");

		this.petType.setId(this.petTypeId);
		this.petType.setName("Parrot");

		this.pet.setId(this.petId);
		this.pet.setName("Monkey");
		this.pet.setBirthDate(LocalDate.now());

		List<PetType> petTypeList = new ArrayList<>();
		petTypeList.add(this.petType);

		when(this.ownerRepository.findById(this.ownerId)).thenReturn(this.owner);
		when(this.petRepository.findPetTypes()).thenReturn(petTypeList);
		when(this.petRepository.findById(this.petId)).thenReturn(this.pet);
	}

	@Test
	public void testInitialCreationFrom() throws Exception {
		String address = "/owners/" + this.ownerId + "/pets/new";
		String attributeName = "pet", viewName = "pets/createOrUpdatePetForm", contentType = "text/html;charset=UTF-8";
		this.mockMVC.perform
					(
						get(address)
					)
			   		.andExpect(status().isOk())
			   		.andExpect(model().attributeExists(attributeName))
			   		.andExpect(view().name(viewName))
			   		.andExpect(content().contentType(contentType));
	}

	@Test
	public void testProcessCreationForm() throws Exception {
		String address = "/owners/" + this.ownerId + "/pets/new";
		String redirectAddress = "redirect:/owners/{ownerId}";
		String name = "Fatemeh" , type = "Parrot";
		this.mockMVC.perform
					(
						post(address)
						.param("name", name)
						.param("type", type)
						.param("birthDate", LocalDate.now().toString())
					)
					.andExpect(status().is3xxRedirection())
					.andExpect(view().name(redirectAddress));
	}

	@Test
	public void testProcessCreationIncompleteForm() throws Exception {
		String address = "/owners/" + this.ownerId + "/pets/new";
		String name = "Fatemeh", viewName = "pets/createOrUpdatePetForm";
		this.mockMVC.perform
					(
						post(address)
						.param("name", name)
						.param("birthDate", LocalDate.now().toString())
					)
					.andExpect(status().isOk())
					.andExpect(view().name(viewName))
					.andExpect(model().hasErrors());
	}

	@Test
	public void testProcessCreationFormWrongParam() throws Exception {
		String address = "/owners/" + this.ownerId + "/pets/new";
		String name = "Fatemeh", viewName = "pets/createOrUpdatePetForm", testText = "TEST";
		this.mockMVC.perform
					(
						post(address)
						.param("name", name)
						.param("birthDate", LocalDate.now().toString())
						.param("test" , testText)
					)
					.andExpect(status().isOk())
					.andExpect(view().name(viewName))
					.andExpect(model().hasErrors());
	}

	@Test
	public void testInitialUpdateForm() throws Exception {
		String address = "/owners/" + this.ownerId + "/pets/" + this.petId + "/edit";
		String viewName = "pets/createOrUpdatePetForm", attributeName = "pet", contentType = "text/html;charset=UTF-8";
		this.mockMVC.perform
					(
						get(address)
					)
					.andExpect(status().isOk())
					.andExpect(view().name(viewName))
					.andExpect(model().attributeExists(attributeName))
					.andExpect(content().contentType(contentType));
	}

	@Test
	public void testProcessUpdateForm() throws Exception {
		String address = "/owners/" + this.ownerId + "/pets/" + this.petId + "/edit";
		String redirectAddress = "redirect:/owners/{ownerId}";
		String name = "Fatemeh" , type = "Parrot";
		this.mockMVC.perform
					(
						post(address)
						.param("name", name)
						.param("type", type)
						.param("birthDate", LocalDate.now().toString())
					)
					.andExpect(status().is3xxRedirection())
					.andExpect(view().name(redirectAddress));
	}

	@Test
	public void testProcessUpdateIncompleteForm() throws Exception {
		String address = "/owners/" + this.ownerId + "/pets/" + this.petId + "/edit";
		String name = "Fatemeh", viewName = "pets/createOrUpdatePetForm";
		this.mockMVC.perform
					(
						post(address)
						.param("name", name)
					)
					.andExpect(status().isOk())
					.andExpect(view().name(viewName))
					.andExpect(model().hasErrors());
	}

	@Test
	public void testProcessUpdateFormWrongParam() throws Exception {
		String address = "/owners/" + this.ownerId + "/pets/" + this.petId + "/edit";
		String name = "Fatemeh", viewName = "pets/createOrUpdatePetForm", testText = "TEST";
		this.mockMVC.perform
					(
						post(address)
						.param("name", name)
						.param("birthDate", LocalDate.now().toString())
						.param("test", testText)
					)
					.andExpect(status().isOk())
					.andExpect(view().name(viewName))
					.andExpect(model().hasErrors());

	}

	@AfterEach
	void teardownTest(){
		this.owner = null;
		this.petType = null;
		this.pet = null;
	}
}
