Challenge Cinco de Seis | Formación Java Orientado a Objetos | Conversor de Moneda

<h1 align="center">:currency_exchange: Conversor de Moneda ☕</h1>

![Challenge Oracle Next Education + Alura Banner](https://raw.githubusercontent.com/EduardoUT/ConversorMoneda-ONE-Alura_Challenge/master/src/main/resources/images/challengeImage.jpg)

En este desafio realicé un conversor de moneda utilizando como moneda base el Peso Mexicano (MXN) y otros códigos de divisas oficiales acorde a la norma ISO-4217 y que se encuentran disponibles en la clase Currency de Java.

Para asegurar la disponibilidad del servicio de conversión de divisas, la aplicación está respaldada por dos proveedores de divisas, esto en caso de que uno falle o se encuentre en mantenimiento:

**API REST Consumidas :currency_exchange::**
| [<sub>Exchange API</sub>](https://github.com/fawazahmed0/exchange-api) |  [<img src="https://raw.githubusercontent.com/EduardoUT/ConversorMoneda-ONE-Alura_Challenge/master/src/main/resources/images/nbp-logo.png" width=115><br><sub>National Bank Of Poland API Web</sub>](https://api.nbp.pl/en.html?ref=public_apis&utm_medium=website#kursyWalut)
| :---: | :---: |

Puede funcionar sin conexión, pero debe usarse online para que se importen las divisas localmente.

Incluye también un conversor de temperaturas (Celsius, Farenheit, Kelvin).

Extra personal:
- Imagenes de fondo diseñadas en Canva.com

**Tecnologías y conocimientos aplicados 💻:**
  - Java SE 8 ☕
  - JUnit Jupiter.
  - Mockito.
  - Interfaz gráfica desarrollada con Swing.
  - Programación Orientada a Objetos.
  - Patrón Singleton.
  - Patrón Strategy.
  - Patron MVC.

**Pruebe el Proyecto JAR (Requiere JRE de Java 8+):**

Enlace de descarga:
https://github.com/EduardoUT/ConversorMoneda-ONE-Alura_Challenge/releases/tag/v1.0.0

**Instrucciones para ejecución:**
  - Una vez descargado, descomprima.
  - Doble clic derecho en el archivo jar o bien desde consola:
    ```
    java -jar converter-2.0-SNAPSHOT-jar-with-dependencies.jar
    ```
  - Asegurese de estar conectado a internet al utilizar el conversor de moneda.
  - Por lo menos conectarse la primera vez para importar divisas y funcionar sin conexión.

Públicado en el topic:

https://github.com/topics/challengeoneconversorlatam


**Dependencias Maven 📖:**
   
![HTTP Client](https://img.shields.io/badge/OkHttp-4.12.0-blue)
![JSON](https://img.shields.io/badge/JSON_In_Java-20250517-blue)

**Dependencias Maven Testing:**

![JUnit Jupiter API](https://img.shields.io/badge/JUnit_Jupiter_Api-5.11.3-blue)
![JUnit Jupiter Params](https://img.shields.io/badge/JUnit_Jupiter_Params-5.11.3-blue)
![JUnit Jupiter Engine](https://img.shields.io/badge/JUnit_Jupiter_Engine-5.11.3-blue)

**Demo:**

![Gif demo de Conversión de Moneda](https://raw.githubusercontent.com/EduardoUT/ConversorMoneda-ONE-Alura_Challenge/master/src/main/resources/images/conversorMonedaDemo.gif)

**Vista Previa 👁️:**

![Vista Previa Interfaz Conversor de Moneda](https://raw.githubusercontent.com/EduardoUT/ConversorMoneda-ONE-Alura_Challenge/master/src/main/resources/images/interfazcurrency.PNG)

![Vista Previa Interfaz Conversor de Temperatura](https://raw.githubusercontent.com/EduardoUT/ConversorMoneda-ONE-Alura_Challenge/master/src/main/resources/images/interfaztemperatura.PNG)

En esta oportunidad, a los Devs se nos solicitó crear un conversor de divisas utilizando el lenguaje Java. Las características solicitadas por nuestro cliente son las siguientes:

**Autor 🧑:**

| [<img src="https://avatars.githubusercontent.com/u/60370547?s=400&u=c31036d0dc68db0d1fe71e36211360a84fc923f8&v=4" width=115><br><sub>Eduardo Reyes Hernández</sub>](https://github.com/EduardoUT) |
| :---: |
