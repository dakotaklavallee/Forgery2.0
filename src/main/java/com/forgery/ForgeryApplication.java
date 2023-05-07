package com.forgery;

import com.forgery.GUITools.MainFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class ForgeryApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = createApplicationContext(args);
		displayMainFrame(context);
	}

	private static ConfigurableApplicationContext createApplicationContext(String[] args) {
		return new SpringApplicationBuilder(ForgeryApplication.class)
				.headless(false)
				.run(args);
	}

	private static void displayMainFrame(ConfigurableApplicationContext context) {
		SwingUtilities.invokeLater(() -> {
			MainFrame mainFrame = context.getBean(MainFrame.class);
			mainFrame.setUp();
		});
	}

}
