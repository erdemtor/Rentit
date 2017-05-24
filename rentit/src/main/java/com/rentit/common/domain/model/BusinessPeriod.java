package com.rentit.common.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Embeddable;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Embeddable
@Value
@NoArgsConstructor(force=true,access= AccessLevel.PRIVATE)
@AllArgsConstructor(staticName="of")
public class BusinessPeriod {
    LocalDate startDate;
    LocalDate endDate;

    public long numberOfWorkingDays() {
        int days = 0;
        LocalDate tempDate = startDate;
        while (!tempDate.isAfter(endDate)){
            if(!isWeekend(tempDate)){
                days++;
            }
            tempDate = tempDate.plusDays(1);
        }
        return days;
    }

    private boolean isWeekend(LocalDate date){
       return  (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY));
    }
    public boolean isIntersectingWith(BusinessPeriod other){
        return  !(this.getStartDate().isAfter(other.getEndDate()) || other.getStartDate().isAfter(this.getEndDate()));
    }
}
