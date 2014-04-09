package org.spacehq.bukkitutils.util;

public class RomanNumeral {
	private static final int[] numbers = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
	private static final String[] letters = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };

	public static String toRomanNumeral(int arabic) {
		String roman = "";
		int N = arabic;
		for(int i = 0; i < numbers.length; i++) {
			while(N >= numbers[i]) {
				roman += letters[i];
				N -= numbers[i];
			}
		}

		return roman;
	}
}
