#include <stdio.h>
#include "io.h"

// Reads a single word from a file, assuming space + tab + EOL to be word boundaries
void ReadWord(char *word, FILE *fin, int MAX_STRING) {
  int a = 0, ch;
  while (!feof(fin)) {
    ch = fgetc(fin);
    if (ch == 13) continue;
    if ((ch == ' ') || (ch == '\t') || (ch == '\n')) {
      if (a > 0) break;
      else continue; 
    }
    if (ch < 33 && ch > 126) continue;
    word[a] = ch;
    a++;
    if (a >= MAX_STRING - 2) {
      //printf("word size exceed max: %s\n", word);
      a--;   // Truncate too long words
    }
  }
  if(a == 0) {
    printf("none words\n");
    word[a] = '@';
    a++;
    fflush(stdout);
  }
  word[a] = 0;
}

