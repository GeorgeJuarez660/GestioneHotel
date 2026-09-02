# Immagine di partenza: contiene Maven 3.9.16 e il JDK 26 (Temurin)
FROM maven:3.9.16-eclipse-temurin-26

# Cartella di lavoro dentro il container: tutti i comandi sotto partono da qui
WORKDIR /app

# Copio il pom.xml (elenco dipendenze e configurazione del progetto)
COPY pom.xml .

# Copio il codice sorgente nella cartella /app/src
COPY src ./src

# Compilo il progetto e creo il jar in /app/target
# -DskipTests salta i test per velocizzare la build
RUN mvn clean package -DskipTests

# Dichiaro che l'app usa la porta 8081 (informativo: la porta va comunque
# mappata con -p 8081:8081 al momento del run)
EXPOSE 8081

# Comando che viene eseguito all'avvio del container
ENTRYPOINT ["java", "-jar", "target/Hotel.jar"]