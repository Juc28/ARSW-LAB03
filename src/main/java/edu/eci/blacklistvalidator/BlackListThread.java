package edu.eci.blacklistvalidator;

import edu.eci.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.util.LinkedList;

public class BlackListThread extends Thread{

    int start;
    int end;
    HostBlacklistsDataSourceFacade skds;
    String ipaddress;
    LinkedList<Integer> blackListOcurrences;
    int checkedListsCount = 0;
    int ocurrencesCount = 0;
    int BLACK_LIST_ALARM_COUNT=5;

    /**
     * @param start
     * @param end
     * @param skds
     * @param ipaddress
     * @param blackListOcurrences
     */
    public BlackListThread (int start, int end, HostBlacklistsDataSourceFacade skds, String ipaddress,LinkedList<Integer> blackListOcurrences) {
        this.start = start;
        this.end = end;
        this.skds = skds;
        this.ipaddress = ipaddress;
        this.blackListOcurrences = blackListOcurrences;
    }

    @Override
    public void run() {

        for (int i=start;i<end && blackListOcurrences.size()<BLACK_LIST_ALARM_COUNT;i++){
            if (skds.isInBlackListServer(i, ipaddress)){
                synchronized (blackListOcurrences){
                    blackListOcurrences.add(i);
                }
                ocurrencesCount++;
            }
            checkedListsCount++;
        }
    }

    public int getOcurrencesCount () {
        return ocurrencesCount;
    }

    public int getCheckedListsCount(){
        return checkedListsCount;
    }
    public LinkedList<Integer> getBlackListOcurrences() {
        return blackListOcurrences;
    }

}