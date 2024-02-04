AUTHOR: Jared Malto
Files Submitting:
Scanner.java- This is where all of the work I did is. In this, I have 2 lists as representations of the tokens. One contains the keywords and the other contains the data that corresponds to the identifiers and constants. My thought process behind it is first tokenize the file and add to the list as strings and data. Then, process that list into the Core language. I believed this method was more intuitive than finding the next token as the user goes along. My thought process for the rest of the methods is in comments.

Core.java and Main.java-Unchanged

Special Features/Comments: No special features implemented :(((. I do want to say that my algorithm is definitely not the most efficient. I definitely could have just added the data as core instead of having another layer on top of it and taking up more memory.

Known Bugs: A bug I found was that sometimes the scanner skips a token. This only happens for a few of the test cases and I couldn\'92t find a patter which was odd.
