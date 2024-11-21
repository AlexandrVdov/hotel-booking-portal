# REST API для сервиса бронирования отелей

Приложение позволяет пользователям возможность забронировать понравившийся
отель на определённый период. Также реализован поиск отелей по критериям и рейтингам и систему
выставления оценок в приложении (от 1 до 5).
Сервис должен позволять администраторам выгружать статистику по работе в формате
CSV-файла

## Используемые технологии

- Spring 3
- Gradle 8.5
- Spring Web MVC
- Spring Data
- Lombok
- MapStruct
- PostgreSQL
- Spring Security
- Kafka
- MongoDB

## Запуск

### Требования

Проект использует синтаксис Java 18. Для локального запуска вам потребуется
установленный JDK 18.

### Подготовка Docker

Соберите проекты - запустите в gradle Task bootJar для сервисов booking-service и statistic-service

Сборка контейнеров (kafka, бд и сервисы)
```shell
cd docker
docker compose up --build
```

### Базовая защита

Настроена Basic-аутентификация. Проверка на роль ROLE_ADMIN, ROLE_USER.

## Обрабатываемые команды

Реализованы REST API для управления сущностями user, booking, hotel, room.

Например, получить комнату по id
```text
GET /api/v1/room/{id}
```
Со всем списком можно ознакомится через swagger
```text
/swagger-ui/index.html
```
или загрузить hotels_REST_API_basics.json в шаблон в POSTMAN из корня проекта

### Особые заголовки и параметры запросов

Позволяет менять рейтинг отеля (по шкале оценивания от 1 до 5).
```text
PUT /api/v1/hotel/4/rating?newMark=3
```
Отдающий постраничную информацию об отелях с учётом фильтрации от пользователя
```text
GET /api/v1/hotel/filter?pageSize=5&pageNumber=0&city=City
```
Отдающий постраничную информацию о комнатах с учётом фильтрации от пользователя
```text
GET /api/v1/room/filter?pageSize=5&pageNumber=0&checkInDate=2024-10-19&checkOutDate=2024-10-29
```
Реализован сервис с методом выгрузки статистических данных в CSV-файл.
```text
GET http://localhost:8082/api/v1/statistic/download
```