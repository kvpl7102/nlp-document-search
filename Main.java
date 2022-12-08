import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    static WordMap<String, FileMap<String, List<Integer>>> wordMap;
    static FileMap<String, List<Integer>> fileMap;

    /**
     * Driver class method.
     * 
     * @param args Not used.
     * @throws Exception Throws file exception.
     */
    public static void main(String[] args) throws Exception {

        wordMap = new WordMap<>();
        fileMap = new FileMap<>();

        String dirName = args[0];
        String queryFile = args[1];

        File dir = new File(dirName);
        // File[] dirFiles = dir.listFiles();

        Path dirPath = dir.toPath();
        DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath);
        for (Path filePath : stream) {
            // File editedFile = processFile(filePath.toFile());
            addToFileMap(filePath.toFile());
            addToWordMap(filePath.toFile());
        }

        // Constructing File Map & Word Map
        // for (File file : dirFiles) {
        // File editedFile = processFile(file);
        // addToFileMap(editedFile);
        // addToWordMap(editedFile);
        // }

        BufferedReader reader = new BufferedReader(new FileReader(queryFile));
        String nextLine;

        while ((nextLine = reader.readLine()) != null) {
            String[] queryLine = nextLine.split(" ");

            if (queryLine[0].equals("search")) {
                findTFIDF(queryLine[queryLine.length - 1], dir);
            } else if (queryLine[0].equals("the")) {
                findBigram(queryLine[queryLine.length - 1], dir);
            }
        }
        reader.close();

    }

    /* -------------------------------------------------------------------------- */

    /**
     * Find the Term frequency - inverse document frequency, a score
     * reflecting the importance of a word in a document. This word
     * appears frequently in that document but has very few occurrencies
     * in other documents in the dataset.
     * 
     * @param word the word needed to find TFIDF
     * @param dir  the dataset
     * @return the most relevant document with said word in the dataset
     * @throws IOException
     * 
     */
    public static void findTFIDF(String word, File dir) throws IOException {

        MyMap<String, Double> wordFrequencies = new MyMap<>();

        Path dirPath = dir.toPath();
        DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath);

        // File[] dirFiles = dir.listFiles();
        String resultFile = "";

        for (Path filePath : stream) {
            double wordTFIDF = calculateTF(word, filePath.toFile()) * calculateIDF(word, dir);
            wordFrequencies.put(filePath.toFile().getName(), wordTFIDF);
        }

        double maxFrequencies = Collections.max(wordFrequencies.values());
        for (Map.Entry<String, Double> entry : wordFrequencies.entrySet()) {
            Map.Entry<String, Double> temp = entry;

            if (temp.getValue() == maxFrequencies) {
                if (resultFile == "") {
                    resultFile = temp.getKey();
                } else if (temp.getKey().compareTo(resultFile) < 0) {
                    resultFile = temp.getKey();
                }
            }

        }
        System.out.println(resultFile);
    }

    /**
     * Calculate the term frequency of a word in a file
     * by countOfWordInFile / totalWordsInFile
     * 
     * @param word the word needed to find TF
     * @param file the file in which the word appeared
     * @return the term frequency of said word in the file
     * @throws IOException
     * 
     */
    public static double calculateTF(String word, File file) throws IOException {
        List<String> wordsList = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String nextLine;
        while ((nextLine = reader.readLine()) != null) {
            String[] line = nextLine.split(" ");
            List<String> line_asList = Arrays.asList(line);
            wordsList.addAll(line_asList);
        }
        reader.close();

        int wordCountInFile = wordCountInFile(word, file);
        int totalWordCount = wordsList.size();

        return (double) wordCountInFile / totalWordCount;
    }

    /**
     * Calculate the inverse document frequency
     * by taking logarithm of ratio of number of
     * documents in dataset to number of documents
     * containing the word
     * 
     * @param word the word needed to find IDF
     * @param dir  the dataset
     * @return the inverse document frequency of said word in the dataset
     */
    public static double calculateIDF(String word, File dir) {

        int totalDoc = dir.listFiles().length;
        int wordCountInDoc = wordMap.get(word).values().size();

        return 1 + Math.log(totalDoc / wordCountInDoc);

    }

    // Calculate number of times a word appearing in a file
    public static int wordCountInFile(String word, File file) {
        if (fileMap.get(word + "-" + file.getName()) == null) {
            return 0;
        }
        return fileMap.get(word + "-" + file.getName()).size();
    }

    /**
     * Suggest the next most probable word appearing after
     * the provided word in the dataset
     * 
     * @param word the word needed to find most probable bigram
     * @param dir  the data set
     * @return the next most probable word of provided word
     * @throws FileNotFoundException
     * 
     */
    public static void findBigram(String word, File dir) throws Exception {
        // File[] dirFiles = dir.listFiles();

        Path dirPath = dir.toPath();
        DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath);

        List<String> bigramWords = new ArrayList<>();
        List<String> wordsList = new ArrayList<>();

        for (Path filePath : stream) {

            BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                String[] line = nextLine.split(" ");
                List<String> line_asList = Arrays.asList(line);
                wordsList.addAll(line_asList);
            }
            reader.close();
        }

        for (int i = 0; i < wordsList.size(); i++) {
            if (wordsList.get(i).equals(word) && i != wordsList.size() - 1) {
                bigramWords.add(wordsList.get(i + 1));
            }
        }

        System.out.println(word + " " + findMostOccurencies(bigramWords));
    }

    // Find the most probable bigram word from a list of bigrams
    public static String findMostOccurencies(List<String> bigramsList) {
        int maxFreq = 0;
        String mostBigramWord = "";

        for (int i = 0; i < bigramsList.size(); i++) {
            String tempWord = bigramsList.get(i);
            int count = 1;
            for (int j = i + 1; j < bigramsList.size(); j++) {
                if (tempWord.equals(bigramsList.get(j))) {
                    count++;
                }
            }
            if (maxFreq < count) {
                maxFreq = count;
                mostBigramWord = tempWord;
            }
            if (maxFreq == count && tempWord.compareTo(mostBigramWord) < 0) {
                mostBigramWord = tempWord;
            }
        }
        return mostBigramWord;
    }

    /**
     * Method constructing the Word Map data structure.
     * 
     * With each file in the dataset, Word Map is going to loop through
     * each word, store them as key and map them with its corresponding
     * Entry in File Map (List of positions in associated file).
     * 
     * 
     * @param file file in the dataset
     * @throws Exception
     * 
     */
    public static void addToWordMap(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(processFile(file)));
        List<String> wordsList = new ArrayList<>();

        String nextLine;
        while ((nextLine = reader.readLine()) != null) {
            String[] line = nextLine.split(" ");
            List<String> line_asList = Arrays.asList(line);
            wordsList.addAll(line_asList);
        }

        for (String word : wordsList) {

            if (!wordMap.containsKey(word)) {
                FileMap<String, List<Integer>> fileMapForWord = new FileMap<>();
                fileMapForWord.put(word + "-" + file.getName(), fileMap.get(word + "-" + file.getName()));
                wordMap.put(word, fileMapForWord);

            } else {
                FileMap<String, List<Integer>> fileMapForWord = wordMap.get(word);
                fileMapForWord.put(word + "-" + file.getName(), fileMap.get(word + "-" + file.getName()));
            }
        }
        reader.close();
    }

    /**
     * Method constructing the File Map data structure.
     * 
     * With each file in the dataset, File Map is going to loop through
     * each word, store them as key and map them with its List of positions
     * in the associated file.
     * 
     * @param file file in the dataset
     * @throws Exception
     * 
     */
    public static void addToFileMap(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(processFile(file)));
        List<String> wordsList = new ArrayList<>();

        String nextLine;
        while ((nextLine = reader.readLine()) != null) {
            String[] line = nextLine.split(" ");
            List<String> line_asList = Arrays.asList(line);
            wordsList.addAll(line_asList);
        }

        for (int i = 0; i < wordsList.size(); i++) {

            if (fileMap.containsKey(wordsList.get(i) + "-" + file.getName())) {
                fileMap.get(wordsList.get(i) + "-" + file.getName()).add(i);

            } else {
                ArrayList<Integer> wordPos = new ArrayList<>();
                wordPos.add(i);
                fileMap.put(wordsList.get(i) + "-" + file.getName(), wordPos);
            }
        }
        reader.close();
    }

    /**
     * Method to pre-process file in the dataset
     * 
     * @param file file in the dataset
     * @return file after removing all punctuations,
     *         replacing 2 spaces with 1 space and
     *         turning all letters into lowercase.
     * @throws Exception
     */
    public static File processFile(File file) throws Exception {

        BufferedReader reader = new BufferedReader(new FileReader(file));

        StringBuilder lines = new StringBuilder();
        String nextLine;
        while ((nextLine = reader.readLine()) != null) {
            lines.append(nextLine);
            lines.append(System.lineSeparator());
        }
        reader.close();

        String fileContents = lines.toString();

        fileContents = fileContents.replaceAll("[^A-Za-z0-9]", " ")
                .trim().replaceAll(" +", " ")
                .toLowerCase();

        FileWriter writer = new FileWriter(file);
        writer.append(fileContents);
        writer.close();

        return file;
    }

}
