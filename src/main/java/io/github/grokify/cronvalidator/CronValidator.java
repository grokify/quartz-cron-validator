package io.github.grokify.cronvalidator;

import org.quartz.CronExpression;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * CLI tool to validate cron expressions using Quartz CronExpression.
 * Returns exit code 0 for valid expressions, 1 for invalid expressions.
 * 
 * Usage:
 *   java -jar cronvalidator.jar "0 0 12 * * ?"
 *   echo "0 0 12 * * ?" | java -jar cronvalidator.jar
 */
public class CronValidator {
    
    public static void main(String[] args) {
        String cronExpression = null;
        
        try {
            // Check if cron expression is provided as command line argument
            if (args.length > 0) {
                cronExpression = args[0];
            } else {
                // Read from stdin for pipe support
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                cronExpression = reader.readLine();
                reader.close();
            }
            
            // Validate the cron expression
            if (cronExpression != null && !cronExpression.trim().isEmpty()) {
                boolean isValid = validateCronExpression(cronExpression.trim());
                System.out.println("Quartz 2.x Cron Expression Validity: " + (isValid ? "VALID" : "INVALID"));
                System.exit(isValid ? 0 : 1);
            } else {
                // No input provided
                printUsage();
                System.exit(1);
            }
            
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Validates a cron expression using Quartz CronExpression.isValidExpression()
     * 
     * @param cronExpression The cron expression to validate
     * @return true if valid, false if invalid
     */
    public static boolean isValidExpression(String cronExpression) {
        try {
            return CronExpression.isValidExpression(cronExpression);
        } catch (Exception e) {
            // Any exception during validation means invalid expression
            return false;
        }
    }
    
    /**
     * Validates a cron expression using Quartz CronExpression.isValidExpression()
     * 
     * @param cronExpression The cron expression to validate
     * @return true if valid, false if invalid
     */
    private static boolean validateCronExpression(String cronExpression) {
        return isValidExpression(cronExpression);
    }
    
    /**
     * Prints usage information
     */
    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("  java -jar cronvalidator.jar \"<cron-expression>\"");
        System.err.println("  echo \"<cron-expression>\" | java -jar cronvalidator.jar");
        System.err.println();
        System.err.println("Examples:");
        System.err.println("  java -jar cronvalidator.jar \"0 0 12 * * ?\"");
        System.err.println("  echo \"0 0 12 * * ?\" | java -jar cronvalidator.jar");
        System.err.println();
        System.err.println("Exit codes:");
        System.err.println("  0 - Valid cron expression");
        System.err.println("  1 - Invalid cron expression or error");
    }
}
