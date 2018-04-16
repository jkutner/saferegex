# SafeRegex

SafeRegex is a tool that tests regular expressions for [ReDoS](https://www.owasp.org/index.php/Regular_expression_Denial_of_Service_-_ReDoS) 
vulnerabilities. In contrast to similar tools, SafeRegex doesn't use plain fuzzing to detect vulnerabilites but uses an 
approach similar to model checking. This makes it much more effective than plain fuzzers.

## Usage

Run the executable JAR against an [evil regex](https://www.owasp.org/index.php/Regular_expression_Denial_of_Service_-_ReDoS)

```sh-session
$ java -jar saferegex.jar "(a|aa)+"
                          
Testing: (a|aa)+
More than 10000 samples found.
***
This expression is vulnerable.
Sample input: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab
```

Or a safe regex:

```sh-session
$ java -jar saferegex.jar "(a|aa)"

Testing: (a|aa)
2 samples found.

Tests: 2
Broken samples: 0
This expression is not vulnerable.
```

## History

The project was created on Feb 16, 2011 by Sebastian Kübeck and hosted on [Google Code](https://code.google.com/archive/p/saferegex/). This project has
been forked from the original and now maintained by [Joe Kutner](http://jkutner.github.io/).

## License

Apache License, Version 2.0
