package com.company;
import com.company.dif.ErrorFormException;
import org.jetbrains.annotations.NotNull;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity //данный класс связан с таблицей БД
@Table(name = "equipment") //в данной таблице хрниятся данные
public class Equipment {
        @Id
        @Column(name = "`id`")
        @GeneratedValue(strategy = GenerationType.IDENTITY)//создание новой записи по айди и инкрементац. данных
        private int id = 0;
        @Column(name = "`name`")
        private  String name;
        @Column(name = "`type`")
        private  String type;
        @Column(name = "`prise`")
        private  double prise = 0;
        @Column(name = "`description`")
        private String description = "--";
        @ManyToMany
        @JoinTable(name = "contracts_equipment",
                joinColumns = {@JoinColumn(name="equip_id")},
                inverseJoinColumns = {@JoinColumn(name ="contract_id")}
        )
        private List<Contract> contracts;
//===========================================

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

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public String getName() {
                return name;
        }


        public double getPrise() {
                return prise;
        }


        public String getDescription() {
                return description;
        }

        public void setDescription(String descript) {
                description = descript;
        }

        public boolean setName(String equipName) throws ErrorFormException {
              //  boolean allLetters = equipName.chars().allMatch(Character::isLetter);

                if ( equipName.length()>30 || equipName.isEmpty() || equipName.contains(" ")) {
                        throw new ErrorFormException("Некорректные символы в имени.");
                } else {
                        this.name = equipName.toLowerCase();
                }
                return true;
        }

        public boolean setPrise(double prs) throws ErrorFormException{
                if(prs > 100)prise =prs;
                else  System.out.println("prise can not be less than 1000rub!");

                if ( prs < 100 || prs > 100000000) {
                        throw new ErrorFormException("Выход из возможного диапазона цен!");
                } else {
                        this.prise = prs;
                }
                return true;
        }

        public int countEntryInContracts(){
                int count = 0;
                if(contracts!=null)
                        for (Contract cont :contracts){
                             for (Equipment eq: cont.getEquip())
                                     if(eq.getId() == this.getId())count++;
                        }
                return count;
        }

        public long countPriceOfEntry(){
                long sumPrice =0;
                if(contracts!=null)
                        for (Contract cont :contracts){
                                for (Equipment eq: cont.getEquip())
                                        if(eq.getId() == this.getId())sumPrice+=this.getPrise();
                        }
                return sumPrice;
        }

        public String toString() {
                return name + " " + type + "  $"+prise;
        }

}
