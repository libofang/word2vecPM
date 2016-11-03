# word2vecPM

This code implements the word2vecPM proposed in paper: [Bofang Li, Tao Liu, Zhe Zhao and Xiaoyong Du - **Investigating Different Context Types and Representations for Learning Word Embeddings**] (xx). It learns word embeddings with GSG and GBOW. Note that word2vecPM is built upon [word2vecf](https://bitbucket.org/yoavgo/word2vecf), which is built upon [word2vec](http://code.google.com/p/word2vec). 


## Running word2vecPM 
- Download this code and compile by running 'make -C ./word2vecPM'.
- Prepare input.
- run the following code to generate word embeddings. (${ddir} indicate the input path)
```Bash
word2vecPM/word2vecPM -train ${ddir}/pairs -min-count 100 -iters 3 -pow 0.75 -cvocab ${ddir}/counts.contexts.vocab -wvocab ${ddir}/counts.words.vocab -dumpcv ${ddir}/sgns.contexts -output ${ddir}/sgns.words -sample 1e-5 -threads 32 -negative 5 -size 500
```

## Preparing input
- The input file's format for GSG and GBOW is actually very similar, each line represent an element of the input collection. For example, 
```
australian scientist @
scientist australian @
scientist discovers @
discovers scientist @
discovers star @
discovers telescope @
star discovers @
telescope discovers @
```
represents the input of collection P, and
```
australian scientist @
scientist australian discovers @
discovers scientist star telescope @
star discovers @
telescope discovers @
```
represents the input of collection M. This file should be named as 'pairs'.
- The vocabularity of word and context from the input collection is also required. For example, 
```
australian 343
scientist 4333
discovers 121
```
represents the vocabulary file. These files should be named as 'counts.words.vocab' and 'counts.contexts.vocab' for word vocabulary and context vocabulary respectively.


## Generating input collection

The java code in ContextPairGenerator folder can be used for generating input collection. One can directly run this code to see a simple example.

This code support the selection of context type (linear or dependency) and context representations (word or structured word). It also support the selection of which collection (P or M) to generate.

Note that [Stanford CoreNLP](http://stanfordnlp.github.io/CoreNLP/) is needed for running this code.





