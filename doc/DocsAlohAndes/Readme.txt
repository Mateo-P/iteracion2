Los pasos a seguir para poder desplegar la aplicacion son los siguientes:
1. Abrir el archivo .XML con un editor de texto y poner en los espacios vacios la credenciales de usuario y constraseña.
2. Tomar el archivo TablasFinales.sql y ejecutarlo en su defecto en SQLDeveloper.
para su ejecucion se recomienda:
	2.1 Ejecutar la instrucciones de crear las tablas Operador, Cliente, Pago y asi sucesivamente(OJO no ejecutar los ALTER table aun)
	2.2 Ejecutar cada uno de los ALTER table para asi garantizar las reglas de negocio y restricciones.
3. Poblar Las tablas en el orden dado ya que asi se cumpliran las restricciones y dejará ingresar los datos sin problema.
4. Para los requerimientos de consulta que se encuentran al final del archivo primero tuvo que haber cumplido con todo lo anterior.
5 Para el bono es necesario modificar los parametros y aspectos que va a tener en cuenta a la hora de querer filtrarlos
	por ejemplo: el rango de fechas y servicios adicionales