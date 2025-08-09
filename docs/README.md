# API Documentation

This directory contains the Javadoc API documentation for the Quartz Cron Validator project.

## Viewing the Documentation

Open `index.html` in your web browser to view the complete API documentation.

## Updating the Documentation

To regenerate the Javadoc documentation after making changes to the code:

1. **Generate Javadoc**: Run the Maven Javadoc plugin
   ```bash
   mvn javadoc:javadoc
   ```

2. **Copy to docs directory**: The generated documentation will be in `target/reports/apidocs/`. Copy it to the docs directory:
   ```bash
   rsync -av target/reports/apidocs/ docs/
   ```

3. **Verify**: Open `docs/index.html` to verify the documentation has been updated.

## Maven Configuration

The project uses the Maven Javadoc plugin which is automatically downloaded when running `mvn javadoc:javadoc`. The documentation is generated from the source code comments and includes:

- Class documentation for `CronValidator`
- Method documentation including the public `isValidExpression()` method
- Package information
- Cross-references and search functionality

## Hosting

These HTML files can be served by any web server or hosted on GitHub Pages by placing them in the `docs/` directory of your repository.