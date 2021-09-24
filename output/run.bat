@echo off
javac %~dp0\Main.java
java -cp %~dp0 Main<%~dp0\test.in >%~dp0\test.out
