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
INSERT INTO Attribute_Type_Values (attribute_type_name, code, value) VALUES ('resistorCaseStyle', 'al', 'Axial Leaded');
INSERT INTO Attribute_Type_Values (attribute_type_name, code, value) VALUES ('resistorCaseStyle', 'rl', 'Radial Leaded');

-- Item Classes
INSERT INTO Item_Classes (name, version, complete, description, unit) VALUES ('resistor', 1, TRUE, 'Resistor', 'pcs');
INSERT INTO Item_Classes_Version_Counters (name, last_version) VALUES ('resistor', 1);
INSERT INTO Attributes (item_class_name, item_class_version, attribute_type) VALUES ('resistor', 1, 'resistance');
INSERT INTO Attributes (item_class_name, item_class_version, attribute_type) VALUES ('resistor', 1, 'resistanceTolerance');
INSERT INTO Attributes (item_class_name, item_class_version, attribute_type) VALUES ('resistor', 1, 'powerRating');
INSERT INTO Attributes (item_class_name, item_class_version, attribute_type) VALUES ('resistor', 1, 'voltageRating');
INSERT INTO Attributes (item_class_name, item_class_version, attribute_type) VALUES ('resistor', 1, 'resistorCaseStyle');

-- Items
INSERT INTO Items (name, item_class_name, item_class_version) VALUES ('tht10K', 'resistor', 1);
INSERT INTO Scalar_Values (item_name, attribute_type, item_class_name, item_class_version, value, scale) VALUES ('tht10K', 'resistance', 'resistor', 1, 10, 3);
INSERT INTO Scalar_Values (item_name, attribute_type, item_class_name, item_class_version, value, scale) VALUES ('tht10K', 'resistanceTolerance', 'resistor', 1, 1, 1);
INSERT INTO Scalar_Values (item_name, attribute_type, item_class_name, item_class_version, value, scale) VALUES ('tht10K', 'powerRating', 'resistor', 1, 0.25, 1);
INSERT INTO Scalar_Values (item_name, attribute_type, item_class_name, item_class_version, value, scale) VALUES ('tht10K', 'voltageRating', 'resistor', 1, 250, 1);
INSERT INTO Dictionary_Values (item_name, attribute_type, item_class_name, item_class_version, attribute_type_name, code) VALUES ('tht10K', 'resistorCaseStyle', 'resistor', 1, 'resistorCaseStyle', 'al');