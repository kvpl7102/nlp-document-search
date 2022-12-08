# IFT2015---TP2
TP2 for course IFT2015 - Data Structures at UDEM

This project implements 2 custom HashMap classes to serve basic Natural Language Processing (NLP) function. The 2 Map classes will store data from the dataset which contains numerous of text documents. 

The NLP operations we will try to implement is:
    1. suggesting the next most probable word like an auto-complete function which is 
       related to the concept named bi-grams in NLP.
    2. finding the most relevant document for a key word search which is related to the 
       concept named TFIDF (term frequency–inverse document frequency) in NLP.
 
 The program will read an input file consisting of multiple queries. There are 2 types of queries: 
    1. "the most probable bigram of <word>" - which suggests the next most probable word that appear after a given word.
    2. "search <word>" - which returns the most relevant document of said word in the dataset

The program will output the result by each line in the terminal.
    
