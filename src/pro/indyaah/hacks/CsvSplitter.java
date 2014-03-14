package pro.indyaah.hacks;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: anuj
 * Date: 13/3/14, 7:49 PM
 * Purpose Served :
 */

public class CsvSplitter {

    /** Takes input file name as argumenet.
     *  Input file contains space seperated values (just like command line arguments)
     *  See input.txt for example.
      * @param args
     */
    public static void main(String[] args) {
        BufferedReader br = null;
        try {
            String sCurrentLine;
            HashSet<String> keywords = new HashSet<String>();
            br = new BufferedReader(new FileReader("input.txt"));
            while ((sCurrentLine = br.readLine()) != null) {
                if (!sCurrentLine.startsWith("#")) {
                    process(sCurrentLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void process(String args) {

        HashMap<String, HashSet<String>> ruleList = new HashMap<String, HashSet<String>>() {
        };
        List<String> ruleFileNameList = new ArrayList<String>();
        String delimiter = "", prepend = "SET", outFilePrepend = "SPLIT_", fileNameToBeSplit = "";
        List<Integer> columnIndex = new ArrayList<Integer>();
        Boolean fileNamesRead = false, columnIndexRead = false, fileNameRead = false;
        BufferedReader br = null;
        Integer ruleSetCounter = 0;


        for (String argument : args.split(" ")) {
            if (argument.equalsIgnoreCase("-f")) {
                fileNameRead = true;
            } else if (argument.equalsIgnoreCase("-c")) {
                fileNamesRead = true;
            } else if (argument.equalsIgnoreCase("-d")) {
                columnIndexRead = true;
            } else if (!fileNameRead) {
                fileNameToBeSplit = argument;
            } else if (fileNameRead && !fileNamesRead) {
                ruleFileNameList.add(argument);
            } else if (fileNameRead && fileNamesRead && !columnIndexRead) {
                columnIndex.add(Integer.parseInt(argument));
            } else if (fileNamesRead && columnIndexRead) {
                delimiter = argument;
                break;
            }
        }

        if (fileNameToBeSplit.isEmpty() || delimiter.isEmpty() || (columnIndex.size() == 0) || (ruleFileNameList.size() == 0)) {
            throw new RuntimeException("Something wrong with arguments");
        }


        for (String fileName : ruleFileNameList) {
            try {
                String sCurrentLine;
                String ruleSetName = prepend + ++ruleSetCounter;
                HashSet<String> keywords = new HashSet<String>();
                br = new BufferedReader(new FileReader(fileName));
                while ((sCurrentLine = br.readLine()) != null) {
                    keywords.add(sCurrentLine.trim());
                }
                ruleList.put(ruleSetName, keywords);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        try {
            HashMap<String, PrintWriter> outputFiles = new HashMap<String, PrintWriter>();

            for (String ruleName : ruleList.keySet()) {
                outputFiles.put(ruleName, new PrintWriter(new File(outFilePrepend + ruleName + "_" + fileNameToBeSplit)));
            }

            String sCurrentLine;
            br = new BufferedReader(new FileReader(fileNameToBeSplit));
            while ((sCurrentLine = br.readLine()) != null) {
                String[] split = sCurrentLine.trim().split(delimiter);
                for (String rulename : ruleList.keySet()) {
                    HashSet<String> rules = ruleList.get(rulename);
                    for (Integer index : columnIndex) {
                        if (rules.contains(split[index - 1].trim())) {
                            outputFiles.get(rulename).write(sCurrentLine + "\n");
                        }
                    }
                }
            }
            for (String key : outputFiles.keySet()) {
                outputFiles.get(key).close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
