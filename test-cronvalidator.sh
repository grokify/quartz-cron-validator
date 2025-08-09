#!/bin/bash

# Test script for cronvalidator.jar
# This script demonstrates all the functionality of the cron validator CLI tool

echo "=== Quartz Cron Validator CLI Test Script ==="
echo

JAR_PATH="target/cronvalidator.jar"

if [ ! -f "$JAR_PATH" ]; then
    echo "Error: $JAR_PATH not found. Please run 'mvn clean package' first."
    exit 1
fi

echo "1. Testing valid cron expressions as CLI arguments:"
echo "---------------------------------------------------"

VALID_EXPRESSIONS=(
    "0 0 12 * * ?"
    "0 15 10 ? * MON-FRI"
    "0 0/5 14 * * ?"
    "*/30 * * * * ?"
)

for expr in "${VALID_EXPRESSIONS[@]}"; do
    if java -jar "$JAR_PATH" "$expr"; then
        echo "✓ '$expr' - VALID (exit code: $?)"
    else
        echo "✗ '$expr' - INVALID (exit code: $?)"
    fi
done

echo
echo "2. Testing invalid cron expressions as CLI arguments:"
echo "-----------------------------------------------------"

INVALID_EXPRESSIONS=(
    "invalid cron"
    "0 0 25 * * ?"
    "0 60 12 * * ?"
    "0 0 12 32 * ?"
    "* * * * * * *"
)

for expr in "${INVALID_EXPRESSIONS[@]}"; do
    if java -jar "$JAR_PATH" "$expr"; then
        echo "✗ '$expr' - VALID (exit code: $?) - UNEXPECTED!"
    else
        echo "✓ '$expr' - INVALID (exit code: $?)"
    fi
done

echo
echo "3. Testing pipe/stdin input:"
echo "----------------------------"

echo "Valid expression via pipe:"
if echo "0 0 12 * * ?" | java -jar "$JAR_PATH"; then
    echo "✓ Pipe input with valid expression - VALID (exit code: $?)"
else
    echo "✗ Pipe input with valid expression - INVALID (exit code: $?)"
fi

echo "Invalid expression via pipe:"
if echo "not a cron" | java -jar "$JAR_PATH"; then
    echo "✗ Pipe input with invalid expression - VALID (exit code: $?) - UNEXPECTED!"
else
    echo "✓ Pipe input with invalid expression - INVALID (exit code: $?)"
fi

echo
echo "4. Testing usage message (empty input):"
echo "----------------------------------------"
echo "" | java -jar "$JAR_PATH" 2>&1
echo "Exit code: $?"

echo
echo "5. Testing batch validation:"
echo "----------------------------"

echo -e "0 0 12 * * ?\n0 15 10 ? * MON-FRI\ninvalid\n0 0 25 * * ?\n*/30 * * * * ?" | while read line; do
    if [ -n "$line" ]; then
        if echo "$line" | java -jar "$JAR_PATH" >/dev/null 2>&1; then
            echo "✓ '$line' - VALID"
        else
            echo "✗ '$line' - INVALID"
        fi
    fi
done

echo
echo "=== Test completed ==="
