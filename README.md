# DAM
ProgrammerHumor Hackathon 2019 Submission.

## Download

Latest Version:
 * [v1.1 Windows](https://github.com/Sam36502/DAM/blob/master/releases/v1.1/DAM_v1.1_win.zip)
 * [v1.1 Linux](https://github.com/Sam36502/DAM/blob/master/releases/v1.1/DAM_v1.1_lin.zip)

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

## Example Programs
I really wouldn't recommend writing any more programs for this.
The assebmbly language is absolutely rubbish.
That is why I have written some programs to show off the
capabilities of the DAM. These include:

 * "Hello, world!" - progs/helloworld.dam, Prints out "Hello, world!".
 * "Cat" - progs/cat.dam, Prints out anything that was input. (use the -i or --input option to set input)
 * "Fibonacci" - progs/fibonacci.dam, Prints out a few of the fibonacci nunmbers.
 * "FizzBuzz" - progs/fizzbuzz.dam, Prints all numbers from 1-100 except, if the number is divisible by
 3, print "fizz". Otherwise if the number is divisible by 5 print "buzz". If it's divisible by both then print "fizzbuzz".

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
The first thing that happens is the tag generation. A 32-digit hexadecimal string
that dictates how the whole system will work together. This also means that if you
find a specific tag you want to reuse, you can. No randomization is done in the Transpiler, Compiler
or VM. All the randomness comes from the tag when it's first generated.

The '.dam' Assembly files are first transpiled into a similar language where only the key-words have
been replaced by somewhat similar key-words. E.g.: `out 1` => `print 1` This means print out the ASCII
char of the byte stored at address 1. Once it's done, the Transpiler writes this new code into a temporary
file in the tmp directory.

Next the compiler class takes the transpiled code and converts it into single bytes corresponding to the
randomized instruction. From the tag, the compiler can see what number instruction it has to write to the
finished binary. E.g.: `set 1 1` => `2 1 1` => `b 1 1`
The keyword is first transformed into it's instruction number. Afterwards the corresponding randomized instruction number is
found and written to the compiled file.

Finally the VM starts and reads the temporary binary file. After reading in all the instructions, they are converted back from
their randomized version to the logical instruction number. Only now does the virtual machine know what to do. The VM has 16 instructions it can perform (excluding the "Do nothing" instruction). They are as follows:
```
 0 in  x    : Takes a byte from stdin and stores it in address x
 1 out x    : Concatenates the byte at address x to stdout
 2 set x y  : Sets the byte at address x to the value y
 3 cpy x y  : Copies a byte from x to y

 4 add x y z: Puts the sum of x and y into z
 5 sub x y z: Puts the difference of y and x into z
 6 mul x y z: Puts the product of x and y into z
 7 div x y z: Puts the integer quotient of x and y into z
 8 mod x y z: Puts the remainder of x divided by y into z
 9 equ x y z: Sets z to 1 if x and y are equal, else 0
10 grt x y z: Sets z to 1 if x is greater that y, else 0
11 lst x y z: Sets z to 1 if x is less that y, else 0

12 if  x    : skips to the next 'end' or 'else' so long as x isn't 0
13 else     : skips to the next 'end' unless the previous code was executed
14 end      : ends an if/else statement
15 jmp x    : Jumps to any line in the program
```

TL;DR\
It does many extremely unnecessary steps to perform very simple things.
