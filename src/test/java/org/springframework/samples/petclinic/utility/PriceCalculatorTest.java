package org.springframework.samples.petclinic.utility;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.owner.Pet;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PriceCalculatorTest {

	private Pet infantPet = mock(Pet.class);
	private Pet grownUpPet = mock(Pet.class);
	private ArrayList<Pet> pets;
	private int yearsBackForGrownUp = 5;
	private double basePricePerPet = 2;
	private double baseCharge = 1;

	@BeforeEach
	private void setup(){
		when(this.infantPet.getBirthDate()).thenReturn(LocalDate.now());
		when(this.grownUpPet.getBirthDate()).thenReturn(LocalDate.now().minusYears(this.yearsBackForGrownUp));
		this.pets = new ArrayList<>();
	}


	@Test
	public void calcPriceInfantsNoVisit(){
		int newBornYear = 0;
		when(this.infantPet.getVisitsUntilAge(newBornYear)).thenReturn(new ArrayList<>());
		for(int i = 0; i < 3; i++){
			this.pets.add(this.infantPet);
		}
		assertEquals(PriceCalculator.calcPrice(this.pets, this.baseCharge, this.basePricePerPet), 10.08);
	}

	@Test
	public void calcPriceGrownUpsNoVisit(){
		when(this.grownUpPet.getVisitsUntilAge(this.yearsBackForGrownUp)).thenReturn(new ArrayList<>());
		for(int i = 0; i < 3; i++){
			this.pets.add(this.grownUpPet);
		}
		assertEquals(PriceCalculator.calcPrice(this.pets, this.baseCharge, this.basePricePerPet), 7.2, 0.00001);
	}

	@Test
	public void calcPriceBoundaryInfantsNoVisit(){
		int boundaryYear = 2;
		when(this.infantPet.getBirthDate()).thenReturn(LocalDate.now().minusYears(boundaryYear));
		when(this.infantPet.getVisitsUntilAge(boundaryYear)).thenReturn(new ArrayList<>());
		this.pets.add(this.infantPet);
		assertEquals(PriceCalculator.calcPrice(this.pets, this.baseCharge, this.basePricePerPet), 3.36);
	}


	@AfterEach
	private void teardown(){
		this.pets = null;
	}
}
