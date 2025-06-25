# NLP Document Search Engine with Bigram Prediction

## Overview

A Java-based natural language processing system that implements intelligent document search and word prediction capabilities. The project features custom hash map data structures for efficient text indexing, bigram-based auto-completion, and TF-IDF scoring for document relevance ranking.

## Features

### **Document Search**
- **TF-IDF Scoring**: Ranks documents by relevance using Term Frequency-Inverse Document Frequency algorithm
- **Multi-document Indexing**: Processes entire document collections for comprehensive search capabilities
- **Lexicographic Tie-breaking**: Consistent results when multiple documents have identical scores

### **Bigram-based Word Prediction**
- **Auto-completion**: Suggests the most probable next word based on bigram analysis
- **Probability Calculation**: Uses conditional probability P(W₂|W₁) = C(W₁,W₂)/C(W₁)
- **Statistical Learning**: Learns word associations from training corpus

### **Performance Optimizations**
- **Custom Hash Maps**: Implemented WordMap and FileMap classes with efficient collision handling
- **Dynamic Load Factor Management**: Automatic resizing when load factor exceeds 0.75
- **Optimized Memory Usage**: Efficient storage of word positions and document references

## Technical Architecture

### Data Structure Design
```
WordMap (HashMap)
├── Key: Word (String)
└── Value: FileMap (HashMap)
    ├── Key: Filename (String)
    └── Value: List<Integer> (Word positions)
```

### Core Components
- **WordMap.java**: Primary hash map storing word-to-document mappings
- **FileMap.java**: Secondary hash map tracking word positions within documents
- **Main.java**: Command-line interface and query processing engine

## Algorithms Implemented

### TF-IDF Calculation
```
TF(w) = count(w) / totalWords
IDF(w) = ln(totalDocuments / documentsContaining(w))
TFIDF(w) = TF(w) × IDF(w)
```

### Bigram Probability
```
P(W₂|W₁) = Count(W₁, W₂) / Count(W₁)
```

## Usage

### Command Line Interface
```bash
java Main <dataset_directory> <query_file>
```

### Query Types
1. **Document Search**: `search <word>`
2. **Bigram Prediction**: `the most probable bigram of <word>`

### Example Usage
```bash
java Main dataset2 query.txt
```

**Input (query.txt):**
```
search planet
the most probable bigram of new
search species
```

**Output:**
```
900.txt
new york
63.txt
```

## Text Preprocessing Pipeline

1. **Punctuation Removal**: Replace all punctuation marks with spaces
2. **Space Normalization**: Convert multiple consecutive spaces to single spaces
3. **Case Conversion**: Transform all text to lowercase for consistent processing
4. **Tokenization**: Split text into individual words for indexing

## Performance Characteristics

- **Time Complexity**: O(1) average case for hash map operations
- **Space Complexity**: O(n×m) where n = unique words, m = average documents per word
- **Load Factor Management**: Maintains efficiency with dynamic resizing
- **Memory Optimization**: Efficient storage of position arrays and document references

## Project Structure

```
├── Main.java              # Entry point and query processor
├── WordMap.java           # Primary hash map implementation
├── FileMap.java           # Document-specific word mapping
├── dataset/               # Sample document collection
│   ├── 0000026.txt
│   ├── 0000048.txt
│   └── 0000165.txt
└── query.txt             # Sample queries
```

## Key Learning Outcomes

- **Data Structures**: Custom hash map implementation with collision handling
- **NLP Fundamentals**: TF-IDF scoring and n-gram language modeling
- **Algorithm Optimization**: Load factor management and performance tuning
- **Software Engineering**: Clean code practices and modular design

## Sample Dataset

The system processes various document types including:
- **Academic Texts**: Research papers and educational content
- **News Articles**: Current events and journalistic content
- **Technical Documentation**: Specialized domain knowledge

## Future Enhancements

- [ ] Support for trigrams and higher n-grams
- [ ] Advanced text preprocessing (stemming, stop word removal)
- [ ] Cosine similarity for document comparison
- [ ] Web interface for interactive querying
- [ ] Machine learning integration for improved predictions

## Installation & Setup

1. **Clone Repository**
   ```bash
   git clone [repository-url]
   cd nlp-document-search
   ```

2. **Compile Java Files**
   ```bash
   javac *.java
   ```

3. **Run Application**
   ```bash
   java Main <dataset_directory> <query_file>
   ```


    
