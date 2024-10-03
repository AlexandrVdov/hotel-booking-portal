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

## Запуск

### Требования

Проект использует синтаксис Java 17. Для локального запуска вам потребуется
установленный JDK 17.

### Используя среду разработки IDEA

Откройте проект. Дождитесь индексации. Создайте конфигурацию запуска
или запустите main метод класса [app/src/main/java/com/example/hotel_booking_portal/HotelBookingPortalApplication.java](app/src/main/java/com/example/hotel_booking_portal/HotelBookingPortalApplication.java)

### Используя Docker

Соберите проект - запустите в gradle Task bootJar

Сборка контейнеров
```shell
cd docker
docker compose up --build
```