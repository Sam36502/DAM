# DAM
ProgrammerHumor Hackathon 2019 Submission.

## Download

Latest Version:
 * [v1.1 Windows](https://github.com/Sam36502/DAM/releases/v1.1/DAM_1.0_win.zip)
 * [v1.1 Linux](https://github.com/Sam36502/DAM/releases/v1.1/DAM_1.0_lin.zip)

## Installation
To install the "Dumb Ass Machina" you must have
at least version 1.8.0 of the Java Runtime Environment.
Download the latest version of the DAM for your operating system and unzip the Archive.
The DAM should be run from the console. If you run it with the -h or --help option you will
see the following help Menu:

```
Usage:
  DAM.bat [OPTIONS] program

Options:
  -h, --help      Display the help menu.
  -v, --version   Prints the current version of the DAM.
  -t, --tag tag   Run a program with a specific tag.
  -l, --log       Runs program and shows info about the process.
  -i, --in input  Starts the program with input.
  -p, --prog-show Displays the instructions being run on the VM.
  -s, --save-tmp  Saves the temporary files from transpiling.
                  and compiling, for debugging.

Examples:
  DAM.bat -h
  DAM.bat helloworld.dam
  DAM.bat -t 674daf093e8b51c2948f5bb14b426c3d helloworld.dam
  DAM.bat --log --show-prog --save-tmp --in Hello helloworld.dam
```

## How it works
As the theme of the Hackathon was overengineering, I decided to make a program that could
run very simple programs via a randomized virtual machine. So when you run a program like
helloworld, It's actually transpiling the assembly language into a different assembly language with
randomized syntax and then compiling from the randomized syntax into a randomized instruction set
and running the compiled binary with a randomized virtual machine.
```
            +------------+   +----------+   +-----------------+
Assembly ==>| Transpiler |==>| Compiler |==>| Virtual Machine |==> Output
  File      +------------+   +----------+   +-----------------+
```
