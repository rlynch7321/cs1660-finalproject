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

Unfortunately I was not able to get the program properly integrated, and so the GUI does not communicate with the cloud and vice versa. At multiple points in the program, the GUI outputs to the console to demonstrate that inputs are being parsed properly and that there *should* be things happening behind the scenes that aren't.

## TermSearchGUI.java

Since the files we want to search are assumed to already be on the cloud (uploading not required), the user is first presented with a menu to select which files to search. Selecting no files and attempting to proceed will trigger a pop-up telling the user that they must select at least one file.

Next, the user is asked to choose between searching for a term or viewing Top-N results. In either case, the user is presented with a text box to enter their term (or N value). If Top-N is selected, the text box is first validated to ensure the value is a positive integer. At this point, the results of the user's request are displayed and the user can Exit the program when finished.

## InvertedIndex.java

InvertedIndex.java is the MapReduce program deployed to the cloud that generates the Inverted Indexes.

Lines in the files are first stripped of some punctuation like commas, periods, quotes, etc. Double dashes (`--`) are replaced with spaces. No filters were applied to  remove common words, largely because the program wasn't fully integrated, and without being able to actually generate the Top-N results, the need to remove overly common words from the output never arose. Each line is then tokenized and written to the context as follows:

Key (Text) | Value (Text)
---|---
hello | file1.txt\~1
hello | file1.txt\~1
hello | file2.txt\~1
world | file2.txt\~1

The key is the word and the value is a string representing the file and the number of occurrences of the word.

In the reduce phase, a key and a list of values are passed in. A HashMap is created to store filenames and counts, and the list of values is processed. For each key, the HashMap is converted to a single string so that each word gets just one line in the final output, which for the above example, would look like this:

Key (Text) | Value (Text)
---|---
hello | file1.txt~2\|file2.txt\~1
world | file2.txt~1

The value is now a string representing all the files in which the word was located as well as the number of occurrences in each file. Each file-count pair is separated by a `|` and each half of the pair is separated by a `~`.

Originally, the implementation of the reduce function could only handle single-pair values like `file1.txt~1`, so it was not possible to do any combining. However, I made a last-minute decision to refactor the code to allow for the combining of data before it is sent to the reducer. It can now process multi-pair input like `file1.txt~2|file2.txt~1` by first splitting the pairs on `|` and then getting the individual values from each pair by splitting again on `~`.

This modification meant the reducer only had 84,931 input records instead of the previous 2,628,256 - and the execution time of the job was shortened by several seconds.

# Video Walkthrough

https://pitt-my.sharepoint.com/:v:/g/personal/ral94_pitt_edu/EUNzMLnPu1lInb-ysxmVracBNMvE-AxKMaf5m6bCbuMdVA?e=aej8bG
