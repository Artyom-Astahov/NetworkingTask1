package company;


import lombok.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Salary {

    private String info;
    private List<Employee> employees;

    public int totalIncome(){
        AtomicInteger counter = new AtomicInteger();
        this.employees.forEach(obj -> counter.addAndGet(obj.getSalary()));
        return counter.get();
    }

    public int totalTax(){
        AtomicInteger counter = new AtomicInteger();
        this.employees.forEach(obj -> counter.addAndGet(obj.getTax()));
        return counter.get();
    }

    public int totalProfit(){
        return this.totalIncome() - this.totalTax();
    }

}
