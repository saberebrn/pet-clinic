package org.springframework.samples.petclinic.utility;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PriceCalculatorTest {

	private static Pet infantPet = mock(Pet.class);
	private static Pet grownUpPet = mock(Pet.class);
	private static Visit mockVisit = mock(Visit.class);
	private static int yearsBackForGrownUp = 5;
	private static int yearsBackForOldVisit = 3;
	private static double basePricePerPet = 100;
	private static double baseCharge = 1;
	private ArrayList<Pet> pets;

	@BeforeEach
	private void setup(){
		when(this.infantPet.getBirthDate()).thenReturn(LocalDate.now());
		when(this.grownUpPet.getBirthDate()).thenReturn(LocalDate.now().minusYears(this.yearsBackForGrownUp));
		this.pets = new ArrayList<>();
	}


	@Test
	public void calcPriceInfantsNoVisitTest(){
		int newBornYear = 0;
		when(this.infantPet.getVisitsUntilAge(newBornYear)).thenReturn(new ArrayList<>());
		for(int i = 0; i < 3; i++){
			this.pets.add(this.infantPet);
		}
		assertEquals(PriceCalculator.calcPrice(this.pets, this.baseCharge, this.basePricePerPet), 504);
	}

	@Test
	public void calcPriceGrownUpsNoVisitTest(){
		when(this.grownUpPet.getVisitsUntilAge(this.yearsBackForGrownUp)).thenReturn(new ArrayList<>());
		for(int i = 0; i < 3; i++){
			this.pets.add(this.grownUpPet);
		}
		assertEquals(PriceCalculator.calcPrice(this.pets, this.baseCharge, this.basePricePerPet), 360);
	}

	@Test
	public void calcPriceBoundaryInfantsNoVisitTest(){
		int boundaryYear = 2;
		when(this.infantPet.getBirthDate()).thenReturn(LocalDate.now().minusYears(boundaryYear));
		when(this.infantPet.getVisitsUntilAge(boundaryYear)).thenReturn(new ArrayList<>());
		this.pets.add(this.infantPet);
		assertEquals(PriceCalculator.calcPrice(this.pets, this.baseCharge, this.basePricePerPet), 168);
	}

	@Test
	public void calcPriceInfantsNoVisitWithDiscountTest(){
		int newBornYear = 0;
		when(this.infantPet.getVisitsUntilAge(newBornYear)).thenReturn(new ArrayList<>());
		for(int i = 0; i < 6; i++){
			this.pets.add(this.infantPet);
		}
		assertEquals(PriceCalculator.calcPrice(this.pets, this.baseCharge, this.basePricePerPet), 3195);
	}

	@Test
	public void calcPriceInfantsNoVisitWithDiscountBoundaryTest(){
		int newBornYear = 0;
		when(this.infantPet.getVisitsUntilAge(newBornYear)).thenReturn(new ArrayList<>());
		for(int i = 0; i < 5; i++){
			this.pets.add(this.infantPet);
		}
		assertEquals(PriceCalculator.calcPrice(this.pets, this.baseCharge, this.basePricePerPet), 1513);
	}

	@Test
	public void calcPriceOldVisitTest(){
		when(this.mockVisit.getDate()).thenReturn(LocalDate.now().minusYears(this.yearsBackForOldVisit));

		List<Visit> grownUpPetVisit = new ArrayList<>();
		grownUpPetVisit.add(this.mockVisit);

		when(this.grownUpPet.getVisitsUntilAge(this.yearsBackForGrownUp)).thenReturn(grownUpPetVisit);
		for(int i = 0; i < 10; i++){
			this.pets.add(this.grownUpPet);
		}
		assertEquals(PriceCalculator.calcPrice(this.pets, this.baseCharge, this.basePricePerPet), 12011);
	}

	@Test
	public void calcPriceBoundaryVisitTest(){
		int boundaryDays = 100;
		when(this.mockVisit.getDate()).thenReturn(LocalDate.now().minusDays(boundaryDays));

		List<Visit> grownUpPetVisit = new ArrayList<>();
		grownUpPetVisit.add(this.mockVisit);

		when(this.grownUpPet.getVisitsUntilAge(this.yearsBackForGrownUp)).thenReturn(grownUpPetVisit);
		for(int i = 0; i < 10; i++){
			this.pets.add(this.grownUpPet);
		}
		assertEquals(PriceCalculator.calcPrice(this.pets, this.baseCharge, this.basePricePerPet), 2282);
	}

	@AfterEach
	private void teardown(){
		this.pets = null;
	}
}
