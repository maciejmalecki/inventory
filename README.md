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

## Domain
![Domain of items](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/domain/items.puml)

## Dependency model
![Dependencies for packages](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/packages.puml)
![Dependencies for classes](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/zerodependency.puml)

## Application use cases
![Use cases](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.github.com/maciejmalecki/inventory/develop/doc/dia/usecases.puml)
