# How should I edit this project in IntelliJ IDEA?

## 1. Browse to where you clone this project and run:

```shell
$ FP-PSP-SERVER> idea .
```

IntelliJ should detect automatically your Maven configuration and also your Spring Boot Configuration.

## 2. Configure the 'dev' profile

Go to menu option Run -> Edit Configuration. Select your automatically detected Spring Boot Configuration and add in the "Active Profiles" section: `dev`.

You can see a screen-shot of such configuration here: https://stackoverflow.com/questions/39738901/how-do-i-activate-a-spring-boot-profile-when-running-from-intellij

## 3. Run your project

On the top right corner select your Spring Configuration and than push the `Play` button (or `Debug`). This step will run your project as a Spring Boot Application and launch it at http://localhost:8080.

# How should I edit this project in Eclipse?

## 1. Import the project to Eclipse

1. Create or open a new Eclipse workspace
2. Select from the menus File -> Import -> Maven -> Existing Maven Projects
3. Browse to the FP-PSP-SERVER directory you cloned from GitHub and accept all of the defaults.

## 2. Configure and run the project

1. In eclipse Project Explorer, right click the project name -> select "Run As" -> "Maven Build..."
2. In the goals, enter `spring-boot:run`
3. Then select the JRE tab, and type your argument in the VM arguments text area:
   ```shell
   -Dspring.profiles.active=dev
   ```
4. Click the Run button and you're good to go :-)

## 3. Configure Formatter with checkstyle

Install Checkstyle plug-in
Import stylesheet using Windows --> Preferences, General --> Checkstyle --> New. Since you have an external file, choose "external file" as the type.
Right-click on your project in the Package view and select Checkstyle --> Create Formatter-Profile.

Then enable the formatter for your workspace:

```
 Windows --> Preferences --> Java --> Code Style --> Formatter. Select formatter: "eclipse-cs [project name]".
```
