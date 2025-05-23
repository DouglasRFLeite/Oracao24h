# 🙏 24h Prayer System

A serverless Java system to organize 24-hour prayer shifts in churches, with backend deployed via AWS Lambda and scalable architecture.

## 🚀 Technologies

- Java 17
- Spring Boot
- Spring Cloud Function
- AWS Lambda + Function URL
- Maven
- DynamoDB (NoSQL)
- GitHub Actions (CI/CD)

## 📦 Features

- Create prayer periods with dates and reasons
- Assign people to specific time slots
- View the full schedule of a period
- Ready-to-use structure for multiple churches
- Automatic deployment via GitHub Actions

## 🛠️ How to run locally

**1. Clone the repository**

```bash
git clone https://github.com/your-user/oracao24h.git
cd oracao24h/backend/oracao24h
```

**2. Run the tests**

```bash
mvn test
```

**3. Run the application locally with HTTP function:**

```bash
mvn spring-boot:run
```

## ☁️ Deployment

Deployment is automated via GitHub Actions every time the `main` branch receives a `push`.

- The generated `.jar` is uploaded to AWS Lambda.
- The function can be accessed via **Function URL** configured in AWS.

## 🧪 Tests

Unit tests are located in the `src/test/java` folder.  
They are automatically executed in the CI workflows (GitHub Actions).

## ✍️ Author

Developed by [Douglas Rocha](https://linkedin.com/in/douglas-rocha-leite)
