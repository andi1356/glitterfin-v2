# Glitterfin v2

Glitterfin v2 is a Spring Boot 3.5 finance tracker that combines a polished web UI with automation tools for categorising expenses that arrive through both the browser and an authenticated REST API. The application persists data in PostgreSQL, renders views with Thymeleaf, and stores uploaded receipt images on disk for later review.

## Features

### Expense workspace
* **Dashboard & authentication** – Secure form-login gates the UI and greets the current user on the landing page once authenticated. 【F:src/main/java/arobu/glitterfinv2/configuration/security/SecurityFilterChainsConfiguration.java†L42-L61】【F:src/main/resources/templates/index.html†L14-L21】
* **Expense table tools** – The `/expenses` page lists every entry for the signed-in owner, offering inline search filters, sortable columns, detail links, and shortcuts to create, edit, or delete entries. 【F:src/main/java/arobu/glitterfinv2/controller/ui/ExpenseViewController.java†L25-L109】【F:src/main/resources/templates/expenses.html†L17-L113】
* **Rich expense form** – Expense creation and editing support timestamps with timezone handling, merchant/source metadata, optional categorisation, receipt uploads, geo-coordinates, and shared/outlier flags. The service transparently stores Base64 encoded receipts to `data/receipts`. 【F:src/main/java/arobu/glitterfinv2/controller/ui/ExpenseViewController.java†L35-L98】【F:src/main/java/arobu/glitterfinv2/service/ExpenseEntryService.java†L56-L132】

### Automation rules
* **Conditions & rules management** – Dedicated screens let power users compose matching conditions and priority-ordered rules that populate fields such as category, description, details, or boolean flags. 【F:src/main/java/arobu/glitterfinv2/controller/ui/RulesetViewController.java†L24-L146】【F:src/main/resources/templates/conditions.html†L1-L160】【F:src/main/resources/templates/rules.html†L1-L160】
* **Automatic enrichment with auditing** – Whenever an API expense is saved, matching conditions are resolved and their rules applied in priority order; each application is captured in the audit table for traceability. 【F:src/main/java/arobu/glitterfinv2/service/ExpenseRulesetService.java†L23-L63】【F:src/main/java/arobu/glitterfinv2/model/entity/ExpenseRulesetAudit.java†L15-L64】

### API ingestion
* **Token-protected endpoint** – `/api/expenses` accepts JSON payloads, requiring both an `X-API-KEY` header and a matching user agent identifier. Requests are authenticated against stored owner credentials and rejected when headers are missing or invalid. 【F:src/main/java/arobu/glitterfinv2/controller/api/ExpenseEntryController.java†L24-L60】【F:src/main/java/arobu/glitterfinv2/configuration/security/api/ApiTokenFilter.java†L32-L86】
* **DTO contract** – API callers can supply amount, timestamp, merchant/source metadata, geo-coordinates, categorisation, Base64 receipts, and boolean flags that align with the enrichment workflow. 【F:src/main/java/arobu/glitterfinv2/model/dto/ExpenseEntryApiPostDTO.java†L7-L47】

## Demo account
A demo user is created automatically on startup so reviewers can explore the UI and API without additional setup. The initializer keeps credentials consistent across runs. 【F:src/main/java/arobu/glitterfinv2/configuration/DemoAccountInitializer.java†L18-L80】【F:src/main/java/arobu/glitterfinv2/model/entity/Owner.java†L16-L61】

| Usage | Value |
| --- | --- |
| Email / username | `demo@glitterfin.app` |
| Password | `DemoPassword123!` |
| API token | `demo-api-token` (send in `X-API-KEY`) |
| User agent ID | `GlitterfinDemoClient` (send as the first token in the `User-Agent` header) |

Example API call:

```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: demo-api-token" \
  -H "User-Agent: GlitterfinDemoClient" \
  -d '{
        "amount": 19.99,
        "timestamp": "2024-05-01T12:30:00Z",
        "source": "Card",
        "merchant": "Coffee Shop",
        "category": "Food",
        "shared": false,
        "outlier": false
      }'
```

## Running the project locally

### Prerequisites
* Java 21 and Maven 3. 【F:pom.xml†L5-L32】
* Docker (optional) for running PostgreSQL via Compose. 【F:compose.yaml†L33-L80】

### 1. Configure environment
Create a `.env.dev` file in the project root with the variables used by the dev profile:

```ini
DB_NAME=glitterfin
DB_USER=glitterfin
DB_PASSWORD=glitterfin
DB_PORT=5432
REQUIRED_PROFILE=dev
SPRING_PROFILES_ACTIVE=dev
AWS_ACCESS_KEY=dummy
AWS_SECRET_KEY=dummy
AWS_REGION=us-east-1
AWS_SES_FROM_EMAIL=dummy@example.com
GEOCODE_API_TOKEN=dummy
```

These values satisfy the datasource and external configuration placeholders declared in `application-dev.properties` and `application.properties`. 【F:src/main/resources/application-dev.properties†L1-L11】【F:src/main/resources/application.properties†L1-L24】

Ensure the `data/receipts` directory exists so file uploads can be persisted:

```bash
mkdir -p data/receipts
```

### 2. Start PostgreSQL
Spin up the bundled development database with Docker Compose:

```bash
docker compose --profile dev up -d
```

The `app-dev` service reuses the same profile if you prefer to run the application inside Docker. 【F:compose.yaml†L52-L78】

### 3. Run the Spring Boot application
From the project root, launch the app with the dev profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Alternatively, build and run the packaged JAR:

```bash
mvn clean package
java -jar target/glitterfin-v2-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

The UI will be available at `http://localhost:8080`. Log in with the demo credentials above to explore expenses, rules, and audit history.

### 4. Running tests
Execute the test suite with Maven:

```bash
mvn test
```

## Repository structure
* `src/main/java` – Spring components, including MVC controllers, services, security configuration, and the demo account initializer.
* `src/main/resources/templates` – Thymeleaf templates for the authenticated UI and forms.
* `src/main/resources/db/changelog` – Liquibase migrations for the PostgreSQL schema.
* `compose.yaml` – Docker Compose profiles for dev and prod deployments.

## Next steps
Potential enhancements include extending the audit UI, surfacing API ingestion metrics via the existing Actuator endpoint, and wiring the email service to notify owners when automation rules execute.
