## Database Exporter Application

This is a Java application for exporting data from a MySQL database to various formats (e.g., HTML, XML, etc.). The application uses Swing for the graphical user interface and supports a plug-and-play architecture for adding new export formats without changing existing code.

## Table of Contents

*   [Features](#features)
*   [Prerequisites](#prerequisites)
*   [Getting Started](#getting-started)
*   [Usage](#usage)
*   [Plugin Architecture](#plugin-architecture)
*   [Configurationless Operation](#configurationless-operation)
*   [Contributing](#contributing)
*   [License](#license)

## Features

*   Export data from MySQL database tables.
*   Supports multiple export formats (e.g., HTML, XML, etc.).
*   Extensible architecture for adding new export formats without modifying existing code.
*   Graphical user interface using Swing.
*   Virtually configurationless operation.

## Prerequisites

*   Java 8 or higher
*   MySQL database

## Getting Started

1.  Clone the repository:

    ```bash
    git clone https://github.com/aamitn/DbExporter.git
    cd DbExporter

2.  Compile the code:

    ```bash
    mvn clean install

3.  Run using jar:

    ```bash
    java -jar out/DbExporter.jar
<p style="text-align: center;">OR</p>

4.  Run using java mainClass:
    ```bash
    mvn compile exec:java

## Usage

Launch the application using the steps mentioned in the Getting Started section.  
Connect to your MySQL database by entering the database name, username, and password.  
Click "Show Tables" to display the available tables.  
Select a table from the list.  
Choose an export format from the dropdown menu.  
Click "Export Table" to export the selected table to the chosen format.

Example Factory Class Usage:

```java
ExporterFactory exportFactory = new ExporterFactory(connection);
Exportable exporter = exportFactory.getExporter("xml"); 
exporter.export("your_table_name", "output.xml");
```

## Plugin Architecture

The application employs a plugin architecture, allowing you to add new export formats without modifying existing code. To add a new export format, simply create a new class implementing the Exportable interface and place it in the org.nmpl.exporters package.

Example for creating a new export format:

```java
package org.nmpl.exporters;
import org.nmpl.Exportable;
public class NewFormatExporter implements Exportable {
// Implement the export method as per your requirements
}
```


No changes to the existing code are needed. The new class is automatically recognized and integrated into the application at runtime.

## Configurationless Operation

The application is designed for configurationless operation. The config.xml file, located in the resources directory, maps export formats to their corresponding Java class names. This enables the application to dynamically recognize and utilize new exporters without explicit configuration.

Example config.xml:

```xml
<exporters>
	<exporter> 
 		<type>html</type> 
		<class>org.nmpl.exporters.HTMLExporter</class> 
	</exporter> 
	<exporter>
		<type>xml</type>
 		<class>org.nmpl.exporters.XMLExporter</class> 
	</exporter>
 	<exporter> 
		<type>newformat</type> 
		<class>org.nmpl.exporters.NewFormatExporter</class>
 	</exporter>
 <!-- Add more exporter configurations as needed --> 
</exporters>
```

## Fork the repository

Create a new branch (git checkout -b feature/new-feature)  
Make your changes and commit them (git commit -am 'Add new feature')  
Push to the branch (git push origin feature/new-feature)  
Open a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.