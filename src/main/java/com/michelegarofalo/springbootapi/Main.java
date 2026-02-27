package com.michelegarofalo.springbootapi;

import com.michelegarofalo.springbootapi.fiscalcode.FiscalCode;
import com.michelegarofalo.springbootapi.gui.GUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(GUI::createAndShowGUI);

		SpringApplication.run(Main.class, args);

		System.out.println("Listening...");
//		FiscalCode.encodeCity("Bari");
	}
}
