package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class OwnerTest {
	Owner ownerObj;
	Pet pishulWithOwner;
	Pet hapuWithOwner;
	Pet pishul;
	Pet hapu;

	public Pet createPet(String petName){
		Pet pet = new Pet();
		pet.setName(petName);
		return pet;
	}

	public Pet createPet(String petName, Owner owner){
		Pet pet = new Pet();
		pet.setName(petName);
		pet.setOwner(owner);
		return pet;
	}

	public Field getAccessibleFieldFromOwner(String fieldName) throws NoSuchFieldException {
		final Field field = ownerObj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field;
	}

	@BeforeEach
	public void setup(){
		this.ownerObj = new Owner();
		this.pishulWithOwner = createPet("pishul", ownerObj);
		this.hapuWithOwner = createPet("hapu", ownerObj);
		this.pishul = createPet("pishul");
		this.hapu = createPet("hapu");
	}

	@Test
	public void testGetAddress() throws Exception {
		final Field addressField = getAccessibleFieldFromOwner("address");
		addressField.set(ownerObj, "Ferdos blvd");
		final String result = ownerObj.getAddress();
		assertEquals("Ferdos blvd", result);
	}

	@Test
	public void testSetAddress() throws Exception {
		ownerObj.setAddress("Ferdos blvd");
		final Field addressField = getAccessibleFieldFromOwner("address");
		assertEquals("Ferdos blvd", addressField.get(ownerObj));
	}

	@Test
	public void testGetCity() throws Exception {
		final Field cityFiled = getAccessibleFieldFromOwner("city");
		cityFiled.set(ownerObj, "Teh");
		final String result = ownerObj.getCity();
		assertEquals("Teh", result);
	}

	@Test
	public void testSetCity() throws Exception {
		ownerObj.setCity("Teh");
		final Field cityFiled = getAccessibleFieldFromOwner("city");
		assertEquals("Teh", cityFiled.get(ownerObj));
	}

	@Test
	public void testGetTelephone() throws Exception {
		final Field telephoneField = getAccessibleFieldFromOwner("telephone");
		telephoneField.set(ownerObj, "09123456789");
		final String result = ownerObj.getTelephone();
		assertEquals("09123456789", result);
	}

	@Test
	public void testSetTelephone() throws Exception {
		ownerObj.setTelephone("09123456789");
		final Field telephoneField = getAccessibleFieldFromOwner("telephone");
		assertEquals("09123456789", telephoneField.get(ownerObj));
	}

	@Test
	public void testGetPets() throws Exception {
		final Set<Pet> pets = new HashSet<>();
		pets.add(pishul);
		pets.add(hapu);
		final Field petsField = getAccessibleFieldFromOwner("pets");
		petsField.set(ownerObj, pets);
		final List<Pet> result = ownerObj.getPets();
		assertTrue(result.contains(pishul));
		assertTrue(result.contains(hapu));
	}

	@Test
	public void testAddPet() throws Exception {
		final Owner ownerObj = new Owner();
		final Field petsField = getAccessibleFieldFromOwner("pets");
		ownerObj.addPet(pishul);
		assertEquals(pishul.getOwner(), ownerObj, "pishul owner not sets");
		assertTrue(((Set<Pet>)petsField.get(ownerObj)).contains(pishul));
	}

	@Test
	public void testRemovePet() throws Exception {
		final Set<Pet> pets = new HashSet<>();
		final Field petsField = getAccessibleFieldFromOwner("pets");
		pets.add(pishulWithOwner);
		pets.add(hapuWithOwner);
		petsField.set(ownerObj, pets);
		ownerObj.removePet(pishulWithOwner);
		assertFalse(((Set<Pet>)petsField.get(ownerObj)).contains(pishulWithOwner));
		assertTrue(((Set<Pet>)petsField.get(ownerObj)).contains(hapuWithOwner));
	}

	@Test
	public void testGetPet() throws Exception {
		final Set<Pet> pets = new HashSet<>();
		final Field petsField = getAccessibleFieldFromOwner("pets");
		pets.add(pishulWithOwner);
		pets.add(hapuWithOwner);
		petsField.set(ownerObj, pets);
		Pet result = ownerObj.getPet("pishul");
		assertEquals(result, pishulWithOwner);
	}

	@Test
	public void testGetPetNull(){
		Pet result = ownerObj.getPet("pishul");
		assertNull(result);
	}
}
