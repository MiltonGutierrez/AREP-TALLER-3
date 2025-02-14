### Escuela Colombiana de Ingeniería

### Arquitectura Empresarial - AREP

#  TALLER MICROFRAMEWORKS

En este taller construí un servidor web en Java, similar a Apache, que fue capaz de entregar páginas HTML e imágenes en formato PNG. Además, desarrollé un framework IoC que permitió la construcción de aplicaciones web a partir de POJOs.

Utilizando este servidor, creé una aplicación web de ejemplo para demostrar su funcionamiento. Es importante destacar que el servidor atendió múltiples solicitudes, aunque no de manera concurrente. Como parte del desarrollo, se implementó un prototipo mínimo que evidenció las capacidades reflexivas de Java. Este prototipo permitió, al menos, la carga de un bean (POJO) y la generación de una aplicación web basada en él.

## Empezando

Estas instrucciones te permitirán obtener una copia del proyecto y ejecutarlo en tu máquina local para propósitos de desarrollo y pruebas.

### Prerequisitos

- Java 21 preferiblemente.
- Maven 3.x
- Acceso a una terminal.

### Instalando

Pasos para configurar el entorno de desarrollo:

1. Clona el repositorio del proyecto:

   ```bash
   git clone https://github.com/MiltonGutierrez/AREP-TALLER-3.git
   cd AREP-TALLER-3
   ```

2. Compila el proyecto usando Maven:

   ```bash
   mvn clean compile
   ```

3. Ejecuta el servidor:

   ```bash
   java -cp target/classes edu.escuelaing.arep.taller3.App
   ```

