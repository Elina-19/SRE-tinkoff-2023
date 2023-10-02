# Домашние задания по SRE

---

## 1.1

Разработка подготовила новую версию приложения oncall v2.0.1:

```
~$ git clone -b v2.0.1 https://github.com/linkedin/oncall.git
```

При первоначальном запуске приложения, на этапе наполнения базы данных данными происходит ошибка:

```
~$ docker-compose up
...
DB successfully loaded /home/oncall/db/schema.v0.sql
Importing /home/oncall/db/dummy_data.sql...
mysql: [Warning] Using a password on the command line interface can be insecure.
ERROR 1136 (21S01) at line 16: Column count doesn't match value count at row 1
Ran into problems during DB bootstrap. oncall will likely not function correctly. mysql exit code: 1 for /home/oncall/db/dummy_data.sql
Failed to load dummy data.Wrote /home/oncall/db_initialized so we don't bootstrap db again
...
```

Вам как SRE нужно понять причину ошибки и описать рекомендацию по устранению

**Формат ответа**: в свободной форме, с описанием причины ошибки и рекомендации по устранению

---

Причина ошибки: количество столбцов в таблице 'team' (11 столбцов) больше, чем количество значений (10 значений), которые добавляются в эту таблицу на 16 строке файла dummy_data.sql

Рекомендация по устранению: добавить недостающее значение в скрипт вставки строки в таблицу 'team' на 16 строке файла dummy_data.sql. Судя по значениям полей, недостающее значение - значение для поля 'api_managed_roster'. Получаем: INSERT INTO `team` VALUES (1,'Test Team','#team','#team-alerts','team@example.com','US/Pacific',1,NULL,0,NULL,0);

Рекомендация по устранению warning-a: вынести переменную MYSQL_ROOT_PASSWORD и её значение в файл .env

---

## 1.2

В департаменте инфраструктуры несколько команд SRE, Вам как SRE предстоит задача по автоматизации процесса дежурств, а именно - разработка приложения, которое по REST API сервиса **OnCall** создаст команды/сотрудников команд и их дежурства согласно следующему описанию:

```yaml
---
teams:
  - name: "k8s SRE"
    scheduling_timezone: "Europe/Moscow"
    email: "k8s@sre-course.ru"
    slack_channel: "#k8s-team"
    users:
      - name: "o.ivanov"
        full_name: "Oleg Ivanov"
        phone_number: "+1 111-111-1111"
        email: "o.ivanov@sre-course.ru"
        duty:
          - date: "02/10/2023"
            role: "primary"
          - date: "03/10/2023"
            role: "secondary"
          - date: "04/10/2023"
            role: "primary"
          - date: "05/10/2023"
            role: "secondary"
          - date: "06/10/2023"
            role: "primary"
      - name: "d.petrov"
        full_name: "Dmitriy Petrov"
        phone_number: "+1 211-111-1111"
        email: "d.petrov@sre-course.ru"
        duty:
          - date: "02/10/2023"
            role: "secondary"
          - date: "03/10/2023"
            role: "primary"
          - date: "04/10/2023"
            role: "secondary"
          - date: "05/10/2023"
            role: "primary"
          - date: "06/10/2023"
            role: "secondary"

  - name: "DBA SRE"
    scheduling_timezone: "Asia/Novosibirsk"
    email: "dba@sre-course.ru"
    slack_channel: "#dba-team"
    users:
      - name: "a.seledkov"
        full_name: "Alexander Seledkov"
        phone_number: "+1 311-111-1111"
        email: "a.seledkov@sre-course.ru"
        duty:
          - date: "02/10/2023"
            role: "primary"
          - date: "03/10/2023"
            role: "primary"
          - date: "04/10/2023"
            role: "primary"
          - date: "05/10/2023"
            role: "secondary"
          - date: "06/10/2023"
            role: "primary"
      - name: "d.hludeev"
        full_name: "Dmitriy Hludeev"
        phone_number: "+1 411-111-1111"
        email: "user-4@sre-course.ru"
        duty:
          - date: "02/10/2023"
            role: "secondary"
          - date: "03/10/2023"
            role: "secondary"
          - date: "04/10/2023"
            role: "vacation"
          - date: "05/10/2023"
            role: "primary"
          - date: "06/10/2023"
            role: "secondary"
```

Формат ответа: исходный код приложения в виде архива, загруженный на платформу.

---

**[Скринкаст](https://youtu.be/qt7kG99BpNA)**

---

## 2.1

Разработка подготовила новую версию приложения oncall v2.0.1:

```
~$ git clone -b v2.0.1 https://github.com/linkedin/oncall.git
```

После развертывания данного приложения на тесте, Вы заметили, что приложение не отдает метрики в формате **prometheus**

Вам как **SRE** нужно понять причину, почему приложение не отдает метрики в формате **prometheus** и подготовить **git patch** с исправлениями

**Формат ответа**: **git patch** в виде архива, загруженный на платформу

---

## 2.2

У Вас развернуты **Prometheus** и приложение **OnCall** - Вам нужно описать способ автоматической регистрации **target**'a в **Prometheus**, то есть при запуске приложения **OnCall**:

```
~$ docker-compose up
```

**Prometheus** должен обнаружить новый **target** и начать снимать метрики приложения

**Формат ответа**: в свободной форме

---

Для автоматической регистрации target-a можно использовать DNS service discovery. Для этого в файле prometheus.yml вместо static_configs необходимо прописать 

```yaml
dns_sd_configs:
  - names:
      - oncall-web
    type: A
    port: 8081
```
---

**[Скринкаст part 1](https://youtu.be/HJfOLofqzDw)**
**[Скринкаст part 2](https://youtu.be/vTh668cW51k)**

---

## 3.1

Вам нужно настроить мониторинг сервера, на котором развёрнут сервис **OnCall**.

- Установить Node Exporter и добавить его в Prometheus.
- Поскольку не все коллекторы используются в вашей системе, необходимо отключить те, которые не используются. В комментариях требуется описать, почему выбор пал на них.
- Сделать простой скрипт для Textfile-collector по любой метрике, которая отсутствует в Node Exporter, обосновать почему вы решили ее использовать.

В качестве решения приложите архив с файлами конфигурации и видео с демонстрацией поиска метрик Node Exporter в Prometheus.

---

## 3.2

Вам нужно **Blackbox Exporter** и настроить мониторинг доступности сервиса **OnCall** следующими пробами:

- tcp
- icmp
- http

В качестве решения приложите архив с файлами конфигурации и видео с демонстрацией поиска метрик Blackbox Exporter в Prometheus.
