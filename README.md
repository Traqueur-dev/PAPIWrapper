# Placeholders Wrapper

This project is a wrapper for PlaceholderAPI that allows you to easily register and use placeholders with lambdas. It also provides the ability to pass arguments to placeholders using `{}` instead of `%%`, with a single level of recursion.

## 📌 Features
- Simple registration of placeholders with lambdas.
- Support for relational placeholders (two-player parameters).
- Argument replacement using `{}` with a single level of recursion.
- Easy integration with PlaceholderAPI.
- Automatic extraction of registered placeholders into a `.md` file.

## 📥 Installation
1. Add PlaceholderAPI as a dependency to your plugin.
2. Add this wrapper to your project.
3. Load the extension using `Placeholders.load(plugin);`

## 🚀 Usage
### Registering a simple placeholder
```java
Placeholders.register("my_placeholder", (player, args) -> {
    return "Hello " + player.getName() + " !";
});
```

### Registering a relational placeholder
```java
Placeholders.register("relational_placeholder", (player1, player2, args) -> {
    return player1.getName() + " and " + player2.getName() + " are friends!";
});
```

### Using a placeholder with an argument
```java
Placeholders.register("message", (player, args) -> {
    return "Message: " + args.get(0);
});
```
Then use: `%myplugin_message_{hello}%`, which will replace `{hello}` with the parsing value from PlaceholderAPI for the placeholder `%hello%`.

## 📜 Dependencies
- PlaceholderAPI
- Spigot API

## 🛠 Starting
Load the system in your main plugin class:
```java
@Override
public void onEnable() {
    Placeholders.load(this);
    //or
    Placeholders.load(this, "custom_prefix"); //assume placeholders are registered with the prefix "custom_prefix" like %custom_prefix_my_placeholder%
}
```

## 📝 Extracting Placeholders
The `Placeholders.extract()` function allows you to automatically generate a `.md` file listing all registered placeholders with their descriptions and arguments.

To include a description and potential arguments in the `.md` file, specify them when registering the placeholder:
```java
Placeholders.register("example", (player, args) -> {
    return "Example value with " + args.get(0) + " and " + args.get(1);
}, "This is an example placeholder", "arg1", "arg2"); // it's like %plugin_example_<arg1>_<arg2>%
```
Then call:
```java
Placeholders.extract();
```
This will generate a markdown file listing all placeholders with their descriptions and expected arguments.

## 📄 License
This project is licensed under the MIT License.
