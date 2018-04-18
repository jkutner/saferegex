# SafeRegex [![Build Status](https://travis-ci.org/jkutner/saferegex.svg?branch=master)](https://travis-ci.org/jkutner/saferegex) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jkutner/saferegex/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.jkutner/saferegex)

SafeRegex is a tool that tests regular expressions for [ReDoS](https://www.owasp.org/index.php/Regular_expression_Denial_of_Service_-_ReDoS)
vulnerabilities. In contrast to similar tools, SafeRegex doesn't use plain fuzzing to detect vulnerabilites but uses an
approach similar to model checking. This makes it much more effective than plain fuzzers.

## Usage

Build the executable JAR:

```sh-session
$ ./mvnw clean package
```

Run the JAR against an [evil regex](https://en.wikipedia.org/wiki/ReDoS#Malicious_regexes):

```sh-session
$ java -jar target/tsaferegex.jar "(a|aa)+"

Testing: (a|aa)+
More than 10000 samples found.
***
This expression is vulnerable.
Sample input: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab
```

Or a safe regex:

```sh-session
$ java -jar target/saferegex.jar "(ht|f)tp(s?)\:\/\/[0-9a-zA-Z]([-.a-zA-Z_]*[0-9a-zA-Z])*(:(0-9)*)?(\/?)([a-zA-Z0-9\-\.\?\,\:\'\/\\\+=&amp;%\$#_]*)?"

Testing: (ht|f)tp(s?)\:\/\/[0-9a-zA-Z]([-.a-zA-Z_]*[0-9a-zA-Z])*(:(0-9)*)?(\/?)([a-zA-Z0-9\-\.\?\,\:\'\/\\\+=&amp;%\$#_]*)?
More than 10000 samples found.
************************************************************************************************************************************************************************************************************
*****************************************************************************************************************************
Tests: 3297
Broken samples: 0
This expression is probably not vulnerable for sample sizes < 10000
```

## History

The project was created on Feb 16, 2011 by Sebastian Kübeck and hosted on [Google Code](https://code.google.com/archive/p/saferegex/). This project has
been forked from the original and now maintained by [Joe Kutner](http://jkutner.github.io/).

## License

Apache License, Version 2.0
