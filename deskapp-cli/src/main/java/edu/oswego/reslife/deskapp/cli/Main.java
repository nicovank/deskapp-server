package edu.oswego.reslife.deskapp.cli;

import edu.oswego.reslife.deskapp.utils.ArrayPlus;

public class Main {
	public static void main(String[] args) {
		if (args.length == 0) {
			CLI cli = new CLI();
			cli.run();
		} else {
			CLI.parse(new ArrayPlus<>(args));
		}
	}
}
