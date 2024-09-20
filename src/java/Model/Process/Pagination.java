/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Process;

/**
 *
 * @author Administrator
 */
public class Pagination {

    private Integer total_pages, total_item, previous_page, next_page, per_page, current_page;
    private boolean has_previous, has_next;

    public Pagination(Integer total_item, Integer per_page, Integer current_page) {
        this.total_item = total_item;
        this.per_page = per_page;
        this.current_page = current_page;
        
        this.total_pages = (total_item + per_page - 1) / per_page; //int n = (a + b - 1) / b;
        
        if (current_page < total_pages) {
            this.has_next = true;
            this.next_page = current_page + 1;
        }
        
        if (current_page > 1) {
            this.has_previous = true;
            this.previous_page = current_page - 1;
        }
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public Integer getPrevious_page() {
        return previous_page;
    }

    public Integer getNext_page() {
        return next_page;
    }

    public Integer getCurrent_page() {
        return current_page;
    }

    public boolean isHas_previous() {
        return has_previous;
    }

    public boolean isHas_next() {
        return has_next;
    }
    


}
