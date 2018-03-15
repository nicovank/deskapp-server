package edu.oswego.reslife.deskapp.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayPlusTest {
	@Test
	void boundsTest() {
		Integer[] originalArray = new Integer[]{
				0, 1, 2, 3, 4, 5
		};
		ArrayPlus<Integer> slice1 = new ArrayPlus<>(originalArray);

		assertEquals(slice1.get(0), originalArray[0]);
		assertEquals(slice1.get(5), originalArray[5]);

		ArrayPlus<Integer> slice2 = slice1.slice(1, 4);
		assertEquals(originalArray[1], slice2.get(0));
		assertEquals(originalArray[3], slice2.get(2));

		ArrayPlus<Integer> slice3 = new ArrayPlus<>(originalArray, 1, 4);
		assertEquals(slice2, slice3);
	}

	@Test
	void setTest() {
		String[] originalArray = new String[]{
				"0", "1", "2", "3", "4", "5"
		};

		ArrayPlus<String> slice1 = new ArrayPlus<>(originalArray.length);
		for (int i = 0; i < originalArray.length; ++i) {
			slice1.set(i, originalArray[i]);
		}

		assertEquals(slice1, new ArrayPlus<>(originalArray));
	}
}
