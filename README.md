# Project 3: Turing Machine Simulator

* Author: Spencer Ford, Luis Acosta
* Class: CS361 Section #001
* Semester: Spring 2025

## Overview

This program both implements a bi-infinite Turing Machine and simulates how the Turing Machine instance processes
the input. This program builds Turing Machines and runs them according to input from text files which contains the
Turing Machine's build instructions followed by an input string as an initial value for the built Turing Machine's tape. 
This program assumes all Turing Machines are deterministic, has a transition for each tape symbol, and will halt. 
Additionally, there is only one halting state and when reached, this program will print the content of visited tape cells.

## Format for Text File to Build/Run Turing Machine

Number of States
Number of Symbols
((Number of States - 1)*(Number of States + 1)) Transitions
Input String for Simulation

See file0.txt for an example.

## Reflection

<br />
Spencer Ford

This project came together rather smoothly with Ford's quick thinking of which data structures to use
from the Java Collections Framework in addition to the quick implementation of how to correctly scan
and parse through the input file. From there, a working and fast solution was developed without much
friction.<br />
Luis Acosta

## Compiling and Using

In the root directory, run the following

`javac -d out tm/*.java`<br />
`java -cp out tm.TMSimulator input.txt`

## Sources used

We used Oracles website to find the best data structures to implement the appropriate interfaces. <br />
[Linked List](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html) <br />
[Hash Map](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html) <br />

----------

project structure:
project-root/
|-src/
| |-tm/
|   |--TMSimulator.java
|   |--Transition.java
|--file0.txt
|--file2.txt
|--file5.txt
|--README
