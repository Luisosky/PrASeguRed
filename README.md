```markdown
# Guia para usar el proyecto

Hola muachos, esto es solo que tienen que hacer por si se nos olvida en algun momento el como hacer funcionar el proyecto en un maquina

## 1. Project Setup

### Requisitos
- **Java 21**
- **Gradle**
- **MongoDB**: Tenerlo instalado local o por Docker deberia de funcionar tambien

### Clonar el repositorio
Clonarlo en la maquina local:
```bash
git clone https://github.com/Luisosky/PASeguRed.git
```


### Environment Config
- Verificar que `application.properties` este en `src/main/resources` Sobre todo debe estar esta linea:
  ```properties
  spring.data.mongodb.uri=mongodb://localhost:27017/PASeguRed
  ```
- Lo podemos cambiar en cualquier momento creo xd

## 2. Runnear

Para runnear la app se puede usar este comando en la terminal:
```bash
./gradlew bootRun
```
Tiene que estar seguro que MongoDB esta ejecutado como servicio (Lo puede ver en Services en Windows). Si no lo esta, Use docker lol.

## 3. Runnear Testeos

Es facil de usar el Test solo use este comando:
```bash
./gradlew test
```
Springboot va a conectarse con la base de datos y va a crear una carpeta para poder almacenar los usuarios test por ahora es muy rudimentario pero se va a cambiar cuando usemos REST

## 4. POSIBLES ERRORES

Si ve algo como:
```
connect ECONNREFUSED 127.0.0.1:27017
connect ECONNREFUSED ::1:27017
```
Es probable que la app no se pueda conectar a MongoDB. Proceda con:
- **Verifique que MongoDB si esta runneando en la maquina local:**  
  - Para linux (Cristian), use `sudo systemctl status mongod` y le da "start" con `sudo systemctl start mongod`.
  - Para macOS, si lo tiene instalado por Homebrew, use `brew services start mongodb-community`.
  - Para Windows, instale MongoDB o fuerce el start en services.
  
- **Para usar Docker:** Si no le da la gana de instalarlo puede usar docker desktop:
  ```bash
  docker run -d --name mongodb -p 27017:27017 mongo:latest
  ```


## 5. Catedra 

### Code Style
- Sea claro en los commits.
- Incluir documntacion y comentarios (xd).

### Branching Strategy
- Por cada implementacion use feature/nombre-rama.
- Hacer los pull request a la rama de desarrollo (`develop` o `main`) El de nosotros es main.

## 6. Ayudaaaaaaaaaa

Si hay otro error que no este descrito:
- Vaya a [MongoDB Installation & Troubleshooting Guide](README-mongodb-installation.md).

Vamoh a meterle duro
```
```
