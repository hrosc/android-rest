package ep.rest;

import java.io.Serializable;
import java.util.Locale;

public class Book implements Serializable {
    public int id, year;
    public String author, title, uri, description;
    public double price;

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH,
                "%s: %s, %d (%.2f EUR)",
                author, title, year, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id != book.id) return false;
        if (year != book.year) return false;
        if (Double.compare(book.price, price) != 0) return false;
        if (author != null ? !author.equals(book.author) : book.author != null) return false;
        if (title != null ? !title.equals(book.title) : book.title != null) return false;
        if (uri != null ? !uri.equals(book.uri) : book.uri != null) return false;
        return description != null ? description.equals(book.description) : book.description == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + year;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
