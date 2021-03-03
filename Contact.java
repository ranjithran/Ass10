package Ass10;

import java.util.Comparator;
import java.util.List;

public class Contact implements Comparable<Contact> {
    private int contactID;
    private String ContactName;
    private String EmailAddress;
    private List<String> contactNumber;

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public List<String> getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(List<String> contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "Contact [ contactID=" + contactID + " ContactName=" + ContactName + ", EmailAddress=" + EmailAddress
                + ", contactNumber=" + contactNumber + "]";
    }

    @Override
    public int compareTo(Contact o) {

        return this.ContactName.compareToIgnoreCase(o.ContactName);
    }
}
