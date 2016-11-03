# word2vecPM

This code implements the word2vecPM proposed in paper: [Bofang Li, Tao Liu, Zhe Zhao and Xiaoyong Du - **Investigating Different Context Types and Representations for Learning Word Embeddings**] (xx) 

word2vecPM is built upon [word2vecf](https://bitbucket.org/yoavgo/word2vecf), which is built upon [word2vec](http://code.google.com/p/word2vec). 

The input of word2vecPM is the collection P (for GSG) or M (for GBOW).

## Running word2vecPM 
- Download this code and compile by running 'make -C ./word2vecPM'.
- Prepare input.
- run the following code to generate word embeddings. (${ddir} indicate the input path)
```Bash
word2vecf/word2vecf -train ${ddir}/pairs -min-count 100 -iters 3 -pow 0.75 -cvocab ${ddir}/counts.contexts.vocab -wvocab ${ddir}/counts.words.vocab -dumpcv ${ddir}/sgns.contexts -output ${ddir}/sgns.words -sample 1e-5 -threads 32 -negative 5 -size 500
```

## Preparing input
- The input file's format for GSG and GBOW is actually very similar, the only different is the number of context. For example, EDITING.... 
