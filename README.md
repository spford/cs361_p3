# cs361_p3
CS361 - Project 3

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

compile: javac -d out src/tm/*.java
run: java -cp out src.tm.TMSimulator input.txt