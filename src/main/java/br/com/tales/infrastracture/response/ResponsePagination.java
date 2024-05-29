package br.com.tales.infrastracture.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class ResponsePagination<T>  {

    private Integer current_page;
    private Integer per_page;
    private Long total_entries;
    private List<T> entries;

    public ResponsePagination(Page<T> obj){
        current_page = obj.getPageable().getPageNumber() +1;
        per_page = obj.getContent().size();
        total_entries = obj.getTotalElements();
        entries = obj.getContent();
    }

}
