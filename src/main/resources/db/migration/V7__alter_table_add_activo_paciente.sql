ALTER TABLE pacientes add activo tinyInt;
UPDATE pacientes set pacientes.activo = 1