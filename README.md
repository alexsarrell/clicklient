# Getting Started

[RU]

Эта библиотека предоставляет JPA-like интерфейс для интеграции с СУБД ClickHouse с использованием Java-аннотаций и объектного отображения.
Чтобы начать работать с библиотекой добавьте в проект следующую зависимость:

1) Для Maven
```xml
<dependency>
        <groupId>com.aalpov</groupId>
        <artifactId>com4j</artifactId>
        <version>5.0.1</version>
</dependency>
```
2) Для Gradle

```kotlin
implementation("com.aalpov:com4j:1.0.0")
```

После установки библиотеки необходимо добавить к вашему `@Configuration` классу
аннотацию `@ClickhouseTablesScan(basePackages = <your base path>)` и инициализировать бин `DatabaseProperties`

```kotlin
@ConfigurationProperties(prefix = "clickhouse")
data class ClickhouseProperties(
    val url: String,
    val username: String,
    val password: String
)


@Configuration
@ClickhouseTablesScan(basePackages = ["com.aalpov.clickhouseclient"])
open class AppConfiguration {

    @Bean
    open fun properties(clickhouseProperties: ClickhouseProperties): DatabaseProperties {
        return DatabaseProperties(
            clickhouseProperties.url,
            clickhouseProperties.username,
            clickhouseProperties.password
        )
    }
}
```

```yaml
clickhouse:
  url: "jdbc:clickhouse://172.30.71.31:8123/default?compress=0"
  username: "default"
  password: "admin"
```

Конфигурация завершена. Чтобы отобразить ваши классы на таблицы в ClickHouse аннотируйте их при помощи
`@Table`

```kotlin

import java.util.Date

@Table("clickhouse_table", primaryKey = ["userId"], orderBy = ["age"], partitionBy = ["created"])
class TestTable(
    val userId: UUID,
    val userName: String,
    val age: Byte,
    val created: Date,
)
```

Также, для гибкой настройки параметров таблиц, вы можете использовать ClickHouse Expressions
```kotlin

import java.util.Date

@Table("clickhouse_table", primaryKey = ["userId"], orderBy = ["age"])
@PartitionBy("toYYYYMM(created)")
class TestTable(
    val userId: UUID,
    val userName: String,
    val age: Byte,
    val created: Date,
)
```

В примере выше партиционирование будет происходить по месяцам

После запуска сервиса библиотека зарегистрирует аннотированный класс и выполнит запрос на создание таблицы

```
CREATE TABLE IF NOT EXISTS TestTable(
    userId UUID,
    userName String,
    age Int8,
    created Date
) ENGINE = MergeTree()
PRIMARY KEY (userId)
ORDER BY (userId)
PARTITION BY toYYYYMM(created)
```

Чтобы сохранить объект или массив объектов класса TestTable в ClickHouse достаточно использовать бин ClickhouseClient,
автоматически добавляемый в ваш Spring контекст при старте приложения.

```kotlin

@Service
class TestService(@Autowired clickhouseClient: ClickhouseClient) {
    fun saveObject(testTable: TestTable) {
        clickhouseClient.insert(testTable)
    }

    fun insertManyObjects(testTables: Iterable<TestTable>) {
        clickhouseClient.insertMany(testTables)
    }
}

```

[EN]

This library provides a brand-new JPA-like interface for integrating with the ClickHouse DBMS using Java annotations and object mapping.

To start working with the library, add the following dependency to your project:

1) For Maven:

```xml
<dependency>
   <groupId>com.aalpov</groupId>
   <artifactId>com4j</artifactId>
   <version>5.0.1</version>
</dependency>
```

2) For Gradle:

```kotlin
implementation("com.aalpov:com4j:1.0.0")
```

After installing the library, you need to add the annotation @ClickhouseTablesScan(basePackages = <your base path>) to your @Configuration class and initialize the DatabaseProperties bean.

```kotlin
@ConfigurationProperties(prefix = "clickhouse")
data class ClickhouseProperties(
    val url: String,
    val username: String,
    val password: String
)
```

```kotlin
@Configuration
@ClickhouseTablesScan(basePackages = ["com.aalpov.clickhouseclient"])
open class AppConfiguration {

    @Bean
    open fun properties(clickhouseProperties: ClickhouseProperties): DatabaseProperties {
        return DatabaseProperties(
            clickhouseProperties.url,
            clickhouseProperties.username,
            clickhouseProperties.password
        )
    }
}
```


```yaml
clickhouse:
    url: "jdbc:clickhouse://172.30.71.31:8123/default?compress=0"
    username: "default"
    password: "admin"
```


Configuration is complete. To map your classes to tables in ClickHouse, annotate them with @Table.

```kotlin
import java.util.Date

@Table("clickhouse_table", primaryKey = ["userId"], orderBy = ["age"], partitionBy = ["created"])
class TestTable(
    val userId: UUID,
    val userName: String,
    val age: Byte,
    val created: Date,
)
```


Also, for flexible table parameter settings, you can use ClickHouse Expressions.

```kotlin
import java.util.Date

@Table("clickhouse_table", primaryKey = ["userId"], orderBy = ["age"])
@PartitionBy("toYYYYMM(created)")
class TestTable(
    val userId: UUID,
    val userName: String,
    val age: Byte,
    val created: Date,
)
```


In the example above, partitioning will be done by months.

After starting the service, the library will register the annotated class and execute the table creation query:

```sql
CREATE TABLE IF NOT EXISTS TestTable(
    userId UUID,
    userName String,
    age Int8,
    created Date
) ENGINE = MergeTree()
PRIMARY KEY (userId)
ORDER BY (userId)
PARTITION BY toYYYYMM(created)

```

To save an object or an array of TestTable objects in ClickHouse, simply use the ClickhouseClient bean, which is automatically added to your Spring context when the application starts.

```kotlin
@Service
class TestService(@Autowired clickhouseClient: ClickhouseClient) {
    fun saveObject(testTable: TestTable) {
        clickhouseClient.insert(testTable)
    }

    fun insertManyObjects(testTables: Iterable<TestTable>) {
        clickhouseClient.insertMany(testTables)
    }
}
```