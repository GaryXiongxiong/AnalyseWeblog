import edu.duke.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LogAnalyzer{
    private ArrayList<LogEntry> records;
    public LogAnalyzer(){
        records =  new ArrayList<LogEntry>();
        readFile();
    }
    public void readFile(){
        FileResource fr = new FileResource();
        for(String line : fr.lines()){
            records.add(WebLogParser.parseEntry(line));
        }
    }
    public void printAll(){
        for (LogEntry le : records) {
            System.out.println(le);
        }
    }
    public int countUniqueIP(){
        ArrayList<String> ips = new ArrayList<String>();
        for(LogEntry le : records){
            if(!ips.contains(le.getIpAddress())){
                ips.add(le.getIpAddress());
            }
        }
        return ips.size();
    }
    public void printAllHigherThanNum(int num){
        for (LogEntry le : records) {
            if(le.getStatusCode()>num) System.out.println(le);
        }
    }
    public ArrayList<String> uniqueIPVisitsOnDay(String date){
        String[] someday = date.split(" ");
        ArrayList<String> ips = new ArrayList<String>();
        for(LogEntry le: records) {
            if(le.getAccessTime().toString().substring(4,7).contains(someday[0])&&le.getAccessTime().toString().substring(8,10).contains(someday[1])&&(!ips.contains(le.getIpAddress()))){
                ips.add(le.getIpAddress());
            }
        }
        return ips;
    }
    public int countUniqueIPsInRange(int low, int high){
        int i = 0;
        for(LogEntry le: records){
            if(le.getStatusCode()>=low&&le.getStatusCode()<=high){
                i++;
            }
        }
        return i;
    }
    public HashMap<String,Integer> iptable(){
        HashMap<String,Integer> iptable = new HashMap<String,Integer>();
        for (LogEntry le : records) {
            if(iptable.containsKey(le.getIpAddress())){
                iptable.put(le.getIpAddress(), iptable.get(le.getIpAddress())+1);
            }
            else{
                iptable.put(le.getIpAddress(), 1);
            }
        }
        return iptable;
    }
    public int mostNumberVisitsByIP(HashMap<String,Integer> iptable){
        int max = 0;
        for(String ip : iptable.keySet()){
            if(iptable.get(ip)>max) max = iptable.get(ip);
        }
        return max;
    }
    public ArrayList<String> iPsMostVisits(HashMap<String,Integer> iptable){
        ArrayList<String> ips = new ArrayList<String>();
        int max = this.mostNumberVisitsByIP(iptable);
        for(String ip : iptable.keySet()){
            if(iptable.get(ip)==max) ips.add(ip);
        }
        return ips;
    }
    public HashMap<String,ArrayList<String>> iPsForDays(){
        HashMap<String,ArrayList<String>> iPsForDays = new HashMap<String,ArrayList<String>>();
        for (LogEntry le : records) {
            String someday = le.getAccessTime().toString().substring(4,7)+ " " +le.getAccessTime().toString().substring(8,10);
            if(!iPsForDays.containsKey(someday)) iPsForDays.put(someday, new ArrayList<String>()); 
            iPsForDays.get(someday).add(le.getIpAddress());
        }
        return iPsForDays;
    }
    public String dayWithMostIPVisits(HashMap<String,ArrayList<String>> iPsForDays){
        int max = 0;
        String day = null;
        for(String someday:iPsForDays.keySet()){
            if(iPsForDays.get(someday).size()>max){
                max = iPsForDays.get(someday).size();
                day = someday;
            }
        }
        return day;
    }
    public ArrayList<String> iPsWithMostVisitsOnDay(HashMap<String,ArrayList<String>> iPsForDays, String someday){
        HashMap<String,Integer> iptable = new HashMap<String,Integer>();
        for (String ip : iPsForDays.get(someday)) {
            if(iptable.containsKey(ip)){
                iptable.put(ip, iptable.get(ip)+1);
            }
            else{
                iptable.put(ip, 1);
            }
        }
        return this.iPsMostVisits(iptable);

    }
    public static void main(String[] args) {
        LogAnalyzer la = new LogAnalyzer();
        System.out.println(la.iPsWithMostVisitsOnDay(la.iPsForDays(), "Mar 17"));;
    }
}