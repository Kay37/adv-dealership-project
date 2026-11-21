# Car Dealership: Sales & Leasing

A Java 17 console app for a used car dealership. It started as my Workbook 4 inventory project and now
also records **sales** and **leases**, works out the pricing, and saves every contract to a file.

Built for the Year Up United / Pluralsight Workbook 5 workshop (inheritance and abstract classes).

## What it does

| # | Menu option | What happens |
|---|---|---|
| 1–6 | Find vehicles | Search by price, make/model, year, color, mileage, or type |
| 7 | List all vehicles | Prints the inventory as a table |
| 8 | Add a vehicle | Adds a car and saves `inventory.csv` |
| 9 | Remove a vehicle | Removes a car by VIN and saves `inventory.csv` |
| 10 | **Sell/Lease a vehicle** | Records a sale or lease, saves it to `contracts.csv`, and removes the car from inventory |
| 99 | Quit | |

### Selling or leasing a car (option 10)

1. Enter the car's VIN. If no car has that VIN, the app says so and goes back to the menu.
2. Enter the customer's name and email. Today's date is filled in for you.
3. Choose **sale** or **lease**.
   - **Sale:** say whether the customer wants to finance.
   - **Lease:** cars more than 3 years old can't be leased, and the app says so.
4. The contract is saved, the car is removed from inventory, and the total price and monthly payment are shown.

## Pricing rules

**Sale**

| Item | Rule |
|---|---|
| Sales tax | 5% of the car's price |
| Recording fee | $100 |
| Processing fee | $295 if the car costs under $10,000, otherwise $495 |
| Total price | car price + sales tax + recording fee + processing fee |
| Monthly payment | $0 if not financed. Financed: 4.25% for 48 months if the total is $10,000 or more, otherwise 5.25% for 24 months |

**Lease**

| Item | Rule |
|---|---|
| Expected ending value | 50% of the car's price |
| Lease fee | 7% of the car's price |
| Total price | ending value + lease fee |
| Monthly payment | 4.0% for 36 months |

Monthly payments use the standard loan formula, with the monthly rate = yearly rate ÷ 12:

```
payment = P × r / (1 − (1 + r)^−n)
```

## How it's built

```
Contract (abstract)        date, customer name, email, vehicle sold
├── SalesContract          sales tax, recording fee, processing fee, finance yes/no
└── LeaseContract          expected ending value, lease fee
```

| Class | Job |
|---|---|
| `Program` | Starts the app |
| `UserInterface` | The menu, all questions to the user, and all printing |
| `InputToolbox` | Safe input helpers (numbers in a range, yes/no, one of two choices) |
| `Dealership` | The dealership's details and its list of vehicles, plus the search methods |
| `Vehicle` | One car: VIN, year, make, model, type, color, odometer, price |
| `DealershipFileManager` | Reads and rewrites `inventory.csv` |
| `Contract` | Abstract parent class. `getTotalPrice()` and `getMonthlyPayment()` are abstract, because each kind of contract works them out differently |
| `SalesContract` / `LeaseContract` | Each one works out its own fees, total, and monthly payment |
| `ContractDataManager` | Appends each contract to `contracts.csv`, using `instanceof` to write the right fields for a sale or a lease |

Money is stored as `BigDecimal` and rounded to cents.

## Data files

Both files are pipe-delimited (`|`) and live in the project folder.

**`inventory.csv`**: the first line is the dealership, then one line per car:

```
D & B Used Cars|111 Old Benbrook Rd|817-555-5555
10112|1993|Ford|Explorer|SUV|Red|525123|995.00
```

**`contracts.csv`**: one line per contract, **added** to the end (never rewritten). The app creates it on the first sale.

```
SALE|20261121|Dana Wyatt|dana@texas.com|10112|1993|Ford|Explorer|SUV|Red|525123|995.00|49.75|100.00|295.00|1439.75|NO|0.00
LEASE|20261121|Zachary Westly|zach@texas.com|55555|2024|Chevrolet|Silverado|truck|Black|2750|31995.00|15997.50|2239.65|18237.15|538.43
```

| Type | Fields after the 8 vehicle fields |
|---|---|
| SALE | sales tax, recording fee, processing fee, total price, finance (YES/NO), monthly payment |
| LEASE | expected ending value, lease fee, total price, monthly payment |

## How to run it

1. Clone this repo and open the `adv-dealership-project` folder in IntelliJ IDEA (JDK 17).
2. Run `Program` (`src/main/java/com/dealership/Program.java`).
3. The app reads `inventory.csv` from the project folder, so run it from there (IntelliJ does this by default).

## How I used AI

I used an AI coach (Claude) while building this. It explained ideas and asked me questions based off of the workshop PDF. I wrote the Java code myself. The AI wrote this README.
