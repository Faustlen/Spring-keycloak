# Task Management System - Учебный проект

## Описание проекта

Это учебный проект для изучения интеграции Spring Boot с Keycloak и Spring Security. 
Проект демонстрирует основы настройки ролевой модели доступа с использованием Keycloak.

## Цель задания

Научиться интегрировать Keycloak с Spring Boot приложением и настроить ролевую модель доступа для REST API.

## Требования к реализации

1. **Создать три REST эндпоинта:**
   - `/api/employee` - доступен только роли EMPLOYEE
   - `/api/manager` - доступен только роли MANAGER  
   - `/api/director` - доступен только роли DIRECTOR
   
   Эндпоинты могут быть простыми - например, просто возвращать строку или выводить сообщение в консоль.

2. **Интегрировать Keycloak и Spring Security:**
   - Добавить необходимые зависимости в `pom.xml`
   - Настроить Spring Security для работы с Keycloak
   - Настроить проверку ролей на уровне методов

3. **Настроить Keycloak:**
   - Создать Realm для приложения
   - Создать Client в Keycloak
   - Настроить роли: DIRECTOR, MANAGER, EMPLOYEE
   - Создать трех тестовых пользователей с разными ролями

4. **Проверить работу:**
   - Получить токен для каждого пользователя
   - Проверить доступность эндпоинтов с разными ролями
   - Убедиться, что эндпоинты защищены и доступны только пользователям с соответствующей ролью

## Технологический стек

- Java 17
- Spring Boot 3.1.3
- Spring Security
- Keycloak 22.0.0
- Maven
- Docker

## Запуск проекта

### 1. Запуск Keycloak

Запустите Keycloak через docker-compose:
```bash
docker-compose up -d
```

Дождитесь полного запуска Keycloak (обычно 30-60 секунд).

### 2. Настройка Keycloak

#### 2.1. Вход в административную консоль

1. Откройте браузер: http://localhost:8080
2. Нажмите "Administration Console"
3. Войдите: `admin` / `admin`

#### 2.2. Создание Realm

1. Наведите на выпадающий список в левом верхнем углу (там "master")
2. Нажмите "Create Realm"
3. Имя Realm: `task-management-realm`
4. Нажмите "Create"

#### 2.3. Создание Client

1. В левом меню выберите "Clients"
2. Нажмите "Create client"
3. Client type: "OpenID Connect"
4. Нажмите "Next"
5. Client ID: `task-management-client`
6. Нажмите "Next"
7. Включите:
   - Client authentication: **ON**
   - Authorization: OFF
   - Authentication flow: OFF
8. Нажмите "Next"
9. Web origins: `http://localhost:8081`
10. Valid redirect URIs: `http://localhost:8081/*`
11. Нажмите "Save"

#### 2.4. Получение Client Secret

1. Перейдите на вкладку "Credentials"
2. Скопируйте значение "Client secret"
3. Вставьте в `src/main/resources/application.properties` в поле `keycloak.credentials.secret`

#### 2.5. Создание ролей

1. В левом меню выберите "Realm roles"
2. Нажмите "Create role"
3. Создайте три роли:
   - `DIRECTOR`
   - `MANAGER`
   - `EMPLOYEE`

#### 2.6. Настройка Client Roles

1. Перейдите в "Clients" → `task-management-client`
2. Перейдите на вкладку "Roles"
3. Создайте три роли (если их еще нет):
   - `DIRECTOR`
   - `MANAGER`
   - `EMPLOYEE`

#### 2.7. Создание пользователей

Для каждой роли создайте пользователя:

**Пользователь 1 (DIRECTOR):**
1. "Users" → "Create new user"
2. Username: `director`
3. Email: `director@example.com`
4. Email verified: ON
5. Нажмите "Create"
6. Вкладка "Credentials" → установите пароль (например: `password`)
7. Отключите "Temporary" → "Save"
8. Вкладка "Role mapping" → "Assign role"
9. "Filter by clients" → выберите `task-management-client`
10. Выберите роль `DIRECTOR` → "Assign"

**Пользователь 2 (MANAGER):**
- Username: `manager`
- Email: `manager@example.com`
- Роль: `MANAGER`
- Пароль: `password`

**Пользователь 3 (EMPLOYEE):**
- Username: `employee`
- Email: `employee@example.com`
- Роль: `EMPLOYEE`
- Пароль: `password`

### 3. Настройка приложения

Обновите `src/main/resources/application.properties`:
- Вставьте ваш Client Secret в поле `keycloak.credentials.secret`

### 4. Запуск Spring Boot приложения

```bash
mvn spring-boot:run
```

Приложение запустится на порту 8081.

## Тестирование

### Получение токена

```bash
curl -X POST http://localhost:8080/realms/task-management-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=employee" \
  -d "password=password" \
  -d "grant_type=password" \
  -d "client_id=task-management-client" \
  -d "client_secret=YOUR_CLIENT_SECRET"
```

Замените `YOUR_CLIENT_SECRET` на реальный секрет из Keycloak.

### Тестирование эндпоинтов

```bash
# Тест эндпоинта для EMPLOYEE
curl -X GET http://localhost:8081/api/employee \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"

# Тест эндпоинта для MANAGER
curl -X GET http://localhost:8081/api/manager \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"

# Тест эндпоинта для DIRECTOR
curl -X GET http://localhost:8081/api/director \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

Замените `YOUR_ACCESS_TOKEN` на токен из ответа предыдущего запроса.

## Структура проекта

- `controller` - REST контроллеры с эндпоинтами
- `config` - конфигурация Spring Security и Keycloak
