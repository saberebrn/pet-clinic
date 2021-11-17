package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.samples.petclinic.model.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimplePriceCalculatorTest {
	List<Pet> pets;
	SimplePriceCalculator simplePriceCalculator = new SimplePriceCalculator();

	@Before
	public void setup() {
		PetType rarePetType = mock(PetType.class);
		PetType notRarePetType = mock(PetType.class);
		Pet rarePet = mock(Pet.class);
		Pet notRarePet = mock(Pet.class);
		when(rarePetType.getRare()).thenReturn(true);
		when(notRarePetType.getRare()).thenReturn(false);
		when(rarePet.getType()).thenReturn(rarePetType);
		when(notRarePet.getType()).thenReturn(notRarePetType);
		pets = Arrays.asList(rarePet, notRarePet);
	}

	@Test
	public void rareNonRareBranchTest() {
		assertEquals(
			130,
			simplePriceCalculator.calcPrice(
				pets,
				20,
				50,
				UserType.SILVER
			),
			3
		);
	}

	@Test
	public void newUserTypeBranchTest() {
		assertEquals(
			0.95 * 122,
			simplePriceCalculator.calcPrice(
				pets,
				100,
				10,
				UserType.NEW
			),
			3
		);
	}
}
