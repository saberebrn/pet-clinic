package org.springframework.samples.petclinic.utility;

import com.github.mryf323.tractatus.*;
import com.github.mryf323.tractatus.experimental.extensions.ReportingExtension;
import com.github.mryf323.tractatus.ClauseCoverage;
import com.github.mryf323.tractatus.Valuation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(ReportingExtension.class)
class TriCongruenceTest {

	private static final Logger log = LoggerFactory.getLogger(TriCongruenceTest.class);

	@Test
	public void sampleTest() {
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(7, 2, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/*
	As it is stated in course slides the set of implicats for 'ab' is {TTFF, TTFT, TTTF}
	and the set of implicants for 'cd' is {FFTT, FTTT, TFTT}
  	but the CUTPNFP is going to cover following set of implicants {TTFF, FFTT, TFFF, FTFF, FFTF, FFFT}
  	It is clear that the CUTPNFP does not subsume UTPC because it does not contains all the implicants that
  	the UTPC covers.
	 */
	private static boolean questionTwo(boolean a, boolean b, boolean c, boolean d, boolean e) {
		boolean predicate = ( a & b ) | (c & d);
//		predicate = a predicate with any number of clauses
		return predicate;
	}

	@Test
	@ClauseCoverage(
		predicate = "a + b", // a = t1arr[0] < 0, b = t1arr[0] + t1arr[1] < t1arr[2]
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false)
		}
	)
	public void CCFalseStateTest(){
		Triangle t1 = new Triangle(-1, 3, 7);
		Triangle t2 = new Triangle(7, -1, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@Test
	@ClauseCoverage(
		predicate = "a + b", // a = t1arr[0] < 0, b = t1arr[0] + t1arr[1] < t1arr[2]
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = true)
		}
	)
	public void CCTrueStateTest(){
		Triangle t1 = new Triangle(4, 10, 8);
		Triangle t2 = new Triangle(10, 8, 4);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	@Test
	@CACC(
		predicate = "a + b", // a = t1arr[0] < 0, b = t1arr[0] + t1arr[1] < t1arr[2]
		majorClause = 'b',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true)
		},
		predicateValue = false
	)
	public void CACCTest1(){
		Triangle t1 = new Triangle(4, 10, 4);
		Triangle t2 = new Triangle(10, 4, 4);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@Test
	@CACC(
		predicate = "a + b",  // a = t1arr[0] < 0, b = t1arr[0] + t1arr[1] < t1arr[2]
		majorClause = 'a',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false)
		},
		predicateValue = true
	)
	public void CACCTest2(){
		Triangle t1 = new Triangle(6,8, 10);
		Triangle t2 = new Triangle(10, 6, 8);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	@Test
	@UniqueTruePoint(
		predicate = "a + b + c", // a = t1arr[0] != t2arr[0], b = t1arr[1] != t2arr[1], c = t1arr[2] != t2arr[2]
		dnf = "a + b + c",
		implicant = "a",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@NearFalsePoint(
		predicate = "a + b + c", // a = t1arr[0] != t2arr[0], b = t1arr[1] != t2arr[1], c = t1arr[2] != t2arr[2]
		dnf = "a + b + c",
		implicant = "a",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	public void CUTPNFTest1(){
		Triangle t1 = new Triangle(6,8, 10);
		Triangle t2 = new Triangle(10, 5, 8);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@Test
	@UniqueTruePoint(
		predicate = "a + b + c", // a = t1arr[0] != t2arr[0], b = t1arr[1] != t2arr[1], c = t1arr[2] != t2arr[2]
		dnf = "a + b + c",
		implicant = "b",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@NearFalsePoint(
		predicate = "a + b + c", // a = t1arr[0] != t2arr[0], b = t1arr[1] != t2arr[1], c = t1arr[2] != t2arr[2]
		dnf = "a + b + c",
		implicant = "b",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	public void CUTPNFTest1(){
		Triangle t1 = new Triangle(6,8, 10);
		Triangle t2 = new Triangle(10, 6, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@Test
	@UniqueTruePoint(
		predicate = "a + b + c", // a = t1arr[0] != t2arr[0], b = t1arr[1] != t2arr[1], c = t1arr[2] != t2arr[2]
		dnf = "a + b + c",
		implicant = "c",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = true)
		}
	)
	@NearFalsePoint(
		predicate = "a + b + c", // a = t1arr[0] != t2arr[0], b = t1arr[1] != t2arr[1], c = t1arr[2] != t2arr[2]
		dnf = "a + b + c",
		implicant = "c",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	public void CUTPNFTest1(){
		Triangle t1 = new Triangle(6,8, 10);
		Triangle t2 = new Triangle(11, 6, 8);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@Test
	@NearFalsePoint(
		predicate = "a + b + c", // a = t1arr[0] != t2arr[0], b = t1arr[1] != t2arr[1], c = t1arr[2] != t2arr[2]
		dnf = "a + b + c",
		implicant = "abc",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@NearFalsePoint(
		predicate = "a + b + c", // a = t1arr[0] != t2arr[0], b = t1arr[1] != t2arr[1], c = t1arr[2] != t2arr[2]
		dnf = "a + b + c",
		implicant = "abc",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	public void CUTPNFTest1(){
		Triangle t1 = new Triangle(6,8, 10);
		Triangle t2 = new Triangle(11, 6, 8);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}
}
