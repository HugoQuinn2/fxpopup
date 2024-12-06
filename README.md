<h1 align="center">
FxPopup
</h1>

<p align="center">
  <a href="https://github.com/HugoQuinn2/fxpopup">
    <img src="https://github.com/user-attachments/assets/af66f7e1-55f5-4433-abce-4b37d4e973c9" alt="Logo">
  </a>
</p>

FxPopup It is a javafx library for displaying notifications in a very simple way.

## Maven
```xml
<dependency>
    <groupId>io.github.hugoquinn2</groupId>
    <artifactId>fxpopup</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Getting started

FxPopup injects xml code into the JavaFx application to display notifications to the user, for this, the main container must be a StackPane, which is provided to the controller.

```java
// Basic load content
StackPane root = new StackPane();
FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/path/to/body.fxml"));
root.getChildren().add(fxmlLoader.load());
Scene scene = new Scene(root, 500, 500);

//Example use lib
FxPopup fxPopup = new FxPopup(root);
```
## Show Message
<p>To display a message it is necessary to create a <code>Message</code> object, by default the messages will be displayed with the light theme <code>Theme.LIGHT</code></p>

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
### Change Theme

<p>You can change the theme with the function <code>fxPopup.setTheme(Theme.DARK)</code>, this will apply to all messages</p>

```java
fxPopup.setTheme(Theme.DARK);
```

## Add action event to message

<p>The functionalities of a message are not limited to simple plain text, it requires important actions. These actions can be added directly to the Message with the function: <code>message.setAction(EventType, EventHandler)</code>.</p>

```java
// Example mouse clicked on message action.
message.setAction(MouseEvent.MOUSE_CLICKED, event -> {
// Custom Action
   ...
});
```

## Add custom theme.

<p>If you don't like the themes already provided, it is possible to apply personal themes with the function, <code>message.setCss(Resource)</code>.</p>

```java
// Example style custom.
String css = Objects.requireNonNull(getClass().getResource("/resource/path/style.css")).toExternalForm();
message.setCss(css);
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