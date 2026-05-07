# MICO JAVA COMPILER

MICO (Minimal English-like Programming Language) is a Java-based mini compiler developed to demonstrate important Compiler Design concepts in a simple and practical way.

## Features

* Lexical Analysis
* Syntax Analysis
* Abstract Syntax Tree (AST) Generation
* Three Address Code (TAC) Generation
* DAG-based Optimization
* Assembly Code Generation

## Technologies Used

* Java
* Stack
* HashMap
* AST (Abstract Syntax Tree)

## Compiler Flow

Input Expression
↓
Lexical Analysis
↓
Syntax Analysis
↓
Three Address Code Generation
↓
DAG Optimization
↓
Assembly Code Generation

## Example Input

```text
(2*3)+(2*3)
```

## Example Output

```text
t1 = 2 * 3
t2 = 2 * 3
t3 = t1 + t2
```

### Optimized TAC

```text
t1 = 2 * 3
t2 = t1
t3 = t1 + t2
```

## How to Run

```bash
javac MICOCompiler.java
java MICOCompiler
```

## Author

Mahak Yadav
