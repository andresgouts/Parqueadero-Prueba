INSERT INTO servicio (cilindraje, fecha_ingreso, placa, tipo_vehiculo, id_tarifa) VALUES (200, '20180227', 'ZJV11C', 'm', (select id_tarifa from tarifa where tipo_vehiculo = 'm'));