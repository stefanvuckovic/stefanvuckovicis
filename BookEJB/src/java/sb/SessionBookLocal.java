/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sb;

import domain.Book;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Stefan
 */
@Local
public interface SessionBookLocal {
    public void getDataFromNet();

    public List<Book> pretraziKnjige(String term, int offset, int limit);

    public int countBooks(String term);
}
