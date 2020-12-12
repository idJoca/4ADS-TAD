# 4ADS-TAD

The repository for the 4ADS final Data Structure assignment

## About

In this project, besides implementing a abstract type of data (TAD, in portuguese), with serialization and relationships,
we've implemented a command system, with a command parser, that enables the user to write their own queries to interact with the Entity system.

## Members

Bruno baldini, Fernando Carneiro, Gustavo Broch, Gustavo Catalane e João Pedro Braz

## How to use

In order to start this project, run the App.java class in the terminal, it's the main class of the application.  
Once started, it'll wait for your commands to be inputed.  
A command has the following structure:

> KEYWORD ENTITY [...,FIELD|:FIELD="VALUE"]

Where:

* **KEYWORD** is one of: CREATE, READ, UPDATE or DELETE

* **ENTITY** is one of: APARTMENT, BUILDING or PERSON (The entities are entirely arbitrary and **not** hardcoded into the application, just check the "setup" method in the App class to see how to register new entities)

* **[...,FIELD|:FIELD="VALUE"]** are the arguments provided to each command. They subdivide into 2 categories:

    * Modifier Arguments (FIELD="VALUE"), actively modify the attributes of an Entity/Entities. Used on CREATE commands, to provide the initial value of an Entity and on UPDATE commands, to modify a existing field.

    * Query Arguments (:FIELD="VALUE", notice the ":", it's mandatory), used to filter existing entites in the application. Used on READ commands, to filter the result set, based on the given fields. Used on UPDATE commands, to filter which entities should be updated. Used on DELETE commands, to filter which entities should be removed.

### Relations

All fields are String based, relations are not an exception. A relation consist of a field that "points" to a Main Field (EntityField with the mainKey parameter set to true) on another Entity. To create a "related" entity simply pass the mainKey field value to the corresponding RelationEntity field, as follows:

> CREATE PERSON name="João Pedro", surname="Braz", apartment="AP 01"  

Wehere "AP 01" corresponds to an existing apartment.

## Examples

To get all the PersonEntitys with the field name containing "João"
> READ PERSON :name="Joao"

To create a new PersonEntity
> CREATE PERSON name="Joao", surname="Braz", apartment="AP 01"  

To change the name of all PersonEntitys which has a name that contains "Joao"
> UPDATE PERSON name="Pedro", :name="Joao"  

To delete all PersonEntitys which has a surname that contains "Braz"
> DELETE PERSON :surname="Braz"  

## Commands

* **close**, closes the application and save to disk the entites.
