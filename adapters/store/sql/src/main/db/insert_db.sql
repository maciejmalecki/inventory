-- Units
INSERT INTO Units (code, name) VALUES ('s', 'second');
INSERT INTO Units (code, name) VALUES ('m', 'metre');
INSERT INTO Units (code, name) VALUES ('kg', 'kilogram');
INSERT INTO Units (code, name) VALUES ('A', 'ampere');
INSERT INTO Units (code, name) VALUES ('K', 'kelvin');
INSERT INTO Units (code, name) VALUES ('mol', 'mole');
INSERT INTO Units (code, name) VALUES ('cd', 'candela');

INSERT INTO Units (code, name) VALUES ('V', 'volt');
INSERT INTO Units (code, name) VALUES ('Ω', 'ohm');
INSERT INTO Units (code, name) VALUES ('F', 'farad');
INSERT INTO Units (code, name) VALUES ('Hz', 'hertz');
INSERT INTO Units (code, name) VALUES ('W', 'watt');

INSERT INTO Units (code, name) VALUES ('%', 'percentage');
INSERT INTO Units (code, name) VALUES ('pcs', 'piece');

-- Attribute Types
INSERT INTO Attribute_Types (name, unit, scalar) VALUES ('resistance', 'Ω', TRUE);
INSERT INTO Attribute_Types (name, unit, scalar) VALUES ('resistanceTolerance', '%', TRUE);
INSERT INTO Attribute_Types (name, unit, scalar) VALUES ('powerRating', 'W', TRUE);
INSERT INTO Attribute_Types (name, unit, scalar) VALUES ('voltageRating', 'V', TRUE);
INSERT INTO Attribute_Types (name, unit, scalar) VALUES ('resistorCaseStyle', NULL, FALSE);
INSERT INTO Attribute_Type_Values (attribute_type_name, value) VALUES ('resistorCaseStyle', 'Axial Leaded');
INSERT INTO Attribute_Type_Values (attribute_type_name, value) VALUES ('resistorCaseStyle', 'Radial Leaded');

-- Item Classes
INSERT INTO Item_Classes (name, description, unit) VALUES ('resistor', 'Resistor', 'pcs');
INSERT INTO Attributes (name, item_class_name, attribute_type) VALUES ('resistance', 'resistor', 'resistance');
INSERT INTO Attributes (name, item_class_name, attribute_type) VALUES ('resistanceTolerance', 'resistor', 'resistanceTolerance');
INSERT INTO Attributes (name, item_class_name, attribute_type) VALUES ('powerRating', 'resistor', 'powerRating');
INSERT INTO Attributes (name, item_class_name, attribute_type) VALUES ('voltageRating', 'resistor', 'voltageRating');
INSERT INTO Attributes (name, item_class_name, attribute_type) VALUES ('resistorCaseStyle', 'resistor', 'resistorCaseStyle');
