package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CategoryStack {

    @Id
    @GeneratedValue
    private Long categoryStackId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "categoryEnumList", joinColumns = @JoinColumn(name = "categoryStackId"))
    @Column(name = "categoryEnum")
    @Enumerated(EnumType.STRING)
    @OrderColumn(name = "position")
    private List<CategoryEnum> categoryEnumList;
    private int index;

    public CategoryStack() {
        categoryEnumList = new ArrayList<>();
        index = -1;
    }

    public void add(CategoryEnum categoryEnum) {
        categoryEnumList.add(categoryEnum);
        index++;
    }

    public void addAll(List<CategoryEnum> categoryEnumListInsert) {
        for (CategoryEnum categoryEnum : categoryEnumListInsert) {
            if (!categoryEnumList.contains(categoryEnum)) {
                this.add(categoryEnum);
            }
        }
    }

    public boolean isEmpty(){
        return categoryEnumList.isEmpty();
    }


    public CategoryEnum pop(){
        CategoryEnum categoryEnum = categoryEnumList.remove(index);
        index--;
        return categoryEnum;
    }

    public Long getCategoryStackId() {
        return categoryStackId;
    }

    public void setCategoryStackId(Long categoryStackId) {
        this.categoryStackId = categoryStackId;
    }

}
