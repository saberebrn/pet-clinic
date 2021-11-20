package org.springframework.samples.petclinic.model.priceCalculators;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerDependentPriceCalculatorTest {
	final PriceCalculator calc = new CustomerDependentPriceCalculator();

	final double basePrice = 10.0;
	final double petPrice = 5.0;

	final int INFANT_YEARS = (int) ReflectionTestUtils.getField(calc, "INFANT_YEARS");
	final double RARE_INFANCY_COEF = (double) ReflectionTestUtils.getField(calc, "RARE_INFANCY_COEF");
	final double COMMON_INFANCY_COEF = (double) ReflectionTestUtils.getField(calc, "COMMON_INFANCY_COEF");
	final double BASE_RARE_COEF = (double) ReflectionTestUtils.getField(calc, "BASE_RARE_COEF");
	final int DISCOUNT_MIN_SCORE = (int) ReflectionTestUtils.getField(calc, "DISCOUNT_MIN_SCORE");

	final Pet rareInfantPet = petMockCreator(INFANT_YEARS, true);
	final Pet rarePet = petMockCreator(INFANT_YEARS + 1, true);
	final Pet infantPet = petMockCreator(INFANT_YEARS, false);
	final Pet normalPet = petMockCreator(INFANT_YEARS + 1, false);

	public static Pet petMockCreator(int age, Boolean isRare){
		Pet pet = mock(Pet.class);
		Date today = new Date();
		when(pet.getBirthDate()).thenReturn(new Date(today.getYear() - age, today.getMonth(), today.getDay()));
		PetType rarity = mock(PetType.class);
		when(rarity.getRare()).thenReturn(isRare);
		when(pet.getType()).thenReturn(rarity);
		return pet;
	}

	public List<Pet> createPetList(Pet pet, boolean discountApply){
		DateTime today = new DateTime();
		int petDiscountScore = 1;
		int discountScore = 0;
		List<Pet> pets = new ArrayList<>();

		if(pet.getType().getRare() &&
			Years.yearsBetween(new DateTime(pet.getBirthDate()), today).getYears() <= INFANT_YEARS) {
			petDiscountScore = 2;
		}

		while(discountScore + petDiscountScore < DISCOUNT_MIN_SCORE) {
			pets.add(pet);
			discountScore += petDiscountScore;
		}

		if(discountApply){
			pets.add(pet);
		}

		return pets;
	}

	@Test
	public void goldUserWithLessThanDiscountMinRequiredScore() {
		List<Pet> pets = createPetList(normalPet, false);
		double price = calc.calcPrice(pets, basePrice, petPrice, UserType.GOLD);
		assertEquals(
			petPrice * pets.size() * UserType.GOLD.discountRate + basePrice,
			price,
			0.0001
		);
	}

	@Test
	public void goldUserWithMoreThanDiscountMinRequiredScore() {
		List<Pet> pets = createPetList(normalPet, true);
		double price = calc.calcPrice(pets, basePrice, petPrice, UserType.GOLD);
		assertEquals(
			(petPrice * pets.size() + basePrice) * UserType.GOLD.discountRate,
			price,
			0.0001
		);
	}

	@Test
	public void UserWithLessThanDiscountMinRequiredScore() {
		List<Pet> pets = createPetList(normalPet, false);
		double price = calc.calcPrice(pets, basePrice, petPrice, UserType.SILVER);
		assertEquals(
			petPrice * pets.size(),
			price,
			0.0001
		);
	}

	@Test
	public void newUserWithMoreThanDiscountMinRequiredScore() {
		List<Pet> pets = createPetList(normalPet, true);
		double price = calc.calcPrice(pets, basePrice, petPrice, UserType.NEW);
		assertEquals(
			petPrice * pets.size() * UserType.NEW.discountRate + basePrice,
			price,
			0.0001
		);
	}

	@Test
	public void silverUserWithMoreThanDiscountMinRequiredScore() {
		List<Pet> pets = createPetList(normalPet, true);
		double price = calc.calcPrice(pets, basePrice, petPrice, UserType.SILVER);
		assertEquals(
			(petPrice * pets.size() + basePrice) *  UserType.SILVER.discountRate,
			price,
			0.0001
		);
	}

	@Test
	public void InfantRatePricingTest() {
		List<Pet> pets = createPetList(rareInfantPet, false);
		double price = calc.calcPrice(pets, basePrice, petPrice, UserType.SILVER);
		assertEquals(
			petPrice * BASE_RARE_COEF * RARE_INFANCY_COEF * pets.size(),
			price,
			0.0001
		);
	}

	@Test
	public void RatePricingTest() {
		List<Pet> pets = createPetList(rarePet, false);
		double price = calc.calcPrice(pets, basePrice, petPrice, UserType.SILVER);
		assertEquals(
			petPrice * BASE_RARE_COEF * pets.size(),
			price,
			0.0001
		);
	}

	@Test
	public void infantPricingTest() {
		List<Pet> pets = createPetList(infantPet, false);
		double price = calc.calcPrice(pets, basePrice, petPrice, UserType.SILVER);
		assertEquals(
			petPrice * COMMON_INFANCY_COEF * pets.size(),
			price,
			0.0001
		);
	}

	@Test
	public void normalPetPricingTest() {
		List<Pet> pets = createPetList(normalPet, false);
		double price = calc.calcPrice(pets, basePrice, petPrice, UserType.SILVER);
		assertEquals(
			petPrice * pets.size(),
			price,
			0.0001
		);
	}
}
