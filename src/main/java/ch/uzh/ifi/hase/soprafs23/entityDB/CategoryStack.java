package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "CATEGORYSTACK")
public class CategoryStack {

    @Id
    @GeneratedValue
    private Long categoryStackId;

    @Embedded
    private Category currentCategory;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "SELECTEDCATEGORIES", joinColumns = @JoinColumn(name = "categoryStackId"))
    @Column(name = "categoryEnum")
    @Enumerated(EnumType.STRING)
    @OrderColumn(name = "position")
    private List<CategoryEnum> selectedCategories;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "REMAININGCATEGORIES", joinColumns = @JoinColumn(name = "categoryStackId"))
    @Column(name = "categoryEnum")
    @OrderColumn(name = "position")
    private List<CategoryEnum> remainingCategories;
    private int stackIdx;

    private boolean randomizedHints;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CLOSESTCOUNTRIES", joinColumns = @JoinColumn(name = "categoryStackId"))
    @Column(name = "countryName")
    @OrderColumn(name = "position")
    private List<String> closestCountries;

    public List<String> getClosestCountries() {
        return closestCountries;
    }

    public void setClosestCountries(List<String> closestCountries) {
        this.closestCountries = closestCountries;
    }

    public boolean isRandomizedHints() {
        return randomizedHints;
    }

    public void setRandomizedHints(boolean randomizedHints) {
        this.randomizedHints = randomizedHints;
    }

    public CategoryStack() {
        selectedCategories = new ArrayList<>();
        remainingCategories = new ArrayList<>();
        currentCategory = new Category();
        stackIdx = -1;
        randomizedHints = false;

        if (!selectedCategories.isEmpty()) {
            currentCategory.setType(selectedCategories.get(0));
        }
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
        this.refillStack();
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
        remainingCategories.clear();
        remainingCategories.addAll(selectedCategories);
        stackIdx = remainingCategories.size()-1;
        if (randomizedHints) {
            Collections.shuffle(remainingCategories);
        }
    }



    public Long getCategoryStackId() {
        return categoryStackId;
    }

    public void setCategoryStackId(Long categoryStackId) {
        this.categoryStackId = categoryStackId;
    }

    public Category getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(Category currentCategory) {
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
