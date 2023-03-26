package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CATEGORYSTACK")
public class CategoryStack {

    @Id
    @GeneratedValue
    private Long categoryStackId;

    private CategoryEnum currentCategory;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "SELECTEDCATEGORIES", joinColumns = @JoinColumn(name = "categoryStackId"))
    @Column(name = "categoryEnum")
    @Enumerated(EnumType.STRING)
    @OrderColumn(name = "position")
    private List<CategoryEnum> selectedCategories;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "REMAININGCATEGORIES", joinColumns = @JoinColumn(name = "categoryStackId"))
    @Column(name = "categoryEnum")
    @Enumerated(EnumType.STRING)
    @OrderColumn(name = "position")
    private List<CategoryEnum> remainingCategories;
    private int stackIdx;

    public CategoryStack() {
        remainingCategories = new ArrayList<>();
        selectedCategories = new ArrayList<>();
        stackIdx = -1;
    }

    public void add(CategoryEnum categoryEnum) {
        selectedCategories.add(categoryEnum);
        stackIdx++;
    }

    public void addAll(List<CategoryEnum> categoryEnumListInsert) {
        for (int i = categoryEnumListInsert.size() - 1; i >= 0; i--) {
            CategoryEnum categoryEnum = categoryEnumListInsert.get(i);
            if (!selectedCategories.contains(categoryEnum)) {
                this.add(categoryEnum);
            }
        }
        refillStack();
    }

    public boolean isEmpty(){
        return remainingCategories.isEmpty();
    }


    public CategoryEnum pop(){
        if(remainingCategories.isEmpty()){
            return null;
        }
        CategoryEnum categoryEnum = remainingCategories.remove(stackIdx);
        stackIdx--;
        return categoryEnum;
    }

    public void refillStack(){
        remainingCategories.addAll(selectedCategories);
        selectedCategories.clear();
        stackIdx = remainingCategories.size()-1;
    }



    public Long getCategoryStackId() {
        return categoryStackId;
    }

    public void setCategoryStackId(Long categoryStackId) {
        this.categoryStackId = categoryStackId;
    }

    public CategoryEnum getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(CategoryEnum currentCategory) {
        this.currentCategory = currentCategory;
    }

    public List<CategoryEnum> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(List<CategoryEnum> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public List<CategoryEnum> getRemainingCategories() {
        return remainingCategories;
    }

    public void setRemainingCategories(List<CategoryEnum> remainingCategories) {
        this.remainingCategories = remainingCategories;
    }

    public int getStackIdx() {
        return stackIdx;
    }

    public void setStackIdx(int stackIdx) {
        this.stackIdx = stackIdx;
    }
}
