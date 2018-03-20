# Contributing

Thanks for being willing to contribute!

**Working on your first Pull Request?** You can learn how from this _free_ series
[How to Contribute to an Open Source Project on GitHub][egghead]

## Project setup

1. Fork and clone the repo
2. `$ mvn install` to install dependencies
3. `$ mvn test` to make sure everything is working fine
4. Create a branch for your PR

> ```
> git remote add upstream https://github.com/FundacionParaguaya/FP-PSP-SERVER
> git fetch upstream
> git checkout -b my_pr_branch
> ```

## Committing and Pushing changes

Please make sure to run the tests before you commit your changes. You can run
`mvn test` which will run the tests and will also run the maven code analyzers plugins:

* [PMD](https://pmd.github.io/), checks some common potential source of bugs and good practices
* [CheckStyle](http://checkstyle.sourceforge.net/), checks code style conventions
* [FindBugs](http://findbugs.sourceforge.net/), also checks some common potential source of bugs

If any of those plugins throws any errors you will have to fix them manually and push those fixes to your PR branch.

## Coding Conventions

* Line length no longer than 120 chars
* 4 spaces tabs
* No empty line at the end of files

We use checkstyle to include standard Java coding conventions

> Checkstyle configuration checks the sun coding conventions from:
>
> * the Java Language Specification at
>   http://java.sun.com/docs/books/jls/second_edition/html/index.html
> * the Sun Code Conventions at http://java.sun.com/docs/codeconv/
> * the Javadoc guidelines at
>   http://java.sun.com/j2se/javadoc/writingdoccomments/index.html
> * the JDK Api documentation http://java.sun.com/j2se/docs/api/index.html

We highly advice to configure a checkstyle plugin in your IDE and use the `checkstyle.xml` file we provide with the project.

For Eclipse users you can check how to configure the formatter [here](docs/IDE.md).

## Some good practices we try to follow

We try to follow some good practices listed in this section.

Overall, we invite contributors to read the existing code and follow the conventions that are already in place :).

* No more than 4 parameters in the Services public constructors. Create new services if needed
* No more than 4 parameters in public methods. Create new DTOs if needed, to use them as parameters
* No more than 15 lines per method
* No more than one if statement per method
* Avoid switch statements if possible, use Java 8 lambdas instead. You can check [this blog post](http://marcels-javanotes.blogspot.com/2016/09/replace-switch-statements-using-lamda.html) for some inspiration. 
* No nested loops if possible
* Use [Builder pattern](https://github.com/HugoMatilla/Effective-JAVA-Summary#2-use-builders-when-faced-with-many-constructors) when faced with classes that require more than one non-empty constructor. Usually this is the case for DTOs used in automated tests
* [Enums instead of int constants](https://github.com/HugoMatilla/Effective-JAVA-Summary#6-enums-and-annotations)
* Use static final properties for String constants, don't hardcode them in methods
* If a POJO does not need getters, don't create them. [Minimize mutability](https://github.com/HugoMatilla/Effective-JAVA-Summary#15-minimize-mutability)
* Don't modify method parameters, create instead new objects and return them. Again, _minimize mutability everywhere_
* Return early from methods as soon as some condition for the execution of the method is not met.
* Don't return `null` values, prefer empty default objects or Java 8 Optionals. For lists return `Collections.empty()`
* If you need a helper method or class search an existing either in [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html) or in [Google Guava](https://github.com/google/guava)

[egghead]: https://egghead.io/series/how-to-contribute-to-an-open-source-project-on-github
