DELETE FROM factura;
DELETE FROM servicio;

DELETE FROM tarifa;
INSERT INTO tarifa (tipo_vehiculo, valor_dia, valor_hora) values ('c',8000,1000); 
INSERT INTO tarifa (tipo_vehiculo, valor_dia, valor_hora) values ('m',4000,500); 