4. Accede al servidor desde tu navegador en [http://localhost:8080](http://localhost:8080).

## Arquitectura

El siguiente diagrama de componentes describe la estructura básica de la aplicación, basada en el patrón **MVC (Modelo-Vista-Controlador)**:

![Componentes Arep](https://github.com/user-attachments/assets/fe00a142-d398-4d1b-a64e-62050092adec)

### Componentes Principales
1. **Browse**:
   - **Puerto 8080**: Punto de entrada para las solicitudes HTTP.

2. **NoteApplication (Lógica de Negocio - Modelo - Http - Server)**:
   - **Controller**: Procesa las peticiones del endpoint de /app, valida datos y coordina la interacción entre el servidor y los servicios.
   - **Services**: Implementan la lógica para operaciones CRUD de notas (crear, leer).
   - **Model**: Define la estructura de datos.
   - **Http**: Es un componente que contiene una implementación propia para representar las clases HttpRequest y HttpResponse.
   - **App**: indica al servidor la ubicacion de los archivos estaticos, y mediante funciones lambda se crean unos servicios get y post del controller, adicionalmente inicia el servicio spring.
   - **Server**: Contiene el servidor Http, MicroSpring y las anotaciones
  
### Flujo de la Aplicación:
1. El navegador envía solicitudes al **HttpServer** (puerto 8080, mediante el inicio del server en App) este procesa la peticion de archivos HTML, CSS, JS e imagenes..
2. El **Controller** recibe las solicitudes del endpoint /app, valida los parámetros y delega la lógica a los `Services` creando al final la respuesta.
3. El **MicroSpring** carga las clases con la anotación @RestController y los metodos, de manera que reciba las solicitudes al endpoint /spring *(en este caso solo /hello)*.
4. Los **Services** interactúan con el **Model** para acceder a la estructura de datos de modo que pueda responder a la petición..

### Diagrama de Clases y Explicación.
Se presentara el diagrama de clases que describe los métodos y las dependencias entre las clases existentes para cada componente del backend.

![Clases AREP](https://github.com/user-attachments/assets/a151df7e-237a-4f67-ab71-d1dd123c4050)

#### Clases Principales:
1. **Clase** `HttpServer`:
   - **Responsabilidad**: Núcleo del servidor web. Escucha en el puerto definido (`PORT`), gestiona conexiones entrantes y delega solicitudes.
   - **Atributos Clave**:
     - `PORT`: Puerto de escucha (ej: *8080*).
     - `WEB_ROOT`: Ruta de archivos estáticos (HTML, CSS, JS).
     - `noteController`: Controlador para peticiones con el endpoint /app/**.
   - **Métodos Destacados**:
     - `runServer()`: Inicia el servidor y acepta conexiones.
     - `handleRequests()`: Dirige solicitudes a métodos específicos (GET/POST) .
     - `handleGetRequests()`: Retorna archivos estáticos que se encuentran en el webroot del servidor (ej: *notes.html*).
     - `handleSpringRequests`: Dirige las solicitudes del enpoint /spring.
     - `handleAppGetRequests()`: Maneja las solicitudes *GET* realizadas al endpoint /app/** de manera que utiliza la función lamda implementada en el controlador para poder obtener el recurso.
     - `handleAppPostRequests()`:Maneja las solicitudes *POST* realizadas al endpoint /app/** de manera que utiliza la función lamda implementada en el controlador para poder realizar la petición.
       
2. **Clase** `MicroSpring`:
   - **Responsabilidad**: Núcleo del microframework basado en spring, utiliza la clase `ClassFileScanner` para obtener el listado de clases con la anotación @RestController, de manera que pueda obtener los metodos con la       anotación @GetMapping, y guardarlos en un Map<String, Method>, adicionalmente procesa las solicitudes del endpoint /spring.
   
3. **Controladores**:
   - **Clase `NoteControllerImpl`**:
     - Acceder a la lógica de negocio.
     - Define los metodos get() y post() que permite al programador definir las rutas mediante el uso de funciones lambda.
     - **Dependencia**: `NoteServices` (inyección de servicios).
   - **Clase `GreetingController`**:
     - Utiliza la anotacion @RestController, y por el momento tiene un metodo con @GetMapping("/spring/hello") de manera que pide un parametro con @RequestParam, esto para poder devolver un saludo.
4. **Servicios**:
   - **Interfaz `NoteServices`**:
     - Define operaciones como `addNote()` y `getNotes()`.
   - **Clase `NoteServicesImpl`**:
     - Implementa la interfaz y gestiona una lista de notas (`notes`).
     - **Atributo**: `notes` (almacenamiento temporal en memoria).
5. **Modelo**:
   - **Clase `Note`**:
     - Representa una nota con atributos: `title`, `group`, `content`, `date`.
     - **Nota**: `date` sugiere el uso de `LocalDate` para manejar fechas.
    
6. **Http**
   - **Clase `HttpRequest`**
      - Implementa la función *getQueryParams()* que permite obtener la lista de los querys en la petición.

#### Flujo de una Solicitud:
1. **Cliente → `HttpServer`**:  
   El navegador envía una solicitud (ej: `POST /app/notes` con parámetros).
2. **`HttpServer` → `NoteController`**:  
   El servidor detecta rutas bajo `/app` y delega al controlador.
   El servidor detecta rutas bajo `/spring` y delega al MicroSpring.
4. **`NoteController` → `NoteServices`**:  
   El controlador valida los datos y usa el servicio para agregar/retornar notas.
5. **Respuesta HTTP**:  
   - Éxito: `200 OK` con JSON de notas.  
   - Error: `400 Bad Request` con mensaje descriptivo (ej: parámetros inválidos).
   - 
### Validaciones y Pruebas:
- Se implementaron pruebas automatizadas con **JUnit** para validar solicitudes `GET`/`POST`, incluyendo manejo de errores (ej: parámetros inválidos devuelven código `400` y JSON con detalles).
- El cliente incluye validaciones frontend para evitar enviar datos incompletos.

## Correr las pruebas 

Para ejecutar las pruebas automatizadas del sistema:

```bash
mvn test
```

### Pruebas 

Estas pruebas validan el correcto funcionamiento de las funcionalidades principales del servidor. Especificamente las peticiones GET y POST hacia el recurso /app/note (para la realización de estas se utilizo JUnit Jupiter. Una muestra de estas pruebas es: 
```java
    @Test
    void testGetNotesResponseShouldReturnEmptyArray() {
        String responseByController = noteController.getServices("/app/note").apply(null, null);
        String responseThatShouldReturn = "[" + "]";
        assertEquals(responseByController, responseThatShouldReturn);
    }
```
# Descripción de las Pruebas

## 1. testGetNotesResponseShouldReturnEmptyArray
**Propósito:**  
Verificar que el método `getNotes()` retorne un Array vacio..

**Qué prueba:**
- Cuerpo de la respuesta como un arreglo JSON vacío (`[]`).

## 2. testGetNotesResponseShouldReturnArrayWithCreatedNotes
**Propósito:**  
Validar que `getNotes()` retorne un JSON con todas las notas creadas, incluyendo sus metadatos.

**Qué prueba:**
- Formato correcto de los campos: `title`, `group`, `content`, `date`.
- Coherencia entre las notas añadidas y las mostradas.

## 3. testPostNoteResponseShouldHandleErrors
**Propósito:**  
Garantizar que el controlador maneje errores en solicitudes POST con parámetros inválidos.

**Qué prueba:**
- Respuestas 400 Bad Request para casos como:
  - Parámetros vacíos (`title=`, `group=`, `content=`).
  - Grupos no permitidos (ej: `group=hi`).
  - Mensajes de error claros en formato JSON (ej: `{"error": "Some parameters are empty"}`).

## 4. testPostNoteResponseShouldReturnNote
**Propósito:**  
Asegurar que una solicitud POST válida retorne la nota creada en formato JSON.

**Qué prueba:**
- Coincidencia exacta entre los campos enviados (`title`, `group`, `content`) y los devueltos.

## 5. shouldThrowNotesServicesExceptionSomeParametersAreEmpty
**Propósito:**  
Validar que el servicio rechace parámetros vacíos lanzando `NoteServicesException`.

**Qué prueba:**
- Escenarios como:
  - Todos los campos vacíos.
  - Campos parcialmente vacíos (`title=`, `group=work`, `content=`).

## 6. shouldThrowNotesServicesExceptionInvalidGroup
**Propósito:**  
Comprobar que el servicio solo permita grupos predefinidos (`personal` o `work`).

**Qué prueba:**
- Lanzamiento de excepciones para grupos no válidos (ej: `invalid`, `personal1`).

## 7. shouldAddNotes
**Propósito:**  
Confirmar que el servicio añade notas correctamente cuando los parámetros son válidos.

**Qué prueba:**
- Incremento del tamaño de la lista de notas después de agregar elementos.
- Ausencia de excepciones en casos válidos.

## 8. shouldSayHelloWorld
- Prueba que en caso de que el request no incluya el parametro "name" devuelva naturalmente *Hello World!*.

## 9. shouldSayHelloWithName
-Prueba que en caso de que el request incluya el parametro "name" devuelva *Hello 'name'!.

# Tecnologías Usadas en Pruebas
- **JUnit Jupiter 5:** Para pruebas unitarias y parametrizadas.
- **Maven:** Gestión de dependencias y ejecución de pruebas.

- **Resultado de las pruebas**
![image](https://github.com/user-attachments/assets/d2645602-e945-453b-8444-b100a1b6e2e1)


### Muestra de la ejecución

1. Acceso al aplicativo:
![image](https://github.com/user-attachments/assets/7942c3df-bb6f-479a-9a4c-a3da2f44ade8)
2. Ejemplo creación de la nota
![image](https://github.com/user-attachments/assets/a2da0022-34cf-48a0-a57e-5965e6db0b0b)
3. Ejemplo petición de notas creadas
![image](https://github.com/user-attachments/assets/63429968-5db6-44f2-9b22-b9d873899ce3)
4. Ejemplo añadir nota con datos incompletos (se realiza la validación desde el cliente por lo que no se ejecuta la petición)
![image](https://github.com/user-attachments/assets/35d2ef47-8986-4233-807f-ed11c2dd91f0)
5. Ejemplo petición al recurso /app/pi
![image](https://github.com/user-attachments/assets/da8ef5df-8767-4064-bf42-ee1e5f871c60)}
6. Ejemplo petición al recurso /app/hello (sin parámetro)
![image](https://github.com/user-attachments/assets/ce07a53f-bebf-4f5e-9592-b1d652eca69e)
7. Ejemplo petición al recurso /app/hello (con parámetro)
![image](https://github.com/user-attachments/assets/496746b9-499d-4a23-9886-1020df2fd422)

## Construido con.

- [Maven](https://maven.apache.org/) - Dependency Management

## Autores

- **Milton Andres Gutierrez Lopez** - *Initial work* - [MiltonGutierrez](https://github.com/MiltonGutierrez)

## Licencia

Este proyecto está licenciado bajo la Licencia GNU - mira el archivo [LICENSE.md](LICENSE.md) para más detalles.

