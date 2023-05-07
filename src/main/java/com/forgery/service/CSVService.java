package com.forgery.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CSVService {

    public String generateLoyaltyResults(String loyaltyFolderPath, String resultsPath) {
        try{
            System.out.println(loyaltyFolderPath);
            Map<String, Integer> patreonUsers = processPatreon(loyaltyFolderPath);
            Map<String, Integer> tribesUsers = processTribes(loyaltyFolderPath);
            System.out.println("Patreon Users: " + patreonUsers);
            System.out.println();
            System.out.println("Tribes Users: " + tribesUsers);
            return generateFinalData(patreonUsers, tribesUsers, resultsPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Something fucked up.";
        }
    };

    public Map<String, Integer> processTribes(String loyaltyPath) throws Exception {
        File folder = new File(loyaltyPath + "/tribes");
        File[] listOfFiles = folder.listFiles();
        List<String> unfilteredTribesEmails = new ArrayList<>();
        for(int i = 0; i < listOfFiles.length; i++){
            FileReader fileReader = new FileReader(listOfFiles[i]);
            CSVReader csvReader = new CSVReader(fileReader);
            List<String> tribesEmailsforFile = new ArrayList<>();
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                if(
                        !(nextRecord[4].equals("Triber Email Address"))
                                && !(nextRecord[4].equals(""))
                        && (nextRecord[2].equals("To the Printer!!"))
                ) {
                    tribesEmailsforFile.add(nextRecord[4]);
                }
            }
            unfilteredTribesEmails.addAll(tribesEmailsforFile);
        }
        Map<String, Integer> tribesMemberMap = new HashMap<>();
        for(int i = 0; i < unfilteredTribesEmails.size(); i++){
            if(tribesMemberMap.containsKey(unfilteredTribesEmails.get(i))){
                tribesMemberMap.put(
                        unfilteredTribesEmails.get(i),
                        tribesMemberMap.get(unfilteredTribesEmails.get(i)) + 1);
            } else {
                tribesMemberMap.put(
                        unfilteredTribesEmails.get(i),
                        1);
            }
        }
        return tribesMemberMap;
    };

    public Map<String, Integer> processPatreon(String loyaltyPath) throws Exception {
        File folder = new File(loyaltyPath + "/patreon");
        File[] listOfFiles = folder.listFiles();
        List<String> unfilteredPatreonEmails = new ArrayList<>();
        for(int i = 0; i < listOfFiles.length; i++){
            FileReader fileReader = new FileReader(listOfFiles[i]);
            CSVReader csvReader = new CSVReader(fileReader);
            List<String> patreonEmailsforFile = new ArrayList<>();
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                if(!(nextRecord[1].equals("Email")) && !(nextRecord[1].equals(""))) {
                    patreonEmailsforFile.add(nextRecord[1]);
                }
            }
            unfilteredPatreonEmails.addAll(patreonEmailsforFile);
        }
        Map<String, Integer> patreonMemberMap = new HashMap<>();
        for(int i = 0; i < unfilteredPatreonEmails.size(); i++){
            if(patreonMemberMap.containsKey(unfilteredPatreonEmails.get(i))){
                patreonMemberMap.put(
                        unfilteredPatreonEmails.get(i),
                        patreonMemberMap.get(unfilteredPatreonEmails.get(i)) + 1);
            } else {
                patreonMemberMap.put(
                        unfilteredPatreonEmails.get(i),
                        1);
            }
        }

        return patreonMemberMap;
    }

    public String generateFinalData(Map<String, Integer> patreonData, Map<String, Integer> tribesData, String resultsPath) throws Exception{
        List<String[]> patreonCondensed = condenseData(patreonData);
        List<String[]> tribesCondensed = condenseData(tribesData);
        List<String[]> commonUsersCondensed = findCommon(patreonData, tribesData);
        writeCSVFile(resultsPath + "/PatreonUserData", patreonCondensed);
        writeCSVFile(resultsPath+ "/TribesUserData", tribesCondensed);
        writeCSVFile(resultsPath + "/TribesAndPatreonUsers", commonUsersCondensed);
        return "We got the data!! WOO.";
    };

    public void writeCSVFile(String fileName, List<String[]> userData) throws Exception {
        File PatreonLoyaltyInfo = new File(fileName);
        CSVWriter writer = new CSVWriter(new FileWriter(PatreonLoyaltyInfo));
        writer.writeAll(userData);
        writer.close();
    }

    public List<String[]> condenseData(Map<String, Integer> data){
        List<String[]> condensedResults = new ArrayList<>();
        condensedResults.add(new String[]{"Member Email", "Loyalty Months"});
        for(Map.Entry<String, Integer> pair : data.entrySet()){
            condensedResults.add(new String[]{pair.getKey(), pair.getValue().toString()});
        }
        return condensedResults;
    }

    public List<String[]> findCommon(Map<String, Integer> patreonData, Map<String, Integer> tribesData){
        Map<String, Integer> combinedData = new HashMap<>();
        for(Map.Entry<String, Integer> pair : tribesData.entrySet()){
            boolean foundMember = patreonData.containsKey(pair.getKey());
            if(foundMember){
                combinedData.put(pair.getKey(), pair.getValue() + patreonData.get(pair.getKey()));
            }
        }
        return condenseData(combinedData);
    }
}
