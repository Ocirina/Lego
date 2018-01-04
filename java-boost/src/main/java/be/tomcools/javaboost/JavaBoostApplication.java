package be.tomcools.javaboost;

import be.tomcools.javaboost.commands.GatttoolCommandWrapper;
import be.tomcools.javaboost.commands.Motor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;
import java.util.regex.Pattern;

@SpringBootApplication
public class JavaBoostApplication {
    private static GatttoolCommandWrapper WRAPPER = new GatttoolCommandWrapper();

	public static void main(String[] args) {
		SpringApplication.run(JavaBoostApplication.class, args);
	}

	@Bean
	CommandLineRunner runOnStartup() {
		return args -> {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Press Boost Button and hit enter.");
            scanner.nextLine();
            WRAPPER.startKeepAlive();

            while(true) {
                System.out.print("# ");
                String nextLine = scanner.nextLine();

                if(Pattern.matches("[ABC]*,[0-9]*,[0-9]*", nextLine)) {
                    System.out.println("direct command");
                    executeDirectCommand(nextLine);
                } else {
                    switch (nextLine) {
                        case "FORWARD":
                            WRAPPER.motorAngle(Motor.AB, 360, 100);
                            break;
                        case "TURN RIGHT":
                            WRAPPER.motorAngle(Motor.A, 360, 100);
                            break;
                        case "TURN LEFT":
                            WRAPPER.motorAngle(Motor.B, 360, 100);
                            break;
                        case "FIRE":
                            WRAPPER.motorAngle(Motor.C, 100, 100);
                            Thread.sleep(1000);
                            WRAPPER.motorAngle(Motor.C, 100, -100);
                            break;
                        default:
                            System.out.println("Invalid command...");
                    }
                }
            }
        };
	}

	private static void executeDirectCommand(String command) {
        String[] commandParts = command.split(",");
        if(commandParts.length != 3) {
            System.out.println("Not valid direct command...");
        }
        Motor m = Motor.valueOf(commandParts[0]);
        int angle = Integer.parseInt(commandParts[1]);
        int dutyCycle = Integer.parseInt(commandParts[2]);
        WRAPPER.motorAngle(m,angle,dutyCycle);
    }
}