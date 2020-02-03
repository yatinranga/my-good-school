package com.nxtlife.mgs.entity.user;

import java.util.Scanner;
import java.util.regex.Pattern;

public class PatternSyntaxChecker {

	public static void main(String... s) {

		Scanner scan = new Scanner(System.in);
		int testCases = Integer.parseInt(scan.nextLine());

		while (testCases > 0) {

			String pattern = scan.nextLine();

			try {

				Pattern.compile(pattern);
				System.out.println("Valid");

			} catch (Exception e) {

				System.out.println("Invalid");
			}

			testCases--;
		}

		scan.close();
	}

}
