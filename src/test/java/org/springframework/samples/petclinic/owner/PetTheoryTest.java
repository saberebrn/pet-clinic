package org.springframework.samples.petclinic.owner;

import org.junit.Assume;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.samples.petclinic.visit.Visit;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(Theories.class)
public class PetTheoryTest {

	@DataPoints
	public static List<LinkedHashSet<Visit>> petNames() {
		List<LinkedHashSet<Visit>> dataPoints = new ArrayList<>();
		dataPoints.add(
			new LinkedHashSet<>(
				Arrays.asList(
					new Visit().setDate(LocalDate.of(2021, 10, 10)),
					new Visit().setDate(LocalDate.of(2021, 9, 10))
				)
			)
		);

		dataPoints.add(
			new LinkedHashSet<>(
				Arrays.asList(
					new Visit().setDate(LocalDate.of(2021, 10, 10)),
					new Visit().setDate(LocalDate.of(2021, 10, 15)),
					new Visit().setDate(LocalDate.of(2021, 10, 20))
				)
			)
		);

		dataPoints.add(null);

		return dataPoints;
	}

	@Theory
	public void testGetPet(LinkedHashSet<Visit> visits) throws Exception {
		Assume.assumeTrue(visits != null);
		Pet pet = new Pet();
		final Field field = pet.getClass().getDeclaredField("visits");
		field.setAccessible(true);
		field.set(pet, visits);

		assertEquals(visits.size(), pet.getVisits().size());
		for(Visit visit: visits){
			assertTrue(pet.getVisits().contains(visit));
		}
	}
}
