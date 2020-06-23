package com.swapnil.SampleSonarQubeDemo;

import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {

	private static final String swapnil = "swapnil";
	private static final String DEPT_DEV = "DEV";

	public static void main(String[] args) {
		String username = "Swapnil";
		String dept = "dev";

		if (username != null && username.equalsIgnoreCase(swapnil)) {
			System.out.println("logged in username is swapnil");
		}

		if (dept.equalsIgnoreCase(DEPT_DEV)) {
			System.out.println("logged in username belongs to Developer group");
		}

		System.out.println(greatestOfThree(1, 2, 3));

		List<String> list = Arrays.asList(new String[] { "A", "B", "C", "D" });
		printList(list);
	}

	public static int greatestOfThree(int a, int b, int c) {
		if (a > b && a > b) {
			if (a > c) {
				return a;
			} else {
				return c;
			}
		} else if (b > c) {
			return b;
		} else {
			return c;
		}
	}

	/*
	 * public static int greatestOfThree(int a, int b, int c) { int result; if (a >
	 * b) { if (a > c) { result = a; } else { result = c; } } else if (b > c) {
	 * result = b; } else { result = c; }
	 * 
	 * return result; }
	 */

	public static void printList(List<String> list) {
		if (list != null && list.size() > 0) {
			for (String item : list) {
				System.out.println(item);
			}
		}
	}
}
