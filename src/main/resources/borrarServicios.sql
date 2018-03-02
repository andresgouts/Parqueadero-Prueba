DELETE FROM factura;
DELETE FROM servicio;

DELETE FROM tarifa;
INSERT INTO tarifa (tipo_vehiculo, valor_dia, valor_hora) values ('carro',8000,1000); 
INSERT INTO tarifa (tipo_vehiculo, valor_dia, valor_hora) values ('moto',4000,500); 