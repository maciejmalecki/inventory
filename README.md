# Inventory - an example hexagonal architecture

[![CircleCI](https://circleci.com/gh/maciejmalecki/inventory.svg?style=shield)](https://circleci.com/gh/maciejmalecki/inventory)
[![CircleCI](https://circleci.com/gh/maciejmalecki/inventory/tree/develop.svg?style=shield)](https://circleci.com/gh/maciejmalecki/inventory/tree/develop)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Preface
This is a practical example of hexagonal architecture. The goals of this little project are to identify and describe patters useful with this kind of architectures. I believe that CQRS (Command Query Responsibility Segregation) is the easiest form of hexagonal architecture to implement, therefore my approach uses this paradigm.

The project is organized in source modules, these fall into three major categories:
* `domain`
* `app`lication
* `infra`structure

![General concepts](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/general.puml)

The category to which given module belongs determines dependencies that are allowed for that module. The categories in fact form an onion-like structure, where domain modules are located in the centre and infrastructure modules belong to the most outer layer. Dependencies always point inwards.

## Domain
The domain area is where we model business logic, the most critical enterprise business rules and data structures. In general, this is a digital model of real business that is usually well establish since years or decades. We do not expect these rules to change rapidly, but rather evolve slowly. It is then worth of investing, to get the cleanest and more correct implementation. In ideal world, this layer should survive he most intense technological revolutions.

### Subdomains
t.b.d.

### Business components
t.b.d.

### Entities and aggregates
Entities are classes of persistent objects. Aggregates are complex objects treated as a whole. Because of CQRS approach, I model entities and aggregates as immutable objects. Their state is obtained via selectors, but modifications are performed only via mutators. Because in this approach entities and aggregates are immutable, they cannot have any state mutating methods. That is, the only methods of such aggregates are querying methods. Instead of state mutating methods we use external objects, so-called Behaviors.

### Selectors and mutators
Selectors and mutators form so-called repositories, that is objects able to create, materialize and modify entities and aggregates. I have introduced selectors and mutators split to reflect CQRS better.

Selectors are exclusively used for querying data. This way, for given aggregate, if we depend only on a selector, we are certainly not modifying this aggregate. In a term of ports&adapters, selectors are ports that are modelled as interfaces suffixed with `Selector`. 

Mutators are exclusively used for data modifications. This way, for given aggregate, if we depend only on a mutator, we are doing modifications. In terms of ports&adapters, mutators are ports that are modelled as interfaces suffixed with `Mutator`.

### Behaviors
Behavior is a class that is usually bound with one or more entities (or aggregates) and has at least one dependency to a mutator. It can depend on multiple mutators as well as on selectors.

![Domain of items](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/domain/items.puml)

## Dependency model
![Dependencies for packages](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/packages.puml)
![Dependencies for classes](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/zerodependency.puml)

## Application use cases
![Use cases](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/usecases.puml)
