package Ass10;

import java.io.*;
import java.sql.*;
import java.util.*;

public class ContactService {
    List<Contact> cl = new ArrayList<Contact>();
    List<Contact> tcl = new ArrayList<Contact>();
    Connection con;

    public void addContact(Contact contact, List<Contact> contacts) {
        for (Contact contact2 : contacts) {
            if (Integer.compare(contact2.getContactID(), contact.getContactID()) == 0) {
                System.out.println("Contact already present");
                return;
            }
        }
        tcl.add(contact);
        cl.add(contact);
    }

    public void removeContact(Contact contact, List<Contact> contacts) throws ContactNotFoundException {
        boolean b = true;
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getContactID() == contact.getContactID()) {
                removeContactForDB(contact);
                contacts.remove(i);
                b = false;
            }
        }
        if (b) {
            throw new ContactNotFoundException("Cannot be removed not found");
        }
        for (int i = 0; i < tcl.size(); i++) {
            if (tcl.get(i).getContactID() == contact.getContactID()) {
                tcl.remove(i);
            }
        }
    }

    public void removeContactForDB(Contact contact) {
        try {
            PreparedStatement ps = con.prepareStatement("Delete from contact_tbl Where contactId=?;");

            ps.setString(1, contact.getContactID() + "");
            if (ps.executeUpdate() == 1) {
                System.out.println("Deleted succesfully");
            }
        } catch (Exception e) {
            System.out.println("Exception found at remove" + e);
        }
    }

    public List<Contact> searchContactByName(String name, List<Contact> contact) throws ContactNotFoundException {
        List<Contact> cl = new ArrayList<Contact>();
        boolean b=true;
        for (Contact contact2 : this.cl) {
            if(contact2.getContactName().toLowerCase().equals(name.toLowerCase()))
            {
                b=false;
                cl.add(contact2);
            }
        }
        if(b){
            throw new ContactNotFoundException("Contact not found");
        }
        return cl;
    }

    public List<Contact> SearchContactByNumber(String number, List<Contact> contact) throws ContactNotFoundException {
        List<Contact> cl = new ArrayList<Contact>();
        boolean b=true;
        for (Contact contact2 : this.cl) {
            if(contact2.getContactNumber().toString().contains(number))
            {
                b=false;
                cl.add(contact2);
            }
        }
        if(b){
            throw new ContactNotFoundException("Contact not found");
        }
        return cl;
    }

    public void addContactNumber(int contactId, String contactNo, List<Contact> contacts) {
            
        for (int i = 0; i < contacts.size(); i++) {
            if(contacts.get(i).getContactID()==contactId){
                contacts.get(i).getContactNumber().add(contactNo);
            }
        }
        for (int i = 0; i < tcl.size(); i++) {
            if(tcl.get(i).getContactID()==contactId){
                tcl.get(i).getContactNumber().add(contactNo);
            }
        }
    }

    public void sortContactsByName(List<Contact> contacts) {
        Collections.sort(contacts);
    }

    public void readContactsFromFile(String filename) {
        List<Contact> cl = new ArrayList<Contact>();

        try (BufferedReader bufferedReader = new BufferedReader(
                new FileReader(new File(ContactService.class.getResource(filename).getFile())))) {
            String s = "";
            while ((s = bufferedReader.readLine()) != null) {
                String[] l = s.split(",");
                Contact c = new Contact();
                c.setContactID(Integer.valueOf(l[0]));
                c.setContactName(l[1]);
                c.setEmailAddress(l[2]);
                List<String> z = new ArrayList<String>();
                for (int i = 3; i < l.length; i++) {
                    z.add(l[i]);
                }
                c.setContactNumber(z);
                cl.add(c);
            }
        } catch (Exception e) {

        }
        this.cl = tcl = cl;
    }

    public void serializeContactDetails(List<Contact> contacts, String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(contacts);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public List<Contact> deserializeContact(String filename) {
        List<Contact> cl = new ArrayList<Contact>();
        try {
            FileInputStream fos = new FileInputStream(filename);
            ObjectInputStream oos = new ObjectInputStream(fos);
            cl = (ArrayList) oos.readObject();
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException e) {
            
            e.printStackTrace();
        }

        return cl;
    }

    public void populateContactFromDb() {

        try {
            PreparedStatement ps = con.prepareStatement("Insert into contact_tbl values(?,?,?,?);");
            for (Contact contact : tcl) {
                ps.setString(1, contact.getContactID() + "");
                ps.setString(2, contact.getContactName());
                ps.setString(3, contact.getEmailAddress());
                ps.setString(4, contact.getContactNumber().toString().replace("[", "").replace("]", ""));
                
                ps.addBatch();
            }
            tcl.clear();
            int[] rs = ps.executeBatch();
            for (int i : rs) {
                if (i == 0) {
                    System.out.println("Something went worng");
                }

            }
        } catch (Exception e) {
            System.out.println("Exception found at Operations" + e);
        }

    }

    public Boolean addContacts(List<Contact> existingContact, Set<Contact> newContacts) {
        boolean b = true;

        return b;
    }

    public void getAllcontact() {
        List<Contact> cl = new ArrayList<Contact>();
        try {
            System.out.println("Getting all contact form Db");
            ResultSet rs = con.createStatement().executeQuery("select contactId,contactName,contactEmail,COALESCE(contactList, 0 ) as contactList from contact_tbl;");
            while (rs.next()) {
                Contact contact = new Contact();
                contact.setContactID(rs.getInt("contactId"));
                contact.setContactName(rs.getString("contactName"));
                contact.setEmailAddress(rs.getString("contactEmail"));
                String tmp = rs.getString("contactList");
                if (tmp.contains(",")) {
                    String[] l = tmp.split(",");
                        contact.setContactNumber(new ArrayList<String>(Arrays.asList(l)));
                    
                }else{
                    contact.setContactNumber(new ArrayList<String>(Arrays.asList("0")));
                }
                cl.add(contact);
            }

        } catch (Exception e) {
            System.out.println("Exception found at getAll" + e);
        }
        this.cl.clear();
        tcl.clear();
        this.cl = tcl = cl;
    }

    public ContactService() {
        try {
            con = Conn.getConn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class ContactNotFoundException extends Exception {
    public ContactNotFoundException(String message) {
        super(message);
    }
}