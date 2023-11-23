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

Пояснения к выбору коллекторов, которые необходимо отключить: 

   - "--no-collector.conntrack" т.к. нет /proc/sys/net/netfilter/

   - "--no-collector.hwmon" т.к. не стоит цели мониторить аппаратное обеспечение

Остальные не используются (node_scrape_collector_succes равен 0)

   - "--no-collector.dmi"

   - "--no-collector.nfs"

   - "--no-collector.nfsd"

   - "--no-collector.nvme"

   - "--no-collector.os"

   - "--no-collector.pressure"

   - "--no-collector.rapl"

   - "--no-collector.tapestats"

   - "--no-collector.zfs"

Скрипт собирает информацию о количестве файлов в директории. Эта метрика полезна, например, при сохранении логов в файлы по директориям. По ней можно понять, что что-то не так, если количество файлов возрастёт

**[Скринкаст](https://youtu.be/mglm_sNPNvY)**

---

## 3.2

Вам нужно **Blackbox Exporter** и настроить мониторинг доступности сервиса **OnCall** следующими пробами:

- tcp
- icmp
- http

В качестве решения приложите архив с файлами конфигурации и видео с демонстрацией поиска метрик Blackbox Exporter в Prometheus.

---

**[Скринкаст](https://youtu.be/75z0cLSMo9s)**

---

## 4.1

- ( Творческое ) Придумать контекст для которого будем готовить метрики
- Для указанного контекста придумать хотя бы 2 индикатора за которыми вы будете следить
- Спроектируйте exporter с отловом указанных метрик
- Все метрики должны попадать в Prometheus

---

## 4.2

Спроектируйте SLO для нашего сервиса ( референс - SRE Workbook там хорошо описано ) по спроектированным ранее метрикам
Примеры требований к сервису:

Сервис должен отвечать < 2c. в 95% случаев

---

## 4.3

Спроектируйте SLO для нашего сервиса по системным метрикам

---

## 5

В рамках курса продолжаем работу по улучшению наблюдаемости сервиса OnCall. В рамках задачи Вам необходимо:

Установить Filebeat, настроить сбор логов с приложения OnCall и отправку в Logstash 
Установить Logstash, настроить прием логов Filebeat и отправку в Elasticsearch 
Установить Elasticsearch, настроить прием логов Logstash 
Установить Kibana, подключить к Elasticsearch 
Логи приложений Filebeat, Logstash, Elasticsearch, Kibana не должны попадать в Elasticsearch 

В итоге, должна получится следующая цепочка:

OnCall + MySQL -> Filebeat -> Logstash -> Elasticsearch -> Kibana 

В качестве решения приложите архив с конфигурационными файлами Filebeat и Logstash и видео с демонстрацией поиска логов OnCall в ElasticSearch.

---

**[Скринкаст](https://youtu.be/xbOYjN_sp8A)**

---

## 6

Не всегда есть качественные логи, но нам надо по ним фильтровать быстро. Oncall предоставляет стандартные логи с http вызовами.

Вам необходимо:

Доставляем логи Oncall в ElasticSearch (из пред домашней работы)
Параметризуем/токенизируем логи:
По IP
По status code
Проверяем, что поиск по параметрам работает
Формат ответа: Файлы конфигурации с настроенной токенизацией, видео-экрана как работает поиск в Kibana по новым параметрам

---

**[Скринкаст](https://youtu.be/F0s8fnxZWZ4)**

---

## 7

Установить Grafana, добавить Prometheus как источник данных

• Импортировать дашборды для установленных ранее экспортеров (node exporter, blackbox exporter, etc)

• Создать дашборд с метриками от вашего кастомного экспортера из прошлого задания

• Создать дашборд с метриками OnCall для группы мониторинга:

Определить, какие данные от экспортеров стоит на нем отображать и в каком виде
Определить пороги для метрик

---

**[Скринкаст](https://youtu.be/8DoWWTiG7d0)**

---

## 7.2

• Создать дашборд для слежения за метриками системы ( не брать стандартный из ветрины бордов Grafana ) на основе метрик node_exporter

Обязательно метрики для CPU, RAM, Network, Disk с использованием USE методологии

• Создать сервисный борд на основе данных:

Healthcheck из blackbox_exporter-а
Данные из вашего экспортера
Добавьте в качестве источника Elasticsearch
Через таблицу выведите последние 10 строк логов из Oncall
Выведите панель с количеством ошибок Oncall

---

**[Скринкаст](https://youtu.be/cHb-VDcwF9c)**

---