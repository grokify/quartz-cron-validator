package io.github.grokify.cronvalidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import org.quartz.CronExpression;

/**
 * Unit tests for CronValidator CLI tool
 * Note: These tests focus on the validation logic rather than the CLI behavior
 * since testing System.exit() is complex in modern Java versions.
 */
public class CronValidatorTest {
    
    @Test
    @DisplayName("Valid cron expressions should be recognized as valid")
    void testValidCronExpressions() {
        String[] validExpressions = {
            "0 0 12 * * ?",           // Every day at noon
            "0 15 10 ? * MON-FRI",    // 10:15 AM every weekday
            "0 0/5 14 * * ?",         // Every 5 minutes starting at 2:00 PM
            "0 0 12 1/5 * ?",         // 12:00 PM every 5 days every month
            "0 0 0 1 1 ? 2025",       // New Year 2025
            "*/30 * * * * ?",         // Every 30 seconds
            "0 0 8-10 * * ?",         // Every hour between 8am and 10am
            "0 0 6,19 * * ?"          // 6am and 7pm every day
        };
        
        for (String expression : validExpressions) {
            assertTrue(CronExpression.isValidExpression(expression), 
                "Expression should be valid: " + expression);
        }
    }
    
    @Test
    @DisplayName("Invalid cron expressions should be recognized as invalid")
    void testInvalidCronExpressions() {
        String[] invalidExpressions = {
            "invalid",
            "0 0 25 * * ?",           // Invalid hour (25)
            "0 60 12 * * ?",          // Invalid minute (60)
            "0 0 12 32 * ?",          // Invalid day of month (32)
            "0 0 12 * 13 ?",          // Invalid month (13)
            "0 0 12 ? * 8",           // Invalid day of week (8)
            "0 0 12",                 // Too few fields
            "0 0 12 * * ? ? ?",       // Too many fields
            "",                       // Empty string
            "* * * * *"               // Missing required field
        };
        
        for (String expression : invalidExpressions) {
            assertFalse(CronExpression.isValidExpression(expression), 
                "Expression should be invalid: " + expression);
        }
    }
    
    @Test
    @DisplayName("Null and empty strings should be handled as invalid")
    void testNullAndEmptyExpressions() {
        // Null expressions throw an exception, so we test that behavior
        assertThrows(IllegalArgumentException.class, () -> {
            CronExpression.isValidExpression(null);
        }, "Null expression should throw IllegalArgumentException");
        
        assertFalse(CronExpression.isValidExpression(""), 
            "Empty expression should be invalid");
        assertFalse(CronExpression.isValidExpression("   "), 
            "Whitespace-only expression should be invalid");
    }
    
    @Test
    @DisplayName("Edge case cron expressions should be handled correctly")
    void testEdgeCaseCronExpressions() {
        // Test some edge cases that should be valid
        String[] edgeCaseValid = {
            "0 0 0 ? * SUN",          // Sunday at midnight
            "59 59 23 31 12 ?",       // Last second of New Year's Eve
            "0 0 12 L * ?",           // Last day of every month at noon
            "0 0 12 ? * 2#1",         // First Monday of every month
            "0 0 12 1W * ?"           // Nearest weekday to the 1st of the month
        };
        
        for (String expression : edgeCaseValid) {
            assertTrue(CronExpression.isValidExpression(expression), 
                "Edge case expression should be valid: " + expression);
        }
        
        // Test some edge cases that should be invalid
        String[] edgeCaseInvalid = {
            "0 0 12 ? * 8",           // Invalid day of week
            "0 0 24 * * ?",           // Invalid hour
            "60 0 12 * * ?",          // Invalid minute
            "0 60 12 * * ?",          // Invalid second (wait, this is minute)
            "* * * * * * *"           // Too many fields
        };
        
        for (String expression : edgeCaseInvalid) {
            assertFalse(CronExpression.isValidExpression(expression), 
                "Edge case expression should be invalid: " + expression);
        }
    }
}
