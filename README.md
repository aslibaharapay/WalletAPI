# Wallets Service

This code consists of building a proof of concept of **RESTAPI** Wallet.
You have to code endpoints for these operations:
1. Get a wallet using its identifier.
1. Top-up money in that wallet using a credit card number. It has to charge that amount internally using a third-party platform.

The basic structure of a wallet is its identifier and its current balance.

I can also find an implementation of the service that would call to the real payments platform.
This implementation is calling to a simulator deployed in one of our environments. Take into account
that this simulator will return 422 http error codes under certain conditions.

I explain my technological solution in briefly on this app with specifications,
Java 11 ,Spring Boot, Rest , Lombok , MapStruct , Junit5

This service work in a microservices environment in high availability. I care about concurrency.
I write Unit test and also Integration Test.

    /*
    For Proof of concept - DB lock is preferable
    Real life scenario too much db lock result in bottleneck so
    REDIS Distributed Locking Tools can be used.
     */
     
## Owner contact
Email : [asli.bhr.apaydin@gmail.com](mailto:asli.bhr.apaydin@gmail.com)
LinkedIn: [aslibaharcay](https://www.linkedin.com/in/asl%C4%B1-bahar-%C3%A7ay-7b0b7779/)
