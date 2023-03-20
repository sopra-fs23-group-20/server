package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Embeddable
public class CategoryStack {

    
    @ElementCollection
    @CollectionTable(name = "categoryEnumList", joinColumns = @JoinColumn(name = "index"))
    @Column(name = "categoryEnumList")
    @Enumerated(EnumType.STRING)
    private final List<CategoryEnum> categoryEnumList;
    private int index;

    public CategoryStack(){
        categoryEnumList = new ArrayList<>();
        index=0;
    }

    public void add(CategoryEnum categoryEnum){
        categoryEnumList.add(categoryEnum);
        index ++;
    }

    public void addAll(List<CategoryEnum> categoryEnumListInsert){
        for(CategoryEnum categoryEnum : categoryEnumListInsert){
            this.add(categoryEnum);
        }
    }

    public CategoryEnum pop(){
        CategoryEnum categoryEnum = categoryEnumList.remove(index);
        index--;
        return categoryEnum;
    }

}
