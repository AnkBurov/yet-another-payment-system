# Yet another demo payment system
[![Build Status](https://travis-ci.org/AnkBurov/yet-another-payment-system.svg?branch=master)](https://travis-ci.org/AnkBurov/yet-another-payment-system) [![Coverage Status](https://coveralls.io/repos/github/AnkBurov/yet-another-payment-system/badge.svg?branch=master)](https://coveralls.io/github/AnkBurov/yet-another-payment-system?branch=master)

An example of a simple payment system. Supports making payments between accounts, deposit money to the account
and withdraw money from the account:
* POST /payment
* PUT /account-operation/deposit
* GET /account-operation/withdraw

## Build

`./gradlew clean build`

## Run

`cd build/libs`
`java -jar yet-another-payment-system.jar`

## Concurrent model

The application doesn't depend on an implicit application synchronization when executing account operations.
Instead, it relies on underlying database synchronization mechanisms, especially on database transaction isolation level.
The application uses READ COMMITTED isolation level, therefore it will read only committed changes from the database.
If several transactions at the same time will attempt to change the same account's balance, then one transaction will succeed and
the second will be locked until the first transaction will unlock the row lock thanks to the READ COMMITTED isolation level.
Due to `UPDATE BALANCE = BALANCE +/- :amount` SQL query, an update from the first transaction won't be lost when second
transaction will commit its changes.
The constraint on the account balance column also won't allow an account balance be a negative number.
Therefore the application is concurrently safe and can be invoked in parallel and also scaled horizontally.