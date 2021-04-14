# CS 1660 Final Project (Option 2)
### Ryan Lynch

## Setup

Before running the GUI on your machine, you will have to be running XLaunch (or similar) as described on [this website](https://cuneyt.aliustaoglu.biz/en/running-gui-applications-in-docker-on-windows-linux-mac-hosts/). For this, you will also have to know your machine's IP address. Mine is 192.168.1.162. You will have to substitute your own IP address into the command.

1. Clone this repository to your local machine. Then `cd` into the Docker directory.
        
        git clone https://github.com/rlynch7321/cs1660-finalproject/

        cd cs1660-finalproject/Docker

2. Once you are in the Docker directory, build the container.

        docker build -t ral94_finalproject .

3. Run the Docker container. Remember to have XLaunch running and to substitute your IP into the command.

        docker run --rm -it -e DISPLAY=192.168.1.162:0.0 ral94_finalproject
        
4. The GUI should now be running on your machine.

## A Note on Integration

Unfortunately I was not able to get the program properly integrated, and so the GUI does not communicate with the cloud and vice versa. 

## The GUI

Since the files we want to search are assumed to already be on the cloud (uploading not required), the user is first presented with a menu to select which files to search. Selecting no files and attempting to proceed will trigger a pop-up telling the user that they must select at least one file.

Next, the user is asked to choose between searching for a term or viewing Top-N results. In either case, the user is presented with a text box to enter their term (or N value). If Top-N is selected, the text box is first validated to ensure the value is a positive integer. At this point, the results of the user's request are displayed and the user can Exit the program when finished.

At multiple points in the program, the GUI outputs information to the console to demonstrate that inputs are being parsed properly and that there *should* be things happening behind the scenes that aren't.

## InvertedIndex.java

InvertedIndex.java is the MapReduce program deployed to the cloud that generates the Inverted Indexes for the files.

Lines in the files are first stripped of some punctuation like commas, periods, quotes, etc. Double dashes (`--`) are replaced with spaces. The lines are tokenized and written to the context as follows:

Key (Text) | Value (Text)
---|---
hello | file1.txt\|1
hello | file1.txt\|1
hello | file2.txt\|1
world | file2.txt\|1

The key is the word and the value is a string representing the file and the number of occurrences of the word.

In the reduce phase, a key and a list of values are passed in. A HashMap is created and the list of values is processed such that each unique key is located on just one line in the final output, which for the above example, would look like this:

Key (Text) | Value (Text)
---|---
hello | file1.txt~2\|file2.txt\|1
world | file2.txt~1

The value is now a string representing all the files in which the word was located as well as the number of occurrences in each file. Each file-count pair is separated by a `|` and each half of the pair is separated by a `~`.
