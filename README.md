# DAM
ProgrammerHumor Hackathon 2019 Submission

## Installation
To install the "Dumb Ass Machina" you must have
at least version 1.8.0 of the Java Runtime Environment.
Download the latest version of the DAM for your operating system and unzip the Archive.
The DAM should be run from the console. If you run it with the -h or --help option you will
see the following dialogue:

Usage:
  DAM.bat [OPTIONS] program

Options:
  -h, --help      Display the help menu.
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