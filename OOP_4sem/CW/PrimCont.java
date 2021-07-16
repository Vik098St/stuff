package com.company;
import com.company.dif.ErrorFormException;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contacts")
public class PrimCont {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//создание новой записи по айди и инкрементац. данных
    private int id;
    @OneToMany (mappedBy="client", fetch = FetchType.EAGER)
    private List<Contract> contracts;
    @Column(name = "`first_name`")
    protected String firstName;
    @Column(name = "`last_name`")
    protected String lastName;
    @Column(name = "`telephone`")
    protected long telephone;
    @Column(name = "`question`")
    private String question = "--";//вопрос по которому обращался пот. клиент
    @Column(name = "`is_client`")
    private boolean isClient;
//===========================================


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }


    public String getFirstName() { return firstName; }
    //public void setFirstName(String firstName) { this.firstName = firstName; }
    public boolean setFirstName(String firstName) throws ErrorFormException {
        boolean allLetters = firstName.chars().allMatch(Character::isLetter);

        if (!allLetters || firstName.isEmpty() || firstName.contains(" ")) {
            throw new ErrorFormException("Некорректные символы в имени.");
        } else {
            this.firstName = firstName.toLowerCase();
        }
        return true;
    }

    public String getLastName() { return lastName; }
    //public void setLastName(String lastName) { this.lastName = lastName; }
    public boolean setLastName(String secondName) throws ErrorFormException {
        boolean allLetters = secondName.chars().allMatch(Character::isLetter);

        if (!allLetters || secondName.isEmpty() || secondName.contains(" ")) {
            throw new ErrorFormException("Некорректные символы в фамилии.");
            //return false;
        } else {
            this.lastName = secondName.toLowerCase();
        }
        return true;
    }



    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }
    public  void setOneContract(Contract contr){
        if (this.contracts == null)this.contracts = new ArrayList<>();
        this.contracts.add(contr);
    }

    public long getTelephone() {
        return telephone;
    }
    //public void setTelephone(long telephone) { this.telephone = telephone; }
    public boolean setTelephone(long tpn) throws ErrorFormException {
        if (tpn < 80000000000L || tpn>89999999999L) {
            throw new ErrorFormException("Неверный формат ввода.");
        } else{
            this.telephone = tpn;
        }
        return true;
    }

    public boolean getIsClient() {
        return isClient;
    }

    public void setIsClient(boolean client) {
        isClient = client;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String q) {
        question = q;
    }

    public String toString() {
        return firstName + " " + lastName;
    }

    public int countDoneContracts(){
        int num =0;
        if(this.getIsClient() && contracts != null)for(Contract con : contracts){
            if(con.isDone())num++;
        }
        return num;
    }
}


