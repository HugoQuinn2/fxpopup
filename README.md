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

## Add Message Popup.
<p>
To display a message it is necessary to create a <code>Message</code> object, 
by default the messages will be displayed with the light theme <code>Theme.LIGHT</code>,
the object Message required the params <code>title</code>, <code>MessageType</code> and 
<code>duration</code> for be displayed, by optional could add <code>param</code>, 
if you want to show a message with more information.
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
## Change Theme

<p>
You can change the theme with the function <code>fxPopup.setTheme(Theme.DARK)</code>, 
this will apply to all messages and forms
</p>

```java
fxPopup.setTheme(Theme.DARK);
```

## Add action event to message

<p>
The functionalities of a message are not limited to simple plain text, 
it requires important actions. These actions can be added directly to 
the Message getting the parent Message.
</p>

```java
// Example mouse clicked on message action.
message.getParent().setOnMouseClicked(mouseEvent -> {
    // Custom Action
        // ...
});
```

## Default message structure.

```ascii
VBox (#messageBody)
├── HBox
│   ├── Pane (#alertPane)
│   ├── VBox
│   │   ├── Label (#messageTitle)
│   │   └── Label (#messageContext)
│   └── Button (#buttonCloseMessage)

```

## Default form structure.

```ascii
VBox (id: messageFormBody)
├── HBox
│   ├── Label (id: titleForm)
│   ├── Button (id: buttonClose)
├── VBox (id: fieldsContainer)
├── Label (id: messageError)
└── HBox
└── Button (id: successButton)

```

