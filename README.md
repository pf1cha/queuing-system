# Результаты

## Автоматический режим
Пример вывода для автоматического режима:

#### Общее количество запросов:
- **Всего сгенерировано запросов**: ```значение```
- **Количество отклонённых запросов**: ```значение```
- **Процент отклонения**: ```значение %```

#### Время работы операторов:
Для каждого оператора будет выведено время работы в системе:
- **Оператор ```id```**: ```значение``` единиц времени

#### Статистика по каждому генератору:
Для каждого генератора будет выведена следующая статистика:
1. **Генератор ```id```**:
    - Сгенерировано звонков: ```значение```
    - Процент отклонённых звонков: ```значение```
    - Среднее время ожидания запроса: ```значение```
    - Среднее время в системе: ```значение```
    - Среднее время обслуживания запроса: ```значение```

## Пошаговый режим
Пошаговый режим происходит при печати букву ```q``` в консоль. 
На каждом шаге будет выведена информацию про действия на данном шаге, а также 
общая статистика для буфера, каждого генератора и для каждого оператора.

Пример вывода для пошагового режима:

### Информация про действия заявки в текущее время
Current time in the system: 0.0
Call 1 was generated
Call is being processed by operator.

### Информация про буфер:
The buffer size is: ```значение```
### Информация для генератора
Generator ```id генератора```  has generated ```число звонков```  calls
### Информация для операторов
Operator ```id оператора```  Current call id: ```id заявки``` , Total calls: ```число звонков``` , Total time work: ```работа оператора```
## Нахождение оптимальных характеристик

В данном случае будет выведена таблица, в которой значения удовлетворяют следующим условиям
- Процент отклонения меньше 10%
- Среднее время работы оператора больше 90% от всего времени
- Время нахождения заявки меньше чем 10 единиц времени

### Таблица
Пример вывода таблицы:
| Generator | Buffer | Operators | Refuse % | Operator Usage (%) | Time in System (s) |
|-----------|--------|-----------|----------|---------------------|--------------------|
| 4         | 11     | 4         | 0.62     | 90.54               | 5.39               |
| 4         | 13     | 4         | 1.13     | 90.40               | 5.51               |
| 4         | 16     | 4         | 0.91     | 90.06               | 6.31               |