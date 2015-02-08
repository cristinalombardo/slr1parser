# slr1parser
Provide a parser implementation of a SLR1 grammar in Extended Backus–Naur Form.

This program convert an input file that contains a grammar in Extended Backus–Naur Form. 
The program produce an ActionGoto table and if you insert a string the program verify if the input string is a string of the language.

## Execute
Requirements:
* JDK 1.8 +

Clone the project:
```console
$ git clone https://github.com/cristinalombardo/slr1parser.git
```
Execute the examples
```console
$ cd slr1parser/dist
$ java -jar slr1parser.jar ebnf-simple.txt
```

## Compile
Requirements:
* JDK 1.8 +
* Apache Maven 3.2.2 [maven.apache.org](http://maven.apache.org/) 

Clone the project:
```console
$ git clone https://github.com/cristinalombardo/slr1parser.git
```
Compile
```console
$ cd slr1parser/slr1parser
$ mvn clean compile assembly:single
```
Execute the examples
```console
$ cd target
$ java -jar slr1parser.jar ebnf-simple.txt
```
