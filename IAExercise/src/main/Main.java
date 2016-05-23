package main;

import java.util.Scanner;

import needed.Manager;

public class Main {

	public static void main(String[] args) {
		Manager manager = new Manager();
		String entry;
		Scanner input = new Scanner(System.in);
		
		System.out.println("Por favor, entre com as regras");
		System.out.println("Para terminar entre com \"END\"");
		entry = input.nextLine();
		while(!entry.equals("END")){
			manager.readRule(entry);
			System.out.println("Próxima regra");
			System.out.println("Para terminar entre com \"END\"");
			entry = input.nextLine();
		}
		
		System.out.println("Por favor, entre com os fatos");
		System.out.println("Para terminar entre com \"END\"");
		entry = input.nextLine();
		while(!entry.equals("END")){
			manager.readFact(entry);
			System.out.println("Próxima regra");
			System.out.println("Para terminar entre com \"END\"");
			entry = input.nextLine();
		}
		
		

	}

}
