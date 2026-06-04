# Supermarket project

Java implementation of the supermarket exercise.

## Requirements

- Java 11 or newer
- Maven, only if you want to use the Maven commands

## Run with Maven

From the root folder:

```bash
mvn exec:java
```

The program starts the CLI. It also loads `my_supermarket.ini` at startup.

## Run without Maven

From the root folder:

```bash
mkdir -p out
javac -d out $(find src/main/java -name "*.java")
java -cp out core.Main
```

To remove the compiled files:

```bash
rm -rf out
```

## Stop the program

In the CLI:

```text
exit
```

or:

```text
stop
```

## Run tests

With Maven:

```bash
mvn test
```

The tests use JUnit 5.

## Scenario files

Some example scenario files are in the `scenarios/` folder.

Example:

```text
runfile scenarios/test_basic_checkout.txt
```

This command is typed inside the CLI.

## Entry point

The main class is:

```text
core.Main
```

## Global structure

`core.Main` creates a `Supermarket` and starts the CLI.

`core.Supermarket` is the central class. It connects the other modules together:

- `cli` reads commands and calls methods on `Supermarket`
- `users` contains `Manager`, `Cashier`, `Customer` and the `Cart`
- `inventory` contains categories, items, stock and category pricing policies
- `discount` contains the customer plans and their discounts
- `delivery` computes delivery requests, slots and fees
- `payment` contains bills, bank cards, the POS device and the transaction system

For a normal checkout, the flow is:

```text
CLI -> Supermarket -> Cart -> Bill -> POSDevice -> TransactionSystem
```

If delivery is requested, `Supermarket` also uses the `delivery` module before the payment amount is finalized.
