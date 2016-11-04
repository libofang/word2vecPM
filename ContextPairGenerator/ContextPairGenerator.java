package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;

import main.CreateContextPair;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class ContextPairGenerator {
	String type; //linear or dependency //use -n to indicate the number of window size
	String rep; //word or bound
	String model; //skip or cbow
	
	
	StanfordCoreNLP pipeline;

	public ContextPairGenerator(String t, String r, String m) {
		type = t;
		rep = r;
		model = m;
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, parse");
		props.setProperty("lexparse.model", "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		
		pipeline = new StanfordCoreNLP(props);
	}

	public List<List<String>> contextPairs(String document) {
		
		List<List<String>> contextListList = new ArrayList<List<String>>();
		Annotation annotation = new Annotation(document);
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			if (type.contains("linear")) {
				int win = Integer.parseInt(type.split("-")[1]);
				List<CoreLabel> wordList = sentence.get(TokensAnnotation.class);
				for (int i = 0; i < wordList.size(); i++) {
					int l = i - win;
					int r = i + win;
					if (l < 0)
						l = 0;
					if (r >= wordList.size())
						r = wordList.size() - 1;
					List<String> contextSumList = new ArrayList<String>();
					contextSumList.add(wordList.get(i).word());
					for (int t = l; t <= r; t++)
						if (t != i) {
							String wt = wordList.get(t).word();
							if (rep.equals("bound")) {
								if (t > i)
									wt += "+" + (t - i);
								else
									wt += (t - i);
							}
							if (model.equals("skip")) {
								List<String> contextList = new ArrayList<String>();
								contextList.add(wordList.get(i).word());
								contextList.add(wt);
								contextListList.add(contextList);
							}
							if (model.equals("cbow")) {
								contextSumList.add(wt);
							}
						}
					if (model.equals("cbow"))
						contextListList.add(contextSumList);
				}
			}

			if (type.contains("dependency")) {
				int win = Integer.parseInt(type.split("-")[1]);
				SemanticGraph dg = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
				System.out.println(dg.edgeListSorted());
				List<IndexedWord> vls = dg.vertexListSorted();
				for (IndexedWord iw1 : vls) {
					List<String> contextSumList = new ArrayList<String>();
					contextSumList.add(iw1.word());
					for (int bi = 0; bi <= 1; bi++) {
						for (IndexedWord iw2 : vls) {
							List<SemanticGraphEdge> eList = null;
							if (bi == 0)
								eList = dg.getShortestDirectedPathEdges(iw1, iw2);
							else
								eList = dg.getShortestDirectedPathEdges(iw2, iw1);
							if (eList == null || eList.size() <= 0)
								continue;
							if (eList.size() <= win) {
								List<String> contextList = new ArrayList<String>();
								String w = null;
								SemanticGraphEdge e = null;
								contextList.add(iw1.word());
								if (bi == 0) {
									e = eList.get(eList.size() - 1);
									w = e.getDependent().word();
								} else {
									e = eList.get(0);
									w = e.getGovernor().word();
								}
								if (rep.equals("bound")) {
									w += "/" + e.getRelation();
									if (bi == 1)
										w += "-1";
								}
								contextList.add(w);
								contextSumList.add(w);
								if (model.equals("skip")) {
									contextListList.add(contextList);
								}
							}
						}
					}
					if (model.equals("cbow"))
						contextListList.add(contextSumList);
				}
			}
		}
		return contextListList;
	}

	public static void main(String args[]) {
		System.out.println(new ContextPairGenerator("dependency-1", "bound", "cbow")
				.contextPairs("Australian scientist discovers star with telescope"));
	}
}
