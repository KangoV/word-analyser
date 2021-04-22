# Word Analyser

This word analyser will takes a file as input and will analyse the words it contains and produce some metrics as output.

For example, given the following text:
```
Hello world & good morning. The date is 18/05/2016
```
the following output will be produced:
```
Word count = 9
Average word length = 4.556
Number of words of length 1 is 1
Number of words of length 2 is 1
Number of words of length 3 is 1
Number of words of length 4 is 2
Number of words of length 5 is 2
Number of words of length 7 is 1
Number of words of length 10 is 1
The most frequently occurring word length is 2, for word lengths of 4 & 5
```

## What is a word

The client provided no guidance on what constitutes a word so in current implementation, *StandardWordAnalyser*, a word is one or more characters seperated by one or more whitespace characters. These include spaces, tabs, new lines etc and can be denoted by thje following regular expression:
```
[\t\n\x0B\f\r]
```
Note that formatted numbers are not counted as words.

## Punctuation

The client provided no indication of any punctuation that should be removed. There is one clue in the example though. The word *morning.* (including the period) is 8 characters. In the output there is no 8 character word analysed. We can infur that a period is stripped prior to analysis.

Given this, the following other punctuation characters (via a regex) are removed prior to analysis:
```
[-+.^:?,=()]
```

## Building

The maven build uses the [Maven Wrapper](https://github.com/takari/maven-wrapper) so that the build will always be executed with the correct version and installed if necessary. This simplfies and makes builds more consistent across machines.

First clone this repository and cd into the project directory, then build using (Linux/Mac):
```
./mvnw clean install
```
or (Windows)
```
mvnw.cmd clean install
```
A normal Maven build will be executed with the one important change that if the user doesn't have the necessary version of Maven specified in `.mvn/wrapper/maven-wrapper.properties` it will be downloaded for the user first, installed and then used.

## Running

The client did not specify guidance on how the program should be run.

The Analyser takes only a file as input. Below is the usage (by passing `--help`:
```
$ java -jar target/words-1.0.0-SNAPSHOT.jar --help
Usage: words [-hV] <path>
read the contents of a plain text file and enable the display of the total
number of words, the average word length, the most frequently occurring word
length, and a list of the number of words of each length.
      <path>      The file containing the words to count.
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
```

###  Jar

The build produces an "uber" jar using the Shade plugin and can be run with the following command (Linux/Mac):
```
java -jar target/words-1.0.0-SNAPSHOT.jar src/test/resources/short_1.txt 
```
or (Windows):
```
java -jar target\words-1.0.0-SNAPSHOT.jar src\test\resources\short_1.txt 
```
This should give the exact same output as the example above

There are a couple of extra test files located in the `src/test/resources` directory.

### Native Image

Provided in the root directory is a binary (`words`) that will run the analysis. This binary is a natively compiled Linux binary that does not require a JVM. This provides much faster startup, less memory and in some cases faster performance.

This can be run via (Linux only):
```
./words src/test/resources/short_1.txt
```
to get help on usage, run:
```
./words --help
```

You will note that this will run faster as no JVM needs rto be started.

Currently native image compilation is disabled in the POM as the [GraalVM](https://www.graalvm.org/) JDK may not be installed. If you want to enable native image compilation, install GraalVM (version should match that in the POM) including the native image extension, uncomment the plugin in the POM and then compile as normal (`clean install`). 

Tip: Use [SDKMAN!](https://sdkman.io/) to manage multiple SDKs and various other libraries.

Due to XML comments not working properly, when uncommenting the graalvm plugin, you need to add the extra dashes to the entries in the `buildArgs` section. It should look like:
```
<buildArgs>
    --no-fallback
    --allow-incomplete-classpath
</buildArgs>
```
Note the double dash prefixes.

# Improvements

At the moment, the file is read into memory and then processed. One imoprovement would be to stream the file from disk/url as it is processed. This would enable the analysis on memory constraint machines and enable the analysis of huge text files.

Gather more information from the client around what constitutes a word and what punctuation should be stripped and at what point. Also, should dates be included as words (they currently are).