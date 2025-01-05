<h1 align="center">
FxPopup
</h1>

<p align="center">
  <a href="https://github.com/HugoQuinn2/fxpopup">
    <img src="https://github.com/user-attachments/assets/af66f7e1-55f5-4433-abce-4b37d4e973c9" alt="Logo">
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
    <version>0.1.0</version>
</dependency>
```

## Getting started

![Recording 2024-12-28 at 14 47 14](https://github.com/user-attachments/assets/366777a9-c1a2-4587-893e-4d90cd8d37d9)

FxPopup seamlessly injects XML code into a JavaFX application to display 
notifications to the user. To function correctly, the main container of 
the application must be a StackPane, which is supplied to the controller. 
If your root parent is not a StackPane, FxPopup will automatically wrap 
your root element in a StackPane to ensure compatibility

```java
//Example use lib
FxPopup fxPopup = new FxPopup();
fxPopup.add(/*Message object*/);
fxPopup.show(/*Model Form*/);
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
fxPopup.setTheme(Theme.DARK);
```

## Message Popup.
<p>
To display a message it is necessary to create a <code>Message</code> object, 
by default the messages will be displayed with position <code>Pos.BOTTOM_RIGHT</code>. 
Message required the params <code>title</code>, <code>MessageType</code> and<code>duration</code> 
for be displayed, by optional could add <code>context</code>, if you want to show a message with more information.
</p>

```java
// Full Message.
Message exampleMessage = new Message(
        "Title",
        "Context",
        MessageType.SUCCESS, // Message.INFO, Message.WARNING, Message.ERROR
        10  // Duration seconds
);

// Message without context.
Message simpleMessage = new Message(
        "Title",
        MessageType.SUCCESS, // Message.INFO, Message.WARNING, Message.ERROR
        10  // Duration seconds
);

fxPopup.add(exampleMessage);
fxPopup.add(simpleMessage);
```

### Add action event to message

<p>
The functionalities of a message are not limited to simple plain text, 
it requires important actions. These actions can be added directly to 
the Message getting the parent Message.
</p>

```java
Message message = new Message("Just a message", MessageType.INFO, 3);
// Example mouse clicked on message action.
message.getParent().setOnMouseClicked(mouseEvent ->{
        /*You code*/
});

fxPopup.add(message);
```

### Default message structure.

```ascii
VBox (#messageBody)
├── HBox
│   ├── Pane (#alertPane)
│   ├── VBox
│   │   ├── Label (#messageTitle)
│   │   └── Label (#messageContext)
│   └── Button (#buttonCloseMessage)

```

### Default form structure.

```ascii
VBox (#messageFormBody)
├── HBox
│   ├── Label (#titleForm)
│   ├── Button (#buttonClose)
├── VBox (#fieldsContainer)
├── Label (#messageError)
└── HBox
    └── Button (#successButton)

```

