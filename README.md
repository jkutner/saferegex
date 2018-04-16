# saferegex

saferegex is a tool for testing regular expressions for ReDoS vulnerabilities. In contrast to similar tools, saferegex doesn't use plain fuzzing to detect vulnerabilites but uses an approach similar to model checking. This makes it much more effective than plain fuzzers.

## Usage

Run the executable JAR against an [Evil Regex](https://www.owasp.org/index.php/Regular_expression_Denial_of_Service_-_ReDoS)

```
$ java -jar saferegex.jar "(a|aa)+"
                          
Testing: (a|aa)+
More than 10000 samples found.
***
This expression is vulnerable.
Sample input: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab
```

## History

The project was created on Feb 16, 2011 by Sebastian Kübeck and hosted on [GoogleCode](https://code.google.com/archive/p/saferegex/). This project has
been forked from the original.

## License

Apache License, Version 2.0
