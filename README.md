<h1 align="center">
FxPopup
</h1>

<p align="center">
  <a href="https://github.com/HugoQuinn2/fxpopup">
    <img src="https://github.com/user-attachments/assets/d24774fd-de30-4528-823a-393fe1661db3" alt="Logo">
  </a>
</p>

FxPopup is a JavaFX library that simplifies the creation of automatic forms
and popup messages with minimal effort. With just a single line of code, developers
can generate dynamic forms or display messages, while maintaining the flexibility to
use custom views for both functionalities.

## Maven
```xml
<dependency>
    <groupId>io.github.hugoquinn2</groupId>
    <artifactId>fxpopup</artifactId>
    <version>1.2.0</version>
</dependency>
```
## Gradle
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.hugoquinn2:fxpopup:1.2.0'
}
```

## Getting started

![Recording 2024-12-28 at 14 47 14](https://github.com/user-attachments/assets/366777a9-c1a2-4587-893e-4d90cd8d37d9)

FxPopup seamlessly injects XML code into a JavaFX application to display 
notifications to the user. To function correctly, the main container of 
the application must be a StackPane. 
If your root parent is not a StackPane, FxPopup will automatically wrap 
your root element in a StackPane to ensure compatibility.

```java
//Example use lib
FxPopup fxPopup = new FxPopup();
fxPopup.add(/*add custom node to window*/);
fxPopup.show(/*stack custom node to window*/);
```
<p>
If you want to display automatic forms, FxPopup requires access to the form's model and validation class.
To achieve this, export the relevant modules to FxPopup at your <code>module-info.java</code> as shown in the following example:
</p>

```java
module your.app {
    requires fxpopup;
    
    opens your.app.forms to fxpopup;
    opens your.app.formsController to fxpopup;
}
```

## Change Theme

<p>
FxPopup by default use <code>SYSTEM</code>, but you can force <code>LIGHT</code> & 
<code>DARK</code> theme in forms and popup with setTheme(Theme), example:
</p>

```java
fxPopup.setTheme(Theme.DARK); //Theme.LIGHT or Theme.SYSTEM
```

