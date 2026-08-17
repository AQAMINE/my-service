# ==========================================
# Étape 1 : Build de l'application avec Maven
# ==========================================
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copie uniquement le pom.xml et télécharge les dépendances (Cache Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copie les sources et génère le JAR (en ignorant les tests pour accélérer le build Docker)
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# Étape 2 : Image d'exécution légère (JRE 21)
# ==========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Création d'un utilisateur non-root pour des raisons de sécurité
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copie le JAR depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposition du port Spring Boot
EXPOSE 8081

# Lancement de l'application
ENTRYPOINT ["java", "-jar", "app.jar"]