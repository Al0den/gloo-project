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

## Open in Eclipse

The easiest way is:

```text
File -> Import -> Maven -> Existing Maven Projects
```

Then select the project folder and finish.

If Eclipse shows errors like `Inventory cannot be resolved to a type`, check the build path:

```text
Right click project -> Build Path -> Configure Build Path -> Source
```

The source folders should include:

```text
src/main/java
src/test/java
```

The test files must stay in `src/test/java`, for example:

```text
src/test/java/inventory/InventoryTest.java
```

and the source files must stay in:

```text
src/main/java/inventory/Inventory.java
src/main/java/inventory/Item.java
```

After changing this, use:

```text
Project -> Clean
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

## Useful commands

```text
additem milk dairy 1.20 1.00 50
restock milk 10
setstockthreshold milk 8
showinventory
```

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
