# Inventory - an example hexagonal architecture

[![CircleCI](https://circleci.com/gh/maciejmalecki/inventory.svg?style=shield)](https://circleci.com/gh/maciejmalecki/inventory)
[![CircleCI](https://circleci.com/gh/maciejmalecki/inventory/tree/develop.svg?style=shield)](https://circleci.com/gh/maciejmalecki/inventory/tree/develop)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Preface
This is a practical example of hexagonal architecture. The goals of this little project are to identify and describe patters useful with this kind of architectures.

The project is organized in source modules, these fall into three major categories:
* `domain`,
* `app`lication,
* `infra`structure.

![General concepts](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/general.puml)

The category to which given module belongs determines dependencies that are allowed for that module. The categories in fact form an onion-like structure, where domain modules are located in the centre and infrastructure modules belong to the most outer layer. Dependencies always point inwards.

## Domain
The domain area is where we model business logic, the most critical enterprise business rules and data structures. In general, this is a digital model of real business that is usually well establish since years or decades. We do not expect these rules to change rapidly, but rather to evolve slowly. It is then worth of investing, to get the cleanest and more correct implementation. In ideal world, this layer should survive he most intense technological revolutions.

### Shared kernel
A shared kernel is realized as a separate module that can be imported by other domain modules. The shared kernel provides common domain functionalities that are shared across domain and in particular: the subdomains.

In this particular example shared kernel defines:
* business transaction port that can be used to demarcate transaction scope in domain level,
* commonly used business level exceptions,
* common data types such as entity identifiers, that can be used to bound one aggregate to another aggregate and communicate with repositories,
* useful utilities such as mutable entity support.

### Subdomains
If domain is big enough that it consist on several subdomains, these can be represented by separate modules on domain level. 

![Subdomains](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/domain.puml)

Subdomains should not depend on each other. If there is a need for a communication between subdomains, we would rather go in loosely coupled integration model by using ports & adapters approach. With this approach we're free to choose exact integration solution: local call vs remote call, synchronous vs asynchronous, event based etc. Because hard dependency between subdomains is forbidden (discouraged), the concrete wiring is done on `app` level or even on `infra` level.

### Business components
Within a subdomain, the primary packaging scheme should follow business component split. Usually there is one component per one aggregate but this is not a hard restriction.

### Entities and aggregates
Entities are classes of persistent objects. Aggregates are complex objects treated as a whole. 

In this particular examples, entities and aggregates are implemented as immutable objects. Mutability aspects are handled by dedicated "mutable" wrappers, capable of remembering local mutation history.

### Repositories
So-called repositories, that is objects able to create, materialize and modify entities and aggregates.

### Domain services
Domain services host logic that is not suitable for embedding directly into entities or aggregates. Domain services usually span over multiple aggregates and have multiple repositories and other domain services injected.

A special kind of domain services are responsible for creating instances of aggregates or other objects thus are called factories.

## App
The App (application) is place where we model application specific logic - that is, the logic that you don't discuss with business experts but are nonetheless necessary to implement the application. We will include most of the CRUD functionalities here that are not needed with respect to the domain but are necessary to give data provisioning capabilities. All bulk operations included imports falls into this category too. When using CQRS approach, most of the "Q" functionality falls here as well.

### Applications
Under `app` directory we collect modules for applications. We follow scheme one application - one module. Name the module after the application, i.e. `product-planner`.

![Product planner](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/product-planner.puml)

### Application components
Primary package composition inside application module should always be an application component. Application component split may follow the business domain component split (but does not need to), plus can introduce several additional components that are purely application specific.

### Queries
Queries are read only repositories that extends functionality of selectors (from the domain). These are necessary to fulfill certain read capabilities of clients (i.e. REST interfaces). We don't expect all view capabilities can be served with aggregates - this is not practical and surely not very performant.

### CRUD repositories
These repositories are extending mutators (and selectors, queries) with modify capabilities necessary to provide data provisioning. We expect that not all CRUD functions are necessary in our domain.

### Facades
Component facades are declared in app and are forming facade over domain plus application specific functions such as CRUDs and queries. From the domain, facades are shielding repositories and behaviors.

We expect a facade per business component.

## Infra
Here we place concrete implementations built with very concrete technologies. Examples are:
* Springboot application,
* Graphical web Angular based client,
* JPA implementation of the repositories,
* Spring-batch based processors.

This is the only place where dependencies to technologies such as Spring, JPA, JDBC, Servlet-API and similar are allowed in 0dependency architecture.

### Store
Store holds implementation of repositories (from `domain`), CRUD repositories and queries (from `app`) using given persistence technology. Store modules depend on concrete persistence library: Hibernate, R2BCD, Reactive Hibernate, JOOQ, JDBI.

![Product planner stores](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/items-store.puml)

#### JDBI
JDBI is a lightweight SQL-to-POJO wrapper library. It allows for nothing more than:
* writing native SQL queries (SELECTS), parametrize them, execute them and then map results into the Java(Kotlin) types: POJOs, collections of POJOs, simple types;
* writing native SQL updates (INSERTs/UPDATEs/DELETEs), parametrize it with Java(Kotlin) values, execute them and sense the results.

JDBI does not provide any support for sophisticated ORM functionalities such as associations, first level caches, object change detections, proxies and lazy fetches, abstraction over native SQL etc. You'll only get what you code yourself.

JDBI seems to be useful each time we have a simple interaction with a database: i.e. our application is mainly reading data by executing series of more or less complex SQL queries, or our application is mass-updating database with series of INSERTs or UPDATEs. As we will see in this example, simple SQL mappers can be useful only with regular business-class applications as long as set of legal data modification is well-defined and restricted (and this should be the case for 90%+ of all business apps!).

Here we solely use so-called declarative way of SQL mapping. With JDBI it is realized via usage of SQL Objects. SQL Objects allows us to write so-called DAOs which are in fact interfaces with method stubs and SQL's declared with appropriate annotation. JDBI and SQL Object extension will generate appropriate implementation of these interfaces in runtime.

Let's look at the following example:
```kotlin
interface ItemStockDao {
    @SqlUpdate("INSERT INTO Item_Stock (item_name, serial, amount) VALUES (:id.itemId.id, :id.serial+1, :amount)")
    fun insertItemStock(id: ItemStockAppId, amount: BigDecimal): Int

    @SqlQuery("SELECT item_name, SUM(amount) AS amount, MAX(serial) AS serial FROM Item_Stock WHERE item_name=:id.id GROUP BY item_name")
    fun selectStockAmount(id: ItemAppId): ItemStockRec?

    @SqlQuery("SELECT item_name, SUM(amount) AS amount, MAX(serial) AS serial FROM Item_Stock WHERE item_name IN (<ids>) GROUP BY item_name")
    fun selectStockAmounts(@BindList("ids") ids: Array<String>): List<ItemStockRec>

    @SqlQuery("""SELECT item_name, MAX(serial) AS serial, manufacturers_code, code AS unit_code, u.name AS unit_name, SUM(amount) AS amount
        FROM Item_Stock its 
            JOIN Items i ON its.item_name = i.name 
            JOIN Item_classes ic ON i.item_class_name = ic.name AND i.item_class_version = ic.version 
            JOIN Units u ON ic.unit = u.code 
        GROUP BY item_name, manufacturers_code, code, u.name
        ORDER BY item_name""")
    fun selectStockWithItems(): List<StockWithItemRec>
}
```
That's it: here we have four SQL queries that handles whole life cycle and all use cases needed to handle `ItemStock` entity. For clarity, each time we need to map a tuple result into the POJO, we declare dedicated class for it (but honestly, we can even map SQL result into business data structures directly, if possible).
```kotlin
data class ItemStockRec(
    val itemName: String,
    val amount: BigDecimal,
    val serial: Int
)

data class StockWithItemRec(
    val itemName: String,
    val serial: Int,
    val manufacturersCode: String?,
    val unitCode: String,
    val unitName: String,
    val amount: BigDecimal
)
```
It has been chosen to put `Rec` classes declarations right below DAO, in exactly the same source file.

With DAO and Rec classes it is now relatively easy to provide `Repository` implementation:
```kotlin
class ItemStockJdbiRepository(private val db: Jdbi) : ItemStockRepository {

    override fun findByItemId(itemId: ItemId): ItemStock = db.withHandle<ItemStock, RuntimeException> { handle ->
        val dao = handle.attach(ItemStockDao::class.java) // here JDBI provided implementation for the DAO
        val itemStock = dao.selectStockAmount(itemId.asAppId()) // here we select (and aggregate) data from DB 
            ?: ItemStockRec(itemId.asAppId().id, BigDecimal.ZERO, 0) // and here we provide default data in case there's nothing in DB yet
        // and below we do make the mapping from Rec structures into domain data structures
        return@withHandle ItemStock(
            id = ItemStockAppId(itemId.asAppId(), itemStock.serial), 
            amount = itemStock.amount) 
    }
    // ...
}
```

### Web
Here we place web servers (usually exposing REST interface that may be used by clients or other systems). Currently, our example provides two client applications: one with Springboot based REST server and one with Springboot based Webflux reactive server.

![Product planner backend](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/product-planner-backend.puml)

### Client
In this specialized area we place all clients to the application including graphical clients, web clients and CLI. In this example we have an Angular client implementation.
