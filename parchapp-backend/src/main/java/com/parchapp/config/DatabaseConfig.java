package com.parchapp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * ============================================================
 * DatabaseConfig — Configuración de la conexión BD ↔ Backend
 * ============================================================
 *
 * Esta clase es el PUENTE EXPLÍCITO entre la base de datos MySQL
 * y el backend Spring Boot. Define tres componentes clave:
 *
 *   1. DataSource          → La conexión física a MySQL
 *   2. EntityManagerFactory→ El motor JPA que convierte objetos Java ↔ tablas SQL
 *   3. TransactionManager  → Controla que las operaciones de BD sean atómicas
 *
 * ¿Por qué se necesita si ya existe application.properties?
 *   application.properties es la configuración abreviada.
 *   Esta clase es la configuración explícita y completa,
 *   que da control total sobre cómo se conecta la aplicación a la BD.
 *   Ambas conviven: esta clase tiene prioridad.
 *
 * ¿Qué significa @Entity y su relación con esta clase?
 *   Cada clase marcada con @Entity (Usuario, Agencia, Plan, Reserva)
 *   le dice a JPA "esta clase es una tabla". El EntityManagerFactory
 *   configurado aquí escanea el paquete "com.parchapp.model" y
 *   detecta automáticamente todas esas clases @Entity para
 *   mapearlas a sus tablas en la BD.
 *
 * Flujo completo:
 *   MySQL ←→ DataSource (HikariCP) ←→ EntityManagerFactory (Hibernate/JPA)
 *   ←→ Repository (Spring Data) ←→ Service ←→ Controller ←→ Frontend
 *
 * @Configuration          → esta clase define beans de Spring (componentes)
 * @EnableJpaRepositories  → activa los repositorios en el paquete indicado
 * @EnableTransactionManagement → activa el soporte de @Transactional
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.parchapp.repository"   // dónde están los Repository
)
public class DatabaseConfig {

    // Lee los valores del archivo application.properties
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriver;

    @Value("${spring.jpa.hibernate.ddl-auto:validate}")
    private String ddlAuto;

    // ─────────────────────────────────────────────────────────────
    // BEAN 1: DataSource (la conexión a MySQL)
    // ─────────────────────────────────────────────────────────────

    /**
     * DataSource con pool de conexiones HikariCP.
     *
     * ¿Qué es un pool de conexiones?
     *   Abrir y cerrar una conexión a la BD por cada petición es lento.
     *   HikariCP mantiene un grupo (pool) de conexiones abiertas y las
     *   reutiliza, lo que hace la aplicación mucho más rápida.
     *
     * @Bean → Spring registra este objeto y lo inyecta donde se necesite
     */
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        // URL de conexión JDBC a MySQL
        // Formato: jdbc:mysql://HOST:PUERTO/NOMBRE_BD?parámetros
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.setDriverClassName(dbDriver);

        // Configuración del pool de conexiones
        config.setMaximumPoolSize(10);      // máximo 10 conexiones simultáneas a MySQL
        config.setMinimumIdle(2);           // mantener siempre mínimo 2 conexiones listas
        config.setConnectionTimeout(30000); // esperar máx 30 seg para obtener una conexión
        config.setIdleTimeout(600000);      // cerrar conexiones inactivas después de 10 min
        config.setMaxLifetime(1800000);     // renovar conexiones cada 30 minutos

        // Nombre del pool (útil para identificarlo en logs)
        config.setPoolName("ParchApp-HikariPool");

        return new HikariDataSource(config);
    }

    // ─────────────────────────────────────────────────────────────
    // BEAN 2: EntityManagerFactory (el motor JPA/Hibernate)
    // ─────────────────────────────────────────────────────────────

    /**
     * EntityManagerFactory — el corazón del mapeo ORM.
     *
     * ¿Qué hace?
     *   - Escanea el paquete "com.parchapp.model" buscando clases @Entity
     *   - Mapea cada @Entity a su tabla en MySQL
     *   - Genera el SQL (INSERT, SELECT, UPDATE, DELETE) automáticamente
     *   - Implementa la especificación JPA usando Hibernate como motor
     *
     * ORM = Object Relational Mapping:
     *   Objeto Java (Usuario.java) ←→ Tabla MySQL (usuario)
     *   Campo Java (nombre)        ←→ Columna MySQL (nombre)
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();

        // Usa la conexión que definimos arriba
        factory.setDataSource(dataSource());

        // Indica dónde están las clases @Entity para escanearlas
        factory.setPackagesToScan("com.parchapp.model");

        // Usa Hibernate como implementación de JPA
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);       // mostrar SQL generado en consola (desarrollo)
        vendorAdapter.setGenerateDdl(false);  // no generar DDL automático (usamos schema.sql)
        factory.setJpaVendorAdapter(vendorAdapter);

        // Propiedades adicionales de Hibernate
        factory.setJpaProperties(hibernateProperties());

        return factory;
    }

    /**
     * Propiedades de configuración de Hibernate.
     * Estas propiedades ajustan el comportamiento del motor ORM.
     */
    private Properties hibernateProperties() {
        Properties props = new Properties();

        // Dialecto: le dice a Hibernate que está hablando con MySQL 8
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        // Mostrar SQL formateado en la consola (útil para depurar)
        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.format_sql", "true");

        // ddl-auto: qué hace Hibernate con el esquema de la BD al arrancar
        //   validate → solo verifica que las tablas coincidan con las @Entity
        //   update   → crea/modifica tablas automáticamente (solo para desarrollo)
        //   create   → borra y recrea todo (¡NUNCA en producción!)
        //   none     → no hace nada con el esquema
        props.setProperty("hibernate.hbm2ddl.auto", ddlAuto);

        // Zona horaria de la BD (Bogotá = UTC-5)
        props.setProperty("hibernate.jdbc.time_zone", "America/Bogota");

        // Mejora el rendimiento agrupando múltiples INSERTs/UPDATEs
        props.setProperty("hibernate.jdbc.batch_size", "20");

        return props;
    }

    // ─────────────────────────────────────────────────────────────
    // BEAN 3: TransactionManager (control de transacciones)
    // ─────────────────────────────────────────────────────────────

    /**
     * TransactionManager — garantiza la integridad de los datos.
     *
     * ¿Qué es una transacción?
     *   Es un conjunto de operaciones que deben ejecutarse TODAS o NINGUNA.
     *   Ejemplo en ParchApp: al crear una reserva se hacen 2 cosas:
     *     1. INSERT en la tabla reserva
     *     2. UPDATE en plan (reducir disponibilidad)
     *   Si falla el paso 2, el TransactionManager revierte el paso 1
     *   automáticamente. La BD queda en estado consistente siempre.
     *
     * Se activa en los servicios con la anotación @Transactional.
     */
    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
