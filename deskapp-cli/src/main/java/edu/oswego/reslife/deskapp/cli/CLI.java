package edu.oswego.reslife.deskapp.cli;

import edu.oswego.reslife.deskapp.utils.ArrayPlus;

import java.util.Scanner;

public class CLI {
	public static void parse(ArrayPlus<String> args) {
		System.out.println("ok");
	}

	public void run() {
		Scanner input = new Scanner(System.in);
		System.out.print("> ");
		String cmd = input.nextLine();

		do {
			parse(new ArrayPlus<>(cmd.split(" ")));

			System.out.print("> ");
			cmd = input.nextLine();
		} while (!cmd.equals("quit"));
	}
}
