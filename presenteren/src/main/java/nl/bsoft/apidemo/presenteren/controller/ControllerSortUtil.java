package nl.bsoft.apidemo.presenteren.controller;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class ControllerSortUtil {

    private static Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    /*
    sortBy contains <field[,(desc|asc)>[,<field[,(desc|asc)>]
     */
    public static List<Sort.Order> getSortOrder(String[] sort) {

        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            // sort=[field, direction]
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }

        return orders;
    }

}
