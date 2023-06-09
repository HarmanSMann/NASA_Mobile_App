package com.example.workday_nasamobileapp.JsonDataProcessing;

public class PaginationHelper {
    private int currentPage = 1;
    private int totalPages = 0;

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

//    public int getTotalPages() {
//        return totalPages;
//    }

    public void goToNextPage() {
        if (currentPage < totalPages) {
            currentPage++;
        }
    }

    public void goToPreviousPage() {
        if (currentPage > 1) {
            currentPage--;
        }
    }

    public boolean hasNextPage() {
        return currentPage < totalPages;
    }

    public boolean hasPreviousPage() {
        return currentPage > 1;
    }
}
