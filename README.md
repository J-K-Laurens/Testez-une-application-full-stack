# Yoga App - Application Full-Stack

Application de gestion de sessions de yoga avec authentification JWT.

---

## Prérequis

- **Java** 11+
- **Maven** 3.6+
- **Node.js** 16+ et **npm**
- **MySQL** 8.0+
- **Angular CLI** (optionnel)

---

## 1. Installation de la base de données

### Créer la base de données MySQL

```bash
mysql -u root -p
```

```sql
CREATE DATABASE yoga_app;
USE yoga_app;
SOURCE ressources/sql/script.sql;
```

### Compte admin par défaut

- **Email** : `yoga@studio.com`
- **Mot de passe** : `test!1234`

---

## 2. Installation de l'application

### Backend (Spring Boot)

```bash
cd back
mvn clean install -DskipTests
```

### Frontend (Angular)

```bash
cd front
npm install
```

---

## 3. Lancement de l'application

### Démarrer le Backend

```bash
cd back
mvn spring-boot:run
```

> API disponible sur : `http://localhost:8080`

### Démarrer le Frontend

```bash
cd front
npm run start
```

> Application disponible sur : `http://localhost:4200`

---

## 4. Lancer les tests

### Backend

```bash
cd back
mvn test
```

### Frontend - Tests unitaires (Jest)

```bash
cd front
npm run test
```

Mode watch :

```bash
npm run test:watch
```

### Frontend - Tests E2E (Cypress)

```bash
cd front
npm run e2e
```

---

## 5. Générer les rapports de couverture

### Backend (JaCoCo)

```bash
cd back
mvn clean test
```

📄 **Rapport** : `back/target/site/jacoco/index.html`

### Frontend - Tests unitaires (Jest)

```bash
cd front
npm run test -- --coverage
```

📄 **Rapport** : `front/coverage/jest/lcov-report/index.html`

### Frontend - Tests E2E (Cypress)

```bash
cd front
npm run e2e:ci
npm run e2e:coverage
```

📄 **Rapport** : `front/coverage/lcov-report/index.html`

---

## Structure du projet

```
├── back/                   # Backend Spring Boot
│   ├── src/main/           # Code source
│   ├── src/test/           # Tests unitaires et intégration
│   └── pom.xml
├── front/                  # Frontend Angular
│   ├── src/                # Code source
│   ├── cypress/e2e/        # Tests E2E
│   └── package.json
└── ressources/
    ├── postman/            # Collection Postman
    └── sql/                # Script SQL
```

---

## Ressources

- **Collection Postman** : `ressources/postman/yoga.postman_collection.json`
- **Script SQL** : `ressources/sql/script.sql`

