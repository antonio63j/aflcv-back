# ManyToMany con @JoinTable

Las entidades son proyecto y herramienta. En la entidad proyecto se define la tabla join así:

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

Definiendo clave única con el par "proyecto_id" y "herramienta_id, eliminamos el error de duplicidad en dicha tabla, que podría producirse con una actualizacion del tipo: 

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
  

