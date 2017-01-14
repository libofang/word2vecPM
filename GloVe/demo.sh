#!/bin/bash
set -e

# Makes programs, downloads sample data, trains a GloVe model, and then evaluates it.
# 

make

ddir=${1}

CORPUS=${ddir}pairs
vocab_file_word=${ddir}counts.words.vocab
vocab_file_context=${ddir}counts.contexts.vocab
vocab_file=${ddir}counts.vocab
COOCCURRENCE_FILE=${ddir}cooccurrence.bin
COOCCURRENCE_SHUF_FILE=${ddir}cooccurrence.shuf.bin
BUILDDIR=build
VERBOSE=2
MEMORY=40.0
VOCAB_MIN_COUNT=2
BINARY=0
NUM_THREADS=32
X_MAX=100
MAX_ITER=30
VECTOR_SIZE=500

$BUILDDIR/cooccur -memory $MEMORY -ddir $ddir -vocab_file $vocab_file -verbose $VERBOSE < $CORPUS > $COOCCURRENCE_FILE

echo "$ $BUILDDIR/shuffle -memory $MEMORY -verbose $VERBOSE < $COOCCURRENCE_FILE > $COOCCURRENCE_SHUF_FILE"
$BUILDDIR/shuffle -memory $MEMORY -verbose $VERBOSE < $COOCCURRENCE_FILE > $COOCCURRENCE_SHUF_FILE

SAVE_FILE=${ddir}glove.words
echo "$ $BUILDDIR/glove -save-file $SAVE_FILE -threads $NUM_THREADS -input-file $COOCCURRENCE_SHUF_FILE -x-max $X_MAX -iter $MAX_ITER -vector-size $VECTOR_SIZE -binary $BINARY -vocab-file $VOCAB_FILE -verbose $VERBOSE"
$BUILDDIR/glove -save-file $SAVE_FILE -threads $NUM_THREADS -input-file $COOCCURRENCE_SHUF_FILE -x-max $X_MAX -iter $MAX_ITER -vector-size $VECTOR_SIZE -binary $BINARY -vocab-file $vocab_file -verbose $VERBOSE

