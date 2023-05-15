package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryStackTest {

    private CategoryStack categoryStack;

    @BeforeEach
    void setUp() {
        categoryStack = new CategoryStack();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(categoryStack.getSelectedCategories());
        assertNotNull(categoryStack.getRemainingCategories());
        assertNotNull(categoryStack.getCurrentCategory());
        assertEquals(-1, categoryStack.getStackIdx());
        assertFalse(categoryStack.isRandomizedHints());
    }

    @Test
    void testAdd() {
        categoryStack.add(CategoryEnum.POPULATION);
        assertEquals(1, categoryStack.getSelectedCategories().size());
        assertEquals(CategoryEnum.POPULATION, categoryStack.getSelectedCategories().get(0));
        assertEquals(0, categoryStack.getStackIdx());
    }

    @Test
    void testAddAll() {
        List<CategoryEnum> categories = Arrays.asList(CategoryEnum.POPULATION, CategoryEnum.OUTLINE);
        categoryStack.addAll(categories);
        assertEquals(2, categoryStack.getSelectedCategories().size());
        assertTrue(categoryStack.getSelectedCategories().containsAll(categories));
        assertEquals(2, categoryStack.getRemainingCategories().size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(categoryStack.isEmpty());
        categoryStack.add(CategoryEnum.POPULATION);
        categoryStack.refillStack();
        assertFalse(categoryStack.isEmpty());
    }

    @Test
    void testPop() {
        categoryStack.add(CategoryEnum.POPULATION);
        categoryStack.refillStack();
        CategoryEnum poppedCategory = categoryStack.pop();
        assertEquals(CategoryEnum.POPULATION, poppedCategory);
        assertEquals(-1, categoryStack.getStackIdx());
        assertTrue(categoryStack.getRemainingCategories().isEmpty());
    }

    @Test
    void testRefillStackWithoutRandomization() {
        categoryStack.setRandomizedHints(false);
        List<CategoryEnum> categories = Arrays.asList(CategoryEnum.POPULATION, CategoryEnum.OUTLINE);
        categoryStack.addAll(categories);
        categoryStack.refillStack();
        assertEquals(categoryStack.getSelectedCategories(), categoryStack.getRemainingCategories());
    }

    @Test
    void testCategoryStackId() {
        categoryStack.setCategoryStackId(1L);
        assertNotNull(categoryStack.getCategoryStackId());
        assertEquals(1L, categoryStack.getCategoryStackId());
    }

    @Test
    void testSelectedCategories() {
        List<CategoryEnum> categories = Arrays.asList(CategoryEnum.POPULATION, CategoryEnum.OUTLINE);
        categoryStack.setSelectedCategories(categories);
        assertNotNull(categoryStack.getSelectedCategories());
        assertEquals(categories, categoryStack.getSelectedCategories());
    }

    @Test
    void testRemainingCategories() {
        List<CategoryEnum> categories = Arrays.asList(CategoryEnum.POPULATION, CategoryEnum.OUTLINE);
        categoryStack.setRemainingCategories(categories);
        assertNotNull(categoryStack.getRemainingCategories());
        assertEquals(categories, categoryStack.getRemainingCategories());
    }

    @Test
    void testStackIdx() {
        categoryStack.setStackIdx(1);
        assertNotNull(categoryStack.getStackIdx());
        assertEquals(1, categoryStack.getStackIdx());
    }

    @Test
    void testSetClosestCountries() {
        List<String> closestCountries = Arrays.asList("Switzerland", "Germany");
        categoryStack.setClosestCountries(closestCountries);
        assertNotNull(categoryStack.getClosestCountries());
        assertEquals(closestCountries, categoryStack.getClosestCountries());
    }

}
