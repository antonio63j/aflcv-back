



## DEPLOY

### Ubuntu 20.04

- instalar openjdk8

- instalar postgresql (version 12)

- generar el jar del proyecto y copiar en /home/antonio/www/aflcv-back/app.jar

- en /home/antonio/www/aflcv-back, crear uploads/imagenes/ y cargar las imagenes necesarias

- en /home/antonio/www/aflcv-back, crear downloads, necesario para que la app coloque el cv para su descarga

- generar el servicio systemclt, para ello creamos el archivo etc/systemd/system/aflcv-service.service:

```
  [Unit]
  Description=Java aflcv Service
  
  [Service]
  User=antonio
  
  WorkingDirectory=/home/antonio/www/aflcv-back
  ExecStart=java -jar /home/antonio/www/aflcv-back/app.jar
  
  [Install]
  WantedBy=multi-user.target
```

  para arrancar el servicio:

```
  sudo systemctl enable aflcv-service
  sudo systemctl start aflcv-service
```

  para el arranque con el boot:

```
  sudo systemctl deamon-reload
```

  

  

## JPA

### ManyToMany con @JoinTable

#### Las entidades son proyecto y herramienta. En la entidad proyecto se define la tabla join así:

```
	@ManyToMany
	@JoinTable(
			  name = "proyecto_herramienta", 
			  joinColumns = @JoinColumn(name = "proyecto_id"), 
			  inverseJoinColumns = @JoinColumn(name = "herramienta_id"),
			  uniqueConstraints = @UniqueConstraint(columnNames={"proyecto_id", "herramienta_id"})
			  )

	private List<Herramienta> herramientas;
```

Definiendo clave única con el par "proyecto_id" y "herramienta_id, eliminamos el error de duplicidad en dicha tabla, que podría producirse con una actualización del tipo: 

       {
            "nombre": "nombre44",
            "empresa": "ahorro corporación",
            "cliente": "ahorro corporación",
            "inicio": "1999-08-01",
            "fin": "2011-06-30",
            "sectorCliente": "Servicios financieros",
            "descripcion": "experiencias",
            "herramientas": [
        		{"id": "4"},
        		{"id": "4"}
            ]     
        }


La clase Herramienta se ha definido así:

    @JsonIgnore
    @ManyToMany (mappedBy = "herramientas")
    private List<Proyecto> proyectos;


Definidas las entidades de esta manera:

- No permite eliminar un herramienta si está presente en la tabla proyecto-herramienta, es decir, si es utilizada en algún proyecto.

- La eliminación de un proyecto, no elimina elimina ninguna herramienta, aún en el caso de que una herramienta esté siendo utilizada en un único proyecto.

- En una actulización de proyecto, no es posible actulizar los campos de las Herramienta que utiliza dicho proyecto. En la actulización de las herramientas de un proyecto, spring solo tendrá en cuenta la lista de "Id´s" o claves de herramientas. En una única actualización de proyecto (PUT), deberán indicarse todas las herramientas del proyecto:

         {
              "nombre": "nombre44",
              "empresa": "ahorro corporación",
              "cliente": "ahorro corporación",
              "inicio": "1999-08-01",
              "fin": "2011-06-30",
              "sectorCliente": "Servicios financieros",
              "descripcion": "experiencias",
              "herramientas": [
          		{"id": "3"},
          		{"id": "4"}
              ]
          }


