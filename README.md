# Проект Heroes Battle

## **Описание**
Данный проект реализует основные механики игры для симуляции битв, включая управление юнитами, поиск пути, симуляцию сражений и генерацию пресетов. Код написан с соблюдением принципов чистого программирования, включает обширное логирование и поддерживает расширяемость через четко определенные интерфейсы.

# **Особенности**

### **Симуляция битвы**
- Симуляция сражений между армиями игрока и ИИ.
- Реализация стратегий атаки и защиты.
- Подробное логирование каждого хода.

### **Поиск пути**
- Эффективные алгоритмы для поиска путей на сетке.
- Учет препятствий и расчет кратчайшего пути.

### **Генерация пресетов**
- Динамическая генерация армий на основе заданных ограничений, таких как лимит очков.
- Выбор оптимальных юнитов по соотношению стоимости и эффективности.

### **Логирование и отладка**
- Пошаговый вывод логов в консоль для анализа.
- Логи включают информацию о поиске пути, генерации армий и деталях сражений.

# **Структура кода**

### **SimulateBattleImpl**
- Выполняет сражения между армиями.
- Логирует процесс битвы на русском языке.

### **UnitTargetPathFinderImpl**
- Предоставляет методы для расчета путей юнитов с учетом препятствий и целей.

### **GeneratePresetImpl**
- Создает настроенные армии на основе доступных юнитов и лимитов очков.

### **SuitableForAttackUnitsFinderImpl**
- Определяет подходящих для атаки юнитов на основе текущих условий.


# **Зависимости**

Убедитесь, что в директории `libs/` присутствуют следующие зависимости:
- `heroes_task_lib-1.0-SNAPSHOT.jar`


## **Команды для компиляции и запуска**

### **Компиляция классов**
Для компиляции всех классов проекта выполните следующую команду:
```bash
javac -d out -sourcepath src -cp libs/heroes_task_lib-1.0-SNAPSHOT.jar src/main/java/com/heroes_task/programs/*.java
```

### **Сборка JAR файла**
Для создания JAR файла выполните следующую команду:
```bash
jar cf heroes_task.jar -C out .
```

### **Перемещение JAR файла в папку libs**
Для перемещения созданного JAR файла в папку `libs` выполните команду:
```bash
mv -force heroes_task.jar libs/
```

### **Запуск игры**
Для запуска игры выполните следующую команду:
```bash
java -jar "Heroes Battle-1.0.0.jar"
```

### Кредиты
Автором репозитория написаны только интерфейсы и README.md в рамках итогового проекта.
Весь остальной код предоставлен составителями итогового задания.