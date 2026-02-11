package com.example.lms.model;

import jakarta.persistence.*;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;
    private String authorName;
    private String bookName;
    private boolean isIssued=false;
    @Column(nullable = false)
    private boolean active = true;


    public Book() {
    }


    public Book(int bookId) {
        this.bookId = bookId;
    }

    public Book(int bookId, String authorName, String bookName, boolean isIssued) {
        this.bookId = bookId;
        this.authorName = authorName;
        this.bookName = bookName;
        this.isIssued = isIssued;

    }


    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public boolean isIssued() {
        return isIssued;
    }

    public void setIssued(boolean issued) {
        this.isIssued = issued;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "book{" +
                "bookId=" + bookId +
                ", authorName='" + authorName + '\'' +
                ", bookName='" + bookName + '\'' +
                ", isIssued=" + isIssued +
                '}';
    }
}
