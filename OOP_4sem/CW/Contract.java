package com.company;
import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity //данный класс связан с таблицей БД
@Table(name = "contracts") //в данной таблице хрниятся данные
public class Contract {
  @Id
  @Column(name = "`id`")
  @GeneratedValue(strategy = GenerationType.IDENTITY)//создание новой записи по айди и инкрементац. данных
  private int id;
    @ManyToOne
    @JoinColumn(name = "`client_id`")
    private   PrimCont client;
  //private  int client_id;
    @ManyToMany
    @JoinTable(name = "contracts_equipment",
            joinColumns = {@JoinColumn(name="contract_id")},
            inverseJoinColumns = {@JoinColumn(name ="equip_id")}
    )
  private List<Equipment> equip = null;
  @Column(name = "`date`")
  private   Date date = new Date();//1.04.2021  Calendar calendar = new GregorianCalendar(2017, 0 , 25);
    @Column(name = "`sign_date`")
    private Date signDate = new Date();
  @Column(name = "`state`")
  private   int state;
  //private   String Type_of_Contract;
  @Column(name = "`contract_price`")
  private   double contract_Prise;
//===========================================
    //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY");

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PrimCont getClient() {
        return client;
    }

    public void setClient(PrimCont Client) {
        client = Client;
    }

    public List<Equipment> getEquip() {
        return equip;
    }

    public void setEquip(List<Equipment> equip) {
        this.equip = equip;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getSignDate() { return signDate; }

    public void setSignDate(Date signDate) { this.signDate = signDate; }


    public boolean isDone(){
        return state > 0;
    }
    public boolean isOverdue(){
        return state < 0;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getContract_Prise() {
        return contract_Prise;
    }

    public void setContract_Prise(double con_Prise) {
        contract_Prise = con_Prise;
    }

    public String toString() {
        return  "Контракт №" + getId();
    }

}
