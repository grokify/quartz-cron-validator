# Quartz Cron Validator CLI

[![Build Status][build-status-svg]][build-status-url]
[![SAST Security Analysis][sast-status-svg]][sast-status-url]
[![SCA Dependency Analysis][sca-status-svg]][sca-status-url]
[![Docs][docs-javadoc-svg]][docs-javadoc-url]
[![License][license-svg]][license-url]

A command-line tool that validates cron expressions using the Quartz Scheduler's `CronExpression.isValidExpression()` method.

## Features

- Validates cron expressions using the industry-standard Quartz library
- Returns proper exit codes (0 for valid, 1 for invalid)
- Supports both command-line arguments and stdin input
- Perfect for use in shell scripts and CI/CD pipelines
- Pipe-friendly for Linux/Unix environments
- Provides `CronValidator.isValidExpression()` static method for programmatic use

## Building

```bash
mvn clean package
```

This will create `cronvalidator.jar` in the `target/` directory.

## Usage

### Command Line Argument
```bash
java -jar target/cronvalidator.jar "0 0 12 * * ?"
echo $?  # Returns 0 for valid, 1 for invalid
```

### Stdin/Pipe Input
```bash
echo "0 0 12 * * ?" | java -jar target/cronvalidator.jar
echo $?  # Returns 0 for valid, 1 for invalid
```

### Shell Script Example
```bash
#!/bin/bash
CRON_EXPR="0 0 12 * * ?"

if java -jar cronvalidator.jar "$CRON_EXPR"; then
    echo "Cron expression is valid"
else
    echo "Cron expression is invalid"
fi
```

### Pipeline Example
```bash
# Validate multiple cron expressions
cat cron_expressions.txt | while read line; do
    if echo "$line" | java -jar cronvalidator.jar; then
        echo "$line - VALID"
    else
        echo "$line - INVALID"
    fi
done
```

## Programmatic Usage

You can also use the `CronValidator.isValidExpression()` method directly in your Java code:

```java
import io.github.grokify.cronvalidator.CronValidator;

public class Example {
    public static void main(String[] args) {
        String cronExpr = "0 0 12 * * ?";
        boolean isValid = CronValidator.isValidExpression(cronExpr);
        System.out.println("Expression '" + cronExpr + "' is " + (isValid ? "valid" : "invalid"));
    }
}
```

## Exit Codes

- `0` - Cron expression is valid
- `1` - Cron expression is invalid or error occurred

## Cron Expression Format

This tool uses Quartz cron expressions, which have 6 or 7 fields:

```
┌───────────── second (0-59)
│ ┌───────────── minute (0-59)
│ │ ┌───────────── hour (0-23)
│ │ │ ┌───────────── day of month (1-31)
│ │ │ │ ┌───────────── month (1-12)
│ │ │ │ │ ┌───────────── day of week (0-7, 0 and 7 are Sunday)
│ │ │ │ │ │ ┌───────────── year (optional)
│ │ │ │ │ │ │
* * * * * * *
```

### Examples

- `0 0 12 * * ?` - Every day at noon
- `0 15 10 ? * MON-FRI` - 10:15 AM every weekday
- `0 0/5 14 * * ?` - Every 5 minutes starting at 2:00 PM and ending at 2:55 PM, every day
- `0 0 12 1/5 * ?` - 12:00 PM every 5 days every month, starting on the first day of the month

## API Documentation

Full Javadoc API documentation is available in the `docs/` directory. Open `docs/index.html` in your web browser to view the complete documentation.

To update the documentation after making code changes:
```bash
mvn javadoc:javadoc
rsync -av target/reports/apidocs/ docs/
```

## Dependencies

- Java 11+
- Quartz Scheduler 2.3.2

 [build-status-svg]: https://github.com/grokify/quartz-cron-validator/actions/workflows/ci.yaml/badge.svg?branch=main
 [build-status-url]: https://github.com/grokify/quartz-cron-validator/actions/workflows/ci.yaml
 [sast-status-svg]: https://github.com/grokify/quartz-cron-validator/actions/workflows/sast.yaml/badge.svg?branch=main
 [sast-status-url]: https://github.com/grokify/quartz-cron-validator/actions/workflows/sast.yaml
 [sca-status-svg]: https://github.com/grokify/quartz-cron-validator/actions/workflows/sca.yaml/badge.svg?branch=main
 [sca-status-url]: https://github.com/grokify/quartz-cron-validator/actions/workflows/sca.yaml
 [docs-javadoc-svg]: https://pkg.go.dev/badge/github.com/grokify/quartz-cron-validator
 [docs-javadoc-url]: https://grokify.github.io/quartz-cron-validator/
 [license-svg]: https://img.shields.io/badge/Javadoc-reference-blue.svg
 [license-url]: https://github.com/quartz-cron-validator/goauth/blob/master/LICENSE
