# word2vecPM

This code implements the word2vecPM proposed in paper: [Bofang Li, Tao Liu, Zhe Zhao and Xiaoyong Du - **Investigating Different Context Types and Representations for Learning Word Embeddings**] (http://openreview.net/forum?id=Bkfwyw5xg). It allows Skip-Gram, CBOW and GloVe models learn word embeddings using different context types and representations. 

Note that word2vecPM is built upon [word2vecf](https://bitbucket.org/yoavgo/word2vecf), which is built upon [word2vec](http://code.google.com/p/word2vec). The code for GloVe is build upon [GloVe](https://github.com/stanfordnlp/GloVe). It's their transparent and reproducible works that makes this repository possible!


## Run generalized Skip-Gram and CBOW
- Download the files in 'word2vecPM' folder and compile by running 'make -C ./word2vecPM'.
- Prepare inputs (see next few sections).
- run the following code to generate word embeddings. (${ddir} indicate the input path)
```Bash
word2vecPM/word2vecPM -train ${ddir}/pairs -min-count 100 -iters 3 -pow 0.75 -cvocab ${ddir}/counts.contexts.vocab -wvocab ${ddir}/counts.words.vocab -dumpcv ${ddir}/sgns.contexts -output ${ddir}/sgns.words -sample 1e-5 -threads 32 -negative 5 -size 500
```

## Run generalized GloVe
- Download the files in 'GloVe' folder.
- Prepare inputs (see next few sections).
- run the following code to generate word embeddings. (Again, ${ddir} indicate the input path)
```Bash
./demo.sh ${ddir}
```


## Inputs format
### GSG and GBOW
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
### GloVe
Our implementation of GloVe uses the same collection P ('pairs' file) as GSG and merge it to form colection \overline{M}, so nothing need to be change. However, since GloVe do not distinguish word and context, the vocabularity files should be merge to form 'counts.vocab', which uses the exactly same format as 'counts.words.vocab' and 'counts.contexts.vocab'.


## Generate input

The java code in 'ContextPairGenerator' folder can be used for generating input collection. One can directly run this code to see a simple example.

This code support the selection of context type (linear or dependency-based) and context representations (word or bound word). It also support the selection of which collection (P or M) to generate.

Note that [Stanford CoreNLP](http://stanfordnlp.github.io/CoreNLP/) is needed for running this code.

## License

<a rel="license" href="http://creativecommons.org/licenses/by-nc/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc/4.0/88x31.png" /></a><br />



