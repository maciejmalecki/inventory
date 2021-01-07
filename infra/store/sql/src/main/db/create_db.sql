CREATE TABLE Units
(
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE Attribute_Types
(
    name   VARCHAR(50) PRIMARY KEY,
    unit   VARCHAR(20),
    scalar BOOLEAN NOT NULL,
    FOREIGN KEY (unit) REFERENCES Units (code)
);

CREATE TABLE Attribute_Type_Values
(
    attribute_type_name VARCHAR(50)  NOT NULL,
    code                VARCHAR(50)  NOT NULL,
    value               VARCHAR(200) NOT NULL,
    PRIMARY KEY (attribute_type_name, code),
    FOREIGN KEY (attribute_type_name) REFERENCES Attribute_Types (name)
);

CREATE TABLE Item_Classes
(
    name        VARCHAR(50)  NOT NULL,
    version     BIGINT       NOT NULL,
    complete    BOOLEAN      NOT NULL,
    description VARCHAR(200) NOT NULL,
    unit        VARCHAR(20)  NOT NULL,
    PRIMARY KEY (name, version),
    FOREIGN KEY (unit) REFERENCES Units (code)
);

CREATE TABLE Item_Classes_Version_Counters
(
    name         VARCHAR(50) PRIMARY KEY,
    last_version BIGINT NOT NULL
);

CREATE TABLE Attributes
(
    item_class_name    VARCHAR(50) NOT NULL,
    item_class_version BIGINT      NOT NULL,
    attribute_type     VARCHAR(50) NOT NULL,
    PRIMARY KEY (item_class_name, item_class_version, attribute_type),
    FOREIGN KEY (item_class_name, item_class_version) REFERENCES Item_Classes (name, version) ON DELETE CASCADE,
    FOREIGN KEY (attribute_type) REFERENCES Attribute_Types (name)
);

CREATE TABLE Categories
(
    category_id BIGSERIAL PRIMARY KEY,
    code        VARCHAR(50)  NOT NULL,
    name        VARCHAR(200) NOT NULL
);

CREATE TABLE Items
(
    name               VARCHAR(50) PRIMARY KEY,
    item_class_name    VARCHAR(50) NOT NULL,
    item_class_version BIGINT      NOT NULL,
    FOREIGN KEY (item_class_name, item_class_version) REFERENCES Item_Classes (name, version)
);

CREATE TABLE Scalar_Values
(
    item_name          VARCHAR(50) NOT NULL,
    attribute_type     VARCHAR(50) NOT NULL,
    item_class_name    VARCHAR(50) NOT NULL,
    item_class_version BIGINT      NOT NULL,
    value              DECIMAL(10, 4),
    scale              DECIMAL(3)  NOT NULL,
    PRIMARY KEY (item_name, attribute_type, item_class_name),
    FOREIGN KEY (item_name) REFERENCES Items (name),
    FOREIGN KEY (attribute_type, item_class_name, item_class_version) REFERENCES Attributes (attribute_type, item_class_name, item_class_version)
);

CREATE TABLE Dictionary_Values
(
    item_name           VARCHAR(50) NOT NULL,
    attribute_type      VARCHAR(50) NOT NULL,
    item_class_name     VARCHAR(50) NOT NULL,
    item_class_version  BIGINT      NOT NULL,
    attribute_type_name VARCHAR(50) NOT NULL,
    code                VARCHAR(50),
    PRIMARY KEY (item_name, attribute_type, item_class_name),
    FOREIGN KEY (item_name) REFERENCES Items (name),
    FOREIGN KEY (attribute_type, item_class_name, item_class_version) REFERENCES Attributes (attribute_type, item_class_name, item_class_version),
    FOREIGN KEY (attribute_type_name, code) REFERENCES Attribute_Type_Values (attribute_type_name, code)
);

CREATE TABLE Categories_Tree_Path
(
    ancestor_id   BIGINT NOT NULL,
    descendant_id BIGINT NOT NULL,
    depth         INT    NOT NULL,
    PRIMARY KEY (ancestor_id, descendant_id),
    FOREIGN KEY (ancestor_id) REFERENCES Categories (category_id),
    FOREIGN KEY (descendant_id) REFERENCES Categories (category_id)
);