# Store API

Store API — это простое RESTful веб-приложение, разработанное на Spring Boot, которое предоставляет эндпоинты для управления аккаунтами пользователей, продуктами, категориями и заказами в магазине.

## Функциональность

### Управление аккаунтами
- Получение списка всех аккаунтов.
- Получение аккаунта по ID.
- Получение аккаунта по никнейму.
- Создание нового аккаунта.
- Обновление существующего аккаунта.
- Удаление аккаунта по ID.

### Управление продуктами
- Получение списка всех продуктов.
- Получение продукта по ID.
- Получение продуктов с фильтрацией по категории и/или цене.
- Создание нового продукта.
- Обновление существующего продукта.
- Удаление продукта по ID.

### Управление категориями
- Получение списка всех категорий.
- Получение категории по ID.
- Создание новой категории.
- Обновление существующей категории.
- Удаление категории по ID.

### Управление заказами
- Получение списка всех заказов.
- Получение заказа по ID.
- Получение заказов по ID аккаунта.
- Создание нового заказа.
- Обновление существующего заказа.
- Удаление заказа по ID.

## Технологии
- Java 17
- Spring Boot
- Lombok
- Maven
- MySQL
- JPA (Hibernate)
- REST API

## Как запустить проект
1. Убедитесь, что у вас установлены Java 17 и Maven.
2. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/B1M0r/Store.git